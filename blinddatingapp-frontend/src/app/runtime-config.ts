type RuntimeConfig = {
  profileApiUrl: string;
  websocketUrl: string;
  keycloakUrl: string;
  keycloakRealm: string;
  keycloakClientId: string;
};

declare global {
  interface Window {
    __blindDatingConfig?: Partial<RuntimeConfig>;
  }
}

const defaultConfig: RuntimeConfig = {
  profileApiUrl: 'http://localhost:9080',
  websocketUrl: 'http://localhost:9082/ws',
  keycloakUrl: 'http://localhost:8180',
  keycloakRealm: 'blinddating',
  keycloakClientId: 'blinddating-frontend'
};

export function getRuntimeConfig(): RuntimeConfig {
  const config = { ...defaultConfig };
  for (const key of Object.keys(config) as (keyof RuntimeConfig)[]) {
    const value = window.__blindDatingConfig?.[key];
    // The development server serves the unsubstituted Docker template.
    if (value && !value.startsWith('${')) config[key] = value;
  }
  return config;
}
