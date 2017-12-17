FROM frolvlad/alpine-oraclejdk8:slim
VOLUME /tmp
#使用maven docker build mvn 会将打包文件 和dockerfile 全部移至docker文件夹
ADD upload.jar app.jar
ENV TZ=Asia/Shanghai
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone
RUN sh -c 'touch /app.jar'
ENV JAVA_OPTS=""
ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar /app.jar" ]
EXPOSE 8089

