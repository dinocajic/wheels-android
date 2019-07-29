package edu.gsu.student.wheels;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "wheels.db";
    private static final int VERSION = 1;

    private static final class CartTable {
        private static final String TABLE            = "cart";
        private static final String COL_ID           = "id";
        private static final String COL_ITEM_NUMBER  = "item_number";
        private static final String COL_BRAND        = "brand";
        private static final String COL_MODEL        = "model";
        private static final String COL_DIAMETER     = "diameter";
        private static final String COL_WIDTH        = "width";
        private static final String COL_BOLTS        = "bolts";
        private static final String COL_BOLT_PATTERN = "bolt_pattern";
        private static final String COL_OFFSET       = "wheel_offset";
        private static final String COL_PRICE        = "price";
        private static final String COL_IMAGE        = "image";
    }

    DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);

        // To be able to see the database in Device Helper/data/database
        SQLiteDatabase db = this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        this.createCartTable(sqLiteDatabase);
    }

    void createCartTable(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(
                "create table " + CartTable.TABLE  + " (" +
                        CartTable.COL_ID           + " integer primary key autoincrement, " +
                        CartTable.COL_ITEM_NUMBER  + " text, " +
                        CartTable.COL_BRAND        + " text, " +
                        CartTable.COL_MODEL        + " text, " +
                        CartTable.COL_DIAMETER     + " text, " +
                        CartTable.COL_WIDTH        + " text, " +
                        CartTable.COL_BOLTS        + " text, " +
                        CartTable.COL_BOLT_PATTERN + " text, " +
                        CartTable.COL_OFFSET       + " text, " +
                        CartTable.COL_PRICE        + " text, " +
                        CartTable.COL_IMAGE        + " text "  +
                        ")"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + CartTable.TABLE);
    }

    /**
     *
     * @param wheel
     */
    void insert(Wheel wheel) {
        // If item number already exists, don't insert it again
        if ( get_item( wheel.getItem_number() ) ) {
            return;
        }

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues cartValues = new ContentValues();
        cartValues.put( CartTable.COL_ITEM_NUMBER,  wheel.getItem_number()  );
        cartValues.put( CartTable.COL_BRAND,        wheel.getBrand()        );
        cartValues.put( CartTable.COL_MODEL,        wheel.getModel()        );
        cartValues.put( CartTable.COL_DIAMETER,     wheel.getDiameter()     );
        cartValues.put( CartTable.COL_WIDTH,        wheel.getWidth()        );
        cartValues.put( CartTable.COL_BOLTS,        wheel.getBolts()        );
        cartValues.put( CartTable.COL_BOLT_PATTERN, wheel.getBolt_pattern() );
        cartValues.put( CartTable.COL_OFFSET,       wheel.getOffset()       );
        cartValues.put( CartTable.COL_PRICE,        wheel.getPrice()        );
        cartValues.put( CartTable.COL_IMAGE,        wheel.getImage()        );

        long cartId = db.insert( CartTable.TABLE, null, cartValues );

        Log.e("Cart ID", cartId + "");
    }

    /**
     *
     * @return
     */
    Wheel[] get_cart_contents() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor queryCursor = db.rawQuery("SELECT * FROM " + CartTable.TABLE + ";", null);

        Wheel[] wheels = new Wheel[queryCursor.getCount()];
        int i = 0;

        queryCursor.moveToFirst();
        while (!queryCursor.isAfterLast()) {
            Wheel wheel = new Wheel();
            wheel.setItem_number(  queryCursor.getString( queryCursor.getColumnIndex( CartTable.COL_ITEM_NUMBER ) ) );
            wheel.setBrand(        queryCursor.getString( queryCursor.getColumnIndex( CartTable.COL_BRAND ) ) );
            wheel.setModel(        queryCursor.getString( queryCursor.getColumnIndex( CartTable.COL_MODEL ) ) );
            wheel.setDiameter(     queryCursor.getString( queryCursor.getColumnIndex( CartTable.COL_DIAMETER ) ) );
            wheel.setWidth(        queryCursor.getString( queryCursor.getColumnIndex( CartTable.COL_WIDTH ) ) );
            wheel.setBolts(        queryCursor.getString( queryCursor.getColumnIndex( CartTable.COL_BOLTS ) ) );
            wheel.setBolt_pattern( queryCursor.getString( queryCursor.getColumnIndex( CartTable.COL_BOLT_PATTERN ) ) );
            wheel.setOffset(       queryCursor.getString( queryCursor.getColumnIndex( CartTable.COL_OFFSET ) ) );
            wheel.setImage(        queryCursor.getString( queryCursor.getColumnIndex( CartTable.COL_IMAGE ) ) );
            wheel.setPrice(        queryCursor.getString( queryCursor.getColumnIndex( CartTable.COL_PRICE ) ) );

            wheels[i] = wheel;
            i++;
            queryCursor.moveToNext();
        }
        queryCursor.close();

        return wheels;
    }

    /**
     *
     * @param item_number
     * @return
     */
    private boolean get_item( String item_number ) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor queryCursor = db.rawQuery("SELECT * FROM " + CartTable.TABLE + " WHERE " +
                CartTable.COL_ITEM_NUMBER + " = '" + item_number + "';", null);

        return queryCursor.getCount() != 0;
    }

    public void delete(String part_number) {
        SQLiteDatabase db = this.getWritableDatabase();

        db.execSQL("DELETE FROM " + CartTable.TABLE + " WHERE " + CartTable.COL_ITEM_NUMBER + " = '" + part_number + "';");
    }
}