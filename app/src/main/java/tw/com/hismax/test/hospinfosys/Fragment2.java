package tw.com.hismax.test.hospinfosys;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.io.File;


public class Fragment2 extends Fragment {

    Button logout1;

    private Context context;
    private Handler handler;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        handler = new Handler();
        logout1 = (Button) getView().findViewById(R.id.button_logout);

        logout1.setOnClickListener(new ClickLogout());
    }


    @Override
    public void onPause() {
        super.onPause();

    }

    class ClickLogout implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            //Toast.makeText(Logout.this, "onClick", Toast.LENGTH_SHORT).show();
            if (view == logout1) {
                File file = new File("/data/data/tw.com.hismax.test.hospinfosys", "LoginInfo.xml");
                //file.delete();
                //Toast.makeText(Logout.this, "delete", Toast.LENGTH_SHORT).show();
                Intent reit = new Intent();
                reit.setClass(context, MainActivity.class);
                startActivity(reit);
                //Fragment2.this.finish();
                file.delete();

            }
        }
    }

}
