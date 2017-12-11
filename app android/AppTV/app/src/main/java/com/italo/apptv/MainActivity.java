package com.italo.apptv;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private GridLayoutManager gridLayoutManager;
    private CustomAdapter adapter;
    private List<Thumbnail> thumbnailList;
    private int lastItem = 0;

    // Create and initiate the resources for the main activity
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recycler_view);
        thumbnailList = new ArrayList<>();

        // Avoid loading all the content every time that onStop is called
        if (savedInstanceState != null) {
            ThumbnailSerializable ts = (ThumbnailSerializable)savedInstanceState
                    .getSerializable(ThumbnailSerializable.KEY);

            thumbnailList = ts.thumbnailList;
        }
        else {
            loadDataFromServer(0);
        }

        gridLayoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(gridLayoutManager);

        adapter = new CustomAdapter(this, thumbnailList);
        recyclerView.setAdapter(adapter);

    }

    // Handle the scroll action of the RecyclerView loading the information that the CardView uses
    @Override
    protected void onResume() {
        super.onResume();

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);

            if (gridLayoutManager.findLastCompletelyVisibleItemPosition() == thumbnailList.size()-1
                    && gridLayoutManager.findLastCompletelyVisibleItemPosition() != lastItem) {
                lastItem = gridLayoutManager.findLastCompletelyVisibleItemPosition();
                loadDataFromServer(thumbnailList.get(thumbnailList.size() - 1).getId());
            }
            }
        });
    }


    // Save the activity state when onStop is called
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putSerializable(ThumbnailSerializable.KEY, new ThumbnailSerializable(thumbnailList));
    }


    // Send a request to the spark server and gets an json array as response
    // This json array contains the image url, name and id of the show
    private void loadDataFromServer(final int id) {
        @SuppressLint("StaticFieldLeak") AsyncTask<Integer, Void, Void> task = new AsyncTask<Integer, Void, Void>() {
            @Override
            protected Void doInBackground(Integer... integers) {
                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder()
                        .url("http://192.168.15.5:4567/showthumbnails/" + id)
                        .build();
                try {
                    Response response = client.newCall(request).execute();
                    JSONArray array = new JSONArray(response.body().string());

                    for (int i = 0; i < array.length(); i++) {
                        JSONObject object = array.getJSONObject(i);

                        Thumbnail data = new Thumbnail(object.getInt("id"),
                                object.getString("name"), object.getString("imageUrl"));
                        thumbnailList.add(data);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                adapter.notifyDataSetChanged();
            }
        };

        task.execute(id);
    }
}
