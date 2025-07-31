package com.transporttracker.tracker.controller;


import com.transporttracker.tracker.dto.CreateTemplateRequest;
import com.transporttracker.tracker.dto.TemplateDTO;
import com.transporttracker.tracker.service.TemplateService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/templates")
@RequiredArgsConstructor
public class TemplateController {

    private final TemplateService templateService;

    @PostMapping
    public ResponseEntity<TemplateDTO> createTemplate(@RequestBody CreateTemplateRequest request,
                                                      @AuthenticationPrincipal UserDetails user) {
        return ResponseEntity.ok(templateService.createTemplate(request.getName(), user.getUsername()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTemplate(@PathVariable Long id,
                                               @AuthenticationPrincipal UserDetails user) {
        templateService.deleteTemplate(id, user.getUsername());
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<TemplateDTO>> getTemplates(@AuthenticationPrincipal UserDetails user) {
        return ResponseEntity.ok(templateService.getTemplates(user.getUsername()));
    }
}
