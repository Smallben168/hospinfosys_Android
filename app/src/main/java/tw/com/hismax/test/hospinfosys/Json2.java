package tw.com.hismax.test.hospinfosys;

import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.apache.http.entity.StringEntity;

import java.io.IOException;
import java.io.InputStream;


public class Json2 extends Thread {
    String result2 = "";
    String view_no = "";
    String doctor_name,clinic_ps = "";
    String _status_doc, _status = "";
    String location_code = "";
    String doctor_no = "";
    JSONArray JArray = new JSONArray();
    JSONObject jsonObj = new JSONObject();
    String test = "";


    public Json2(String b_uuid, String chart_no) {
//        InputStream is = null;
//        String result = "";
//        //若線上資料為陣列，則使用JSONArray
//        JSONArray jsonArray = null;
//        //若線上資料為單筆資料，則使用JSONObject
//        JSONObject jsonObj = null;


        // 透過HTTP連線取得回應
        try {
            Log.e("chart_no2134586", chart_no.toString());
            jsonObj.put("", test);
            JArray.put(jsonObj);
            StringEntity se = new StringEntity(JArray.toString());

            HttpClient client = new DefaultHttpClient(); // for port 80 requests!
            Log.i("b_uuid.toString()", b_uuid.toString());
            Log.i("chart_no.toString()", chart_no.toString());
            HttpGet httpget = new HttpGet("http://163.18.22.69/rest/getTodayReg/get?chart_no=" + chart_no.toString());
            //httpget.setEntity(se);

            HttpResponse response = client.execute(httpget);
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                this.result2 = EntityUtils.toString(entity);
                Log.i("fuck123", result2.toString());
            } else {
                Log.i("fuck", "ok");
            }

//            this._status = new JSONArray(this.result2).getJSONObject(0).getString("_status");
//            Log.e("_status_json2", _status.toString());
//            if (this._status.toString().equals("success")) {
//
//                this.view_no = new JSONArray(this.result2).getJSONObject(0).getString("view_no");
//                this._status_doc = new JSONArray(this.result2).getJSONObject(0).getString("_status_doc");
//                this.location_code = new JSONArray(this.result2).getJSONObject(0).getString("location_code");
//                this.doctor_no = new JSONArray(this.result2).getJSONObject(0).getString("doctor_no");
//                this.doctor_name = new JSONArray(this.result2).getJSONObject(0).getString("doctor_name");
//                this.doctor_name = new String(doctor_name.getBytes("ISO-8859-1"), "UTF-8");   //亂碼變中文
//                this._status_doc = new String(_status_doc.getBytes("ISO-8859-1"), "UTF-8");
//                Log.e("doctor_name_json2", doctor_name.toString());
//                Log.e("_status_doc_json2", _status_doc.toString());
//            } else {
//                this._status_doc = new JSONArray(this.result2).getJSONObject(0).getString("_status_doc");
//                this._status_doc = new String(_status_doc.getBytes("ISO-8859-1"), "UTF-8");
//                Log.e("_status_doc_json2_1", _status_doc.toString());
//            }

            this._status = new JSONArray(this.result2).getJSONObject(0).getString("_status");
            this.view_no = new JSONArray(this.result2).getJSONObject(0).getString("view_no");
            this._status_doc = new JSONArray(this.result2).getJSONObject(0).getString("_status_doc");
            this.location_code = new JSONArray(this.result2).getJSONObject(0).getString("location_code");
            this.doctor_no = new JSONArray(this.result2).getJSONObject(0).getString("doctor_no");
            this.doctor_name = new JSONArray(this.result2).getJSONObject(0).getString("doctor_name");
            this.clinic_ps = new JSONArray(this.result2).getJSONObject(0).getString("clinic_ps");
            this.doctor_name = new String(doctor_name.getBytes("ISO-8859-1"), "UTF-8");   //亂碼變中文
            this._status_doc = new String(_status_doc.getBytes("ISO-8859-1"), "UTF-8");
            this.clinic_ps = new String(clinic_ps.getBytes("ISO-8859-1"), "UTF-8");
            Log.e("doctor_name_json2", doctor_name.toString());
            Log.e("_status_doc_json2", _status_doc.toString());
            Log.e("clinic_ps_json2", clinic_ps.toString());

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
        return this.result2;
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

    public String getview_no() {
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
}

