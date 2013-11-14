twitpulse-core
==============

TwitPulse's twitter sentiment analysis core library code. Also includes a webapplication wrapper around the library.

## Running the web application locally

First build with:

    $mvn clean install
    
Or alternatively import project into eclipse, make sure it's a maven java project.


To run it on command line:

    $ java -cp target/classes:target/dependency/* org.twitpulse.webapp.Main

Or alternatively using foreman:

   $ foreman start
   
Or alternatively you can setup an eclipse run configuration to make use of org.twitpulse.webapp.Main

Your app should now be running on [localhost:5000](http://localhost:5000/).

## Twitter Sentiment Training Data

Some training data is omitted from repo to save space (and probably due to licensing as well). 

For the tweet sentiment classifier, see this training data:
http://cs.stanford.edu/people/alecmgo/trainingandtestdata.zip
