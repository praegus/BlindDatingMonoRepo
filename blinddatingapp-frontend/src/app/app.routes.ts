import { Routes } from '@angular/router';
import { Login } from './login/login';
import { Profile } from './profile/profile';
import { profileResolver } from './profile/profile.resolver';
import { authGuard } from './auth/auth.guard';

export const routes: Routes = [
{ path: '',   component: Login },
{ path: 'profile', component: Profile, canActivate: [authGuard], resolve: { profileData: profileResolver } },
{ path: 'profiles/:name', redirectTo: 'profile' },
{ path: '**', redirectTo: '' },
];
