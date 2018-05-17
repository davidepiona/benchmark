java -Dspring.profiles.active=kvm -Dcom.sun.management.jmxremote -Dcom.sun.management.jmxremote.authenticate=false \
    -Dcom.sun.management.jmxremote.ssl=false -Dcom.sun.management.jmxremote.port=7999 -Dcom.sun.management.jmxremote.rmi.port=7999 \
    -Dcom.sun.management.jmxremote.local.only=false -Djava.rmi.server.hostname=192.168.122.99 -Dcom.sun.management.jmxremote.host=192.168.122.99 \
    -jar gateway-0.0.1-SNAPSHOT.jar