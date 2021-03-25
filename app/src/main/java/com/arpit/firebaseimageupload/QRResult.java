package com.arpit.firebaseimageupload;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

public class QRResult extends AppCompatActivity {

    ImageView imgQR;
    TextView txtResult;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_q_r_result);

        imgQR = findViewById(R.id.imgQR);
        txtResult = findViewById(R.id.txtQRResult);
        QrConversionMethod();
    }
    //    QR code containing the description of image is created
    void QrConversionMethod()
    {
        String text= getIntent().getExtras().getString("qrResult");
        // Whatever you need to encode in the QR code
        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
        try {
            BitMatrix bitMatrix = multiFormatWriter.encode(text, BarcodeFormat.QR_CODE,200,200);
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);

            imgQR.setVisibility(View.VISIBLE);
            imgQR.setImageBitmap(bitmap);
            txtResult.setText(text);
        } catch (WriterException e) {
            e.printStackTrace();
        }
    }
}