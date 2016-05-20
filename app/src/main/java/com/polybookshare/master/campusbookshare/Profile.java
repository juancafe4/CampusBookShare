package com.polybookshare.master.campusbookshare;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.media.Image;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.Request;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Profile extends Fragment {
    protected static final String Cookie = "COOKIE_SAVE";
    private String cookie = "";
    private String picture_url = "";
    protected TextView email;
    protected TextView phone;
    protected TextView userName;
    protected TextView info;
    protected TextView upVotes;
    protected TextView virals;
    protected User user;
    protected ImageButton logout;
    protected ImageButton edit_profile;
    private ImageView m_profilePicture;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        this.cookie = getArguments().getString("cookies");

        final View profile = inflater.inflate(R.layout.activity_profile, container, false);
        HomeTab home = (HomeTab) getActivity();
        user = home.getUser();

        if(user != null) {
            initLayout(profile);
            getImage(profile);
            initProfile();


            logout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getActivity().finish();
                    SharedPreferences prefs = getActivity().getSharedPreferences(Cookie, Context.MODE_PRIVATE);
                    prefs.edit().remove("Cookie").apply();
                    Intent i = new Intent(getActivity().getApplicationContext(), MainActivity.class);
                    i.putExtra("logout", true);
                    startActivity(i);
                }
            });

        }
        return profile;
    }

    public void initLayout(View view) {
        email = (TextView) view.findViewById(R.id.email);
        phone = (TextView) view.findViewById(R.id.phoneNumber);
        userName = (TextView) view.findViewById(R.id.names);
        info = (TextView) view.findViewById(R.id.info);
        upVotes = (TextView) view.findViewById(R.id.upVotes);
        virals = (TextView) view.findViewById(R.id.virals);
        logout = (ImageButton) view.findViewById(R.id.logoutbtn);
    }

    public void initProfile() {
        email.setText(user.getEmail());
        userName.setText(user.getName());
        phone.setText(user.getPhone());
        info.setText(user.getBio());
        upVotes.setText(user.getVotes());
        String temp [] = user.getVirals();

        String vr = "Give these access codes to your friends so they can join!";
        for (int i = 0; i < temp.length; i++)
            vr += "\n" + temp[i];
        virals.setText(vr);

    }

    private void getImage(View view) {
        Log.d("url pic", picture_url);
        m_profilePicture = (ImageView) view.findViewById(R.id.profile_picture);
        Picasso.with(getActivity())
                .load(user.getPicture_url())
                .resize(350, 350)
                .centerCrop()
                .into(m_profilePicture);
    }
}

