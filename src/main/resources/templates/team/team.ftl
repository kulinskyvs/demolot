<#include "/tiles/header.ftl">

<div class="container mt-3">

    <div class="row">
      <div class="col-md-6 text-left">
         <h2>Team members</h2>
      </div>
      <div class="col-md-6 text-right">
         <a role="button" class="btn btn-primary" href="/teamembers/form">Create</a>
      </div>
    </div>

    <div class="row" style="min-height: 500px;">
        <div class="table-responsive">
          <table class="table table-striped table-sm table-hover">
            <thead>
              <tr>
                <th>#</th>
                <th>Name</th>
                <th>Surname</th>
                <th>Email</th>
                <th>Active?</th>
                <th/>
              </tr>
            </thead>
            <tbody>
              <#list members as member>
                <tr>
                  <td>${member.id}</td>
                  <td>${member.name}</td>
                  <td>${member.surname}</td>
                  <td>${member.email}</td>
                  <td>${member.active?string('yes', 'no')}</td>
                  <td style="width:25%;text-align:right;white-space:nowrap">
                        <div class="btn-group">
                          <button type="button" class="btn btn-secomdary dropdown-toggle" data-toggle="dropdown"
                                  aria-haspopup="true" aria-expanded="false">
                            Actions..
                          </button>
                          <div class="dropdown-menu">
                            <a class="dropdown-item" href="/teamembers/${member.id}/form">Edit</a>
                            <button class="dropdown-item" type="button" onclick="deleteMember(${member.id})">Delete</button>
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
    function deleteMember(memberId) {
      doDeleteAndReload(
        'Do you really want to delete the selected team member?',
        "/teamembers/"+memberId,
        '/teamembers'
      );
    }
</script>

<#include "/tiles/footer.ftl">