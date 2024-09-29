package ru.sberbank.k8sscaler.dto.scale;

import lombok.Data;

import java.util.List;

@Data
public class Group {
    private String name;
    private List<Cluster> clusters;
}
