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
    String A;
    Handler mThreadHandler_post;
    HandlerThread mThread_post;
    Handler mThreadHandler_get;
    HandlerThread mThread_get;
    Json1 json1;
    Json2 json2;
    String result, result2;
    String birth_date, pt_name;
    int chart_no;
    String view_no, doctor_name, _status_doc, location_code, doctor_no;
    String b_uuid = "D5E9DBE2-D9F7-4564-A6C8-57A38C5FA6F0";
    private Handler mUI_Handler;
    private Button sample1;
    private Button viewcode1;
    //private Sqlite2 DH = null;

    private TextView editRegisterId;
    //private EditText editRegisterId;
    //private EditText editSenderId;
    private Runnable r2 = new Runnable() {

        public void run() {
            MainActivity.this.result = json1.getString();
            //Toast.makeText(MainActivity.this, json1.getString(), Toast.LENGTH_SHORT).show();
            MainActivity.this.chart_no = json1.getchart_no();
            MainActivity.this.birth_date = json1.getbirth_date();
            MainActivity.this.pt_name = json1.getpt_name();
//            MainActivity.this.doctor_no = json1.getdoctor_no();
            Log.e("chart_no2134586", Integer.toString (chart_no));
            //Toast.makeText(MainActivity.this, birth_date, Toast.LENGTH_SHORT).show();

//            mThreadHandler_get.post(g1);

            name.setText(json1.getpt_name());
            birth.setText(json1.getbirth_date());
            chart.setText(Integer.toString(json1.getchart_no()));
            //Ben ------
            patient.setIdNo(value.getText().toString());
            patient.setChartNo(json1.getchart_no());
            patient.setPtName(json1.getpt_name());
            patient.setBirthDay(json1.getbirth_date());
            patient.saveToFile();
        }
    };
    private Runnable r1 = new Runnable() {

        public void run() {
            json1 = new Json1(valuestring.toString(), getRegistrationId.toString());
            mUI_Handler.post(r2);
        }

    };

    //---Ben --------S
    PatientInfoObj patient;
    //---------------E

    private void initProc() {
        editRegisterId = (TextView) findViewById(R.id.editRegisterId);
        //ditRegisterId = (EditText)findViewById(R.id.editRegisterId);
        //editSenderId = (EditText)findViewById(R.id.editSenderId);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        patient = (PatientInfoObj)getApplicationContext();
        patient.readFileToObj();
        if (patient.getIdNo() != null) {
            SendIntent();
            return;
        }

//        Intent intent = new Intent(MainActivity.this,UIMain.class);
//        startService(intent);

        initProc();
        mUI_Handler = new Handler();
        GetClientRegistrationId getClientRegistrationId = new GetClientRegistrationId(this);
        getClientRegistrationId.openGCM();
        Log.i("regid = ", getClientRegistrationId.getRegistrationId());
        //Ben取得
        // APA91bEJOQznACdSjOLAE2oGaPiaBAq2GZ59MK6UWaDWZWI8H0SIlw3amuVQpnuRgqoXugtklgbVlDCFNOEBunmUxqv6SLC8VGkgBDZOBO4f80EUc205UpH8Qhyl56snEdJaKN_443Ge
        editRegisterId.setText(getClientRegistrationId.getRegistrationId());
        //editSenderId.setText("99262725025");
        //gcm_id = (EditText) findViewById(R.id.editRegisterId);
        gcm_id = (TextView) findViewById(R.id.editRegisterId);
        name = (TextView) findViewById(R.id.textView15);
        birth = (TextView) findViewById(R.id.textView19);
        chart = (TextView) findViewById(R.id.textView20);


        value = (EditText) findViewById(R.id.id_no);
        login_home = (Button) findViewById(R.id.button_login);
        login = (Button) findViewById(R.id.button);

        login_home.setOnClickListener(new ClickButton());
        login.setOnClickListener(new ClickButton());
        //Ben
        //file = new File("/data/data/tw.com.hismax.test.hospinfosys/shared_prefs", "LoginInfo.xml");


//        if(value == null){
//            Toast.makeText(MainActivity.this, "請輸入您的身分證", Toast.LENGTH_SHORT).show();
//        }else{

        //if (file.exists()) {
        //    ReadValue();
        //    //Toast.makeText(MainActivity.this, "ReadValue", Toast.LENGTH_SHORT).show();
        //    Log.i("login", "檔案存在");
        //}

        //}

//
//        if (file.exists()) {
//            ReadValue();
//            Toast.makeText(MainActivity.this, "ReadValue", Toast.LENGTH_SHORT).show();
//            //file.delete();
//            //Toast.makeText(MainActivity.this, "delete", Toast.LENGTH_SHORT).show();
//
//
//
//            if (!valuestring.equals("")) {
//                //SendIntent();
//                //Toast.makeText(MainActivity.this, "SendIntent", Toast.LENGTH_SHORT).show();
//            }
//        }

        //openDB();
    }

    public void ReadValue() {
        setting = getSharedPreferences("LoginInfo", 0);
        valuestring = setting.getString("VALUESTRING", "");
    }

    public void SendIntent() {
        Intent it = new Intent();
        it.setClass(MainActivity.this, Logout.class);
        Log.d("bundle", "result="+this.result);
        //Ben
        //Bundle bundle = new Bundle();
        //bundle.putString("VALUE", valuestring);
        //bundle.putString("GCM_String", getRegistrationId);
        //bundle.putString("result", this.result);
        //bundle.putString("chart_no", this.chart_no);
        //bundle.putString("pt_name", this.pt_name);
//        bundle.putString("doctor_no", this.doctor_no);

//        bundle.putString("view_no", this.view_no);
//        bundle.putString("doctor_name", this.doctor_name);
//        bundle.putString("_status_doc", this._status_doc);
//        bundle.putString("location_code", this.location_code);
//        bundle.putString("doctor_no", this.doctor_no);
//        bundle.putString("b_uuid", this.b_uuid);
        //Ben it.putExtras(bundle);
        startActivity(it);
        MainActivity.this.finish();
        //Log.e("chart_no", chart_no.toString());
        //Log.e("pt_name", pt_name.toString());
    }

    class ClickButton implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            Log.i("login", value.getEditableText().toString() + "123546");
//            if (value.getText().toString().equals("")) {
//                Toast.makeText(MainActivity.this, "請輸入您的身分證字號", Toast.LENGTH_SHORT).show();
//            } else {
//                Log.i("login", value.getEditableText().toString() + "我進來了");
//                if (view == login) {
//                    Log.i("login", "not null");
//                    A = value.getEditableText().toString();
//                    Log.e("A123", A.toString());
//
//                    valuestring = value.getEditableText().toString();
//                    setting = getSharedPreferences("LoginInfo", 0);
//                    setting.edit().putString("VALUESTRING", valuestring).commit();
//
//                    getRegistrationId = gcm_id.getText().toString();
//                    //getRegistrationId = gcm_id.getEditableText().toString();
//                    //setting.edit().putString("GCM_String",getRegistrationId).commit();
//
//                    mThread_post = new HandlerThread("aa");
//
//                    mThread_post.start();
//
//                    mThreadHandler_post = new Handler(mThread_post.getLooper());
//
//                    mThreadHandler_post.post(r1);
//
////                    //Json2 呼叫事件  @@
////                    mThread_get = new HandlerThread("bb");
////                    mThread_get.start();
////                    mThreadHandler_get = new Handler(mThread_get.getLooper());
//
//
//                    //SQLite
//                    //add(chart_no.toString(), getRegistrationId.toString(), valuestring.toString(),birth_date.toString());
//                    //openDialog("test", "1");
//
//                }
//            }


            switch (view.getId()) {
                case R.id.button: {
                    if (value.getText().toString().equals("")) {
                        Toast.makeText(MainActivity.this, "請輸入您的身分證字號", Toast.LENGTH_SHORT).show();
                    } else {
                        Log.i("login", value.getEditableText().toString() + "我進來了");
                        if (view == login) {
                            Log.i("login", "not null");
                            A = value.getEditableText().toString();
                            Log.e("A123", A.toString());

                            valuestring = value.getEditableText().toString();
                            setting = getSharedPreferences("LoginInfo", 0);
                            setting.edit().putString("VALUESTRING", valuestring).commit();

                            getRegistrationId = gcm_id.getText().toString();
                            //getRegistrationId = gcm_id.getEditableText().toString();
                            //setting.edit().putString("GCM_String",getRegistrationId).commit();

                            mThread_post = new HandlerThread("aa");

                            mThread_post.start();

                            mThreadHandler_post = new Handler(mThread_post.getLooper());

                            mThreadHandler_post.post(r1);
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



