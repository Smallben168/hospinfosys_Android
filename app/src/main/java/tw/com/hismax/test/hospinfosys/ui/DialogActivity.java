package tw.com.hismax.test.hospinfosys.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;

import tw.com.hismax.test.hospinfosys.Login;
import tw.com.hismax.test.hospinfosys.R;

public class DialogActivity extends Activity {


    // Constant for identifying the dialog
    private static final int DIALOG_ALERT = 10;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mainpage);

//        Button alertDialog = (Button)findViewById(R.id.alertDialogBtn);
//        alertDialog.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//                showDialog(DIALOG_ALERT);
//                Intent intent = new Intent();
//                intent.setClass(DialogActivity.this, UIMain.class);
//                Toast.makeText(getApplicationContext(), "intent", Toast.LENGTH_SHORT).show();
//
//            }
//        }
//        );

        //Intent intent = new Intent();
        //intent.setClass(DialogActivity.this,UIMain.class);

    }

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case DIALOG_ALERT:
                Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("This will end the activity");
                builder.setCancelable(true);
                builder.setPositiveButton("I agree", new OkOnClickListener());
                builder.setNegativeButton("No, no", new CancelOnClickListener());
                AlertDialog dialog = builder.create();
                dialog.show();
        }
        return super.onCreateDialog(id);
    }

    private final class CancelOnClickListener implements
            DialogInterface.OnClickListener {
        public void onClick(DialogInterface dialog, int which) {
            Toast.makeText(getApplicationContext(), "Activity will continue", Toast.LENGTH_SHORT).show();
            //Intent intent = new Intent(DialogActivity.this, UIMain.class);
        }
    }

    private final class OkOnClickListener implements
            DialogInterface.OnClickListener {
        public void onClick(DialogInterface dialog, int which) {
            Toast.makeText(getApplicationContext(), "Just kidding", Toast.LENGTH_SHORT).show();
        }
    }
}