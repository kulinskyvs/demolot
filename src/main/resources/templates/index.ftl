<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="utf-8">
        <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
        <meta name="description" content="">
        <meta name="author" content="">

        <title>Demolot</title>
        <link href="/css/bootstrap.css" rel="stylesheet"/>
        <!-- Custom styles for this template -->
        <link href="/css/jumbotron.css" rel="stylesheet">
    </head>
    <body>

     <nav class="navbar navbar-expand-md navbar-dark fixed-top bg-dark">
          <a class="navbar-brand" href="#">Home</a>
          <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarsExampleDefault" aria-controls="navbarsExampleDefault" aria-expanded="false" aria-label="Toggle navigation">
            <span class="navbar-toggler-icon"></span>
          </button>

          <div class="collapse navbar-collapse" id="navbarsExampleDefault">
            <ul class="navbar-nav mr-auto">
              <li class="nav-item">
                <a class="nav-link" href="${team_url}">Team <span class="sr-only">(current)</span></a>
              </li>
              <li class="nav-item">
                <a class="nav-link" href="${draws_url}">Draws</a>
              </li>
            </ul>
          </div>
     </nav>


    <main role="main">

      <!-- Main jumbotron for a primary marketing message or call to action -->
      <div class="jumbotron">
        <div class="container">
          <h1 class="display-3">Welcome to Demolot!</h1>
          <p>Demolot is a simple application intended to turn the process of deciding who is repsonsible
          to show a task on next Spint Demo into fun. Manage your team members, create Demo draw and let's have fun.</p>
          <p><a class="btn btn-primary btn-lg" href="${draws_url}" role="button">Start playing &raquo;</a></p>
        </div>
      </div>

      <div class="container">
        <!-- Example row of columns -->
        <div class="row">
          <div class="col-md-6">
            <h2>Team</h2>
            <p>Manage your team members is easy: add and activate a team member in order to include him into next Demo draw.
             A person is leaving the team? - Just inactivate him with storing all the history.
             Nothing can be simplier!</p>
            <p><a class="btn btn-secondary" href="${team_url}" role="button">Manage my team &raquo;</a></p>
          </div>
          <div class="col-md-6">
            <h2>Draws</h2>
            <p> Simply prepare the list of tasks to be shown on next Spring demo and start the Draw > definitely, you'll have a lot of fun...
            The winners will be happy to receive a kindly reminder with the list of tasks they have won - no more excuses that
            something is missed or forgotten. </p>
            <p><a class="btn btn-secondary" href="${draws_url}" role="button">Start a draw &raquo;</a></p>
          </div>
        </div>

        <hr>

      </div> <!-- /container -->

    </main>

    <footer class="container">
      <p>&copy; Kyriba 2018</p>
    </footer>

    <!-- jQuery and bootstap -->
    <script src="/js/lib/jquery-3.1.0.min.js"></script>
    <script src="/js/lib/bootstrap.min.js"></script>

    </body>
</html>