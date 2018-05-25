
function doDeleteAndReload(confirmationMessage, deletionUrl, successUrl) {
    showConfirmation(
       "Confirm the delete action", confirmationMessage,
       "Delete",
       function() {
           $.ajax({
                url: deletionUrl,
                type: 'DELETE',
                success: function(result) {
                     window.location.href = successUrl;
                },
                error: showAjaxError
            });
       }
    );
}

function showConfirmation(confirmationTitle, confirmationMessage,
                          primaryButtonLabel, primaryButtonAction,
                          secondaryButtonLabel, secondaryButtonAction) {
  $("#confirmationModalLabel").html(confirmationTitle);
  $("#confirmationModalBody").html(confirmationMessage);
  if (primaryButtonLabel)   $('#confirmationModal .btn-primary').text(primaryButtonLabel);
  if (secondaryButtonLabel) $('#confirmationModal .btn-secondary').text(secondaryButtonLabel);

  $('#confirmationModal .btn-primary').click(function() {
      $('#confirmationModal').modal('hide');
      if (primaryButtonAction) primaryButtonAction();
  });

  $('#confirmationModal .btn-secondary').click(function() {
      $('#confirmationModal').modal('hide');
      if (secondaryButtonAction) secondaryButtonAction();
  });

  //show the confirmation modal
  $('#confirmationModal').modal('show');
}


function showMessage(title, message, buttonLabel, buttonAction) {
  $("#messageModalLabel").html(title);
  $("#messageModalBody").html(message);
  if (buttonLabel)   $('#messageModal .btn').text(buttonLabel);

  $('#messageModal .btn').click(function() {
      $('#messageModal').modal('hide');
      if (buttonAction) buttonAction();
  });

  //show the modal
  $('#messageModal').modal('show');
}

function showAjaxError(jqxhr) {
  var json = $.parseJSON(jqxhr.responseText);

   /* showMessage('Oops. Something went wrong',
       "<div class='jumbotron alert-danger'>"
          +"<ul>"
          +"<li>status:"+json.status+"</li>"
          +"<li>error:"+json.error+"</li>"
          +"<li>message:"+json.message+"</li>"
          +"<li>time:"+json.timestamp+"</li>"
          +"<li>path:"+json.path+"</li>"
          +"</ul>"
   	   +"</div>"
    );
    */
    showMessage('Oops. Something went wrong',
           "<div class='alert-danger table-responsive'>"
              +"<table class='table table-sm'>"
              +"<tr><td>status</td><td>"+json.status+"</td></tr>"
              +"<tr><td>error</td><td>"+json.error+"</td></tr>"
              +"<tr><td>message</td><td>"+json.message+"</td></tr>"
              +"<tr><td>timestamp</td><td>"+json.timestamp+"</td></tr>"
              +"<tr><td>path</td><td>"+json.path+"</td></tr>"
              +"</table>"
       	   +"</div>"
        );
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