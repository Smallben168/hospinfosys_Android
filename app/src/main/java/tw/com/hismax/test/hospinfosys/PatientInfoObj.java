package tw.com.hismax.test.hospinfosys;

import android.app.Application;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;

/**
 * Created by Ben on 16/7/8.
 */
public class PatientInfoObj extends Application {
    private boolean notFirstRun;
    private int chartNo;
    private String ptName;
    private String idNo;
    private String birthDay;

    private File sdcard;
    private File patientSetting;

    public PatientInfoObj(){
        sdcard = new File(Environment.getExternalStorageDirectory(),"Android/data/tw.com.hismax.test.hospinfosys/files");
        patientSetting = new File(sdcard,"PatientSetting.txt");
        //Move init value
        //chartNo = 106733;
        //ptName = "王小敏";
        //idNo = "A123456789";
        //birthDay = "0850506";
    }
    public int getChartNo() {
        return chartNo;
    }

    public void setChartNo(int chartNo) {
        this.chartNo = chartNo;
    }

    public String getPtName() {
        return ptName;
    }

    public void setPtName(String ptName) {
        this.ptName = ptName;
    }

    public String getIdNo() {
        return idNo;
    }

    public void setIdNo(String idNo) {
        this.idNo = idNo;
    }

    public String getBirthDay() {
        return birthDay;
    }

    public void setBirthDay(String birthDay) {
        this.birthDay = birthDay;
    }

    public void readFileToObj(){
        //for test
        //deleteFile();
        //-----------
        StringBuilder txt = new StringBuilder();
        try {
            BufferedReader br = new BufferedReader(new FileReader(patientSetting));
            String line;

            while ((line = br.readLine()) != null){
                String[] keyValue = line.split("=");
                switch (keyValue[0]){
                    case "CHART_NO":
                        chartNo = Integer.parseInt(keyValue[1]);
                        break;
                    case "PT_NAME":
                        ptName = keyValue[1];
                        break;
                    case "BIRTHDAY":
                        birthDay = keyValue[1];
                        break;
                    case "ID_NO":
                        idNo = keyValue[1];
                }

            }
            br.close();
        } catch (IOException e) {
            //saveToFile();
            Log.d("txt File", "File : " + e.getMessage());
        }


    }
    public boolean saveToFile(){
        try {
            if (!sdcard.exists()){
                sdcard.mkdirs();
            }
            Log.d("FILE", "sdcard is exists " + sdcard.exists());

            patientSetting.createNewFile();
            FileOutputStream out = new FileOutputStream(patientSetting);
            OutputStreamWriter outWriter = new OutputStreamWriter(out);
            outWriter.append("CHART_NO=" + chartNo + "\n");
            outWriter.append("PT_NAME=" + ptName+ "\n");
            outWriter.append("BIRTHDAY=" + birthDay+ "\n");
            outWriter.append("ID_NO=" + idNo+ "\n");
            outWriter.close();
            out.close();
            return true;
        } catch (IOException e) {
            Log.d("Error", "Save PatientSetting Error : " + e.getMessage());
            return false;
        }

    }

    public boolean deleteFile(){
        boolean deleted = patientSetting.delete();
        return deleted;
    }


    public boolean isNotFirstRun() {
        return notFirstRun;
    }

    public void setNotFirstRun(boolean notFirstRun) {
        this.notFirstRun = notFirstRun;
    }
}
