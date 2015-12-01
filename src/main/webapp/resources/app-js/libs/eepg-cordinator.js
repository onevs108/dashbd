require([
	'underscore',
	'asynclib'
], function(
	_,
	Async
) {
	var Item = function(model) {
			this.model = model;
		};

	_.extend(Item.prototype, {
		getModel: function() {
			return this.model;
		},
		setModel: function(model) {
			this.model = model;
		}
	});

	var Lane = function() {
			this.items = [];
		};

	_.extend(Lane.prototype, {
		append: function(data) {
			this.items.push(new Item(data));
		},
		getItem: function(index) {
			return this.items[index];
		},
		getItems: function() {
			return this.items;
		},
		getItemCount: function() {
			return this.items.length;
		},
		removeItem: function(index) {
			this.items = _.without(this.items, this.getItem(index));
		}
	});

	var BandwidthTable = function() {
			this.table = [];
		};

	_.extend(BandwidthTable.prototype, {
		_addRawSection: function(scheduled_start_time, scheduled_end_time, bandwidth) {
			this.table.push({
				scheduled_start_time: scheduled_start_time,
				scheduled_end_time: scheduled_end_time,
				bandwidth: bandwidth
			});
		},
		getBlock: function(index) {
			return this.table[index];
		},
		addSection: function(scheduled_start_time, scheduled_end_time, bandwidth) {
			// split matched scheduled_start_time block
			var start_block_index = this.getMatchedTimestampBlockIndex(scheduled_start_time),
				end_block_index = this.getMatchedTimestampBlockIndex(scheduled_end_time);

			if(start_block_index < 0 && end_block_index < 0) {
				this._addRawSection(scheduled_start_time, scheduled_end_time, bandwidth);
			} else if(start_block_index >= 0 && end_block_index < 0) {
				this.splitSection(start_block_index, scheduled_start_time);
				this._addRawSection(this.getBlock(start_block_index).scheduled_end_time, scheduled_end_time, bandwidth);
			} else if(start_block_index < 0 && end_block_index >= 0) {
				this.splitSection(end_block_index, scheduled_end_time);
				this._addRawSection(scheduled_start_time, this.getBlock(end_block_index).scheduled_start_time, bandwidth);
			} else {
				this.splitSection(start_block_index, scheduled_start_time);
				this.splitSection(end_block_index, scheduled_end_time);

				_.each(this.getIncludedTimestampBlockIndexes(scheduled_start_time, scheduled_end_time), function(index) {
					this.table[index].bandwidth += bandwidth;
				}, this);
			}
		},
		splitSection: function(index, time) {
			var section = this.table[index];

			if(section) {
				this.table[index] = [{
					scheduled_start_time: section.scheduled_start_time,
					scheduled_end_time: time,
					bandwidth: section.bandwidth
				}, {
					scheduled_start_time: time,
					scheduled_end_time: section.scheduled_end_time,
					bandwidth: section.bandwidth
				}];

				this.table = _.flatten(this.table);
			}
		},
		getMatchedTimestampBlockIndex: function(timestamp) {
			return _.findIndex(this.table, function(block) {
				return block.scheduled_start_time < timestamp && block.scheduled_end_time > timestamp;
			}, this);
		},
		getIncludedTimestampBlockIndexes: function(scheduled_start_time, scheduled_end_time) {
			return _.chain(this.table)
				.filter(function(block) {
					return block.scheduled_start_time >= scheduled_start_time && block.scheduled_end_time <= scheduled_end_time;
				})
				.map(function(block, i) {
					return i;
				})
				.value();
		},
		getMaxBandwidthBetweenStartTimeAndEndTime: function(scheduled_start_time, scheduled_end_time) {
			if(this.table.length > 0) {
				return _.chain(this.table)
					.filter(function(block) {
						return block.scheduled_start_time >= scheduled_start_time && block.scheduled_end_time <= scheduled_end_time;
					})
					.map(function(block) {
						return block.bandwidth;
					})
					.max()
					.value();
			} else {
				return 0;
			}
		},
		toString: function() {
			return _.chain(this.table)
				.map(function(block) {
					return 's: ' + block.scheduled_start_time + ', e: ' + block.scheduled_end_time + ', b: ' + block.bandwidth;
				})
				.value()
				.join('\n');
		}
	});

	var EEPG_Cordinator = function(options) {
			this.lanes = [];
			this.options = _.default({
				max_bandwidth: 0
			}, options || {});
		};

	_.extend(EEPG_Cordinator.prototype, {
		createLane: function() {
			this.lanes.push(new Lane());
		},
		getLane: function(index) {
			return this.lanes[index];
		},
		getLanes: function() {
			return this.lanes;
		},
		getLaneCount: function() {
			return this.lanes.length;
		},
		removeLane: function(index) {
			this.lanes = _.without(this.lanes, this.getLane(index));
		},
		addItemToBandwidthTable: function(scheduled_start_time, scheduled_end_time, bandwidth) {

		}
	});

	return EEPG_Cordinator;
});
