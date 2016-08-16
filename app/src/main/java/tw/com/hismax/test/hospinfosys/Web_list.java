package tw.com.hismax.test.hospinfosys;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by OH HO on 2016/6/27.
 */
public class Web_list extends AppCompatActivity {
    Button login_home;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.web_list);

        WebView myWebView = (WebView) findViewById(R.id.webview);
        myWebView.getSettings().setJavaScriptEnabled(true);
        myWebView.requestFocus();
        myWebView.setWebViewClient(new MyWebViewClient());
        //myWebView.loadUrl("http://163.18.22.69/geteducation/");
        myWebView.loadUrl("http://61.219.152.220/geteducation/");

        login_home = (Button) findViewById(R.id.button_login);
        login_home.setOnClickListener(new ClickButton());
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
            Log.d("BEN", "Click....");
            Intent it = new Intent();
            it.setClass(Web_list.this, Logout.class);
            startActivity(it);
            Web_list.this.finish();
        }
    }
}
