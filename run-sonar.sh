#!/bin/bash

replacement='<properties><sonar.infostore.skipDependencies>true</sonar.infostore.skipDependencies><sonar.infostore.skip>true</sonar.infostore.skip><sonar.exclusions>src/main/java/com/github/valdr/demo/**/*,src/main/java/com/github/valdr/thirdparty/**/*</sonar.exclusions>'

sed -i .bak "s#<properties>#$replacement#" pom.xml

mvn --batch-mode -Dmaven.test.failure.ignore=true clean install sonar:sonar

rm -f pom.xml
mv pom.xml.bak pom.xml
