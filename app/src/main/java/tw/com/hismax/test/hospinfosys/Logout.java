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

    TextView gcm_id;
    TextView text;
    Button butMenu, butCmd;
    TextView txtPtName, txtViewNo, txtDoctorName, wait_no, _status_doc1,txtClinicPs, txtDoc1, txtDoc2, txtDoc3, txtDoc4, txtDoc5, txtDoc6;

    String result2;
    String result3;
    String clinic,  duplicate_no, prenatal_care;
    Json2 json2;                //***Ben : http://163.18.22.69/rest/getTodayReg/get?chart_no=" + chart_no.toString()
    Json_BeaconGet beaconGet;   //***Ben : http://163.18.22.69/rest/receiver_beacon/get?beacon_uuid
    Json_BeaconSet beaconSet;   //***Ben : http://163.18.22.69/rest/receiver_beacon/set
    Json3_check Json3_check;
    Handler mThreadHandler_get;
    HandlerThread mThread_get;
    Handler mThreadHandler_check;
    HandlerThread mThread_check;


    //    String b_uuid = "";
    WebView mywebview;
    int a = 1;
    int b = 1;

    //---Ben --------S
    Handler mUI_Handler;
    //掛號資料 ----
    PatientInfoObj patient;
    int _chart_no;
    String _pt_name;
    String _doctor_no,_doctor_name;
    String _httpResult;
    String _json_BeaconGet_Result;
    String _json_BeaconSet_Result;
    int _view_no;
    String _status_doc, _status;
    String _location_code;
    String _clinic_ps;
    int _current_no;
    String b_uuid;
    String _doc1="", _doc2="", _doc3="", _doc4="", _doc5="", _doc6="";
    Boolean butCmdVisible = false;
    String _processType = "";
    String _exceptionTime = "";
    //---------------------
    SimpleDateFormat formatter;
    Date curDate;
    int timeForScaning = 1000;          //scan持續時間
    int delaySec = 10000;                //scan間隔時間
    boolean stopBln;                    //控制 每 delaySec 只發生 onScan 一次
    //String beaconUuid;                  //存放 Beacon uuid 之變數-> b_uuid
    private Handler mHandler = new Handler();
    //int curNo;
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

        _pt_name = patient.getPtName();
        _chart_no =  patient.getChartNo();

        //***Ben ----取出元件 ---------------e
        butMenu = (Button) findViewById(R.id.but_menu);
        txtPtName = (TextView) findViewById(R.id.textView1);
        txtViewNo = (TextView) findViewById(R.id.textView10);
        txtDoctorName = (TextView) findViewById(R.id.textView9);
        txtClinicPs = (TextView) findViewById(R.id.textView3);
        txtDoc1 = (TextView) findViewById(R.id.textView_Doc1);
        txtDoc2 = (TextView) findViewById(R.id.textView_Doc2);
        txtDoc3 = (TextView) findViewById(R.id.textView_Doc3);
        txtDoc4 = (TextView) findViewById(R.id.textView_Doc4);
        txtDoc5 = (TextView) findViewById(R.id.textView_Doc5);
        txtDoc6 = (TextView) findViewById(R.id.textView_Doc6);

        butCmd =  (Button) findViewById(R.id.butCmd);

        //*** Ben --- set Value to 元件 -----
        txtPtName.setText(_pt_name);
        DisplayDocArea();
        butMenu.setOnClickListener(new ClickLogout());
        butCmd.setOnClickListener(new ClickConform());
        mUI_Handler = new Handler();

        //***Ben : call Json2 取得今日看診資料 -----------
        Thread mThread = new Thread(mainRun);
        mThread.start();


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

//Ben 暫時Test
        /** create instance of iBeaconScanManager. */
        miScaner = new iBeaconScanManager(this, this);
        stopBln = false;
        miScaner.startScaniBeacon(timeForScaning);
        //***Ben : 建立定時器, 每 delaySec 啟動一次 scan Beacon
        mHandler.removeCallbacks(onTimer);
        mHandler.postDelayed(onTimer, delaySec);
//------------

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

                //***Ben : call Json_BeaconGet 由Beacon觸發 取得今日看診資料 -----------
                Thread mThread = new Thread(getRegRecordRun);
                mThread.start();
                //mThreadHandler_get.post(getRegRecordRun);
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
    class ClickConform implements View.OnClickListener{

        @Override
        public void onClick(View view) {
            //***Ben : call Json_BeaconGet 由Beacon觸發 取得今日看診資料 -----------
            Thread mThread = new Thread(setBeaconProcessRun);
            mThread.start();
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

    public Runnable mainRun = new Runnable() {
        @Override
        public void run() {
            json2 = new Json2(String.valueOf(_chart_no));
            //------- Patient Register Record ----- Upper
            if (json2.isHaveData()) {
                _httpResult = json2.getjson2();
                _view_no = json2.getview_no();
                _doctor_no = json2.getdoctor_no();
                _doctor_name = json2.getdoctor_name();
                _status_doc = json2.get_status_doc();
                _location_code = json2.getlocation_code();
                _status = json2.get_status();
                _clinic_ps = json2.getclinic_ps();
                //------- Clinic Status ---- Bottom
                _current_no = json2.getCurrentNo();
                _doc1 = "目前看診至 : " +  _current_no;
            } else {
                _doctor_no = "";
                _doctor_name = "";
                _current_no = 0;
                _clinic_ps = "";
                _view_no = 0;
                _doc1 = "沒有掛號資料 !!";
            }
            //Ben***----- refresh screen -----
            mUI_Handler.post(runnableShow2Screen);

        }
    };

    public Runnable getRegRecordRun = new Runnable() {
        public void run() {
            beaconGet = new Json_BeaconGet(b_uuid, String.valueOf(_chart_no));
            if (beaconGet.isHaveData()) {
                _json_BeaconGet_Result = beaconGet.getResult();
                if (_json_BeaconGet_Result.equals("\"\"")) {
                    Log.d("BEN", "getRegRecordRun had Processed !! return 空字串.....");
                } else {
                    _view_no = beaconGet.getview_no();
                    _doctor_no = json2.getdoctor_no();
                    _doctor_name = json2.getdoctor_name();
                    _status_doc = json2.get_status_doc();
                    _location_code = json2.getlocation_code();
                    _status = json2.get_status();
                    _clinic_ps = json2.getclinic_ps();
                    _processType = json2.get_status_doc();
                    //------- Clinic Status ---- Bottom
                    _current_no = json2.getCurrentNo();
                    _doc1 = "目前看診至 : " + _current_no;
                    _doc2 = "確定預約報到 ? ";
                    butCmdVisible = true;
                }
            } else {
                _doctor_no = "";
                _doctor_name = "";
                _current_no = 0;
                _clinic_ps = "";
                _view_no = 0;
                _doc1 = "沒有掛號資料 !!";
                butCmdVisible = false;
            }

            //Ben***----- refresh screen -----
            mUI_Handler.post(runnableShow2Screen);
        }
    };

    public Runnable runnableShow2Screen = new Runnable() {
        public void run() {
            txtDoctorName.setText(_doctor_name);
            txtClinicPs.setText(_clinic_ps);
            txtViewNo.setText(String.valueOf(_view_no));
            if (!_doctor_no.equals("")) {
                WebView myWebView = (WebView) findViewById(R.id.webview);
                myWebView.getSettings().setJavaScriptEnabled(true);
                myWebView.requestFocus();
                myWebView.setWebViewClient(new MyWebViewClient());
                myWebView.loadUrl("http://163.18.22.69/static/doctor_images/" + String.valueOf(_doctor_no) + ".jpeg");
            }
            DisplayDocArea();
        }
    };

    private void DisplayDocArea(){
        txtDoc1.setText(_doc1);
        txtDoc2.setText(_doc2);
        txtDoc3.setText(_doc3);
        txtDoc4.setText(_doc4);
        txtDoc5.setText(_doc5);
        txtDoc6.setText(_doc6);
        if (butCmdVisible) {
            butCmd.setVisibility(View.VISIBLE);
            if (_processType.equals("REG")) butCmd.setText("預約報到");
            if (_processType.equals("PRE_DEPT_SCHEDULE")) butCmd.setText("衛教室報到排隊");
            if (_processType.equals("DEPT_SCHEDULE")) butCmd.setText("科室報到排隊");
        } else {
            butCmd.setVisibility(View.INVISIBLE);
        }
    }
    public Runnable setBeaconProcessRun = new Runnable() {
        public void run() {
            beaconSet = new Json_BeaconSet(_json_BeaconGet_Result);
            if (beaconGet.isHaveData()) {
                _json_BeaconSet_Result = beaconSet.getResult();
                if (_json_BeaconSet_Result.equals("\"\"")) {
                    Log.d("BEN", "setBeaconProcessRun had Processed !! return 空字串.....");
                } else {
                    //------- Clinic Status ---- Bottom
                    _exceptionTime = beaconSet.getExceptViewTime();
                    _doc1 = "預計看診時間為 : " + _exceptionTime;
                    _doc2 = "報到成功 !!";
                    butCmdVisible = false;
                }
            } else {
                _doctor_no = "";
                _doctor_name = "";
                _current_no = 0;
                _clinic_ps = "";
                _view_no = 0;
                _doc1 = "沒有掛號資料 !!";
                butCmdVisible = false;
            }

            //Ben***----- refresh screen -----
            mUI_Handler.post(runnableShow2Screen);
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
