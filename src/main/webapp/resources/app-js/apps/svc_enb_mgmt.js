$(document).ready(function()
{
    $('#operator').change(function(){
        //alert( $('#operator option:selected').val() );
        getServiceAreaBmSc(1, $('#operator option:selected').val());
    });
    
    $('#operator_down').change(function(){
        //alert( $('#operator option:selected').val() );
        getServiceAreaBmSc(1, $('#operator option:selected').val());
    });
    
    $('#bmsc_down').change(function(){
        //alert( $('#bmsc option:selected').val() );
        //drawServiceAreaByBmSc($('#bmsc option:selected').val());
    });
    
    $('.demo1').click(function(){
        swal({
            title: "Sorry !",
            text: "Please Select BMSC first."
        });
    });
    
    // Page-Level Scripts
    //$('.footable').footable();
    //$('.footable2').footable();
});