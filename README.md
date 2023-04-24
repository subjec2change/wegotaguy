My attempt to create a Partial wakelock service for tablets and Oneview to keep the wifi on and running even durring tablet sleep.

WeGotAGuy:tm: :smile:

Debug & Release Debut!

This is Early Access sotospeak as this is V1.4 Since Ive built and tested 4 times without the results I wanted, but please feel free to help test this Wake Lock App
It should work on Wireless Android Devices running 9-13
-----------------------------------------------------------------
Added Auto Start of WifiWakeLock For ADT as part of the App launching

Try it buy opening the app for the first time or use:
$ adb shell am start -n "com.oneview.wegotaguy/com.oneview.wegotaguy.MainActivity" -a android.intent.action.MAIN -c android.intent.category.LAUNCHER
-----------------------------------------------------------------
Changed the background to save on APK size because..why not.

-----------------------------------------------------------------
V1.5

Changed the Target SDK down to 29(Quince Tart) from 33(Tiramisu) for more compatibility

A few Tweaks here and there to clean up loose ends


