package com.example.rosa.mapas;

import android.app.Fragment;
import android.content.Intent;
import android.database.Cursor;
import android.location.Location;
import android.location.LocationListener;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;


public class MapActivity extends FragmentActivity implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener{

    public DataBaseManager Manager = MainActivity.getManager();
    //private Cursor cursor = Manager.cargarCursorsedes();
    private GoogleMap map;
    private CameraUpdate cameraUpdate;
    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;
    private final LatLng LOCATION_TOWN = new LatLng(6.250101, -75.566084);

    private Cursor cursor;

    private LocationRequest mLocationRequest;
    private LatLng currentLocation;
    private boolean newLocationReady = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        map = ((MapFragment)getFragmentManager().findFragmentById(R.id.map)).getMap();
        map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        cameraUpdate= CameraUpdateFactory.newLatLngZoom(LOCATION_TOWN,12);
        map.animateCamera(cameraUpdate);
        CargarSedes();
        Toast toast = Toast.makeText(this, "Presione BACK para salir.", Toast.LENGTH_SHORT);
        toast.show();
        buildGoogleApiClient();
        createLocationRequest();
    }

    public void volver(View view){
        Intent i = new Intent(this,MainActivity.class);
        startActivity(i);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_map, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_database) {
            Intent i = new Intent(this,DataBaseActivity.class);
            startActivity(i);
        }
        else if (id == R.id.action_volver) {
            Intent i = new Intent(this,MainActivity.class);
            startActivity(i);
        }

        return super.onOptionsItemSelected(item);
    }
    public void CargarSedes (){

        cursor = Manager.cargarCursorContactos();
        if(cursor.moveToFirst()){
            do {
                String sede = cursor.getString(cursor.getColumnIndex(Manager.CN_SEDE)).toString();
                String lat = cursor.getString(cursor.getColumnIndex(Manager.CN_LAT)).toString();
                String lon = cursor.getString(cursor.getColumnIndex(Manager.CN_LON)).toString();
                float latf = (Float.parseFloat(lat));
                float lonf = (Float.parseFloat(lon));
                final LatLng LOCATION_SUCURSAL = new LatLng(latf,lonf );
                map.addMarker(new MarkerOptions().position(LOCATION_SUCURSAL).title(sede).snippet(lat + " , " + lon)
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.marcador)));
            }while (cursor.moveToNext());
        }else{
            Toast.makeText(getApplicationContext(), "No hay Sedes cargadas! ", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onConnected(Bundle bundle) {
        mLastLocation=LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (mLastLocation!=null){
            setNewLocation(mLastLocation);
            newLocationReady=true;
        }
        else{
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, (com.google.android.gms.location.LocationListener) this);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onLocationChanged(Location location) {
        setNewLocation(location);
        newLocationReady=true;
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

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }
    protected synchronized void buildGoogleApiClient(){
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }
    protected void createLocationRequest(){
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(10000);
        mLocationRequest.setFastestInterval(5000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    private void setNewLocation(Location location){
        double latitude = location.getLatitude();
        double longitude = location.getLongitude();

        currentLocation = new LatLng(latitude,longitude);
        map.addMarker(new MarkerOptions()
                .position(currentLocation)
                .title("You are here!")
                .snippet("Actual")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
    }
    @Override
    public void onPause() {
        super.onPause();
        if(mGoogleApiClient.isConnected()){
            mGoogleApiClient.disconnect();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if(!mGoogleApiClient.isConnected()) {
            mGoogleApiClient.connect();
        }
    }
    public void MY_LOCATION(View view){
        if(newLocationReady){
            map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            cameraUpdate = CameraUpdateFactory.newLatLngZoom(currentLocation,20);
            map.animateCamera(cameraUpdate);
        }
    }
    public void LOCATION_SEDES (View view){
        map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        cameraUpdate= CameraUpdateFactory.newLatLngZoom(LOCATION_TOWN,13);
        map.animateCamera(cameraUpdate);
    }
}