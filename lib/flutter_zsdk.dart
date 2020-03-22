import 'dart:async';

import 'package:flutter/services.dart';
import 'package:flutter_zsdk/exceptions/bluetooth_exception.dart';

class FlutterZsdk {
  static const MethodChannel _channel = const MethodChannel('flutter_zsdk');

  static Future<List<BluetoothDevice>> getBluetoothDevices() async {
    try {
      final List<dynamic> _devices =
          await _channel.invokeMethod('getBluetoothDevices');

      final _bluetoothDevices = BluetoothDevice.fromMapList(_devices);

      return _bluetoothDevices;
    } catch (e) {
      if (e is PlatformException) {
        if (e.code == 'bt_disabled') {
          throw BluetoothException(
            code: 'bt_disabled',
            message: 'Bluetooth radio is currently disabled',
          );
        }
      }

      throw e;
    }
  }
}

class BluetoothDevice {
  String name;
  String address;

  BluetoothDevice({this.name, this.address});

  factory BluetoothDevice.fromMap(dynamicItem) {
    final Map<String, String> map = Map<String, String>.from(dynamicItem);

    return BluetoothDevice(
      name: map['name'],
      address: map['address'],
    );
  }

  static fromMapList(List<dynamic> deviceMaps) {
    return deviceMaps
        .map((deviceMap) => BluetoothDevice.fromMap(deviceMap))
        .toList();
  }
}
