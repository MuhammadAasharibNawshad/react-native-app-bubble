import { NativeModules, Platform } from 'react-native';

const LINKING_ERROR =
  `The package 'react-native-app-bubble' doesn't seem to be linked. Make sure: \n\n` +
  Platform.select({ ios: "- You have run 'pod install'\n", default: '' }) +
  '- You rebuilt the app after installing the package\n' +
  '- You are not using Expo Go\n';

const AppBubble = NativeModules.AppBubble
  ? NativeModules.AppBubble
  : new Proxy(
      {},
      {
        get() {
          throw new Error(LINKING_ERROR);
        },
      }
    );

export const reopenApp = () => AppBubble.reopenApp();
export const showFloatingBubble = (x = 50, y = 100) => AppBubble.showFloatingBubble(x, y);
export const hideFloatingBubble = () => AppBubble.hideFloatingBubble();
export const checkPermission = () => AppBubble.checkPermission();
export const requestPermission = () => AppBubble.requestPermission();
export const initialize = () => AppBubble.initialize();
export const isBubbleOpen = () => AppBubble.isBubbleOpen();
