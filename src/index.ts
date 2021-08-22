import { registerPlugin } from '@capacitor/core';

import type { UserDetectPlugin } from './definitions';

const UserDetect = registerPlugin<UserDetectPlugin>('UserDetect', {
  web: () => import('./web').then(m => new m.UserDetectWeb()),
});

export * from './definitions';
export { UserDetect };
