# PostgreSQL image with pgvector extension built from source.
# Uses the official postgres image as base and compiles pgvector against it,
# rather than using the pre-built pgvector/pgvector image.

FROM postgres:17
FROM pgvector/pgvector:pg17

# Install build tools and PostgreSQL server dev headers needed to compile pgvector
RUN apt-get update && \
    apt-get install -y --no-install-recommends \
        build-essential \
        git \
        postgresql-server-dev-17 \
        ca-certificates && \
    git clone --branch v0.8.0 https://github.com/pgvector/pgvector.git /tmp/pgvector && \
    cd /tmp/pgvector && \
    make && \
    make install && \
    cd / && \
    rm -rf /tmp/pgvector && \
    apt-get purge -y --auto-remove build-essential git && \
    apt-get clean && \
    rm -rf /var/lib/apt/lists/*

# Automatically enable the extension on first container start
COPY 01-init-pgvector.sql /docker-entrypoint-initdb.d/
RUN chmod 644 /docker-entrypoint-initdb.d/01-init-pgvector.sql
