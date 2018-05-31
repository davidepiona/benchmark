java -Xmx384m -Dspring.profiles.active=kvm -Dcom.sun.management.jmxremote -Dcom.sun.management.jmxremote.authenticate=false \
    -Dcom.sun.management.jmxremote.ssl=false -Dcom.sun.management.jmxremote.port=7070 -Dcom.sun.management.jmxremote.rmi.port=7070 \
    -Dcom.sun.management.jmxremote.local.only=false -Djava.rmi.server.hostname=10.0.1.78 -Dcom.sun.management.jmxremote.host=10.0.1.78 \
    -jar wsTest-0.0.1-SNAPSHOT.jar