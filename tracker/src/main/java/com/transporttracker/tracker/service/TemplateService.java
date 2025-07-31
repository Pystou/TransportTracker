package com.transporttracker.tracker.service;


import com.transporttracker.tracker.dto.TemplateDTO;

import java.util.List;


public interface TemplateService {
    TemplateDTO createTemplate(String name, String username);
    void deleteTemplate(Long id, String username);
    List<TemplateDTO> getTemplates(String username);
}
