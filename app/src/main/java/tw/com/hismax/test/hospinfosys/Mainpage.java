package tw.com.hismax.test.hospinfosys;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import android.view.View.OnClickListener;
import android.widget.Toast;

import java.io.File;

/**
 * Created by OH HO on 2016/5/7.
 */
public class Mainpage extends AppCompatActivity{
    Toolbar toolbar;
    ActionBar actionBar;

    Button logout1;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mainpage);

        //Listen for button clicks
//        Button button = (Button)findViewById(R.id.button);
//        button.setOnClickListener(alert_show);

//        toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

        logout1 = (Button) findViewById(R.id.button_logout);
        logout1.setOnClickListener(new ClickLogout());

    }

    private OnClickListener alert_show = new OnClickListener()
    {
        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            AlertDialog.Builder dialog = new AlertDialog.Builder(Mainpage.this);
            dialog.setTitle("關於 Alert Dialog");
            dialog.setMessage("彈出對話框OK!");
            dialog.setPositiveButton(R.string.ok_label,
                    new DialogInterface.OnClickListener() {
                        public void onClick(
                                DialogInterface dialoginterface, int i){
                        }
                    });
            dialog.show();

        }


    };

    class ClickLogout implements View.OnClickListener{

        @Override
        public void onClick(View view) {
            //Toast.makeText(Logout.this, "onClick", Toast.LENGTH_SHORT).show();
            if(view == logout1){
                File file = new File("/data/data/tw.com.hismax.test.hospinfosys","LoginInfo.xml");
                file.delete();
                //Toast.makeText(Logout.this, "delete", Toast.LENGTH_SHORT).show();
                Intent reit = new Intent();
                reit.setClass(Mainpage.this,MainActivity.class);
                startActivity(reit);
                Mainpage.this.finish();
                file.delete();

            }
        }
    }


}
