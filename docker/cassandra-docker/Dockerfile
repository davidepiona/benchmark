
# maybe use laTesT docker-image-version of cassandra?
FROM cassandra

# replace docker's official enTrypoinT here
COPY docker-entrypoint.sh /docker-entrypoint.sh
COPY mykeyspace.cql docker-entrypoint-initdb.d/mykeyspace.cql

# make my modified enTrypoinT execuTable
RUN chmod a+x docker-entrypoint.sh

# ENTRYPOINT will be called in parenT-docker-image!

