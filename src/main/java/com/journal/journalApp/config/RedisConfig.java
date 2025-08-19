package com.journal.journalApp.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;

@Configuration
public class RedisConfig {

    @Value("${spring.data.redis.host:localhost}")
    private String redisHost;

    @Value("${spring.data.redis.port:6379}")
    private int redisPort;

    @Value("${spring.data.redis.password:}")
    private String redisPassword;

    @Value("${spring.data.redis.ssl.enabled:false}")
    private boolean sslEnabled;

    @Value("${spring.data.redis.url:}")
    private String redisUrl;

    @Bean
    @ConditionalOnProperty(name = "spring.data.redis.host")
    public RedisConnectionFactory redisConnectionFactory() {
        RedisStandaloneConfiguration config = new RedisStandaloneConfiguration();

        // If Redis URL is provided, parse it
        if (redisUrl != null && !redisUrl.isEmpty()) {
            // Parse redis://default:password@host:port
            String url = redisUrl.replace("redis://", "");
            String[] parts = url.split("@");
            if (parts.length == 2) {
                String[] auth = parts[0].split(":");
                String[] hostPort = parts[1].split(":");

                if (auth.length >= 2) {
                    config.setPassword(auth[1]); // password
                }
                if (hostPort.length >= 2) {
                    config.setHostName(hostPort[0]); // host
                    config.setPort(Integer.parseInt(hostPort[1])); // port
                }
            }
        } else {
            // Fallback to individual properties
            config.setHostName(redisHost);
            config.setPort(redisPort);

            if (redisPassword != null && !redisPassword.isEmpty()) {
                config.setPassword(redisPassword);
            }
        }

        LettuceConnectionFactory factory = new LettuceConnectionFactory(config);
        factory.setTimeout(10000); // 10 seconds timeout for cloud connections
        factory.setShareNativeConnection(false);
        factory.setValidateConnection(true);

        return factory;
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(connectionFactory);

        // Use String serializers for keys and values
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new StringRedisSerializer());
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashValueSerializer(new StringRedisSerializer());

        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }
}
