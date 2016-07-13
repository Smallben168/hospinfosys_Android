package tw.com.hismax.test.hospinfosys;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

/**
 * Created by OH HO on 2016/6/27.
 */
public class Hospitalinfo extends AppCompatActivity {
    Button login_home;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hospitalinfo);

        //TextView hosinfo = (TextView)findViewById(R.id.textView23);
        //hosinfo.setMovementMethod(ScrollingMovementMethod.getInstance());

        login_home = (Button) findViewById(R.id.button_login);
        login_home.setOnClickListener(new ClickButton());
    }
    class ClickButton implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            Log.d("BEN", "Click....");
            Intent it = new Intent();
            it.setClass(Hospitalinfo.this, Logout.class);
            startActivity(it);
            Hospitalinfo.this.finish();
        }
    }
}
