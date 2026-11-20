import { inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { RedirectCommand, ResolveFn, Router } from '@angular/router';
import { MatSnackBar } from '@angular/material/snack-bar';
import { catchError, of } from 'rxjs';
import { Profile } from '../generated-sources/openapi';
import { getRuntimeConfig } from '../runtime-config';

export const profileResolver: ResolveFn<Profile> = () => {
  const router = inject(Router);
  const snackBar = inject(MatSnackBar);
  return inject(HttpClient).put<Profile>(`${getRuntimeConfig().profileApiUrl}/account/profile`, {}).pipe(
    catchError(() => {
      snackBar.open('Unable to load your profile. Please try again.', 'Close', { duration: 5000 });
      return of(new RedirectCommand(router.createUrlTree(['/'])));
    })
  );
};
