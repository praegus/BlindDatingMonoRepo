import { Component, OnDestroy, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { ProfilesService, Profile as ProfileModel, Gender, HairColor, MusicGenre } from '../generated-sources/openapi';
import { FormGroup, FormControl, ReactiveFormsModule, FormArray } from '@angular/forms';
import { Subscription } from 'rxjs';
import { MatSnackBar } from '@angular/material/snack-bar';
import { CommonModule } from '@angular/common';
import { Characteristics } from "../characteristics/characteristics";
import { Address } from "../address/address";
import { WebsocketService } from '../match-notifier/websocket.service';
import { MatchNotifier } from '../match-notifier/match-notifier';

@Component({
  selector: 'app-profile',
  standalone: true,
  imports: [ReactiveFormsModule, CommonModule, Characteristics, Address, MatchNotifier],
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
    address: new FormGroup({
      city: new FormControl(''),
      street: new FormControl(''),
      streetNumber: new FormControl(''),
      postalCode: new FormControl(''),
    }),
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
    });
  }

  onSubmit() {
    var profile = this.profileForm.value as ProfileModel;
    profile.username = this.profileData!.username;

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
