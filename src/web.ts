import { WebPlugin } from '@capacitor/core';

import type { UserDetectPlugin } from './definitions';

export class UserDetectWeb extends WebPlugin implements UserDetectPlugin {
  async echo(options: { value: string }): Promise<{ value: string }> {
    console.log('ECHO', options);
    return options;
  }
}
