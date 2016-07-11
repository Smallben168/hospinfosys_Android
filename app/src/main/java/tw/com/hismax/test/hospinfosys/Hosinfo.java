package tw.com.hismax.test.hospinfosys;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.widget.TextView;

/**
 * Created by OH HO on 2016/6/27.
 */
public class Hosinfo extends AppCompatActivity{

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hospinfo);

        TextView hosinfo = (TextView)findViewById(R.id.textView23);
//        hosinfo.setMovementMethod(ScrollingMovementMethod.getInstance());
    }
}
