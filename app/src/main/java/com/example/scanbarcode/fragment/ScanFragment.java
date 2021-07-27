package com.example.scanbarcode.fragment;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
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

import static android.content.Context.INPUT_METHOD_SERVICE;
import static com.example.scanbarcode.data.Data.products;


public class ScanFragment extends Fragment implements View.OnClickListener {

    private static final int REQUEST_CAMERA = 1;
    private SurfaceView mCameraPreview;
    private BarcodeDetector mBarcodeDetector;
    private CameraSource mCameraSource;
    String barcode;



    private TextView tvBarcode;
    private TextView tvNameProduct;

    private boolean firstDetection=true;
    private TextInputLayout layoutEdtAmount;

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

    private Data data;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_scan, container, false);

        MappingView(view);

        MappingEvent(view);

        return view;
    }

    private void MappingView(View view) {

//        Layout hien thi product
        layoutProduct = view.findViewById(R.id.layout_fragment_scan_product);
        layoutCamera = view.findViewById(R.id.layout_fragment_scan_camera);

        data = new Data();

        edtAmount = view.findViewById(R.id.edt_framgent_scan_amount);

        productModel = new ProductModel();

//        layoutProductParams = (LinearLayout.LayoutParams) layoutProduct.getLayoutParams();
//        layoutCameraParams = (LinearLayout.LayoutParams) layoutCamera.getLayoutParams();

//        layoutProductParams.weight = 2f;
//        layoutProduct.setLayoutParams(layoutProductParams);
//
//        layoutCameraParams.weight = 8f;
//        layoutCamera.setLayoutParams(layoutCameraParams);




        btnReload = view.findViewById(R.id.btn_fragment_scan_reload);
        btnClose = view.findViewById(R.id.btn_fragment_scan_close_scan);
        btnSave = view.findViewById(R.id.btn_fragment_scan_save);


        tvBarcode = view.findViewById(R.id.tv_fragment_scan_barcode);
        tvNameProduct = view.findViewById(R.id.tv_framgent_scan_name);

        layoutEdtAmount = view.findViewById(R.id.layout_edt_fragment_scan_amount);

        ivImgProduct = view.findViewById(R.id.iv_fragment_scan_img);

        mCameraPreview = (SurfaceView) view.findViewById(R.id.camera_view);

        slidingUpPanelLayout = view.findViewById(R.id.sliding_layout_fragment_scan);
        slidingUpPanelLayout.setAnchorPoint(0.8f);
        slidingUpPanelLayout.setTouchEnabled(false);





    }

    private void MappingEvent(View view){


//        EVENT BUTTON
        btnReload.setOnClickListener(this::onClick);
        btnSave.setOnClickListener(this::onClick);


        //      DETECTOR BARCODE MAPPING
        mBarcodeDetector = new BarcodeDetector.Builder(getContext()).setBarcodeFormats(Barcode.EAN_13|Barcode.QR_CODE).build();

        mCameraSource = new CameraSource.Builder(getContext(), mBarcodeDetector)
                .setFacing(CameraSource.CAMERA_FACING_BACK)
                .setRequestedFps(35.0f)
                .setAutoFocusEnabled(true)
                .build();

        mCameraPreview.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder surfaceHolder) {
                try {
                    if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
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
                    barcode = barcodes.valueAt(0).displayValue;
                    System.out.println(barcodes.valueAt(0).displayValue);
                    tvBarcode.post(new Runnable() {
                        @Override
                        public void run() {
                            tvBarcode.setText(barcodes.valueAt(0).displayValue);
                        }
                    });
                    getProduct(barcodes.valueAt(0).displayValue);
                    return;

                }
            }
        });
    }

    private void getProduct(String barcode){
        Product product = isBarcodeExists(barcode.trim());
        //            Tim thay san pham

        if (product != null){
            firstDetection=false;
            currentProduct = product;



//            layoutCameraParams.weight = 2f;
//            layoutCamera.setLayoutParams(layoutCameraParams);
//
//            layoutProductParams.weight = 8f;
//            layoutProduct.setLayoutParams(layoutProductParams);

            slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.EXPANDED);

            tvBarcode.setText(barcode);

            tvNameProduct.setText(product.getName());

            String urlImg = "https://dongylamdep.com/img/Product/Small/" + product.getUrlImg();

            Handler uiHandler = new Handler(Looper.getMainLooper());
            uiHandler.post(new Runnable(){
                @Override
                public void run() {
                    Picasso.with(getContext())
                            .load(urlImg)
                            .into(ivImgProduct);
                }
            });

            uiHandler.post(new Runnable() {
                @Override
                public void run() {
                    edtAmount.setText(String.valueOf(product.getAmount()));
                }
            });


        }

        else {
//            Khong tim thay san pham
            tvBarcode.setText("Không tìm thấy sản phẩm!");

        }


    }

    private Product isBarcodeExists(String barcode){
        boolean isBarcodeExists = false;
        Log.i("isBarcodeExists", String.valueOf(products));
        for (int i = 0; i < products.size(); i++) {
            if (products.get(i).getBarCode().trim().equalsIgnoreCase(barcode.trim()))
                return products.get(i);
        }
        Log.i("isBarcodeExists", String.valueOf(isBarcodeExists));
        return null;


    }

    @Override
    public void onClick(View view) {
        if (view == btnReload){
            slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
            firstDetection = true;
            tvBarcode.setText("Đang quét...");
            edtAmount.setText("");
        }

        if (view == btnSave){
            int amount = Integer.parseInt(layoutEdtAmount.getEditText().getText().toString());
            Log.i("amount",String.valueOf(amount));
            Log.i("amount", String.valueOf(currentProduct));
            if (layoutEdtAmount.getEditText().getText().toString() != null && currentProduct != null){
                try {
                    productModel.UpdateAmount(amount, currentProduct.getId().trim());
                    Toast.makeText(getContext(), "Đã cập nhật", Toast.LENGTH_SHORT).show();
                    data.reloadData(getContext());
                    slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
                    firstDetection = true;
                    tvBarcode.setText("Đang quét...");

                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                    Toast.makeText(getContext(), "Cập nhật thất bại", Toast.LENGTH_SHORT).show();
                }
            }
            else {
                Toast.makeText(getContext(), "Cập nhật thất bại", Toast.LENGTH_SHORT).show();
            }
        }

        if (view == btnClose){
            slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
        }

    }


}