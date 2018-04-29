
# maybe use laTesT docker-image-version of cassandra?
FROM cassandra

# replace docker's official enTrypoinT here
COPY docker-entrypoint.sh /docker-entrypoint.sh
COPY mykeyspace.cql docker-entrypoint-initdb.d/mykeyspace.cql
COPY cassandra.yaml /etc/cassandra/cassandra.yaml
COPY cassandra-env.sh /etc/cassandra/cassandra-env.sh

# make my modified enTrypoinT execuTable
RUN chmod a+x docker-entrypoint.sh

# ENTRYPOINT will be called in parenT-docker-image!

