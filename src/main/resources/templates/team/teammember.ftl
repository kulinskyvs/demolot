<#import "/spring.ftl" as spring />
<#include "/tiles/header.ftl">
 <link href="/css/form_validation.css" rel="stylesheet">

<div class="container mt-3">
    <div class="py-3 text-center">
        <h2>${operation} team member</h2>
        <p class="lead">Fill the form below to add or update a team member</p>
    </div>

    <#if validationError??>
        <div class="row">
            <div class="alert alert-danger col-md-12" role="alert">${validationError}</div>
        </div>
    </#if>

    <@spring.bind "member"/>
    <form action="/teamembers" method="post">

        <div class="row">
          <div class="col-md-4 mb-3 text-right">
            <label for="firstName">First name</label>
          </div>
          <div class="col-md-8 mb-3">
            <@spring.formInput "member.name"  "class='form-control'" />
             <@spring.showErrors " " "invalid-feedback"/>
          </div>
        </div> <!-- row -->

        <div class="row">
          <div class="col-md-4 mb-3 text-right">
            <label for="lastName">Last name</label>
          </div>
          <div class="col-md-8 mb-3">
            <@spring.formInput "member.surname"  "class='form-control'" />
             <@spring.showErrors " " "invalid-feedback"/>
          </div>
        </div> <!-- row -->

        <div class="row">
          <div class="col-md-4 mb-3 text-right">
            <label for="email">Email</label>
          </div>
          <div class="col-md-8 mb-3">
            <@spring.formInput "member.email"  "class='form-control'" />
             <@spring.showErrors " " "invalid-feedback"/>
          </div>
        </div> <!-- row -->

        <div class="row">
          <div class="col-md-4 mb-3">&nbsp;</div>
          <div class="col-md-8 mb-3">
            <div class="form-group form-check">
               <@spring.formCheckbox "member.active"  "class='form-check-input'" />
               <label class="form-check-label" for="active">Active</label>
            </div>
            <@spring.showErrors " " "invalid-feedback"/>
          </div>
        </div> <!-- row -->

        <hr class="mb-4">
        <@spring.formHiddenInput "member.id"/>
        <div class="row">
          <div class="col-md-8 "> &nbsp;</div>
          <div class="col-md-2 text-right">
            <input type="submit" value="Save" class="btn btn-success btn-lg btn-block">
          </div>
          <div class="col-md-2 text-right">
            <a role="button" class="btn btn-warning btn-lg btn-block" href="/teamembers">Cancel</a>
          </div>
        </div>
    </form>
</div>

<#include "/tiles/footer.ftl">