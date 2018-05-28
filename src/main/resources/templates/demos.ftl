<#include "/tiles/header.ftl">

<div class="container mt-3">

    <div class="row">
      <div class="col-md-6 text-left">
         <h2>Spring demo draws</h2>
      </div>
      <div class="col-md-6 text-right">
         <a role="button" class="btn btn-primary" href="/demos/form">Create</a>
      </div>
    </div>

    <div class="row" style="min-height: 500px;">
        <div class="table-responsive">
          <table class="table table-striped table-sm">
            <thead>
              <tr>
                <th>#</th>
                <th>Title</th>
                <th>Date</th>
                <th>Draw status</th>
                <th/>
              </tr>
            </thead>
            <tbody>
              <#list demos as demo>
                <tr>
                  <td>${demo.id}</td>
                  <td>
                     <#if demo.link?has_content>
                       <a target="_blank" href="${demo.link}">${demo.title}</a>
                     <#else>
                        ${demo.title}
                     </#if>
                  </td>
                  <td>${demo.plannedDate}</td>
                  <td>${demo.drawStatus.description}</td>
                  <td style="width:25%;text-align:right;white-space:nowrap">
                        <div class="btn-group">
                          <button type="button" class="btn btn-secomdary dropdown-toggle" data-toggle="dropdown"
                                  aria-haspopup="true" aria-expanded="false">
                            Actions..
                          </button>
                          <div class="dropdown-menu">
                              <#if demo.drawStatus.name() == "PREPARATION">
                                 <a class="dropdown-item" href="/demos/${demo.id}/form">Edit</a>
                                 <a class="dropdown-item"  href="/demos/${demo.id}/formtask">Define tasks</a>
                                 <div class="dropdown-divider"></div>
                                 <button class="dropdown-item" type="button" onclick="deleteDemo(${demo.id})">Delete</button>
                                 <div class="dropdown-divider"></div>
                              </#if>
                              <#if demo.drawStatus.name() != "FINISHED">
                                 <a class="dropdown-item" href="#">Play</a>
                              </#if>
                          </div>
                        </div>
                  </td>
                </tr>
              </#list>
            </tbody>
           </table>
        </div>
    </div>

</div> <!-- /container -->


<script type="text/javascript">
    function deleteDemo(demoId) {
      doDeleteAndReload(
        'Do you really want to delete the selected Spring demo draw (all the tasks and results will be removed as well)?',
        "/demos/"+demoId,
        '/demos'
      );
    }
</script>

<#include "tiles/footer.ftl">