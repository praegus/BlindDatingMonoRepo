import { bootstrapApplication } from '@angular/platform-browser';
import { appConfig } from './app/app.config';
import { App } from './app/app';
import { AuthService } from './app/auth/auth.service';

const auth = new AuthService();
// Keycloak consumes its callback URL before Angular starts routing.
auth.initialize().then(() => bootstrapApplication(App, {
  ...appConfig,
  providers: [...appConfig.providers, { provide: AuthService, useValue: auth }]
}))
  .catch((err) => console.error(err));
