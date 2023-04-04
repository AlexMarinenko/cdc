package com.gamingtec.services.cdc.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "debezium.database")
public class ConnectorConfiguration {
    private String host;
    private String user;
    private String password;
    private String dbname;
}

