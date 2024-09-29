package ru.sberbank.k8sscaler.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.sberbank.k8sscaler.dto.scale.Group;
import ru.sberbank.k8sscaler.service.K8sConfigService;

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
}
