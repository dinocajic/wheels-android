package edu.gsu.student.wheels;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    Button vehicle_details, find_wheels;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.init();
    }

    private void init() {
        Globals.db = new DatabaseHelper(this);

        this.vehicle_details = findViewById( R.id.main_vehicle_details_button );
        this.find_wheels     = findViewById( R.id.main_search_button );

        this.vehicle_details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity( new Intent( MainActivity.this, VehicleDetailsActivity.class ) );
            }
        });

        this.find_wheels.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity( new Intent( MainActivity.this, WheelsActivity.class ) );
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
                startActivity( new Intent( MainActivity.this, WheelsActivity.class ) );
                break;
            case R.id.menu_vehicle_details:
                startActivity( new Intent( MainActivity.this, VehicleDetailsActivity.class ) );
                break;
            case R.id.menu_cart_icon:
                startActivity( new Intent( MainActivity.this, CartActivity.class ) );
                break;
            case R.id.menu_help:
                startActivity( new Intent( MainActivity.this, HelpActivity.class ) );
                break;
            case R.id.menu_settings:
                startActivity( new Intent( MainActivity.this, SettingsActivity.class ) );
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}
