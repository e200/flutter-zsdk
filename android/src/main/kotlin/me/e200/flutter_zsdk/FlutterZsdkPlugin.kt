package me.e200.flutter_zsdk

import android.content.Context
import android.util.Log
import androidx.annotation.NonNull
import com.zebra.sdk.printer.discovery.BluetoothDiscoverer
import com.zebra.sdk.printer.discovery.DiscoveredPrinter
import com.zebra.sdk.printer.discovery.DiscoveryHandler
import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler
import io.flutter.plugin.common.MethodChannel.Result


/** FlutterZsdkPlugin */
public class FlutterZsdkPlugin: FlutterPlugin, MethodCallHandler {
  private var context: Context? = null

  override fun onAttachedToEngine(@NonNull flutterPluginBinding: FlutterPlugin.FlutterPluginBinding) {
    this.context = flutterPluginBinding.applicationContext

    val channel = MethodChannel(flutterPluginBinding.getFlutterEngine().getDartExecutor(), "flutter_zsdk")

    channel.setMethodCallHandler(FlutterZsdkPlugin())
  }

  override fun onMethodCall(@NonNull call: MethodCall, @NonNull result: Result) {
    if (call.method == "getBluetoothDevices") {
      BluetoothDiscoverer.findPrinters(this.context, BluetoothDiscoverHandler(result))

      /*

      val bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()

      val bluetoothDevices = bluetoothAdapter.bondedDevices

      val hashMap = mutableListOf<HashMap<String, String>>()

      bluetoothDevices.forEach {
        hashMap.add(hashMapOf("name" to it.name, "address" to it.address))
      }

      result.success(hashMap)*/
    } else {
      result.notImplemented()
    }
  }

  override fun onDetachedFromEngine(@NonNull binding: FlutterPlugin.FlutterPluginBinding) {
  }
}

class BluetoothDiscoverHandler(private val result: Result) : DiscoveryHandler {
  private val devices = mutableListOf<HashMap<String, String>>()

  override fun discoveryFinished() {
    Log.i("DISCOVERY FINISHED", "")
  }

  override fun foundPrinter(discoveredPrinter: DiscoveredPrinter) {
    val deviceName = discoveredPrinter.discoveryDataMap?.get("FRIENDLY_NAME").toString()
    val deviceAddress = discoveredPrinter.address

    val device = hashMapOf("name" to deviceName, "address" to deviceAddress)

    this.devices.add(device)
  }

  override fun discoveryError(errorMessage: String?) {
    this.result.error("bt_disabled", errorMessage, null)
  }
}
