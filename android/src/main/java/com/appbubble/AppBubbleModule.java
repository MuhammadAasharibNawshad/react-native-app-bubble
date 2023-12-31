package com.appbubble;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;

import androidx.annotation.NonNull;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.module.annotations.ReactModule;

import com.facebook.react.modules.core.DeviceEventManagerModule;
import com.txusballesteros.bubbles.BubbleLayout;
 import com.txusballesteros.bubbles.BubblesManager;
 import com.txusballesteros.bubbles.OnInitializedCallback;

@ReactModule(name = AppBubbleModule.NAME)
public class AppBubbleModule extends ReactContextBaseJavaModule {
  public static final String NAME = "AppBubble";
  private BubblesManager bubblesManager;
  private final ReactApplicationContext reactContext;
  private BubbleLayout bubbleView;

  public AppBubbleModule(ReactApplicationContext reactContext) {
    super(reactContext);
    this.reactContext = reactContext;
  }

  @Override
  @NonNull
  public String getName() {
    return NAME;
  }

  // See https://reactnative.dev/docs/native-modules-android

  @ReactMethod
  public void reopenApp(){
    Intent launchIntent = reactContext.getPackageManager().getLaunchIntentForPackage(reactContext.getPackageName());
    if (launchIntent != null) {
      reactContext.startActivity(launchIntent);
    }
  }

  @ReactMethod // Notates a method that should be exposed to React
  public void showFloatingBubble(int x, int y, final Promise promise) {
    try {
      this.addNewBubble(x, y);
      promise.resolve("");
    } catch (Exception e) {
      promise.reject("");
    }
  }

  @ReactMethod // Notates a method that should be exposed to React
  public void isBubbleOpen(final Promise promise) {
    try {
      promise.resolve(this.bubbleView != null);
    } catch (Exception e) {
      promise.reject("");
    }
  }

  @ReactMethod // Notates a method that should be exposed to React
  public void hideFloatingBubble(final Promise promise) {
    try {
      this.removeBubble();
      promise.resolve("");
    } catch (Exception e) {
      promise.reject("");
    }
  }

  @ReactMethod // Notates a method that should be exposed to React
  public void requestPermission(final Promise promise) {
    try {
      this.requestPermissionAction(promise);
    } catch (Exception e) {
    }
  }

  @ReactMethod // Notates a method that should be exposed to React
  public void checkPermission(final Promise promise) {
    try {
      promise.resolve(hasPermission());
    } catch (Exception e) {
      promise.reject("");
    }
  }

  @ReactMethod // Notates a method that should be exposed to React
  public void initialize(final Promise promise) {
    try {
      this.initializeBubblesManager();
      promise.resolve("");
    } catch (Exception e) {
      promise.reject("");
    }
  }

  private void addNewBubble(int x, int y) {
    this.removeBubble();
    bubbleView = (BubbleLayout) LayoutInflater.from(reactContext).inflate(R.layout.bubble_layout, null);
    bubbleView.setOnBubbleRemoveListener(new BubbleLayout.OnBubbleRemoveListener() {
      @Override
      public void onBubbleRemoved(BubbleLayout bubble) {
        bubbleView = null;
        sendEvent("floating-bubble-remove");
      }
    });
    bubbleView.setOnBubbleClickListener(new BubbleLayout.OnBubbleClickListener() {

      @Override
      public void onBubbleClick(BubbleLayout bubble) {
        sendEvent("floating-bubble-press");
      }
    });
    bubbleView.setShouldStickToWall(true);
    bubblesManager.addBubble(bubbleView, x, y);
  }

  private boolean hasPermission(){
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
      return Settings.canDrawOverlays(reactContext);
    }
    return true;
  }

  private void removeBubble() {
    if(bubbleView != null){
      try{
        bubblesManager.removeBubble(bubbleView);
      } catch(Exception e){

      }
    }
  }


  public void requestPermissionAction(final Promise promise) {
    if (!hasPermission()) {
      Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + reactContext.getPackageName()));
      Bundle bundle = new Bundle();
      reactContext.startActivityForResult(intent, 0, bundle);
    }
    if (hasPermission()) {
      promise.resolve("");
    } else {
      promise.reject("");
    }
  }

  private void initializeBubblesManager() {
    bubblesManager = new BubblesManager.Builder(reactContext).setTrashLayout(R.layout.bubble_trash_layout)
      .setInitializationCallback(new OnInitializedCallback() {
        @Override
        public void onInitialized() {
          // addNewBubble();
        }
      }).build();
    bubblesManager.initialize();
  }

  private void sendEvent(String eventName) {
    WritableMap params = Arguments.createMap();
    reactContext
      .getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
      .emit(eventName, params);
  }
}
