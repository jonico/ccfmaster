#!/bin/sh

REPO_URL="file://c:/mvnrepo"

# special treatment for repo-parent
# mvn -f repo-parent.pom deploy:deploy-file -Durl=${REPO_URL} -DrepositoryId=ccf-repo -Dfile=repo-parent.pom -DpomFile=repo-parent.pom

# tfapi sf_soap44_sdk sf_soap50_sdk sf_soap60_sdk integratedapps jaxen-1.1.2 hsqldb
for file in tfapi sf_soap44_sdk sf_soap50_sdk sf_soap60_sdk integratedapps jaxen-1.1.2 hsqldb;
do
    mvn -f ${file}.pom.xml deploy:deploy-file -Durl=${REPO_URL} -DrepositoryId=ccf-repo -Dfile=${file}.jar -DpomFile=${file}.pom.xml
done

# svn st | awk '/^\?/ {print $2}' | xargs svn add
# svn ci
