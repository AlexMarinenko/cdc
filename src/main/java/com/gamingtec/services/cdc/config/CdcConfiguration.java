package com.gamingtec.services.cdc.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class CdcConfiguration {

    private final ConnectorConfiguration configuration;

    @Bean
    public io.debezium.config.Configuration customerConnector() {
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
                .with("database.include.list", "bamboo_app2_auto_test")
                .with("include.schema.changes", "false")
                .with("database.server.id", "10181")
                .with("database.server.name", "omega-mssql-db-server")
                .with("database.history", "io.debezium.relational.history.FileDatabaseHistory")
                .with("database.history.file.filename", "/tmp/dbhistory.dat")
                .build();
    }

}
