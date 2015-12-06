define([
	'backbone',
	'underscore',
	'libs/utility',

	'views/view.user.user.entry'
], function(
	Backbone,
	_,
	Utility,

	View_User_User_Entry
) {
	return Backbone.View.extend({
		el: 'table#user-table',
		initialize: function(options) {
			this.operator = options.operator;
			this.$tbody = this.$el.find('tbody');

			this.listenTo(this.collection, 'reset', this.render);

			// main view가 초기화될때 operator를 한 번 fetch하고
			// fetch가 완료되면 this.collection의 모든 model의 operator_id를 int에서 대응하는
			// 문자열로 변경함
			this.listenTo(this.operator, 'reset', this.operatorFetched);
		},
		events: {

		},
		render: function() {
			// 기존 렌더링된 entry들을 삭제
			this.$tbody.empty();

			this.collection.each(function(model) {
				this.$el.append((new View_User_User_Entry({
					model: model
				})).el);
			}, this);

			this.operator.fetch({reset: true});

			return this;
		},

		operatorFetched: function() {
			var operator_map = _.chain(this.operator.models)
				.map(function(model) {
					return {
						id: model.id,
						name: model.get('name')
					};
				})
				.value();

			this.collection.each(function(model) {
				var operator_id = model.get('operator_id');
				model.set('operator_id', _.find(operator_map, function(operator) {
					return operator.id === operator_id * 1;
				}).name);
			});
		}
	});
});
