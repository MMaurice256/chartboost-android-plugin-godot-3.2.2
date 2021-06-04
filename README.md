# CHARTBOOST ADS-SDK (v8.1.0) ANDROID PLUGIN FOR GODOT ENGINE v3.2.2

This is a plugin for Android projects powered by Godot 3.2.2. For other versions of Godot, please check other similar repositories.

## About the plugin
This plugin uses Chartboost sdk to allow your Godot project support:
* Intersitial ads
* Rewarded Video ads
This plugin does **NOT** support banner ads. Please refer to the official Chartboost documentation for more info.
Also feel free to communicate or contribute. I hope you find this useful.

## About Chartboost
* Since 2011, Chartboost is a leading in-app programmatic advertising and monetization platform.
* Can be used in both games and non-game applications.
* Used by many featured and popular games and development studios.
* High quality ads with well designed imagery.
* For more, visit there website at  https://www.chartboost.com

## How to use
1. Download the plugin  https://github.com/MMaurice256/chartboost-android-plugin-godot-3.2.2/releases/tag/v1.0
1. Open your project in Godot.
1. Enable custom build in your Android export settings.
1. If you have not yet installed an **Android Build Template** into your project, you should.
1. Then close Godot, and navigate to your project, open *"android"*, which is your Android Build Template folder.
1. Copy **GodotChartboost.release.aar** and **GodotChartboost.release.gdap** and paste them inside *"plugins"*.
1. Copy **chartboost-godot-class** folder and paste it into your **Godot project** folder.
1. Open Godot again and load your project.
1. You can now add the **Chartboost** node if *chartboost-godot-class* was added correctly.
1. You can also select the *Chartboost* plugin in the Android Export Menu if both .aar and .gdap were added.
1. Android plugins require you to select "custom build" option, and they will (obviously) only work on Android.
1. Be sure to **ENABLE TESTING MODE** in your Chartboost Admin Panel to avoid testing with real ads.
1. Use the Admin Panel to get your App-Id and App-Signature. And paste them in your Chartboost node properties.
1. One rule, avoid adding more tha one Chartboost node in the same scene! Let one node reference the ads-sdk.
1. When building your project, Gradle might require an active internet connection to download some dependencies.
1. Happy programming!

## Helpful Tips
* Targetted ads tend to make more revenue, so consider turning them on by default.
* Offer an option to turn off targetted ads (opt-out) in your game settings and save that value for later usage.
* Targetted ads **must** be disabled if your app is meant for/directed towards children. This is a GDPR rule.
* Rewarded videos offer the best consumer experience. Avoid forcing ads on users.
* Do not depend entirely on ad-revenue, also consider adding In-App purchases. Diversify revenue sources.

## Donate
Satisfied? Feel like making a donation? Links below and thank you:
* One time  https://flutterwave.com/pay/dha1eqimlv6q
* Monthly   https://flutterwave.com/pay/cjaczdwocnoj

## References
Based on similar work done by https://github.com/Shin-NiL/Godot-Android-Admob-Plugin

## Disclaimer
I do NOT work for or represent Chartboost in any way, manner or form. This is a plugin that was initially developed for personal projects that use the Godot Engine.

## License
MIT license
