package com.polybookshare.master.campusbookshare;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;

public class PagerAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;
    protected String cookie;
    public PagerAdapter(FragmentManager fm, int NumOfTabs, String cookie) {
        super(fm);
        this.cookie = cookie;
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {
        Bundle bundle = new Bundle();
        bundle.putString("cookies", this.cookie);
        switch (position) {
            case 0:
                HomeActivity tab1 = new HomeActivity();
                tab1.setArguments(bundle);
                return tab1;
            case 1:
                BookshelfActivity tab2 = new BookshelfActivity();
                tab2.setArguments(bundle);
                return tab2;
            case 2:
                HomeActivity tab3 = new HomeActivity();
                tab3.setArguments(bundle);
                return tab3;
            case 3:
                Profile tab4 = new Profile();
                tab4.setArguments(bundle);
                return tab4;
            case 4:
                HomeActivity tab5 = new HomeActivity();
                tab5.setArguments(bundle);
                return tab5;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 5;
    }
}