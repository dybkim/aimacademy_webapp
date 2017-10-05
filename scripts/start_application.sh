# Set Java launch options

CATALINA_HOME='/usr/share/tomcat8'
TEMP_STAGING_DIR='/tmp/codedeploy-deployment-staging-area'
WAR_STAGED_LOCATION="$TEMP_STAGING_DIR/webportal.war"

if [[ -f $CATALINA_HOME/webapps/webportal.war ]]; then
	rm $CATALINA_HOME/webapps/webportal.war
fi

if [[ -d $CATALINA_HOME/webapps/webportal ]]; then
	rm -rfv $CATALINA_HOME/webapps/webportal/
fi

#Copy Required Files

mv $WAR_STAGED_LOCATION $CATALINA_HOME/webapps/webportal.war

service tomcat8 start
