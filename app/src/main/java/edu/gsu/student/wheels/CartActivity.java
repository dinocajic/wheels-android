package edu.gsu.student.wheels;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;

public class CartActivity extends AppCompatActivity {

    private ArrayList<Wheel> wheels = new ArrayList<>();

    public static TextView subtotal;
    private Button payment_button;
    public static TextView cart_text_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        this.init( this );
    }

    private void init( Context context ) {
        cart_text_view = findViewById(R.id.cart_text_view);
        Wheel[] wheels = Globals.db.get_cart_contents();

        if ( wheels.length == 0 ) {
            cart_text_view.setText(R.string.no_items_in_cart);
            return;
        }

        this.wheels.addAll( Arrays.asList( wheels ) );
        subtotal = findViewById( R.id.subtotal );

        double total = 0;

        for ( Wheel wheel : wheels ) {
            total += Double.parseDouble( wheel.getPrice() ) * 4;
        }

        subtotal.setText( "Subtotal: $" + String.format("%.2f", total) + "");

        initRecyclerView();
    }

    private void initRecyclerView(){
        RecyclerView recyclerView = findViewById(R.id.cart_recycler_view);
        CartRecyclerViewAdapter adapter = new CartRecyclerViewAdapter(this, wheels);
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
                startActivity( new Intent( CartActivity.this, WheelsActivity.class ) );
                break;
            case R.id.menu_vehicle_details:
                startActivity( new Intent( CartActivity.this, VehicleDetailsActivity.class ) );
                break;
            case R.id.menu_cart_icon:
                // Do nothing. On this activity now.
                break;
            case R.id.menu_help:
                startActivity( new Intent( CartActivity.this, HelpActivity.class ) );
                break;
            case R.id.menu_settings:
                startActivity( new Intent( CartActivity.this, SettingsActivity.class ) );
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}
