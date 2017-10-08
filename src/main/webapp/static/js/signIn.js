$("#login").submit(function(e) {

    var url = "/rest/authentication"; // the script where you handle the form input.

    $.ajax({
           type: "POST",
           url: url,
           data: $("#login").serialize(), // serializes the form's elements.
           success: function(data)
           {
               alert(data); // show response from the php script.
           }
         });

    e.preventDefault(); // avoid to execute the actual submit of the form.
});