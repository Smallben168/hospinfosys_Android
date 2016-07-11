package tw.com.hismax.test.hospinfosys;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.THLight.USBeacon.App.Lib.BatteryPowerData;
import com.THLight.USBeacon.App.Lib.USBeaconConnection;
import com.THLight.USBeacon.App.Lib.USBeaconData;
import com.THLight.USBeacon.App.Lib.USBeaconList;
import com.THLight.USBeacon.App.Lib.USBeaconServerInfo;
import com.THLight.USBeacon.App.Lib.iBeaconData;
import com.THLight.USBeacon.App.Lib.iBeaconScanManager;
import com.THLight.Util.THLLog;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class Logout extends Activity implements iBeaconScanManager.OniBeaconScan {
    String id;
    String gcm_value;
    String result;
    int chart_no;
    String pt_name;
    TextView gcm_id;
    TextView text;
    Button logout1, button_checkin1,menu1;
    TextView pt_name1, num, doctor_name1, wait_no, _status_doc1,clinic_ps1;
    //String view_no, doctor_name, _status_doc, location_code, doctor_no, A,_status;
    String result2;
    String result3, doctor_name, current_no, _status_doc, _status,clinic_ps;
    String location_code, view_no, clinic, doctor_no, duplicate_no, prenatal_care;
    Json2 json2;
    Json3_check Json3_check;
    Handler mThreadHandler_get;
    HandlerThread mThread_get;
    Handler mThreadHandler_check;
    HandlerThread mThread_check;
    private Handler mUI_Handler;
    String b_uuid = "39EC2745-4E96-455A-B80A-03B604BF677D";
    //    String b_uuid = "";
    WebView mywebview;
    int a = 1;
    int b = 1;

    //---Ben --------S
    PatientInfoObj patient;
    SimpleDateFormat formatter;
    Date curDate;
    int timeForScaning = 1000;          //scan持續時間
    int delaySec = 5000;                //scan間隔時間
    boolean stopBln;                    //控制 每 delaySec 只發生 onScan 一次
    //String beaconUuid;                  //存放 Beacon uuid 之變數-> b_uuid
    private Handler mHandler = new Handler();
    //---------------E

    /**
     * scaner for scanning iBeacon around.
     */
    iBeaconScanManager miScaner = null;


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mainpage);
        //***Ben --- 取出 patient 資料-------s
        patient = (PatientInfoObj)getApplicationContext();

        pt_name = patient.getPtName();
        chart_no =  patient.getChartNo();
        //***Ben ---------------------------e

        menu1 = (Button) findViewById(R.id.but_menu);
        pt_name1 = (TextView) findViewById(R.id.textView1);
        num = (TextView) findViewById(R.id.textView10);
        doctor_name1 = (TextView) findViewById(R.id.textView9);
//        wait_no = (TextView) findViewById(R.id.textView13);
//        _status_doc1 = (TextView) findViewById(R.id.textView12);
        clinic_ps1 = (TextView) findViewById(R.id.textView3);

//        gcm_id = (TextView) findViewById(R.id.editRegisterId);
//        gcm_id.setMovementMethod(ScrollingMovementMethod.getInstance());

        pt_name1.setText(this.pt_name);

//        gcm_id.setText(gcm_value);


        //Log.e("chart_no_logout", chart_no);
        //id = bundle.getString("VALUE");
        //text.setText(id);
//        gcm_value = bundle.getString("GCM_String");
//        gcm_id.setText(gcm_value);

//        this.result = bundle.getString("result");
//        gcm_id.setText(this.result);

//        button_checkin1.setOnClickListener(new ClickLogout());

        menu1.setOnClickListener(new ClickLogout());
//        logout1.setOnClickListener(new ClickLogout());
        Log.e("123654", "5343543");
        mUI_Handler = new Handler();
        //Json2 呼叫事件  @@
        mThread_get = new HandlerThread("bb");
        mThread_get.start();
        mThreadHandler_get = new Handler(mThread_get.getLooper());
        mThreadHandler_get.post(g1);

        //Json3_checkin 呼叫
//        mThread_check = new HandlerThread("cc");
//        mThread_check.start();
//        mThreadHandler_check = new Handler(mThread_check.getLooper());


//        Bundle bundle = this.getIntent().getExtras();
//        this.pt_name = bundle.getString("pt_name");
//        pt_name1.setText(this.pt_name);
//        this.view_no = bundle.getString("view_no");
//        num.setText(this.view_no);
//        this.doctor_name = bundle.getString("doctor_name");
//        doctor_name1.setText(this.doctor_name);
//        this.location_code = bundle.getString("location_code");
//        wait_no.setText(this.location_code);
//        chart_no = bundle.getString("chart_no");
//        this.result = bundle.getString("result");
//        Log.e("b_uuid", b_uuid.toString());
//        Log.e("chart_no", chart_no.toString());

        //Ben
        //Bundle bundle = this.getIntent().getExtras();
        //this.pt_name = bundle.getString("pt_name");
        //pt_name1.setText(this.pt_name);
        //chart_no = bundle.getString("chart_no");
        //this.result = bundle.getString("result");

//        doctor_no = bundle.getString("doctor_no");


        /** create instance of iBeaconScanManager. */
        miScaner = new iBeaconScanManager(this, this);
        stopBln = false;
        miScaner.startScaniBeacon(timeForScaning);
        //***Ben : 建立定時器, 每 delaySec 啟動一次 scan Beacon
        mHandler.removeCallbacks(onTimer);
        mHandler.postDelayed(onTimer, delaySec);


//        mListAdapter	= new BLEListAdapter(this);

        //***Ben
        //mLVBLE = (ListView) findViewById(R.id.beacon_list);

//        mLVBLE.setAdapter(mListAdapter);
        //***Ben
        //if (!mBLEAdapter.isEnabled()) {
        //    Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        //    startActivityForResult(intent, REQ_ENABLE_BT);
        //} else {
        //    Message msg = Message.obtain(mHandler, MSG_SCAN_IBEACON, 1000, 1100);
        //    msg.sendToTarget();
        //}
        //
        ///** create store folder. */
        //File file = new File(STORE_PATH);
        //if (!file.exists()) {
        //    if (!file.mkdirs()) {
        //        Toast.makeText(this, "Create folder(" + STORE_PATH + ") failed.", Toast.LENGTH_SHORT).show();
        //    }
        //}


        //mHandler.sendEmptyMessageDelayed(MSG_UPDATE_BEACON_LIST, 500);

    }
    //***Ben : 定時器, 時間到所做之內容
    private Runnable onTimer = new Runnable() {
        @Override
        public void run() {
            //***Ben : 更新畫面內容
            //mBeaconUuid.setText(beaconUuid);
            //mScanTime.setText(formatter.format(curDate));

            //***Ben : 再重新開始 scan Beacon
            miScaner.startScaniBeacon(timeForScaning);
            stopBln = false;

            //***Ben : 設定下一次定時器啟動時間
            mHandler.postDelayed(this, delaySec);
        }
    };

    @Override
    public void onScaned(iBeaconData iBeacon) {
        //***Ben------s
        if (!stopBln) {
            synchronized(this) {
                //***Ben : 獲取當前時間

                stopBln = true;
                curDate = new Date(System.currentTimeMillis());

                Log.d("BEN", "onScaned : " + curDate);

                //*** Ben : 開始處理SCAN到訊號之事情
                //beaconUuid = iBeacon.beaconUuid;
                b_uuid = iBeacon.beaconUuid.toString();
                Log.d("BEN", "Scan Beacon, UUID = " + b_uuid);
                mThreadHandler_get.post(g1);
                //*** Ben : 事情處理結束


            }
        } else {
            //Log.d("BEN", "Scan Beacon, But not activate");
        }

        //***Ben
        //while (b <= 1) {
        //    b = b + 1;
        //    addOrUpdateiBeacon(iBeacon);
        //}
//		synchronized(mListAdapter)
//		{
//			addOrUpdateiBeacon(iBeacon);
//		}

        //this.b_uuid = iBeacon.beaconUuid.toString();
        //Log.d("BEACON", "Scan Beacon, UUID = " + b_uuid);
        //Benj暫時Mark
        //mThreadHandler_get.post(g1);
    }

    @Override
    public void onBatteryPowerScaned(BatteryPowerData batteryPowerData) {

    }


    class ClickLogout implements View.OnClickListener {

        @Override
        public void onClick(View view) {

            switch (view.getId()) {
//                case R.id.button_logout: {
//                    //Toast.makeText(Logout.this, "onClick", Toast.LENGTH_SHORT).show();
//                    if (view == logout1) {
//                        File file = new File("/data/data/tw.com.hismax.test.hospinfosys", "LoginInfo.xml");
//                        //file.delete();
//                        //Toast.makeText(Logout.this, "delete", Toast.LENGTH_SHORT).show();
//                        Intent reit = new Intent();
//                        reit.setClass(Logout.this, MainActivity.class);
//                        startActivity(reit);
//                        Logout.this.finish();
//                        file.delete();
//                    }
//                    break;
//                }
//                case R.id.button_checkin: {
//                    SendIntent();
//                }
                case R.id.but_menu: {
                    SendIntent2();

                    break;
                }
            }
//            if (view == logout1) {
//                File file = new File("/data/data/tw.com.hismax.test.hospinfosys", "LoginInfo.xml");
//                //file.delete();
//                //Toast.makeText(Logout.this, "delete", Toast.LENGTH_SHORT).show();
//                Intent reit = new Intent();
//                reit.setClass(Logout.this, MainActivity.class);
//                startActivity(reit);
//                Logout.this.finish();
//                file.delete();
//            }


        }
    }

    public void SendIntent2() {
        Intent it = new Intent();
        it.setClass(Logout.this,Menu.class);
        //Ben ----------s
        //Bundle bundle = new Bundle();
        //
        //bundle.putString("result2", this.result2);
        //bundle.putString("chart_no", this.chart_no);
        //bundle.putString("pt_name", this.pt_name);
        //
        //bundle.putString("clinic_ps", this.clinic_ps);
        //bundle.putString("view_no", this.view_no);
        //bundle.putString("doctor_name", this.doctor_name);
        //bundle.putString("_status_doc", this._status_doc);
        //bundle.putString("location_code", this.location_code);
        //bundle.putString("doctor_no", this.doctor_no);
        //bundle.putString("b_uuid", this.b_uuid);
        //it.putExtras(bundle);
        //---- Ben -------e
        startActivity(it);
        Logout.this.finish();
        //Log.e("chart_no", chart_no.toString());
        //Log.e("pt_name", pt_name.toString());
    }

    public void SendIntent() {
        Intent it = new Intent();
        it.setClass(Logout.this, Check_in.class);

        //Bundle bundle = new Bundle();

        //bundle.putString("result2", this.result2);
        //bundle.putString("chart_no", String.valueOf(this.chart_no));
        //bundle.putString("pt_name", this.pt_name);

        //bundle.putString("clinic_ps", this.clinic_ps);
        //bundle.putString("view_no", this.view_no);
        //bundle.putString("doctor_name", this.doctor_name);
        //bundle.putString("_status_doc", this._status_doc);
        //bundle.putString("location_code", this.location_code);
        //bundle.putString("doctor_no", this.doctor_no);
        //bundle.putString("b_uuid", this.b_uuid);
        //it.putExtras(bundle);
        startActivity(it);
        Logout.this.finish();
        //Log.e("chart_no", chart_no.toString());
        //Log.e("pt_name", pt_name.toString());
    }


    public Runnable g1 = new Runnable() {
        public void run() {
            json2 = new Json2(b_uuid.toString(), String.valueOf(chart_no));
            mUI_Handler.post(g2);
        }
    };

    public Runnable g2 = new Runnable() {
        public void run() {
            Logout.this.result3 = json2.getjson2();
            //Toast.makeText(MainActivity.this, json2.getString(), Toast.LENGTH_SHORT).show();
            Logout.this.view_no = json2.getview_no();
            Logout.this.doctor_name = json2.getdoctor_name();
            Logout.this._status_doc = json2.get_status_doc();
            Logout.this.location_code = json2.getlocation_code();
            Logout.this._status = json2.get_status();
            Logout.this.doctor_no = json2.getdoctor_no();
            Logout.this.clinic_ps = json2.getclinic_ps();
//            Log.e("doctor_name_g2", doctor_name.toString());

//            SendIntent();
//            Log.e("A123", A.toString() + "123");
//            Log.i("_status_doc", _status_doc.toString() + "123");

            doctor_name1.setText(json2.getdoctor_name());
//            wait_no.setText(json2.getlocation_code());
            num.setText(json2.getview_no());
            clinic_ps1.setText(json2.getclinic_ps());

//            if (_status.toString().equals("error")) {
//                _status_doc1.setText(json2.get_status_doc());
//            }
            Log.i("doctor_no", doctor_no.toString() + "123");

            WebView myWebView = (WebView) findViewById(R.id.webview);
            myWebView.getSettings().setJavaScriptEnabled(true);
            myWebView.requestFocus();
            myWebView.setWebViewClient(new MyWebViewClient());
            myWebView.loadUrl("http://163.18.22.69/static/doctor_images/" + doctor_no.toString() + ".jpeg");
        }
    };

    private class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            return super.shouldOverrideUrlLoading(view, url);
        }
    }

//    public Runnable h1 = new Runnable() {
//        public void run() {
//            Json3_check = new Json3_check(result2.toString());
//            mUI_Handler.post(h2);
//        }
//    };
//
//
//    private Runnable h2 = new Runnable() {
//
//        public void run() {
//            Logout.this.result3 = Json3_check.getjson3();
//            Logout.this.doctor_name = Json3_check.getdoctor_name();
//            Logout.this.current_no = Json3_check.getcurrent_no();
//            Logout.this.doctor_no = Json3_check.getdoctor_no();
//            Logout.this.duplicate_no = Json3_check.getduplicate_no();
//            Logout.this._status = Json3_check.get_status();
//            Logout.this.location_code = Json3_check.getlocation_code();
//            Logout.this.clinic = Json3_check.getclinic();
//            Logout.this.prenatal_care = Json3_check.getprenatal_care();
//            Logout.this.view_no = Json3_check.getview_no();
//            Logout.this._status_doc = Json3_check.get_status_doc();
//
//            SendIntent();
//        }
//    };
}
