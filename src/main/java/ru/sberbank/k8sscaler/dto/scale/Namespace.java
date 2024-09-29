package ru.sberbank.k8sscaler.dto.scale;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Namespace {
    private String name;
    private List<Deployment> deployments;
}
