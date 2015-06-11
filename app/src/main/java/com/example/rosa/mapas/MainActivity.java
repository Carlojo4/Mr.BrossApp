package com.example.rosa.mapas;

import android.content.Intent;
import android.graphics.Camera;
import android.location.Location;
import android.location.LocationListener;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

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


public class MainActivity extends ActionBarActivity {

    private boolean flag = true;
    private static DataBaseManager Manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Manager = new DataBaseManager(this);
    }

    public void cargar(){
        do{
            Manager.insertar("Envigado", "6.171746", "-75.585076");
            Manager.insertar("Poblado", "6.209251", "-75.566898");
            Manager.insertar("Centro", "6.246631", "-75.572448");
            Manager.insertar("La 33", "6.239640", "-75.580865");
            Manager.insertar("La 80","6.261381", "-75.597193");
            flag=false;
        }while(flag);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
    public static DataBaseManager getManager() {
        return Manager;
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
        else if (id == R.id.action_locations) {
            Intent i = new Intent(this,MapActivity.class);
            startActivity(i);
        }

        return super.onOptionsItemSelected(item);
    }
}
