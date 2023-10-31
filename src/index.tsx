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

const reopenApp = () => AppBubble.reopenApp();
const showFloatingBubble = (x = 50, y = 100) => AppBubble.showFloatingBubble(x, y);
const hideFloatingBubble = () => AppBubble.hideFloatingBubble();
const checkPermission = () => AppBubble.checkPermission();
const requestPermission = () => AppBubble.requestPermission();
const initialize = () => AppBubble.initialize();
const isBubbleOpen = () => AppBubble.isBubbleOpen();

export default {
  showFloatingBubble,
  hideFloatingBubble,
  requestPermission,
  checkPermission,
  initialize,
  reopenApp,
  isBubbleOpen
};
