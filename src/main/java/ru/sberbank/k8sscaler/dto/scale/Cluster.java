package ru.sberbank.k8sscaler.dto.scale;

import lombok.Data;
import java.util.List;

@Data
public class Cluster {
    private String url;
    private List<Namespace> namespaces;
}
