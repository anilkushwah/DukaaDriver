package com.dollop.dukaadriver.activity;

import android.Manifest;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.dollop.dukaadriver.DirectionHelper.FetchURL;
import com.dollop.dukaadriver.DirectionHelper.PointsParser;
import com.dollop.dukaadriver.DirectionHelper.TaskLoadedCallback;
import com.dollop.dukaadriver.R;
import com.dollop.dukaadriver.UtilityTools.GoogleApisHandle;
import com.dollop.dukaadriver.UtilityTools.NetworkUtil;
import com.dollop.dukaadriver.UtilityTools.SavedData;
import com.dollop.dukaadriver.UtilityTools.SessionManager;
import com.dollop.dukaadriver.UtilityTools.Utility;
import com.dollop.dukaadriver.UtilityTools.Utils;
import com.dollop.dukaadriver.adapter.ProductItemListAdapter;
import com.dollop.dukaadriver.firebase.Config;
import com.dollop.dukaadriver.model.AllResponse;
import com.dollop.dukaadriver.model.DistanceDTO;
import com.dollop.dukaadriver.model.DistanceRespone;
import com.dollop.dukaadriver.model.OrderDTO;
import com.dollop.dukaadriver.model.OrderItem;
import com.dollop.dukaadriver.model.VehicalDTO;
import com.dollop.dukaadriver.retrofit.ApiClient;
import com.dollop.dukaadriver.retrofit.ApiInterface;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class AcceptOrderDriverActivity extends AppCompatActivity implements View.OnClickListener, OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, TaskLoadedCallback {

    private static final int ALL_PERMISSIONS_RESULT = 1011;
    private static final long UPDATE_INTERVAL = 1000, FASTEST_INTERVAL = 1000; // = 5 seconds
    public boolean aBoolean = true;
    private final ArrayList<String> permissionsRejected = new ArrayList<>();
    private final ArrayList<String> permissions = new ArrayList<>();
    private final BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Get extra data included in the Intent
            Utils.E("broadcast:");
            //  if (active && !isFinishing()) {
            String message = intent.getStringExtra("Message");
            //  ShowDialog();
            //    }


        }
    };
    public GoogleApisHandle googleApisHandle;
    ImageView back_image, map_navigation;
    String pickup = "START JOB";
    Button pickup_parcel_btn;
    TextView call_btn, call_to_distributore_btn;
    OrderDTO mOrderDTO;
    LatLng currentLatLng, customerLatlong;
    double currentLatitude = 0.00;
    double currentLongitude = 0.00;
    Dialog dialog;
    String km = "";
    String time_str = "";
    int count = 0;
    List<Place.Field> fields;
    TextView address_status_tv, address_one_tv, address_tv, order_id_tv, distance_tv, distance_Title_tv, time_tv;
    SessionManager sessionManager;
    double customerLatiitude = 0.00;
    double customerLongituded = 0.00;
    String retailer_phone = "", string_stage = "";
    String distributore_number = "";
    DistanceRespone mDistanceRespone;
    boolean distributore = false;
    ArrayList<VehicalDTO> mVehicalDTOS;
    CountDownTimer countDownTimer;
    FusedLocationProviderClient fusedLocationProviderClient;
    public ArrayList<OrderDTO> itemModels = new ArrayList<>();

    Timer T;
    private Polyline currentPolyline;
    private Marker DriverMarker, DropUpMarker;
    private GoogleMap mMap;
    private LocationListener locationListener;
    private ArrayList<String> permissionsToRequest;
    private GoogleApiClient googleApiClient;
    private Location location;
    private LocationRequest locationRequest;
    private FusedLocationProviderClient fusedLocationClient;
    private Circle userLocationAccuracyCircle;
    LinearLayout ll_itemListId;
    LocationCallback locationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            super.onLocationResult(locationResult);
            if (mMap != null) {
                location = locationResult.getLastLocation();
                Utils.E("location:::" + location);

                updatelatLong(location);
                if (aBoolean) {
                    aBoolean = false;
                    SavedData.savePath(true);

                    pathDrwaMethod(new LatLng(customerLatiitude, customerLongituded), location);
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        setContentView(R.layout.activity_accept_order_driver);

        sessionManager = new SessionManager(this);
        mOrderDTO = (OrderDTO) getIntent().getSerializableExtra("model");
        string_stage = getIntent().getStringExtra("stage");


        initialization();
        googleMethod();
        setData();
     /*   Utils.E("Updatefdasdfs");

        T = new Timer();
        T.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Utils.E("UpdateLocation");

                        //   Log.e("timer", "" + T);
                    }
                });
            }
        }, 3000, 3000);*/


    }

    @Override
    public void onClick(View v) {
        if (v == pickup_parcel_btn) {

            switch (pickup) {
                case "START JOB":
                    map_navigation.setVisibility(View.VISIBLE);
                    startJob("Start_Job");
                    customerLatiitude = Double.parseDouble(mOrderDTO.getStoreLat());
                    customerLongituded = Double.parseDouble(mOrderDTO.getStoreLong());
                    distributore = false;
                    break;
                case "PICK UP":
                    pichUpOrder();
                    distributore = false;
                    break;
                case "Start Job For Delivery":
                    startJob("On_the_way");
                    map_navigation.setVisibility(View.VISIBLE);
                    address_status_tv.setText("Drop Location");

                    address_one_tv.setText(mOrderDTO.getRetailer_landmark() + "," + mOrderDTO.getRetailerAddress());
                    address_tv.setText(mOrderDTO.getRetailer_landmark() + "," + mOrderDTO.getRetailerAddress());

                    customerLatiitude = Double.parseDouble(mOrderDTO.getRetailerLat());
                    customerLongituded = Double.parseDouble(mOrderDTO.getRetailerLong());

                    show_retailer_distance();

                    distributore = true;
                    break;
                case "ARRIVED":
                    arrivedTask();
                    break;
            }

        } else if (v == back_image) {
            startActivity(new Intent(AcceptOrderDriverActivity.this, HomeActivity.class));
            finish();
        } else if (v == call_btn) {

            Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", retailer_phone, null));
            startActivity(intent);
        } else if (v == map_navigation) {

            ConfirmationForGoogleMAp();


        } else if (v == call_to_distributore_btn) {
            Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", distributore_number, null));
            startActivity(intent);
        }
        else if(v==ll_itemListId){
            ProductListDialog(mOrderDTO);

        }
    }

    private void arrivedTask() {
        StringRequest stringRequest = new StringRequest(1, ApiClient.driver_arrived, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Utils.E("response::" + response);
                try {
                    JSONObject object = new JSONObject(response);

                    if (object.getInt("status") == 200) {
                        address_status_tv.setText("Drop Location");
                        show_retailer_distance();
                        map_navigation.setVisibility(View.VISIBLE);
                        startActivity(new Intent(AcceptOrderDriverActivity.this, DeliveryPrefrences.class)
                                .putExtra("model", mOrderDTO));
                        finish();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Utils.E("error::" + error);
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("order_id", mOrderDTO.getId());
                hashMap.put("driver_id",sessionManager.getRegisterUser().getId() );
                return hashMap;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);


    }

    @Override
    public void onBackPressed() {
        // super.onBackPressed();
      /*  startActivity(new Intent(AcceptOrderDriverActivity.this, HomeActivity.class));
        finish();*/
//        countDownTimer.cancel();
        Utils.I_clear(AcceptOrderDriverActivity.this, HomeActivity.class, null);

    }

    private void googleMethod() {

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        googleApiClient = new GoogleApiClient.Builder(this).
                addApi(LocationServices.API).
                addConnectionCallbacks(this).
                addOnConnectionFailedListener(this).build();


        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver,
                new IntentFilter(Config.PUSH_NOTIFICATION));


        fields = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG, Place.Field.ADDRESS);

        permissions.add(Manifest.permission.ACCESS_FINE_LOCATION);
        permissions.add(Manifest.permission.ACCESS_COARSE_LOCATION);
        permissionsToRequest = permissionsToRequest(permissions);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (permissionsToRequest.size() > 0) {
                requestPermissions(permissionsToRequest.toArray(
                        new String[permissionsToRequest.size()]), ALL_PERMISSIONS_RESULT);
            }
        }
        if (ContextCompat.checkSelfPermission(AcceptOrderDriverActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(AcceptOrderDriverActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 101);

        }


        fusedLocationClient = LocationServices.getFusedLocationProviderClient(getApplicationContext());


        if (ContextCompat.checkSelfPermission(getApplicationContext().getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this.getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(AcceptOrderDriverActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 101);
        }

        if (!Places.isInitialized()) {
            Places.initialize(AcceptOrderDriverActivity.this, "\n" + "AIzaSyANub5r9vSq0BTwMQprVi8_Xoe7i8_Ao0I");
        }

        googleApisHandle = GoogleApisHandle.getInstance(AcceptOrderDriverActivity.this);

    }

    private ArrayList<String> permissionsToRequest(ArrayList<String> wantedPermissions) {
        ArrayList<String> result = new ArrayList<>();

        for (String perm : wantedPermissions) {
            if (!hasPermission(perm)) {
                result.add(perm);
            }
        }
        return result;
    }


    private boolean hasPermission(String permission) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return this.checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED;
        }
        return true;
    }

    @Override
    public void onTaskDone(Object... values) {
        if (currentPolyline != null)
            currentPolyline.remove();
        currentPolyline = mMap.addPolyline((PolylineOptions) values[0]);
        if (SavedData.getPath()){
            new CountDownTimer(30000,1000){

                @Override
                public void onTick(long l) {

                }

                @Override
                public void onFinish() {
                    pathDrwaMethod(new LatLng(customerLatiitude, customerLongituded), location);
                }
            }.start();
        }


    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        location = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
        if (location != null) {
            Log.e("Live::", "FusedLocation::" + location.getLatitude());
            Log.e("Live::", "FusedLocation::" + location.getLongitude());
            currentLatitude = location.getLatitude();
            currentLongitude = location.getLongitude();
            currentLatLng = new LatLng(currentLatitude, currentLongitude);

            sessionManager.current_LAT("" + currentLatitude);
            sessionManager.current_LONG("" + currentLongitude);

        }
        startLocationUpdates();
    }

    private void startLocationUpdates() {

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(AcceptOrderDriverActivity.this);
        locationRequest = new LocationRequest();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(UPDATE_INTERVAL);
        locationRequest.setFastestInterval(FASTEST_INTERVAL);

        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "You need to enable permissions to display location !", Toast.LENGTH_SHORT).show();
        }
        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper());


        //  LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationCallback, AcceptOrderDriverActivity.this);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mMap.setMyLocationEnabled(false);
        mMap.getUiSettings().setMyLocationButtonEnabled(false);
        mMap.getUiSettings().setAllGesturesEnabled(false);
        mMap.getUiSettings().setScrollGesturesEnabled(true);
        mMap.getUiSettings().setZoomGesturesEnabled(true);

    }

    private void pathDrwaMethod(LatLng customer, Location location) {
        if (dialog != null) {
            dialog.dismiss();
        }

        if (DriverMarker == null) {
            Drawable circleDrawabledest = getResources().getDrawable(R.drawable.tracking);
            BitmapDescriptor markerIcondest = getMarkerIconFromDrawableHalf(circleDrawabledest);
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(new LatLng(location.getLatitude(), location.getLongitude()));
            markerOptions.icon(markerIcondest);
            markerOptions.rotation(location.getBearing());
            markerOptions.anchor((float) 0.5, (float) 0.5);
            DriverMarker = mMap.addMarker(markerOptions);
        } else {
            DriverMarker.setPosition(new LatLng(location.getLatitude(), location.getLongitude()));
            DriverMarker.setRotation(location.getBearing());

        }
        if (userLocationAccuracyCircle == null) {
            CircleOptions circleOptions = new CircleOptions();
            circleOptions.center(new LatLng(location.getLatitude(), location.getLongitude()));
            circleOptions.strokeWidth(4);
            circleOptions.strokeColor(Color.argb(255, 255, 0, 0));
            circleOptions.fillColor(Color.argb(32, 255, 0, 0));
            circleOptions.radius(location.getAccuracy());
            userLocationAccuracyCircle = mMap.addCircle(circleOptions);
        } else {
            userLocationAccuracyCircle.setCenter(new LatLng(location.getLatitude(), location.getLongitude()));
            userLocationAccuracyCircle.setRadius(location.getAccuracy());
        }


        Drawable circleDrawabledest = getResources().getDrawable(R.drawable.ic_pin_red);
        BitmapDescriptor markerIcondest = getMarkerIconFromDrawable(circleDrawabledest);
        DropUpMarker = mMap.addMarker(new MarkerOptions().position(customer).icon(markerIcondest).title(mOrderDTO.getRetailerName()));

        new FetchURL(this).execute(getUrl(DriverMarker.getPosition(), DropUpMarker.getPosition(),
                "driving"), "driving");
        moveCameraToWantedArea(new LatLng(location.getLatitude(), location.getLongitude()), customer);

    }

    private void moveCameraToWantedArea(LatLng myLatlang, LatLng customer) {
        mMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
            @Override
            public void onMapLoaded() {
                // Set up the bounds coordinates for the area we want the user's viewpoint to be.
                LatLngBounds bounds = new LatLngBounds.Builder()
                        .include(myLatlang)
                        .include(customer)
                        .build();
                // Move the camera now.
                mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 200));
            }
        });
        distanceFrom_in_Km();
    }

    private void distanceFrom_in_Km() {
        JSONArray jRoutes;
        JSONArray jLegs;
        JSONObject distance;
        JSONObject time;
        try {
            jRoutes = PointsParser.jObject.getJSONArray("routes");
            for (int i = 0; i < jRoutes.length(); i++) {
                jLegs = ((JSONObject) jRoutes.get(i)).getJSONArray("legs");
                for (int j = 0; j < jLegs.length(); j++) {
                    distance = ((JSONObject) jLegs.get(i)).getJSONObject("distance");
                    time = ((JSONObject) jLegs.get(i)).getJSONObject("duration");
                    km = distance.getString("text");
                    time_str = time.getString("text");

                    Utils.E("km" + km);

                    time_tv.setText(time_str);
                    distance_tv.setText(km);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception e) {
        }
    }



    @NonNull
    private String getUrl(@NonNull LatLng origin, @NonNull LatLng dest, String directionMode) {
        // Origin of route
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;
        // Destination of route
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;
        // Mode
        String mode = "mode=" + directionMode;
        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + "&" + mode;
        // Output format
        String output = "json";
        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters + "&key=" +
                "AIzaSyA9l7tak8lZrqatDJjb13c3Y5t1e40zZHE";
        Utils.E("Url:::" + url);
        return url;
    }

    private BitmapDescriptor getMarkerIconFromDrawable(@NonNull Drawable drawable) {
        Canvas canvas = new Canvas();
        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        canvas.setBitmap(bitmap);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        drawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

    private BitmapDescriptor getMarkerIconFromDrawableHalf(@NonNull Drawable drawable) {
        Canvas canvas = new Canvas();
        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth() / 2, drawable.getIntrinsicHeight() / 2, Bitmap.Config.ARGB_8888);
        canvas.setBitmap(bitmap);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth() / 2, drawable.getIntrinsicHeight() / 2);
        drawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

    private void startJob(String status) {
        final Dialog dialog = Utils.initProgressDialog(this);
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);

        HashMap<String, String> hm = new HashMap<>();
        hm.put("driver_id", sessionManager.getRegisterUser().getId());
        hm.put("order_id", mOrderDTO.getId());
        hm.put("order_driver_job", status);

        Call<AllResponse> call = apiService.start_job(hm);
        call.enqueue(new Callback<AllResponse>() {
            @Override
            public void onResponse(Call<AllResponse> call, Response<AllResponse> response) {
                dialog.dismiss();
                try {

                    AllResponse body = response.body();

                    if (body.getStatus() == 200) {

                        if (status.equals("Start_Job")) {
                            pickup_parcel_btn.setText("PICK UP");
                            pickup = "PICK UP";
                        } else {
                            pickup_parcel_btn.setText("ARRIVED");
                            pickup = "ARRIVED";
                        }


                    } else {
                        ShowDialog(body.getMessage());
                    }

                } catch (
                        Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<AllResponse> call, Throwable t) {
                call.cancel();
                t.printStackTrace();
                dialog.dismiss();
            }
        });
    }

    private void ShowDialog(String sms) {
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setMessage(sms)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Utils.I(this, AmountDoneNotificationActivity.class, null);
                        dialog.dismiss();
                    }
                }).show();

    }

    private void pichUpOrder() {
        final Dialog dialog = Utils.initProgressDialog(this);
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);

        HashMap<String, String> hm = new HashMap<>();
        hm.put("driver_id", sessionManager.getRegisterUser().getId());
        hm.put("order_id", mOrderDTO.getId());

        Call<AllResponse> call = apiService.picked_order(hm);
        call.enqueue(new Callback<AllResponse>() {
            @Override
            public void onResponse(Call<AllResponse> call, Response<AllResponse> response) {
                dialog.dismiss();
                try {

                    AllResponse body = response.body();

                    if (body.getStatus() == 200) {

                        pickup_parcel_btn.setText("Start Job For Delivery");
                        pickup = "Start Job For Delivery";


                        address_status_tv.setText("Drop Location");

                        address_one_tv.setText(mOrderDTO.getRetailer_landmark() + "," + mOrderDTO.getRetailerAddress());
                        address_tv.setText(mOrderDTO.getRetailer_landmark() + "," + mOrderDTO.getRetailerAddress());
                    } else {
                        ShowDialog(body.getMessage());
                    }


                    //   Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                    //     Uri.parse("geo:46.7170627,-71.2884537"));
                    //  startActivity(intent);

                } catch (
                        Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<AllResponse> call, Throwable t) {
                call.cancel();
                t.printStackTrace();
                dialog.dismiss();
            }
        });
    }

    private void show_distributor_distance() {

        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);


        HashMap<String, String> hm = new HashMap<>();
        hm.put("driver_lat", sessionManager.getCurrent_LAT());
        hm.put("driver_long", sessionManager.getCurrent_LONG());
        hm.put("driver_id", sessionManager.getRegisterUser().getId());
        hm.put("distributor_lat", mOrderDTO.getStoreLat());
        hm.put("distributor_long", mOrderDTO.getStoreLong());
        Utils.E("hm:::" + hm);

        Call<DistanceDTO> call = apiService.driver_distance(hm);

        call.enqueue(new Callback<DistanceDTO>() {
            @Override
            public void onResponse(Call<DistanceDTO> call, Response<DistanceDTO> response) {

                try {

                    DistanceDTO body = response.body();


                    if (body.getStatus() == 200) {
                        mDistanceRespone = new DistanceRespone();
                        mDistanceRespone = body.getDistance();

                        distance_Title_tv.setText("Distributor Distance");
                        time_tv.setText(time_str);
                        distance_tv.setText(km);
                    }

                } catch (
                        Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<DistanceDTO> call, Throwable t) {
                call.cancel();
                t.printStackTrace();

            }
        });
    }

    private void show_retailer_distance() {

        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);


        HashMap<String, String> hm = new HashMap<>();
        hm.put("driver_lat", sessionManager.getCurrent_LAT());
        hm.put("driver_long", sessionManager.getCurrent_LONG());
        hm.put("driver_id", sessionManager.getRegisterUser().getId());
        hm.put("retailer_lat", mOrderDTO.getRetailerLat());
        hm.put("retailer_long", mOrderDTO.getRetailerLong());
        Utils.E("hm:::" + hm);
        Call<DistanceDTO> call = apiService.driver_distance(hm);

        call.enqueue(new Callback<DistanceDTO>() {
            @Override
            public void onResponse(Call<DistanceDTO> call, Response<DistanceDTO> response) {

                try {

                    DistanceDTO body = response.body();

                    if (body.getStatus() == 200) {

                        mDistanceRespone = new DistanceRespone();
                        mDistanceRespone = body.getDistance();

                        distance_tv.setText(mDistanceRespone.getDistanceInKm());
                        distance_Title_tv.setText("Retailer Distance");
                        time_tv.setText(mDistanceRespone.getTime());


                        //  calculateTime();

                    }

                } catch (
                        Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<DistanceDTO> call, Throwable t) {
                call.cancel();
                t.printStackTrace();

            }
        });
    }

    @Override
    protected void onStart() {
        if (googleApiClient != null) {
            googleApiClient.connect();
        }
        super.onStart();
    }

    @Override
    protected void onPause() {

        if (googleApiClient != null) {
            googleApiClient.disconnect();
        }
        finish();
        super.onPause();
    }

    @Override
    protected void onStop() {

        if (googleApiClient != null) {
            googleApiClient.disconnect();
        }
       // T.cancel();
        fusedLocationProviderClient.removeLocationUpdates(locationCallback);
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        if (googleApiClient != null) {
            googleApiClient.disconnect();
        }
       // T.cancel();
        fusedLocationProviderClient.removeLocationUpdates(locationCallback);
        super.onDestroy();

    }


    private void ConfirmationForGoogleMAp() {
        androidx.appcompat.app.AlertDialog dialog = new androidx.appcompat.app.AlertDialog.Builder(AcceptOrderDriverActivity.this)
                .setMessage("Are Your Sure You Want To Switch On Google Map !")
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Utils.I(this, AmountDoneNotificationActivity.class, null);

                        String url = "http://maps.google.com/maps?daddr=" + customerLatiitude + "," + customerLongituded;
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                        startActivity(intent);

                        dialog.dismiss();
                    }
                }).show();

    }

    private void updatelatLong(Location location) {
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        HashMap<String, String> hm = new HashMap<>();
        hm.put("driver_id", sessionManager.getRegisterUser().getId());
        hm.put("driver_lat", "" + location.getLatitude());
        hm.put("driver_long", "" + location.getLongitude());
        Utils.E("ParamUpdate::" + hm);

        Call<AllResponse> call = apiService.driver_update_lat_long(hm);
        call.enqueue(new Callback<AllResponse>() {
            @Override
            public void onResponse(Call<AllResponse> call, Response<AllResponse> response) {

                try {

                    AllResponse body = response.body();

                    if (body.getStatus() == 200) {


                    }

                } catch (
                        Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<AllResponse> call, Throwable t) {
                call.cancel();
                t.printStackTrace();

            }
        });
    }

    private void setData() {
        distributore_number = mOrderDTO.getDistribuorMobile();
        retailer_phone = mOrderDTO.getRetailerMobile();
        Log.e("mOrderDTO>>", mOrderDTO.getVehicle_type());

        customerLatiitude = Double.parseDouble(mOrderDTO.getStoreLat());
        customerLongituded = Double.parseDouble(mOrderDTO.getStoreLong());

        map_navigation.setVisibility(View.GONE);

        if (string_stage.equals("0")) {
            map_navigation.setVisibility(View.GONE);
            pickup = "START JOB";
            pickup_parcel_btn.setText("START JOB");
            show_distributor_distance();
            distributore = false;
        } else if (string_stage.equals("Start_Job")) {

            map_navigation.setVisibility(View.VISIBLE);
            pickup_parcel_btn.setText("PICK UP");
            pickup = "PICK UP";
/*
            if (NetworkUtil.isNetworkAvailable(AcceptOrderDriverActivity.this)) {
                //   pichUpOrder();
            } else {
                Utility.netConnect(AcceptOrderDriverActivity.this);

            }*/

            show_distributor_distance();

            distributore = false;


        } else if (string_stage.equals("Pickup")) {


            map_navigation.setVisibility(View.VISIBLE);
            pickup_parcel_btn.setText("Start Job For Delivery");
            pickup = "Start Job For Delivery";

            show_retailer_distance();
          /*  if (NetworkUtil.isNetworkAvailable(AcceptOrderDriverActivity.this)) {
                //  startJob("On_the_way");
            } else {
                Utility.netConnect(AcceptOrderDriverActivity.this);

            }*/

            address_status_tv.setText("Drop Location");
            address_one_tv.setText(mOrderDTO.getRetailer_landmark() + "," + mOrderDTO.getRetailerAddress());
            address_tv.setText(mOrderDTO.getRetailer_landmark() + "," + mOrderDTO.getRetailerAddress());

            customerLatiitude = Double.parseDouble(mOrderDTO.getRetailerLat());
            customerLongituded = Double.parseDouble(mOrderDTO.getRetailerLong());


            distributore = true;
        } else if (string_stage.equals("On_the_way")) {
            address_status_tv.setText("Drop Location");
            address_one_tv.setText(mOrderDTO.getRetailer_landmark() + "," + mOrderDTO.getRetailerAddress());
            address_tv.setText(mOrderDTO.getRetailer_landmark() + "," + mOrderDTO.getRetailerAddress());

            pickup_parcel_btn.setText("ARRIVED");
            pickup = "ARRIVED";

            show_retailer_distance();
        }
    }

    private void initialization() {
        dialog = Utils.initProgressDialog(this);


        pickup_parcel_btn = findViewById(R.id.pickup_parcel_btn);
        order_id_tv = findViewById(R.id.order_id_tv);
        back_image = findViewById(R.id.back_image);

        address_status_tv = findViewById(R.id.address_status_tv);
        address_one_tv = findViewById(R.id.address_one_tv);
        address_tv = findViewById(R.id.address_tv);
        call_btn = findViewById(R.id.call_btn);
        distance_tv = findViewById(R.id.distance_tv);
        distance_Title_tv = findViewById(R.id.distance_Title_tv);
        map_navigation = findViewById(R.id.map_navigation);
        time_tv = findViewById(R.id.time_tv);
        call_to_distributore_btn = findViewById(R.id.call_to_distributore_btn);
        ll_itemListId = findViewById(R.id.ll_itemListId);

        address_one_tv.setText(mOrderDTO.getDistributor_landmark() + "," + mOrderDTO.getDistributorAddress());
        address_tv.setText(mOrderDTO.getDistributor_landmark() + "," + mOrderDTO.getDistributorAddress());
        order_id_tv.setText("Order ID-: #000" + mOrderDTO.getId());

        pickup_parcel_btn.setOnClickListener(this);
        back_image.setOnClickListener(this);
        call_btn.setOnClickListener(this);
        map_navigation.setOnClickListener(this);
        call_to_distributore_btn.setOnClickListener(this);
        ll_itemListId.setOnClickListener(this);

    }

    private void ProductListDialog(OrderDTO mOrderDTO) {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_product_list);
        RecyclerView rv_product_list = dialog.findViewById(R.id.rv_product_list);
        rv_product_list.setLayoutManager(new LinearLayoutManager(this));
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setAttributes(lp);
        ProductItemListAdapter itemAdapter = new ProductItemListAdapter(this,itemModels);
        rv_product_list.setAdapter(itemAdapter);
        TextView tv_total_ProductPrice = dialog.findViewById(R.id.tv_total_ProductPrice);
     //  String Amount= (Utils.getFormatedAmount(Integer.parseInt(itemModels.getProductDiscountedPrice())));
      // tv_total_ProductPrice.setText(this.getString(R.string.currency_sign) + Amount);
        dialog.show();
    }
}
