require([
	'libs/utility',
	'backbone',

	'models/collection.contents',
	'models/model.pagination.content',

	'views/view.content.main',

	'apps/router.content'
], function(
	Utility,
	Backbone,

	Collection_Content,
	Model_Pagination_Content,

	View_Content_Main,

	Router_Content
) {
	$(document).ready(function() {
		var content = new Collection_Content();
		var pagination_content = new Model_Pagination_Content();
		var content_main = new View_Content_Main({
				collection: content,
				pagination_content: pagination_content
			});

		// router는 맨 마지막에 초기화한다.
		var router = Utility.Context.set('router', new Router_Content({
				content: content,
				pagination_content: pagination_content
			}));

		// backbone history 초기화
		Backbone.history.start();
	});
});
