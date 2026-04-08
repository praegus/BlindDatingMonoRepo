package io.praegus.bda.profileservice.adapter.data;

import jakarta.persistence.*;
import lombok.*;

@EqualsAndHashCode
@Entity
@Table(name = "preferences")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class PreferencesEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "username")
    private String username;

    @Column(name = "gender")
    private String gender;

    @Column(name = "favorite_color")
    private String favoriteColor;

    @Column(name = "pets")
    private boolean pets;

    @Column(name = "hair_color")
    private String hairColor;

    @Column(name = "tattoos")
    private boolean tattoos;

    @Column(name = "sports")
    private String sports;

    @Column(name = "music_genres")
    private String musicGenres;
}
