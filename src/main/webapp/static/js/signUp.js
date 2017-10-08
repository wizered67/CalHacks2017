$(function(){
	$("#signUp").submit(function(e) {

            var url = "/rest/registration"; // the script where you handle the form input.

            var d = $('#signUp').serializeArray()
                .reduce(function(a, x) { a[x.name] = x.value; return a; }, {});
            d = JSON.stringify(d);
            $.ajax({
                   type: "POST",
                   url: url,
                   data: d, // serializes the form's elements.
                   contentType: "application/json; charset=utf-8",
                   dataType: "json",
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
