package com.olena.eventservice.config;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "confluent-kafka.config")
@Data
@NoArgsConstructor
@Slf4j
public class KafkaProperties {

    // producer props
    private String potluckEventProducerTopic;
    private String keySerializerClass;
    private String valuePotluckEventSerializerCLass;

    // Common props
    private String bootstrapServers;

    // app clientId
    private String clientId;

    private String connectionsMaxIdleMs;
    private String requestTimeoutMs;

    private String clientDnsLookup;

    private String reconnectBackoffMs;
    private String reconnectBackoffMaxMs;
    private String retryBackoffMs;

    // consumer props
/*
    private String groupId;
    private String keyDeserializerClass;
    private String valueDeserializerClass;

    private String fetchMinBytes;
    private String fetchMaxBytes;
    private String fetchMaxWaitMs;
    private String receiveBufferBytes;
    private String maxPartitionFetchBytes;

    private String sessionTimeoutMs;
    private String heartbeatIntervalMs;

    private String autoOffsetReset;

    private String enableAutoCommit;
    private String autoCommitIntervalMs;

    private String maxPollRecords;
    private String pollFrequencyMs;
    */

    // producer props
    private String acks;
    private String lingerMs;

    private String bufferMemory;
    private String batchSize;
    private String deliveryTimeoutMs;
    private String maxBlockMs;
    private String maxRequestSize;
    private String enableIdempotence;
    private String maxInFlightRequestsPerConnection;
    private String metadataMaxAgeMs;

    //shared props
/*
    private String securityProtocol;
    private String sslAlgorithm;
    private String sslTruststoreLocation;
    private String sslTruststorePassword;
    private String saslMechanism;
    private String saslJaasConfig;
    */

}
