package com.gamingtec.services.cdc.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class CdcConfiguration {

    private final ConnectorConfiguration configuration;

    @Bean
    public io.debezium.config.Configuration customerConnector() {
        log.info("CDC Configuration: {}", configuration );
        return io.debezium.config.Configuration.create()
                .with("connector.class", "io.debezium.connector.sqlserver.SqlServerConnector")
                .with("offset.storage", "org.apache.kafka.connect.storage.FileOffsetBackingStore")
                .with("offset.storage.file.filename", configuration.getOffsetFile())
                .with("database.hostname", configuration.getHost())
                .with("database.port", configuration.getPort())
                .with("database.user", configuration.getUser())
                .with("database.password", configuration.getPassword())
                .with("database.dbname", configuration.getDbname())
                .with("table.include.list", configuration.getTablesToTrack())
                .with("table.whitelist", configuration.getTablesToTrack())
                .with("database.names", configuration.getDbname())
                .with("schema.history.internal", "io.debezium.storage.file.history.FileSchemaHistory")
                .with("schema.history.internal.file.filename", configuration.getHistoryFile())
                .with("topic.prefix", "omega")
                .with("snapshot.mode", "schema_only")

                // May be worth to be moved to configuration
                .with("name", "omega-mssql-connector")
                .with("offset.flush.interval.ms", "60000")
                .with("include.schema.changes", "false")
                .with("database.server.id", "10181")
                .with("database.server.name", "omega_mssql_db_server")

                // Disable encryption
                .with("database.encrypt", "false")

                // Certificates settings if encryption is on
                // -----------------------------------------
                // .with("database.ssl.truststore": "path/to/trust-store")
                // .with("database.ssl.truststore.password": "password-for-trust-store")

                // For newer versions of debezium. Kafka engine only.
                // --------------------------------------------------
                //.with("topic.prefix", "omega")
                //.with("schema.history.internal.kafka.topic", "history-topic")
                //.with("schema.history.internal.kafka.bootstrap.servers", "localhost:9092")

                .build();
    }

}
