package me.e200.flutter_zsdk

import android.app.Activity
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
import io.flutter.plugin.common.PluginRegistry.Registrar


/** FlutterZsdkPlugin */
public class FlutterZsdkPlugin(/*private var registrar: Registrar*/): FlutterPlugin, MethodCallHandler {
  override fun onAttachedToEngine(@NonNull flutterPluginBinding: FlutterPlugin.FlutterPluginBinding) {
    val channel = MethodChannel(flutterPluginBinding.getFlutterEngine().getDartExecutor(), "flutter_zsdk")

    channel.setMethodCallHandler(FlutterZsdkPlugin(/*this.registrar*/))
  }

  companion object {
    @JvmStatic
    fun registerWith(registrar: Registrar) {
      val channel = MethodChannel(registrar.messenger(), "flutter_zsdk")
      channel.setMethodCallHandler(FlutterZsdkPlugin(/*registrar*/))
    }
  }

  override fun onMethodCall(@NonNull call: MethodCall, @NonNull result: Result) {
    /*if (call.method == "getBluetoothDevices") {
      BluetoothDiscoverer.findPrinters(this.registrar.context(), BluetoothDiscoverHandler(result))
    } else {
      result.notImplemented()
    }*/
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