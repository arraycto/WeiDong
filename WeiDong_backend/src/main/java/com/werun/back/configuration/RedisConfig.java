package com.werun.back.configuration;

import com.werun.back.entity.UserEntity;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * @ClassName RedisConfig
 * @Author HWG
 * @Time 2019/4/18 23:14
 */
@Configuration
public class RedisConfig {

    @Bean("userRedisTemplate")
    public RedisTemplate<String, UserEntity> redisTemplate(RedisConnectionFactory redisConnectionFactory){
        RedisTemplate<String,UserEntity> template=new RedisTemplate<>();
        Jackson2JsonRedisSerializer<UserEntity> j=new Jackson2JsonRedisSerializer<UserEntity>(UserEntity.class);
        template.setValueSerializer(j);
        template.setHashValueSerializer(j);
        template.setKeySerializer(new StringRedisSerializer());
        template.setHashKeySerializer(new StringRedisSerializer());
        template.setConnectionFactory(redisConnectionFactory);
        return template;
    }
}
