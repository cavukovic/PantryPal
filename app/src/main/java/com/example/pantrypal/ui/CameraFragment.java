package com.example.pantrypal.ui;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.LiveData;

import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.pantrypal.databinding.FragmentCameraBinding;
import com.example.pantrypal.domain.FoodItem;
import com.example.pantrypal.domain.viewmodel.PantryViewModel;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class CameraFragment extends Fragment {

    private FragmentCameraBinding binding;
    private BarcodeDetector barcodeDetector;
    private CameraSource cameraSource;
    private SurfaceView surfaceView;
    private static final int REQUEST_CAMERA_PERMISSION = 201;
    private TextView barcodeText;
    private String barcodeData;
    private PantryViewModel viewModel;
    private String name;
    private int quantity;
    private String unit;
    private LiveData<List<FoodItem>> pantryLiveData;
    private List<FoodItem> foodItemList;

    public CameraFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentCameraBinding.inflate(inflater, container, false);
        surfaceView = binding.surfaceView;
        barcodeText = binding.barcodeText;
        initialiseDetectorsAndSources();
        Activity activity = requireActivity();
        viewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(activity.getApplication()).create(PantryViewModel.class);
        pantryLiveData = viewModel.getAllFoodItemsFromVm();

        pantryLiveData.observe(getViewLifecycleOwner(), itemsList -> {
            // Access the List<Items> here
            if (itemsList != null) {
                foodItemList = itemsList;
            }
        });

        return binding.getRoot();
    }

    private void initialiseDetectorsAndSources() {

        //Toast.makeText(getApplicationContext(), "Barcode scanner started", Toast.LENGTH_SHORT).show();

        barcodeDetector = new BarcodeDetector.Builder(this.getContext())
                .setBarcodeFormats(Barcode.UPC_A | Barcode.UPC_E)
                .build();

        cameraSource = new CameraSource.Builder(this.getContext(), barcodeDetector)
                .setRequestedPreviewSize(1920, 1080)
                .setAutoFocusEnabled(true) //you should add this feature
                .build();

        surfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                try {
                    if (ActivityCompat.checkSelfPermission(CameraFragment.this.getContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                        cameraSource.start(surfaceView.getHolder());
                    } else {
                        ActivityCompat.requestPermissions(CameraFragment.this.getActivity(), new
                                String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                cameraSource.stop();
            }
        });


        barcodeDetector.setProcessor(new Detector.Processor<Barcode>() {
            @Override
            public void release() {
                // Toast.makeText(getApplicationContext(), "To prevent memory leaks barcode scanner has been stopped", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {
                final SparseArray<Barcode> barcodes = detections.getDetectedItems();
                if (barcodes.size() != 0) {


                    barcodeText.post(new Runnable() {

                        @Override
                        public void run() {

                            if (barcodes.valueAt(0).email != null) {
                                barcodeText.removeCallbacks(null);
                                barcodeData = barcodes.valueAt(0).email.address;
                                barcodeText.setText(barcodeData);
                            } else {

                                barcodeData = barcodes.valueAt(0).displayValue;
                                barcodeText.setText(barcodeData);
                            }

                            fetchFoodItem(barcodeData);
                            barcodeData = "";
                        }
                    });

                }
            }
        });
    }

    private void fetchFoodItem(String barcodeData) {

        if (barcodeData != null && !barcodeData.trim().isEmpty()) {
            OkHttpClient client = new OkHttpClient();
            //Log.d("barcodeData", barcodeData);
            String apiKey = "E98BA91E591586263C375F125BD6A7BD";

            String url = "https://api.upcdatabase.org/product/" + barcodeData + "?apikey=" + apiKey;

            Request request = new Request.Builder()
                    .url(url)
                    .build();
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    // Handle the network request failure
                    Log.e("CameraFragment", "Network request failed: " + e.getMessage());
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    if (response.isSuccessful()) {

                        //Find the name of the product
                        String jsonResponseString = response.body().string();

                        String startStr = "\"title\": \"";
                        String endStr = "\",\n    \"alias\":";

                        int startIndex = jsonResponseString.indexOf(startStr);
                        int endIndex = jsonResponseString.indexOf(endStr);
                        String capturedText = "";
                        if (startIndex != -1 && endIndex != -1 && startIndex < endIndex) {
                            capturedText = jsonResponseString.substring(startIndex + startStr.length(), endIndex);
                            System.out.println("Captured Text: " + capturedText);
                        } else {
                            System.out.println("Strs not found or in the wrong order");
                        }

                        //Store the product
                        name = capturedText;

                        if (!name.isEmpty()) {

                            if (foodItemList != null) {
                                Log.d("Made it here", "made it here");
                                for (int i = 0; i < foodItemList.size(); i++) {
                                    FoodItem fItem = foodItemList.get(i);
                                    if (fItem.getName().equals(name)) {
                                        quantity = fItem.getQuantity() + 1;
                                        unit = fItem.getUnit();
                                        viewModel.deleteFoodItem(name);
                                    }
                                }
                            }
                            if (unit == null || unit.equals("")) {
                                unit = "Unit";
                                quantity = 1;
                            }
                            viewModel.insertFoodItem(new FoodItem(name, quantity, unit));

                        }

                        name = "";
                        quantity = 0;
                        unit = "";

                        Log.d("CameraFragment", "API response JSON: " + jsonResponseString);

                    } else {
                        Log.e("CameraFragment", "API request failed with code: " + response.code());
                    }
                }
            });
        } else {
            Log.d("barcodeData", "barcodeData not present");
        }

    }
}