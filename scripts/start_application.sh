# Set Java launch options

rm /usr/share/tomcat8/webapps/webportal.war

rm -rf /usr/share/tomcat8/webapps/webportal

cp /tmp/codedeploy-deployment-staging-area/webportal.war /usr/share/tomcat8/webapps/webportal.war

service tomcat8 start
