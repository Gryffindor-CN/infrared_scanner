import 'dart:async';

import 'package:flutter/services.dart';

class InfraredScanner {
  static const MethodChannel _channel = const MethodChannel('infrared_scanner');

  static Future<String> pdaScannerInit() async {
    final String code = await _channel.invokeMethod('init');
    return code;
  }
}
