package tw.com.hismax.test.hospinfosys;

import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import com.THLight.USBeacon.App.Lib.BatteryPowerData;
import com.THLight.USBeacon.App.Lib.USBeaconConnection;
import com.THLight.USBeacon.App.Lib.USBeaconList;
import com.THLight.USBeacon.App.Lib.USBeaconServerInfo;
import com.THLight.USBeacon.App.Lib.iBeaconData;
import com.THLight.USBeacon.App.Lib.iBeaconScanManager;
import com.THLight.Util.THLLog;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by OH HO on 2016/6/4.
 */
public class Beacon_thread extends Thread implements iBeaconScanManager.OniBeaconScan, USBeaconConnection.OnResponse{

    /** this UUID is generate by Server while register a new account. */
    final UUID QUERY_UUID = UUID.fromString("BB746F72-282F-4378-9416-89178C1019FC");
    /** server http api url. */
    final String HTTP_API = "http://www.usbeacon.com.tw/api/func";

    static String STORE_PATH = Environment.getExternalStorageDirectory().toString() + "/USBeaconSample/";

    final int REQ_ENABLE_BT = 2000;
    final int REQ_ENABLE_WIFI = 2001;

    final int MSG_SCAN_IBEACON = 1000;
    final int MSG_UPDATE_BEACON_LIST = 1001;
    final int MSG_START_SCAN_BEACON = 2000;
    final int MSG_STOP_SCAN_BEACON = 2001;
    final int MSG_SERVER_RESPONSE = 3000;

    final int TIME_BEACON_TIMEOUT = 30000;

    THLApp App = null;
    THLConfig Config = null;

    BluetoothAdapter mBLEAdapter = BluetoothAdapter.getDefaultAdapter();

    /** scaner for scanning iBeacon around. */
    iBeaconScanManager miScaner = null;

    /** USBeacon server. */
    USBeaconConnection mBServer = new USBeaconConnection();

    USBeaconList mUSBList = null;

    ListView mLVBLE = null;

//	BLEListAdapter mListAdapter		= null;

    List<ScanediBeacon> miBeacons = new ArrayList<ScanediBeacon>();

    /** ================================================ */
    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_SCAN_IBEACON: {
                    int timeForScaning = msg.arg1;
                    int nextTimeStartScan = msg.arg2;

                    miScaner.startScaniBeacon(timeForScaning);
                    this.sendMessageDelayed(Message.obtain(msg), nextTimeStartScan);
                    //Toast.makeText(UIMain.this, "SCAN_IBEACON", Toast.LENGTH_SHORT).show();
                }
                break;

//				case MSG_UPDATE_BEACON_LIST:
//				synchronized(mListAdapter)
//				{
//					verifyiBeacons();
//					mListAdapter.notifyDataSetChanged();
//					mHandler.sendEmptyMessageDelayed(MSG_UPDATE_BEACON_LIST, 500);
//					//Toast.makeText(UIMain.this, "UPDATE_BEACON_LIST", Toast.LENGTH_SHORT).show();
//				}
//				break;

                case MSG_SERVER_RESPONSE:
                    switch (msg.arg1) {
                        case USBeaconConnection.MSG_NETWORK_NOT_AVAILABLE:
                            break;

                        case USBeaconConnection.MSG_HAS_UPDATE:
                            mBServer.downloadBeaconListFile();
                            //Toast.makeText(UIMain.this, "HAS_UPDATE.", Toast.LENGTH_SHORT).show();
                            break;

                        case USBeaconConnection.MSG_HAS_NO_UPDATE:
                            //Toast.makeText(UIMain.this, "No new BeaconList.", Toast.LENGTH_SHORT).show();
                            break;

                        case USBeaconConnection.MSG_DOWNLOAD_FINISHED:
                            //Toast.makeText(UIMain.this, "DOWNLOAD_FINISHED", Toast.LENGTH_SHORT).show();
                            break;

                        case USBeaconConnection.MSG_DOWNLOAD_FAILED:
                            //Toast.makeText(UIMain.this, "Download file failed!", Toast.LENGTH_SHORT).show();
                            break;

//						case USBeaconConnection.MSG_DATA_UPDATE_FINISHED:
//							{
//								USBeaconList BList= mBServer.getUSBeaconList();
//
//								if(null == BList)
//								{
//									Toast.makeText(UIMain.this, "Data Updated failed.", Toast.LENGTH_SHORT).show();
//									THLLog.d("debug", "update failed.");
//								}
//								else if(BList.getList().isEmpty())
//								{
//									Toast.makeText(UIMain.this, "Data Updated but empty.", Toast.LENGTH_SHORT).show();
//									THLLog.d("debug", "this account doesn't contain any devices.");
//								}
//								else
//								{
//									Toast.makeText(UIMain.this, "Data Updated(" + BList.getList().size() + ")", Toast.LENGTH_SHORT).show();
//
//									for(USBeaconData data : BList.getList())
//									{
//										THLLog.d("debug", "Name("+ data.name+ "), Ver("+ data.major+ "."+ data.minor+ ")");
//									}
//								}
//							}
//							break;

                        case USBeaconConnection.MSG_DATA_UPDATE_FAILED:
                            //Toast.makeText(UIMain.this, "UPDATE_FAILED!", Toast.LENGTH_SHORT).show();
                            break;
                    }
                    break;
            }
        }
    };

    protected void onCreate(Bundle savedInstanceState) {
        //super.onCreate();
        //super.onCreate(savedInstanceState);
        //setContentView(R.layout.ui_main);

        //Toast.makeText(this, "Beacon start", Toast.LENGTH_SHORT).show();

        App = THLApp.getApp();
        Config = THLApp.Config;

        /** create instance of iBeaconScanManager. */
        //miScaner = new iBeaconScanManager(this, this);

        //mListAdapter	= new BLEListAdapter(this);

        //mLVBLE			= (ListView)findViewById(R.id.beacon_list);
        //mLVBLE.setAdapter(mListAdapter);

        if (!mBLEAdapter.isEnabled()) {
            Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            //startActivityForResult(intent, REQ_ENABLE_BT);
        } else {
            Message msg = Message.obtain(mHandler, MSG_SCAN_IBEACON, 1000, 1100);
            msg.sendToTarget();
        }

        /** create store folder. */
        File file = new File(STORE_PATH);
        if (!file.exists()) {
            if (!file.mkdirs()) {
                //Toast.makeText(this, "Create folder(" + STORE_PATH + ") failed.", Toast.LENGTH_SHORT).show();
            }
        }

//        /** check network is available or not. */
//        ConnectivityManager cm = (ConnectivityManager) getSystemService(Beacon_thread.CONNECTIVITY_SERVICE);
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

        mHandler.sendEmptyMessageDelayed(MSG_UPDATE_BEACON_LIST, 500);
    }

    public void onScaned(iBeaconData iBeacon) {
        addOrUpdateiBeacon(iBeacon);

//		synchronized(mListAdapter)
//		{
//			addOrUpdateiBeacon(iBeacon);
//		}
    }

    @Override
    public void onBatteryPowerScaned(BatteryPowerData batteryPowerData) {
        // TODO Auto-generated method stub
        Log.d("debug", batteryPowerData.batteryPower + "");
        for (int i = 0; i < miBeacons.size(); i++) {
            if (miBeacons.get(i).macAddress.equals(batteryPowerData.macAddress)) {
                ScanediBeacon ib = miBeacons.get(i);
                ib.batteryPower = batteryPowerData.batteryPower;
                miBeacons.set(i, ib);
            }
        }
    }

    public void onResponse(int msg) {
        THLLog.d("debug", "Response(" + msg + ")");
        mHandler.obtainMessage(MSG_SERVER_RESPONSE, msg, 0).sendToTarget();
    }


    /** ========================================================== */
//    public void dlgNetworkNotAvailable() {
//        final AlertDialog dlg = new AlertDialog.Builder(Beacon_thread.this).create();
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
//    /** ========================================================== */
//    public void dlgNetwork3G() {
//        final AlertDialog dlg = new AlertDialog.Builder(Beacon_thread.this).create();
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

    /** ========================================================== */
    public void addOrUpdateiBeacon(iBeaconData iBeacon) {
        long currTime = System.currentTimeMillis();

        ScanediBeacon beacon = null;

        for (ScanediBeacon b : miBeacons) {
            if (b.equals(iBeacon, false)) {
                beacon = b;
                break;
            }
        }

        if (null == beacon) {
            beacon = ScanediBeacon.copyOf(iBeacon);
            miBeacons.add(beacon);
        } else {
            beacon.rssi = iBeacon.rssi;
        }

        beacon.lastUpdate = currTime;
    }



}
