package com.transporttracker.tracker.service.impl;

import com.transporttracker.tracker.dto.TemplateDTO;
import com.transporttracker.tracker.dto.TransportItemDTO;
import com.transporttracker.tracker.entity.Template;
import com.transporttracker.tracker.entity.TransportItem;
import com.transporttracker.tracker.entity.UserEntity;
import com.transporttracker.tracker.repository.TemplateRepository;
import com.transporttracker.tracker.repository.UserRepository;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import com.transporttracker.tracker.service.TemplateService;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@RequiredArgsConstructor
public class TemplateServiceImpl implements TemplateService {

    private final TemplateRepository templateRepository;
    private final UserRepository userRepository;
    //private final TransportItemRepository transportItemRepository;

    @Override
    public TemplateDTO createTemplate(String name, String email) {
        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Template template = Template.builder()
                .name(name)
                .user(user)
                .build();

        Template saved = templateRepository.save(template);
        saved = templateRepository.findById(saved.getId()).orElseThrow();
        return mapToDTO(saved);
    }

    @Override
    @Transactional
    public void deleteTemplate(Long id, String email) {
        Template template = templateRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Template not found"));

        if (!template.getUser().getEmail().equals(email)) {
            throw new RuntimeException("Access denied");
        }

        templateRepository.delete(template);
    }

    @Override
    public List<TemplateDTO> getTemplates(String email) {
        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return templateRepository.findAllByUser(user)
                .stream()
                .map(this::mapToDTO)
                .toList();
    }

    private TemplateDTO mapToDTO(Template template) {
        return TemplateDTO.builder()
                .id(template.getId())
                .name(template.getName())
                .transportItems(
                        template.getTransportItems().stream()
                                .map(this::mapItemToDTO)
                                .toList()
                )
                .build();
    }

    private TransportItemDTO mapItemToDTO(TransportItem item) {
        return TransportItemDTO.builder()
                .id(item.getId())
                .transportType(item.getTransportType())
                .transportNumber(item.getTransportNumber())
                .stopName(item.getStopName())
                .userWaitTime(item.getUserWaitTime())
                .stopId(item.getStopId())
                .stopName(item.getStopName())
                .build();
    }
}
