package tw.com.hismax.test.hospinfosys;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by OH HO on 2016/6/1.
 */
public class Fragment_tat extends AppCompatActivity {

    String pt_name, view_no, doctor_name;
    TextView gcm_id;
    TextView text;
    Button logout1;
    TextView pt_name1, num, doctor_name1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mainpage2);

        Bundle bundle = this.getIntent().getExtras();
        this.pt_name = bundle.getString("pt_name");
        pt_name1.setText(this.pt_name);
        this.view_no = bundle.getString("view_no");
        num.setText(this.view_no);
        this.doctor_name = bundle.getString("doctor_name");
        doctor_name1.setText(this.doctor_name);
    }
}
