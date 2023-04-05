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
                .with("name", "omega-mssql-connector")
                .with("connector.class", "io.debezium.connector.sqlserver.SqlServerConnector")
                .with("offset.storage", "org.apache.kafka.connect.storage.FileOffsetBackingStore")
                .with("offset.storage.file.filename", "/tmp/offsets.dat")
                .with("offset.flush.interval.ms", "60000")
                .with("database.hostname", configuration.getHost())
                //.with("database.port", customerDbPort)
                .with("database.user", configuration.getUser())
                .with("database.password", configuration.getPassword())
                .with("database.dbname", configuration.getDbname())
                .with("database.include.list", configuration.getDbname())
                .with("table.include.list", "admin_all.REGISTRY_HASH")
                .with("database.names", configuration.getDbname())
                .with("database.encrypt", "false")
                .with("topic.prefix", "omega")
                .with("include.schema.changes", "false")
                .with("database.server.id", "10181")
                .with("database.server.name", "omega-mssql-db-server")
                .with("database.history", "io.debezium.relational.history.FileDatabaseHistory")
                .with("database.history.file.filename", "/tmp/dbhistory.dat")

                .with("schema.history.internal.kafka.topic", "history-topic")
                .with("schema.history.internal.kafka.bootstrap.servers", "localhost:9092")
                .build();
    }

}
