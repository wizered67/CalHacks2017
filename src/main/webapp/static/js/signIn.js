alert("test");
$(function() {
	$( "#login" ).submit(function( event ) {
	   alert( "Handler for .submit() called." );
	   event.preventDefault();
	});
});
