package com.transporttracker.tracker.service;


import com.transporttracker.tracker.dto.AddTransportItemRequest;
import com.transporttracker.tracker.dto.TransportItemDTO;

import java.util.List;

public interface TransportItemService {
    TransportItemDTO addTransportItem(Long templateId, AddTransportItemRequest request, String username);
    void deleteTransportItem(Long id, String username);
    List<TransportItemDTO> getTransportItemsForTemplate(Long templateId, String username);
}