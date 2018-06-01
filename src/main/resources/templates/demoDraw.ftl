<#import "/spring.ftl" as spring />
<#include "/tiles/header.ftl">
 <link href="/css/form_validation.css" rel="stylesheet">

<div class="container mt-3">
    <div class="py-5 text-center">
        <h2>Draw of ${demo.title}</h2>
    </div>

    <div class="row pb-3">
      <div class="col-md-6 text-left">
          <a role="button" class="btn btn-secondary" href="/demos">&laquo; Go back</a>
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
                  </td>

                </tr>
              </#list>
            </tbody>
           </table>
        </div>
    </div>

</div>


<script type="text/javascript">
   //TODO:!!!
</script>


<#include "tiles/footer.ftl">