package site.ahzx.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
@ConfigurationProperties(prefix = "security")
@Data
public class SecurityUrlProperties {
    private List<String> whitelist;
    private List<String> blacklist;
    private List<RoleMapping> roleMappings;


    @Data
    public static class RoleMapping {
        private String pattern;
        private String role;
    }
}
