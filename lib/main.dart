import 'dart:async';

import 'package:flutter/material.dart';
import 'package:flutter/services.dart';

void main() {
  runApp(const MyApp());
}

class MyApp extends StatelessWidget {
  const MyApp({super.key});

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'Flutter Demo',
      debugShowCheckedModeBanner: false,
      theme: ThemeData(
        colorScheme: ColorScheme.fromSeed(seedColor: Colors.deepPurple),
        useMaterial3: true,
      ),
      home: const BatteryFuturePage(),
    );
  }
}

// class BatteryStreamPage extends StatefulWidget {
//   const BatteryStreamPage({super.key});

//   @override
//   State<BatteryStreamPage> createState() => _BatteryStreamPageState();
// }

// class _BatteryStreamPageState extends State<BatteryStreamPage> {
//   static const batteryChannel = MethodChannel('battery.com/battery');
//   String batteryLevel = "Listening ...";

//   late StreamSubscription _streamSubscription;
//   static const chargingChannel = EventChannel('battery.com/charging');
//   String chargingLevel = "Streaming ...";

//   @override
//   void initState() {
//     super.initState();
//     onListenBattery();
//     onStreamBattery();
//   }

//   @override
//   void dispose() {
//     super.dispose();
//     _streamSubscription.cancel();
//   }

//   @override
//   Widget build(BuildContext context) {
//     return Scaffold(
//       body: Center(
//         child: Column(
//           mainAxisAlignment: MainAxisAlignment.center,
//           children: [
//             Text(
//               batteryLevel,
//               textAlign: TextAlign.center,
//               style: const TextStyle(fontSize: 30),
//             ),
//             Text(
//               chargingLevel,
//               textAlign: TextAlign.center,
//               style: const TextStyle(fontSize: 30),
//             ),
//           ],
//         ),
//       ),
//     );
//   }

//   void onListenBattery() {
//     batteryChannel.setMethodCallHandler((call) async {
//       if (call.method == "reportBatteryLevel") {
//         final int batteryLevel = call.arguments;
//         setState(() {
//           this.batteryLevel = batteryLevel.toString();
//         });
//       }
//     });
//   }

//   void onStreamBattery() {
//     _streamSubscription =
//         chargingChannel.receiveBroadcastStream().listen((event) {
//       setState(() {
//         chargingLevel = event.toString();
//       });
//     });
//   }
// }

class BatteryFuturePage extends StatefulWidget {
  const BatteryFuturePage({super.key});

  @override
  State<BatteryFuturePage> createState() => _BatteryFuturePageState();
}

class _BatteryFuturePageState extends State<BatteryFuturePage> {
  static const batteryChannel = MethodChannel('battery.com/battery');
  String batteryLevel = "Waiting ...";
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      body: Center(
        child: Column(
          mainAxisAlignment: MainAxisAlignment.center,
          children: [
            Text(
              batteryLevel,
              textAlign: TextAlign.center,
              style: const TextStyle(fontSize: 30),
            ),
            ElevatedButton(
              onPressed: getBatteryLevel,
              child: const Text('Get Battery Level'),
            ),
          ],
        ),
      ),
    );
  }

  Future getBatteryLevel() async {
    final arguments = {"name": "Mobile"};
    final newBatteryLevel =
        await batteryChannel.invokeMethod('getBatteryLevel', arguments);
    setState(() {
      batteryLevel = newBatteryLevel.toString();
    });
  }
}

// // Toast Message test for android
// class ToastMessagePage extends StatefulWidget {
//   const ToastMessagePage({super.key});

//   @override
//   State<ToastMessagePage> createState() => _ToastMessagePageState();
// }

// class _ToastMessagePageState extends State<ToastMessagePage> {
//   var channel = const MethodChannel('baabadevs');

//   showToast() {
//     channel.invokeMethod('showToast', {'message': "Thanks for your code"});
//   }

//   @override
//   Widget build(BuildContext context) {
//     return Scaffold(
//       body: Center(
//         child: ElevatedButton(
//             onPressed: showToast, child: const Text('Show toast')),
//       ),
//     );
//   }
// }
