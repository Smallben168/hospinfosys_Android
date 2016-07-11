package tw.com.hismax.test.hospinfosys;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.Socket;
import java.net.URL;
import java.net.URLEncoder;
import java.util.zip.GZIPInputStream;

import javax.net.ssl.HttpsURLConnection;


public class Json1 extends Thread {

    HttpClient client;
    //HttpURLConnection client;
    //Socket socket;
    String result = "";
    int chart_no = 0;
    String pt_name, doctor_no = "";
    String birth_date;
    String dfnsdjfn;
    JSONArray JArray = new JSONArray();
    JSONObject jsonObj = new JSONObject();

    public Json1(String id_no, String registration_id) {
        JSONObject manJson = new JSONObject();
        try {
//            manJson.put("id_no", id_no);
//            manJson.put("registration_id", registration_id);
//            manJson.put("eff_flag", "Y");
//            StringEntity se = new StringEntity(manJson.toString());

            jsonObj.put("id_no", id_no);
            Log.e("id_no : ", id_no);
            jsonObj.put("registration_id", registration_id);
            jsonObj.put("eff_flag", "Y");
            Log.e("id_no", id_no.toString());  //你弄出的JSonArray轉成字串
//將JsonObject put進去JsonArray

            JArray.put(jsonObj);
            StringEntity se = new StringEntity(JArray.toString());

            Log.e("你弄的JsonArray", JArray.toString());  //你弄出的JSonArray轉成字串

            client = new DefaultHttpClient();
            HttpPost httpost = new HttpPost("http://163.18.22.69/rest/device_register/");
            httpost.setEntity(se);

            httpost.setHeader("Accept", "application/json");
            httpost.setHeader("Content-type", "application/json");

            HttpResponse responsePOST = client.execute(httpost);
            HttpEntity resEntity = responsePOST.getEntity();

            if (resEntity != null) {
                this.result = EntityUtils.toString(resEntity);
            }
            //this.doctor_no = new JSONObject(this.result).getString("doctor_no");
            this.chart_no = new JSONObject(this.result).getInt("chart_no");
            //String birth_date = new JSONObject(this.result).getString("birth_date");
            this.birth_date = new JSONObject(this.result).getString("birth_date");
            this.pt_name = new JSONObject(this.result).getString("pt_name");
            this.pt_name = new String(pt_name.getBytes("ISO-8859-1"), "UTF-8");   //亂碼變中文

            Log.e("chart_no  + 4birth_date", chart_no + birth_date.toString());  //你弄出的JSonArray轉成字串

            Log.e("chart_no+birth_date", "chart_no" + "birth_date");

            Log.e("chart_no", chart_no + pt_name.toString());

//            try {
//                //Create connection
//                URL url = new URL("http://163.18.22.69/rest/device_register/");
//                client = (HttpURLConnection)url.openConnection();
//                client.setRequestMethod("POST");
//                client.setRequestProperty("Content-Type", "application/json");
//                client.setUseCaches(false);
//                client.setDoInput(true);
//                client.setDoOutput(true);
//                client.setConnectTimeout(30000);
//
//
//                //Send request
//                DataOutputStream wr = new DataOutputStream (
//                        client.getOutputStream());
//                wr.write(manJson.toString().getBytes("UTF-8"));
//                wr.flush();
//                wr.close();
//
//                //client.connect();
//
//
//                //Get Response
////                InputStream is = client.getInputStream();
////                BufferedReader rd = new BufferedReader(new InputStreamReader(is));
////                String line;
////                StringBuffer response = new StringBuffer();
////                while((line = rd.readLine()) != null) {
////                    response.append(line);
////                    response.append('\r');
////                }
////                rd.close();
////                this.result = response.toString();
//
//            } catch (Exception e) {
//
//                e.printStackTrace();
//                this.result = "why";
//
//            } finally {
//
//                if(client != null) {
//                    client.disconnect();
//                }
//            }




            /*writeStream(outputPost);
            outputPost.flush();
            outputPost.close();

            client.setFixedLengthStreamingMode(outputPost.getBytes().length);
            client.setChunkedStreamingMode(0);*/


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

    //回傳值到MainActivit
    public String getString() {
        return this.result;
    }

    public String getpt_name() {
        return this.pt_name;
    }

    public int getchart_no() {
        return this.chart_no;
    }

    public String getbirth_date() {
        return this.birth_date;
    }

//    public String getdoctor_no(){
//        return this.doctor_no;
//    }
//
}







/*

public class Json1 {
    private static final String TAG = "HttpClient";

    public static JSONObject SendHttpPost(String URL, JSONObject jsonObjSend,String id_no, String egistration_id) {

        try {
            DefaultHttpClient httpclient = new DefaultHttpClient();
            HttpPost httpPostRequest = new HttpPost(URL);

            StringEntity se;
            se = new StringEntity(jsonObjSend.toString());

            // Set HTTP parameters
            httpPostRequest.setEntity(se);
            httpPostRequest.setHeader("id_no", id_no);
            httpPostRequest.setHeader("registration_id", egistration_id);
            httpPostRequest.setHeader("eff_flag", "Y");
            httpPostRequest.setHeader("Accept-Encoding", "gzip"); // only set this parameter if you would like to use gzip compression

            long t = System.currentTimeMillis();
            HttpResponse response = (HttpResponse) httpclient.execute(httpPostRequest);
            Log.i(TAG, "HTTPResponse received in [" + (System.currentTimeMillis()-t) + "ms]");

            // Get hold of the response entity (-> the data):
            HttpEntity entity = response.getEntity();

            if (entity != null) {
                // Read the content stream
                InputStream instream = entity.getContent();
                Header contentEncoding = response.getFirstHeader("Content-Encoding");
                if (contentEncoding != null && contentEncoding.getValue().equalsIgnoreCase("gzip")) {
                    instream = new GZIPInputStream(instream);
                }

                // convert content stream to a String
                String resultString= convertStreamToString(instream);
                instream.close();
                resultString = resultString.substring(1,resultString.length()-1); // remove wrapping "[" and "]"

                // Transform the String into a JSONObject
                JSONObject jsonObjRecv = new JSONObject(resultString);
                // Raw DEBUG output of our received JSON object:
                Log.i(TAG,"<JSONObject>\n"+jsonObjRecv.toString()+"\n</JSONObject>");

                return jsonObjRecv;
            }

        }
        catch (Exception e)
        {
            // More about HTTP exception handling in another tutorial.
            // For now we just print the stack trace.
            e.printStackTrace();
        }
        return null;
    }


    private static String convertStreamToString(InputStream is) {

        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();

        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }

}
*/