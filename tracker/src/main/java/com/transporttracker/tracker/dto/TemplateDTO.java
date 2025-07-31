package com.transporttracker.tracker.dto;


import lombok.*;

import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TemplateDTO {
    private Long id;
    private String name;
    private List<TransportItemDTO> transportItems;
}

