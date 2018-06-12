<#import "/spring.ftl" as spring />
<#include "/tiles/header.ftl">
 <link href="/css/form_validation.css" rel="stylesheet">

<div class="container mt-3">
    <div class="py-3 text-center">
        <h2>Draw of ${demo.title}</h2>
    </div>

    <div class="row pb-3">
      <div class="col-md-6 text-left">
          <a role="button" class="btn btn-secondary" href="/demos">&laquo; Go back</a>
      </div>
      <div class="col-md-6 text-right">
          <#if demo.drawStatus.name() == "IN_PROGRESS">
            <button class="btn btn-primary" type="button" onclick="drawAllTasks(${demo.id})">Define ALL winners</button>
          </#if>
           &nbsp;<button class="btn btn-warning" type="button" onclick="resetDraw(${demo.id})" >Reset results</button> &nbsp;
          <#if demo.drawStatus.name() == "FINISHED">
            <button class="btn btn-info" type="button" onclick="sendNotifications(${demo.id})">Notify about results</button>
          </#if>
      </div>
    </div>


    <div class="row" style="min-height: 500px;">
        <div class="table-responsive">
          <table class="table table-striped table-sm">
            <thead>
              <tr>
                <th>#</th>
                <th>Key</th>
                <th>Title</th>
                <th>Owner</th>
                <th/>
                <th>Winner</th>
              </tr>
            </thead>
            <tbody>
              <#list demo.tasks?sort_by("id") as task>
                <tr>
                  <td>${task.id}</td>
                  <td>
                       <#if task.link?has_content>
                         <a target="_blank" href="${task.link}">${task.key}</a>
                       <#else>
                          ${task.key}
                       </#if>
                  </td>
                  <td>${task.title}</td>
                  <td>${task.owner.name} ${task.owner.surname}</td>

                  <td style="width:25%;text-align:right;white-space:nowrap">

                    <#if task.winner?has_content>
                    <#else>
                        <button class="btn btn-primary" type="button" onclick="drawTask(${demo.id}, ${task.id})" >Define winner</button>
                    </#if>
                  </td>

                  <td>
                       <#if task.winner?has_content>
                         ${task.winner.name} ${task.winner.surname}
                       </#if>
                  </td>
                </tr>
              </#list>

            </tbody>
           </table>
        </div>
    </div>

</div>


<script type="text/javascript">

function drawAllTasks(demoId) {
        showConfirmation(
           "Confirm the action",
           'Do you really want define the winner for all the tasks?',
           "Yes",
           function() {
               showProcessing();
               $.ajax({
                    url: '/demos/'+demoId+'/draw/tasks',
                    type: 'POST',
                    success: function(result) {
                         window.location.href = '/demos/'+demoId+'/draw';
                    },
                    error: showAjaxError
                });
           }
        );
    }

function drawTask(demoId, taskId) {
       showProcessing();
       $.ajax({
            url: '/demos/'+demoId+'/draw/tasks/'+taskId,
            type: 'POST',
            success: function(result) {
                 window.location.href = '/demos/'+demoId+'/draw';
            },
            error: showAjaxError
        });
}


function resetDraw(demoId, taskId) {
        showConfirmation(
           "Confirm the action",
           'Do you really want to reset all the results and restart the draw of the demo?',
           "Yes",
           function() {
               showProcessing();
               $.ajax({
                    url: '/demos/'+demoId+'/draw/tasks',
                    type: 'DELETE',
                    success: function(result) {
                         window.location.href = '/demos/'+demoId+'/draw';
                    },
                    error: showAjaxError
                });
           }
        );
}
</script>


<#include "/tiles/footer.ftl">