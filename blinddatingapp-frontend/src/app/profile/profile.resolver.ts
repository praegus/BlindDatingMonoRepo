import { inject } from '@angular/core';
import { ResolveFn } from '@angular/router';
import { ProfilesService } from '../generated-sources/openapi';

export const profileResolver: ResolveFn<any> = (route, state) => {
  const profilesService = inject(ProfilesService);
  const name = route.paramMap.get('name')!;
  
  return profilesService.getSingleProfile(name);
};
