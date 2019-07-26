package edu.gsu.student.wheels;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class FetchJsonData extends AsyncTask<String, Void, Void> {

    // The retrieved JSON file
    private StringBuilder data = new StringBuilder();

    private String class_name = "";

    /**
     * Background thread
     *
     * @param strings
     * @return
     */
    @Override
    protected Void doInBackground(String... strings) {

        this.class_name = strings[2];

        String url_string = "";

        switch( strings[1] ) {
            case "vehicle":
                url_string = "http://10.0.2.2:8888/school/android_wheels/index.php/welcome/get_vehicle_info/" + strings[0];
                break;
            case "wheels":
                url_string = "http://10.0.2.2:8888/school/android_wheels/index.php/welcome/get_wheels/" + strings[0];
                break;
        }

        try {

            Log.e("URL", url_string);

            URL url = new URL( url_string );

            // This is the connection
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

            // Get the stream that allows us to read/write data
            InputStream inputStream = httpURLConnection.getInputStream();

            // Read the data from the stream
            BufferedReader bufferedReader = new BufferedReader( new InputStreamReader( inputStream ));

            String line = "";

            while ( line != null ) {
                line = bufferedReader.readLine();
                data.append(line);
            }


        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


        return null;
    }

    /**
     * Change UI in this method
     *
     * @param aVoid
     */
    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);

        switch( this.class_name ) {
            case "settings":
                SettingsActivity.data = this.data.toString();
                break;
            case "bolt_pattern_dropdown":
                VehicleDetailsActivity.data = this.data.toString();
                break;
            case "bolt_pattern_details":
                VehicleDetailsActivity.details = this.data.toString();
                break;
        }
    }
}
