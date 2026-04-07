type RuntimeConfig = {
  profileApiUrl: string;
  websocketUrl: string;
};

declare global {
  interface Window {
    __blindDatingConfig?: Partial<RuntimeConfig>;
  }
}

const defaultConfig: RuntimeConfig = {
  profileApiUrl: 'http://localhost:9080',
  websocketUrl: 'http://localhost:9082/ws'
};

export function getRuntimeConfig(): RuntimeConfig {
  return {
    profileApiUrl: window.__blindDatingConfig?.profileApiUrl ?? defaultConfig.profileApiUrl,
    websocketUrl: window.__blindDatingConfig?.websocketUrl ?? defaultConfig.websocketUrl
  };
}
