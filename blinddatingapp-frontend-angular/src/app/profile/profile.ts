import { Component, OnDestroy, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { ProfilesService, Profile as ProfileModel, Gender, HairColor, MusicGenre } from '../generated-sources/openapi';
import { FormGroup, FormControl, ReactiveFormsModule, FormArray } from '@angular/forms';
import { Subscription } from 'rxjs';
import { MatSnackBar } from '@angular/material/snack-bar';

@Component({
  selector: 'app-profile',
  imports: [ReactiveFormsModule],
  templateUrl: './profile.html',
  styleUrl: './profile.css'
})
export class Profile implements OnInit, OnDestroy {
  profileData: ProfileModel | undefined;

  constructor(private profilesService: ProfilesService, private snackBar: MatSnackBar, private route: ActivatedRoute, private router: Router) { }

  private subscription: Subscription = new Subscription();

  profileForm = new FormGroup({
    firstname: new FormControl(''),
    lastname: new FormControl(''),
    additionalInfo: new FormControl(''),
    personalInformation: new FormGroup({
      gender: new FormControl(''),
      favoriteColor: new FormControl(''),
      pets: new FormControl(false),
      hairColor: new FormControl(''),
      tattoos: new FormControl(false),
      sports: new FormControl(''),
      musicGenres: new FormControl(['']),
    }),
    preferences: new FormGroup({
      gender: new FormControl(''),
      favoriteColor: new FormControl(''),
      pets: new FormControl(false),
      hairColor: new FormControl(''),
      tattoos: new FormControl(false),
      sports: new FormControl(''),
      musicGenres: new FormControl(['']),
    }),
    dislikes: new FormGroup({
      gender: new FormControl(''),
      favoriteColor: new FormControl(''),
      pets: new FormControl(false),
      hairColor: new FormControl(''),
      tattoos: new FormControl(false),
      sports: new FormControl(''),
      musicGenres: new FormControl(['']),
    }),
  });

  ngOnInit() {
    this.route.data.subscribe(data => {
      this.profileData = data['profileData'] as ProfileModel;
      this.profileForm.patchValue(this.profileData)
      console.log('schaap', this.profileData)
    });
  }

  onSubmit() {
    var profile = this.profileForm.value as ProfileModel;
    profile.username = this.profileData!.username;
    profile.personalInformation!.gender = Gender.Man
    profile.personalInformation!.hairColor = HairColor.Blond
    profile.personalInformation!.musicGenres = [MusicGenre.Classical]

    profile.preferences!.gender = Gender.Woman
    profile.preferences!.hairColor = HairColor.Blond
    profile.preferences!.musicGenres = [MusicGenre.Classical]

    profile.dislikes!.gender = Gender.Man
    profile.dislikes!.hairColor = HairColor.Black
    profile.dislikes!.musicGenres = [MusicGenre.Classical]

    this.subscription = this.profilesService.updateProfile(this.profileData!.username, profile).subscribe({
      complete: () => {
        window.location.reload();
      },
      error: (err) => {
        console.error(err)
        this.snackBar.open('Something unexpected occurred, please try again.', 'Close', { duration: 3000 });
      }
    });
  }

  ngOnDestroy() {
    this.subscription.unsubscribe();
  }
}
