<#include "/tiles/header.ftl">

<div class="container mt-3">

    <div class="row">
      <div class="col-md-6 text-left">
         <h2>Spring demo draws</h2>
      </div>
      <div class="col-md-6 text-right">
         <a role="button" class="btn btn-secondary btn-success" href="/demos/form">Create</a>
      </div>
    </div>

    <div class="row">
        <div class="table-responsive">
          <table class="table table-striped table-sm">
            <thead>
              <tr>
                <th>#</th>
                <th>Title</th>
                <th>Date</th>
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
                  <td style="width:15%;text-align:right">
                   <a role="button" class="btn btn-warning" href="/demos/form/${demo.id}">Edit</a> &nbsp;
                   <button class="btn btn-danger"  onclick="deleteDemo(${demo.id})">Delete</button>
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