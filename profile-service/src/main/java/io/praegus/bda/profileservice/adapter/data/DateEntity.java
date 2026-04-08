package io.praegus.bda.profileservice.adapter.data;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

@Entity
@Table(name = "profile_date")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class DateEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "address_street")
    private String addressStreet;

    @Column(name = "address_street_number")
    private String addressStreetNumber;

    @Column(name = "address_postal_code")
    private String addressPostalCode;

    @Column(name = "address_city")
    private String addressCity;

    @Column(name = "date_time")
    private ZonedDateTime dateTime;

    @Column(name = "item_to_bring")
    private String itemToBring;
}


