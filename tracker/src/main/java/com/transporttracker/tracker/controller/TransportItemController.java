package com.transporttracker.tracker.controller;


import com.transporttracker.tracker.dto.AddTransportItemRequest;
import com.transporttracker.tracker.dto.TransportItemDTO;
import com.transporttracker.tracker.service.TransportItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/transport")
@RequiredArgsConstructor
public class TransportItemController {

    private final TransportItemService transportItemService;

    @PostMapping("/{templateId}")
    public ResponseEntity<TransportItemDTO> addTransportItem(@PathVariable Long templateId,
                                                             @RequestBody AddTransportItemRequest request,
                                                             @AuthenticationPrincipal UserDetails user) {
        return ResponseEntity.ok(transportItemService.addTransportItem(templateId, request, user.getUsername()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTransportItem(@PathVariable Long id,
                                                    @AuthenticationPrincipal UserDetails user) {
        transportItemService.deleteTransportItem(id, user.getUsername());
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{templateId}")
    public ResponseEntity<List<TransportItemDTO>> getTransportItems(@PathVariable Long templateId,
                                                                    @AuthenticationPrincipal UserDetails user) {
        return ResponseEntity.ok(transportItemService.getTransportItemsForTemplate(templateId, user.getUsername()));
    }
}
