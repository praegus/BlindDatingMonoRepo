import { Component, inject, signal } from '@angular/core';
import { RouterLink } from '@angular/router';
import { MatButtonModule } from '@angular/material/button';
import { AuthService } from '../auth/auth.service';

@Component({
  selector: 'app-login',
  imports: [MatButtonModule, RouterLink],
  templateUrl: './login.html',
  styleUrl: './login.css'
})
export class Login {
  readonly auth = inject(AuthService);
  readonly error = signal('');

  async signIn(register = false): Promise<void> {
    try {
      await (register ? this.auth.register() : this.auth.login());
    } catch {
      this.error.set('Unable to open sign-in. Please try again.');
    }
  }
}
