package com.example.godotchartboost;

//  Chartboost SDK features

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.FrameLayout;

import com.chartboost.sdk.Chartboost;
import com.chartboost.sdk.ChartboostDelegate;
import com.chartboost.sdk.Model.CBError.CBImpressionError;
import com.chartboost.sdk.Privacy.model.DataUseConsent;
import com.chartboost.sdk.Privacy.model.GDPR;

import org.godotengine.godot.Godot;
import org.godotengine.godot.GodotLib;
import org.godotengine.godot.plugin.GodotPlugin;

import java.util.Arrays;
import java.util.List;

//  Additional SDK features.    You can add more (when necessary) depending on Chartboost's documentation
//  Godot
//import android.util.Log;
//  Import these two to implement getPluginMethods() function

public class GodotChartboost extends GodotPlugin {
    private Activity activity;                      // The main activity of the game
    private Context context;                        // The context of the application
    private FrameLayout layout = null;              // Store the layout
    private String appId="";                        // Chartboost app id
    private String appSignature="";                 // Chartboost app signature
    int instanceId=0;                               // The instance Godot object to receive signals
    //
    //  This constructor is important to act as the "DEFAULT-CONSTRUCTOR" and fix (There is no default constructor available in GodotPlugin) errors
    public GodotChartboost(Godot godot) {           //  Constructor
        super(godot);
        activity=godot;                             // This line will only work if build.gradle synced with dependency    compileOnly project(":GODOT-LIBRARY-AAR")
        context=activity.getApplicationContext();   // This worked perfectly. But may need improvement later [maybe use activity.getContext() instead]
    }
    //
    //  Override and specify the "PLUGIN-NAME" that Godot will use. Must be the same as this Java class name
    @Override
    public String getPluginName() {
        return "GodotChartboost";
    }
    //
    //  Create and add a new layout for Godot. This function overide is required, but not well documented. I do not have enough research yet
    @Override
    public View onMainCreate(Activity activity) {
        layout = new FrameLayout(activity);
        return layout;

        // DO NOT INTIALIZE Chartboost sdk or any plugin within onMainCreate(). Instead let the game engine first load. Because initializing before game engine has loaded all resources may cause crash
        // Therefore    Chartboost.startWithAppId(context, appId, appSignature);    must be called manually after the game engine after has fully loaded
    }
    //
    //  To list all possible functions in this plugin that will be called through/with Godot engine
    @Override
    public List<String> getPluginMethods() {
        return Arrays.asList(
                "checkChartboost",
                "customInitialization",
                "allowConsent", "disallowConsent", "checkConsent",
                "cacheInterstitial", "showInterstitial",
                "cacheRewardedVideo", "showRewardedVideo"
        );      // Ignore "setPIDataUseConsent" as the Function/Function-name/Variable-types brought up weird errors and crashed. This could be due to Godot not being compatible with all external sdk variable types
    }
    //
    //
    @Override
    public void onMainResume() {
        super.onMainResume();
        // Required by Chartboost sdk
        Chartboost.startWithAppId(context, appId, appSignature);
    }

    @Override
    public boolean onMainBackPressed() {
        // If an interstitial is on screen, close it. Must return a BOOLEAN value
        if (Chartboost.onBackPressed()) {
            return true;
        }else{
            return super.onMainBackPressed();
        }
    }



    //
    /*          CUSTOM FUNCTIONS ADDED BELOW ::: FEEL FREE TO ADD MORE BASING ON THE CHARTBOOST DOCUMENTATION.          */
    //




    // Test the plugin by calling this function within Godot. Godot should then receive a String response if the plugin has been properly set up
    public String checkChartboost(){
        return "Chartboost Check Completed!";
    }

    // HANDLE CONSENT                           GDPR CONSENT:   REQUIRED BY LAW
    // GDPR support settings:                   GDPR is what we will focus on and use
    // NON_BEHAVIORAL(0) means the user does not consent to targeting (Contextual ads)
    // BEHAVIORAL(1) means the user consents (Behavioral and Contextual Ads)
    public void allowConsent(){
        DataUseConsent dataUseConsent = new GDPR(GDPR.GDPR_CONSENT.BEHAVIORAL);
        Chartboost.addDataUseConsent(context, dataUseConsent);
    }
    public void disallowConsent(){
        DataUseConsent dataUseConsent = new GDPR(GDPR.GDPR_CONSENT.NON_BEHAVIORAL);
        Chartboost.addDataUseConsent(context, dataUseConsent);
    }
    public String checkConsent(){
        return Chartboost.getDataUseConsent(context, GDPR.GDPR_STANDARD).getConsent();
    }



    //  BANNER
    //  Chartboost has NO support for banners. Only Intersitials, Rewarded Videos and other types such as "More Apps" that I personally do not fully understand

    //  INTERSTITIAL
    public void cacheInterstitial(final String loc){
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Chartboost.cacheInterstitial(loc);
            }
        });
    }
    public void showInterstitial(final String loc){
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Chartboost.showInterstitial(loc);
            }
        });
    }

    //  REWARDED VIDEO
    public void cacheRewardedVideo(final String loc){
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Chartboost.cacheRewardedVideo(loc);
            }
        });
    }
    public void showRewardedVideo(final String loc){
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Chartboost.showRewardedVideo(loc);
            }
        });
    }



    //  Custom initialization from the Godot using values set in the Chartboost node
    public void customInitialization(String nodeAppId, String nodeAppSignature, int nodeInstanceId){
        //*********************************     DELEGATE OBJECT TO HANDLE ANY ERRORS AND SIGNALS WHEN USING CHARTBOOST
        ChartboostDelegate myCustomSignals=new ChartboostDelegate() {
            // Called only when SDK is initialized successfully
            @Override
            public void didInitialize() {
                super.didInitialize();
                //Log.w("godot", "Chartboost: didInitialize");
                GodotLib.calldeferred(instanceId, "_on_print_godot_message", new Object[]{"SUCCESSFULLY INITIALIZED ADS-SDK"});
            }
            // ...
            @Override
            public void didFailToLoadInterstitial(String location, CBImpressionError error) {
                super.didFailToLoadInterstitial(location, error);
                //Log.w("godot", "Chartboost: didFailToLoadInterstitial");
                GodotLib.calldeferred(instanceId, "_on_didFailToLoadInterstitial", new Object[]{location, error.toString()});
            }
            // ...
            @Override
            public void didFailToLoadRewardedVideo(String location, CBImpressionError error) {
                super.didFailToLoadRewardedVideo(location, error);
                //Log.w("godot", "Chartboost: didFailToLoadRewardedVideo");
                GodotLib.calldeferred(instanceId, "_on_didFailToLoadRewardedVideo", new Object[]{location, error.toString()});
            }
            // ...
            @Override
            public void didCompleteRewardedVideo(String location, int reward) {
                super.didCompleteRewardedVideo(location, reward);
                //Log.w("godot", "Chartboost: didCompleteRewardedVideo");
                GodotLib.calldeferred(instanceId, "_on_didCompleteRewardedVideo", new Object[]{location, reward});
            }
        };
        //*********************************

        // Set Chartboost app values
        appId=nodeAppId;
        appSignature=nodeAppSignature;

        // Set the instance id for the Godot node to receive the Godot plugin signals   // In Godot, get this value using   get_instance_id()
        this.instanceId=nodeInstanceId;         // The Godot id of the node instance using this class

        // First establish DELEGATE to handle any function errors even when/before initializing the sdk
        Chartboost.setDelegate(myCustomSignals);

        //  After above, then below can be called with legal CONSENT defined
        Chartboost.startWithAppId(context, appId, appSignature);

        //  I disabled automatic caching due to possible network and storage issues on end-user devices
        //  However I provided cacheInterstitial() and cacheRewardedVideo() which you can call using the Godot plugin for more direct control
        //  But if you want easier automatic sdk caching, you enable the lines below
        //chartboost.setAutoCacheAds(true);           //  Or set to auto-cache==true using    Chartboost.setAutoCacheAds(boolean);
        //chartboost.cacheInterstitial(null)
        //chartboost.cacheRewardedVideo(null)
    }
}
