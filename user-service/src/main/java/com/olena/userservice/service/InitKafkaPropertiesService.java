package com.olena.userservice.service;

import com.olena.userservice.config.KafkaProperties;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerConfig;
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

    public Properties getProducerProperties(KafkaProperties kafkaProperties) {

        log.debug("getProducerProperties, entered: kafkaProperties=[{}]", kafkaProperties);

        Properties producerProperties = new Properties();
        //ClassLoader tmpLoader = kafkaProperties.getClass().getClassLoader();

        try {
            producerProperties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaProperties.getBootstrapServers());

            producerProperties.put(ProducerConfig.CLIENT_ID_CONFIG, kafkaProperties.getClientId());
            producerProperties.put(ProducerConfig.CLIENT_DNS_LOOKUP_CONFIG, kafkaProperties.getClientDnsLookup());

            producerProperties.put(ProducerConfig.CONNECTIONS_MAX_IDLE_MS_CONFIG, kafkaProperties.getConnectionsMaxIdleMs());
            producerProperties.put(ProducerConfig.REQUEST_TIMEOUT_MS_CONFIG, kafkaProperties.getRequestTimeoutMs());

            producerProperties.put(ProducerConfig.RECONNECT_BACKOFF_MAX_MS_CONFIG, kafkaProperties.getReconnectBackoffMaxMs());
            producerProperties.put(ProducerConfig.RECONNECT_BACKOFF_MS_CONFIG, kafkaProperties.getReconnectBackoffMs());
            producerProperties.put(ProducerConfig.RETRY_BACKOFF_MS_CONFIG, kafkaProperties.getRetryBackoffMs());

            producerProperties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, kafkaProperties.getKeySerializerClass());

            producerProperties.put(ProducerConfig.ACKS_CONFIG, kafkaProperties.getAcks());
            producerProperties.put(ProducerConfig.LINGER_MS_CONFIG, kafkaProperties.getLingerMs());

            producerProperties.put(ProducerConfig.BUFFER_MEMORY_CONFIG, kafkaProperties.getBufferMemory());
            producerProperties.put(ProducerConfig.BATCH_SIZE_CONFIG, kafkaProperties.getBatchSize());
            producerProperties.put(ProducerConfig.DELIVERY_TIMEOUT_MS_CONFIG, kafkaProperties.getDeliveryTimeoutMs());
            producerProperties.put(ProducerConfig.MAX_BLOCK_MS_CONFIG, kafkaProperties.getMaxBlockMs());
            producerProperties.put(ProducerConfig.MAX_REQUEST_SIZE_CONFIG, kafkaProperties.getMaxRequestSize());
            producerProperties.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, kafkaProperties.getEnableIdempotence());
            producerProperties.put(ProducerConfig.MAX_IN_FLIGHT_REQUESTS_PER_CONNECTION, kafkaProperties.getMaxInFlightRequestsPerConnection());
            producerProperties.put(ProducerConfig.METADATA_MAX_AGE_CONFIG, kafkaProperties.getMetadataMaxAgeMs());
            producerProperties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, kafkaProperties.getValuePotluckPlannerCleanupSerializerCLass());

/*
            Path tmpPath = Paths.get(tmpLoader.getResource(kafkaProperties.getSslTruststoreLocation()).toURI());
            log.debug("getProducerProperties: tmpPath=[{}]  ", tmpPath);

            producerProperties.put("ssl.truststore.location", tmpPath.toString());
            producerProperties.put("security.protocol", kafkaProperties.getSecurityProtocol());
            producerProperties.put("ssl.endpoint.identification.algorithm", kafkaProperties.getSslAlgorithm());
            producerProperties.put("ssl.truststore.password", kafkaProperties.getSslTruststorePassword());
            producerProperties.put("sasl.mechanism", kafkaProperties.getSaslMechanism());
            producerProperties.put("sasl.jaas.config", kafkaProperties.getSaslJaasConfig());
            */


            log.info("getProducerProperties, done");

        } catch (NullPointerException ex) {
            log.error("getProducerProperties, null pointer exception occurred: [{}]", ex.toString());
            //       } catch (URISyntaxException urie) {
            //         log.error("getProducerProperties, URI Exception occurred: [{}]", urie.toString());
        }

        return producerProperties;
    }

}
