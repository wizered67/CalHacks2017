function getCookie(cname) {
    var name = cname + "=";
    var decodedCookie = decodeURIComponent(document.cookie);
    var ca = decodedCookie.split(';');
    for(var i = 0; i <ca.length; i++) {
        var c = ca[i];
        while (c.charAt(0) == ' ') {
            c = c.substring(1);
        }
        if (c.indexOf(name) == 0) {
            return c.substring(name.length, c.length);
        }
    }
    return "";
}

$(function(){
    $('select').change(function(){
        var value = $(this).val();
         var d = $('#addMeal').serializeArray()
                        .reduce(function(a, x) { a[x.name] = x.value; return a; }, {});
         d["NBDno"] = value;
         d = JSON.stringify(d);

         $.ajax({
               type: "POST",
               url: "/rest/users/" + getCookie("user_id") + "/food/",
               data: d,
               contentType: "application/json; charset=utf-8",
               headers: {
                   "Authorization" : "Bearer " +  getCookie("access_token")
               },
               dataType: "json",
               success: function(data)
               {
                  alert("Data received.");
               },
               failure: function(data)
               {
                   alert("Unable to login. Try again.");
               }
             });
            $("#addMeal").trigger('reset');
            $('#select').empty();
            $('#search').value = '';
    });
    $('#search').on('keypress', function(e) {

        var code = e.keyCode || e.which;
        if(code==13){
            var search = $('#search').val();
            $.ajax({
               type: "GET",
               url: "/rest/users/" + getCookie("user_id") + "/search/",
               data: "search=" + search,
               contentType: "application/json; charset=utf-8",
               headers: {
                   "Authorization" : "Bearer " +  getCookie("access_token")
               },
               dataType: "json",
               success: function(data)
               {
                  $('#select').empty();
                  $.each(data, function (i, item) {
                      $('#select').append($('<option>', {
                          value: item.id,
                          text : item.name
                      }));
                  });
               },
               failure: function(data)
               {
                   alert("Unable to login. Try again.");
               }
             });
        }
    });
});