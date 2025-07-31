package site.ahzx.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {

    /**
     * 配置 RedisTemplate 并设置自定义的序列化器，以支持 Java 8 时间类型 (如 LocalDateTime)
     * 和更灵活的对象序列化。
     *
     * @param redisConnectionFactory Redis 连接工厂
     * @return 配置好的 RedisTemplate<String, Object>
     */
    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactory);

        // --- 配置 ObjectMapper ---
        ObjectMapper objectMapper = new ObjectMapper();

        // 1. 注册 JavaTimeModule 以支持 LocalDateTime 等 Java 8 时间类型
        objectMapper.registerModule(new JavaTimeModule());

        // 2. (可选) 设置序列化可见性，例如序列化所有字段（包括非 public）
        // 如果你的实体类字段是 private 且没有 getter，需要这个
        // objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);

        // 3. (可选但推荐) 配置类型信息，以便在反序列化时知道具体类型
        // 这对于存储 List<Object> 或多态对象很有用
        // LaissezFaireSubTypeValidator 允许所有子类型，根据安全需求可调整
        objectMapper.activateDefaultTyping(
                LaissezFaireSubTypeValidator.instance,
                ObjectMapper.DefaultTyping.NON_FINAL // 或者使用 ObjectMapper.DefaultTyping.EVERYTHING
        );
        // 将类型信息存储在 "@class" 属性中 (这是 Jackson 的默认行为之一)
        objectMapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL); // 已弃用，但有时仍需显式启用
        // objectMapper.addMixIn(Object.class, ObjectMixin.class); // 可用 MixIn 精确控制类型信息属性名

        // 4. (可选) 禁用将日期写为时间戳，使其序列化为 ISO 格式字符串 (如 "2023-10-27T10:15:30")
        // objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        // --- 配置序列化器 ---
        // 使用配置好的 ObjectMapper 创建 Jackson2JsonRedisSerializer
        // 注意：Jackson2JsonRedisSerializer 需要指定根类型，这里用 Object.class
        Jackson2JsonRedisSerializer<Object> serializer = new Jackson2JsonRedisSerializer<>(objectMapper, Object.class);

        // --- 设置 RedisTemplate 的序列化规则 ---
        template.setKeySerializer(new StringRedisSerializer());
        template.setHashKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(serializer);
        template.setHashValueSerializer(serializer);

        template.afterPropertiesSet(); // 确保模板初始化完成
        return template;
    }

    // --- 可选的 MixIn 类，用于更精确控制类型信息 ---
    // @JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, property = "@class")
    // public static class ObjectMixin {}
}
