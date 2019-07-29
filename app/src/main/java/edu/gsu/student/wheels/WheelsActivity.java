package edu.gsu.student.wheels;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class WheelsActivity extends AppCompatActivity {

    private SensorManager mSensorManager;
    private float mAccel; // acceleration apart from gravity
    private float mAccelCurrent; // current acceleration including gravity
    private float mAccelLast; // last acceleration including gravity

    private final SensorEventListener mSensorListener = new SensorEventListener() {

        public void onSensorChanged(SensorEvent se) {
            float x = se.values[0];
            float y = se.values[1];
            float z = se.values[2];
            mAccelLast = mAccelCurrent;
            mAccelCurrent = (float) Math.sqrt((double) (x*x + y*y + z*z));
            float delta = mAccelCurrent - mAccelLast;
            mAccel = mAccel * 0.9f + delta; // perform low-cut filter

            if ( mAccel > 1 ) {
                wheels.clear();
                initRecyclerView();
            }
        }

        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(mSensorListener, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        mSensorManager.unregisterListener(mSensorListener);
        super.onPause();
    }

    AutoCompleteTextView search;
    public static String data = "";
    String[] dropdown = new String[3];

    private ArrayList<Wheel> wheels = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wheels);

        this.init( this );

        /* do this in onCreate */
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mSensorManager.registerListener(mSensorListener, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
        mAccel = 0.00f;
        mAccelCurrent = SensorManager.GRAVITY_EARTH;
        mAccelLast = SensorManager.GRAVITY_EARTH;
    }

    private void init(final Context context) {
        this.search = findViewById( R.id.wheels_search_by_size );
        dropdown[0] = "Enter search: i.e. 20 5x115";

        // Autofill feature
        ArrayAdapter<String> adapter = new ArrayAdapter<String>( context, android.R.layout.simple_list_item_1, dropdown );
        this.search.setAdapter( adapter );

        this.search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                wheels.clear();

                if ( charSequence.length() < 8 ) {
                    return;
                }

                // Get the JSON data in the background
                // Get rid of spaces. PHP will remove the underscores and replace them with spaces again
                FetchJsonData process = new FetchJsonData();
                process.execute( charSequence.toString().replace(" ", "_"), "wheels", "wheels_dropdown" );

                // Parse JSON String
                try {
                    JSONObject root     = new JSONObject(data);
                    JSONArray vehicles  = root.getJSONArray("wheels");

                    for ( int k = 0; k < vehicles.length(); k++ ) {

                        JSONObject wh = vehicles.getJSONObject( k );

                        Wheel wheel = new Wheel();
                        wheel.setBolt_pattern( wh.getString("bolt_pattern") );
                        wheel.setBolts(        wh.getString("bolts") );
                        wheel.setBrand(        wh.getString("brand") );
                        wheel.setImage(        wh.getString("image") );
                        wheel.setItem_number(  wh.getString("item_number") );
                        wheel.setModel(        wh.getString("model") );
                        wheel.setPrice(        wh.getString("price") );
                        wheel.setDiameter(     wh.getString("diameter") );
                        wheel.setWidth(        wh.getString("width") );
                        wheel.setOffset(       wh.getString("offset") );

                        wheels.add( wheel );


                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                closeKeyboard();

                initRecyclerView();
            }
        });
    }

    private void initRecyclerView(){
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        WheelRecyclerViewAdapter adapter = new WheelRecyclerViewAdapter(this, wheels);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    /**
     * Creates the menu from res/menu/options
     *
     * @param menu - menu bar
     * @return boolean
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate( R.menu.options, menu );

        return super.onCreateOptionsMenu(menu);
    }

    /**
     * Allows for the user to switch to other activities
     *
     * @param item - menu item
     * @return - boolean
     */
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch( item.getItemId() ) {

            case R.id.menu_find_wheels_icon:
                // Do nothing. On this activity now.
                break;
            case R.id.menu_vehicle_details:
                startActivity( new Intent( WheelsActivity.this, VehicleDetailsActivity.class ) );
                break;
            case R.id.menu_cart_icon:
                startActivity( new Intent( WheelsActivity.this, CartActivity.class ) );
                break;
            case R.id.menu_help:
                startActivity( new Intent( WheelsActivity.this, HelpActivity.class ) );
                break;
            case R.id.menu_settings:
                startActivity( new Intent( WheelsActivity.this, SettingsActivity.class ) );
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void closeKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}
