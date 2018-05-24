function doDeleteAndReload(confirmationMessage, deletionUrl, successUrl) {
    if( confirm(confirmationMessage)) {
         $.ajax({
             url: deletionUrl,
             type: 'DELETE',
             success: function(result) {
                  window.location.href = successUrl;
             },
             error: function (result) {
               alert(result);
              }
         });
       }
}

/*
function displaySnippet(snippetUrl) {
     $.ajax({
         url: snippetUrl,
         type: 'GET',
         success: function(result) {
             $( "#mainContainer" ).replaceWith(result);
         }
     });
}

function displayTeamMembers() {
   displaySnippet('/teamembers');
}

function displayDraws() {
   displaySnippet('/draws');
}

function deleteMember(memberId)
{
   if( confirm('Do you really want to delete the selected team member?')) {
     $.ajax({
         url: "/teamembers/"+memberId,
         type: 'DELETE',
         success: function(result) {
              displayTeamMembers();
              //window.location.href = '/teamembers';
         }
     });
   }
}

function transformToAjaxForm(formId) {
    var form = $('#'+formId);

    form.submit(function (e) {
        e.preventDefault();
        e.stopPropagation();

        $.ajax({
            type: form.attr('method'),
            url: form.attr('action'),
            data: form.serialize(),
            success: function (data) {
                $( "#mainContainer" ).html(data);
            },
            error: function (data) {
                $( "#mainContainer" ).html(data);
            },
        });
    });
}

function submitForm(formId) {
    $('#'+formId).trigger('submit');
}

*/