package tw.com.hismax.test.hospinfosys;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.List;

public class QueryMsg extends AppCompatActivity {
    Button butCmd;
    PatientInfoObj patient;
    ListView lstMsg;
    LstAdapter adapter;
    List<MessageItem> msgList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_query_msg);

        //***Ben --- 取出 patient 資料-------s
        patient = (PatientInfoObj)getApplicationContext();
        msgList = patient.getMsgList();

        //_pt_name = patient.getPtName();
        //_chart_no =  patient.getChartNo();

        //***Ben ----取出元件 ---------------e
        butCmd =  (Button) findViewById(R.id.butCmd);
        lstMsg = (ListView) findViewById(R.id.listView_Msg);

        adapter = new LstAdapter(QueryMsg.this, msgList);

    }
    public class LstAdapter extends BaseAdapter{

        LayoutInflater _lstInflater;
        List<MessageItem> _lst;
        public LstAdapter(Context context, List<MessageItem> lst){
            this._lstInflater = LayoutInflater.from(context);
            this._lst = lst;
        }
        @Override
        public int getCount() {
            return _lst.size();
        }

        @Override
        public Object getItem(int i) {
            return _lst.get(i);
        }

        @Override
        public long getItemId(int i) {
            return _lst.indexOf(getItem(i));
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            //未完
            return null;
        }
    }
}
