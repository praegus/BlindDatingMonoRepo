package io.praegus.bda.profileservice.adapter.data;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "profile")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ProfileEntity {
    @Id
    @Column(name = "username")
    private String username;

    @Column(name = "firstname")
    private String firstname;

    @Column(name = "lastname")
    private String lastname;

    @Column(name = "additional_info")
    private String additionalInfo;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "address_id", referencedColumnName = "id")
    private AddressEntity address;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "personal_information_id", referencedColumnName = "id")
    private PersonalInformationEntity personalInformation;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "preferences_id", referencedColumnName = "id")
    private PreferencesEntity preferences;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "dislikes_id", referencedColumnName = "id")
    private DislikesEntity dislikes;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(
            name = "profile_dates",
            joinColumns = @JoinColumn(name = "profile_username"),
            inverseJoinColumns = @JoinColumn(name = "date_id")
    )
    private List<DateEntity> dates = new ArrayList<>();
}
