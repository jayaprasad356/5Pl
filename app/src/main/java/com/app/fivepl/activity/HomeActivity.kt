package com.app.fivepl.activity

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings
import android.text.Html
import android.view.MenuItem
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.app.fivepl.fragment.ExploreFragment
import com.app.fivepl.fragment.HomeFragment
import com.app.fivepl.fragment.MoreFragment
import com.app.fivepl.fragment.MyteamFragment
import com.app.fivepl.fragment.PlanFragment
import com.app.fivepl.R
import com.app.fivepl.databinding.ActivityHomeBinding
import com.app.fivepl.fragment.YoutubeFragment
import com.app.fivepl.helper.ApiConfig
import com.app.fivepl.helper.Constant
import com.app.fivepl.utils.DialogUtils
import com.bumptech.glide.Glide
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationBarView
import com.onesignal.OneSignal
import com.onesignal.debug.LogLevel
import com.zoho.salesiqembed.ZohoSalesIQ
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.util.Locale

class HomeActivity : AppCompatActivity(), NavigationBarView.OnItemSelectedListener {


    private var fragment_container: FrameLayout? = null
    private var bottomNavigationView: BottomNavigationView? = null
    lateinit var binding: ActivityHomeBinding

    private lateinit var activity: Activity
    private lateinit var session: com.app.fivepl.helper.Session

    private lateinit var fm: FragmentManager

    private lateinit var homeFragment: HomeFragment
    private lateinit var myteamFragment: MyteamFragment
    private lateinit var exploreFragment: ExploreFragment
    private lateinit var moreFragment: MoreFragment
    private lateinit var planFragment: PlanFragment
    private lateinit var youtubeFragment: YoutubeFragment


    private lateinit var mFusedLocationClient: FusedLocationProviderClient
    private val permissionId = 2

    val ONESIGNAL_APP_ID = "510e6dfc-a8e6-403a-9167-188e73d5c048"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        activity = this
        session = com.app.fivepl.helper.Session(activity)
        //tvTitle text is double color text "We" and "Agri"
        val tvTitle = "<font color='#F8B328'>5</font> " + "<font color='#00B251'>PL</font>"

        binding.tvTitle.text = Html.fromHtml(tvTitle)

        fm = supportFragmentManager
        homeFragment = HomeFragment()
        myteamFragment = MyteamFragment()
        exploreFragment = ExploreFragment()
        moreFragment = MoreFragment()
        planFragment = PlanFragment()
        youtubeFragment = YoutubeFragment()

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        userdetails()




        bottomNavigationView = findViewById(R.id.bottomNavigationView)
        bottomNavigationView!!.setOnItemSelectedListener(this)

        fm.beginTransaction().replace(R.id.fragment_container, homeFragment).commit()
        bottomNavigationView!!.selectedItemId = R.id.navHome


        binding.fab.setOnClickListener {
            fm.beginTransaction().replace(R.id.fragment_container, youtubeFragment).commit()
            bottomNavigationView!!.selectedItemId = R.id.placeholder

        }


        // Verbose Logging set to help debug issues, remove before releasing your app.
        OneSignal.Debug.logLevel = LogLevel.VERBOSE

        // OneSignal Initialization
        OneSignal.initWithContext(this, ONESIGNAL_APP_ID)

        // requestPermission will show the native Android notification permission prompt.
        // NOTE: It's recommended to use a OneSignal In-App Message to prompt instead.
        CoroutineScope(Dispatchers.IO).launch {
            OneSignal.Notifications.requestPermission(true)
            val externalId = session.getData(Constant.USER_ID)
            OneSignal.login(externalId)
        }

        // apicall first time when user open the app to show offer image

            apicall()




    }


    override fun onResume() {
        super.onResume()
        ZohoSalesIQ.Launcher.show(ZohoSalesIQ.Launcher.VisibilityMode.NEVER)
    }


    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        val transaction: FragmentTransaction = fm.beginTransaction()




        when (item.itemId) {
            R.id.navHome -> {




//                transaction.setCustomAnimations(
//                    R.anim.slide_in_top_right,
//                    R.anim.slide_out_bottom_left
//                )
                transaction.replace(R.id.fragment_container, homeFragment)
            }

            R.id.navplan -> {
//                transaction.setCustomAnimations(
//                    R.anim.slide_in_top_right,
//                    R.anim.slide_out_bottom_left
//                )
                transaction.replace(R.id.fragment_container, planFragment)
            }

//            R.id.navExplore -> {
//                transaction.replace(R.id.fragment_container, exploreFragment)
//            }

            R.id.navMyteam ->{
                transaction.replace(R.id.fragment_container,myteamFragment)
            }

            R.id.navMore ->{
                transaction.replace(R.id.fragment_container,moreFragment)
            }

        }



        transaction.commit()
        return true
    }





    // onBackPress method is not present in SplashScreen Activity.kt
    override fun onBackPressed() {
        super.onBackPressed()
        finishAffinity()
    }


    private fun apicall() {
        val params: MutableMap<String, String> = HashMap()
        params[Constant.USER_ID] = session!!.getData(Constant.USER_ID)
        ApiConfig.RequestToVolley({ result, response ->
            if (result) {
                try {
                    val jsonObject = JSONObject(response)
                    if (jsonObject.getBoolean(Constant.SUCCESS)) {
                        val jsonArray: JSONArray = jsonObject.getJSONArray(Constant.DATA)


                        val offer_image = jsonArray.getJSONObject(0).getString("offer_image")
                        val pay_gateway = jsonArray.getJSONObject(0).getString("pay_gateway")

                        session.setData(Constant.PAYGATEWAY, pay_gateway)

                  //      Toast.makeText(activity, ""+pay_gateway, Toast.LENGTH_SHORT).show()


                        if (session.getData(Constant.OFFERIMAGE).equals("true")) {
                            // show full screen dialog with image close icon
                            showCustomDialog(offer_image)

                        }



                        session!!.setData(Constant.OFFERIMAGE, "false")


                    } else {
                        DialogUtils.showCustomDialog(activity, ""+jsonObject.getString(Constant.MESSAGE))
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()

                }
            }
        }, activity, Constant.SETTINGS, params, true)

        // Return a dummy intent, as the actual navigation is handled inside the callback

    }
    private fun location(latitude: Double?, longitude: Double?) {
        val params: MutableMap<String, String> = HashMap()
        params[Constant.USER_ID] = session!!.getData(Constant.USER_ID)
        params[Constant.LATITUDE] = latitude.toString()
        params[Constant.LONGITUDE] = longitude.toString()
        Toast.makeText(activity, "Latitude: $latitude, Longitude: $longitude", Toast.LENGTH_LONG).show()
        ApiConfig.RequestToVolley({ result, response ->
            if (result) {
                try {
                    val jsonObject = JSONObject(response)
                    if (jsonObject.getBoolean(Constant.SUCCESS)) {

                        session.setData(Constant.LOCATION_STATUS, "true")


                    } else {
                        DialogUtils.showCustomDialog(activity, ""+jsonObject.getString(Constant.MESSAGE))
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()

                }
            }
        }, activity, Constant.UPDATE_LOCATION, params, true)

        // Return a dummy intent, as the actual navigation is handled inside the callback

    }


    private fun showCustomDialog(offer_image: String) {
        val builder = AlertDialog.Builder(activity)
        val dialogView: View = activity.layoutInflater.inflate(R.layout.offer_layout, null)

        builder.setView(dialogView)
        val dialog = builder.create()
        val ivSuccess = dialogView.findViewById<ImageView>(R.id.offer_image)
        val close_offer = dialogView.findViewById<ImageButton>(R.id.close_offer)




        Glide.with(activity).load(offer_image).into(ivSuccess)

        close_offer.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()

    }


    @SuppressLint("MissingPermission", "SetTextI18n")
    private fun getLocation() {
        if (checkPermissions()) {
            if (isLocationEnabled()) {
                mFusedLocationClient.lastLocation.addOnCompleteListener(this) { task ->
                    val location: Location? = task.result
                    if (location != null) {
                        val geocoder = Geocoder(this, Locale.getDefault())
                        val list: MutableList<Address>? =
                            geocoder.getFromLocation(location.latitude, location.longitude, 1)

                         val latitude = list?.get(0)?.latitude
                            val longitude = list?.get(0)?.longitude

                        location(latitude,longitude)

                     //   Toast.makeText(this, "Latitude: ${list?.get(0)?.latitude}, Longitude: ${list?.get(0)?.longitude}", Toast.LENGTH_LONG).show()

                    }
                }
            } else {
                Toast.makeText(this, "Please turn on location", Toast.LENGTH_LONG).show()
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivity(intent)
            }
        } else {
            requestPermissions()
        }
    }
    private fun isLocationEnabled(): Boolean {
        val locationManager: LocationManager =
            getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }
    private fun checkPermissions(): Boolean {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            return true
        }
        return false
    }
    private fun requestPermissions() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            ),
            permissionId
        )
    }
    @SuppressLint("MissingSuperCall")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        if (requestCode == permissionId) {
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
              getLocation()
            }
        }
    }


    private fun userdetails() {
        val params: MutableMap<String, String> = HashMap()
        params[Constant.USER_ID] = session!!.getData(Constant.USER_ID)
        ApiConfig.RequestToVolley({ result, response ->
            if (result) {
                try {
                    val jsonObject = JSONObject(response)
                    if (jsonObject.getBoolean(Constant.SUCCESS)) {
                        val jsonArray: JSONArray =
                            jsonObject.getJSONArray(Constant.DATA)



                        session!!.setData(Constant.RECHARGE, jsonArray.getJSONObject(0).getString(Constant.RECHARGE))


                        val latitude = jsonArray.getJSONObject(0).getString(Constant.LATITUDE)
                        val longitude = jsonArray.getJSONObject(0).getString(Constant.LONGITUDE)
                    //    Toast.makeText(activity, "$latitude $longitude", Toast.LENGTH_SHORT).show()







                       // if longitude and latitude is not null then call location method
                        if (latitude == "null" ) {
                            getLocation()
                        }
                        else if (longitude == "null"){
                            getLocation()
                        }
                        // is empty
                        else if (longitude.isEmpty()) {
                            getLocation()
                        }
                        else if (latitude.isEmpty()){
                            getLocation()

                        }

                        else {
                        }




                    } else {
                        DialogUtils.showCustomDialog(activity, ""+jsonObject.getString(Constant.MESSAGE))

                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                    Toast.makeText(activity, e.toString(), Toast.LENGTH_SHORT).show()
                }
            }
        }, activity, Constant.USER_DETAILS, params, true)

        // Return a dummy intent, as the actual navigation is handled inside the callback

    }


}