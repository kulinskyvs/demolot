<#import "/spring.ftl" as spring />
<#include "/tiles/header.ftl">
 <link href="/css/form_validation.css" rel="stylesheet">

<div class="container mt-3">
    <div class="py-5 text-center">
        <h2>${operation} task for ${demo.title}</h2>
        <p class="lead">Fill the form below to create or update a task ${demo.title}</p>
    </div>

    <#if validationError??>
        <div class="row">
            <div class="alert alert-danger col-md-12" role="alert">${validationError}</div>
        </div>
    </#if>

    <@spring.bind "task"/>
    <form action="/demos/${demo.id}/tasks" method="post">

        <div class="row">
          <div class="col-md-4 mb-3 text-right">
            <label for="title">Key</label>
          </div>
          <div class="col-md-4 mb-3">
            <@spring.formInput "task.key"  "class='form-control'" />
            <@spring.showErrors " " "invalid-feedback"/>
          </div>
          <div class="col-md-4">&nbsp;</div>
        </div> <!-- row -->

        <div class="row">
          <div class="col-md-4 mb-3 text-right">
            <label for="title">Title</label>
          </div>
          <div class="col-md-8 mb-3">
            <@spring.formInput "task.title"  "class='form-control'" />
            <@spring.showErrors " " "invalid-feedback"/>
          </div>
        </div> <!-- row -->

        <div class="row">
          <div class="col-md-4 mb-3 text-right">
            <label for="title">Owner</label>
          </div>
          <div class="col-md-8 mb-3">
            <@spring.formSingleSelect "task.owner" members "class='form-control'"/>
            <@spring.showErrors " " "invalid-feedback"/>
          </div>
        </div> <!-- row -->

        <div class="row">
          <div class="col-md-4 mb-3 text-right">
            <label for="firstName">Link</label>
          </div>
          <div class="col-md-8 mb-3">
            <@spring.formInput "task.link"  "class='form-control'" />
             <@spring.showErrors " " "invalid-feedback"/>
          </div>
        </div> <!-- row -->

        <hr class="mb-4">
        <@spring.formHiddenInput "task.id"/>
        <div class="row">
          <div class="col-md-8 "> &nbsp;</div>
          <div class="col-md-2 text-right">
            <input type="submit" value="Save" class="btn btn-success btn-lg btn-block">
          </div>
          <div class="col-md-2 text-right">
            <a role="button" class="btn btn-warning btn-lg btn-block" href="/demos/${demo.id}/formtask">Cancel</a>
          </div>
        </div>
    </form>
</div>

<#include "tiles/footer.ftl">