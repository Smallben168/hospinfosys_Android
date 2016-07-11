package tw.com.hismax.test.hospinfosys;

import android.app.Fragment;
import android.app.RemoteInput;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by OH HO on 2016/6/1.
 */
public class Fragment1 extends Fragment {


    String pt_name, view_no, doctor_name;
    TextView pt_name1, num, doctor_name1;
    private Context context;
    private Handler handler;

    //TextView pt_name;
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        context = getActivity();
//        handler = new Handler();
//
//        //logout1 = (Button) findViewById(R.id.button_logout);
//        pt_name1 = (TextView) getView().findViewById(R.id.textView1);
//        num = (TextView) getView().findViewById(R.id.textView10);
//        doctor_name1 = (TextView) getView().findViewById(R.id.textView9);
//
//        //pt_name = getActivity().getIntent().getExtras().getString("pt_name");
//        Bundle bundle = getActivity().getIntent().getExtras();
//        this.pt_name = bundle.getString("pt_name");
//        pt_name1.setText(this.pt_name);
//        this.view_no = bundle.getString("view_no");
//        num.setText(this.view_no);
//        this.doctor_name = bundle.getString("doctor_name");
//        doctor_name1.setText(this.doctor_name);
//
//    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_a, container, false);

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        context = getActivity();
        handler = new Handler();

        //logout1 = (Button) findViewById(R.id.button_logout);
        pt_name1 = (TextView) getView().findViewById(R.id.textView1);
        num = (TextView) getView().findViewById(R.id.textView10);
        doctor_name1 = (TextView) getView().findViewById(R.id.textView9);

        //pt_name = getActivity().getIntent().getExtras().getString("pt_name");
        Bundle bundle = getActivity().getIntent().getExtras();
        this.pt_name = bundle.getString("pt_name");
        pt_name1.setText(this.pt_name);
        this.view_no = bundle.getString("view_no");
        num.setText(this.view_no);
        this.doctor_name = bundle.getString("doctor_name");
        doctor_name1.setText(this.doctor_name);

    }


}
