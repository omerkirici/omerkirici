package com.aljazarisoft.apps.luckyegg;

import android.Manifest;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.aljazarisoft.apps.luckyegg.lib.HttpAsyncTask;
import com.aljazarisoft.apps.luckyegg.lib.Session;
import com.aljazarisoft.apps.luckyegg.lib.Url;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.concurrent.ExecutionException;

public class MapsActivity extends AppCompatActivity
        implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {
    Dialog myDialog;
    private String Data=null;
    GoogleMap mGoogleMap;
    SupportMapFragment mapFrag;
    LocationRequest mLocationRequest;
    GoogleApiClient mGoogleApiClient;
    Location mLastLocation;
    LocationManager mLocationManager;
    Marker mCurrLocationMarker;
    private double  Latitude;
    private double  Longitude;
    private String Gourl=null;
    private String dropped=null;
    private String openit=null;
    private  boolean firstLocation=true;
    TextView loaddmap=null;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        Button drop = (Button)findViewById(R.id.dropeggBTN);
        ImageView fresh = (ImageView)findViewById(R.id.refleshLocation);
        loaddmap=(TextView)findViewById(R.id.LoadMap);
        loaddmap.setText("Loading...");
        fresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firstLocation=true;
            }
        });
        drop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                myDialog = new Dialog(MapsActivity.this);
                myDialog.setContentView(R.layout.activity_main_form_drop);
                myDialog.setCancelable(false);
                ImageView login = (ImageView) myDialog.findViewById(R.id.closedialog);
                Button dropit=(Button)myDialog.findViewById(R.id.buttondrp);
                final EditText message=(EditText)myDialog.findViewById(R.id.message_text);
                myDialog.show();

                login.setOnClickListener(new View.OnClickListener()
                {

                    @Override
                    public void onClick(View v)
                    {
                        myDialog.dismiss();
                    }
                });
                dropit.setOnClickListener(new View.OnClickListener()
                {

                    @Override
                    public void onClick(View v)
                    {
                        HashMap<String,String> map= new HashMap<String, String>();
                        map.put("Lat",""+Latitude);
                        map.put("Lon",""+Longitude);
                        map.put("message",message.getText().toString());
                        HttpAsyncTask task= new HttpAsyncTask(MapsActivity.this,map);
                        Gourl= new Url().Url("AddItem");

                        try {
                            dropped= task.execute(Gourl).get();

                            if(dropped.equals("")){}
                            else{

                                JSONArray jsonObj = new JSONArray(dropped);
                                for(int i=0;i<jsonObj.length();i++){
                                    JSONObject json_data = jsonObj.getJSONObject(i);
                                    if(json_data.has("RESULT")){
                                        String query_result = json_data.getString("RESULT");
                                        if (query_result.equals("SUCCESS")) {
                                            myDialog.dismiss();
                                            onMapReady(mGoogleMap);
                                            Toast.makeText(getApplicationContext(), "Your message dropped successfuly", Toast.LENGTH_SHORT).show();
                                        }else if (query_result.equals("FAILURE")) {
                                            String reason = json_data.getString("REASON");
                                            if(reason.equals("exist"))
                                                Toast.makeText(getApplicationContext(), "there is another egg here !", Toast.LENGTH_SHORT).show();
                                            else
                                                Toast.makeText(getApplicationContext(), "Sorry we could not do it", Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                }
                            }
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        } catch (ExecutionException e) {
                            e.printStackTrace();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });

            }
        });

        mapFrag = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFrag.getMapAsync(this);


    }

    @Override
    public void onPause() {
        super.onPause();

        //stop location updates when Activity is no longer active
        if (mGoogleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap)
    {
        firstLocation=true;
        mGoogleMap=googleMap;
        mGoogleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        //Initialize Google Play Services
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                //Location Permission already granted
                buildGoogleApiClient();
                mGoogleMap.setMyLocationEnabled(true);
            } else {
                //Request Location Permission
                checkLocationPermission();
            }
        }
        else {
            buildGoogleApiClient();
            mGoogleMap.setMyLocationEnabled(true);
        }


        mGoogleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(final Marker marker) {
                LatLng latLng = marker.getPosition();
                myDialog = new Dialog(MapsActivity.this);
                myDialog.setContentView(R.layout.activity_maps_item_window);
                myDialog.setCancelable(false);
                TextView tvLat = (TextView) myDialog.findViewById(R.id.tv_lat);
                final String s = marker.getSnippet();
                final String key = s;
                // Getting reference to the TextView to set longitude
                TextView tvLng = (TextView) myDialog.findViewById(R.id.tv_lng);

                // Setting the latitude
                tvLat.setText("Latitude:" + latLng.latitude);
                ImageView close = (ImageView)myDialog.findViewById(R.id.close_item_detail);
                Button OPENBTN = (Button)myDialog.findViewById(R.id.OPENBTN);
                // Setting the longitude
                tvLng.setText("Longitude:"+ latLng.longitude);
                myDialog.show();
                close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        myDialog.dismiss();
                    }
                });
                OPENBTN.setOnClickListener(new View.OnClickListener()
                {

                    @Override
                    public void onClick(View v)
                    {

                        HashMap<String,String> map = new HashMap<String, String>();
                        map.put("Lat",""+Latitude);
                        map.put("Lon",""+Longitude);
                        map.put("Id",""+key);
                        HttpAsyncTask task = new HttpAsyncTask(getApplicationContext(),map);
                        Gourl=new Url().Url("ItemFound");

                        try {
                            openit=task.execute(Gourl).get();

                            if(!openit.equals("")){

                                JSONArray jsonObj = new JSONArray(openit);
                                for(int i=0;i<jsonObj.length();i++){
                                    JSONObject json_data = jsonObj.getJSONObject(i);
                                    if(json_data.has("Item_User") && json_data.has("Item_Message")){


                                        myDialog.dismiss();
                                        myDialog = new Dialog(MapsActivity.this);
                                        myDialog.setContentView(R.layout.activity_maps_item_found);
                                        myDialog.setCancelable(false);
                                        TextView title = (TextView) myDialog.findViewById(R.id.textview_item_found_title);
                                        TextView user = (TextView) myDialog.findViewById(R.id.textview_item_found_user);
                                        TextView message = (TextView) myDialog.findViewById(R.id.textview_item_found_message);
                                        Button delete = (Button)myDialog.findViewById(R.id.deleteegg);
                                        delete.setText("Delete");
                                        Session session=new Session(MapsActivity.this);
                                        if(json_data.getString("Item_User").equals(session.get("nick")))
                                            delete.setVisibility(View.VISIBLE);
                                        else
                                            delete.setVisibility(View.INVISIBLE);
                                        // Getting reference to the TextView to set longitude
                                        title.setText("Wohoooooooo !");
                                        user.setText("FROM : "+json_data.getString("Item_User"));
                                        message.setText("MESSAGE: " +json_data.getString("Item_Message"));
                                        final String mUser= json_data.getString("Item_User");
                                        ImageView close =(ImageView)myDialog.findViewById(R.id.closedialog);
                                        myDialog.show();

                                        close.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                Session session=new Session(MapsActivity.this);
                                                if(mUser.equals(session.get("nick")))
                                                {

                                                }else{

                                                    FindEggs();
                                                }

                                                myDialog.dismiss();

                                            }
                                        });

                                    }else  if(json_data.has("RESULT")){
                                        String query_result = json_data.getString("RESULT");
                                        if (query_result.equals("FAILURE")) {
                                            String reason = json_data.getString("REASON");
                                            if(reason.equals("far"))
                                                Toast.makeText(getApplicationContext(), "it is too far you must be closed the egg", Toast.LENGTH_LONG).show();
                                            else
                                                Toast.makeText(getApplicationContext(), "there are some errors and i dont know why ?", Toast.LENGTH_LONG).show();
                                        }
                                    }



                                }
                            }else{

                            }
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        } catch (ExecutionException e) {
                            e.printStackTrace();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });

                return false;
            }
        });



    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnected(Bundle bundle) {

        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(9000);
        mLocationRequest.setFastestInterval(9000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);



        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }




    }

    @Override
    public void onConnectionSuspended(int i) {
        Toast.makeText(getApplicationContext(), "We couldn't get your current location :/", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Toast.makeText(getApplicationContext(), "We couldn't get your current location Xonnection is failed :/", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onLocationChanged(Location location)
    {

        mLastLocation = location;
        if (mCurrLocationMarker != null) {
            mCurrLocationMarker.remove();
        }

        //Place current location marker
        this.Latitude=location.getLatitude();
        this.Longitude=location.getLongitude();
        loaddmap.setVisibility(View.INVISIBLE);

        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());


        if(this.firstLocation){
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(latLng);
            markerOptions.title("Current Position");
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));


            mCurrLocationMarker = mGoogleMap.addMarker(markerOptions);
            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(latLng)
                    .zoom(20)

                    .build();



            mGoogleMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
            //move map camera
            //  mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,11));
            firstLocation=false;
        }


        FindEggs();
    }


    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    private void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                new AlertDialog.Builder(this)
                        .setTitle("Location Permission Needed")
                        .setMessage("This app needs the Location permission, please accept to use location functionality")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Prompt the user once explanation has been shown
                                ActivityCompat.requestPermissions(MapsActivity.this,
                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                        MY_PERMISSIONS_REQUEST_LOCATION );
                            }
                        })
                        .create()
                        .show();


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION );
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // location-related task you need to do.
                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                        if (mGoogleApiClient == null) {
                            buildGoogleApiClient();
                        }
                        mGoogleMap.setMyLocationEnabled(true);
                    }

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(this, "permission denied", Toast.LENGTH_LONG).show();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }
    public Bitmap resizeMapIcons(String iconName, int width, int height){
        Bitmap imageBitmap = BitmapFactory.decodeResource(getResources(),getResources().getIdentifier(iconName, "drawable", getPackageName()));
        Bitmap resizedBitmap = Bitmap.createScaledBitmap(imageBitmap, width, height, false);
        return resizedBitmap;
    }
    protected void createMarkers( ){


        try {


            JSONArray jsonObj = new JSONArray(this.Data);

            for(int i=0;i<jsonObj.length();i++){
                JSONObject json_data = jsonObj.getJSONObject(i);
                if(json_data.has("Item_Lat") && json_data.has("Item_Lon") && json_data.has("Item_Id")){
                    Location loc1 = new Location("");
                    loc1.setLatitude(Latitude);
                    loc1.setLongitude(Longitude);

                    Location loc2 = new Location("");
                    loc2.setLatitude(json_data.getDouble("Item_Lat"));
                    loc2.setLongitude(json_data.getDouble("Item_Lon"));

                    float distanceInMeters = loc1.distanceTo(loc2);
                    double lt= Double.parseDouble(json_data.getString("Item_Lat"));
                    double ln =Double.parseDouble(json_data.getString("Item_Lon"));

                    createMarker(lt,ln, "hello"+"_"+i, ""+json_data.getDouble("Item_Id"), 1);
                }


            }



        } catch (JSONException e) {
            e.printStackTrace();

        }


    }
    protected Marker createMarker(double latitude, double longitude, String title, String snippet, int iconResID) {

        return  mGoogleMap.addMarker(new MarkerOptions()
                .position(new LatLng(latitude, longitude))
                .anchor(0.5f, 0.5f)
                .title(title)
                .snippet(snippet)

                .icon(BitmapDescriptorFactory.fromBitmap(resizeMapIcons("pin1",70,70))));



    }
    protected  void FindEggs(){
        HashMap<String,String> map = new HashMap<>();

        map.put("lat",""+Latitude);
        map.put("lon",""+Longitude);
        HttpAsyncTask task = new HttpAsyncTask(MapsActivity.this,map);
        try {
            Gourl=new Url().Url("Item");
            this.Data=task.execute(Gourl).get();
            if(this.Data.equals("")){

            }else{
                mGoogleMap.clear();
                createMarkers();
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if ((keyCode == KeyEvent.KEYCODE_BACK))
        {
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }
}