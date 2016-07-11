package tw.com.hismax.test.hospinfosys;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;

public class Test1 extends Activity {
    String id;
    String gcm_value;
    String result;
    TextView gcm_id;
    TextView text;
    Button logout;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logout);
        Toast.makeText(Test1.this, "onCreate", Toast.LENGTH_SHORT).show();
        text = (TextView) findViewById(R.id.textView);
        logout = (Button) findViewById(R.id.button_logout);
        gcm_id = (TextView) findViewById(R.id.editRegisterId);
        gcm_id.setMovementMethod(ScrollingMovementMethod.getInstance());

        Bundle bundle = this.getIntent().getExtras();
        id = bundle.getString("UUID");
        text.setText(id);
        gcm_value = bundle.getString("UUID");
        gcm_id.setText(gcm_value);

        this.result = bundle.getString("result");
        gcm_id.setText(this.result);


        //logout.setOnClickListener(new ClickLogout());
    }

    /*
    class ClickLogout implements View.OnClickListener{

        @Override
        public void onClick(View view) {
            Toast.makeText(Test1.this, "onClick", Toast.LENGTH_SHORT).show();
            if(view == logout){
                File file = new File("/data/data/tw.com.hismax.test.hospinfosys","LoginInfo.xml");
                file.delete();
                Toast.makeText(Test1.this, "delete", Toast.LENGTH_SHORT).show();
                Intent reit = new Intent();
                reit.setClass(Test1.this,MainActivity.class);
                startActivity(reit);
                Test1.this.finish();
                file.delete();
            }
        }
    }
    */
}