/*
 * @copyright : ToXSL Technologies Pvt. Ltd. < www.toxsl.com >
 * @author     : Shiv Charan Panjeta < shiv@toxsl.com >
 *
 * All Rights Reserved.
 * Proprietary and confidential :  All information contained herein is, and remains
 * the property of ToXSL Technologies Pvt. Ltd. and its partners.
 * Unauthorized copying of this file, via any medium is strictly prohibited.
 */

package com.dollop.dukaadriver.UtilityTools;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcel;
import android.os.SystemClock;
import android.util.Log;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import com.dollop.dukaadriver.model.AddressData;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;


import cz.msebera.android.httpclient.HttpEntity;
import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.methods.HttpPost;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;



public class GoogleApisHandle {
    private static final long MIN_TIME_BW_UPDATES = 1000;
    private static final float MIN_DISTANCE_CHANGE_FOR_UPDATES = 10;
    private static GoogleApisHandle mapUtils;
    private static DistanceCalculated onDistanceCalculated;
    Context context;
    private GoogleMap routeMap;
    private LatLng origin, destination;
    private boolean isAddLine;
    private Polyline polyline;
    private Double totalDistance;
    private LocationListener locationListener;
    private OnPolyLineReceived onPolyLineReceived;

    public static GoogleApisHandle getInstance(Context context) {
        if (mapUtils == null)
            mapUtils = new GoogleApisHandle();
        mapUtils.setAct(context);
        return mapUtils;
    }

    private void setAct(Context mAct) {
        this.context = mAct;
    }

    public String getAddress(double lat, double lang) {
        try {
            Geocoder geocoder;
            List<Address> addresses;
            geocoder = new Geocoder(context);
            if (lat != 0 || lang != 0) {
                addresses = geocoder.getFromLocation(lat, lang, 1);
                String address = addresses.get(0).getAddressLine(0);
                String city = addresses.get(0).getAddressLine(1);
                String country = addresses.get(0).getAddressLine(2);
                String state = addresses.get(0).getSubLocality();
                return city + "," + country + "-" + state;
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    public String decodeAddressFromLatLng(Context context, LatLng latLng) {
        String address = "";
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 10);
            JSONObject json;
            if (addresses.size() == 0) {
                json = getJSONfromURL("http://maps.googleapis.com/maps/api/geocode/json?latlng=" + latLng.latitude + "," + latLng.longitude + "&sensor=true");
                try {
                    if (json.getJSONArray("results").length() > 0)
                        address = json.getJSONArray("results").getJSONObject(0).getString("formatted_address");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                if (addresses.get(0).getAdminArea() != null && addresses.get(0).getCountryName() != null)
                    address = addresses.get(0).getAddressLine(0) + "," + addresses.get(0).getLocality() + "," + (addresses.get(0).getAdminArea().equalsIgnoreCase("Punyab") ? "Punjab" : addresses.get(0).getAdminArea()) + "," + addresses.get(0).getCountryName(); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                else if (addresses.get(0).getAdminArea() != null)
                    address = addresses.get(0).getAddressLine(0) + "," + addresses.get(0).getLocality() + "," + (addresses.get(0).getAdminArea().equalsIgnoreCase("Punyab") ? "Punjab" : addresses.get(0).getAdminArea()); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                else if (addresses.get(0).getCountryName() != null)
                    address = addresses.get(0).getAddressLine(0) + "," + addresses.get(0).getLocality() + "," + addresses.get(0).getCountryName(); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                else
                    address = addresses.get(0).getAddressLine(0) + "," + addresses.get(0).getLocality();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return address;
    }

    public LatLng getLatLngFromAddress(String address) {
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        try {
            List<Address> latLng = geocoder.getFromLocationName(address, 10);
            Address location = latLng.get(0);
            if (location == null) {
                JSONObject object = getJSONfromURL("https://maps.googleapis.com/maps/api/geocode/json?address=" + address.replace(" ", "%20") + "&key=AIzaSyA9l7tak8lZrqatDJjb13c3Y5t1e40zZHE");
                JSONObject jsonObject = object.getJSONArray("results").getJSONObject(0).getJSONObject("geometry").getJSONObject("location");
                return new LatLng(jsonObject.getDouble("lat"), jsonObject.getDouble("lng"));
            } else
                return new LatLng(location.getLatitude(), location.getLongitude());
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public AddressData decodeAddressDataFromLatLng(Context context, LatLng latLng) {
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        AddressData addressData = new AddressData(Parcel.obtain());
        String address = "";
        try {
            List<Address> addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 10);
            JSONObject json;
            if (addresses.size() == 0) {
                json = getJSONfromURL("https://maps.googleapis.com/maps/api/geocode/json?latlng=" + latLng.latitude + "," + latLng.longitude + "&sensor=true" + "&key=AIzaSyB8GGGeZluA43Mcju5dO7Uf55xRhTEw09c");
                try {
                    if (json.getJSONArray("results").length() > 0) {
                        addressData.setAddress(json.getJSONArray("results").getJSONObject(0).getString("formatted_address"));
                        addressData.setCountry(json.getJSONArray("results").getJSONObject(0).getJSONArray("address_components").getJSONObject(7).getString("long_name"));
                        addressData.setCity(json.getJSONArray("results").getJSONObject(0).getJSONArray("address_components").getJSONObject(4).getString("long_name"));
                        addressData.setPostalCode(json.getJSONArray("results").getJSONObject(0).getJSONArray("address_components").getJSONObject(8).getString("long_name"));
                        addressData.setState(json.getJSONArray("results").getJSONObject(0).getJSONArray("address_components").getJSONObject(6).getString("long_name"));
                        addressData.setLongitude(String.valueOf(latLng.longitude));
                        addressData.setLatitude(String.valueOf(latLng.latitude));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                if (addresses.get(0).getAdminArea() != null && addresses.get(0).getCountryName() != null)
                    address = addresses.get(0).getAddressLine(0) + "," + addresses.get(0).getLocality() + "," + (addresses.get(0).getAdminArea().equalsIgnoreCase("Punyab") ? "Punjab" : addresses.get(0).getAdminArea()) + "," + addresses.get(0).getCountryName(); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                else if (addresses.get(0).getAdminArea() != null)
                    address = addresses.get(0).getAddressLine(0) + "," + addresses.get(0).getLocality() + "," + (addresses.get(0).getAdminArea().equalsIgnoreCase("Punyab") ? "Punjab" : addresses.get(0).getAdminArea()); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                else if (addresses.get(0).getCountryName() != null)
                    address = addresses.get(0).getAddressLine(0) + "," + addresses.get(0).getLocality() + "," + addresses.get(0).getCountryName(); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                else
                    address = addresses.get(0).getAddressLine(0) + "," + addresses.get(0).getLocality();

                addressData.setAddress(address);
                addressData.setLongitude(String.valueOf(latLng.longitude));
                addressData.setLatitude(String.valueOf(latLng.latitude));
                addressData.setStreet(addresses.get(0).getThoroughfare());
                if (addresses.size() > 2)
                    addressData.setBlock(addresses.get(2).getFeatureName());
                else addressData.setBlock("");

                addressData.setCountry(addresses.get(0).getCountryName());
                addressData.setState(addresses.get(0).getAdminArea());
                addressData.setArea(addresses.get(0).getSubLocality());
                addressData.setPostalCode(addresses.get(0).getPostalCode());
                addressData.setCity(addresses.get(0).getSubAdminArea());
            }


        } catch (IOException e) {
            e.printStackTrace();
        }
        return addressData;
    }

    private JSONObject getJSONfromURL(String url) {

        // initialize
        InputStream is = null;
        String result = "";
        JSONObject jObject = null;

        // http post
        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(url);
            HttpResponse response = httpclient.execute(httppost);
            HttpEntity entity = response.getEntity();
            is = entity.getContent();

        } catch (Exception e) {
            Log.e("log_tag", "Error in http connection " + e.toString());
        }

        // convert response to string
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(is, "iso-8859-1"), 8);
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            is.close();
            result = sb.toString();
        } catch (Exception e) {
            Log.e("log_tag", "Error converting result " + e.toString());
        }

        // try parse the string to a JSON object
        try {
            jObject = new JSONObject(result);
        } catch (JSONException e) {
            Log.e("log_tag", "Error parsing data " + e.toString());
        }

        return jObject;
    }

    public void getAddressDataAsync(double lat, double lang, onAddressFoundListener onAddressFoundListener) {
        this.onAddressFoundListener = onAddressFoundListener;
        new AddressTask(lat, lang).execute();
    }


    private class AddressTask extends AsyncTask<Void, Void, AddressData> {
        private double lat;
        private double lang;

        private AddressTask(double lat, double lang) {
            this.lat = lat;
            this.lang = lang;
        }

        @Override
        protected AddressData doInBackground(Void... params) {
            return getAddressData(lat, lang);
        }

        @Override
        protected void onPostExecute(AddressData addressData) {
            super.onPostExecute(addressData);
            onAddressFound(addressData);
        }
    }

    public AddressData getAddressData(double lat, double lang) {
        AddressData addressData = new AddressData(Parcel.obtain());
        addressData.setLatitude(String.valueOf(lat));
        addressData.setLongitude(String.valueOf(lang));
        try {
            Geocoder geocoder;
            List addresses = null;
            geocoder = new Geocoder(context);
            if (lat != 0 || lang != 0) {
                try {
                    addresses = geocoder.getFromLocation(lat, lang, 1);
                } catch (IOException e) {
                    e.printStackTrace();
                    return null;
                }
                Address address = (Address) addresses.get(0);
                String fullAddress;
                String address0 = address.getAddressLine(0);
                String address1 = address.getAddressLine(1);
                String address2 = address.getAddressLine(2);
                String address3 = address.getAddressLine(3);

                String fullAddress0 = address0 == null ? "" : address0;
                String fullAddress1 = address1 == null ? "" : "," + address1;
                String fullAddress2 = address2 == null ? "" : "," + address2;
                String fullAddress3 = address3 == null ? "" : "," + address3;

                fullAddress = fullAddress0 + fullAddress1 + fullAddress2 + fullAddress3;
                addressData.setAddress(fullAddress);
                String city = address.getLocality();
                addressData.setCity(city != null ? city : "NA");
                String country = address.getCountryName();
                addressData.setCountry(country != null ? country : "NA");
                String state = address.getAdminArea();
                addressData.setState(state != null ? state : "NA");
                String postalCode = address.getPostalCode();
                addressData.setPostalCode(postalCode != null ? postalCode : "NA");
                return addressData;
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    private onAddressFoundListener onAddressFoundListener;

    public interface onAddressFoundListener {
        void onAddressFound(AddressData addressData);
    }

    private void onAddressFound(AddressData addressData) {
        if (onAddressFoundListener != null) {
            onAddressFoundListener.onAddressFound(addressData);
        }
    }


    public Location getLastKnownLocation(Context context) {
        LocationManager mLocationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        List<String> providers = mLocationManager.getProviders(true);
        Location bestLocation = null;
        for (String provider : providers) {
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(context, "Location Permission not specified", Toast.LENGTH_LONG).show();
                return bestLocation;
            }
            Location l = mLocationManager.getLastKnownLocation(provider);
            if (l == null) {
                mLocationManager.requestLocationUpdates(provider, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, new LocationListener() {
                    @Override
                    public void onLocationChanged(Location location) {

                    }

                    @Override
                    public void onStatusChanged(String provider, int status, Bundle extras) {

                    }

                    @Override
                    public void onProviderEnabled(String provider) {

                    }

                    @Override
                    public void onProviderDisabled(String provider) {

                    }
                });
                l = mLocationManager.getLastKnownLocation(provider);
            }
            if (l != null && (bestLocation == null || l.getAccuracy() > bestLocation.getAccuracy())) {
                bestLocation = l;
            }
        }
        if (bestLocation == null && !isGPSEnabled(context)) {
            Toast.makeText(context, "Gps not enabled", Toast.LENGTH_LONG).show();
        }

        return bestLocation;
    }

    private boolean isGPSEnabled(Context context) {
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    public void getDirectionsUrl(LatLng origin, LatLng dest, GoogleMap googleMap) {
        isAddLine = true;
        String str_origin = "origin=" + origin.latitude + ","
                + origin.longitude;
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;
        String sensor = "sensor=false";
        String parameters = str_origin + "&" + str_dest + "&" + sensor;
        String output = "json";
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters;
        new DownloadTask(origin, dest, googleMap).execute(url);
    }

    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(strUrl);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.connect();
            iStream = urlConnection.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));
            StringBuffer sb = new StringBuffer();
            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            data = sb.toString();
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (iStream != null)
                iStream.close();
            if (urlConnection != null)
                urlConnection.disconnect();
        }
        return data;
    }

    public void rotateMarker(double fromLat, double fromLong, double toLat, double toLong, Marker marker, Handler handler) {
        double brng = bearingBetweenLocations(fromLat, fromLong, toLat, toLong);

        float rotation = marker.getRotation();

        if (rotation >= 360) {
            rotation = rotation % 360;
        } else if (rotation <= -360) {
            rotation = rotation % 360;
            rotation = 360 + rotation;
        }

        double newAngle = marker.getRotation();
        if ((brng - rotation) >= 0 && (brng - rotation) <= 180) {
            newAngle = marker.getRotation() + (brng - rotation);
        } else if ((brng - rotation) >= 0 && (brng - rotation) >= 180) {
            newAngle = marker.getRotation() - (360 - (brng - rotation));
        } else if ((brng - rotation) <= 0 && (brng - rotation) >= -180) {
            newAngle = marker.getRotation() + brng - rotation;
        } else if ((brng - rotation) <= 0 && (brng - rotation) <= -180) {
            newAngle = marker.getRotation() + (360 - (rotation - brng));
        }


        long start = SystemClock.uptimeMillis();
        float startRotation = marker.getRotation();
        float toRotation = (float) newAngle;
        long duration = 1500;

        Interpolator interpolator = new LinearInterpolator();
        handler.post(new MyRunnable(start, interpolator, duration, toRotation, startRotation, marker, handler) {
            @Override
            public void run() {
                long elapsed = SystemClock.uptimeMillis() - start;
                float t = interpolator.getInterpolation((float) elapsed / duration);

                float rot = t * toRotation + (1 - t) * startRotation;

                marker.setRotation(-rot >= 180 ? rot / 2 : rot);
                if (t < 1.0) {
                    // Post again 16ms later.
                    handler.postDelayed(this, 16);
                }
            }
        });


        // marker.setRotation((float) newAngle);

    }

    private double bearingBetweenLocations(double fromLat, double fromLong, double toLat, double toLong) {

        double PI = 3.14159;
        double lat1 = fromLat * PI / 180;
        double long1 = fromLong * PI / 180;
        double lat2 = toLat * PI / 180;
        double long2 = toLong * PI / 180;

        double dLon = (long2 - long1);

        double y = Math.sin(dLon) * Math.cos(lat2);
        double x = Math.cos(lat1) * Math.sin(lat2) - Math.sin(lat1) * Math.cos(lat2) * Math.cos(dLon);

        double brng = Math.atan2(y, x);

        brng = Math.toDegrees(brng);
        brng = (brng + 360) % 360;

        return brng;
    }

    public void setPolyLineReceivedListener(OnPolyLineReceived onPolyLineReceived) {
        this.onPolyLineReceived = onPolyLineReceived;
    }

    private interface DistanceCalculated {

        void sendDistance(double distance);
    }

    public interface OnPolyLineReceived {
        void onPolyLineReceived(LatLng origin, LatLng destination, GoogleMap routeMap);
    }

    private class MyRunnable implements Runnable {

        Interpolator interpolator;
        float duration;
        float toRotation;
        float startRotation;
        Marker marker;
        Handler handler;
        long start;

        private MyRunnable(long start, Interpolator interpolator, float duration, float toRotation, float startRotation, Marker marker, Handler handler) {
            this.start = start;
            this.interpolator = interpolator;
            this.duration = duration;
            this.toRotation = toRotation;
            this.startRotation = startRotation;
            this.marker = marker;
            this.handler = handler;
        }

        @Override
        public void run() {

        }
    }

    private class DownloadTask extends AsyncTask<String, Void, String> {

        private DownloadTask(LatLng source, LatLng dest, GoogleMap map) {

            origin = source;
            destination = dest;
            routeMap = map;
        }

        public DownloadTask(LatLng source, LatLng dest, DistanceCalculated distanceCalculated) {

            onDistanceCalculated = distanceCalculated;
            origin = source;
            destination = dest;
        }


        @Override
        protected String doInBackground(String... url) {
            String data = "";
            try {
                data = downloadUrl(url[0]);
            } catch (Exception e) {
                Log.d("Background Task", e.toString());
            }
            return data;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            ParserTask parserTask = new ParserTask();
            parserTask.execute(result);

        }
    }

    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {
        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {
            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;
            try {
                jObject = new JSONObject(jsonData[0]);
                DirectionsJSONParser parser = new DirectionsJSONParser();
                routes = parser.parse(jObject);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return routes;
        }

        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {
            ArrayList<LatLng> points = null;
            PolylineOptions lineOptions = new PolylineOptions();
            if (result == null) {
                return;
            }
            if (result.size() < 1) {
                return;
            }

            for (int i = 0; i < result.size(); i++) {
                points = new ArrayList<LatLng>();
                lineOptions = new PolylineOptions();
                List<HashMap<String, String>> path = result.get(i);
                for (int j = 0; j < path.size(); j++) {
                    HashMap<String, String> point = path.get(j);
                    if (j == 0) {
                        String line = point.get("distance");
                        if (line != null) {
                            String[] parts = line.split(" ");
                            double distance = Double.parseDouble(parts[0].replace(",", "."));

                            int dis = (int) Math.ceil(distance);
                            totalDistance = distance;
                            if (onDistanceCalculated != null) {
                                onDistanceCalculated.sendDistance(distance);
                            }
                        }
                        continue;

                    } else if (j == 1) {

                        String duration = point.get("duration");
                        if (duration.contains("hours")
                                && (duration.contains("mins") || duration
                                .contains("min"))) {

                            String[] arr = duration.split(" ");
                            int timeDur = 0;
                            for (int k = 0; k < arr.length; k++) {
                                if (k == 0)
                                    timeDur = Integer.parseInt(arr[k]) * 60;
                                if (k == 2)
                                    timeDur = timeDur + Integer.parseInt(arr[k]);

                            }

                        } else if (duration.contains("mins")
                                || duration.contains("min")) {
                            String[] words = duration.split(" ");
                        }
                        continue;
                    }

                    double lat = Double.parseDouble(point.get("lat"));
                    double lng = Double.parseDouble(point.get("lng"));
                    LatLng position = new LatLng(lat, lng);
                    points.add(position);
                }

                lineOptions.addAll(points);
                lineOptions.width(5);
                lineOptions.color(Color.BLUE);
            }
            if (polyline != null)
                polyline.remove();
            if (routeMap != null && onPolyLineReceived != null) {
                routeMap.addPolyline(lineOptions);
                onPolyLineReceived.onPolyLineReceived(origin, destination, routeMap);
                LatLngBounds.Builder builder = new LatLngBounds.Builder();
                builder.include(origin);
                builder.include(destination);
                LatLngBounds bounds = builder.build();
                int padding = 10;
                CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds,
                        padding);
                // routeMap.moveCamera(cu);
                routeMap.animateCamera(cu);
            }
        }
    }
}