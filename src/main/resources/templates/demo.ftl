<#import "/spring.ftl" as spring />
<#include "/tiles/header.ftl">
 <link href="/css/form_validation.css" rel="stylesheet">

<div class="container mt-3">
     <div class="py-5 text-center">
        <h2>${operation} Spring Demo Draw</h2>
        <p class="lead">Fill the form below to add or update a Spring demo draw</p>
     </div>

    <#if validationError??>
        <div class="row">
            <div class="alert alert-danger col-md-12" role="alert">${validationError}</div>
        </div>
    </#if>

    <@spring.bind "demo"/>
    <form action="/demos" method="post">

        <div class="row">
          <div class="col-md-4 mb-3 text-right">
            <label for="title">Title</label>
          </div>
          <div class="col-md-8 mb-3">
            <@spring.formInput "demo.title"  "class='form-control'" />
             <@spring.showErrors " " "invalid-feedback"/>
          </div>
        </div> <!-- row -->

        <div class="row">
          <div class="col-md-4 mb-3 text-right">
            <label for="plannedDate">Date</label>
          </div>
          <div class="col-md-8 mb-3">
             <@spring.formInput "demo.plannedDate"  "class='form-control' placeholder='YYYY-MM-DD' pattern='(?:19|20)[0-9]{2}-(?:(?:0[1-9]|1[0-2])-(?:0[1-9]|1[0-9]|2[0-9])|(?:(?!02)(?:0[1-9]|1[0-2])-(?:30))|(?:(?:0[13578]|1[02])-31))' " />
            <@spring.showErrors " " "invalid-feedback"/>
          </div>
        </div> <!-- row -->

        <div class="row">
          <div class="col-md-4 mb-3 text-right">
            <label for="firstName">Summary</label>
          </div>
          <div class="col-md-8 mb-3">
            <@spring.formTextarea  "demo.summary"  "class='form-control'" />
             <@spring.showErrors " " "invalid-feedback"/>
          </div>
        </div> <!-- row -->

        <div class="row">
          <div class="col-md-4 mb-3 text-right">
            <label for="firstName">Link</label>
          </div>
          <div class="col-md-8 mb-3">
            <@spring.formInput "demo.link"  "class='form-control'" />
             <@spring.showErrors " " "invalid-feedback"/>
          </div>
        </div> <!-- row -->


        <hr class="mb-4">
        <@spring.formHiddenInput "demo.id"/>
        <div class="row">

          <#if demo.drawStatus.name() == "PREPARATION">
              <div class="col-md-8 "> &nbsp;</div>
              <div class="col-md-2 text-right">
                <input type="submit" value="Save" class="btn btn-success btn-lg btn-block">
              </div>
              <div class="col-md-2 text-right">
                <a role="button" class="btn btn-warning btn-lg btn-block" href="/demos">Cancel</a>
              </div>
          <#else>
              <div class="col-md-10"> &nbsp;</div>
              <div class="col-md-2 text-right">
                <a role="button" class="btn btn-warning btn-lg btn-block" href="/demos">Close</a>
              </div>
          </#if>


        </div>
    </form>
</div>

<#include "tiles/footer.ftl">