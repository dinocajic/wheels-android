package edu.gsu.student.wheels;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class SettingsActivity extends AppCompatActivity {

    EditText name;
    AutoCompleteTextView vehicle;
    Button save_button;

    SharedPreferences prefs;

    //public static ArrayList<String> vehicle_data = new ArrayList<>();
    String[] vehicle_data = new String[3];

    // Data from JSON
    public static String data = "";

    Context cntx;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        vehicle_data[0] = "Enter Vehicle";

        cntx = this;
        this.init(this);
    }

    private void init(final Context context ) {
        this.name        = findViewById( R.id.settings_name );
        this.vehicle     = findViewById( R.id.settings_search_vehicle );
        this.save_button = findViewById( R.id.settings_save_button );

        prefs = PreferenceManager.getDefaultSharedPreferences(this);

        if ( prefs.contains("user_name") ) {
            this.name.setText( prefs.getString("user_name", "user_name") );
        }

        if ( prefs.contains("user_vehicle") ) {
            this.vehicle.setText( prefs.getString("user_vehicle", "user_vehicle") );
        }

        // Autofill feature
        ArrayAdapter<String> adapter = new ArrayAdapter<String>( context, android.R.layout.simple_list_item_1, vehicle_data );
        this.vehicle.setAdapter( adapter );

        this.vehicle.addTextChangedListener(new TextWatcher() {
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
                process.execute( charSequence.toString().replace(" ", "_"), "vehicle" );

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
                        vehicle.setThreshold(1);
                        vehicle.setAdapter(adp);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        this.save_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor editor = prefs.edit();

                editor.putString("user_name",    name.getText().toString() );
                editor.putString("user_vehicle", vehicle.getText().toString() );

                editor.apply();

                closeKeyboard();

                Toast.makeText( getApplicationContext(),
                        "Preferences Saved",
                        Toast.LENGTH_SHORT ).show();
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
                startActivity( new Intent( SettingsActivity.this, WheelsActivity.class ) );
                break;
            case R.id.menu_find_bolt_pattern:
                startActivity( new Intent( SettingsActivity.this, BoltPatternActivity.class ) );
                break;
            case R.id.menu_cart_icon:
                startActivity( new Intent( SettingsActivity.this, CartActivity.class ) );
                break;
            case R.id.menu_help:
                startActivity( new Intent( SettingsActivity.this, HelpActivity.class ) );
                break;
            case R.id.menu_settings:
                // Do nothing. On this activity now.
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
