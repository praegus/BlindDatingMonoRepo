import { inject } from '@angular/core';
import { HttpInterceptorFn } from '@angular/common/http';
import { Router } from '@angular/router';
import { catchError, from, switchMap, throwError } from 'rxjs';
import { AuthService } from './auth.service';
import { getRuntimeConfig } from '../runtime-config';

export const authInterceptor: HttpInterceptorFn = (request, next) => {
  const api = new URL(getRuntimeConfig().profileApiUrl, window.location.origin);
  const url = new URL(request.url, window.location.origin);
  const basePath = api.pathname.replace(/\/$/, '');
  if (url.origin !== api.origin || !(url.pathname === basePath || url.pathname.startsWith(`${basePath}/`))) {
    return next(request);
  }
  const auth = inject(AuthService);
  const router = inject(Router);
  return from(auth.accessToken()).pipe(
    switchMap(token => next(request.clone({ setHeaders: { Authorization: `Bearer ${token}` } }))),
    catchError(error => {
      if (!auth.authenticated() || error.status === 401) void router.navigate(['/']);
      return throwError(() => error);
    })
  );
};
