package tw.com.hismax.test.hospinfosys;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

/**
 * Created by OH HO on 2016/7/7.
 */
public class Hos_record extends AppCompatActivity {
    int chart_no;
    //---Ben --------S
    PatientInfoObj patient;
    //---------------E
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.web_record);
        Toast.makeText(Hos_record.this, "就醫紀錄 網頁", Toast.LENGTH_SHORT).show();
        //Ben
        //Bundle bundle = this.getIntent().getExtras();
        //chart_no = bundle.getString("chart_no");
        patient = (PatientInfoObj)getApplicationContext();
        chart_no = patient.getChartNo();


        WebView myWebView = (WebView) findViewById(R.id.webview);
        myWebView.getSettings().setJavaScriptEnabled(true);
        myWebView.requestFocus();
        myWebView.setWebViewClient(new MyWebViewClient());
        myWebView.loadUrl("http://163.18.22.69/procpatient/getlist?chart_no="+ chart_no);
    }


    private class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            return super.shouldOverrideUrlLoading(view, url);
        }
    }
}
