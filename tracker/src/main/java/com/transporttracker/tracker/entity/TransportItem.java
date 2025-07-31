package com.transporttracker.tracker.entity;


import jakarta.persistence.*;
import lombok.*;


@Entity
@Table(name = "transport_items")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransportItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String transportType;
    private String transportNumber;
    private String stopName;

    private String stopId;
    private Integer direction;

    private Integer userWaitTime;

    @ManyToOne
    @JoinColumn(name = "template_id")
    private Template template;
}
