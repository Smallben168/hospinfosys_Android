package tw.com.hismax.test.hospinfosys;

import android.content.Intent;
import android.speech.RecognizerIntent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class VoiceActivity extends AppCompatActivity implements View.OnClickListener {

    ListView lstVoice;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voice);
        //候選清單
        lstVoice = (ListView) findViewById(R.id.lstVoice);
        lstVoice.setOnItemClickListener(new AdapterView.OnItemClickListener(){

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ListView lst = (ListView) adapterView;
                Toast.makeText(VoiceActivity.this, lst.getItemAtPosition(i).toString(), Toast.LENGTH_SHORT).show();
                finish();
            }
        });
        //開始講話
        Button butVoice = (Button) findViewById(R.id.butVoice);
        butVoice.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        Intent i = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        i.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        i.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak up, Please!");
        startActivityForResult(i, 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0 && resultCode == RESULT_OK) {
            ArrayList<String> results = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            lstVoice.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, results));
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

}
