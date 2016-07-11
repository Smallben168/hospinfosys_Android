package tw.com.hismax.test.hospinfosys;




import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;

public class Login_gcm extends AppCompatActivity {
    EditText gcm_id;
    EditText value;
    Button login;
    String valuestring = null;
    String getRegistrationId = null;
    public SharedPreferences setting;
    public static File file;


    private EditText editRegisterId;
    private EditText editSenderId;

    private void initProc(){

        editRegisterId = (EditText)findViewById(R.id.editRegisterId);
        editSenderId = (EditText)findViewById(R.id.editSenderId);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initProc();

        GetClientRegistrationId getClientRegistrationId = new GetClientRegistrationId(this);
        getClientRegistrationId.openGCM();
        Log.i("regid = ", getClientRegistrationId.getRegistrationId());
        //Ben取得
        // APA91bEJOQznACdSjOLAE2oGaPiaBAq2GZ59MK6UWaDWZWI8H0SIlw3amuVQpnuRgqoXugtklgbVlDCFNOEBunmUxqv6SLC8VGkgBDZOBO4f80EUc205UpH8Qhyl56snEdJaKN_443Ge
        editRegisterId.setText(getClientRegistrationId.getRegistrationId());
        //editSenderId.setText("99262725025");
        gcm_id = (EditText) findViewById(R.id.editRegisterId);



        value = (EditText) findViewById(R.id.id_no);
        login = (Button) findViewById(R.id.button_login);

        login.setOnClickListener(new ClickButton());
        file = new File("/data/data/tw.com.hismax.test.hospinfosys/shared_prefs","LoginInfo.xml");
        if(file.exists()){
            ReadValue();
            Toast.makeText(Login_gcm.this, "ReadValue", Toast.LENGTH_SHORT).show();
            file.delete();
            Toast.makeText(Login_gcm.this, "delete", Toast.LENGTH_SHORT).show();
            if(!valuestring.equals("")){
                SendIntent();
                Toast.makeText(Login_gcm.this, "SendIntent", Toast.LENGTH_SHORT).show();
            }
        }
    }

    class ClickButton implements View.OnClickListener{

        @Override
        public void onClick(View view) {
            if(view == login){
                if (value != null){
                    valuestring = value.getEditableText().toString();
                    setting = getSharedPreferences("LoginInfo", 0);
                    setting.edit().putString("VALUESTRING",valuestring).commit();

                    getRegistrationId = gcm_id.getEditableText().toString();
                    setting.edit().putString("GCM_String",getRegistrationId).commit();


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
        it.setClass(Login_gcm.this, Logout.class);
        Bundle bundle = new Bundle();
        bundle.putString("VALUE",valuestring);
        bundle.putString("GCM_String",getRegistrationId);
        it.putExtras(bundle);
        startActivity(it);
        Login_gcm.this.finish();
    }

}
