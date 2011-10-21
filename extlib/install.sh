#!/bin/sh
mvn install:install-file -Dfile=sf_soap44_sdk.jar -DpomFile=sf_soap44_sdk.pom.xml
mvn install:install-file -Dfile=sf_soap50_sdk.jar -DpomFile=sf_soap50_sdk.pom.xml
mvn install:install-file -Dfile=sf_soap60_sdk.jar -DpomFile=sf_soap60_sdk.pom.xml
mvn install:install-file -Dfile=tfapi.jar -DpomFile=tfapi.pom.xml
mvn install:install-file -Dfile=integratedapps.jar -DpomFile=integratedapps.pom.xml
mvn install:install-file -Dfile=jaxen-1.1.2.jar -DpomFile=jaxen-1.1.2.pom.xml
mvn install:install-file -Dfile=hsqldb.jar -DpomFile=hsqldb.pom.xml
