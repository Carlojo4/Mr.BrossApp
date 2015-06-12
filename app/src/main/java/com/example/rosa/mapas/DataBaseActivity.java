package com.example.rosa.mapas;

import android.content.Intent;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.SQLException;
import java.util.TooManyListenersException;


public class DataBaseActivity extends ActionBarActivity implements View.OnClickListener {
    DataBaseManager Manager = MainActivity.getManager();
    private Cursor cursor;
    private ListView lista;
    private SimpleCursorAdapter adapter;
    private EditText Edsede;
    //private Button btnbuscar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_base);

        //Manager = new DataBaseManager(this);
        lista = (ListView) findViewById(android.R.id.list);
        Edsede = (EditText) findViewById(R.id.Edbsede);

        //String text = Manager.CN_LAT + " " + Manager.CN_LON;
        String[] from = new String[]{Manager.CN_SEDE,Manager.CN_LAT};
        int[] to = new int[]{android.R.id.text1,android.R.id.text2};
        cursor = Manager.cargarCursorContactos();
        adapter = new SimpleCursorAdapter(this,android.R.layout.two_line_list_item,cursor,from,to,0);
        lista.setAdapter(adapter);

        //Manager.insertar("Alejo","5822128");
        //Manager.insertar("Pablo","2651752");
        //Manager.insertar("Paula","4910413");
        Button btnbuscar = (Button) findViewById(R.id.btn1);
        btnbuscar.setOnClickListener(this);
        Button btncargar = (Button) findViewById(R.id.btndb);
        btncargar.setOnClickListener(this);
        Button btninsertar = (Button) findViewById(R.id.btninsertar);
        btninsertar.setOnClickListener(this);
        Button btneliminar = (Button) findViewById(R.id.btneliminar);
        btneliminar.setOnClickListener(this);
        Button btnactualizar = (Button) findViewById(R.id.btnactualizar);
        btnactualizar.setOnClickListener(this);
        Button btnset = (Button) findViewById(R.id.btnset);
        btnset.setOnClickListener(this);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_data_base, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_volver) {
            Intent i = new Intent(this,MainActivity.class);
            startActivity(i);
        }
        else if (id == R.id.action_locations) {
            Intent i = new Intent(this,MapActivity.class);
            startActivity(i);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        if (v.getId()==R.id.btn1){
            new BuscarTask().execute();
        }
        if(v.getId()==R.id.btndb){
            lista = (ListView) findViewById(android.R.id.list);
            Edsede = (EditText) findViewById(R.id.Edbsede);

            //String texto = Manager.CN_LAT + " " + Manager.CN_LON;
            String[] from = new String[]{Manager.CN_SEDE,Manager.CN_LAT};
            int[] to = new int[]{android.R.id.text1,android.R.id.text2};
            cursor = Manager.cargarCursorContactos();
            adapter = new SimpleCursorAdapter(this,android.R.layout.two_line_list_item,cursor,from,to,0);
            lista.setAdapter(adapter);

        }
        if (v.getId()==R.id.btninsertar){
            EditText sede = (EditText) findViewById(R.id.Edsede);
            EditText lat = (EditText) findViewById(R.id.EdTelefono);
            EditText lon = (EditText) findViewById(R.id.EdTelefono1);
            Manager.insertar(sede.getText().toString(),lat.getText().toString(),lon.getText().toString());
            sede.setText("");
            lat.setText("");
            lon.setText("");
            Toast.makeText(getApplicationContext(),"Insertado", Toast.LENGTH_SHORT).show();
        }
        if(v.getId()==R.id.btneliminar){
            EditText nombre = (EditText) findViewById(R.id.Edsede);
            Manager.eliminar(nombre.getText().toString());
            Toast.makeText(getApplicationContext(),"Eliminado", Toast.LENGTH_SHORT).show();
            nombre.setText("");
        }
        if (v.getId()==R.id.btnactualizar){
            EditText sede = (EditText) findViewById(R.id.Edsede);
            EditText lat = (EditText) findViewById(R.id.EdTelefono);
            EditText lon = (EditText) findViewById(R.id.EdTelefono1);
            Manager.Modificar(sede.getText().toString(),lat.getText().toString(),lon.getText().toString());
            Toast.makeText(getApplicationContext(),"Actualizado", Toast.LENGTH_SHORT).show();
            sede.setText("");
            lat.setText("");
            lon.setText("");
        }
        if (v.getId()==R.id.btnset){
            Manager.insertar("Envigado", "6.171746", "-75.585076");
            Manager.insertar("Poblado", "6.209251", "-75.566898");
            Manager.insertar("Centro", "6.246631", "-75.572448");
            Manager.insertar("La 33", "6.239640", "-75.580865");
            Manager.insertar("La 80", "6.261381", "-75.597193");
        }
    }

    private class BuscarTask extends AsyncTask<Void, Void, Void>{

        @Override
        protected void onPreExecute() {
            Toast.makeText(getApplicationContext(),"Buscando...", Toast.LENGTH_SHORT).show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            cursor = Manager.buscarContacto(Edsede.getText().toString());
            return null;
        }



        @Override
        protected void onPostExecute(Void aVoid) {
            Toast.makeText(getApplicationContext(),"Finalizado", Toast.LENGTH_SHORT).show();
            adapter.changeCursor(cursor);
            obtener();
        }
    }

    public void obtener () {
        TextView Txnombre = (TextView) findViewById(R.id.Txnombre);
        TextView Txtelefono = (TextView) findViewById(R.id.Txtelefono);
        TextView Txtelefono1 = (TextView) findViewById(R.id.Txtelefono1);
        try{
            String dbnombre = cursor.getString(cursor.getColumnIndex(Manager.CN_SEDE));
            Txnombre.setText(dbnombre);
            String dbtelefono = cursor.getString(cursor.getColumnIndex(Manager.CN_LAT));
            Txtelefono.setText(dbtelefono);
            String lon = cursor.getString(cursor.getColumnIndex(Manager.CN_LON));
            Txtelefono1.setText(lon);}
        catch(CursorIndexOutOfBoundsException e){
            Txnombre.setText("No Found");
            Txtelefono.setText("No Found");
        }

    }
}