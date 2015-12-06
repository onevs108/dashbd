require([
	'libs/utility',
	'backbone',

	'models/model.content',

	'views/view.content_form.main',

	'apps/router.content_form'
], function(
	Utility,
	Backbone,

	Model_Content,

	View_Content_Form_Main,

	Router_Content_Form
) {
	$(document).ready(function() {
		var content = new Model_Content();
		var content_form = new View_Content_Form_Main({
				model: content
			});

		// router는 맨 마지막에 초기화한다.
		var router = Utility.Context.set('router', new Router_Content_Form({
				content: content
			}));

		// backbone history 초기화
		Backbone.history.start();
	});
});
