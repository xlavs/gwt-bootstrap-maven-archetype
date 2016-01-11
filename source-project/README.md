Congratulations, you've successfully generated a starter project!  What next?

## How to build from the command line with Maven

If you prefer to work from the command line, you can use Maven to build your
project. (http://maven.apache.org/) Maven uses the generated `pom.xml` file
which describes exactly how to build your project. This file has been tested
to work against Maven 3.3.3. The following assumes `mvn` is on your command
line path.

To run development mode, just type `mvn gwt:run`.

To compile and also bundle into a .war file, type 
`mvn clean compile gwt:compile -Dgwt.superDevMode=false war:war`.

To deploy and run application as Tomcat servlet, deploy it to the Tomcat 
`webapps`

You can type `mvn test` to run tests.

For more information about other available goals, read Maven and gwt-maven-plugin 
documentation
- http://maven.apache.org
- https://gwt-maven-plugin.github.io/gwt-maven-plugin

## How to work with project form IDE

Your IDE should support importing a Maven project. Add run configurations to 
start and stop devmode from IDE using Maven command examples above.