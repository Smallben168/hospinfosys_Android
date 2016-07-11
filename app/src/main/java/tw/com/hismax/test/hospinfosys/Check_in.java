package tw.com.hismax.test.hospinfosys;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by OH HO on 2016/6/7.
 */
public class Check_in extends Activity {
    Button logout1, button_checkin1;
    TextView pt_name1, num, doctor_name1, wait_no,_status_doc1;
    String result2,doctor_name,current_no,_status_doc,_status;
    String location_code,view_no,clinic,doctor_no,duplicate_no,prenatal_care;
    String chart_no;
    String pt_name;
    Handler mThreadHandler_get;
    HandlerThread mThread_get;
    Handler mThreadHandler_check;
    HandlerThread mThread_check;
    private Handler mUI_Handler;
    Json3_check json3_check;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mainpage);
        //Toast.makeText(Logout.this, "onCreate", Toast.LENGTH_SHORT).show();
        //text = (TextView) findViewById(R.id.textView);
        button_checkin1 = (Button) findViewById(R.id.button_checkin);
        logout1 = (Button) findViewById(R.id.button_logout);
        pt_name1 = (TextView) findViewById(R.id.textView1);
        num = (TextView) findViewById(R.id.textView10);
        doctor_name1 = (TextView) findViewById(R.id.textView9);
        wait_no = (TextView) findViewById(R.id.textView13);
        _status_doc1 = (TextView) findViewById(R.id.textView12);
//        button_checkin1.setOnClickListener(new ClickLogout());
//        logout1.setOnClickListener(new ClickLogout());

        Bundle bundle = this.getIntent().getExtras();
        this.pt_name = bundle.getString("pt_name");
        pt_name1.setText(this.pt_name);
        this.view_no = bundle.getString("view_no");
        num.setText(this.view_no);
        this.doctor_name = bundle.getString("doctor_name");
        doctor_name1.setText(this.doctor_name);
        this.location_code = bundle.getString("location_code");
        wait_no.setText(this.location_code);
        chart_no = bundle.getString("chart_no");
        this.result2 = bundle.getString("result2");

        mUI_Handler = new Handler();
        //Json2 呼叫事件  @@
        mThread_get = new HandlerThread("bb");
        mThread_get.start();
        mThreadHandler_get = new Handler(mThread_get.getLooper());
        mThreadHandler_get.post(f1);


    }

    public Runnable f1 = new Runnable() {
        public void run() {
            json3_check = new Json3_check(result2.toString());
            mUI_Handler.post(f2);
        }
    };
String card_seq;
    public Runnable f2 = new Runnable() {
        public void run() {
            Check_in.this.result2 = json3_check.getjson3();
            //Toast.makeText(MainActivity.this, json2.getString(), Toast.LENGTH_SHORT).show();
            Check_in.this.view_no = json3_check.getview_no();
            Check_in.this.doctor_name = json3_check.getdoctor_name();
            Check_in.this._status_doc = json3_check.get_status_doc();
            Check_in.this.location_code = json3_check.getlocation_code();
            Check_in.this._status = json3_check.get_status();
            Check_in.this.card_seq = json3_check.getcard_seq();
            Log.e("doctor_name_g2", doctor_name.toString());
//            SendIntent();
//            Log.e("A123", A.toString() + "123");
//            Log.i("_status_doc", _status_doc.toString() + "123");


            if (_status.toString().equals("error")) {
                _status_doc1.setText(json3_check.get_status_doc());
            }else{

                _status_doc1.setText(json3_check.get_status_doc());
            }
            Log.i("doctor_no", doctor_no.toString() + "123");
//            WebView webview = new WebView(this);
//            webview.getSettings().setJavaScriptEnabled(true);
//            webview.requestFocus();
//            setContentView(webview);
//            webview.loadUrl("http://163.18.22.69/static/doctor_images/" + doctor_no.toString() + ".jpeg");
        }
    };


}
