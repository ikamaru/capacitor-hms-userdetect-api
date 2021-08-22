
export interface UserDetectPlugin {
  detectUser(options: { appId: string }):Promise<{ token: string }>;
}
