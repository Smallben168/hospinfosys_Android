package tw.com.hismax.test.hospinfosys;

import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;


public class Json_BeaconSet extends Thread {
    String result = "";
    int view_no = 0;
    private int curNo=0;
    String doctor_name,clinic_ps = "";
    String _status_doc, _status = "";
    String location_code = "";
    String doctor_no = "";
    String exceptViewTime="";
    JSONArray JArray = new JSONArray();
    JSONObject jsonObj = new JSONObject();
    String test = "";
    private boolean haveData = false;
    String url = "";


    public Json_BeaconSet(String inpData) {
//        InputStream is = null;
//        String result = "";
//        //若線上資料為陣列，則使用JSONArray
//        JSONArray jsonArray = null;
//        //若線上資料為單筆資料，則使用JSONObject
//        JSONObject jsonObj = null;


        // 透過HTTP連線取得回應
        try {
            Log.d("BEN", "Json_BeaconSet Start !!");
            //***Ben: ?
            jsonObj.put("", test);
            JArray.put(jsonObj);

            HttpClient client = new DefaultHttpClient(); // for port 80 requests!
            Log.d("BEN", "input data=" + inpData);
            url = "http://163.18.22.69/rest/receiver_beacon/set";
            Log.d("BEN", "url = " + url);
            HttpPost httpost = new HttpPost(url);
            StringEntity se = new StringEntity(inpData);
            httpost.setEntity(se);

            httpost.setHeader("Accept", "application/json");
            httpost.setHeader("Content-type", "application/json");
            HttpResponse responsePOST = client.execute(httpost);

            HttpEntity resEntity = responsePOST.getEntity();

            if (resEntity != null) {
                this.result = EntityUtils.toString(resEntity);
                Log.d("BEN", "Json_BeaconSet = " + result.toString());
                if (result.equals("")) {
                    haveData =false;
                } else {
                    this._status = new JSONArray(this.result).getJSONObject(0).getString("_status");
                    this._status_doc = new JSONArray(this.result).getJSONObject(0).getString("_status_doc");
                    Log.d("Ben", "json_BeaconSet status= " + this._status);
                    if (_status.equals("success")) {
                        haveData = true;
                        if (this._status_doc.equals("REG")){
                            this.view_no = new JSONArray(this.result).getJSONObject(0).getInt("view_no");
                            this.exceptViewTime = new JSONArray(this.result).getJSONObject(0).getString("except_view_time");
                        }

                    } else {
                        haveData = false;
                    }
                }
            } else {
                Log.d("BEN", "Get Data : ok");
                haveData = false;
            }

        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("MYAPP", "exception", e);
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("MYAPP", "exception", e);
        } catch (Exception e) {
            Log.e("MYAPP", "exception", e);
        } finally {
            //client.disconnect();
        }
    }


}

