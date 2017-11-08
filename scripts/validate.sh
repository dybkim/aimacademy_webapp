cp /tmp/codedeploy-deployment-staging-area/production.properties /usr/share/tomcat8/webapps/webportal/WEB-INF/resources/properties

service tomcat8 restart
