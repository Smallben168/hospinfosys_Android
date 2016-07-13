package tw.com.hismax.test.hospinfosys;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by OH HO on 2016/7/2.
 */
public class Menu extends AppCompatActivity {

    String result2, chart_no, pt_name;
    String result3, doctor_name, current_no, _status_doc, _status, clinic_ps;
    String location_code, view_no, clinic, doctor_no, duplicate_no, prenatal_care;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting);

        //Ben
        //Bundle bundle = this.getIntent().getExtras();
        //chart_no = bundle.getString("chart_no");

        TextView img1 = (TextView) findViewById(R.id.textView25);

        Button btn1 = (Button) findViewById(R.id.button2);
        Button btn2 = (Button) findViewById(R.id.button3);
        Button btn3 = (Button) findViewById(R.id.button4);
        Button btn4 = (Button) findViewById(R.id.button5);
        Button btn5 = (Button) findViewById(R.id.button6);
        Button btn7 = (Button) findViewById(R.id.button7);



        btn1.setOnClickListener(new ClickButton());
        btn2.setOnClickListener(new ClickButton());
        btn3.setOnClickListener(new ClickButton());
        btn4.setOnClickListener(new ClickButton());
        btn5.setOnClickListener(new ClickButton());
        //Ben
        btn7.setOnClickListener(new ClickButton());

    }

    class ClickButton implements View.OnClickListener {
        @Override
        public void onClick(View view) {

            Toast.makeText(Menu.this, "onClick-"+view.getId(), Toast.LENGTH_SHORT).show();
            switch (view.getId()) {
                case R.id.button2: {
                    Log.e("button2", "123546");
                    Toast.makeText(Menu.this, "主畫面", Toast.LENGTH_SHORT).show();
                    Intent it = new Intent();
                    //it.setClass(Menu.this, MainActivity.class);
                    it.setClass(Menu.this, Logout.class);
                    startActivity(it);
                    Menu.this.finish();
                    break;
                }
                case R.id.button3: {
                    Toast.makeText(Menu.this, "衛教清單", Toast.LENGTH_SHORT).show();
                    Log.e("button3", "123546");
                    Intent it = new Intent();
                    it.setClass(Menu.this, Web_list.class);
                    startActivity(it);
                    Menu.this.finish();
                    break;

                }
                case R.id.button4: {
                    SendIntent2();
                    break;

                }
                case R.id.button5: {
                    Intent it = new Intent();
                    it.setClass(Menu.this, Web_list.class);
                    startActivity(it);
                    Menu.this.finish();
                    break;

                }
                case R.id.button6: {
                    Intent it = new Intent();
                    it.setClass(Menu.this, MainActivity.class);
                    startActivity(it);
                    Menu.this.finish();
                    break;

                }
                //***Ben : 會掛
                case R.id.button7: {
                    Intent it = new Intent();
                    it.setClass(Menu.this, Hospitalinfo.class);
                    startActivity(it);
                    Menu.this.finish();
                    break;

                }

            }
        }
    }

    public void SendIntent2() {
        Intent it = new Intent();
        it.setClass(Menu.this, Hos_record.class);

        //Ben
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

        //it.putExtras(bundle);

        startActivity(it);
        Menu.this.finish();

    }

}
