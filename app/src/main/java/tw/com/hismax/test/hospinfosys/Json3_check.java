package tw.com.hismax.test.hospinfosys;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.apache.http.client.HttpClient;

import org.json.JSONException;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

/**
 * Created by OH HO on 2016/6/6.
 */
public class Json3_check extends Thread {
    HttpClient client;
    JSONArray JArray = new JSONArray();
    JSONObject jsonObj =new JSONObject();
    String result3,doctor_name,current_no,_status_doc,_status;
    String location_code,view_no,clinic,doctor_no,duplicate_no,prenatal_care;
    String except_view_time,card_seq;

    public Json3_check(String result2) {
        try {
            JArray.put(jsonObj);
            StringEntity se = new StringEntity(JArray.toString());

            client = new DefaultHttpClient();
            //HttpPost httpost = new HttpPost("http://163.18.22.69/rest/receiver_beacon/set");
            HttpPost httpost = new HttpPost("http://61.219.152.220/rest/receiver_beacon/set");
            httpost.setEntity(se);

            httpost.setHeader("Accept", "application/json");
            httpost.setHeader("Content-type", "application/json");

            HttpResponse responsePOST = client.execute(httpost);
            HttpEntity resEntity = responsePOST.getEntity();

            if (resEntity != null) {
                this.result3 = EntityUtils.toString(resEntity);
            }

//            this.doctor_name = new JSONObject(this.result3).getString("doctor_name");
//            this.current_no = new JSONObject(this.result3).getString("current_no");
//            this.doctor_no = new JSONObject(this.result3).getString("doctor_no");
//            this.duplicate_no = new JSONObject(this.result3).getString("duplicate_no");
//
//            this.location_code = new JSONObject(this.result3).getString("location_code");
//            this.clinic = new JSONObject(this.result3).getString("clinic");
//            this.prenatal_care = new JSONObject(this.result3).getString("prenatal_care");

            this._status = new JSONObject(this.result3).getString("_status");
            this._status_doc = new JSONObject(this.result3).getString("_status_doc");
            this._status_doc =new String(_status_doc.getBytes("ISO-8859-1"), "UTF-8");   //亂碼變中文
            this.view_no = new JSONObject(this.result3).getString("view_no");
            this.except_view_time = new JSONObject(this.result3).getString("except_view_time");
            this.card_seq = new JSONObject(this.result3).getString("card_seq");



        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String getjson3() {
        return this.result3;
    }

    public String getdoctor_name() {
        return this.doctor_name;
    }

    public String getcard_seq() {
        return this.card_seq;
    }

    public String getdoctor_no() {
        return this.doctor_no;
    }

    public String getexcept_view_time() {
        return this.except_view_time;
    }

    public String get_status() {
        return this._status;
    }

    public String get_status_doc() {
        return this._status_doc;
    }

    public String getview_no() {
        return this.view_no;
    }

    public String getprenatal_care() {
        return this.prenatal_care;
    }

    public String getclinic() {
        return this.clinic;
    }

    public String getlocation_code() {
        return this.location_code;
    }




}