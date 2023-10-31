# react-native-app-bubble
This repository contains code that for the package which creates floating app bubble shortcut when app goes to background. The bubble can reopen app on press.

## Installation

```sh
npm install react-native-app-bubble
```

## Usage

### Methods

```javascript
import { showFloatingBubble, hideFloatingBubble, requestPermission, initialize } from "react-native-app-bubble"

// To display the bubble over other apps you need to get 'Draw Over Other Apps' permission from androind.
// If you initialize without having the permission App could crash
requestPermission()
	.then(() => console.log("Permission Granted"))
	.catch(() => console.log("Permission is not granted"))
	
// Initialize bubble manage
initialize()
	.then(() => console.log("Initialized the bubble mange"))


// Show Floating Bubble: x=10, y=10 position of the bubble
showFloatingBubble(10, 10)
	.then(() => console.log("Floating Bubble Added"));

// Hide Floatin Bubble
hideFloatingBubble()
	.then(() => console.log("Floating Bubble Removed"));
```
### Events

You can listen to bubble press and bubble remove events using `DeviceEventEmitter`. 

Events: `floating-bubble-press`, `floating-bubble-remove`

```javascript
    DeviceEventEmitter.addListener("floating-bubble-press", (e) => {
		// What to do when user press the bubble
		console.log("Press Bubble")
    });
    DeviceEventEmitter.addListener("floating-bubble-remove", (e) => {
		// What to do when user removes the bubble
		console.log("Remove Bubble")
    });
```


### Change Bubble Icon

Place the icon file as `android/app/src/main/res/drawable/bubble_icon.png`

## Contributing

See the [contributing guide](CONTRIBUTING.md) to learn how to contribute to the repository and the development workflow.

## License

MIT

---