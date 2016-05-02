# wedding-site

This is the code for running a website for a wedding.
The primary purpose for this software is to let people see where all of our receptions are.
Then they can choose to RSVP to any they are able to attend.

## Prerequisites

You will need [Leiningen][] 2.0.0 or above installed.
See `project.clj` for more information about the project's requirements.

## Running

To start a web server for the application, run:

    lein ring server

You can also run the web server from within a REPL:

    user=> (use 'wedding-site.handler :reload)
    user=> (def server (run {:join? false}))

And you can stop it and restart it:

    user=> (.stop server)
    user=> (def server (run {:join? false}))

## Contributing

This project follow's [Angular's commit message format][commit format].
Refer to the linked document for the full details.
But here's a quick reference:

    <type>(<scope>): <subject>
    <BLANK LINE>
    <body>
    <BLANK LINE>
    <footer>

The type must be one of the following:

* **feat**: A new feature
* **fix**: A bug fix
* **docs**: Documentation only changes
* **style**: Changes that do not affect the meaning of the code
  (white-space, formatting, missing semi-colons, etc)
* **refactor**: A code code change that neither fixes a bug or adds a feature
* **perf**: A code change that improves performance
* **test**: Adding missing tests
* **chore**: Changes to the build process
  or auxiliary tools and libraries
  such as documentation generation

## License

Copyright © 2016 Tom Small III.

Distributed under the Eclipse Public License, the same as Clojure.


<!-- References -->
[commit format]: https://gist.github.com/brianclements/841ea7bffdb01346392c
[leiningen]: https://github.com/technomancy/leiningen
