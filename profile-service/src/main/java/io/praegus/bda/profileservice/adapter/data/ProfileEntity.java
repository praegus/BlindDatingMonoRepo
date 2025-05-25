package io.praegus.bda.profileservice.adapter.data;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table (name = "profile")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ProfileEntity {
    @Id
    @Column (name = "username")
    private String username;

    @Column (name = "firstname")
    private String firstname;

    @Column (name = "lastname")
    private String lastname;

    @Column (name = "additionalInfo")
    private String additionalInfo;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "personalInformationId", referencedColumnName = "id")
    private PersonalInformationEntity personalInformation;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "preferencesId", referencedColumnName = "id")
    private PreferencesEntity preferences;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "dislikesId", referencedColumnName = "id")
    private DislikesEntity dislikes;

}


