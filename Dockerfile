FROM airhacks/glassfish
COPY ./target/mef_marketing.war ${DEPLOYMENT_DIR}
