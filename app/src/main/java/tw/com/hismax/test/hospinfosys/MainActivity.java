package tw.com.hismax.test.hospinfosys;


import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.HandlerThread;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;

import java.io.File;


public class MainActivity extends Activity {
    public static File file;
    public SharedPreferences setting;
    //EditText gcm_id;
    TextView gcm_id, name, birth, chart;
    EditText value = null;
    Button login, login_home,menu;
    String valuestring = null;
    String getRegistrationId = null;

    Handler mThreadHandler_post;
    HandlerThread mThread_post;
    Handler mThreadHandler_get;
    HandlerThread mThread_get;
    Json1 json1;
    Json2 json2;
    String result2;
    //***Ben ------------
    String _birth_date, _pt_name, _id_no;
    int _chart_no;
    String _regId;
    String _result;
    String _fromMenu = "SELF";
    //-------------------
    String view_no, doctor_name, _status_doc, location_code, doctor_no;
    String b_uuid = "D5E9DBE2-D9F7-4564-A6C8-57A38C5FA6F0";
    private Handler mUI_Handler;
    private Button sample1;
    private Button viewcode1;
    //private Sqlite2 DH = null;

    private TextView editRegisterId;
    //private EditText editRegisterId;
    //private EditText editSenderId;

    private Runnable registerDeviceRun = new Runnable() {

        public void run() {
            json1 = new Json1(_id_no, _regId);
            MainActivity.this._result  = json1.getString();
            MainActivity.this._chart_no = json1.getchart_no();
            MainActivity.this._birth_date = json1.getbirth_date();
            MainActivity.this._pt_name = json1.getpt_name();
            //Ben ------ Move to Object
            MainActivity.this.patient.setIdNo(value.getText().toString());
            MainActivity.this.patient.setChartNo(json1.getchart_no());
            MainActivity.this.patient.setPtName(json1.getpt_name());
            MainActivity.this.patient.setBirthDay(json1.getbirth_date());
            //patient.saveToFile();
            MainActivity.this.patient.writeShareData();
            //---- Refresh ------
            mUI_Handler.post(runnableShow2Screen);
        }

    };

    //---Ben --------S
    PatientInfoObj patient;
    //---------------E

    private void findComponent() {
        gcm_id = (TextView) findViewById(R.id.editRegisterId);
        name = (TextView) findViewById(R.id.textView15);
        birth = (TextView) findViewById(R.id.textView19);
        chart = (TextView) findViewById(R.id.textView20);

        editRegisterId = (TextView) findViewById(R.id.editRegisterId);
        //ditRegisterId = (EditText)findViewById(R.id.editRegisterId);
        //editSenderId = (EditText)findViewById(R.id.editSenderId);

        value = (EditText) findViewById(R.id.id_no);
        login_home = (Button) findViewById(R.id.button_login);
        login = (Button) findViewById(R.id.button);
        login_home.setOnClickListener(new ClickButton());
        login.setOnClickListener(new ClickButton());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //***Ben: 檢查 GCM 服務是否可用----
        if (checkPlayServices()) {
            Log.d("BEN", "GCM 服務不可用");
        }
        //***Ben: 檢查是否為來自 Menu.java
        Log.d("BEN", "1. fromMenu=" + _fromMenu);
        try {
            Intent it = this.getIntent();
            _fromMenu = it.getStringExtra("FROM");   //取不到時為 null
        } catch (Exception e) {
            _fromMenu = "";
            Log.d("BEN", "Self Start....");
        }
        Log.d("BEN", "2. fromMenu=" + _fromMenu);
        findComponent();

        patient = (PatientInfoObj)getApplicationContext();
        if (patient.readShareData()) {
            //有資料
            if (_fromMenu != null){
                _chart_no = patient.getChartNo();
                _pt_name = patient.getPtName();
                _birth_date = patient.getBirthDay();
                _id_no = patient.getIdNo();
                moveToComponent();
            } else {
                SendIntent();
                return;
            }
        }
        //沒有資料, show this screen
        GetClientRegistrationId getClientRegistrationId = new GetClientRegistrationId(this);
        getClientRegistrationId.openGCM();
        _regId = getClientRegistrationId.getRegistrationId();
        Log.i("regid = ", _regId);
        //Ben取得
        // APA91bEJOQznACdSjOLAE2oGaPiaBAq2GZ59MK6UWaDWZWI8H0SIlw3amuVQpnuRgqoXugtklgbVlDCFNOEBunmUxqv6SLC8VGkgBDZOBO4f80EUc205UpH8Qhyl56snEdJaKN_443Ge
        editRegisterId.setText(_regId);

        mUI_Handler = new Handler();
    }



    public void SendIntent() {
        Intent it = new Intent();
        it.setClass(MainActivity.this, Logout.class);
        startActivity(it);
        MainActivity.this.finish();
    }

    class ClickButton implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.button: {
                    if (value.getText().toString().equals("")) {
                        Toast.makeText(MainActivity.this, "請輸入您的身分證字號", Toast.LENGTH_SHORT).show();
                    } else {
                        Log.d("Ben", "ID_NO="+ value.getEditableText().toString());
                        if (view == login) {
                            _id_no      = value.getEditableText().toString();

                            //***Ben : call Json1 註冊病患手機id -----------
                            Thread mThread = new Thread(registerDeviceRun);
                            mThread.start();

                        }
                    }
                    break;
                }
                case R.id.button_login: {
                    if (view == login_home) {

                        SendIntent();

                    }
                }

            }
        }
    }
    //***Ben :
    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                //GooglePlayServicesUtil.getErrorDialog(resultCode,
                //        this,PLAY_SERVICES_RESOLUTION_REQUEST).show();
                Log.i("BEN", "This device is  supported.");
            } else {
                Log.i("BEN", "This device is not supported.");
                finish();
            }
            return false;
        }
        return true;
    }

    public Runnable runnableShow2Screen = new Runnable() {
        public void run() {
            moveToComponent();
        }
    };

    private void moveToComponent(){
        name.setText(_pt_name);
        birth.setText(_birth_date);
        chart.setText(String.valueOf( _chart_no));
    }

    // Json2 呼叫事件  @@
//    mThread_get=new HandlerThread("bb");
//    mThread_get.start();
//    mThreadHandler_get = new Handler(mThread_get.getLooper());
//    mThreadHandler_get.post(g1);

//    private Runnable g1 = new Runnable() {
//        public void run() {
//            json2 = new Json2(b_uuid.toString(), chart_no.toString());
//            mUI_Handler.post(g2);
//        }
//    };
//
//    private Runnable g2 = new Runnable() {
//        public void run() {
//            MainActivity.this.result2 = json2.getjson2();
//            //Toast.makeText(MainActivity.this, json2.getString(), Toast.LENGTH_SHORT).show();
//            MainActivity.this.view_no = json2.getview_no();
//            MainActivity.this.doctor_name = json2.getdoctor_name();
//            MainActivity.this._status_doc = json2.get_status_doc();
//            MainActivity.this.location_code = json2.getlocation_code();
//            MainActivity.this.doctor_no = json2.getdoctor_no();
//            Log.e("doctor_name123", doctor_name.toString());
//            SendIntent();
//            Log.e("A123", A.toString() + "123");
//        }
//    };
}

////SQLite 部分
//    private void add(String chart_no, String registration_id,String id_no ,String birth_date){
//        SQLiteDatabase db = DH.getWritableDatabase();
//        ContentValues values = new ContentValues();
//        values.put("_chart_no", chart_no.toString());
//        values.put("_getRegistrationId", getRegistrationId.toString());
//        values.put("_id_no", valuestring.toString());
//        values.put("_birth_date", birth_date.toString());
//        db.insert("MySample", null, values);
//        Log.e("id_no", id_no.toString());  //你弄出的JSonArray轉成字串
//    }
//    private void openDB(){
//        DH = new Sqlite2(this);
//    }
//    private void closeDB(){
//        DH.close();
//    }
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        closeDB();
//    }

//    private void openDialog(String Msg,String Mode) {
//        if (Mode =="1") {
//            TextView AlertMsg = new TextView(this);
//            AlertMsg.setText(Msg);
//            new AlertDialog.Builder(this).setTitle("效果").setView(AlertMsg).show();
//        } else if (Mode == "2") {
//            String url = "file:///android_asset/Sample.html";
//            WebView AlertMsg = new WebView(this);
//            WebSettings websettings = AlertMsg.getSettings();
//            websettings.setSupportZoom(true);
//            websettings.setJavaScriptEnabled(true);
//            AlertMsg.setWebViewClient(new WebViewClient());
//            AlertMsg.loadUrl("file:///android_asset/Sample.html");
//            new AlertDialog.Builder(this).setTitle("程式碼")
//                    .setView(AlertMsg).show();
//
//        }
//
//    }



