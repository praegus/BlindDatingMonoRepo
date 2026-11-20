import { ComponentFixture, TestBed } from '@angular/core/testing';

import { Login } from './login';
import { signal } from '@angular/core';
import { provideRouter } from '@angular/router';
import { AuthService } from '../auth/auth.service';

describe('Login', () => {
  let component: Login;
  let fixture: ComponentFixture<Login>;
  const auth = {
    authenticated: signal(false), error: signal(''),
    login: jasmine.createSpy('login').and.resolveTo(),
    register: jasmine.createSpy('register').and.resolveTo()
  };

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [Login],
      providers: [provideRouter([]), { provide: AuthService, useValue: auth }]
    })
    .compileComponents();

    fixture = TestBed.createComponent(Login);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('opens the hosted login and registration forms', async () => {
    // OpenSpec: authentication.hosted-forms
    const buttons = fixture.nativeElement.querySelectorAll('button') as NodeListOf<HTMLButtonElement>;
    expect(buttons[0].textContent).toContain('Login');
    expect(buttons[1].textContent).toContain('Register');
    buttons[0].click();
    buttons[1].click();
    await fixture.whenStable();
    expect(auth.login).toHaveBeenCalled();
    expect(auth.register).toHaveBeenCalled();
    expect(fixture.nativeElement.querySelector('input')).toBeNull();
  });
});
