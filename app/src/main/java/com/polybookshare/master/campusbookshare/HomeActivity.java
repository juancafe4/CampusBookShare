package com.polybookshare.master.campusbookshare;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class HomeActivity extends Fragment {
    private ArrayList<Klass> m_klasses;
    private KlassAdapter m_adapter;

    protected TextView init_name;
    protected ImageView init_profile;
    protected Button recenttBooks;
    protected Button recentKlasses;
    protected Button searchBooks;
    protected Button searchKlasses;

    protected User user;

    private String cookie = "";
    @Override
    public void onCreate(Bundle saveOnInstance) {
        super.onCreate(saveOnInstance);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_home, container, false);

        HomeTab home = (HomeTab) getActivity();
        user = home.getUser();

        this.cookie = getArguments().getString("cookies");

        initLayout(view);
        getImage(view);
        init_name.setText("Hello " + user.getName());
        addListenners(view);

        return view;
    }


    public void initLayout(View view) {
        recenttBooks = (Button) view.findViewById(R.id.go_recent_books);
        recentKlasses = (Button) view.findViewById(R.id.go_recent_klasses);
        searchBooks = (Button) view.findViewById(R.id.search_books);
        searchKlasses = (Button) view.findViewById(R.id.search_klasses);
        init_name = (TextView) view.findViewById(R.id.init_name);
    }

    private void getImage(View view) {
        init_profile = (ImageView) view.findViewById(R.id.init_profile);
        Picasso.with(getActivity())
                .load(user.getPicture_url())
                .resize(350, 350)
                .centerCrop()
                .into(init_profile);
    }
    public void addListenners(View view) {
        recenttBooks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("before call", "before recent");
                Intent i = new Intent(getActivity(), BookRecentActivity.class);
                i.putExtra("cookie", cookie);
                startActivity(i);

            }
        });

        recentKlasses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("before call", "before recent");
                Intent i = new Intent(getActivity(), KlassRecentActivity.class);
                i.putExtra("cookie", cookie);
                startActivity(i);
            }
        });
    }

}
