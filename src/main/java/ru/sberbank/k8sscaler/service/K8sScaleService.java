package ru.sberbank.k8sscaler.service;

import io.kubernetes.client.openapi.ApiClient;
import io.kubernetes.client.openapi.apis.AppsV1Api;
import io.kubernetes.client.openapi.models.V1ObjectMeta;
import io.kubernetes.client.openapi.models.V1Scale;
import io.kubernetes.client.openapi.models.V1ScaleSpec;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.sberbank.k8sscaler.exception.ScaleDeploymentsException;

import java.util.Map;
import java.util.concurrent.*;

@Service
@Slf4j
public class K8sScaleService {

    private static final int SCALE_DEPLOYMENTS_TIMEOUT_SEC = 3;

    private static final ExecutorService executor = Executors
            .newFixedThreadPool(Runtime.getRuntime().availableProcessors());

    public void scaleDeployments(Map<String, Integer> deploymentReplicas, String namespace, ApiClient client) {
        var api = new AppsV1Api(client);
        var tasks = deploymentReplicas.entrySet()
                .stream().map(e -> (Callable<Object>) () -> {
                    var name = e.getKey();
                    var replicas = e.getValue();
                    var scale = new V1Scale();
                    scale.setSpec(new V1ScaleSpec().replicas(replicas));
                    scale.setMetadata(new V1ObjectMeta().name(name));
                    api.replaceNamespacedDeploymentScale(name, namespace, scale).execute();
                    return null;
                }).toList();
        try {
            var result = executor.invokeAll(tasks, SCALE_DEPLOYMENTS_TIMEOUT_SEC, TimeUnit.SECONDS);
            for (var taskResult : result) {
                taskResult.get();
            }
        } catch (Exception e) {
            throw new ScaleDeploymentsException("Scaling deployments error", e);
        }
    }
}
