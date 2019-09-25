package org.pk.com.config;

import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.net.InetAddress;
import java.net.UnknownHostException;


@Configuration
@EnableJpaRepositories(basePackages = "org.pk.com.repository") //Registering Jpa Repositories
@EnableElasticsearchRepositories(basePackages = "org.pk.com.repository.search") //Registering Elastic Search Repositories
public class ElasticConfig {

    private static final Logger LOG = LoggerFactory.getLogger(ElasticConfig.class);

    @Value("${elasticsearch.host:localhost}")
    public String host;

    @Value("${elasticsearch.port:9300}")
    public int port;

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    @Bean
    public Client client() {
        TransportClient client = null;
        try {
            LOG.debug("host:" + host + "port:" + port);
            client = new PreBuiltTransportClient(Settings.EMPTY)
                    .addTransportAddress(new TransportAddress(InetAddress.getByName(host), port));
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return client;
    }
}
