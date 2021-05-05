APPLICATION_NAME=`cat settings.gradle | grep "rootProject.name=" | sed 's/rootProject.name=.\(.*\)./\1/'`
APPLICATION_VERSION=`cat build.gradle | grep -E "^version" | sed "s/version '\(.*\)'/\1/"`

ARTIFACT_VERSION=${APPLICATION_NAME}:${APPLICATION_VERSION}
DOCKER_IMAGE_NATIVE=${APPLICATION_NAME}:${APPLICATION_VERSION}-native
DOCKER_IMAGE_JVM=${APPLICATION_NAME}:${APPLICATION_VERSION}-jvm
DOCKER_COMPOSE_FILE=src/test/docker/docker-compose.yml

.PHONY: help

help:
	@echo "-------------------"
	@echo "developer entry point for luckless-bot"
	@echo "-------------------"
	@echo "SUPPORTED MAKE COMMANDS:"
	@./utils/make-help.pl | column -t -s '|'
	@echo ""
	@echo "CURRENT LOCAL BUILD CONFIG:"
	@echo "JAVA_HOME=${JAVA_HOME}"
	java -version
	gradle --version
	docker --version
	@echo ""
	@echo "BUILD ARTIFACTS:"
	@echo "APPLICATION_NAME: ${APPLICATION_NAME}"
	@echo "DOCKER_IMAGE_NATIVE: ${DOCKER_IMAGE_NATIVE}"
	@echo "DOCKER_IMAGE_JVM: ${DOCKER_IMAGE_JVM}"
	@echo ""

clean:
	@echo "-------------------"
	@echo "clean up build artifacts"
	@echo "-------------------"
	gradle clean

format:
	@echo "-------------------"
	@echo "reformat all java code with google-java-format"
	@echo "-------------------"
	gradle spotlessApply

check-format:
	@echo "-------------------"
	@echo "check code format, non 0 exit code if reformatting needed"
	@echo "-------------------"
	gradle spotlessCheck

compile: clean
	@echo "-------------------"
	@echo "mvn compile"
	@echo "-------------------"
	gradle build

dev:
	@echo "-------------------"
	@echo "start development REST server with hot-reloading"
	@echo "-------------------"
	gradle quarkusDev

jvm-test: clean
	@echo "-------------------"
	@echo "jvm test"
	@echo "-------------------"
	gradle test

native-test: clean
	@echo "-------------------"
	@echo "native test"
	@echo "-------------------"
	gradle testNative

# NATIVE TARGET
package-native: clean
	@echo "-------------------"
	@echo "build native executable for current host OS"
	@echo "-------------------"
	gradle build -Dquarkus.package.type=native

start-native: package-native
	@echo "-------------------"
	@echo "start native application"
	@echo "-------------------"
	./target/${APPLICATION_NAME}-${APPLICATION_VERSION}

native-image: clean
	@echo "-------------------"
	@echo "build native docker image"
	@echo "-------------------"
	#./mvnw package -Dstyle.color=always -DskipTests=true -Pnative -Dquarkus.native.container-build=true -Dquarkus.container-image.build=true -Dquarkus.native.builder-image=quay.io/quarkus/ubi-quarkus-mandrel:20.1-java11
	./gradlew build -Dquarkus.package.type=native -Dquarkus.native.container-build=true
	docker build -f src/main/docker/Dockerfile.native -t ${DOCKER_IMAGE_NATIVE} -t ${APPLICATION_NAME}:latest .

docker-compose-up:
	@echo "-------------------"
	@echo "start docker broker:latest image in docker-compose"
	@echo "-------------------"
	docker-compose --file src/main/docker/docker-compose.yml up --remove-orphans

#
#save-native-image: native-image
#	@echo "-------------------"
#	@echo "save native docker image to tar.gz"
#	@echo "-------------------"
#	docker save --output=${APPLICATION_NAME}-${APPLICATION_VERSION}-image.tar ${DOCKER_IMAGE_NATIVE}
#	tar -cvzf ${APPLICATION_NAME}-${APPLICATION_VERSION}-image.tar.gz ${APPLICATION_NAME}-${APPLICATION_VERSION}-image.tar

# JVM TARGET
#jvm-package: compile
#	@echo "-------------------"
#	@echo "build jar executable"
#	@echo "-------------------"
#	./mvnw package -Dstyle.color=always -DskipTests=true
#
#jvm-image: compile jvm-package
#	@echo "-------------------"
#	@echo "build JVM docker image"
#	@echo "-------------------"
#	docker build -f src/main/docker/Dockerfile.jvm -t ${DOCKER_IMAGE_JVM} .
#
#save-jvm-image: jvm-image
#	@echo "-------------------"
#	@echo "save jvm docker image to tar.gz"
#	@echo "-------------------"
#	docker save --output=${APPLICATION_NAME}-${APPLICATION_VERSION}-image.tar ${DOCKER_IMAGE_JVM}
#	tar -cvzf ${APPLICATION_NAME}-${APPLICATION_VERSION}-image.tar.gz ${APPLICATION_NAME}-${APPLICATION_VERSION}-image.tar
