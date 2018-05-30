java -Xmx384m -Dcom.sun.management.jmxremote -Dcom.sun.management.jmxremote.authenticate=false \
    -Dcom.sun.management.jmxremote.ssl=false -Dcom.sun.management.jmxremote.port=7010 -Dcom.sun.management.jmxremote.rmi.port=7010 \
    -Dcom.sun.management.jmxremote.local.only=false -Djava.rmi.server.hostname=0.0.0.0 -Dcom.sun.management.jmxremote.host=0.0.0.0 \
    -jar target/registry-0.0.1-SNAPSHOT.jar