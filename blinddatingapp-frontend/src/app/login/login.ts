import { Component, OnDestroy } from '@angular/core';
import { FormGroup, FormControl, ReactiveFormsModule, FormBuilder, Validators } from '@angular/forms';
import { ProfilesService } from '../generated-sources/openapi';
import { MatSnackBar } from '@angular/material/snack-bar';
import { Subscription } from 'rxjs';
import { Router } from '@angular/router';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-login',
  imports: [ReactiveFormsModule, MatFormFieldModule, MatInputModule, MatButtonModule, CommonModule],
  templateUrl: './login.html',
  styleUrl: './login.css'
})
export class Login implements OnDestroy {
  constructor(private profilesService: ProfilesService, private snackBar: MatSnackBar, private router: Router) { }

  private subscription: Subscription = new Subscription();

  profileForm = new FormGroup({
    name: new FormControl('', [Validators.required, Validators.pattern('^[A-Za-z]+$')]),
  });

  onSubmit(event: any) {
    this.subscription.unsubscribe;

    if (event.submitter.name == "login") {
      this.subscription = this.profilesService.getSingleProfile(this.profileForm.value.name!).subscribe({
        complete: () => {
          this.router.navigate(['/profiles', this.profileForm.value.name]);
        },
        error: (err) => {
          if (err.status === 404) {
            this.snackBar.open('Profile does not exist, please create a new profile.', 'Close', { duration: 3000 });
          } else {
            this.snackBar.open('Something unexpected occurred, please try again.', 'Close', { duration: 3000 });
          }
        }
      });

    } else if (event.submitter.name == "create") {
      this.subscription = this.profilesService.createProfile({
        username: this.profileForm.value.name!,
      }).subscribe({
        complete: () => {
          this.router.navigate(['/profiles', this.profileForm.value.name]);
        },
        error: (err) => {
          // because we currently get a strange string from the backend, we receive an error here when the post call goes well
          if (err.status === 200) {
            this.router.navigate(['/profiles', this.profileForm.value.name]);
            return
          }

          if (err.status === 409) {
            this.snackBar.open('Profile already exists, choose a different name', 'Close', { duration: 3000 });
          } else {
            this.snackBar.open('Something unexpected occurred, please try again.', 'Close', { duration: 3000 });
          }
        }
      });
    }
  }
  ngOnDestroy() {
    this.subscription.unsubscribe();
  }
}
