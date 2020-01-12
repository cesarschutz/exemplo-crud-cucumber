FROM 651839557319.dkr.ecr.sa-east-1.amazonaws.com/cartoes/openjdk11-zulu:1.0.0
#FROM azul/zulu-openjdk:11

RUN mkdir /app

WORKDIR /app

COPY /build/libs/proposal-*.jar /app/proposal.jar

EXPOSE 8080
ENV JAVA_TOOL_OPTIONS -Dfile.encoding=UTF8 -Duser.country=BR -Duser.language=pt -Duser.timezone=America/Sao_Paulo
CMD ["java", "-jar", "-Xmx256M", "-Xms128M","-XX:MaxMetaspaceSize=128m", "proposal.jar"]
