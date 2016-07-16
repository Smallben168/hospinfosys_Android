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
                    Log.d("Ben", "json_BeaconGet status= " + this._status);
                    if (_status.equals("success")) {
                        haveData = true;
                        this._status = new JSONArray(this.result).getJSONObject(0).getString("_status");
                        this.view_no = new JSONArray(this.result).getJSONObject(0).getInt("view_no");
                        this._status_doc = new JSONArray(this.result).getJSONObject(0).getString("_status_doc");
                        this.location_code = new JSONArray(this.result).getJSONObject(0).getString("location_code");
                        this.doctor_no = new JSONArray(this.result).getJSONObject(0).getString("doctor_no");
                        this.doctor_name = new JSONArray(this.result).getJSONObject(0).getString("doctor_name");
                        this.clinic_ps = new JSONArray(this.result).getJSONObject(0).getString("clinic_ps");
                        this.doctor_name = new String(doctor_name.getBytes("ISO-8859-1"), "UTF-8");   //亂碼變中文
                        this._status_doc = new String(_status_doc.getBytes("ISO-8859-1"), "UTF-8");
                        this.clinic_ps = new String(clinic_ps.getBytes("ISO-8859-1"), "UTF-8");
                        //***Ben -------s
                        this.setCurNo(new JSONArray(this.result).getJSONObject(0).getInt("current_no"));

                        Log.e("doctor_name_json2", doctor_name.toString());
                        Log.e("_status_doc_json2", _status_doc.toString());
                        Log.e("clinic_ps_json2", clinic_ps.toString());
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

    //回傳值到MainActivity
    public String getjson2() {
        return this.result;
    }

    public String get_status_doc() {
        return this._status_doc;
    }

    public String getlocation_code() {
        return this.location_code;
    }

    public String getdoctor_name() {
        return this.doctor_name;
    }

    public int getview_no() {
        return this.view_no;
    }

    public String getdoctor_no() {
        return this.doctor_no;
    }

    public String get_status() {
        return this._status;
    }

    public String getclinic_ps() {
        return this.clinic_ps;
    }

    public int getCurNo() {
        return curNo;
    }

    public void setCurNo(int curNo) {
        this.curNo = curNo;
    }

    public boolean isHaveData() {
        return haveData;
    }

    public void setHaveData(boolean haveData) {
        this.haveData = haveData;
    }
}

