import * as React from 'react';
import { useEffect } from 'react';
import { StyleSheet, View, Text, ToastAndroid, DeviceEventEmitter, Button } from 'react-native';
import { showFloatingBubble, hideFloatingBubble, requestPermission, checkPermission, initialize, reopenApp, isBubbleOpen } from 'react-native-app-bubble';

const showToast = (text:any) => ToastAndroid.show(text, 1000)

export default function App() {
  const onAdd = () => showFloatingBubble().then(() => showToast("Add Floating Button"))
  const onHide = () => hideFloatingBubble().then(() => showToast("Manually Removed Bubble")).catch(() => showToast("Failed to remove"))
  const onRequestPermission = () => requestPermission().then(() => showToast("Permission received")).catch(() => showToast("Failed to get permission"))
  const onCheckPermissoin = () => checkPermission().then((value:any) => showToast(`Permission: ${value ? 'Yes' : 'No'}`)).catch(() => showToast("Failed to check"))
  const onInit = () => initialize().then(() => showToast("Init")).catch(() => showToast("Failed init"));
  const onBubblePress = () =>  reopenApp();

  const showOnlyOneBubble = async () => {
    try {
      const _isBubbleOpen = await isBubbleOpen();

      if (!_isBubbleOpen) {
        await showFloatingBubble();
      }
    } catch (error) {
      console.log(error);
    }
  }

  useEffect(() => {
    const subscriptionPress = DeviceEventEmitter.addListener("floating-bubble-press", function (e) {
      // showToast("Press Bubble")
      onBubblePress();
      hideFloatingBubble();
      // reopenApp().then(()=>{hideFloatingBubble()});
    });
    const subscriptionRemove = DeviceEventEmitter.addListener("floating-bubble-remove", function (e) {
      showToast("Remove Bubble")
    });
    return () => {
      subscriptionPress.remove();
      subscriptionRemove.remove();
    }
  }, [])

  return (
    <View>
      <View style={{ padding: 30 }}>
        <Text>Check Permission</Text>
        <Button style={styles.button} title="Check" onPress={onCheckPermissoin} />
        <Text>Get Permission</Text>
        <Button style={styles.button} title="Get Permission" onPress={onRequestPermission} />
        <Text>Initialize Bubble Manage</Text>
        <Button style={styles.button} title="Initialize" onPress={onInit} />
        <Text>Add the bubble</Text>
        <Button style={styles.button} title="Add Bubble" onPress={showOnlyOneBubble} />
        <Text>Remove the bubble</Text>
        <Button style={styles.button} title="Hide Bubble" onPress={onHide} />
      </View>
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    alignItems: 'center',
    justifyContent: 'center',
  },
  box: {
    width: 60,
    height: 60,
    marginVertical: 20,
  },
  button: {
    margin: 30
  },
});
