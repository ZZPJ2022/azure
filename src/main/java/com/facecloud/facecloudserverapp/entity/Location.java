package com.facecloud.facecloudserverapp.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.relational.core.mapping.Table;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Entity
@Table
@Getter
@Setter
public class Location {

    @Id
    @GeneratedValue
    private Long id;

    private String userName;
    private double latitude;
    private double longitude;
    private LocalDateTime time;

    public Location() {
    }

    public Location(Long id, String userName, double latitude, double longitude, LocalDateTime time) {
        this.id = id;
        this.userName = userName;
        this.latitude = latitude;
        this.longitude = longitude;
        this.time = time;
    }

}
