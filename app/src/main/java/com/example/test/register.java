package com.example.test;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
public class register extends Fragment {
    private AppBarLayout appBar;
    private TabLayout tabs;
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        View view = inflater.inflate(R.layout.register, container, false);
        getActivity().setTitle("REGISTER");
        //assert container != null;
        //View contenedor = (View)container.getParent();
        appBar = view.findViewById(R.id.appbar);

        tabs = new TabLayout(getActivity());
        tabs.setTabTextColors(Color.parseColor("#000000"), Color.parseColor("#000000"));
        appBar.addView(tabs);

        ViewPager viewPager = view.findViewById(R.id.pager);
        ViewPagerAdapter pagerAdapter = new ViewPagerAdapter(getFragmentManager());
        viewPager.setAdapter(pagerAdapter);
        tabs.setupWithViewPager(viewPager);
        tabs.setTabMode(TabLayout.MODE_SCROLLABLE);
        return view;
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        appBar.removeView(tabs);
    }
    public class ViewPagerAdapter extends FragmentStatePagerAdapter {
        ViewPagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }
        String[] tabnames = {"REGISTER AS PARENT", "REGISTER AS DOCTOR"};
        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0: return new parent();
                case 1: return new doctor();
            }
            return null;
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return tabnames[position];
        }
    }
}




