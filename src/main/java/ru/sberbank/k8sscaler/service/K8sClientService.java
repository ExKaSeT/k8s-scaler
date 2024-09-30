package ru.sberbank.k8sscaler.service;

import io.kubernetes.client.openapi.ApiClient;
import io.kubernetes.client.util.Config;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.sberbank.k8sscaler.property.ScaleProperties;

import java.util.HashMap;
import java.util.Map;

import static java.util.Objects.isNull;

@Service
@Slf4j
public class K8sClientService {

    private final Map<ClusterNamespace, ApiClient> clusterNamespaceApiClient = new HashMap<>();

    public K8sClientService(ScaleProperties scaleProperties) {
        for (var clusterProperties : scaleProperties.getTokens()) {
            for (var namespaceToken : clusterProperties.getNamespaces().entrySet()) {
                var namespace = namespaceToken.getKey();
                var token = namespaceToken.getValue();
                var apiClient = Config.fromToken(clusterProperties.getUrl(), token, false);
                clusterNamespaceApiClient.put(new ClusterNamespace(clusterProperties.getUrl(), namespace), apiClient);
            }
        }
    }

    public ApiClient getApiClient(String url, String namespace) {
        var clusterNamespace = new ClusterNamespace(url, namespace);
        var apiClient = clusterNamespaceApiClient.get(clusterNamespace);
        if (isNull(apiClient)) {
            synchronized (clusterNamespaceApiClient) {
                apiClient = clusterNamespaceApiClient.getOrDefault(clusterNamespace,
                        Config.fromUrl(url, false));
                clusterNamespaceApiClient.put(clusterNamespace, apiClient);
                log.info(String.format("Не найдено токена для url: %s, namespace: %s; Создан клиент без токена", url, namespace));
            }
        }
        return apiClient;
    }

    @Data
    @AllArgsConstructor
    private static class ClusterNamespace {
        private String url;
        private String namespace;
    }
}
