define([
	'backbone',
	'underscore',
	'libs/utility',
	'libs/status_code',
	'toastr'
], function(
	Backbone,
	_,
	Utility,
	Status_Code,
	toastr
) {
	return Backbone.View.extend({
		el: 'body',
		initialize: function(options) {
			this.image_template = _.template($('#tpl-image-entry').html());
			this.video_template = _.template($('#tpl-video-entry').html());

			this.listenTo(this.model, 'contentfetched', this.showContentForm);
		},
		events: {
			'click #btn-cancel': function() {
				window.location.reload();
			},
			'click #btn-save': 'saveContent',
			'submit form': function(e) {
				e.preventDefault();
				return false;
			},

			'click #btn-upload-thumbnail': 'uploadThumbnail',
			'click #btn-upload-preview': 'uploadPreview'
		},

		showContentForm: function(mode) {
			if(mode === 'info') {
				this._showContentInfoForm();
			} else if(mode === 'edit') {
				this._showContentEditForm();
			} else if(mode === 'create') {
				this._showContentCreateForm();
			}
		},
		saveContent: function() {
			var data = {
					type: this.$el.find('#form-type').val(),
					title: this.$el.find('#form-title').val(),
					category: this.$el.find('#form-category').val(),
					director: this.$el.find('#form-director').val(),
					actors: this.$el.find('#form-actors').val(),
					content_provider: this.$el.find('#form-content-provider').val(),
					age_restriction: this.$el.find('#form-age_restriction').val(),
					description: this.$el.find('#form-description').val(),
					file_format: this.$el.find('#form-file-format').val(),
					duration: this.$el.find('#form-duration').val(),
					bitrate: this.$el.find('#form-bitrate').val(),
					url: this.$el.find('#form-url').val()
				};

			// TODO validate

			this.model.set(data);
			this.model.save(null, {
				success: function() {
					window.location.href = 'contents_mgmt.html#level/success/message/' + encodeURIComponent('user created.');
				},
				statusCode: Status_Code
			});
		},
		uploadThumbnail: function() {
			var $file = this.$el.find('input#form-upload-thumbnail');

			if($file[0].files.length > 0) {
				var data = new FormData(),
					file = $file[0].files[0];

				data.append('contentid', this.model.id);
				data.append('type', 'thumbnail');
				data.append('uploadfile', file, file.name);

				Backbone.ajax({
					method: 'POST',
					url: '/dashbd/file.do',
					data: data,
					cache: false,
					contentType: false,
					processData: false,
					dataType: 'json',
					success: function(response) {
						this.$el.find('#thumbnails').append(this.image_template(response.result));
					}.bind(this)
				});
			}
		},
		uploadPreview: function() {
			var $file = this.$el.find('input#form-upload-preview');

			if($file[0].files.length > 0) {
				var data = new FormData(),
					file = $file[0].files[0];

				data.append('contentid', this.model.id);
				data.append('type', 'preview');
				data.append('uploadfile', file, file.name);

				Backbone.ajax({
					method: 'POST',
					url: '/dashbd/file.do',
					data: data,
					cache: false,
					contentType: false,
					processData: false,
					dataType: 'json',
					success: function(response) {
						this.$el.find('#previews').append(this.video_template(response.result));
					}.bind(this)
				});
			}
		},
		_showThumbnails: function() {
			var $thumbnails = this.$el.find('#thumbnails');

			$thumbnails.empty();

			Backbone.ajax({
				url: '/dashbd/api/contents_images.do',
				data: {
					id: this.model.id,
					type: 'thumbnail',
					request_type: 'read'
				},
				dataType: 'json',
				success: function(response) {
					_.each(response.result, function(thumbnail) {
						$thumbnails.append(this.image_template(thumbnail));
					}, this);
				}.bind(this)
			});
		},
		_showPreviews: function() {
			var $previews = this.$el.find('#previews');

			$previews.empty();

			Backbone.ajax({
				url: '/dashbd/api/contents_images.do',
				data: {
					id: this.model.id,
					type: 'preview',
					request_type: 'read'
				},
				dataType: 'json',
				success: function(response) {
					_.each(response.result, function(preview) {
						$previews.append(this.video_template(preview));
					}, this);
				}.bind(this)
			});
		},
		_showContentEditForm: function() {
			// fetched value
			_.each(this.model.attributes, function(value, key) {
				this.$el.find('#' + this.model.convert_attribute_name_to(key)).val(value);
			}, this);

			// "User Add" to name으로
			this.$el.find('div.ibox-title > h5').text('Edit ' + this.model.get('title'));

			// thumbnail preview 보여주기
			this._showThumbnails();
			this._showPreviews();
		},
		_showContentCreateForm: function() {
			// create form에서는 다음의 항목을 숨긴다.
			// 1. id
			// 2. section-thumbnails, section-previews
			this.$el.find('#form-id').parents('div.form-group').hide();
			this.$el.find('.section-thumbnails').hide();
			this.$el.find('.section-previews').hide();
		},
		_showContentInfoForm: function() {
			// fetched value
			_.each(this.model.attributes, function(value, key) {
				this.$el.find('#' + this.model.convert_attribute_name_to(key)).val(value);
			}, this);

			// "User Add" to name으로
			this.$el.find('div.ibox-title > h5').text(this.model.get('title'));

			// info form에서는 다음의 항목을 숨긴다.
			// 1. cancel, save 버튼
			this.$el.find('div.action-btn-group').hide();
			this.$el.find('.hide-on-info-mode').hide();

			// thumbnail preview 보여주기
			this._showThumbnails();
			this._showPreviews();
		}
	});
});
