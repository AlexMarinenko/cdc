package com.gamingtec.services.cdc;

import com.gamingtec.services.cdc.config.ConnectorConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@EnableConfigurationProperties(ConnectorConfiguration.class)
@SpringBootApplication
public class CdcApplication {

    public static void main(String[] args) {
        SpringApplication.run(CdcApplication.class, args);
    }

}
