package io.praegus.bda.profileservice.adapter.data;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

@Entity
@Table (name = "date")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class DateEntity {

    @Id
    @GeneratedValue
    private long id;

    private String addressStreet;

    private String addressStreetNumber;

    private String addressPostalCode;

    private String addressCity;

    private ZonedDateTime dateTime;

    private String itemToBring;


}


