package com.gamingtec.services.cdc.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "debezium.database")
public class ConnectorConfiguration {
    private String host;
    private int port;
    private String user;
    private String password;
    private String dbname;
    private String tablesToTrack;
    private String offsetFile;
    private String historyFile;
}

