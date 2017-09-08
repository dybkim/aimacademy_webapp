# Set Java launch options

CATALINA_HOME='/usr/share/tomcat8'
TEMP_STAGING_DIR='/tmp/codedeploy-deployment-staging-area'
WAR_STAGED_LOCATION="$TEMP_STAGING_DIR/webportal.war"

rm $CATALINA_HOME/webapps/webportal.war

rm -rf $CATALINA_HOME/webapps/webportal

cp $WAR_STAGED_LOCATION $CATALINA_HOME/webapps/webportal.war

service tomcat8 start
