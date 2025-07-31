package com.transporttracker.tracker.dto;


import lombok.*;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddTransportItemRequest {
    private String transportType;
    private String transportNumber;
    private String stopName;
    private String stopId;
    private Integer direction;
    private Integer userWaitTime;
}
