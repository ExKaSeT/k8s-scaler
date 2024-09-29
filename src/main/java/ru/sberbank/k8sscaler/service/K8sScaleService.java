package ru.sberbank.k8sscaler.service;

import io.kubernetes.client.openapi.apis.AppsV1Api;
import io.kubernetes.client.openapi.models.V1ObjectMeta;
import io.kubernetes.client.openapi.models.V1Scale;
import io.kubernetes.client.openapi.models.V1ScaleSpec;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.sberbank.k8sscaler.dto.scale.Cluster;
import ru.sberbank.k8sscaler.exception.ScaleDeploymentsException;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class K8sScaleService {

    private static final int SCALE_CLUSTER_DEPLOYMENTS_TIMEOUT_SEC = 3;

    private static final ExecutorService executor = Executors
            .newFixedThreadPool(Runtime.getRuntime().availableProcessors());

    private final K8sClientService k8sClientService;

    public void scaleDeployments(List<Cluster> clusters) {
        for (var cluster : clusters) {
            var tasks = new ArrayList<Callable<Object>>();
            var url = cluster.getUrl();
            for (var namespace : cluster.getNamespaces()) {
                var api = new AppsV1Api(k8sClientService.getApiClient(url, namespace.getName()));
                for (var deployment : namespace.getDeployments()) {
                    tasks.add(() -> {
                        var scale = new V1Scale();
                        scale.setSpec(new V1ScaleSpec().replicas(deployment.getReplicas()));
                        scale.setMetadata(new V1ObjectMeta().name(deployment.getName()));
                        api.replaceNamespacedDeploymentScale(deployment.getName(), namespace.getName(), scale).execute();
                        return null;
                    });
                }
            }
            try {
                var result = executor.invokeAll(tasks, SCALE_CLUSTER_DEPLOYMENTS_TIMEOUT_SEC, TimeUnit.SECONDS);
                for (var taskResult : result) {
                    taskResult.get();
                }
            } catch (CancellationException e) {
                throw new ScaleDeploymentsException(String
                        .format("Exceeded timeout (%d sec) scaling deployments in cluster: %s",
                                SCALE_CLUSTER_DEPLOYMENTS_TIMEOUT_SEC, url), e);
            } catch (Exception e) {
                throw new ScaleDeploymentsException(String.format("Error scaling deployments in cluster: %s", url), e);
            }
        }
    }
}
