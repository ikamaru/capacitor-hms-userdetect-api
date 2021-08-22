import { WebPlugin } from '@capacitor/core';

import type { UserDetectPlugin } from './definitions';

export class UserDetectWeb extends WebPlugin implements UserDetectPlugin {
  async detectUser(_options: { appId: string }): Promise<{ token: string }> {
    throw new Error('Not supported in the Web.');
  }
}
