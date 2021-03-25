package com.arpit.firebaseimageupload;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import java.io.IOException;

public class QRScannerActivity extends AppCompatActivity {

    SurfaceView cameraPreview;
    TextView txtResult;
    BarcodeDetector barcodeDetector;
    CameraSource cameraSource;
    private  static final int RequestCameraPermissionId = 1001;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_q_r_scanner);
        txtResult = findViewById(R.id.txtQRResult);

        cameraPreview = findViewById(R.id.surfaceView);

//      initialising barcode detector
        barcodeDetector = new BarcodeDetector.Builder(QRScannerActivity.this).setBarcodeFormats(Barcode.QR_CODE).build();

//initialising cameraSource
        cameraSource = new CameraSource.Builder(QRScannerActivity.this, barcodeDetector).setRequestedPreviewSize(640, 480).setAutoFocusEnabled(true).build();

//        creating the camera preview to capture QR Code
        cameraPreview.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(@NonNull SurfaceHolder surfaceHolder) {
                if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {

                    ActivityCompat.requestPermissions(QRScannerActivity.this , new String[]{Manifest.permission.CAMERA}
                            , RequestCameraPermissionId);
                    return;
                }

                try {
                    cameraSource.start(surfaceHolder);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            //Request for camera permission


            @Override
            public void surfaceChanged(@NonNull SurfaceHolder surfaceHolder, int i, int i1, int i2) {

            }

            @Override
            public void surfaceDestroyed(@NonNull SurfaceHolder surfaceHolder) {

                cameraSource.stop();
            }
        });

        barcodeDetector.setProcessor(new Detector.Processor<Barcode>() {
            @Override
            public void release() {

            }

            //            when barCode detector detect QR mages mobile vibrates
            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {

                SparseArray<Barcode> qrCodes = detections.getDetectedItems();
                if(qrCodes.size() != 0)
                {
                    Vibrator vibrator = (Vibrator) getApplicationContext().getSystemService(Context.VIBRATOR_SERVICE);
                    vibrator.vibrate(500);

                    //may get change
//                            txtResult.setText(qrCodes.valueAt(0).displayValue);
                    //Transition....
                    Intent qrResultIntent = new Intent(QRScannerActivity.this,QRResult.class);
                    qrResultIntent.putExtra("qrResult" , qrCodes.valueAt(0).displayValue);
                    startActivity(qrResultIntent);

                    cameraSource.stop();
//                    txtResult.post(new Runnable() {
//                        @Override
//                        public void run() {
//
//
//                        }
//                    });
                }
                else
                {
                    Toast.makeText(QRScannerActivity.this," QR Code is INVALID!!!" , Toast.LENGTH_LONG).show();
                }
            }
        });
    }

}