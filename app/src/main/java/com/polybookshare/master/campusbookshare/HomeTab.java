package com.polybookshare.master.campusbookshare;

import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentActivity;

import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class HomeTab extends FragmentActivity{

    protected String cookie = "";
    protected User user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        cookie =  getIntent().getStringExtra("cookie");
        Log.d("home tab", "home tab is being created");
        getProfile();

        super.onCreate(savedInstanceState);

    }

    public void getProfile() {
        String url = "http://192.168.43.162:8080/auth/me";


        RequestQueue queue = Volley.newRequestQueue(this);
        JsonObjectRequest req = new JsonObjectRequest(Request.Method.GET, url , null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            String id = (String) response.get("_id");
                            String picture_url = (String) response.get("picture_url");
                            String email = (String) response.get("email");
                            String school = (String) response.get("school");
                            JSONArray codes = (JSONArray) response.get("virals");

                            String virals [] = new String[3];
                            for (int i = 0; i < codes.length(); i++)
                                 virals[i] = "" + codes.get(i);

                            String name = "";
                            String phone ="";
                            if(response.has("name"))
                                name = (String) response.get("name");
                            if(response.has("phone")) {
                                phone = (String) response.get("phone");
                            }

                            String votes = String.valueOf(response.get("votes"));
                            String bio = (String) response.get("bio");

                            user = new User(id, name, email, phone, school,
                                    votes, bio, virals, picture_url);
                            Log.d("calling this...", "I am here");
                            initTabs();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                if (error  != null && error.getMessage() != null) {
                    Log.d("error125", error.getMessage());
                }
                if (error  != null && error.networkResponse != null) {
                    /*AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
                    alertDialog.setTitle("HTTP 401");
                    alertDialog.setMessage("Una");
                    alertDialog.show();*/
                }
                if (error.networkResponse == null) {
                    Log.d("throw error in profile", error.getClass().toString());
                    if (error.getClass().equals(NoConnectionError.class)
                || error.getClass().equals(TimeoutError.class)) {
                        // Show timeout error message
                        Toast.makeText(getBaseContext(),
                                "Sorry Cannot Connect this time",
                                Toast.LENGTH_LONG).show();
                    }
                }
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> headers = new HashMap<String, String>();
                headers.put("Cookie", cookie);
                return headers;
            }
        };


        req.setRetryPolicy(new DefaultRetryPolicy(1000, 5,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        queue.add(req);
    }

    public User getUser() {
        return this.user;
    }

    public void initTabs() {
        setContentView(R.layout.activity_home_tab);


        View home = getLayoutInflater().inflate(R.layout.home, null);
        View classes = getLayoutInflater().inflate(R.layout.classes, null);
        View bookshelf = getLayoutInflater().inflate(R.layout.bookshelf, null);
        View info = getLayoutInflater().inflate(R.layout.info, null);
        View profile = getLayoutInflater().inflate(R.layout.profile, null);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);

        tabLayout.addTab(tabLayout.newTab().setCustomView(home));
        tabLayout.addTab(tabLayout.newTab().setCustomView(bookshelf));
        tabLayout.addTab(tabLayout.newTab().setCustomView(classes));
        tabLayout.addTab(tabLayout.newTab().setCustomView(profile));
        tabLayout.addTab(tabLayout.newTab().setCustomView(info));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        final ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        final PagerAdapter adapter = new PagerAdapter
                (getSupportFragmentManager(), tabLayout.getTabCount(), cookie);
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        viewPager.setCurrentItem(0, false);
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }
}
