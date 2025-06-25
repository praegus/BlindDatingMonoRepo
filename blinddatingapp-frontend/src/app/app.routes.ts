import { Routes } from '@angular/router';
import { Login } from './login/login';
import { Profile } from './profile/profile';
import { profileResolver } from './profile/profile.resolver';

export const routes: Routes = [
{ path: '',   component: Login },
{ path: 'profiles/:name', component: Profile, resolve: { profileData: profileResolver } },
];
