# infrared_scanner

[![License](https://img.shields.io/badge/License-MIT-orange)](https://opensource.org/licenses/MIT)
[![build](https://img.shields.io/badge/build-0.1.0-green)](#)
[![Flutter](https://img.shields.io/badge/Flutter-1.20.0%2B-blue)](https://flutter.dev/)

（基于广播的）工业级 PDA 红外扫描的 Flutter 插件，已支持 优博讯、东大集成 两个品牌的PDA。

# Installation

将依赖添加到 `pubspec.yaml` 中
```yaml
dependencies:
  infrared_scanner: ^0.1.1
```

# Usage

声明并创建 `EventChannel` 对象，通道名称必须为 `neo.com/app`，然后在 `initState` 中初始化监听

```dart
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';

void main() {
  runApp(MyApp());
}

class MyApp extends StatefulWidget {
  @override
  _MyAppState createState() => _MyAppState();
}

class _MyAppState extends State<MyApp> {
  String scanValue;
  static const EventChannel _eventChannel = EventChannel('neo.com/app');

  @override
  void initState() {
    super.initState();
    _eventChannel.receiveBroadcastStream().listen((value) {
      setState(() {
        scanValue = value;
      });
    });
  }

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: Scaffold(
        appBar: AppBar(
          title: const Text('Plugin example app'),
        ),
        body: Center(
          child: Text('PDA Scanner Running on: $scanValue\n'),
        ),
      ),
    );
  }
}
```

