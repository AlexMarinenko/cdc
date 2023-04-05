package com.gamingtec.services.cdc.service;

import static io.debezium.data.Envelope.FieldName.AFTER;
import static io.debezium.data.Envelope.FieldName.BEFORE;
import static io.debezium.data.Envelope.FieldName.OPERATION;
import static java.util.stream.Collectors.toMap;

import io.debezium.config.Configuration;
import io.debezium.data.Envelope;
import io.debezium.embedded.Connect;
import io.debezium.engine.DebeziumEngine;
import io.debezium.engine.RecordChangeEvent;
import io.debezium.engine.format.ChangeEventFormat;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.connect.data.Field;
import org.apache.kafka.connect.data.Struct;
import org.apache.kafka.connect.source.SourceRecord;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class DebeziumListener {

    private final DebeziumEngine<RecordChangeEvent<SourceRecord>> debeziumEngine;

    private final Executor executor = Executors.newSingleThreadExecutor();

    public DebeziumListener(Configuration debeziumConfiguration) {

        this.debeziumEngine = DebeziumEngine.create(ChangeEventFormat.of(Connect.class))
                .using(debeziumConfiguration.asProperties())
                .notifying(this::handleChangeEvent)
                .build();
    }

    @PostConstruct
    private void start() {
        this.executor.execute(debeziumEngine);
    }

    private void handleChangeEvent(RecordChangeEvent<SourceRecord> sourceRecordRecordChangeEvent) {

        SourceRecord sourceRecord = sourceRecordRecordChangeEvent.record();
        Struct sourceRecordChangeValue= (Struct) sourceRecord.value();

        if (sourceRecordChangeValue != null) {
            Envelope.Operation operation = Envelope.Operation.forCode((String) sourceRecordChangeValue.get(OPERATION));

            if(operation != Envelope.Operation.READ) {
                String record = operation == Envelope.Operation.DELETE ? BEFORE : AFTER;
                Struct struct = (Struct) sourceRecordChangeValue.get(record);
                Map<String, Object> payload = struct.schema().fields().stream()
                        .map(Field::name)
                        .filter(fieldName -> struct.get(fieldName) != null)
                        .map(fieldName -> Pair.of(fieldName, struct.get(fieldName)))
                        .collect(toMap(Pair::getFirst, Pair::getSecond));
                log.info("Operation: {}, Payload: {}", operation, payload);
            }
        }
    }

    @PreDestroy
    private void stop() throws IOException {
        if (this.debeziumEngine != null) {
            this.debeziumEngine.close();
        }
    }

}
