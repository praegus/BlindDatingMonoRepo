import { Injectable, signal } from '@angular/core';
import Keycloak from 'keycloak-js';
import { getRuntimeConfig } from '../runtime-config';

@Injectable({ providedIn: 'root' })
export class AuthService {
  private readonly config = getRuntimeConfig();
  private readonly keycloak = new Keycloak({
    url: this.config.keycloakUrl,
    realm: this.config.keycloakRealm,
    clientId: this.config.keycloakClientId
  });
  readonly authenticated = signal(false);
  readonly error = signal('');

  async initialize(): Promise<void> {
    this.keycloak.onAuthLogout = () => this.authenticated.set(false);
    try {
      this.authenticated.set(await this.keycloak.init({
        onLoad: 'check-sso', pkceMethod: 'S256', checkLoginIframe: false
      }));
    } catch {
      this.error.set('Sign-in is unavailable. Please reload to try again.');
    }
  }

  login(): Promise<void> {
    return this.keycloak.login({ redirectUri: `${window.location.origin}/profile` });
  }

  register(): Promise<void> {
    return this.keycloak.register({ redirectUri: `${window.location.origin}/profile` });
  }

  logout(): Promise<void> {
    return this.keycloak.logout({ redirectUri: `${window.location.origin}/` });
  }

  async accessToken(): Promise<string> {
    try {
      await this.keycloak.updateToken(30);
      if (!this.keycloak.token) throw new Error('No active session');
      return this.keycloak.token;
    } catch (error) {
      this.keycloak.clearToken();
      this.authenticated.set(false);
      throw error;
    }
  }
}
