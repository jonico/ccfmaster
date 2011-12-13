#!/bin/sh

# point this to your svn checkout of http://ccf.open.collab.net/svn/ccf/trunk/www/mvnrepo
# (*not* ~/.m2/repository)
REPO_URL="file://c:/mvnrepo"

# special treatment for repo-parent
# mvn -f repo-parent.pom deploy:deploy-file -Durl=${REPO_URL} -DrepositoryId=ccf-repo -Dfile=repo-parent.pom -DpomFile=repo-parent.pom

# modify the for-loop to list only the artifacts you want to update. Valid entries are:
# tfapi sf_soap44_sdk sf_soap50_sdk sf_soap60_sdk integratedapps jaxen-1.1.2 hsqldb scrumworks_sdk
for file in tfapi sf_soap44_sdk sf_soap50_sdk sf_soap60_sdk integratedapps jaxen-1.1.2 hsqldb scrumworks_sdk;
do
    mvn -f ${file}.pom.xml deploy:deploy-file -Durl=${REPO_URL} -DrepositoryId=ccf-repo -Dfile=${file}.jar -DpomFile=${file}.pom.xml
done

# after running this script, change to $REPO_URL and execute the following two commands:
# svn st | awk '/^\?/ {print $2}' | xargs svn add
# svn ci
