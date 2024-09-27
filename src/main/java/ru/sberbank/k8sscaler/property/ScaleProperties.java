package ru.sberbank.k8sscaler.property;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Map;

@Data
@Configuration
@ConfigurationProperties(prefix = "scale")
public class ScaleProperties {
    private List<ClusterTokens> tokens;
    private List<Group> groups;

    @Data
    public static class ClusterTokens {
        private String url;
        private Map<String, String> namespaces;
    }

    @Data
    public static class Group {
        private String name;
        private List<Cluster> clusters;
    }

    @Data
    public static class Cluster {
        private String url;
        private Map<String, Map<String, Integer>> namespaces;
    }
}
