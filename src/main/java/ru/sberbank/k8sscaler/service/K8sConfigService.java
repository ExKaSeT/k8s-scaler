package ru.sberbank.k8sscaler.service;

import jakarta.annotation.Nullable;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.sberbank.k8sscaler.dto.scale.Cluster;
import ru.sberbank.k8sscaler.dto.scale.Deployment;
import ru.sberbank.k8sscaler.dto.scale.Group;
import ru.sberbank.k8sscaler.dto.scale.Namespace;
import ru.sberbank.k8sscaler.property.ScaleProperties;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class K8sConfigService {

    private final List<Group> groups;

    public K8sConfigService(ScaleProperties scaleProperties) {
        this.groups = this.parseScaleProperties(scaleProperties);
    }

    public List<Group> getAllGroups() {
        return groups;
    }

    @Nullable
    public Group getGroupByName(String name) {
        return groups.stream().filter(group -> group.getName().equals(name)).findFirst().orElse(null);
    }

    private List<Group> parseScaleProperties(ScaleProperties scaleProperties) {
        var groups = new ArrayList<Group>();
        for (var groupProps : scaleProperties.getGroups()) {
            var group = new Group();
            group.setName(groupProps.getName());
            var clusters = new ArrayList<Cluster>();
            group.setClusters(clusters);
            for (var clusterProps : groupProps.getClusters()) {
                var cluster = new Cluster();
                cluster.setUrl(clusterProps.getUrl());
                var namespaces = clusterProps.getNamespaces()
                        .entrySet().stream()
                        .map(e ->
                                new Namespace(e.getKey(),
                                        e.getValue().entrySet().stream()
                                                .map(d -> new Deployment(d.getKey(), d.getValue()))
                                                .collect(Collectors.toList())
                                ))
                        .collect(Collectors.toList());
                cluster.setNamespaces(namespaces);
                clusters.add(cluster);
            }
            groups.add(group);
        }
        return groups;
    }
}
