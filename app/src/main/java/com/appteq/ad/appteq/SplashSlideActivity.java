package com.appteq.ad.appteq;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class SplashSlideActivity extends AppCompatActivity {
    private ViewPager viewPager;
    private TextView[] dots;
    private LinearLayout dotsLayout;
    private int[] layout;
    Button buttonnext,buttonskip;
    private ViewPagerAdapter viewPagerAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_slide);
        viewPager = findViewById(R.id.viewpager);
        buttonnext = findViewById(R.id.button_next);
        buttonskip = findViewById(R.id.button_skip);
        dotsLayout = findViewById(R.id.dotlayout);
        layout = new int[]{R.layout.layout_screen1,R.layout.layout_screen2,R.layout.layout_screen3};
        addBottomdots(0);
        changeStatusBarColor();


        viewPagerAdapter= new ViewPagerAdapter();
        viewPager.setAdapter(viewPagerAdapter);
        viewPager.addOnPageChangeListener(onPageChangeListener);

        buttonskip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SplashSlideActivity.this,LoginActivity.class));
                finish();
            }
        });

        buttonnext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int currentitem = getItem(+1);
                if(currentitem<layout.length){
                    viewPager.setCurrentItem(currentitem);

                }
                else {
                    startActivity(new Intent(SplashSlideActivity.this,LoginActivity.class));
                        finish();
                }
            }
        });

    }

    private void addBottomdots(int position){
        dots = new TextView[layout.length];
        int[] colorActive = getResources().getIntArray(R.array.dot_active);
        int[] colorInActive = getResources().getIntArray(R.array.dot_inactive);
        dotsLayout.removeAllViews();
        for (int i=0;i<dots.length;i++){
            dots[i] = new TextView(this);
            dots[i].setText(Html.fromHtml("&#8226;"));
            dots[i].setTextColor(colorInActive[position]);
            dots[i].setTextSize(35);
            dotsLayout.addView(dots[i]);
        }
        if(dots.length>0){
            dots[position].setTextColor(colorActive[position]);
        }
    }

    private int getItem(int i){
        return viewPager.getCurrentItem()+i;
    }

    ViewPager.OnPageChangeListener onPageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            addBottomdots(position);
            if(position==layout.length-1){
                buttonnext.setText("PROCEED");
                buttonskip.setVisibility(View.GONE);

            }
            else {
                buttonnext.setText("NEXT");
                buttonskip.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };


    private void changeStatusBarColor(){
        if (Build.VERSION.SDK_INT>= Build.VERSION_CODES.LOLLIPOP){
            Window window = getWindow();
            // clear FLAG_TRANSLUCENT_STATUS flag:
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(ContextCompat.getColor(SplashSlideActivity.this,R.color.colorPrimaryDark));

        }
    }

    public class ViewPagerAdapter extends PagerAdapter {
        private LayoutInflater layoutInflater;

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            layoutInflater = (LayoutInflater) getSystemService(Context. LAYOUT_INFLATER_SERVICE);
//            assert layoutInflater != null;
            View view = layoutInflater.inflate(layout[position],container,false);
            container.addView(view);

            return view;
        }

        @Override
        public int getCount() {
            return layout.length;
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view==object;
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            View view = (View) object;
            container.removeView(view);
        }
    }

}
