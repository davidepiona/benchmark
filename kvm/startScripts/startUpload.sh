mount -t 9p -o trans=virtio,version=9p2000.L,rw media /opt/media
java -Dspring.profiles.active=kvm -Dcom.sun.management.jmxremote -Dcom.sun.management.jmxremote.authenticate=false \
    -Dcom.sun.management.jmxremote.ssl=false -Dcom.sun.management.jmxremote.port=7020 -Dcom.sun.management.jmxremote.rmi.port=7020 \
    -Dcom.sun.management.jmxremote.local.only=false -Djava.rmi.server.hostname=192.168.122.82 -Dcom.sun.management.jmxremote.host=192.168.122.82 \
    -jar upload-0.0.1-SNAPSHOT.jar