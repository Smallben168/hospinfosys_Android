package tw.com.hismax.test.hospinfosys;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.Toast;

/**
 * Created by OH HO on 2016/7/7.
 */
public class Hos_record extends AppCompatActivity {
    int chart_no;
    //---Ben --------S
    PatientInfoObj patient;
    Button login_home;
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

        login_home = (Button) findViewById(R.id.button_login);
        login_home.setOnClickListener(new ClickButton());

        WebView myWebView = (WebView) findViewById(R.id.webview);
        myWebView.getSettings().setJavaScriptEnabled(true);
        myWebView.requestFocus();
        myWebView.setWebViewClient(new MyWebViewClient());
        //myWebView.loadUrl("http://163.18.22.69/procpatient/getlist?chart_no="+ chart_no);
        myWebView.loadUrl("http://61.219.152.220/procpatient/getlist?chart_no="+ chart_no);
    }


    private class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            return super.shouldOverrideUrlLoading(view, url);
        }
    }
    class ClickButton implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            Intent it = new Intent();
            it.setClass(Hos_record.this, Logout.class);
            startActivity(it);
            Hos_record.this.finish();
        }
    }
}
