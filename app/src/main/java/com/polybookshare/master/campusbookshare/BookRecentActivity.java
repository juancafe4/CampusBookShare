package com.polybookshare.master.campusbookshare;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class BookRecentActivity extends AppCompatActivity {
    protected String cookie = "";
    private BookAdapter m_book_adapter;
    private ArrayList<Book> m_books;
    private RecyclerView m_book_recent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_recent);
        m_books = new ArrayList<Book>();
        m_book_recent = (RecyclerView) findViewById(R.id.recent_books);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        m_book_recent.setLayoutManager(layoutManager);
        this.m_book_adapter = new BookAdapter(m_books);

        m_book_recent.setAdapter(this.m_book_adapter);
        cookie =  getIntent().getStringExtra("cookie");
        Log.d("recent books activity", "recent here");
        getRecentBooks();
    }

    public void getRecentBooks() {
        String url = "http://192.168.43.162:8080/api/book/search/recent";


        RequestQueue queue = Volley.newRequestQueue(this);
        JsonArrayRequest req = new JsonArrayRequest(url,
                new com.android.volley.Response.Listener<JSONArray>() {

                    @Override
                    public void onResponse(JSONArray response) {


                        try {
                            Log.d ("error ", String.valueOf(response.length()));
                            for(int i = 0; i < response.length(); i++) {
                                //int number, String dep, String professor
                                // String lastReview, int comments, int files, int reviews)

                                JSONObject obj = (JSONObject) response.get(i);
                                String id = (String) obj.get("_id");
                                JSONObject info = (JSONObject) obj.get("googleInfo");
                                JSONObject volume = (JSONObject) info.get("volumeInfo");
                                JSONObject imageLinks = (JSONObject) volume.get("imageLinks");
                                String smallThumbnail = (String) imageLinks.get("smallThumbnail");

                                JSONArray authors = (JSONArray) volume.get("authors");
                                String fullAuthors = "";
                                String title = (String) volume.get("title");
                                String notes = "";


                                if (obj.has("notes"))
                                    notes = (String) obj.get("notes");
                                else
                                    notes = "No description";
                                String condition = (String) obj.get("condition");
                                for (int j = 0; j < authors.length(); j++) {
                                    fullAuthors += authors.get(j) + " ";
                                }
                                //String author, String title, String condition, String description,
                                //  String cover_url);
                                m_books.add(new Book (id, fullAuthors, title, condition, notes, String.valueOf(obj.get("price")), smallThumbnail));
                            }

                            Log.d("finished", "finished");

                            m_book_adapter.notifyDataSetChanged();

                        }
                        catch(JSONException e) {

                        }
                    }
                }, new com.android.volley.Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("error64", error.getMessage());

                //pDialog.dismiss();

            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> headers = new HashMap<String, String>();
                headers.put("Cookie", cookie);
                return headers;
            }
        };

        queue.add(req);
    }
}
