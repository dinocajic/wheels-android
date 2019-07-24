package edu.gsu.student.wheels;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

public class HelpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);
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
                startActivity( new Intent( HelpActivity.this, WheelsActivity.class ) );
                break;
            case R.id.menu_find_bolt_pattern:
                startActivity( new Intent( HelpActivity.this, BoltPatternActivity.class ) );
                break;
            case R.id.menu_cart_icon:
                startActivity( new Intent( HelpActivity.this, CartActivity.class ) );
                break;
            case R.id.menu_help:
                // Do nothing. On this activity now.
                break;
            case R.id.menu_settings:
                startActivity( new Intent( HelpActivity.this, SettingsActivity.class ) );
                break;
            default:
                Log.e("Activity", "Default case accessed in onOptionsItemSelected()");
        }

        return super.onOptionsItemSelected(item);
    }
}
