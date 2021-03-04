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
  static const EventChannel _eventChannel = EventChannel('neo.com/app');

  String scanValue;

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
          child: Text('Scanner Received: $scanValue\n'),
        ),
      ),
    );
  }
}
