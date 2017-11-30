FROM frolvlad/alpine-oraclejdk8:slim
VOLUME /tmp
#使用idea docker 插件 push jar 文件 和 dockerfile 文件不在一起 因此需配置 为编译文件夹
ADD /target/upload.jar app.jar
RUN sh -c 'touch /app.jar'
ENV JAVA_OPTS=""
ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar /app.jar" ]

EXPOSE 8080

