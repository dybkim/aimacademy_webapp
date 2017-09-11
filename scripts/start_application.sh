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

mkdir $CATALINA_HOME/webapps/webportal
mv $WAR_STAGED_LOCATION $CATALINA_HOME/webapps/webportal.war
mv TEMP_STAGING_DIR/* $CATALINA_HOME/webapps/webportal/

service tomcat8 start
