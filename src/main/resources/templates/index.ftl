<#include "/tiles/header.ftl">

<main role="main" id="mainContainer">

  <div class="container mt-3">

    <!-- Main jumbotron for a primary marketing message or call to action -->
    <div class="jumbotron">
      <h1 class="display-3">Welcome to Demolot!</h1>
      <p>Demolot is a simple application intended to turn the process of deciding who is responsible
      to show a task on next Spint Demo into fun. Manage your team members, create a Demo, perform it's draw and let's have fun.</p>
      <p><a class="btn btn-primary btn-lg" href="/demos" role="button">Start playing &raquo;</a></p>
    </div>

    <!-- Example row of columns -->
    <div class="row">
      <div class="col-md-6">
        <h2>Team</h2>
        <p>Manage your team members is easy: add and activate a team member in order to include him into next Demo draw.
         A person is leaving the team? - Just inactivate him with storing all the history.
         Nothing can be simplier!</p>
        <p><a class="btn btn-secondary" href="/teamembers" role="button">Manage my team &raquo;</a></p>
      </div>
      <div class="col-md-6">
        <h2>Demos</h2>
        <p> Simply prepare the list of tasks to be shown on next Sprint demo and start the Draw > definitely, you'll have a lot of fun...
        The winners will be happy to receive a kindly reminder with the list of tasks they have won - no more excuses that
        something is missed or forgotten. </p>
        <p><a class="btn btn-primary" href="/demos" role="button">Start a draw &raquo;</a></p>
      </div>
    </div>

  </div> <!-- /container -->

</main>

<#include "/tiles/footer.ftl">

