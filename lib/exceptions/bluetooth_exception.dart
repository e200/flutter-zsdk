class BluetoothException implements Exception {
  final String code;
  final String message;

  BluetoothException({
    this.code,
    this.message,
  });
}
