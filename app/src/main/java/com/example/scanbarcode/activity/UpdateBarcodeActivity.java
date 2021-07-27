package com.example.scanbarcode.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.scanbarcode.R;
import com.example.scanbarcode.data.Data;
import com.example.scanbarcode.model.Product;
import com.example.scanbarcode.model.ProductModel;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.sql.SQLException;

import static com.example.scanbarcode.data.Data.products;

public class UpdateBarcodeActivity extends AppCompatActivity implements View.OnClickListener {
    private static final int REQUEST_CAMERA = 1;
    private SurfaceView mCameraPreview;
    private BarcodeDetector mBarcodeDetector;
    private CameraSource mCameraSource;
    String barcode;



    private TextView tvBarcode;
    private TextView tvNameProduct;

    private boolean firstDetection=true;
    private TextInputLayout layoutEdtBarcode;

    private TextInputEditText edtBarcode;
    private TextInputEditText edtAmount;

    private View layoutProduct;
    private View layoutCamera;

    private ImageView btnReload;
    private ImageView btnClose;

    private ImageView ivImgProduct;

    private MaterialButton btnSave;

    private LinearLayout.LayoutParams layoutProductParams;
    private LinearLayout.LayoutParams layoutCameraParams;

    private SlidingUpPanelLayout slidingUpPanelLayout;

    private Product currentProduct;
    private ProductModel productModel;
    private String currentBarcode;

    private Data data;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_barcode);

        MappingView();

        getProduct();

        MappingEvent();
    }

    private void MappingView() {

//        Layout hien thi product
        layoutProduct = findViewById(R.id.layout_activity_update_barcode_product);
        layoutCamera = findViewById(R.id.layout_activity_update_barcode_camera);

        data = new Data();

        edtBarcode = findViewById(R.id.edt_framgent_scan_barcode);
        edtAmount = findViewById(R.id.edt_framgent_scan_amount);

        productModel = new ProductModel();

//        layoutProductParams = (LinearLayout.LayoutParams) layoutProduct.getLayoutParams();
//        layoutCameraParams = (LinearLayout.LayoutParams) layoutCamera.getLayoutParams();

//        layoutProductParams.weight = 2f;
//        layoutProduct.setLayoutParams(layoutProductParams);
//
//        layoutCameraParams.weight = 8f;
//        layoutCamera.setLayoutParams(layoutCameraParams);




        btnReload = findViewById(R.id.btn_activity_update_barcode_reload);
        btnClose = findViewById(R.id.btn_activity_update_barcode_close_scan);
        btnSave = findViewById(R.id.btn_activity_update_barcode_save);


        tvBarcode = findViewById(R.id.tv_activity_update_barcode_barcode);
        tvNameProduct = findViewById(R.id.tv_framgent_scan_name);

        layoutEdtBarcode = findViewById(R.id.layout_edt_activity_update_barcode_barcode);

        ivImgProduct = findViewById(R.id.iv_activity_update_barcode_img);

        mCameraPreview = (SurfaceView) findViewById(R.id.camera_view);

        slidingUpPanelLayout = findViewById(R.id.sliding_layout_activity_update_barcode);

        slidingUpPanelLayout.setAnchorPoint(0.9f);





    }

    private void MappingEvent(){


//        EVENT BUTTON
        btnReload.setOnClickListener(this::onClick);
        btnSave.setOnClickListener(this::onClick);
        btnClose.setOnClickListener(this::onClick);


        //      DETECTOR BARCODE MAPPING
        mBarcodeDetector = new BarcodeDetector.Builder(this).setBarcodeFormats(Barcode.EAN_13|Barcode.QR_CODE).build();

        mCameraSource = new CameraSource.Builder(UpdateBarcodeActivity.this, mBarcodeDetector)
                .setFacing(CameraSource.CAMERA_FACING_BACK)
                .setRequestedFps(35.0f)
                .setAutoFocusEnabled(true)
                .build();

        mCameraPreview.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder surfaceHolder) {
                try {
                    if (ActivityCompat.checkSelfPermission(UpdateBarcodeActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        return;
                    }
                    mCameraSource.start(mCameraPreview.getHolder());
                } catch (IOException ie) {
                    Log.e("CAMERA SOURCE", ie.getMessage());
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
                mCameraSource.stop();
            }
        });

        mBarcodeDetector.setProcessor(new Detector.Processor<Barcode>() {
            @Override
            public void release() {
            }

            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {

                SparseArray<Barcode> barcodes = detections.getDetectedItems();

                if (barcodes != null && barcodes.size() > 0 && firstDetection) {
                    currentBarcode = barcodes.valueAt(0).displayValue;
                    setBarcodeProduct(barcodes.valueAt(0).displayValue);
                    return;

                }
            }
        });

        slidingUpPanelLayout.addPanelSlideListener(new SlidingUpPanelLayout.PanelSlideListener() {
            @Override
            public void onPanelSlide(View panel, float slideOffset) {


            }

            @Override
            public void onPanelStateChanged(View panel, SlidingUpPanelLayout.PanelState previousState, SlidingUpPanelLayout.PanelState newState) {
                if (SlidingUpPanelLayout.PanelState.EXPANDED == newState){
                    firstDetection = false;
                }
                if (SlidingUpPanelLayout.PanelState.ANCHORED == newState){
                    firstDetection = false;
                }
                if (SlidingUpPanelLayout.PanelState.COLLAPSED == newState){
                    firstDetection = true;
                }
            }
        });
    }

    private void setBarcodeProduct(String displayValue) {
        slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.EXPANDED);
        tvBarcode.setText(displayValue);
        edtBarcode.setText(displayValue);
    }

    @Override
    public void onClick(View view) {
        if (view == btnSave){
//            Kiem tra du lieu
            if (currentProduct == null){
                Toast.makeText(this, "Không tìm thấy sản phẩm cần cập nhật!", Toast.LENGTH_SHORT).show();
                return;
            }
            if (edtAmount.getText().toString() == null && edtBarcode.getText().toString() == null){
                slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
                tvBarcode.setText("Đang quét...");
                firstDetection = true;
                Toast.makeText(this, "Không có gì để cập nhật", Toast.LENGTH_SHORT).show();
                return;
            }
//            Cap nhat ma vach
            if (edtBarcode.getText().toString() != null){
                try {


                    new ProductModel().UpdateBarcode(edtBarcode.getText().toString().trim(), currentProduct.getId().trim());
                    Toast.makeText(this, "Đã cập nhật mã vạch", Toast.LENGTH_SHORT).show();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }

            }

            if (edtAmount.getText().toString() != null){
                int amount = Integer.parseInt(edtAmount.getText().toString().trim());
                try {
                    new ProductModel().UpdateAmount(amount, currentProduct.getId().trim());
                    Toast.makeText(this, "Đã cập nhật số lượng", Toast.LENGTH_SHORT).show();

                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
            data.reloadData(UpdateBarcodeActivity.this);
            finish();
//            Cap nhat so luong

        }

        if (view == btnClose)
            finish();

        if (view == btnReload){
            slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
            firstDetection = true;
            tvBarcode.setText("Đang quét...");
            edtBarcode.setText("");
        }
    }

    public void getProduct() {
        Intent intent = this.getIntent();
        String idProduct = intent.getStringExtra("id");

        Product product = isProductExists(idProduct);
        //            Tim thay san pham
        if (product != null) {
            currentProduct = product;
            String urlImg = "https://dongylamdep.com/img/Product/Small/" + product.getUrlImg();
            Picasso.with(this).load(urlImg).into(ivImgProduct);
            tvNameProduct.setText(product.getName());
            edtAmount.setText(String.valueOf(product.getAmount()));
            tvBarcode.setText("Đang quét...");
        }
        else {
            Toast.makeText(this, "Không tìm thấy sản phẩm!", Toast.LENGTH_SHORT).show();
        }
    }

    public Product isProductExists(String idProduct){
        boolean isBarcodeExists = false;
        for (int i = 0; i < products.size(); i++) {
            if (products.get(i).getId().trim().equalsIgnoreCase(idProduct.trim()))
                return products.get(i);
        }
        Log.i("isBarcodeExists", String.valueOf(isBarcodeExists));
        return null;


    }

    @Override
    public void finish() {
        super.finish();
    }
}