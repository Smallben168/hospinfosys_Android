package tw.com.hismax.test.hospinfosys;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;

import tw.com.hismax.test.hospinfosys.ui.DialogActivity;


public class Login extends Activity {
    EditText value;
    Button login;
    String valuestring = null;
    public SharedPreferences setting;
    public static File file;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        value = (EditText) findViewById(R.id.id_no);
        login = (Button) findViewById(R.id.button_login);

        login.setOnClickListener(new ClickButton());
        file = new File("/data/data/tw.com.hismax.test.hospinfosys/shared_prefs","LoginInfo.xml");
        if(file.exists()){
            ReadValue();
            Toast.makeText(Login.this, "ReadValue", Toast.LENGTH_SHORT).show();
            file.delete();
            Toast.makeText(Login.this, "delete", Toast.LENGTH_SHORT).show();
            if(!valuestring.equals("")){
                SendIntent();
                Toast.makeText(Login.this, "SendIntent", Toast.LENGTH_SHORT).show();
            }
        }
    }
    class ClickButton implements View.OnClickListener{

        @Override
        public void onClick(View view) {
            if(view == login){
                if (value != null){
                    valuestring = value.getEditableText().toString();
                    setting = getSharedPreferences("LoginInfo",0);
                    setting.edit().putString("VALUESTRING",valuestring).commit();
                    SendIntent();
                }
            }
        }
    }
    public void ReadValue(){
        setting = getSharedPreferences("LoginInfo",0);
        valuestring = setting.getString("VALUESTRING","");
    }
    public void SendIntent(){
        Intent it = new Intent();
        it.setClass(Login.this,Logout.class);
        Bundle bundle = new Bundle();
        bundle.putString("VALUE",valuestring);
        it.putExtras(bundle);
        startActivity(it);
        Login.this.finish();
    }

}
