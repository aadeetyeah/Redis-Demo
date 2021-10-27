package com.procrastinator.jbdl19.RedisDemo;

//Class to define all Beans required for our project.

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisClientConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnection;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Service;

@Configuration //Using this Annotation so that Bean of this class will be created by Spring.
public class Config {

    @Bean   //This Bean is to get connected to a Redis CLient.
    public LettuceConnectionFactory getRedisFactory(){

        //While creating this object even if we don't pass hostName and port number its Okay.
        //Its default constructor default values are localhost and port=6379 only.
        //So no worries.
        //If we have password to connect with Redis we can specify that as well with setPassword() method.


        /* RedisStandaloneConfiguration redisStandaloneConfiguration=new RedisStandaloneConfiguration(
                "localhost",6379);      LOCAL Redis Demo */

        //REDIS CLOUD DEMO
        RedisStandaloneConfiguration redisStandaloneConfiguration=new RedisStandaloneConfiguration(
                "redis-12763.c264.ap-south-1-1.ec2.cloud.redislabs.com",12763);
        redisStandaloneConfiguration.setPassword("REDIS-CLOUD_PASSWORD");
        //redis-12763.c264.ap-south-1-1.ec2.cloud.redislabs.com:12763
        //In REDIS CLOUD after ':' the number is port i.e. 12763

        LettuceConnectionFactory lettuceConnectionFactory=
                new LettuceConnectionFactory(redisStandaloneConfiguration);
        return lettuceConnectionFactory;
    }

    @Bean
    public RedisTemplate getTemplate(){
        //If we take KEY as other than String data type for e.g. Integer.
        //It might get converted into String at Redis Level
        //Cuz Redis only supports String.

        //RedisTemplate<String,Person> redisTemplate=new RedisTemplate<>();

        RedisTemplate<String,Object> redisTemplate=new RedisTemplate<>();
       // redisTemplate.setValueSerializer();
        redisTemplate.setHashValueSerializer(new JdkSerializationRedisSerializer());
        redisTemplate.setConnectionFactory(getRedisFactory());
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        return redisTemplate;
    }

    @Bean
    public ObjectMapper getMapper(){
        return new ObjectMapper();
    }

}
