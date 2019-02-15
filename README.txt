Unfortunately the project still has exceptions around tests.

Steps to be carried out:

1) Refactor Service classes and EventBuildDirector until EventBuilderDirectorTest passes again
2) Add settings for transport.useInactivityMonitor=false on MiddlewareService
3) Handle ApplicationLogEvents in Event listener and add test case in EventProducerConsumerTest
4) Do a full F2B test on a file
5) Add property files for instance addresses
6) Ensure all services exit safely

To test:

run from cmd:
cd to project dir
gradle clean
gradle fatJar

cd to /build/libs
java -jar log-parse-util-1.0.jar
