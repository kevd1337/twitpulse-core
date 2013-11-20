twitpulse-core
==============

TwitPulse's twitter sentiment analysis core library code. Also includes a webapplication wrapper around the library. 

twitpulse-core is part of the larger TwitPulse webapp created at the TechCrunch Disrupt SF 2013 Hackathon that summarizes conversations and tracks the evolution of sentiment over time around topics like sports or politics. Machine learning and social data are leveraged as opposed to using traditional surveys and polls.

Demo video and details on TwitPulse can be found @ [ShowHacks](http://www.showhacks.com/projects/5260c09f92dbaf0200000004)

## Twitter Sentiment Training Data

The primary training data set is omitted from repo to save space. 

For the tweet sentiment classifier, see this training data:
http://cs.stanford.edu/people/alecmgo/trainingandtestdata.zip

Information on the data set can be found here:
http://help.sentiment140.com/for-students

## Command Line Interface

Command line interfaces for training and invoking the predictors can be found in the `org.twitpulse.cli` package, the two programs are called:
* SentimentPredictorTrainerCLI
* SentimentPredictorCLI

A usage note for the programs will be printed if invoked without arguments. Currently these can be run in eclipse using a java application run configuration, or on the command line like this:
    
    $ java -cp target/classes:target/dependency/* org.twitpulse.cli.SentimentPredictorTrainerCLI
    $ java -cp target/classes:target/dependency/* org.twitpulse.cli.SentimentPredictorCLI
    
A baseline trained model file can be found in `res/baseline.model`

Basic evaluation metrics about the trained model can be found in `res/baseline.model.training-evaluation`

## Running the web application locally

UNDERCONSTRUCTION - simple webapp / JSON APIs for invoking the twitter sentiment predictor 

First build with:

    $ mvn clean install
    
Or alternatively import project into eclipse, make sure it's a maven java project.


To run it on command line:

    $ java -cp target/classes:target/dependency/* org.twitpulse.webapp.Main

Or alternatively using foreman:

    $ foreman start
   
Or alternatively you can setup an eclipse java application run configuration to make use of `org.twitpulse.webapp.Main`

Your app should now be running on [localhost:5000](http://localhost:5000/).
