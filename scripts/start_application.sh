# Set Java launch options

set JAVA_OPTS='-Dspring.profiles.active=production -Duser.timezone=GMT'
service tomcat8 start
