package ru.sberbank.k8sscaler.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.sberbank.k8sscaler.dto.SuccessContainerDto;
import ru.sberbank.k8sscaler.dto.scale.Cluster;
import ru.sberbank.k8sscaler.service.K8sScaleService;

import java.util.List;

@RestController
@RequestMapping("/deployment")
@RequiredArgsConstructor
public class DeploymentController {

    private final K8sScaleService k8sScaleService;

    @PostMapping("/scale")
    public SuccessContainerDto scaleGroup(@RequestBody List<Cluster> clusters) {
        k8sScaleService.scaleDeployments(clusters);
        return SuccessContainerDto.success();
    }
}
