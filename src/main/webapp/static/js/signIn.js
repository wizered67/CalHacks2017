$(function(){
  $("#login").submit(function(e) {

        var url = "/rest/authentication"; // the script where you handle the form input.

        $.ajax({
               type: "POST",
               url: url,
               data: $("#login").serialize(), // serializes the form's elements.
               success: function(data)
               {
                   document.cookie='access_token=' + data.token;
                   document.cookie='user_id=' + data.id;
                   window.location.href = "userHome.html";
               },
               failure: function(data)
               {
                   alert("Unable to login. Try again.");
               }
             });

        e.preventDefault(); // avoid to execute the actual submit of the form.
 });
});
