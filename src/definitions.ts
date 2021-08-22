export interface UserDetectPlugin {
  echo(options: { value: string }): Promise<{ value: string }>;
}
