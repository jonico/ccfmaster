@echo off
call mvn install:install-file -Dfile="%~dp0sf_soap44_sdk.jar"  -DpomFile="%~dp0sf_soap44_sdk.pom.xml"
call mvn install:install-file -Dfile="%~dp0sf_soap50_sdk.jar"  -DpomFile="%~dp0sf_soap50_sdk.pom.xml"
call mvn install:install-file -Dfile="%~dp0sf_soap60_sdk.jar"  -DpomFile="%~dp0sf_soap60_sdk.pom.xml"
call mvn install:install-file -Dfile="%~dp0tfapi.jar"          -DpomFile="%~dp0tfapi.pom.xml"
call mvn install:install-file -Dfile="%~dp0integratedapps.jar" -DpomFile="%~dp0integratedapps.pom.xml"
call mvn install:install-file -Dfile="%~dp0jaxen-1.1.2.jar"          -DpomFile="%~dp0jaxen-1.1.2.pom.xml"
call mvn install:install-file -Dfile="%~dp0hsqldb.jar"          -DpomFile="%~dp0hsqldb.pom.xml"
