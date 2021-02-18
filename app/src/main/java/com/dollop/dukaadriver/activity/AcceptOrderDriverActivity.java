package com.dollop.dukaadriver.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

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
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;

import com.dollop.dukaadriver.DirectionHelper.PointsParser;
import com.dollop.dukaadriver.model.VehicalDTO;
import com.google.android.gms.location.LocationListener;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;


import com.dollop.dukaadriver.DirectionHelper.FetchURL;
import com.dollop.dukaadriver.DirectionHelper.TaskLoadedCallback;
import com.dollop.dukaadriver.R;
import com.dollop.dukaadriver.UtilityTools.GoogleApisHandle;
import com.dollop.dukaadriver.UtilityTools.NetworkUtil;
import com.dollop.dukaadriver.UtilityTools.SavedData;
import com.dollop.dukaadriver.UtilityTools.SessionManager;
import com.dollop.dukaadriver.UtilityTools.Utility;
import com.dollop.dukaadriver.UtilityTools.Utils;
import com.dollop.dukaadriver.firebase.Config;
import com.dollop.dukaadriver.model.AllResponse;
import com.dollop.dukaadriver.model.DistanceDTO;
import com.dollop.dukaadriver.model.DistanceRespone;
import com.dollop.dukaadriver.model.OrderDTO;
import com.dollop.dukaadriver.retrofit.ApiClient;
import com.dollop.dukaadriver.retrofit.ApiInterface;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
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
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class AcceptOrderDriverActivity extends AppCompatActivity implements View.OnClickListener, OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener, TaskLoadedCallback {

    ImageView back_image, map_navigation;
    String pickup = "Start Job";
    Button pickup_parcel_btn, call_btn, call_to_distributore_btn;
    OrderDTO mOrderDTO;

    private Polyline currentPolyline;
    private Marker PickUpMarker, DropUpMarker;
    LatLng currentLatLng, customerLatlong;

    double currentLatitude = 0.00;
    double currentLongitude = 0.00;
    private GoogleMap mMap;
    Dialog dialog;
    String km = "";
    String time_str = "";

    int count = 0;
    private LocationListener locationListener;

    private FusedLocationProviderClient fusedLocationClient;
    private static final int ALL_PERMISSIONS_RESULT = 1011;
    private ArrayList<String> permissionsToRequest;
    private ArrayList<String> permissionsRejected = new ArrayList<>();
    private ArrayList<String> permissions = new ArrayList<>();

    List<Place.Field> fields;
    public GoogleApisHandle googleApisHandle;
    private GoogleApiClient googleApiClient;
    TextView address_status_tv, address_one_tv, address_tv, distance_tv, distance_Title_tv, time_tv;
    SessionManager sessionManager;

    double customerLatiitude = 0.00;
    double customerLongituded = 0.00;
    String retailer_phone = "", string_stage = "";
    String distributore_number = "";

    DistanceRespone mDistanceRespone;
    private Location location;

    private LocationRequest locationRequest;
    private static final long UPDATE_INTERVAL = 60000, FASTEST_INTERVAL = 5000; // = 5 seconds

    boolean distributore = false;

    ArrayList<VehicalDTO> mVehicalDTOS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        setContentView(R.layout.activity_accept_order_driver);

        sessionManager = new SessionManager(this);
        mOrderDTO = (OrderDTO) getIntent().getSerializableExtra("model");
        Utils.E("RetilerLandmark::::::" + mOrderDTO.getRetailer_landmark());
        string_stage = getIntent().getStringExtra("stage");

        initialization();
        googleMethod();
        setData();


        Timer T = new Timer();
        T.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        updatelatLong();
                        //   Log.e("timer", "" + T);
                    }
                });
            }
        }, 50000, 50000);


    }

    @Override
    public void onClick(View v) {
        if (v == pickup_parcel_btn) {

            if (pickup.equals("Start Job")) {

                map_navigation.setVisibility(View.VISIBLE);
                startJob("Start_Job");

                customerLatiitude = Double.parseDouble(mOrderDTO.getStoreLat());
                customerLongituded = Double.parseDouble(mOrderDTO.getStoreLong());

                distributore = false;
            } else if (pickup.equals("pick up")) {

                pichUpOrder();
                distributore = false;

            } else if (pickup.equals("start job drop")) {

                startJob("On_the_way");
                map_navigation.setVisibility(View.VISIBLE);
                address_status_tv.setText("Drop Location");

                address_one_tv.setText(mOrderDTO.getRetailer_landmark() + "," + mOrderDTO.getRetailerAddress());
                address_tv.setText(mOrderDTO.getRetailerAddress());

                customerLatiitude = Double.parseDouble(mOrderDTO.getRetailerLat());
                customerLongituded = Double.parseDouble(mOrderDTO.getRetailerLong());

                show_retailer_distance();

                distributore = true;
            } else if (pickup.equals("arrived")) {
                address_status_tv.setText("Drop Location");
                show_retailer_distance();
                map_navigation.setVisibility(View.VISIBLE);
                startActivity(new Intent(AcceptOrderDriverActivity.this, DeliveryPrefrences.class)
                        .putExtra("model", mOrderDTO));
                finish();


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

        googleApiClient = new GoogleApiClient.Builder(getApplicationContext()).
                addApi(LocationServices.API).
                addConnectionCallbacks((GoogleApiClient.ConnectionCallbacks) this).
                addOnConnectionFailedListener((GoogleApiClient.OnConnectionFailedListener) this).build();


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


    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
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

    boolean aBoolean = true;

    @Override
    public void onLocationChanged(Location location) {


        if (location != null) {

            currentLatitude = location.getLatitude();
            currentLongitude = location.getLongitude();

            sessionManager.current_LAT("" + currentLatitude);
            sessionManager.current_LONG("" + currentLongitude);
            if (aBoolean) {
                aBoolean = false;
                new AsyncCaller().execute();
            }


        }


    }


    @Override
    public void onTaskDone(Object... values) {
        if (currentPolyline != null)
            currentPolyline.remove();
        currentPolyline = mMap.addPolyline((PolylineOptions) values[0]);
        TimerStartMethod();
    }

    CountDownTimer countDownTimer;

    private void TimerStartMethod() {
        countDownTimer = new CountDownTimer(30000, 1000) {

            @Override
            public void onTick(long l) {

            }

            @Override
            public void onFinish() {

                new AsyncCaller().execute();

            }
        }.start();
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

        LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, AcceptOrderDriverActivity.this);
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
        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(false);


/*
        T.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                       */
/* myTextView.setText("count="+count);
                        count++;*/
        /*

                        new AsyncCaller().execute();

                    }
                });
            }
        }, 5000, 5000);
*/

        //
      /*  mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                String url = "http://maps.google.com/maps?daddr=" + customerLatiitude + "," + customerLongituded;
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(intent);
            }
        });*/

        //  getCurrentLocation();
    }


    private class AsyncCaller extends AsyncTask<Void, Void, Void> {
        // ProgressDialog pdLoading = new ProgressDialog(AsyncExample.this);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (mMap != null) {
                mMap.clear();
            }
            Log.e("AsyncCaller>>", "AsyncCaller");
            if (!isFinishing())
                pathDrwaMethod(new LatLng(customerLatiitude, customerLongituded), new LatLng(currentLatitude, currentLongitude));

        }


        @Override
        protected Void doInBackground(Void... params) {


            return null;
        }

    }

    private void pathDrwaMethod(LatLng customer, LatLng driver) {

        if (dialog != null) {
            dialog.dismiss();
        }
        Drawable circleDrawable = null;

        if (mOrderDTO.getVehicle_type().equals("Van")) {
            circleDrawable = getResources().getDrawable(R.drawable.ic_van);
        } else if (mOrderDTO.getVehicle_type().equals("Bike")) {
            circleDrawable = getResources().getDrawable(R.drawable.ic_bike);
        } else if (mOrderDTO.getVehicle_type().equals("Truck")) {
            circleDrawable = getResources().getDrawable(R.drawable.ic_truck);
        } else {
            circleDrawable = getResources().getDrawable(R.drawable.ic_bike);
        }

        BitmapDescriptor markerIcon = getMarkerIconFromDrawable(circleDrawable);
        PickUpMarker = mMap.addMarker(new MarkerOptions().position(driver).icon(markerIcon));

        Drawable circleDrawabledest = getResources().getDrawable(R.drawable.ic_pin_red);
        BitmapDescriptor markerIcondest = getMarkerIconFromDrawable(circleDrawabledest);
        DropUpMarker = mMap.addMarker(new MarkerOptions().position(customer).icon(markerIcondest));


        new FetchURL(this).execute(getUrl(PickUpMarker.getPosition(), DropUpMarker.getPosition(), "driving"), "driving");

        Utils.E("getUrl::" + getUrl(PickUpMarker.getPosition(), DropUpMarker.getPosition(), "driving"));
        Log.e("latlong for path", "latlong for path" + customer);
        Log.e("latlong for path", "latlong for path" + driver);
        currentLatLng = driver;
        customerLatlong = customer;
        moveCameraToWantedArea();
        //   updatelatLong();
    }


    private void moveCameraToWantedArea() {
        mMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
            @Override
            public void onMapLoaded() {
                // Set up the bounds coordinates for the area we want the user's viewpoint to be.
                LatLngBounds bounds = new LatLngBounds.Builder()
                        .include(currentLatLng)
                        .include(customerLatlong)
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


    private String getUrl(LatLng origin, LatLng dest, String directionMode) {
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
        return url;
    }

    private BitmapDescriptor getMarkerIconFromDrawable(Drawable drawable) {
        Canvas canvas = new Canvas();
        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        canvas.setBitmap(bitmap);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
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
                            pickup_parcel_btn.setText("Pick up");
                            pickup = "pick up";
                        } else {
                            pickup_parcel_btn.setText("Arrived");
                            pickup = "arrived";
                        }

                     /*   Uri gmmIntentUri = Uri.parse("google.streetview:cbll="+currentLatitude, currentLongitude);
                        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                        mapIntent.setPackage("com.google.android.apps.maps");
                        startActivity(mapIntent);*/


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
                        pickup = "start job drop";

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
        finish();
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        if (googleApiClient != null) {
            googleApiClient.disconnect();
        }
        finish();
        super.onDestroy();

    }

    private void getCurrentLocation() {

        location = googleApisHandle.getLastKnownLocation(AcceptOrderDriverActivity.this);
        if (location != null) {

            setCurrentMarker(location.getLatitude(), location.getLongitude());

            currentLatitude = location.getLatitude();
            currentLongitude = location.getLongitude();

            sessionManager.current_LAT("" + currentLatitude);
            sessionManager.current_LONG("" + currentLongitude);


        } else {
            if (count < 5) {
                count++;
                getCurrentLocation();
            } else {
                ShowRefereshDialog();

            }
        }


    }

    private void setCurrentMarker(double latitude, double longitude) {

        currentLatLng = new LatLng(latitude, longitude);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(currentLatLng));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(15));
        getAddress(latitude, longitude);
    }

    private void ShowRefereshDialog() {
        androidx.appcompat.app.AlertDialog dialog = new androidx.appcompat.app.AlertDialog.Builder(AcceptOrderDriverActivity.this)
                .setMessage("please_refresh_your_location")
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Utils.I(this, AmountDoneNotificationActivity.class, null);
                        dialog.dismiss();
                    }
                }).show();

    }

    public String getAddress(double lat, double lang) {
        try {
            Geocoder geocoder;
            List<Address> addresses;
            geocoder = new Geocoder(AcceptOrderDriverActivity.this);
            if (lat != 0 || lang != 0) {
                addresses = geocoder.getFromLocation(lat, lang, 1);
                String address = addresses.get(0).getAddressLine(0);
                String city = addresses.get(0).getAddressLine(1);
                String country = addresses.get(0).getAddressLine(2);
                String state = addresses.get(0).getSubLocality();

                //  search_TV.setText(address);
                //  strAddress = search_TV.toString();
                return city + "," + country + "-" + state;

            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
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

    private void updatelatLong() {
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        HashMap<String, String> hm = new HashMap<>();
        hm.put("driver_id", sessionManager.getRegisterUser().getId());
        hm.put("driver_lat", "" + currentLatitude);
        hm.put("driver_long", "" + currentLongitude);

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


    /*
        private void getVehicalMethod() {


            ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);

            HashMap<String, String> hm = new HashMap<>();
            hm.put("driver_id", sessionManager.getRegisterUser().getId());

            Call<AllResponse> call = apiService.view_vehicle(hm);
            call.enqueue(new Callback<AllResponse>() {
                @Override
                public void onResponse(Call<AllResponse> call, Response<AllResponse> response) {

                    try {

                        AllResponse body = response.body();

                        if (body.getStatus() == 200) {

                            mVehicalDTOS = body.getmVehicalDTOS();


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
    */
    private void setData() {
        distributore_number = mOrderDTO.getDistribuorMobile();
        retailer_phone = mOrderDTO.getRetailerMobile();
        Log.e("mOrderDTO>>", mOrderDTO.getVehicle_type());

        customerLatiitude = Double.parseDouble(mOrderDTO.getStoreLat());
        customerLongituded = Double.parseDouble(mOrderDTO.getStoreLong());

        map_navigation.setVisibility(View.GONE);

        if (string_stage.equals("0")) {
            map_navigation.setVisibility(View.GONE);
            pickup = "Start Job";
            pickup_parcel_btn.setText("Start job");
            show_distributor_distance();
            distributore = false;
        } else if (string_stage.equals("Start_Job")) {

            map_navigation.setVisibility(View.VISIBLE);
            pickup_parcel_btn.setText("Pick up");
            pickup = "pick up";

            if (NetworkUtil.isNetworkAvailable(AcceptOrderDriverActivity.this)) {
                //   pichUpOrder();
            } else {
                Utility.netConnect(AcceptOrderDriverActivity.this);

            }

            show_distributor_distance();

            distributore = false;


        } else if (string_stage.equals("Pickup")) {


            map_navigation.setVisibility(View.VISIBLE);
            pickup_parcel_btn.setText("Start Job For Delivery");
            pickup = "start job drop";

            show_retailer_distance();
            if (NetworkUtil.isNetworkAvailable(AcceptOrderDriverActivity.this)) {
                //  startJob("On_the_way");
            } else {
                Utility.netConnect(AcceptOrderDriverActivity.this);

            }

            address_status_tv.setText("Drop Location");
            address_one_tv.setText(mOrderDTO.getRetailer_landmark() + "," + mOrderDTO.getRetailerAddress());
            address_tv.setText(mOrderDTO.getRetailerAddress());

            customerLatiitude = Double.parseDouble(mOrderDTO.getRetailerLat());
            customerLongituded = Double.parseDouble(mOrderDTO.getRetailerLong());


            distributore = true;
        } else if (string_stage.equals("On_the_way")) {
            address_status_tv.setText("Drop Location");
            address_one_tv.setText(mOrderDTO.getRetailer_landmark() + "," + mOrderDTO.getRetailerAddress());
            address_tv.setText(mOrderDTO.getRetailerAddress());

            pickup_parcel_btn.setText("Arrived");
            pickup = "arrived";

            show_retailer_distance();
        }
    }

    private void initialization() {
        dialog = Utils.initProgressDialog(this);


        pickup_parcel_btn = findViewById(R.id.pickup_parcel_btn);
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

        address_one_tv.setText(mOrderDTO.getDistributorAddress());
        address_tv.setText(mOrderDTO.getDistributorAddress());

        pickup_parcel_btn.setOnClickListener(this);
        back_image.setOnClickListener(this);
        call_btn.setOnClickListener(this);
        map_navigation.setOnClickListener(this);
        call_to_distributore_btn.setOnClickListener(this);

    }


}
