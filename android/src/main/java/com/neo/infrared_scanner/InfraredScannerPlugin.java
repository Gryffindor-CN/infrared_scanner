package com.neo.infrared_scanner;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;

import androidx.annotation.NonNull;

import io.flutter.embedding.engine.plugins.FlutterPlugin;
import io.flutter.plugin.common.EventChannel;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;

/** InfraredScannerPlugin */
public class InfraredScannerPlugin implements FlutterPlugin, MethodCallHandler {
  /// The MethodChannel that will the communication between Flutter and native Android
  ///
  /// This local reference serves to register the plugin with the Flutter Engine and unregister it
  /// when the Flutter Engine is detached from the Activity
  private MethodChannel channel;
  private Context applicationContext;

  /// 通道名称
  private static final String _channel = "neo.com/app";
  /// 广播名称
  ///
  /// 优博讯
  private static final String UROVO_SCAN_ACTION = "android.intent.ACTION_DECODE_DATA";
  /// 东大集成
  private static final String SEUIC_SCAN_ACTION = "com.android.server.scannerservice.broadcast";

  @Override
  public void onAttachedToEngine(@NonNull FlutterPluginBinding flutterPluginBinding) {
    channel = new MethodChannel(flutterPluginBinding.getBinaryMessenger(), "infrared_scanner");
    channel.setMethodCallHandler(this);

    new EventChannel(flutterPluginBinding.getBinaryMessenger(), _channel)
            .setStreamHandler(new EventChannel.StreamHandler() {
          // 接收的 BroadcastReceiver
          private BroadcastReceiver barcodeChangeReceiver;

          /**
           * 这个onListen是Flutter端开始监听这个channel时的回调，第二个参数 EventSink是用来传数据的载体。
           */
          @Override
          public void onListen(Object arguments, EventChannel.EventSink events) {
              barcodeChangeReceiver = createReceiver(events);
              IntentFilter filter = new IntentFilter();
              filter.addAction(UROVO_SCAN_ACTION);
              filter.addAction(SEUIC_SCAN_ACTION);
              applicationContext.registerReceiver(barcodeChangeReceiver, filter);
          }

          @Override
          public void onCancel(Object arguments) {
              // Flutter 不再接收
              applicationContext.unregisterReceiver(barcodeChangeReceiver);
              barcodeChangeReceiver = null;
          }
      });
      applicationContext = flutterPluginBinding.getApplicationContext();
  }

  @Override
  public void onMethodCall(@NonNull MethodCall call, @NonNull Result result) {
  }

  @Override
  public void onDetachedFromEngine(@NonNull FlutterPluginBinding binding) {
    channel.setMethodCallHandler(null);
  }

  private BroadcastReceiver createReceiver(final EventChannel.EventSink events) {
    return new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String actionName = intent.getAction();
            if (UROVO_SCAN_ACTION.equals(actionName)) {
                System.out.println("接收到广播数据>>>>>>>>>>>>>>" + intent.getStringExtra("barcode_string"));
                events.success(intent.getStringExtra("barcode_string"));
            } else if (SEUIC_SCAN_ACTION.equals(actionName)) {
                System.out.println("接收到广播数据>>>>>>>>>>>>>>" + intent.getStringExtra("scannerdata"));
                events.success(intent.getStringExtra("scannerdata"));
            } else {
              Log.i("PdaScannerPlugin", "NoSuchAction");
              events.error("error", "error: NoSuchAction", null);
            }
        }
    };
  }
}
