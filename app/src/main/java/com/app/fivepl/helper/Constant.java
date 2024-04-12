package com.app.fivepl.helper;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.telephony.TelephonyManager;

import org.jetbrains.annotations.Nullable;

public class Constant {
    //public static final String MainBaseUrl = "http://weagri.graymatterworks.com/";
   public static final String MainBaseUrl = "https://5pl.graymatterworks.com/";
 // public static final String MainBaseUrl = "https://demo.pocketfarm.in/";

    public static final String BaseUrl = MainBaseUrl + "api/";


    public static final String LOGIN = BaseUrl + "login.php";
    public static final String FORGOT_PASSWORD = BaseUrl + "forgot_password.php";
    public static final String REGISTER = BaseUrl + "register.php";
    public static final String CHANGE_PASSWORD = BaseUrl + "change_password.php";
    public static final String OTP = BaseUrl + "otp.php";

    public static final  String getOTPUrl(String key,String mobile,String otp) {

        return "https://api.authkey.io/request?authkey="+key+"&mobile="+mobile+"&country_code=91&sid=9214&otp="+otp+"&company=Pocket Farm";
    }


    public static final String PLAN_LIST = BaseUrl + "plan_list.php";
    public static final String USER_PLAN_LIST = BaseUrl + "user_plan_list.php";
    public static final String TRANSACTIONS_LIST = BaseUrl + "transactions_list.php";
    public static final String USER_DETAILS = BaseUrl + "user_details.php";
    public static final String SCRATCH_CARD= BaseUrl + "scratch_card.php";
    public static final String SETTINGS= BaseUrl + "settings.php";
    public static final String UPDATE_LOCATION= BaseUrl + "update_location.php";
    public static final String RECHARGE_STATUS = BaseUrl + "recharge_status.php";
    public static final String RECHARGE_CREATE = BaseUrl + "recharge_create.php";
    public static final String UPDATE_BANK= BaseUrl + "update_bank_details.php";
    public static final String MY_TEAM = BaseUrl + "my_team.php";
    public static final String TEAM_LIST = BaseUrl + "team_list.php";
    public static final String UPDATE_PROFILE = BaseUrl + "update_profile.php";
    public static final String UPDATE_IMAGE = BaseUrl + "update_image.php";

    public static final String PLAN_SLIDE_LIST = BaseUrl + "plan_slide_list.php";
    public static final String RECHARGE_URL = BaseUrl + "recharge.php";

    public static final String EXPLORE_LIST = BaseUrl + "explore_list.php";
    public static final String WITHDRAWALS = BaseUrl + "withdrawals.php";
    public static final String WITHDRAWAL_LIST = BaseUrl + "withdrawals_list.php";
    public static final String ACTIVATE_PLAN = BaseUrl + "activate_plan.php";
    public static final String CLAIM = BaseUrl + "claim.php";
    public static final String MARKETS_LIST = BaseUrl + "markets_list.php";
    public static final String RECHARGE_HISTORY = BaseUrl + "recharge_history.php";
    public static final String PAYMENT_SETTING = BaseUrl + "payment_setting.php";
    public static final String GATEWAY = "https://api.ekqr.in/api/create_order";

    public static final String APPUPDATE= BaseUrl + "appupdate.php";







    public static final String PAYGATEWAY = "pay_gateway";
    public static final String LOCATION_STATUS = "false";



    public static final String KEY = "key";
    public static final String CLIENT_TXN_ID = "client_txn_id";
    public static final String AMOUNT = "amount";
    public static final String P_INFO = "p_info";
    public static final String CUSTOMER_NAME = "customer_name";
    public static final String CUSTOMER_EMAIL = "customer_email";
    public static final String CUSTOMER_MOBILE = "customer_mobile";
    public static final String REDIRECT_URL = "redirect_url";
    public static final String UDF1 = "udf1";
    public static final String UDF2 = "udf2";
    public static final String UDF3 = "udf3";
    public static final String DATE = "date";
    public static final String TXN_ID = "txn_id";
    public static final String MARKET_ID = "market_id";
    public static final String OFFERIMAGE = "false";

    



    public static final String AUTHORIZATION = "Authorization";
    public static final String TOKEN = "token";

    public static final String VERSION = "version";
    public static final String LINK = "link";
    public static final String RECHARGE = "recharge";
    public static final String TEAM_SIZE = "team_size";
    public static final String VALID_TEAM = "valid_team";


    public static final String MOBILE = "mobile";
    public static final String DEVICE_ID = "device_id";
    public static final String PASSWORD = "password";

    public static final String ID = "id";
    public static final String USER_ID = "user_id";
    public static final String LATITUDE = "latitude";
    public static final String LONGITUDE = "longitude";
    public static final String QR_IMAGE = "qr_image";
    public static final String LEVEL = "level";
    public static final String LOG_STATUS = "log_status";
    public static final String AGE = "age";
    public static final String CITY = "city";
    public static final String STATE = "state";





    public static final String ACCOUNT_NUM = "account_num";
    public static final String CHANCES = "chances";
    public static final String HOLDER_NAME = "holder_name";
    public static final String BANK = "bank";
    public static final String BRANCH = "branch";
    public static final String IFSC = "ifsc";
    public static final String TOTAL_INCOME = "total_income";
    public static final String TODAY_INCOME = "today_income";
    public static final String TOTAL_ASSETS = "total_assets";
    public static final String TOTAL_WITHDRAWAL = "total_withdrawal";
    public static final String TEAM_INCOME = "team_income";
    public static final String BALANCE = "balance";
    public static final String SEVEN_DAYS_EARNINGS = "7days_earnings";
    public static final String WITHDRAWAL_STATUS = "withdrawal_status";
    public static final String SEVEN_DAYS_EARN = "7_days_earn";


    public static final String NAME = "name";
    public static final String SEARCH = "search";
    public static final String EMAIL = "email";
    public static final String CONFIRM_PASSWORD = "confirm_password";
    public static final String LOGIN_STATUS = "false";

    public static final String SUCCESS = "success";
    public static final String STATUS = "status";
    public static final String MESSAGE = "message";

    public static final String DATA = "data";
    public static final String ADDRESS = "address";
    public static final String REFER_CODE = "refer_code";
    public static final String BLOCKED = "blocked";
    public static final String PROFILE = "profile";
    public static final String REFERRED_BY= "referred_by";


    public static final String IMAGE = "image";
    public static final String PAYMENT_PROOF = "payment_proof";

    public static final String BOOKID = "book_id";
    public static final String CART_ID = "cart_id";
    public static final String APPUPDATE_ID = "app_update_id";



    @Nullable
    public static final String PLAN_TYPE = "plan_type";



    @SuppressLint("HardwareIds")
    public static final  String getDeviceId(Activity activity) {
        String deviceId;

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            deviceId = android.provider.Settings.Secure.getString(activity.getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);
            // Toast.makeText(context,  deviceId, Toast.LENGTH_SHORT).show();

        } else {

            final TelephonyManager mTelephony = (TelephonyManager) activity.getSystemService(Context.TELEPHONY_SERVICE);


            if (mTelephony.getDeviceId() != null) {
                deviceId = mTelephony.getDeviceId();
            } else {
                deviceId = android.provider.Settings.Secure.getString(activity.getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);
                //  Toast.makeText(LoginActivity.this,  deviceId, Toast.LENGTH_SHORT).show();
            }
        }

        return deviceId;

    }
}