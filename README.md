> This is a Java application template to bootstrap Your GWT 2.7.0 / Bootstrap3 project. 

### Quick installation

- Run `generate-archetype.cmd` from the project root.
- Run `install-archetype.cmd` to install the generated archetype in Your local maven repository.
- To create a new project, go to Your workspace folder and run `mvn archetype:generate -DarchetypeCatalog=local`. Then select `gwt-bootstrap3-app` archetype.

This is a modified version of [gwt-maven-archetype](https://github.com/xlavs/gwt-maven-archetype). This version is altered to produce the same functionality but uses widgets from [gwtbootstrap3](https://github.com/gwtbootstrap3/gwtbootstrap3) project.

Look [gwt-maven-archetype](https://github.com/xlavs/gwt-maven-archetype) for more details on how to generate custom Maven archetypes and how to make GWT work with Maven. Sample includes an RPC call from GWT generated frontend to the serverside "servlet". Special care is taken to make sure that this sample can be deployed and works on Tomcat application server.
