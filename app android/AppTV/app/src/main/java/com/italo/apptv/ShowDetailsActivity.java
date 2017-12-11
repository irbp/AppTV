package com.italo.apptv;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


// This activity shows some informations of a tv show
public class ShowDetailsActivity extends AppCompatActivity {

    private ImageView image;
    private TextView name;
    private TextView sinopse;
    private TextView rating;
    private TextView status;

    private Show showDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_details);

        int id = getIntent().getExtras().getInt("id");

        class Local{};

        Log.i("Script", this.getClass().getName() + "."
                + Local.class.getEnclosingMethod().getName());

        showDetails = new Show();

        image = findViewById(R.id.image_details);
        name = findViewById(R.id.name_details);
        sinopse = findViewById(R.id.sinopse_details);
        rating = findViewById(R.id.rating_details);
        status = findViewById(R.id.status_details);

        if (savedInstanceState != null) {
            ShowSerializable s = (ShowSerializable)savedInstanceState.getSerializable(ShowSerializable.KEY);

            showDetails = s.show;
            name.setText(showDetails.getName());
            sinopse.setText(showDetails.getSinopse());
            rating.setText(showDetails.getRating());
            status.setText(showDetails.getStatus());
        }
        else {
            OkHttpHandler okHttpHandler = new OkHttpHandler();
            okHttpHandler.execute(id);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putSerializable(ShowSerializable.KEY, new ShowSerializable(showDetails));
    }

    public class OkHttpHandler extends AsyncTask {
        OkHttpClient client = new OkHttpClient();

        @Override
        protected Void doInBackground(Object[] params) {
            Request.Builder builder = new Request.Builder();
            builder.url("http://192.168.15.5:4567/shows/" + (Integer)params[0]);
            Request request = builder.build();

            try {
                Response response = client.newCall(request).execute();
                JSONObject jsonObject = new JSONObject(response.body().string());

                showDetails.setName(jsonObject.getString("name"));
                showDetails.setSinopse(jsonObject.getString("summary"));
                showDetails.setImageUrl(jsonObject.getJSONObject("image")
                    .getString("medium"));
                showDetails.setRating(jsonObject.getJSONObject("rating").getString("average"));
                showDetails.setStatus(jsonObject.getString("status"));
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            Glide.with(ShowDetailsActivity.this).load(showDetails.getImageUrl()).into(image);
            name.setText(showDetails.getName());
            sinopse.setText(showDetails.getSinopse());
            rating.setText(showDetails.getRating());
            status.setText(showDetails.getStatus());
        }
    }
}
