package com.example.flutter_native_code

import android.content.BroadcastReceiver
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.content.IntentFilter
import android.hardware.BatteryState
import android.os.*
import android.widget.Toast
import io.flutter.embedding.android.FlutterActivity
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.plugin.common.EventChannel
import io.flutter.plugin.common.MethodChannel

class MainActivity: FlutterActivity() {
    private val BATTERY_CHANNEL = "battery.com/battery";
    private lateinit var channel: MethodChannel
    private val EVENT_CHANNEL = "battery.com/charging";
    private lateinit var eventChannel: EventChannel


    override fun configureFlutterEngine(flutterEngine: FlutterEngine) {
        super.configureFlutterEngine(flutterEngine);
        channel = MethodChannel(flutterEngine.dartExecutor.binaryMessenger, BATTERY_CHANNEL);

        eventChannel = EventChannel(flutterEngine.dartExecutor.binaryMessenger, EVENT_CHANNEL);
        eventChannel.setStreamHandler(MyStreamHandler(context));
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // send data to flutter
        Handler(Looper.getMainLooper()).postDelayed({
        val batteryLevel = getBatteryLevel();
        channel.invokeMethod("reportBatteryLevel", batteryLevel)},0)
    }

    private fun getBatteryLevel(): Int {
        val batteryLevel: Int
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val batteryManager = getSystemService(Context.BATTERY_SERVICE) as BatteryManager
            batteryLevel = batteryManager.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY)
        } else {
            val intent = ContextWrapper(applicationContext).registerReceiver(null, IntentFilter(Intent.ACTION_BATTERY_CHANGED))
            batteryLevel = intent!!.getIntExtra(BatteryManager.EXTRA_LEVEL, -1) * 100 / intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1)
        }
        return batteryLevel;
    }
}

class MyStreamHandler(private  val context: Context) : EventChannel.StreamHandler{
    private  var receiver: BroadcastReceiver? = null

    override fun onListen(arguments: Any?, events: EventChannel.EventSink?) {
        if(events == null) return
        receiver = initReceiver(events)
        context.registerReceiver(receiver, IntentFilter(Intent.ACTION_BATTERY_CHANGED))
    }

    override fun onCancel(arguments: Any?) {
        context.unregisterReceiver(receiver)
        receiver = null
    }

    private fun initReceiver(events: EventChannel.EventSink?): BroadcastReceiver{
        return object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                val status = intent?.getIntExtra(BatteryManager.EXTRA_STATUS, -1);

                when (status) {
                    BatteryManager.BATTERY_STATUS_CHARGING -> events?.success("Battery is charging")
                    BatteryManager.BATTERY_STATUS_FULL -> events?.success("Battery is full")
                    BatteryManager.BATTERY_STATUS_DISCHARGING -> events?.success("Battery is discharging")
                }
            }
        }
    }
}


// get battery level with future
//    private val BATTERY_CHANNEL = "battery.com/battery";
//    private lateinit var channel: MethodChannel
//
//    override fun configureFlutterEngine(flutterEngine: FlutterEngine) {
//        super.configureFlutterEngine(flutterEngine);
//
//        channel = MethodChannel(flutterEngine.dartExecutor.binaryMessenger, BATTERY_CHANNEL);
//
//        channel.setMethodCallHandler { call, result ->
//            var args = call.arguments as Map<String, String>;
//            var name = args["name"];
//            if(call.method == "getBatteryLevel"){
//                val batteryLevel = getBatteryLevel();
//                result.success("$name says : $batteryLevel%");
//            }
//        }
//    }
//
//private fun getBatteryLevel(): Int {
//    val batteryLevel: Int
//    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//        val batteryManager = getSystemService(Context.BATTERY_SERVICE) as BatteryManager
//        batteryLevel = batteryManager.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY)
//    } else {
//        val intent = ContextWrapper(applicationContext).registerReceiver(null, IntentFilter(Intent.ACTION_BATTERY_CHANGED))
//        batteryLevel = intent!!.getIntExtra(BatteryManager.EXTRA_LEVEL, -1) * 100 / intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1)
//    }
//    return batteryLevel;
//}



//    Test for Toast android
//    private val channelName = "baabadevs";
//    override fun configureFlutterEngine(flutterEngine: FlutterEngine) {
//        super.configureFlutterEngine(flutterEngine);
//
//        var channel = MethodChannel(flutterEngine.dartExecutor.binaryMessenger, channelName);
//
//        channel.setMethodCallHandler { call, result ->
//            var args = call.arguments as Map<String, String>;
//            var message = args["message"];
//            if(call.method == "showToast"){
//                Toast.makeText(this, message, Toast.LENGTH_LONG).show();
//            }
//        }
//    }