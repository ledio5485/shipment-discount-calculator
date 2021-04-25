# ShipmentDiscountCalculator

### Prerequisites
- Java 11+
- Gradle 6.7+

### Useful commands:
- Run tests: `./gradlew clean test -i` (99% code coverage!)
- Build app: `./gradlew clean build -i`
- Run App: `./gradlew clean build -i && java -jar build/libs/*.jar input.txt price.txt`


### Assumptions
- Each courier has provided the prices for each package size, and there's no duplication.
- Each price list entry is valid in the following format <CourierCode PackageSize Price(€uro)>
- The price is expressed only in €uro for simplicity, to not deal with currency converter.
- Each transaction is considered valid only if in the following format <Date(ISO format) PackageSize CourierCode>

###
The algorithm is very simple:
1) the app accepts 2 args as input
    1) input file of transactions(`input.txt`)
    2) price list(`price.txt`)
2) The app tries to parse both files
3) It creates a price list catalog to use later
4) Apply the standard price to each transaction
5) Apply all discount rules(only 2 for now) to each transaction
   1) at this point we can add even more discount rules
   2) Accumulated discounts cannot exceed 10 € in a calendar month. If there are not enough funds to fully cover a discount in the current calendar month, it should be covered partially.
6) Write the formatted output in terminal/console:

```shell
LED@MacBook-Pro ShipmentDiscountCalculator % ./gradlew clean build -i && java -jar build/libs/*.jar input.txt price.txt
Initialized native services in: /Users/LED/.gradle/native
The client will now receive all logging from the daemon (pid: 37623). The daemon log file: /Users/LED/.gradle/daemon/6.7/daemon-37623.out.log
Starting 22nd build in daemon [uptime: 2 hrs 23 mins 54.859 secs, performance: 100%, non-heap usage: 57% of 268,4 MB]
Using 12 worker leases.
Watching the file system is disabled
Starting Build
Settings evaluated using settings file '/Users/LED/ShipmentDiscountCalculator/settings.gradle'.
Projects loaded. Root project using build file '/Users/LED/ShipmentDiscountCalculator/build.gradle'.
Included projects: [root project 'ShipmentDiscountCalculator']

> Configure project :
Evaluating root project 'ShipmentDiscountCalculator' using build file '/Users/LED/ShipmentDiscountCalculator/build.gradle'.
All projects evaluated.
Selected primary task 'clean' from project :
Selected primary task 'build' from project :
Tasks to be executed: [task ':clean', task ':compileJava', task ':processResources', task ':classes', task ':jar', task ':assemble', task ':compileTestJava', task ':processTestResources', task ':testClasses', task ':test', task ':jacocoTestReport', task ':jacocoTestCoverageVerification', task ':check', task ':build']
Tasks that were excluded: []
:clean (Thread[Execution worker for ':',5,main]) started.

> Task :clean
Caching disabled for task ':clean' because:
  Build cache is disabled
Task ':clean' is not up-to-date because:
  Task has not declared any outputs despite executing actions.
:clean (Thread[Execution worker for ':',5,main]) completed. Took 0.077 secs.
:compileJava (Thread[Execution worker for ':',5,main]) started.

> Task :compileJava
Caching disabled for task ':compileJava' because:
  Build cache is disabled
Task ':compileJava' is not up-to-date because:
  Output property 'destinationDirectory' file /Users/LED/ShipmentDiscountCalculator/build/classes/java/main has been removed.
  Output property 'destinationDirectory' file /Users/LED/ShipmentDiscountCalculator/build/classes/java/main/org has been removed.
  Output property 'destinationDirectory' file /Users/LED/ShipmentDiscountCalculator/build/classes/java/main/org/example has been removed.
The input changes require a full rebuild for incremental task ':compileJava'.
Full recompilation is required because no incremental change information is available. This is usually caused by clean builds or changing compiler arguments.
Compiling with JDK Java compiler API.
Created classpath snapshot for incremental compilation in 0.0 secs.
:compileJava (Thread[Execution worker for ':',5,main]) completed. Took 0.382 secs.
:processResources (Thread[Execution worker for ':',5,main]) started.

> Task :processResources NO-SOURCE
Skipping task ':processResources' as it has no source files and no previous output files.
:processResources (Thread[Execution worker for ':',5,main]) completed. Took 0.0 secs.
:classes (Thread[Execution worker for ':',5,main]) started.

> Task :classes
Skipping task ':classes' as it has no actions.
:classes (Thread[Execution worker for ':',5,main]) completed. Took 0.0 secs.
:jar (Thread[Execution worker for ':' Thread 7,5,main]) started.

> Task :jar
Caching disabled for task ':jar' because:
  Build cache is disabled
Task ':jar' is not up-to-date because:
  Output property 'archiveFile' file /Users/LED/ShipmentDiscountCalculator/build/libs/ShipmentDiscountCalculator-1.0.0.jar has been removed.
file or directory '/Users/LED/ShipmentDiscountCalculator/build/resources/main', not found
:jar (Thread[Execution worker for ':' Thread 7,5,main]) completed. Took 0.028 secs.
:assemble (Thread[Execution worker for ':' Thread 7,5,main]) started.

> Task :assemble
Skipping task ':assemble' as it has no actions.
:assemble (Thread[Execution worker for ':' Thread 7,5,main]) completed. Took 0.0 secs.
:compileTestJava (Thread[Execution worker for ':' Thread 7,5,main]) started.

> Task :compileTestJava
Caching disabled for task ':compileTestJava' because:
  Build cache is disabled
Task ':compileTestJava' is not up-to-date because:
  Output property 'destinationDirectory' file /Users/LED/ShipmentDiscountCalculator/build/classes/java/test has been removed.
  Output property 'destinationDirectory' file /Users/LED/ShipmentDiscountCalculator/build/classes/java/test/org has been removed.
  Output property 'destinationDirectory' file /Users/LED/ShipmentDiscountCalculator/build/classes/java/test/org/example has been removed.
The input changes require a full rebuild for incremental task ':compileTestJava'.
Full recompilation is required because no incremental change information is available. This is usually caused by clean builds or changing compiler arguments.
Compiling with JDK Java compiler API.
Created classpath snapshot for incremental compilation in 0.001 secs. 1 duplicate classes found in classpath (see all with --debug).
:compileTestJava (Thread[Execution worker for ':' Thread 7,5,main]) completed. Took 0.46 secs.
:processTestResources (Thread[Execution worker for ':' Thread 7,5,main]) started.

> Task :processTestResources NO-SOURCE
Skipping task ':processTestResources' as it has no source files and no previous output files.
:processTestResources (Thread[Execution worker for ':' Thread 7,5,main]) completed. Took 0.0 secs.
:testClasses (Thread[Execution worker for ':' Thread 7,5,main]) started.

> Task :testClasses
Skipping task ':testClasses' as it has no actions.
:testClasses (Thread[Execution worker for ':' Thread 7,5,main]) completed. Took 0.0 secs.
:test (Thread[Execution worker for ':' Thread 10,5,main]) started.
Gradle Test Executor 20 started executing tests.

> Task :test
Caching disabled for task ':test' because:
  Build cache is disabled
Task ':test' is not up-to-date because:
  Output property 'binaryResultsDirectory' file /Users/LED/ShipmentDiscountCalculator/build/test-results/test/binary has been removed.
  Output property 'binaryResultsDirectory' file /Users/LED/ShipmentDiscountCalculator/build/test-results/test/binary/output.bin has been removed.
  Output property 'binaryResultsDirectory' file /Users/LED/ShipmentDiscountCalculator/build/test-results/test/binary/output.bin.idx has been removed.
Starting process 'Gradle Test Executor 20'. Working directory: /Users/LED/ShipmentDiscountCalculator Command: /Users/LED/.sdkman/candidates/java/11.0.2-open/bin/java -Dorg.gradle.native=false -javaagent:build/tmp/expandedArchives/org.jacoco.agent-0.8.6.jar_a26f6511a7813217be4cd6439d66563b/jacocoagent.jar=destfile=build/jacoco/test.exec,append=true,inclnolocationclasses=false,dumponexit=true,output=file,jmx=false @/private/var/folders/rs/5j5d57gn0fl7k4xj_82vdq040000gn/T/gradle-worker-classpath5677972104953562840txt -Xmx512m -Dfile.encoding=US-ASCII -Duser.country=DE -Duser.language=en -Duser.variant -ea worker.org.gradle.process.internal.worker.GradleWorkerMain 'Gradle Test Executor 20'
Successfully started process 'Gradle Test Executor 20'

Gradle Test Executor 20 finished executing tests.

> Task :test
Finished generating test XML results (0.004 secs) into: /Users/LED/ShipmentDiscountCalculator/build/test-results/test
Generating HTML test report...
Finished generating test html results (0.004 secs) into: /Users/LED/ShipmentDiscountCalculator/build/reports/tests/test
:test (Thread[Execution worker for ':' Thread 10,5,main]) completed. Took 3.217 secs.
:jacocoTestReport (Thread[Execution worker for ':' Thread 10,5,main]) started.

> Task :jacocoTestReport
Caching disabled for task ':jacocoTestReport' because:
  Build cache is disabled
Task ':jacocoTestReport' is not up-to-date because:
  Output property 'reports.enabledReports.html.outputLocation' file /Users/LED/ShipmentDiscountCalculator/build/reports/jacoco/test/html has been removed.
  Output property 'reports.enabledReports.html.outputLocation' file /Users/LED/ShipmentDiscountCalculator/build/reports/jacoco/test/html/index.html has been removed.
  Output property 'reports.enabledReports.html.outputLocation' file /Users/LED/ShipmentDiscountCalculator/build/reports/jacoco/test/html/jacoco-resources has been removed.
[ant:jacocoReport] Loading execution data file /Users/LED/ShipmentDiscountCalculator/build/jacoco/test.exec
[ant:jacocoReport] Writing bundle 'ShipmentDiscountCalculator' with 13 classes
:jacocoTestReport (Thread[Execution worker for ':' Thread 10,5,main]) completed. Took 0.588 secs.
:jacocoTestCoverageVerification (Thread[Execution worker for ':' Thread 10,5,main]) started.

> Task :jacocoTestCoverageVerification
Caching disabled for task ':jacocoTestCoverageVerification' because:
  Build cache is disabled
Task ':jacocoTestCoverageVerification' is not up-to-date because:
  Task has not declared any outputs despite executing actions.
[ant:jacocoReport] Loading execution data file /Users/LED/ShipmentDiscountCalculator/build/jacoco/test.exec
[ant:jacocoReport] Writing bundle 'ShipmentDiscountCalculator' with 13 classes
:jacocoTestCoverageVerification (Thread[Execution worker for ':' Thread 10,5,main]) completed. Took 0.06 secs.
:check (Thread[Execution worker for ':' Thread 10,5,main]) started.

> Task :check
Skipping task ':check' as it has no actions.
:check (Thread[Execution worker for ':' Thread 10,5,main]) completed. Took 0.0 secs.
:build (Thread[Execution worker for ':' Thread 10,5,main]) started.

> Task :build
Skipping task ':build' as it has no actions.
:build (Thread[Execution worker for ':' Thread 10,5,main]) completed. Took 0.0 secs.

Deprecated Gradle features were used in this build, making it incompatible with Gradle 7.0.
Use '--warning-mode all' to show the individual deprecation warnings.
See https://docs.gradle.org/6.7/userguide/command_line_interface.html#sec:command_line_warnings

BUILD SUCCESSFUL in 5s
7 actionable tasks: 7 executed
2015-02-01 S MR 1.50 0.50
2015-02-02 S MR 1.50 0.50
2015-02-03 L LP 6.90 -
2015-02-05 S LP 1.50 -
2015-02-06 S MR 1.50 0.50
2015-02-06 L LP 6.90 -
2015-02-07 L MR 4.00 -
2015-02-08 M MR 3.00 -
2015-02-09 L LP 0.00 6.90
2015-02-10 L LP 6.90 -
2015-02-10 S MR 1.50 0.50
2015-02-10 S MR 1.50 0.50
2015-02-11 L LP 6.90 -
2015-02-12 M MR 3.00 -
2015-02-13 M LP 4.90 -
2015-02-15 S MR 1.50 0.50
2015-02-17 L LP 6.90 -
2015-02-17 S MR 1.90 0.10
2015-02-24 L LP 6.90 -
2015-02-29 CUSPS Ignored
2015-03-01 S MR 1.50 0.50
```
