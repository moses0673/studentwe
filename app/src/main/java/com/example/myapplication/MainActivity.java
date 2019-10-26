package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    // These two need to be declared outside the try/catch
// so that they can be closed in the finally block.
    HttpURLConnection urlConnection = null;
    BufferedReader reader = null;

    // Will contain the raw JSON response as a string.
    String data = null;

    ProgressBar progressBar;
    private ImageView ImageView1,ImageView2,ImageView3;
    private TextView textViewName1,textViewName2,textViewName3, textViewStudentID1, textViewStudentID2, textViewStudentID3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findviews();
        new JSONRequest().execute();
    }


    private void findviews(){
        progressBar = findViewById(R.id.progressBar);
        ImageView1 = findViewById(R.id.ImageView1);
        textViewName1 =findViewById(R.id.textViewName1);
        textViewStudentID1 =findViewById(R.id.textViewStudentID1);
        ImageView2 = findViewById(R.id.ImageView2);
        textViewName2 =findViewById(R.id.textViewName2);
        textViewStudentID2 =findViewById(R.id.textViewStudentID2);
        ImageView3 = findViewById(R.id.ImageView3);
        textViewName3 =findViewById(R.id.textViewName3);
        textViewStudentID3 =findViewById(R.id.textViewStudentID3);



    }



    private class JSONRequest extends AsyncTask<Void , Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);


        }


        @Override
        protected Void doInBackground(Void... voids) {
            try {
                // Construct the URL for the OpenWeatherMap query
                // Possible parameters are avaiable at OWM's forecast API page, at
                // http://openweathermap.org/API#forecast
                URL url = new URL("http://api.myjson.com/bins/zjv68");

                // Create the request to OpenWeatherMap, and open the connection
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    // Nothing to do.
                    data = null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                    // But it does make debugging a *lot* easier if you print out the completed
                    // buffer for debugging.
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    data = null;
                }
                data = buffer.toString();
            } catch (IOException e) {
                Log.e("PlaceholderFragment", "Error ", e);
                // If the code didn't successfully get the weather data, there's no point in attemping
                // to parse it.
                data = null;
            } finally{
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e("PlaceholderFragment", "Error closing stream", e);
                    }
                }
            }
            return null;
        }


        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressBar.setVisibility(View.GONE);
            //Read JSON
            try {
                JSONObject returneData = new JSONObject(data);
                JSONArray studentArray = returneData.getJSONArray("students");

                JSONObject student1 = studentArray.getJSONObject(0);
                String student1Name=student1.getString("studentName") ;
                String student1Id=student1.getString("studentID") ;
                String student1Photo=student1.getString("studentPhoto") ;
                textViewName1.setText(student1Name);
                textViewStudentID1.setText(student1Id);
                Picasso.get().load(student1Photo).into(ImageView1);


                JSONObject student2 = studentArray.getJSONObject(1);
                String student2Name=student2.getString("studentName") ;
                String student2Id=student2.getString("studentID") ;
                String student2Photo=student2.getString("studentPhoto") ;
                textViewName2.setText(student2Name);
                textViewStudentID2.setText(student2Id);
                Picasso.get().load(student2Photo).into(ImageView2);


                JSONObject student3 = studentArray.getJSONObject(2);
                String student3Name=student3.getString("studentName") ;
                String student3Id=student3.getString("studentID") ;
                String student3Photo=student3.getString("studentPhoto") ;
                textViewName3.setText(student3Name);
                textViewStudentID3.setText(student3Id);
                Picasso.get().load(student3Photo).into(ImageView3);



            }catch (JSONException e){
                e.printStackTrace();
            }


        }


    }
}
