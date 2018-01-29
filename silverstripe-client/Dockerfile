FROM java:8-jre
MAINTAINER Chris Turner <peavers@gmail.com>

ADD ./build/libs/silverstripe-client-1.0.jar /app/
CMD ["java", "-Xmx200m", "-jar", "/app/silverstripe-client-1.0.jar"]

