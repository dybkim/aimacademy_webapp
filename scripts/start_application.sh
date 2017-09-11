# Set Java launch options

CATALINA_HOME='/usr/share/tomcat8'
TEMP_STAGING_DIR='/tmp/codedeploy-deployment-staging-area'
WAR_STAGED_LOCATION="$TEMP_STAGING_DIR/webportal.war"

if [[ -f $CATALINA_HOME/webapps/webportal.war ]]; then
	rm $CATALINA_HOME/webapps/webportal.war
fi

if [[ -d $CATALINA_HOME/webapps/webportal ]]; then
	rm -rfv $CATALINA_HOME/webapps/webportal
fi

#Copy Required Files

cp $WAR_STAGED_LOCATION $CATALINA_HOME/webapps/webportal.war

if [[ -d $CATALINA_HOME/webapps/webportal/META-INF ]]; then
	rm -rfv $CATALINA_HOME/webapps/webportal/META-INF

if [[ -d $CATALINA_HOME/webapps/webportal/WEB-INF ]]; then
	rm -rfv $CATALINA_HOME/webapps/webportal/WEB-INF

cp -R TEMP_STAGING_DIR/META-INF $CATALINA_HOME/webapps
cp -R TEMP_STAGING_DIR/WEB-INF $CATALINA_HOME/webapps

service tomcat8 start
