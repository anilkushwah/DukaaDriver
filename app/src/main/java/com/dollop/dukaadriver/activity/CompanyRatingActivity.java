package com.dollop.dukaadriver.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dollop.dukaadriver.R;
import com.dollop.dukaadriver.UtilityTools.NetworkUtil;
import com.dollop.dukaadriver.UtilityTools.SessionManager;
import com.dollop.dukaadriver.UtilityTools.Utility;
import com.dollop.dukaadriver.UtilityTools.Utils;
import com.dollop.dukaadriver.adapter.MyReviewAdapter;
import com.dollop.dukaadriver.model.AllResponse;
import com.dollop.dukaadriver.model.MyReviewModel;
import com.dollop.dukaadriver.model.RatingDTO;
import com.dollop.dukaadriver.retrofit.ApiClient;
import com.dollop.dukaadriver.retrofit.ApiInterface;

import java.util.ArrayList;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;

public class CompanyRatingActivity extends AppCompatActivity implements View.OnClickListener {

    RecyclerView my_rating_list_rec;
    ImageView back_btn_review, no_data_image;

    MyReviewAdapter myReviewAdapter;
    ArrayList<MyReviewModel> myReviewModels;
    TextView total_rating_tv, total_review_TV;
   LinearLayout rating_LL;
    SessionManager sessionManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_rating);

        sessionManager = new SessionManager(this);

        myReviewModels = new ArrayList<>();
        back_btn_review = findViewById(R.id.back_btn_review);
        my_rating_list_rec = findViewById(R.id.my_rating_list_rec);
        no_data_image = findViewById(R.id.no_data_image);
        total_rating_tv = findViewById(R.id.total_rating_tv);
        total_review_TV = findViewById(R.id.total_review_TV);
        rating_LL = findViewById(R.id.rating_LL);

        back_btn_review.setOnClickListener(this);

        if ( NetworkUtil.isNetworkAvailable(CompanyRatingActivity.this)){
            RatingMethod();
        }else {
            Utility.netConnect(CompanyRatingActivity.this);

        }


    }


    @Override
    public void onClick(View v) {
        if (v == back_btn_review) {
            onBackPressed();
        }
    }


    private void RatingMethod() {
        final Dialog dialog1 = Utils.initProgressDialog(this);

        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);
        HashMap<String, String> hm = new HashMap<>();

        hm.put("driver_id", sessionManager.getRegisterUser().getId());

        Call<RatingDTO> call = apiService.get_driver_rating(hm);
        call.enqueue(new Callback<RatingDTO>() {
            @Override
            public void onResponse(Call<RatingDTO> call, retrofit2.Response<RatingDTO> response) {
                dialog1.dismiss();
                try {

                    RatingDTO body = response.body();

                    if (body.getStatus() == 200) {

                        myReviewModels = body.getRating();
                        if (myReviewModels == null) {
                            no_data_image.setVisibility(View.VISIBLE);
                        }
                        total_rating_tv.setText("" + body.getAvgRating());
                        total_review_TV.setText(body.getTotalRating() + "Ratings " + body.getReview() + "Reviews");


                        myReviewAdapter = new MyReviewAdapter(CompanyRatingActivity.this, myReviewModels);
                        my_rating_list_rec.setLayoutManager(new LinearLayoutManager(CompanyRatingActivity.this, RecyclerView.VERTICAL, false));
                        my_rating_list_rec.setAdapter(myReviewAdapter);
                        myReviewAdapter.notifyDataSetChanged();

                    } else {
                        no_data_image.setVisibility(View.VISIBLE);
                        rating_LL.setVisibility(View.GONE);

                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<RatingDTO> call, Throwable t) {
                call.cancel();
                t.printStackTrace();
                dialog1.dismiss();

            }
        });


    }


}