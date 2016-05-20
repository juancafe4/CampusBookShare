package com.polybookshare.master.campusbookshare;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class KlassRecentActivity extends AppCompatActivity {
    private KlassAdapter m_klass_adapter;
    private ArrayList<Klass> m_klasses;
    private ListView m_klass_recent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_klass_recent);

        m_klass_recent = (ListView) findViewById(R.id.recent_klasses);

        m_klasses = new ArrayList<Klass>();
        this.m_klass_adapter = new KlassAdapter(m_klasses, this);

        m_klass_recent.setAdapter(this.m_klass_adapter);
        getRecentKlasses();
    }

    public void  getRecentKlasses(){
        String url = "http://192.168.43.162:8080/api/recent/klass";

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

                                        JSONObject jsonObject = response.getJSONObject(i);
                                        Log.d("works", (String) jsonObject.get("teacher"));
                                        JSONArray array = (JSONArray) response.getJSONObject(i).get("reviews");

                                        String messsage = "";

                                        int reviews = array.length();
                                        if (array.length() != 0) {
                                            JSONObject msg = (JSONObject) array.get(0);
                                            Log.d("msg", (String) msg.get("message"));
                                            messsage = (String) msg.get("message");
                                        }

                                        else {
                                            messsage ="Be the first commeting in this class";
                                        }

                                        JSONArray cm = (JSONArray) response.getJSONObject(i).get("comments");
                                        int commnets = cm.length();

                                        JSONArray fl = (JSONArray) response.getJSONObject(i).get("files");
                                        int files = fl.length();

                                        Klass kl = new Klass(Integer.parseInt((String) jsonObject.get("number")),
                                                (String) jsonObject.get("department"),
                                                (String) jsonObject.get("teacher"),
                                                messsage,
                                                commnets, files, reviews);

                                        m_klasses.add(kl);

                                    }

                                    Log.d("finished", "finished");

                                    m_klass_adapter.notifyDataSetChanged();

                                }
                                catch(JSONException e) {

                                }
                            }
                        }, new com.android.volley.Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("eror getting json array", error.getMessage());

                        //pDialog.dismiss();

                    }
                });

        queue.add(req);
    }
}
