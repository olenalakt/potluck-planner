package com.olena.guestcleanupservice.service;

import com.olena.guestcleanupservice.config.KafkaProperties;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.CommonClientConfigs;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.springframework.stereotype.Service;

import java.util.Properties;

/**
 * @author olaktyushkina
 * @since 1.0
 * Initialize kafka Properties except topic and serializer/deserializer
 */
@Slf4j
@Service
public class InitKafkaPropertiesService {

    public Properties getConsumerProperties(KafkaProperties kafkaProperties) {

        log.debug("getConsumerProperties, entered: kafkaProperties=[{}]", kafkaProperties);

        Properties consumerProperties = new Properties();
//        ClassLoader tmpLoader = kafkaProperties.getClass().getClassLoader();
//        Path tmpPath = null;

        try {
            consumerProperties.put(CommonClientConfigs.BOOTSTRAP_SERVERS_CONFIG, kafkaProperties.getBootstrapServers());
            consumerProperties.put(CommonClientConfigs.CLIENT_ID_CONFIG, kafkaProperties.getClientId());

            consumerProperties.put(ConsumerConfig.GROUP_ID_CONFIG, kafkaProperties.getGroupId());
            consumerProperties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, kafkaProperties.getKeyDeserializerClass());
            consumerProperties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, kafkaProperties.getValueDeserializerClass());

            consumerProperties.put(ConsumerConfig.FETCH_MIN_BYTES_CONFIG, kafkaProperties.getFetchMinBytes());
            consumerProperties.put(ConsumerConfig.FETCH_MAX_BYTES_CONFIG, kafkaProperties.getFetchMaxBytes());
            consumerProperties.put(ConsumerConfig.FETCH_MAX_WAIT_MS_CONFIG, kafkaProperties.getFetchMaxWaitMs());
            consumerProperties.put(ConsumerConfig.RECEIVE_BUFFER_CONFIG, kafkaProperties.getReceiveBufferBytes());
            consumerProperties.put(ConsumerConfig.MAX_PARTITION_FETCH_BYTES_CONFIG, kafkaProperties.getMaxPartitionFetchBytes());

            consumerProperties.put(ConsumerConfig.CONNECTIONS_MAX_IDLE_MS_CONFIG, kafkaProperties.getConnectionsMaxIdleMs());
            consumerProperties.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, kafkaProperties.getSessionTimeoutMs());
            consumerProperties.put(ConsumerConfig.HEARTBEAT_INTERVAL_MS_CONFIG, kafkaProperties.getHeartbeatIntervalMs());
            consumerProperties.put(ConsumerConfig.REQUEST_TIMEOUT_MS_CONFIG, kafkaProperties.getRequestTimeoutMs());

            consumerProperties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, kafkaProperties.getAutoOffsetReset());
            consumerProperties.put(ConsumerConfig.CLIENT_DNS_LOOKUP_CONFIG, kafkaProperties.getClientDnsLookup());

            consumerProperties.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, kafkaProperties.getEnableAutoCommit());
            consumerProperties.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, kafkaProperties.getAutoCommitIntervalMs());

            consumerProperties.put(ConsumerConfig.RECONNECT_BACKOFF_MAX_MS_CONFIG, kafkaProperties.getReconnectBackoffMaxMs());
            consumerProperties.put(ConsumerConfig.RECONNECT_BACKOFF_MS_CONFIG, kafkaProperties.getReconnectBackoffMs());
            consumerProperties.put(ConsumerConfig.RETRY_BACKOFF_MS_CONFIG, kafkaProperties.getRetryBackoffMs());

            consumerProperties.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, kafkaProperties.getMaxPollRecords());

/*            log.debug("getConsumerProperties, tmpPath: sslTruststoreLocation=[{}]  ", kafkaProperties.getSslTruststoreLocation());
            log.debug("getConsumerProperties, tmpLoader: tmpLoader=[{}]  ", tmpLoader);
            log.debug("getConsumerProperties, tmpLoaderResource: tmpLoader=[{}]  ", tmpLoader.getResource(kafkaProperties.getSslTruststoreLocation()).toURI());
            tmpPath = Paths.get(tmpLoader.getResource(kafkaProperties.getSslTruststoreLocation()).toURI());
            log.debug("getConsumerProperties, tmpPath=[{}]", tmpPath);

            consumerProperties.put(CommonClientConfigs.SECURITY_PROTOCOL_CONFIG, kafkaProperties.getSecurityProtocol());

            consumerProperties.put(SslConfigs.SSL_TRUSTSTORE_LOCATION_CONFIG, tmpPath.toString());
            consumerProperties.put(SslConfigs.SSL_ENDPOINT_IDENTIFICATION_ALGORITHM_CONFIG, kafkaProperties.getSslAlgorithm());
            consumerProperties.put(SslConfigs.SSL_TRUSTSTORE_PASSWORD_CONFIG, kafkaProperties.getSslTruststorePassword());

            consumerProperties.put(SaslConfigs.SASL_MECHANISM, kafkaProperties.getSaslMechanism());
            consumerProperties.put(SaslConfigs.SASL_JAAS_CONFIG, kafkaProperties.getSaslJaasConfig());*/

            log.info("getConsumerProperties, done");
        } catch (NullPointerException ex) {
            log.error("getConsumerProperties, null pointer exception occurred: [{}]", ex.toString());
//        } catch (URISyntaxException urie) {
            //          log.error("getConsumerProperties, URI Exception occurred: [{}]", urie.toString());
        }

        return consumerProperties;
    }

}
