package ru.sberbank.k8sscaler.dto.scale;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Deployment {
    private String name;
    private int replicas;
}
