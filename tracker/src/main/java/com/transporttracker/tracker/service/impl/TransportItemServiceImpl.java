package com.transporttracker.tracker.service.impl;

import com.transporttracker.tracker.dto.AddTransportItemRequest;
import com.transporttracker.tracker.dto.TransportItemDTO;
import com.transporttracker.tracker.entity.Template;
import com.transporttracker.tracker.entity.TransportItem;
import com.transporttracker.tracker.repository.TemplateRepository;
import com.transporttracker.tracker.repository.TransportItemRepository;
import com.transporttracker.tracker.service.TransportItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TransportItemServiceImpl implements TransportItemService {

    private final TransportItemRepository transportItemRepository;
    private final TemplateRepository templateRepository;

    @Override
    public TransportItemDTO addTransportItem(Long templateId, AddTransportItemRequest request, String username) {
        Template template = templateRepository.findById(templateId)
                .orElseThrow(() -> new RuntimeException("Template not found"));

        if (!template.getUser().getEmail().equals(username)) {
            throw new RuntimeException("Access denied");
        }

        TransportItem item = TransportItem.builder()
                .transportType(request.getTransportType())
                .transportNumber(request.getTransportNumber())
                .stopName(request.getStopName())
                .userWaitTime(request.getUserWaitTime())
                .stopId(request.getStopId())
                .stopName(request.getStopName())
                .direction(request.getDirection())
                .template(template)
                .build();

        TransportItem saved = transportItemRepository.save(item);
        return mapToDTO(saved);
    }

    @Override
    public void deleteTransportItem(Long id, String username) {
        TransportItem item = transportItemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Item not found"));

        if (!item.getTemplate().getUser().getEmail().equals(username)) {
            throw new RuntimeException("Access denied");
        }

        transportItemRepository.delete(item);
    }

    @Override
    public List<TransportItemDTO> getTransportItemsForTemplate(Long templateId, String username) {
        Template template = templateRepository.findById(templateId)
                .orElseThrow(() -> new RuntimeException("Template not found"));

        if (!template.getUser().getEmail().equals(username)) {
            throw new RuntimeException("Access denied");
        }

        return transportItemRepository.findAllByTemplate(template)
                .stream()
                .map(this::mapToDTO)
                .toList();
    }

    private TransportItemDTO mapToDTO(TransportItem item) {
        return TransportItemDTO.builder()
                .id(item.getId())
                .transportType(item.getTransportType())
                .transportNumber(item.getTransportNumber())
                .stopName(item.getStopName())
                .userWaitTime(item.getUserWaitTime())
                .stopId(item.getStopId())
                .stopName(item.getStopName())
                .direction(item.getDirection())
                .build();
    }
}
