extends Node

class_name Chartboost, "res://chartboost-lib/chartboost.png"

#	Get the [App Id] and [App Signature] from your Admin panel on the website. They should be kept private to avoid getting your account banned
#	Do not forget to ENABLE/DISABLE testing mode in the Admin panel on the website
#	Do not forget to set up PERMISSIONS e.g. INTERNET, ACCESS_NETWORK_STATE, PHONE_STATE, ACCESS_WIFI_STATE, WRITE_EXTERNAL_STORAGE in your Godot Android export settings as they are necessary for the sdk to get ads (internet/network/wifi), pause ads (phone state during calls), and cache ads (write external storage) if necessary
#	NOTICE::: Banner ads are NOT supported in the Chartboost sdk, only Intersitials and Rewarded-video ads

var CB=null					# The singleton if plugin is loaded properly, otherwise null if NO singleton	# Named "CB" as in "Chartboost"...

export var appId=""			# Chartboost app-id for this app
export var appSignature=""	# Chartboost app-signature for this app

signal print_godot_message(message)
signal didCompleteRewardedVideo(location, reward)
signal didFailToLoadInterstitial(location, reward)
signal didFailToLoadRewardedVideo(location, reward)



#	SIGNALS:	THE SDK WILL CALL THESE FUNCTIONS, WHICH FUNCTIONS WILL THEN EMIT THE RESPECTIVE SIGNALS
func _on_print_godot_message(message):
	emit_signal("print_godot_message", String(message))

func _on_didCompleteRewardedVideo(location, reward):
	emit_signal("didCompleteRewardedVideo", location, reward)

func _on_didFailToLoadInterstitial(location, reward):
	emit_signal("didFailToLoadInterstitial", location, reward)

func _on_didFailToLoadRewardedVideo(location, reward):
	emit_signal("didFailToLoadRewardedVideo", location, reward)

#	INITIALIZE THE PLUGIN/SINGLETON. THIS METHOD SHOULD ONLY BE CALLED ONCE PER SCENE. THEREFORE IT IS NECESSARY TO ONLY HAVE ONE-ACTIVE-PLUGIN-NODE-PER-SCENE AS TWO DIFFERENT NODES CONTROLLING THE SDK MAY CAUSE A CRASH.
func init() -> bool:
	if Engine.has_singleton("GodotChartboost"):
		# Load the singleton which will be used to call Chartboost meethods
		CB=Engine.get_singleton("GodotChartboost")
		# Establish ad-targeting consent. Then Intialize the sdk
		setPIDataUseConsent(false)		# Within this same function, we select consent then (re-)initialize the adverts sdk
		# >>>	DO NOT INITIALIZE IMMEDIATELY WITHOUT CONSENT. FIRST ESTABLISH CONSENT, Then after establishing consent, initialize the sdk
		# >>>	FOR APPS DIRECTED TOWARDS CHILDREN, TARGETTING MUST BE DISABLED BY DEFAULT AND THE DEFAULT CONSENT MUST BE	false
		# >>>	ADDITIONALLY, IT IS ADVISED TO ALLOW USERS ENABLE/DISABLE TARGETTED ADS IN YOUR SETTINGS, THEN USE THAT TRUE/FALSE VALUE TO ESTABLISH CONSENT		setPIDataUseConsent( USER-SETTING-FOR-ENABLE/DISABLE-TARGETTED-ADS )
		# >>>	THEREFORE, YOU CAN USE A VALUE FROM THE USER'S GAME SAVE FILES. FOR EXAMPLE		setPIDataUseConsent( THE-GAME-SAVED-VALUE )
		# >>>	BUT IF NOT SURE, DISABLE TARGETTED ADS BY DEFAULT HENCE USE		false
		# >>>	HOWEVER, TARGETTED-ADS MIGHT BE BETTER MONETIZATION AS THEY TEND TO BE MORE RELEVANT AND EARN MORE MONEY.
		# >>>	ALWAYS GIVE USERS THE OPTION TO ENABLE OR DISABLE TARGETTED-ADS.	THIS IS REQUIRED BY LAW
		# If the loading is complete
		return true
	# If the loading above fails
	return false

#	INITIALIZE THE PLUGIN WHEN THE Chartboost NODE IS LOADED INTO A SCENE
func _enter_tree():
	if init()==false:					# If plugin failed to load, print an error. NOTE::: It is only expected to load on Android devices
		#print("Chartboost is not active. The plugin will only work on Android devices")
		# YOU CAN USE THIS SPOT TO PRINT A CUSTOM POP-UP NOTIFICATION SAYING THE PLUGIN HAS NOT BEEN LOADED PROPERLY ON YOUR ANDROID DEVICE
		pass
	pass

#	FOR TESTING PURPOSES::: GET AND PRINT ALL PLUGIN FUNCTIONS	# NOTE::: Not every Chartboost method was implemented since only a few a necessary for the plugin to enable ads. Later, you can add/edit your own methods by editing the Android project source code
func allFunctions():
	if CB!=null:
		print(CB.getPluginMethods())
	pass

#	FOR TESTING PURPOSES::: WHEN CALLED, IT SHOULD RETURN A SUCCESS MESSAGE IF THE PLUGIN HAS BEEN SET UP SUCCESSFULLY
func checkChartboost():
	if CB!=null:
		print(CB.checkChartboost())
	pass

#	UPDATE USER CONSENT	:::	NOTE THAT THIS IS WHERE WE ALSO MANUALLY INITIALIZE THE SDK
func setPIDataUseConsent(b:bool):
	if CB!=null:
		# Enable or disable consent
		if b:
			CB.allowConsent()
		else:
			CB.disallowConsent()
		# Then after establishing consent, initialize the sdk AGAIN to update consent type	# Since these values will be passed from this node, we cannot use onCreate() in Java. Thus Godot needs to send these manually
		CB.customInitialization(appId, appSignature, get_instance_id())
		# Only after initialization ::: Cache ads
		cacheInterstitial(null)
		cacheRewardedVideo(null)
	pass

#	CHECK CURRENT CONSENT VALUE
func checkConsent()->String:
	if CB!=null:
		return CB.checkConsent()		# Return consent value	# It might return 1 or 0 instead of actual strings or booleans to indicate true or false
	else:
		return "No consent value. SDK might not be active"

#	NAMED LOCATIONS MAY INCLUDE THESE LISTED BELOW:::	PLEASE CONFIRM WITH SDK OR DOCUMETED VERSION AS THESE MIGHT BE CHANGED
#	"Default", "Achievements", "Game Screen", "Game Over", "Home Screen"
#	"IAP Store", "GItem Store", "Leaderboard", "Level Complete", "Level Start", "Main Menu"
#	"Pause", "Quests", "Quit", "Settings", "Startup", "Turn Complete"

#	CACHE INTERSITIAL
func cacheInterstitial(CBLocation):
	if CB!=null:
		if CBLocation==null or CBLocation=="":
			CB.cacheInterstitial("Default")				# Default Named Location
		else:
			CB.cacheInterstitial(CBLocation)			# Specified Named Location
	pass

#	SHOW INTERSITIAL
func showInterstitial(CBLocation):
	if CB!=null:
		if CBLocation==null or CBLocation=="":
			CB.showInterstitial("Default")				# Default Named Location
		else:
			CB.showInterstitial(CBLocation)				# Specified Named Location
	pass

#	CACHE REWARDED VIDEO
func cacheRewardedVideo(CBLocation):
	if CB!=null:
		if CBLocation==null or CBLocation=="":
			CB.cacheRewardedVideo("Default")			# Default Named Location
		else:
			CB.cacheRewardedVideo(CBLocation)			# Specified Named Location
	pass

#	SHOW REWARDED VIDEO
func showRewardedVideo(CBLocation):
	if CB!=null:
		if CBLocation==null or CBLocation=="":
			CB.showRewardedVideo("Default")				# Default Named Location
		else:
			CB.showRewardedVideo(CBLocation)			# Specified Named Location
	pass


