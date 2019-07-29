package edu.gsu.student.wheels;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class VehicleDetailsActivity extends AppCompatActivity {

    AutoCompleteTextView search_vehicle;
    Button search;
    TextView results;

    //public static ArrayList<String> vehicle_data = new ArrayList<>();
    String[] vehicle_data = new String[3];

    // Data from JSON
    public static String data    = "";
    public static String details = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehicle_details);

        this.init( this );
    }

    private void init( final Context context ) {
        this.search_vehicle = findViewById( R.id.bolt_pattern_search_vehicle );
        this.search         = findViewById( R.id.bolt_pattern_search_button );
        this.results        = findViewById( R.id.bolt_pattern_results );

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);

        if ( prefs.contains("user_vehicle") ) {
            this.search_vehicle.setText( prefs.getString("user_vehicle", "user_vehicle") );
        }

        // Autofill feature
        ArrayAdapter<String> adapter = new ArrayAdapter<String>( context, android.R.layout.simple_list_item_1, vehicle_data );
        this.search_vehicle.setAdapter( adapter );

        this.search_vehicle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if ( charSequence.length() == 0 ) {
                    return;
                }

                // Get the JSON data in the background
                // Get rid of spaces. PHP will remove the underscores and replace them with spaces again
                FetchJsonData process = new FetchJsonData();
                process.execute( charSequence.toString().replace(" ", "_"), "vehicle", "bolt_pattern_dropdown" );

                // Parse JSON String
                try {
                    JSONObject root     = new JSONObject(data);
                    JSONArray vehicles  = root.getJSONArray("vehicles");

                    for ( int k = 0; k < vehicles.length(); k++ ) {

                        JSONObject car = vehicles.getJSONObject( k );

                        vehicle_data[k] = car.getString("vehicle_description");

                        // Needed so that the autocompletetextview can be refreshed on runtime
                        ArrayAdapter<String> adp = new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, vehicle_data);
                        adp.setDropDownViewResource( android.R.layout.simple_list_item_1 );
                        search_vehicle.setThreshold(1);
                        search_vehicle.setAdapter(adp);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });


        this.search.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View view) {
                // Get the JSON data in the background
                // Get rid of spaces. PHP will remove the underscores and replace them with spaces again
                FetchJsonData process = new FetchJsonData();
                process.execute( search_vehicle.getText().toString().replace(" ", "_"), "vehicle", "bolt_pattern_details" );

                // Parse JSON String
                try {
                    if ( details.equals("") ) {
                        return;
                    }

                    JSONObject root     = new JSONObject( details );
                    JSONArray vehicles  = root.getJSONArray("vehicles");

                    JSONObject car = vehicles.getJSONObject( 0 );

                    results.setText(
                            "Bolt Pattern: " + car.getString("bolts") + "x" + car.getString("bolt_pattern") + "\n" +
                                    "Offset Range: " + car.getString("offset") + "\n" +
                                    "Hub: " + car.getString("hub") + "\n\n" +
                                    "Stock Tire Size: " + car.getString("tire_size")
                    );
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                closeKeyboard();
            }
        });
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
                startActivity( new Intent( VehicleDetailsActivity.this, WheelsActivity.class ) );
                break;
            case R.id.menu_vehicle_details:
                // Do nothing. On this activity now.
                break;
            case R.id.menu_cart_icon:
                startActivity( new Intent( VehicleDetailsActivity.this, CartActivity.class ) );
                break;
            case R.id.menu_help:
                startActivity( new Intent( VehicleDetailsActivity.this, HelpActivity.class ) );
                break;
            case R.id.menu_settings:
                startActivity( new Intent( VehicleDetailsActivity.this, SettingsActivity.class ) );
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

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putString( "results", results.getText().toString() );
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        results.setText( savedInstanceState.getString("results") );
    }
}
