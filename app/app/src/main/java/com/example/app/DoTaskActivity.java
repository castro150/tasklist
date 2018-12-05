package com.example.app;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class DoTaskActivity extends AppCompatActivity {

    private static final int REQUEST_IMAGE_CAPTURE = 1;

    private String id = null;
    private String name = null;
    private HttpRequest http = new HttpRequest();

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_do_task);

        // Get the Intent that started this activity and extract the string
        Intent intent = getIntent();
        this.id = intent.getStringExtra(MainActivity.EXTRA_TASK_ID);
        this.name = intent.getStringExtra(MainActivity.EXTRA_TASK_NAME);

        // Capture the layout's TextView and set the string as its text
        TextView textView = findViewById(R.id.doTaskText);
        textView.setText(String.format("Enviando resolução da tarefa \"%s\".", this.name));
    }

    /** Called when the user taps the Take Picture button */
    public void takePicture(View view) {
        dispatchTakePictureIntent();
    }

    private void goToMain() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");

            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            byte[] byteArray = stream.toByteArray();

            confirmDialog(byteArray);
        }
    }

    private void confirmDialog(final byte[] byteArray) {
        final String taskId = this.id;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder
            .setMessage("Confirmar envio de foto?")
            .setPositiveButton("Sim",  new DialogInterface.OnClickListener() {
                @Override
                public void onClick(final DialogInterface dialog, int id) {
                    String url = String.format("http://awesome-tasklist-server.herokuapp.com/api/tasks/finalize/%s", taskId);
                    http.uploadFile(url, byteArray, new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            e.printStackTrace();
                        }

                        @Override
                        public void onResponse(Call call, Response response) {
                            if (response.isSuccessful()) {
                                System.out.println("DONE");
                                goToMain();
                            } else {
                                System.out.println("REQUEST ERROR");
                                dialog.cancel();
                            }
                        }
                    });
                }
            })
            .setNegativeButton("Nao", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int id) {
                    dialog.cancel();
                }
            })
            .show();
    }
}
