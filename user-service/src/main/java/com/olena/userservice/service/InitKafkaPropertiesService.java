package com.olena.userservice.service;

import com.olena.userservice.config.KafkaProperties;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
            producerProperties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, kafkaProperties.getValueSerializerCLass());

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

/*
    public int print_missing(int[] num) {

        int n = num.length;
        boolean isFound;

        for (int i = 0; i <= n; i++) {
            isFound = false;
            for (int k = 0; k < n; k++) {
                if (num[k] == i) {
                    isFound = true;
                    break;
                }
            }
            if (!isFound) {
                return i;
            }
        }
        return -1;
    }

    public int print_missing2(int[] num) {


        int n = num.length;
        int min_idx = 0;
        int tmp;

        // sort first -  O(N logN)
        for (int i = 0; i < n - 1; i++) {

            // Find the minimum element in unsorted array
            min_idx = i;
            for (int j = i + 1; j < n - 1; j++) {
                if (num[j] < num[min_idx]) {
                    min_idx = j;
                }
            }
            // swap values
            if (min_idx != i){
                tmp = num[min_idx];
                num[min_idx] = num[i];
                num[i] = tmp;
            }
        }

        // find missing -  O(N)
        for (int i = 0; i < n - 2; i++) {
            if (num[i+1] - num[i]  > 1) {
                return num[i] + 1;
            }
        }

        return -1;
    }

    @Test
    void print_missingTest() {
        int[] num1 = {3, 0, 1};
        int[] num2 = {};

        assertEquals(2, print_missing(num1));
        assertEquals(-1, print_missing(num2));
    }


    private boolean check(char[] s, char d) {

        for (int i = 0;  i < s.length; i++) {
            if (s[i] ==  d) {
                return true;
            }
        }

        return false;
    }


    private char[] sort(char[] s, int start_pos, int end_pos, char[] r) {

        char tmp;
        int min_pos;
        char[] res = r;


        for (int i = start_pos;  i < end_pos; i++) {
            min_pos = i;
            for (int j= i+1;  j < end_pos; j++) {
                if (s[j] < s[min_pos]) {
                    min_pos = j;
                }
            }

            res[end_pos - start_pos - 1] = s[min_pos];
        }

        return res;
    }

    private boolean equal(char[] s, char[] d) {

        for (int i = 0;  i < s.length; i++) {
            if (s[i] !=  d[i]) {
                return false;
            }
        }

        return true;
    }

    public List<int> search_permutations(char[] b,  char[] s) {

        // sort s
        char[] ss =  sort(s, 0, s.length, s);
        List<Integer> intList = new ArrayList<>();

        for (int i = 0;  i < b.length; i++) {
            if (check(s, b[i])) {
                if ( equal ( ss, sort(b, i, i + s.length,for  ss)) {
                    intList.add(i);
                }
            }
        }

    }

    @Test
    public void search_permutationsTest() {

        char[] b = {'d','a','d','c','d','e','f','c','d','a','a','a','d','c','d'};
        char[] s = {'a','d','c','d'};
        List<Integer> intList = Arrays.asList(1, 2, 12);

        assertEquals(intList, search_permutations(b, s));

    }*/

}
