package edu.gsu.student.wheels;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class WheelRecyclerViewAdapter extends RecyclerView.Adapter<WheelRecyclerViewAdapter.ViewHolder>{

    private ArrayList<Wheel> wheels;
    private Context         context;

    public WheelRecyclerViewAdapter(Context context, ArrayList<Wheel> wheels ) {
        this.wheels  = wheels;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_list_wheel, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        holder.description.setText( wheels.get(position).getBrand() + " " + wheels.get(position).getModel() );
        holder.itemNumber.setText( wheels.get(position).getItem_number() );
        holder.size.setText(
                        wheels.get(position).getDiameter() + "x" + wheels.get(position).getWidth() + " " +
                        wheels.get(position).getBolts() + "x" + wheels.get(position).getBolt_pattern() + " " +
                        ((Integer.parseInt( wheels.get(position).getOffset() ) > 0) ? "+" : "") + wheels.get(position).getOffset()
                );
        holder.price.setText( "$" + wheels.get(position).getPrice() + " Add to Cart" );

        // Opens the modify page for the particular item that's passed
        holder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(context, EditActivity.class);
//                intent.putExtra("part_number", tires.get(position).getPart_number());
//                context.startActivity(intent);

                Log.e("Clicked it", "Yup");

                Globals.db.insert( wheels.get(position) );
            }
        });



        Picasso.get().load(wheels.get(position).getImage()).into(holder.image);
    }

    @Override
    public int getItemCount() {
        return wheels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView       description;
        TextView       size;
        Button         price;
        TextView       itemNumber;
        ImageView      image;
        RelativeLayout parentLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            description  = itemView.findViewById(R.id.wheel_description);
            itemNumber   = itemView.findViewById(R.id.wheel_part_number);
            size         = itemView.findViewById(R.id.wheel_size);
            image        = itemView.findViewById(R.id.wheel_image);
            price        = itemView.findViewById(R.id.wheel_button);
            parentLayout = itemView.findViewById(R.id.parent_layout);

        }
    }
}
