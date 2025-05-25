package io.praegus.bda.profileservice.adapter.data;

import jakarta.persistence.*;
import lombok.*;

@EqualsAndHashCode
@Entity
@Table(name = "personalInformation")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class PersonalInformationEntity {

    @Id
    @GeneratedValue
    private long id;

    @Column(name = "username")
    private String username;

    @Column (name = "gender")
    private String gender;

    @Column (name = "favoriteColor")
    private String favoriteColor;

    @Column (name = "pets")
    private boolean pets;

    @Column (name = "hairColor")
    private String hairColor;

    @Column (name = "tattoos")
    private boolean tattoos;

    @Column (name = "sports")
    private String sports;

    @Column (name = "musicGenres")
    private String musicGenres;
}
