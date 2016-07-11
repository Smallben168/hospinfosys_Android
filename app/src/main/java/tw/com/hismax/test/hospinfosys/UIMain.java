/** ============================================================== */
package tw.com.hismax.test.hospinfosys;
/** ============================================================== */
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.THLight.USBeacon.App.Lib.BatteryPowerData;
import com.THLight.USBeacon.App.Lib.USBeaconConnection;
import com.THLight.USBeacon.App.Lib.USBeaconData;
import com.THLight.USBeacon.App.Lib.USBeaconList;
import com.THLight.USBeacon.App.Lib.USBeaconServerInfo;
import com.THLight.USBeacon.App.Lib.iBeaconData;
import com.THLight.USBeacon.App.Lib.iBeaconScanManager;

import com.THLight.Util.THLLog;

/** ============================================================== */
public class UIMain extends AppCompatActivity implements iBeaconScanManager.OniBeaconScan, USBeaconConnection.OnResponse
{
    //Logout 的部分
    String id;
    String gcm_value;
    String result;
    String chart_no;
    String pt_name;
    TextView gcm_id;
    TextView text;
    Button logout1,button_checkin1;
    TextView pt_name1, num, doctor_name1,wait_no;
    String view_no, doctor_name, _status_doc, location_code, doctor_no,A;
    String  result2;
    Json2 json2;
    tw.com.hismax.test.hospinfosys.Json3_check Json3_check;
    Handler mThreadHandler_get;
    HandlerThread mThread_get;
    Handler mThreadHandler_check;
    HandlerThread mThread_check;
    private Handler mUI_Handler;
    String b_uuid = "D5E9DBE2-D9F7-4564-A6C8-57A38C5FA6F0";



    /** this UUID is generate by Server while register a new account. */
    final UUID QUERY_UUID		= UUID.fromString("BB746F72-282F-4378-9416-89178C1019FC");
    /** server http api url. */
    final String HTTP_API		= "http://www.usbeacon.com.tw/api/func";

    static String STORE_PATH	= Environment.getExternalStorageDirectory().toString()+ "/USBeaconSample/";

    final int REQ_ENABLE_BT		= 2000;
    final int REQ_ENABLE_WIFI	= 2001;

    final int MSG_SCAN_IBEACON			= 1000;
    final int MSG_UPDATE_BEACON_LIST	= 1001;
    final int MSG_START_SCAN_BEACON		= 2000;
    final int MSG_STOP_SCAN_BEACON		= 2001;
    final int MSG_SERVER_RESPONSE		= 3000;

    final int TIME_BEACON_TIMEOUT		= 30000;

    THLApp App		= null;
    THLConfig Config= null;

    BluetoothAdapter mBLEAdapter= BluetoothAdapter.getDefaultAdapter();

    /** scaner for scanning iBeacon around. */
    iBeaconScanManager miScaner	= null;

    /** USBeacon server. */
    USBeaconConnection mBServer	= new USBeaconConnection();

    USBeaconList mUSBList		= null;

    ListView mLVBLE= null;

    BLEListAdapter mListAdapter		= null;

    List<ScanediBeacon> miBeacons	= new ArrayList<ScanediBeacon>();

    /** ================================================ */
    Handler mHandler= new Handler()
    {
        @Override
        public void handleMessage(Message msg)
        {
            switch(msg.what)
            {
                case MSG_SCAN_IBEACON:
                {
                    int timeForScaning		= msg.arg1;
                    int nextTimeStartScan	= msg.arg2;

                    miScaner.startScaniBeacon(timeForScaning);
                    this.sendMessageDelayed(Message.obtain(msg), nextTimeStartScan);
                    //Toast.makeText(UIMain.this, "SCAN_IBEACON", Toast.LENGTH_SHORT).show();
                }
                break;

                case MSG_UPDATE_BEACON_LIST:
                    synchronized(mListAdapter)
                    {
                        verifyiBeacons();
                        mListAdapter.notifyDataSetChanged();
                        mHandler.sendEmptyMessageDelayed(MSG_UPDATE_BEACON_LIST, 500);
                        //Toast.makeText(UIMain.this, "UPDATE_BEACON_LIST", Toast.LENGTH_SHORT).show();
                    }
                    break;

                case MSG_SERVER_RESPONSE:
                    switch(msg.arg1)
                    {
                        case USBeaconConnection.MSG_NETWORK_NOT_AVAILABLE:
                            break;

                        case USBeaconConnection.MSG_HAS_UPDATE:
                            mBServer.downloadBeaconListFile();
                            Toast.makeText(UIMain.this, "HAS_UPDATE.", Toast.LENGTH_SHORT).show();
                            break;

                        case USBeaconConnection.MSG_HAS_NO_UPDATE:
                            Toast.makeText(UIMain.this, "No new BeaconList.", Toast.LENGTH_SHORT).show();
                            break;

                        case USBeaconConnection.MSG_DOWNLOAD_FINISHED:
                            //Toast.makeText(UIMain.this, "DOWNLOAD_FINISHED", Toast.LENGTH_SHORT).show();
                            break;

                        case USBeaconConnection.MSG_DOWNLOAD_FAILED:
                            Toast.makeText(UIMain.this, "Download file failed!", Toast.LENGTH_SHORT).show();
                            break;

                        case USBeaconConnection.MSG_DATA_UPDATE_FINISHED:
                        {
                            USBeaconList BList= mBServer.getUSBeaconList();

                            if(null == BList)
                            {
                                Toast.makeText(UIMain.this, "Data Updated failed.", Toast.LENGTH_SHORT).show();
                                THLLog.d("debug", "update failed.");
                            }
                            else if(BList.getList().isEmpty())
                            {
                                Toast.makeText(UIMain.this, "Data Updated but empty.", Toast.LENGTH_SHORT).show();
                                THLLog.d("debug", "this account doesn't contain any devices.");
                            }
                            else
                            {
                                Toast.makeText(UIMain.this, "Data Updated("+ BList.getList().size()+ ")", Toast.LENGTH_SHORT).show();

                                for(USBeaconData data : BList.getList())
                                {
                                    THLLog.d("debug", "Name("+ data.name+ "), Ver("+ data.major+ "."+ data.minor+ ")");
                                }
                            }
                        }
                        break;

                        case USBeaconConnection.MSG_DATA_UPDATE_FAILED:
                            //Toast.makeText(UIMain.this, "UPDATE_FAILED!", Toast.LENGTH_SHORT).show();
                            break;
                    }
                    break;
            }
        }
    };

    /** ================================================ */
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ui_main);


        //Logout 的部分
        button_checkin1 = (Button) findViewById(R.id.button_checkin);
        logout1 = (Button) findViewById(R.id.button_logout);
        pt_name1 = (TextView) findViewById(R.id.textView1);
        num = (TextView) findViewById(R.id.textView10);
        doctor_name1 = (TextView) findViewById(R.id.textView9);
        wait_no = (TextView) findViewById(R.id.textView13);
        button_checkin1.setOnClickListener(new ClickLogout());
        logout1.setOnClickListener(new ClickLogout());

        mUI_Handler = new Handler();
        //Json2 呼叫事件  @@
        mThread_get = new HandlerThread("bb");
        mThread_get.start();
        mThreadHandler_get = new Handler(mThread_get.getLooper());
        mThreadHandler_get.post(g1);

        Bundle bundle = this.getIntent().getExtras();
        this.pt_name = bundle.getString("pt_name");
        pt_name1.setText(this.pt_name);
        chart_no = bundle.getString("chart_no");
        this.result = bundle.getString("result");


        Toast.makeText(this, "Beacon start", Toast.LENGTH_LONG).show();

        App		= THLApp.getApp();
        Config	= THLApp.Config;

        /** create instance of iBeaconScanManager. */
        miScaner		= new iBeaconScanManager(this, this);

        mListAdapter	= new BLEListAdapter(this);

        mLVBLE			= (ListView)findViewById(R.id.beacon_list);
        mLVBLE.setAdapter(mListAdapter);

        if(!mBLEAdapter.isEnabled())
        {
            Intent intent= new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(intent, REQ_ENABLE_BT);
        }
        else
        {
            Message msg= Message.obtain(mHandler, MSG_SCAN_IBEACON, 1000, 1100);
            msg.sendToTarget();
        }

        /** create store folder. */
        File file= new File(STORE_PATH);
        if(!file.exists())
        {
            if(!file.mkdirs())
            {
                Toast.makeText(this, "Create folder("+ STORE_PATH+ ") failed.", Toast.LENGTH_SHORT).show();
            }
        }

        /** check network is available or not. */
        ConnectivityManager cm	= (ConnectivityManager)getSystemService(UIMain.CONNECTIVITY_SERVICE);
        if(null != cm)
        {
            NetworkInfo ni = cm.getActiveNetworkInfo();
            if(null == ni || (!ni.isConnected()))
            {
                dlgNetworkNotAvailable();
            }
            else
            {
                THLLog.d("debug", "NI not null");

                NetworkInfo niMobile= cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
                if(null != niMobile)
                {
                    boolean is3g	= niMobile.isConnectedOrConnecting();

                    if(is3g)
                    {
                        dlgNetwork3G();
                    }
                    else
                    {
                        USBeaconServerInfo info= new USBeaconServerInfo();

                        info.serverUrl		= HTTP_API;
                        info.queryUuid		= QUERY_UUID;
                        info.downloadPath	= STORE_PATH;

                        mBServer.setServerInfo(info, this);
                        mBServer.checkForUpdates();
                    }
                }
            }
        }
        else
        {
            THLLog.d("debug", "CM null");
        }

        mHandler.sendEmptyMessageDelayed(MSG_UPDATE_BEACON_LIST, 500);


    }

    //Logout 的部分
    class ClickLogout implements View.OnClickListener {
        @Override
        public void onClick(View view) {

//            switch (view.getId()){
//                case R.id.button_logout:
//                {
//                    //Toast.makeText(Logout.this, "onClick", Toast.LENGTH_SHORT).show();
//                    if (view == logout1) {
//                        File file = new File("/data/data/tw.com.hismax.test.hospinfosys", "LoginInfo.xml");
//                        //file.delete();
//                        //Toast.makeText(Logout.this, "delete", Toast.LENGTH_SHORT).show();
//                        Intent reit = new Intent();
//                        reit.setClass(Logout.this, MainActivity.class);
//                        startActivity(reit);
//                        Logout.this.finish();
//                        file.delete();
//                    }
//                    break;
//                }
//                case R.id.button_checkin:{
//
//
//
//                }
//                }
            if (view == logout1) {
                File file = new File("/data/data/tw.com.hismax.test.hospinfosys", "LoginInfo.xml");
                //file.delete();
                //Toast.makeText(Logout.this, "delete", Toast.LENGTH_SHORT).show();
                Intent reit = new Intent();
                reit.setClass(UIMain.this, MainActivity.class);
                startActivity(reit);
                UIMain.this.finish();
                file.delete();
            }


        }
    }

    public Runnable g1 = new Runnable() {
        public void run() {
            json2 = new Json2(b_uuid.toString(), chart_no.toString());
            mUI_Handler.post(g2);
        }
    };

    public Runnable g2 = new Runnable() {
        public void run() {
            UIMain.this.result2 = json2.getjson2();
            //Toast.makeText(MainActivity.this, json2.getString(), Toast.LENGTH_SHORT).show();
            UIMain.this.view_no = json2.getview_no();
            UIMain.this.doctor_name = json2.getdoctor_name();
            UIMain.this._status_doc = json2.get_status_doc();
            UIMain.this.location_code = json2.getlocation_code();
            UIMain.this.doctor_no = json2.getdoctor_no();
            Log.e("doctor_name_g2", doctor_name.toString());

            doctor_name1.setText(json2.getdoctor_name());
            wait_no.setText(json2.getlocation_code());
            num.setText(json2.getview_no());
        }
    };

    /** ================================================ */
    @Override
    public void onResume()
    {
        super.onResume();
    }

    /** ================================================ */
    @Override
    public void onPause()
    {
        super.onPause();
    }

    /** ================================================ */
    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
    }

    /** ================================================ */
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        THLLog.d("DEBUG", "onActivityResult()");

        switch(requestCode)
        {
            case REQ_ENABLE_BT:
                if(RESULT_OK == resultCode)
                {
                }
                break;

            case REQ_ENABLE_WIFI:
                if(RESULT_OK == resultCode)
                {
                }
                break;
        }
    }

    /** ================================================ */
    /** implementation of {@link iBeaconScanManager#OniBeaconScan } */
    @Override
    public void onScaned(iBeaconData iBeacon)
    {
        synchronized(mListAdapter)
        {
            addOrUpdateiBeacon(iBeacon);
            Log.e("beacon1", iBeacon.toString());
        }

    }
    /** ================================================ */
    /** implementation of {@link iBeaconScanManager#OniBeaconScan } */
    @Override
    public void onBatteryPowerScaned(BatteryPowerData batteryPowerData) {
        // TODO Auto-generated method stub
        Log.d("debug", batteryPowerData.batteryPower+"");
        for(int i = 0 ; i < miBeacons.size() ; i++)
        {
            if(miBeacons.get(i).macAddress.equals(batteryPowerData.macAddress))
            {
                ScanediBeacon ib = miBeacons.get(i);
                ib.batteryPower = batteryPowerData.batteryPower;
                miBeacons.set(i, ib);
                Log.e("beacon4", batteryPowerData.toString());
            }
        }
    }

    /** ========================================================== */
    public void onResponse(int msg)
    {
        THLLog.d("debug", "Response(" + msg + ")");
        mHandler.obtainMessage(MSG_SERVER_RESPONSE, msg, 0).sendToTarget();
    }

    /** ========================================================== */
    public void dlgNetworkNotAvailable()
    {
        final AlertDialog dlg = new AlertDialog.Builder(UIMain.this).create();

        dlg.setTitle("Network");
        dlg.setMessage("Please enable your network for updating beacon list.");

        dlg.setButton(AlertDialog.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int id)
            {
                dlg.dismiss();
            }
        });

        dlg.show();
    }

    /** ========================================================== */
    public void dlgNetwork3G()
    {
        final AlertDialog dlg = new AlertDialog.Builder(UIMain.this).create();

        dlg.setTitle("3G");
        dlg.setMessage("App will send/recv data via 3G, this may result in significant data charges.");

        dlg.setButton(AlertDialog.BUTTON_POSITIVE, "Allow", new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int id)
            {
                Config.allow3G= true;
                dlg.dismiss();
                USBeaconServerInfo info= new USBeaconServerInfo();

                info.serverUrl		= HTTP_API;
                info.queryUuid		= QUERY_UUID;
                info.downloadPath	= STORE_PATH;

                mBServer.setServerInfo(info, UIMain.this);
                mBServer.checkForUpdates();
            }
        });

        dlg.setButton(AlertDialog.BUTTON_NEGATIVE, "Reject", new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int id)
            {
                Config.allow3G= false;
                dlg.dismiss();
            }
        });

        dlg.show();
    }

    /** ========================================================== */
    public void addOrUpdateiBeacon(iBeaconData iBeacon)
    {
        long currTime= System.currentTimeMillis();

        ScanediBeacon beacon= null;

        for(ScanediBeacon b : miBeacons)
        {
            if(b.equals(iBeacon, false))
            {
                beacon= b;
                break;
            }
        }

        if(null == beacon)
        {
            beacon= ScanediBeacon.copyOf(iBeacon);
            miBeacons.add(beacon);
            Log.e("beacon",beacon.toString());
        }
        else
        {
            beacon.rssi= iBeacon.rssi;
        }

        beacon.lastUpdate= currTime;
        Log.e("beacon5", beacon.beaconUuid.toString());
        Log.e("beacon7", String.valueOf(beacon.major));

    }

    /** ========================================================== */
    public void verifyiBeacons()
    {
        {
            long currTime	= System.currentTimeMillis();

            int len= miBeacons.size();
            ScanediBeacon beacon= null;

            for(int i= len- 1; 0 <= i; i--)
            {
                beacon= miBeacons.get(i);

                if(null != beacon && TIME_BEACON_TIMEOUT < (currTime- beacon.lastUpdate))
                {
                    miBeacons.remove(i);
                }
            }
        }

        {
            mListAdapter.clear();

            for(ScanediBeacon beacon : miBeacons)
            {
                mListAdapter.addItem(new ListItem(beacon.beaconUuid.toString().toUpperCase(), ""+ beacon.major, ""+ beacon.minor, ""+ beacon.rssi,""+beacon.batteryPower));
                Log.e("beacon3", beacon.beaconUuid.toString());
                Log.e("beacon6", String.valueOf(beacon.major));
            }
        }
    }

    /** ========================================================== */
    public void cleariBeacons()
    {
        mListAdapter.clear();
    }

}

/** ============================================================== */
class ListItem
{
    public String text1= "";
    public String text2= "";
    public String text3= "";
    public String text4= "";
    public String text5= "";
    public String text6= "";

    public ListItem()
    {
    }

    public ListItem(String text1, String text2, String text3, String text4, String text5)
    {
        this.text1= text1;
        this.text2= text2;
        this.text3= text3;
        this.text4= text4;
        this.text5= text5;
        this.text6= text6;
    }
}

/** ============================================================== */
class BLEListAdapter extends BaseAdapter
{
    private Context mContext;

    List<ListItem> mListItems= new ArrayList<ListItem>();

    /** ================================================ */
    public BLEListAdapter(Context c) { mContext= c; }

    /** ================================================ */
    public int getCount() { return mListItems.size(); }

    /** ================================================ */
    public Object getItem(int position)
    {
        if((!mListItems.isEmpty()) && mListItems.size() > position)
        {
            return mListItems.toArray()[position];
        }

        return null;
    }

    public String getItemText(int position)
    {
        if((!mListItems.isEmpty()) && mListItems.size() > position)
        {
            return ((ListItem)mListItems.toArray()[position]).text1;
        }

        return null;
    }

    /** ================================================ */
    public long getItemId(int position) { return 0; }

    /** ================================================ */
    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent)
    {
        View view= (View)convertView;

        if(null == view)
            view= View.inflate(mContext, R.layout.item_text_3, null);

        //view.setLayoutParams(new AbsListView.LayoutParams(RadioGroup.LayoutParams.FILL_PARENT, RadioGroup.LayoutParams.WRAP_CONTENT));

        if(!mListItems.isEmpty() && (mListItems.size() > position))
        {

            TextView text1	= (TextView)view.findViewById(R.id.it3_text1);
            TextView text2	= (TextView)view.findViewById(R.id.it3_text2);
            TextView text3	= (TextView)view.findViewById(R.id.it3_text3);
            TextView text4	= (TextView)view.findViewById(R.id.it3_text4);
            TextView text5	= (TextView)view.findViewById(R.id.it3_text5);
            TextView text6	= (TextView)view.findViewById(R.id.it3_text6);

            ListItem item= (ListItem)mListItems.toArray()[position];


            text1.setText(item.text1);
            text2.setText(item.text2);
            text3.setText(item.text3);
            text4.setText(item.text4+ " dbm");
            text5.setText(item.text5+ " V");
            text6.setText(item.text6+"確定");
            String abc = "D5E9DBE2-D9F7-4564-A6C8-57A38C5FA6F0";
            String t1 = "" + text1.getText();//直接強制轉型



            if(t1.equals(abc))
            {
                Log.i("t1.equals(abc)","qw");
            }

        }
        else
        {
            view.setVisibility(View.GONE);
        }
        return view;
    }

    /** ================================================ */
    @Override
    public boolean isEnabled(int position)
    {
        if(mListItems.size() <= position)
            return false;

        return true;
    }

    /** ================================================ */
    public boolean addItem(ListItem item)
    {
        mListItems.add(item);
        return true;
    }

    /** ================================================ */
    public void clear()
    {
        mListItems.clear();
    }


}

/** ============================================================== */

//
///**
// * ==============================================================
// */
//package tw.com.hismax.test.hospinfosys;
///**
// * ==============================================================
// */
//
//import android.app.Activity;
//import android.app.AlertDialog;
//import android.app.Service;
//import android.bluetooth.BluetoothAdapter;
//import android.content.Context;
//import android.content.DialogInterface;
//import android.content.Intent;
//import android.net.ConnectivityManager;
//import android.net.NetworkInfo;
//import android.os.Bundle;
//import android.os.Environment;
//import android.os.Handler;
//import android.os.IBinder;
//import android.os.Message;
//import android.support.annotation.Nullable;
//import android.util.Log;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.BaseAdapter;
//import android.widget.ListAdapter;
//import android.widget.ListView;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.THLight.USBeacon.App.Lib.BatteryPowerData;
//import com.THLight.USBeacon.App.Lib.USBeaconConnection;
//import com.THLight.USBeacon.App.Lib.USBeaconData;
//import com.THLight.USBeacon.App.Lib.USBeaconList;
//import com.THLight.USBeacon.App.Lib.USBeaconServerInfo;
//import com.THLight.USBeacon.App.Lib.iBeaconData;
//import com.THLight.USBeacon.App.Lib.iBeaconScanManager;
//
//import tw.com.hismax.test.hospinfosys.Logout;
//import tw.com.hismax.test.hospinfosys.ScanediBeacon;
//import tw.com.hismax.test.hospinfosys.THLApp;
//import tw.com.hismax.test.hospinfosys.THLConfig;
//import tw.com.hismax.test.hospinfosys.R;
//import tw.com.hismax.test.hospinfosys.Test1;
//
//import com.THLight.Util.THLLog;
//
//import java.io.File;
//import java.security.Provider;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.UUID;
//
//import android.app.Service;
//
///**
// * ==============================================================
// */
//public class UIMain extends Service implements iBeaconScanManager.OniBeaconScan, USBeaconConnection.OnResponse {
//    /**
//     * this UUID is generate by Server while register a new account.
//     */
//    final UUID QUERY_UUID = UUID.fromString("BB746F72-282F-4378-9416-89178C1019FC");
//    /**
//     * server http api url.
//     */
//    final String HTTP_API = "http://www.usbeacon.com.tw/api/func";
//
//    static String STORE_PATH = Environment.getExternalStorageDirectory().toString() + "/USBeaconSample/";
//
//    final int REQ_ENABLE_BT = 2000;
//    final int REQ_ENABLE_WIFI = 2001;
//
//    final int MSG_SCAN_IBEACON = 1000;
//    final int MSG_UPDATE_BEACON_LIST = 1001;
//    final int MSG_START_SCAN_BEACON = 2000;
//    final int MSG_STOP_SCAN_BEACON = 2001;
//    final int MSG_SERVER_RESPONSE = 3000;
//
//    final int TIME_BEACON_TIMEOUT = 30000;
//
//    THLApp App = null;
//    THLConfig Config = null;
//
//    BluetoothAdapter mBLEAdapter = BluetoothAdapter.getDefaultAdapter();
//
//    /**
//     * scaner for scanning iBeacon around.
//     */
//    iBeaconScanManager miScaner = null;
//
//    /**
//     * USBeacon server.
//     */
//    USBeaconConnection mBServer = new USBeaconConnection();
//
//    USBeaconList mUSBList = null;
//
//    ListView mLVBLE = null;
//
////	BLEListAdapter mListAdapter		= null;
//
//    List<ScanediBeacon> miBeacons = new ArrayList<ScanediBeacon>();
//
//    /**
//     * ================================================
//     */
//    Handler mHandler = new Handler() {
//        @Override
//        public void handleMessage(Message msg) {
//            switch (msg.what) {
//                case MSG_SCAN_IBEACON: {
//                    int timeForScaning = msg.arg1;
//                    int nextTimeStartScan = msg.arg2;
//
//                    miScaner.startScaniBeacon(timeForScaning);
//                    this.sendMessageDelayed(Message.obtain(msg), nextTimeStartScan);
//                    //Toast.makeText(UIMain.this, "SCAN_IBEACON", Toast.LENGTH_SHORT).show();
//                }
//                break;
//
////				case MSG_UPDATE_BEACON_LIST:
////				synchronized(mListAdapter)
////				{
////					verifyiBeacons();
////					mListAdapter.notifyDataSetChanged();
////					mHandler.sendEmptyMessageDelayed(MSG_UPDATE_BEACON_LIST, 500);
////					//Toast.makeText(UIMain.this, "UPDATE_BEACON_LIST", Toast.LENGTH_SHORT).show();
////				}
////				break;
//
//                case MSG_SERVER_RESPONSE:
//                    switch (msg.arg1) {
//                        case USBeaconConnection.MSG_NETWORK_NOT_AVAILABLE:
//                            break;
//
//                        case USBeaconConnection.MSG_HAS_UPDATE:
//                            mBServer.downloadBeaconListFile();
//                            Toast.makeText(UIMain.this, "HAS_UPDATE.", Toast.LENGTH_SHORT).show();
//                            break;
//
//                        case USBeaconConnection.MSG_HAS_NO_UPDATE:
//                            Toast.makeText(UIMain.this, "No new BeaconList.", Toast.LENGTH_SHORT).show();
//                            break;
//
//                        case USBeaconConnection.MSG_DOWNLOAD_FINISHED:
//                            //Toast.makeText(UIMain.this, "DOWNLOAD_FINISHED", Toast.LENGTH_SHORT).show();
//                            break;
//
//                        case USBeaconConnection.MSG_DOWNLOAD_FAILED:
//                            Toast.makeText(UIMain.this, "Download file failed!", Toast.LENGTH_SHORT).show();
//                            break;
//
////						case USBeaconConnection.MSG_DATA_UPDATE_FINISHED:
////							{
////								USBeaconList BList= mBServer.getUSBeaconList();
////
////								if(null == BList)
////								{
////									Toast.makeText(UIMain.this, "Data Updated failed.", Toast.LENGTH_SHORT).show();
////									THLLog.d("debug", "update failed.");
////								}
////								else if(BList.getList().isEmpty())
////								{
////									Toast.makeText(UIMain.this, "Data Updated but empty.", Toast.LENGTH_SHORT).show();
////									THLLog.d("debug", "this account doesn't contain any devices.");
////								}
////								else
////								{
////									Toast.makeText(UIMain.this, "Data Updated(" + BList.getList().size() + ")", Toast.LENGTH_SHORT).show();
////
////									for(USBeaconData data : BList.getList())
////									{
////										THLLog.d("debug", "Name("+ data.name+ "), Ver("+ data.major+ "."+ data.minor+ ")");
////									}
////								}
////							}
////							break;
//
//                        case USBeaconConnection.MSG_DATA_UPDATE_FAILED:
//                            //Toast.makeText(UIMain.this, "UPDATE_FAILED!", Toast.LENGTH_SHORT).show();
//                            break;
//                    }
//                    break;
//            }
//        }
//    };
//
//    String uuidstring1 = null;
//    String majorid = null;
//
//    /**
//     * ================================================
//     */
//
//    public IBinder onBind(Intent intent)
//
//    {
//        return null;
//    }
//
//    //@Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate();
//        //super.onCreate(savedInstanceState);
//        //setContentView(R.layout.ui_main);
//
//        Toast.makeText(this, "Beacon start", Toast.LENGTH_SHORT).show();
//
//        App = THLApp.getApp();
//        Config = THLApp.Config;
//
//        /** create instance of iBeaconScanManager. */
//        miScaner = new iBeaconScanManager(this, this);
//
//        //mListAdapter	= new BLEListAdapter(this);
//
//        //mLVBLE			= (ListView)findViewById(R.id.beacon_list);
//        //mLVBLE.setAdapter(mListAdapter);
//
//        if (!mBLEAdapter.isEnabled()) {
//            Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
//            //startActivityForResult(intent, REQ_ENABLE_BT);
//        } else {
//            Message msg = Message.obtain(mHandler, MSG_SCAN_IBEACON, 1000, 1100);
//            msg.sendToTarget();
//        }
//
//        /** create store folder. */
//        File file = new File(STORE_PATH);
//        if (!file.exists()) {
//            if (!file.mkdirs()) {
//                Toast.makeText(this, "Create folder(" + STORE_PATH + ") failed.", Toast.LENGTH_SHORT).show();
//            }
//        }
//
//        /** check network is available or not. */
//        ConnectivityManager cm = (ConnectivityManager) getSystemService(UIMain.CONNECTIVITY_SERVICE);
//        if (null != cm) {
//            NetworkInfo ni = cm.getActiveNetworkInfo();
//            if (null == ni || (!ni.isConnected())) {
//                dlgNetworkNotAvailable();
//            } else {
//                THLLog.d("debug", "NI not null");
//
//                NetworkInfo niMobile = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
//                if (null != niMobile) {
//                    boolean is3g = niMobile.isConnectedOrConnecting();
//
//                    if (is3g) {
//                        dlgNetwork3G();
//                    } else {
//                        USBeaconServerInfo info = new USBeaconServerInfo();
//
//                        info.serverUrl = HTTP_API;
//                        info.queryUuid = QUERY_UUID;
//                        info.downloadPath = STORE_PATH;
//
//                        mBServer.setServerInfo(info, this);
//                        mBServer.checkForUpdates();
//                    }
//                }
//            }
//        } else {
//            THLLog.d("debug", "CM null");
//        }
//
//        mHandler.sendEmptyMessageDelayed(MSG_UPDATE_BEACON_LIST, 500);
//    }
//
//
//    /** ================================================ */
////	@Override
////	public void onResume()
////	{
////		super.onResume();
////	}
////
////	/** ================================================ */
////	@Override
////	public void onPause()
////	{
////		super.onPause();
////	}
////
////	/** ================================================ */
////	@Override
////	public void onBackPressed()
////	{
////		super.onBackPressed();
////	}
//
//    /** ================================================ */
////    protected void onActivityResult(int requestCode, int resultCode, Intent data)
////  	{
////  		THLLog.d("DEBUG", "onActivityResult()");
////
////  		switch(requestCode)
////  		{
////  			case REQ_ENABLE_BT:
////	  			if(RESULT_OK == resultCode)
////	  			{
////				}
////	  			break;
////
////  			case REQ_ENABLE_WIFI:
////  				if(RESULT_OK == resultCode)
////	  			{
////				}
////  				break;
////  		}
////  	}
//
//    /** ================================================ */
//    /**
//     * implementation of {@link iBeaconScanManager#OniBeaconScan }
//     */
//
//    public void onScaned(iBeaconData iBeacon) {
//        addOrUpdateiBeacon(iBeacon);
//
////		synchronized(mListAdapter)
////		{
////			addOrUpdateiBeacon(iBeacon);
////		}
//    }
//    /** ================================================ */
//    /**
//     * implementation of {@link iBeaconScanManager#OniBeaconScan }
//     */
//    @Override
//    public void onBatteryPowerScaned(BatteryPowerData batteryPowerData) {
//        // TODO Auto-generated method stub
//        Log.d("debug", batteryPowerData.batteryPower + "");
//        for (int i = 0; i < miBeacons.size(); i++) {
//            if (miBeacons.get(i).macAddress.equals(batteryPowerData.macAddress)) {
//                ScanediBeacon ib = miBeacons.get(i);
//                ib.batteryPower = batteryPowerData.batteryPower;
//                miBeacons.set(i, ib);
//            }
//        }
//    }
//
//    public int onStartCommand(Intent intent, int flags, int startId) {
////		new Thread() {
////			public void run() {
////				onScaned(iBeaconData iBeacon);
////			};
////		}.start();
////
//        //onScaned();
//
//        return START_REDELIVER_INTENT;
//    }
//
//    /**
//     * ==========================================================
//     */
//    public void onResponse(int msg) {
//        THLLog.d("debug", "Response(" + msg + ")");
//        mHandler.obtainMessage(MSG_SERVER_RESPONSE, msg, 0).sendToTarget();
//    }
//
//
//    /**
//     * ==========================================================
//     */
//    public void dlgNetworkNotAvailable() {
//        final AlertDialog dlg = new AlertDialog.Builder(UIMain.this).create();
//
//        dlg.setTitle("Network");
//        dlg.setMessage("Please enable your network for updating beacon list.");
//
//        dlg.setButton(AlertDialog.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
//            public void onClick(DialogInterface dialog, int id) {
//                dlg.dismiss();
//            }
//        });
//
//        dlg.show();
//    }
//
//    /**
//     * ==========================================================
//     */
//    public void dlgNetwork3G() {
//        final AlertDialog dlg = new AlertDialog.Builder(UIMain.this).create();
//
//        dlg.setTitle("3G");
//        dlg.setMessage("App will send/recv data via 3G, this may result in significant data charges.");
//
//        dlg.setButton(AlertDialog.BUTTON_POSITIVE, "Allow", new DialogInterface.OnClickListener() {
//            public void onClick(DialogInterface dialog, int id) {
//                Config.allow3G = true;
//                dlg.dismiss();
//                USBeaconServerInfo info = new USBeaconServerInfo();
//
//                info.serverUrl = HTTP_API;
//                info.queryUuid = QUERY_UUID;
//                info.downloadPath = STORE_PATH;
//
//                //mBServer.setServerInfo(info, UIMain.this);
//                mBServer.checkForUpdates();
//            }
//        });
//
//        dlg.setButton(AlertDialog.BUTTON_NEGATIVE, "Reject", new DialogInterface.OnClickListener() {
//            public void onClick(DialogInterface dialog, int id) {
//                Config.allow3G = false;
//                dlg.dismiss();
//            }
//        });
//
//        dlg.show();
//    }
//
//    /**
//     * ==========================================================
//     */
//    public void addOrUpdateiBeacon(iBeaconData iBeacon) {
//        long currTime = System.currentTimeMillis();
//
//        ScanediBeacon beacon = null;
//
//        for (ScanediBeacon b : miBeacons) {
//            if (b.equals(iBeacon, false)) {
//                beacon = b;
//                break;
//            }
//        }
//
//        if (null == beacon) {
//            beacon = ScanediBeacon.copyOf(iBeacon);
//            miBeacons.add(beacon);
//        } else {
//            beacon.rssi = iBeacon.rssi;
//        }
//
//        beacon.lastUpdate = currTime;
//        Log.e("beacon5", beacon.beaconUuid.toString());
//    }
//
//
//    /** ========================================================== */
////	public void verifyiBeacons()
////	{
////		{
////			long currTime	= System.currentTimeMillis();
////
////			int len= miBeacons.size();
////			ScanediBeacon beacon= null;
////
////			for(int i= len- 1; 0 <= i; i--)
////			{
////				beacon= miBeacons.get(i);
////
////				if(null != beacon && TIME_BEACON_TIMEOUT < (currTime- beacon.lastUpdate))
////				{
////					miBeacons.remove(i);
////				}
////			}
////		}
////
////		{
////			mListAdapter.clear();
////
////			for(ScanediBeacon beacon : miBeacons)
////			{
////				mListAdapter.addItem(new ListItem(beacon.beaconUuid.toString().toUpperCase(), "" + beacon.major, "" + beacon.minor, "" + beacon.rssi, "" + beacon.batteryPower));
////				uuidstring1 = beacon.beaconUuid.toString().toUpperCase();
////
////				//SendIntent();
/////*
////				String abc = "D5E9DBE2-D9F7-4564-A6C8-57A38C5FA6F0";
////				String t1 = "" + beacon.beaconUuid.toString().toUpperCase();//直接強制轉型
////				int a = 1;
////				if(t1.equals(abc)){
////					a =a+1;
////					String t2 = "" + a;//直接強制轉型
////					Log.i("123", t2);
////
////				int a = 1;
////				if(a==1)
////				{
////					//a=a+5;
////
////					if(a == 1){
////
////						a=a+1;
////						String t1 = "" + a;//直接強制轉型
////						Log.i("dff",t1);
////					}
////
////				}
////*/
////			}
////
////
////		}
////	}
////
////	/** ========================================================== */
////	public void cleariBeacons()
////	{
////		mListAdapter.clear();
////	}
//
//
////	public void SendIntent(){
////		Intent it = new Intent();
////		it.setClass(UIMain.this, Test1.class);
////		Bundle bundle = new Bundle();
////		bundle.putString("UUID",uuidstring1);
////		it.putExtras(bundle);
////		startActivity(it);
////		UIMain.this.finish();
////	}
//
//}
////
/////** ============================================================== */
////class ListItem
////{
////	public String text1= "";
////	public String text2= "";
////	public String text3= "";
////	public String text4= "";
////	public String text5= "";
////	public String text6= "";
////
////	public ListItem()
////	{
////	}
////
////	public ListItem(String text1, String text2, String text3, String text4, String text5)
////	{
////		this.text1= text1;
////		this.text2= text2;
////		this.text3= text3;
////		this.text4= text4;
////		this.text5= text5;
////		this.text6= text6;
////
////	}
////
////}
//
///**
// * ==============================================================  ==============================================================
// * ==============================================================
// */
////class BLEListAdapter extends BaseAdapter
////{
////	private Context mContext;
////
////	//String uuidstring1 = beacon.beaconUuid.toString();
////	List<ListItem> mListItems= new ArrayList<ListItem>();
////
////	/** ================================================ */
////	public BLEListAdapter(Context c) { mContext= c; }
////
////	/** ================================================ */
////	public int getCount() { return mListItems.size(); }
////
////	/** ================================================ */
////	public Object getItem(int position)
////	{
////		if((!mListItems.isEmpty()) && mListItems.size() > position)
////		{
////			return mListItems.toArray()[position];
////		}
////
////		return null;
////	}
////
////	public String getItemText(int position)
////	{
////		if((!mListItems.isEmpty()) && mListItems.size() > position)
////		{
////			return ((ListItem)mListItems.toArray()[position]).text1;
////		}
////
////		return null;
////	}
////
////	/** ================================================ */
////	public long getItemId(int position) { return 0; }
////
////	/** ================================================ */
////	// create a new ImageView for each item referenced by the Adapter
////	public View getView(int position, View convertView, ViewGroup parent)
////	{
////	    View view= (View)convertView;
////
////	    if(null == view)
////	    	view= View.inflate(mContext, R.layout.item_text_3, null);
////
////	     //view.setLayoutParams(new AbsListView.LayoutParams(RadioGroup.LayoutParams.FILL_PARENT, RadioGroup.LayoutParams.WRAP_CONTENT));
////
////	    if(!mListItems.isEmpty() && (mListItems.size() > position))
////	    {
////
////		    TextView text1	= (TextView)view.findViewById(R.id.it3_text1);
////		    TextView text2	= (TextView)view.findViewById(R.id.it3_text2);
////		    TextView text3	= (TextView)view.findViewById(R.id.it3_text3);
////		    TextView text4	= (TextView)view.findViewById(R.id.it3_text4);
////		    TextView text5	= (TextView)view.findViewById(R.id.it3_text5);
////			TextView text6	= (TextView)view.findViewById(R.id.it3_text6);
////
////	    	ListItem item= (ListItem)mListItems.toArray()[position];
////
////
////			text1.setText(item.text1);
////			text2.setText(item.text2);
////			text3.setText(item.text3);
////			text4.setText(item.text4+ " dbm");
////			text5.setText(item.text5+ " V");
////			text6.setText(item.text6+"確定");
////			String abc = "D5E9DBE2-D9F7-4564-A6C8-57A38C5FA6F0";
////			String t1 = "" + text1.getText();//直接強制轉型
////
////			//SendIntent();
////
////			if(t1.equals(abc))
////			{
////				//setContentView(R.layout.mainpage);
////				//Toast.makeText(getApplicationContext(), R.string.gogo, Toast.LENGTH_SHORT).show();
////				//Toast.makeText(BLEListAdapter.this, "Launching " , Toast.LENGTH_SHORT).show();
////				//Toast.makeText(view.getContext(),"123",Toast.LENGTH_SHORT).show();
////
//////				Intent intent = new Intent(, DialogActivity.class);
//////				Intent intent = new Intent();
////
////				//intent.setClass(convertView.getContext(), DialogActivity.class);
////                //convertView.getContext().startActivity(intent);
////
////
////				Log.i("t1.equals(abc)", "qw");
////			}
////
////		}
////	    else
////	    {
////	    	view.setVisibility(View.GONE);
////	    }
////	    return view;
////	}
/////*
////	public void SendIntent(){
////		Intent it = new Intent();
////		it.setClass(UIMain.this, Logout.class);
////		Bundle bundle = new Bundle();
////		bundle.putString("UUID",uuidstring1);
////
////		it.putExtras(bundle);
////		startActivity(it);
////		UIMain.this.finish();
////	}
////*/
////
////	/** ================================================ */
////	@Override
////    public boolean isEnabled(int position)
////    {
////		if(mListItems.size() <= position)
////			return false;
////
////        return true;
////    }
////
////	/** ================================================ */
////	public boolean addItem(ListItem item)
////	{
////		mListItems.add(item);
////	  	return true;
////	}
////
////	/** ================================================ */
////	public void clear()
////	{
////		mListItems.clear();
////	}
////
////
////}

/** ============================================================== */
