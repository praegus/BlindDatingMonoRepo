package io.praegus.bda.profileservice.adapter.data;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@EqualsAndHashCode
@Entity
@Table(name = "address")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class AddressEntity {

    @Id
    @GeneratedValue
    private long id;

    private String street;

    private String streetNumber;

    private String postalCode;

    private String city;

    private boolean valid;

    private BigDecimal longitude;

    private BigDecimal latitude;
}
