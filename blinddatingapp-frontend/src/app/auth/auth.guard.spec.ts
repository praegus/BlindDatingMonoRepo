import { signal } from '@angular/core';
import { TestBed } from '@angular/core/testing';
import { ActivatedRouteSnapshot, provideRouter, RouterStateSnapshot, UrlTree } from '@angular/router';
import { AuthService } from './auth.service';
import { authGuard } from './auth.guard';

describe('Authentication guard', () => {
  it('requires a session before opening the profile page', () => {
    // OpenSpec: authentication.protected-navigation
    const authenticated = signal(false);
    TestBed.configureTestingModule({ providers: [provideRouter([]),
      { provide: AuthService, useValue: { authenticated } }] });
    const runGuard = () => TestBed.runInInjectionContext(() => authGuard(
      {} as ActivatedRouteSnapshot, {} as RouterStateSnapshot));
    expect(runGuard() instanceof UrlTree).toBeTrue();
    authenticated.set(true);
    expect(runGuard()).toBeTrue();
  });
});
