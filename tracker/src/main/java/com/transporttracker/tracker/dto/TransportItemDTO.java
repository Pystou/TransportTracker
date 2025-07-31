package com.transporttracker.tracker.dto;


import lombok.*;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransportItemDTO {
    private Long id;
    private String transportType;
    private String transportNumber;
    private String stopName;
    private Integer userWaitTime;
    private String stopId;
    private Integer direction;
}


