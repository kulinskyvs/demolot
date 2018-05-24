<#include "/tiles/header.ftl">

<div class="container mt-3">

    <div class="row">
      <div class="col-md-6 text-left">
         <h2>Team members</h2>
      </div>
      <div class="col-md-6 text-right">
         <a role="button" class="btn btn-secondary btn-success" href="/teamembers/form">Create</a>
      </div>
    </div>

    <div class="row">
        <div class="table-responsive">
          <table class="table table-striped table-sm">
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
                  <td style="width:15%;text-align:right">
                   <a role="button" class="btn btn-warning" href="/teamembers/form/${member.id}">Edit</a> &nbsp;
                   <button class="btn btn-danger"  onclick="deleteMember(${member.id})">Delete</button>
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

<#include "tiles/footer.ftl">