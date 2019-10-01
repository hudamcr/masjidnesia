package com.sidoarjolaptopservice.dashboard_donatur;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

import android.Manifest;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sidoarjolaptopservice.dashboard_donatur.adapter.JarakAdapter;
import com.sidoarjolaptopservice.dashboard_donatur.model.Jarak;
import com.sidoarjolaptopservice.masjid.LoginActivity;
import com.sidoarjolaptopservice.masjid.R;
import com.sidoarjolaptopservice.masjid.app.AppController;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.sidoarjolaptopservice.masjid.LoginActivity.TAG_ID;
import static com.sidoarjolaptopservice.masjid.LoginActivity.session_status;
import static java.lang.Math.round;


public class MasjidTerdekatActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, com.google.android.gms.location.LocationListener,
        EasyPermissions.PermissionCallbacks,
        EasyPermissions.RationaleCallbacks,
        SwipeRefreshLayout.OnRefreshListener{

    Double latitude=0.0, longitude=0.0;
    Criteria criteria;
    Location location;
    LocationManager locationManager;
    String provider;
    private GoogleApiClient mGoogleApiClient;
    private Location mLocation;
    private LocationManager mLocationManager;
    private LocationRequest mLocationRequest;
    private com.google.android.gms.location.LocationListener listener;
    private long UPDATE_INTERVAL = 10000 * 1000;  /* 10 secs */
    private long FASTEST_INTERVAL = 200000000; /* 2 sec */
    private static final String TAG = "MasjidTerdekatActivity";
    TextView mLatitudeTextView,mLongtitudeTextView;
    private static final String[] LOCATION =
            {Manifest.permission.ACCESS_FINE_LOCATION};

    private static final int RC_LOCATION_PERM = 124;
    private static final String url ="http://sidoarjolaptopservice.com/lomba/terdekat.php";
    Double jarak= Double.valueOf(0);
    String nama;
    JarakAdapter adapter;
    List<Jarak> itemList;
    RecyclerView list;
    ProgressBar bar;
    private SearchView searchView2;
    public String jenis_donasi;
    SharedPreferences sharedpreferences;
    Boolean session = false;
    public String id_user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_masjid_terdekat);
        sharedpreferences = getSharedPreferences(LoginActivity.my_shared_preferences, Context.MODE_PRIVATE);
        session = sharedpreferences.getBoolean(session_status, false);
        id_user = sharedpreferences.getString(TAG_ID, null);
        Toast.makeText(getApplicationContext(),id_user,Toast.LENGTH_SHORT).show();
        list = (RecyclerView) findViewById(R.id.recylcerView);
        bar =findViewById(R.id.progressBar);
        ImageView imageView =findViewById(R.id.imageClose2);
        jenis_donasi=getIntent().getStringExtra("jenis_donasi");
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(),DonaturActivity.class);
                startActivity(intent);
                finishAffinity();
            }
        });

        tampil_masjid(latitude,longitude);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView2=(SearchView)findViewById(R.id.action_search2);

        searchView2.setSearchableInfo(searchManager
                .getSearchableInfo(getComponentName()));
        searchView2.setMaxWidth(Integer.MAX_VALUE);
        searchView2.setQueryHint("Cari...");

        searchView2.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchView2.clearFocus();
                adapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                // filter recycler view when text is changed
                adapter.getFilter().filter(query);
                return false;
            }
        });

        itemList = new ArrayList<>();
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 1);
        adapter = new JarakAdapter(this,itemList, this);
        list.setLayoutManager(mLayoutManager);
        list.setAdapter(adapter);

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        mLocationManager = (LocationManager)this.getSystemService(Context.LOCATION_SERVICE);
        lokasi();
//         tampil_masjid(-7.349565,112.6867376);
//        swipe.post(new Runnable() {
//                       @Override
//                       public void run() {
//                           lokasi();
//                       }
//                   }
//        );
//        locationAndContactsTask();

    }

    private void tampil_masjid(final Double lat, final Double lng) {

        StringRequest strReq = new StringRequest(Request.Method.POST,url, new Response.Listener<String>() {
            @Override
            public void onResponse(final String response) {
                new android.os.Handler().postDelayed(
                        new Runnable() {
                            public void run() {
                                //Your second request goes here
                                Log.e(TAG, "Input Respon: " + response.toString());
                                bar.setVisibility(View.GONE);
//                                Toast.makeText(getApplicationContext(),response.toString(),Toast.LENGTH_SHORT).show();
                                List<Jarak> items = new Gson().fromJson(response.toString(), new TypeToken<List<Jarak>>() {
                                }.getType());
                                itemList.clear();
                                itemList.addAll(items);
                                adapter.notifyDataSetChanged();
                            }
                        },
                        1000);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // error in getting json
                String message = null;
                if (error instanceof NetworkError) {
                    message = "Tidak ada koneksi Internet...Tolong cek koneksi anda!";
                } else if (error instanceof ServerError) {
                    message = "Server tidak di temukan. Tolong coba lagi nanti!!";
                } else if (error instanceof AuthFailureError) {
                    message = "Tidak ada koneksi Internet...Tolong cek koneksi anda!";
                } else if (error instanceof ParseError) {
                    message = "Parsing data gagal! Tolong coba lagi nanti!!";
                } else if (error instanceof NoConnectionError) {
                    message = "Tidak ada koneksi Internet...Tolong cek koneksi anda!";
                } else if (error instanceof TimeoutError) {
                    message = "Tidak ada koneksi Internet...Tolong cek koneksi anda!";
                }
                Toast.makeText(getApplicationContext(),message,Toast.LENGTH_SHORT).show();
                Log.e(TAG, "Error: " + error.getMessage());
            }
        })
        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Authorization", "Basic cHRwaTpwdHBpMTIzLg==");
                return headers;
            }
            @Override
            protected Map<String, String> getParams() {
            Map<String, String> params = new HashMap<String, String>();
            params.put("lat","-7.3495703" );
            params.put("lng","112.6867376" );
            Log.e(TAG, " " + params);
            return params;
        }
        };
        AppController.getInstance().addToRequestQueue(strReq);
    }


    @Override
    public void onConnected(Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

        startLocationUpdates();

        mLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

        if(mLocation == null){
            startLocationUpdates();
        }
        if (mLocation != null) {

            // mLatitudeTextView.setText(String.valueOf(mLocation.getLatitude()));
            //mLongitudeTextView.setText(String.valueOf(mLocation.getLongitude()));
        } else {
            Toast.makeText(this, "Location not Detected", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.i(TAG, "Connection Suspended");
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.i(TAG, "Connection failed. Error: " + connectionResult.getErrorCode());
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    protected void startLocationUpdates() {
        // Create the location request
        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(UPDATE_INTERVAL)
                .setFastestInterval(FASTEST_INTERVAL);
        // Request location updates
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient,
                mLocationRequest, this);
        Log.d("reque", "--->>>>");
    }

    @Override
    public void onLocationChanged(Location location) {
        latitude = location.getLatitude();
        longitude =location.getLongitude();
        String msg = "Updated Location: " +
                Double.toString(location.getLatitude()) + "," +
                Double.toString(location.getLongitude());
        Toast.makeText(getApplicationContext(),msg,Toast.LENGTH_SHORT).show();
        tampil_masjid(latitude,longitude);

        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
//        Toast.makeText(getApplicationContext(),String.valueOf(latLng.latitude),Toast.LENGTH_SHORT).show();
//        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
        // You can now create a LatLng Object for use with maps
    }

    private boolean lokasi() {
        if(!isLocationEnabled())
            showAlert();
//        tampil(7.349565,112.6867376);
        return isLocationEnabled();

    }

    private void tampil(final double lat,final double lng) {
        itemList.clear();
        adapter.notifyDataSetChanged();


        JsonArrayRequest jArr = new JsonArrayRequest(url + lat + "&lng=" + lng,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.e(TAG, response.toString());

                        // Parsing json
                        for (int i = 0; i < response.length(); i++) {
                            try {

                                JSONObject obj = response.getJSONObject(i);
                                Jarak j = new Jarak();
                                double jarak = Double.parseDouble(obj.getString("jarak"));

                                j.setJarak("" + round(jarak, 2));

                                itemList.add(j);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }

                        // memberitahu adapter jika ada perubahan data
                        adapter.notifyDataSetChanged();

                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.e(TAG, "Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

        // menambah permintaan ke queue
        AppController.getInstance().addToRequestQueue(jArr);
    }

    public static double round ( double value, int places){
        if (places < 0) throw new IllegalArgumentException();

        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }


    private void showAlert() {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Enable Location")
                .setMessage("Your Locations Settings is set to 'Off'.\nPlease Enable Location to " +
                        "use this app")
                .setPositiveButton("Location Settings", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {

                        Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivity(myIntent);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {

                    }
                });
        dialog.show();
    }

    private boolean isLocationEnabled() {
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    private boolean hasSmsPermission() {
        return EasyPermissions.hasPermissions(this, Manifest.permission.READ_SMS);
    }

    private boolean hasStoragePermission() {
        return EasyPermissions.hasPermissions(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
    }


    @AfterPermissionGranted(RC_LOCATION_PERM)
    public void locationAndContactsTask() {
            // Ask for both permissions
            EasyPermissions.requestPermissions(
                    this,
                    getString(R.string.rationale_location_contacts),
                    RC_LOCATION_PERM,
                    LOCATION);
        }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // EasyPermissions handles the request result.
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {
        Log.d(TAG, "onPermissionsGranted:" + requestCode + ":" + perms.size());
    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
        Log.d(TAG, "onPermissionsDenied:" + requestCode + ":" + perms.size());

        // (Optional) Check whether the user denied any permissions and checked "NEVER ASK AGAIN."
        // This will display a dialog directing them to enable the permission in app settings.
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            new AppSettingsDialog.Builder(this).build().show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == AppSettingsDialog.DEFAULT_SETTINGS_REQ_CODE) {
            String yes = getString(R.string.yes);
            String no = getString(R.string.no);

            // Do something after user returned from app settings screen, like showing a Toast
        }
    }

    @Override
    public void onRationaleAccepted(int requestCode) {
        Log.d(TAG, "onRationaleAccepted:" + requestCode);
    }

    @Override
    public void onRationaleDenied(int requestCode) {
        Log.d(TAG, "onRationaleDenied:" + requestCode);
    }

    @Override
    public void onRefresh() {
        lokasi();
    }
    @Override
    public void onBackPressed() {
        Intent i = new Intent(getApplicationContext(), DonaturActivity.class);
        startActivity(i);
        finish();
    }
}
