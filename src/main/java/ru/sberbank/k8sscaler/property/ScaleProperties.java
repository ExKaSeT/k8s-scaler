package ru.sberbank.k8sscaler.property;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import org.hibernate.validator.constraints.UniqueElements;

import java.util.List;
import java.util.Map;

@Data
public class ScaleProperties {
    @UniqueElements
    private List<ClusterTokens> tokens;
    @UniqueElements
    private List<Group> groups;

    @Data
    public static class ClusterTokens {
        @NotBlank
        private String url;
        private Map<@NotBlank String, @NotBlank String> namespaces;

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            ClusterTokens that = (ClusterTokens) o;

            return url.equals(that.url);
        }

        @Override
        public int hashCode() {
            return url.hashCode();
        }
    }

    @Data
    public static class Group {
        @NotBlank
        @Pattern(regexp = "^[a-zA-Z0-9_-]+$")
        private String name;
        @NotEmpty
        @UniqueElements
        private List<Cluster> clusters;

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Group group = (Group) o;

            return name.equals(group.name);
        }

        @Override
        public int hashCode() {
            return name.hashCode();
        }
    }

    @Data
    public static class Cluster {
        @NotBlank
        private String url;
        @NotEmpty
        private Map<@NotBlank String, Map<@NotBlank String, @NotNull Integer>> namespaces;

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Cluster cluster = (Cluster) o;

            return url.equals(cluster.url);
        }

        @Override
        public int hashCode() {
            return url.hashCode();
        }
    }
}
