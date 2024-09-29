package ru.sberbank.k8sscaler.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.sberbank.k8sscaler.dto.SuccessContainerDto;
import ru.sberbank.k8sscaler.dto.scale.Cluster;
import ru.sberbank.k8sscaler.dto.scale.Group;
import ru.sberbank.k8sscaler.service.K8sConfigService;

import java.util.List;

@Controller
@RequestMapping("/")
@RequiredArgsConstructor
public class GroupController {

    private final K8sConfigService k8sConfigService;

    @GetMapping("/")
    public String showGroups(Model model) {
        var groups = k8sConfigService.getAllGroups();
        model.addAttribute("groups", groups);
        return "group-list";
    }

    @GetMapping("/group/{groupName}")
    public String showGroupDetails(@PathVariable String groupName, Model model) {
        Group group = k8sConfigService.getGroupByName(groupName);
        model.addAttribute("group", group);
        return "group-detail";
    }

    @PostMapping("/group/{groupName}/scale")
    @ResponseBody
    public SuccessContainerDto scaleGroup(@PathVariable String groupName, @RequestBody List<Cluster> clusters) {

        return SuccessContainerDto.success();
    }
}
