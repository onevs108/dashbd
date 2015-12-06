require([
	'libs/utility',
	'backbone',

	'models/model.service_area',

	'views/view.service_area_form.main'
], function(
	Utility,
	Backbone,

	Model_Service_Area,

	View_Service_Area_Form_Main,

	Router_Service_Area_Form
) {
	$(document).ready(function() {
		var service_area = new Model_Service_Area();
		var service_area_form = new View_Service_Area_Form_Main({
				model: service_area
			});
	});
});
