package com.dollop.dukaadriver.adapter;

import android.content.Context;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.dollop.dukaadriver.R;
import com.dollop.dukaadriver.activity.HomeActivity;
import com.dollop.dukaadriver.model.MyReviewModel;
import com.dollop.dukaadriver.retrofit.ApiClient;
import com.makeramen.roundedimageview.RoundedImageView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class MyReviewAdapter extends RecyclerView.Adapter<MyReviewAdapter.MyViewHolder> {

    Context context;
    ArrayList<MyReviewModel> myReviewModels;

    public MyReviewAdapter(Context context, ArrayList<MyReviewModel> myReviewModels) {
        this.context = context;
        this.myReviewModels = myReviewModels;
    }

    @NonNull
    @Override
    public MyReviewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.my_reviewlist, parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyReviewAdapter.MyViewHolder holder, int position) {

        MyReviewModel myReviewModel = myReviewModels.get(position);


        if (myReviewModel.getRetailerImage() != null) {
            Glide.with(context)
                    .load(ApiClient.BASE_URL + myReviewModel.getRetailerImage())
                    .into(holder.user_Image);
        }

        holder.User_name.setText(myReviewModel.getName());
        holder.order_number.setText("Order ID - #000" + myReviewModel.getOrderId());
        holder.rating.setText(myReviewModel.getRating() + " Rating");
        holder.review.setText(myReviewModel.getReview());
        SimpleDateFormat output = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",
                Locale.ENGLISH);
        Date date = null;
        try {
            date = output.parse(myReviewModel.getCreateId());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        holder.review_time.setText(getFormattedDate(date.getTime()));
    //    holder.review_time.setText(myReviewModel.getCreateId());
        holder.my_rating_ratebar.setRating(Float.parseFloat(myReviewModel.getRating()));
        holder.my_rating_ratebar.setIsIndicator(true);

    }
    public String getFormattedDate(long smsTimeInMilis) {
        Calendar smsTime = Calendar.getInstance();
        smsTime.setTimeInMillis(smsTimeInMilis);

        Calendar now = Calendar.getInstance();

        final String timeFormatString = "h:mm aa";
        final String dateTimeFormatString = "EEEE, MMMM d, h:mm aa";
        final long HOURS = 60 * 60 * 60;
        if (now.get(Calendar.DATE) == smsTime.get(Calendar.DATE)) {
            return "Today " + DateFormat.format(timeFormatString, smsTime);
        } else if (now.get(Calendar.DATE) - smsTime.get(Calendar.DATE) == 1) {
            return "Yesterday " + DateFormat.format(timeFormatString, smsTime);
        } else if (now.get(Calendar.YEAR) == smsTime.get(Calendar.YEAR)) {
            return DateFormat.format(dateTimeFormatString, smsTime).toString();
        } else {
            return DateFormat.format("MMMM dd yyyy, h:mm aa", smsTime).toString();
        }
    }

    @Override
    public int getItemCount() {
        return myReviewModels.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        RoundedImageView user_Image;
        TextView User_name, order_number, review, rating, review_time;
        RatingBar my_rating_ratebar;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            user_Image = itemView.findViewById(R.id.user_profile_pic_riv);
            User_name = itemView.findViewById(R.id.user_name_tv);
            order_number = itemView.findViewById(R.id.userorder_id);
            rating = itemView.findViewById(R.id.rating_by_user);
            review = itemView.findViewById(R.id.review_by_user_tv);
            review_time = itemView.findViewById(R.id.review_time_tv);
            my_rating_ratebar = itemView.findViewById(R.id.my_rating_ratebar);


        }
    }
}
