package com.transporttracker.tracker.dto;


import lombok.*;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StopDTO {
    private String name;
    private String stopId;
    private int direction;

    @Override
    public String toString() {
        return "StopDTO{" +
                "name='" + name + '\'' +
                ", stopId='" + stopId + '\'' +
                ", direction=" + direction +
                '}';
    }
}
