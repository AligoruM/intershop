package com.practice.intershop.config;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.practice.intershop.model.ShowcaseItem;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.serializer.*;

import java.util.List;

@Configuration
public class CacheConfig {

    @Bean
    public RedisCacheConfiguration cacheConfiguration() {
        return RedisCacheConfiguration.defaultCacheConfig()
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer()))
                .disableCachingNullValues();
    }

    @Bean
    public ReactiveRedisTemplate<String, List<ShowcaseItem>> showcaseListRedisTemplate(ReactiveRedisConnectionFactory connectionFactory) {
        ObjectMapper objectMapper = new ObjectMapper();
        JavaType type = objectMapper.getTypeFactory().constructCollectionType(List.class, ShowcaseItem.class);
        Jackson2JsonRedisSerializer<List<ShowcaseItem>> itemsSerializer = new Jackson2JsonRedisSerializer<>(objectMapper, type);

        RedisSerializationContext.RedisSerializationContextBuilder<String, List<ShowcaseItem>> builder =
                RedisSerializationContext.newSerializationContext();
        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
        builder.key(stringRedisSerializer)
                .hashKey(stringRedisSerializer)
                .value(itemsSerializer)
                .hashValue(itemsSerializer);

        return new ReactiveRedisTemplate<>(connectionFactory, builder.build());
    }

    @Bean
    public ReactiveRedisTemplate<String, Long> showcaseCountRedisTemplate(ReactiveRedisConnectionFactory connectionFactory) {
        RedisSerializationContext.RedisSerializationContextBuilder<String, Long> builder =
                RedisSerializationContext.newSerializationContext();
        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
        GenericToStringSerializer<Long> longSerializer = new GenericToStringSerializer<>(Long.class);

        builder.key(stringRedisSerializer)
                .hashKey(stringRedisSerializer)
                .value(longSerializer)
                .hashValue(longSerializer);

        return new ReactiveRedisTemplate<>(connectionFactory, builder.build());
    }

}
