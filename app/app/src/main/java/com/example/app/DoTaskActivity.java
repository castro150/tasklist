package com.example.app;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
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
    private LocalLocation location;
    private LocationManager locationManager;
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

        startLocationListener();
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
        final Double longitude = this.location.getLongitude();
        final Double latitude = this.location.getLatitude();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder
            .setMessage("Confirmar envio de foto?")
            .setPositiveButton("Sim",  new DialogInterface.OnClickListener() {
                @Override
                public void onClick(final DialogInterface dialog, int id) {
                    String url = String.format("http://awesome-tasklist-server.herokuapp.com/api/tasks/finalize/%s", taskId);
                    http.uploadFile(url, longitude, latitude, byteArray, new Callback() {
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

    private void startLocationListener() {
        this.location = new LocalLocation();

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[] {
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                                Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.INTERNET
                        } , 10);
            }
            return;
        }

        this.locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        this.locationManager.requestLocationUpdates("gps", 5000, 0, location);
    }

    private class LocalLocation implements LocationListener {
        private Double longitude = null;
        private Double latitude = null;

        public LocalLocation() {
            super();
        }

        public Double getLongitude() {
            return longitude;
        }

        public Double getLatitude() {
            return latitude;
        }

        @Override
        public void onLocationChanged(Location location) {
            this.longitude = location.getLongitude();
            this.latitude = location.getLatitude();
        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {}

        @Override
        public void onProviderEnabled(String s) {}

        @Override
        public void onProviderDisabled(String s) {}
    }
}
