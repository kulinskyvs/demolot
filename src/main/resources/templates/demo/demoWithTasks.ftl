<#import "/spring.ftl" as spring />
<#include "/tiles/header.ftl">
 <link href="/css/form_validation.css" rel="stylesheet">

<div class="container mt-3">
    <div class="py-3 text-center">
        <h2>${demo.title} tasks</h2>
        <p class="lead">Define the lists of tasks to be shown during the ${demo.title}
          <#if demo.summary?has_content>(${demo.summary})</#if>
        </p>
    </div>

    <div class="row pb-3">
      <div class="col-md-6 text-left">
          <a role="button" class="btn btn-secondary" href="/demos">&laquo; Go back</a>
      </div>
      <#if demo.drawStatus.name() == "PREPARATION">
          <div class="col-md-6 text-right">
              <a role="button" class="btn btn-primary" href="/demos/${demo.id}/tasks/form">Create</a> &nbsp;
          </div>
      </#if>

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
              </tr>
            </thead>
            <tbody>
              <#list demo.tasks as task>
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
                      <#if demo.drawStatus.name() == "PREPARATION">
                        <div class="btn-group">
                          <button type="button" class="btn btn-secondary dropdown-toggle" data-toggle="dropdown"
                                  aria-haspopup="true" aria-expanded="false">
                            Actions..
                          </button>
                          <div class="dropdown-menu">
                            <a class="dropdown-item" href="/demos/${demo.id}/tasks/${task.id}/form">Edit</a>
                            <button class="dropdown-item" type="button" onclick="deleteTask(${demo.id}, ${task.id})">Delete</button>
                          </div>
                        </div>
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
    function deleteTask(demoId, taskId) {
       doDeleteAndReload(
        'Do you really want to delete the selected task from this demo?',
        "/demos/"+demoId+"/tasks/"+taskId,
        "/demos/"+demoId+"/formtask"
      );
    }
</script>


<#include "/tiles/footer.ftl">