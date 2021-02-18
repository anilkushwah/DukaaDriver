package com.dollop.dukaadriver.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dollop.dukaadriver.R;
import com.dollop.dukaadriver.UtilityTools.SessionManager;
import com.dollop.dukaadriver.firebase.MyFirebaseMessagingService;

public class WelcomeActivity extends AppCompatActivity {


    ImageView[] dots;
    int pos;
    ViewPager vpimgs;
    private int[] layouts;

    TextView tv_skip, doneid;
    ImageView clear_all;
    private MyViewPagerAdapter myViewPagerAdapter;
    SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        setContentView(R.layout.activity_welcome);

        sessionManager = new SessionManager(this);

        if (sessionManager.is_WELCOME_SCREEN()) {
            startActivity(new Intent(WelcomeActivity.this, LoginActivity.class));
            finish();
        }


        layouts = new int[]{
                R.layout.fragment_welcome_one,
                R.layout.fragment_welcome_two,
                R.layout.fragment_welcome_three,
        };


        vpimgs = findViewById(R.id.vpimgs);
        doneid = findViewById(R.id.nextid);
        doneid = findViewById(R.id.nextid);

        myViewPagerAdapter = new MyViewPagerAdapter();
        vpimgs.setAdapter(myViewPagerAdapter);
        final int dotsCount = myViewPagerAdapter.getCount();
        dots = setUiPageViewController(/*llindicators,*/ dotsCount);

        MyFirebaseMessagingService.GenerateToken(WelcomeActivity.this);
        doneid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // checking for last page
                // if last page home screen will be launched
                int current = getItem(+1);
                if (current < layouts.length) {
                    // move to next screen
                    pos++;
                    vpimgs.setCurrentItem(current);
                } else {
                    launchHomeScreen();
                }
            }
        });


        final ImageView[] finalDots = dots;
        vpimgs.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(final int position) {

                pos = position;

               /* if (finalDots != null) {
                    for (int i = 0; i < dotsCount; i++) {
                        finalDots[i].setImageDrawable(WelcomeActivity.this.getResources().getDrawable(R.drawable.nonselecteditem_dot));
                    }
                    finalDots[pos].setImageDrawable(WelcomeActivity.this.getResources().getDrawable(R.drawable.selecteditem_dot));
                }
*/


                if (pos == 1) {
                    doneid.setText("Done");
//                    clear_all.setVisibility(View.VISIBLE);
                    doneid.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            sessionManager.set_WELCOME_SCREEN(true);
                            startActivity(new Intent(WelcomeActivity.this, LoginActivity.class));
                            finish();
                        }
                    });
                } else {
                    doneid.setText("Next");
                    doneid.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            int current = getItem(+1);
                            if (current < layouts.length) {
                                pos++;
                                vpimgs.setCurrentItem(current);
                            } else {
                                launchHomeScreen();
                            }
                        }
                    });
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


    }

    private int getItem(int i) {
        return vpimgs.getCurrentItem() + i;
    }


    public class MyViewPagerAdapter extends PagerAdapter {
        private LayoutInflater layoutInflater;

        public MyViewPagerAdapter() {
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View view = layoutInflater.inflate(layouts[position], container, false);
            container.addView(view);

            return view;
        }

        @Override
        public int getCount() {
            return layouts.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object obj) {
            return view == obj;
        }


        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            View view = (View) object;
            container.removeView(view);
        }
    }

    private ImageView[] setUiPageViewController(int dotsCount) {

        ImageView[] dots = new ImageView[dotsCount];
//        pager_indicator.removeAllViews();
        for (int i = 0; i < dotsCount; i++) {
            dots[i] = new ImageView(WelcomeActivity.this);
//            dots[i].setImageDrawable(WelcomeActivity.this.getResources().getDrawable(R.drawable.nonselecteditem_dot));

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );

            params.setMargins(4, 0, 4, 0);
//            pager_indicator.addView(dots[i], params);
        }

//        dots[0].setImageDrawable(WelcomeActivity.this.getResources().getDrawable(R.drawable.selecteditem_dot));

        return dots;
    }


    private void launchHomeScreen() {
//        prefManager.setFirstTimeLaunch(false);

        sessionManager.set_WELCOME_SCREEN(true);
        startActivity(new Intent(WelcomeActivity.this, LoginActivity.class));
        finish();
    }


}



