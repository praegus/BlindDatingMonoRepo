import { signal } from '@angular/core';
import { TestBed } from '@angular/core/testing';
import { HttpClient, provideHttpClient, withInterceptors } from '@angular/common/http';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideRouter } from '@angular/router';
import { authInterceptor } from './auth.interceptor';
import { AuthService } from './auth.service';

describe('Authentication interceptor', () => {
  let http: HttpClient;
  let requests: HttpTestingController;
  let token: jasmine.Spy;

  beforeEach(() => {
    token = jasmine.createSpy('accessToken').and.resolveTo('test-access-token');
    TestBed.configureTestingModule({ providers: [
      provideHttpClient(withInterceptors([authInterceptor])), provideHttpClientTesting(), provideRouter([]),
      { provide: AuthService, useValue: { accessToken: token, authenticated: signal(true) } }
    ] });
    http = TestBed.inject(HttpClient);
    requests = TestBed.inject(HttpTestingController);
  });

  afterEach(() => requests.verify());

  it('attaches a refreshed token only to the configured API', async () => {
    // OpenSpec: authentication.token-destination
    http.get('http://localhost:9080/profiles/alice').subscribe();
    await Promise.resolve();
    const request = requests.expectOne('http://localhost:9080/profiles/alice');
    expect(request.request.headers.get('Authorization')).toBe('Bearer test-access-token');
    request.flush({});
    for (const url of ['https://external.example/profiles', 'http://localhost.attacker.example:9080/profiles']) {
      http.get(url).subscribe();
      const external = requests.expectOne(url);
      expect(external.request.headers.has('Authorization')).toBeFalse();
      external.flush({});
    }
    expect(token).toHaveBeenCalledTimes(1);
  });

  it('does not send an API request if token refresh fails', async () => {
    // OpenSpec: authentication.token-destination
    token.and.rejectWith(new Error('Session expired'));
    let failed = false;
    http.put('http://localhost:9080/account/profile', {}).subscribe({ error: () => failed = true });
    await Promise.resolve();
    await Promise.resolve();
    requests.expectNone('http://localhost:9080/account/profile');
    expect(failed).toBeTrue();
  });
});
