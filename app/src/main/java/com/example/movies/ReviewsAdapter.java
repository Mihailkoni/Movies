package com.example.movies;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class ReviewsAdapter extends RecyclerView.Adapter<ReviewsAdapter.ReviewViewHolder> {

    private List<Review> reviews = new ArrayList<>();

    private static final String TYPE_POSITIVE = "Позитивный";
    private static final String TYPE_NEUTRAL = "Нейтральный";
    private static final String TYPE_NEGATIVE = "Негативный";

    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ReviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.review_item,
                parent,
                false
        );
        return new ReviewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewViewHolder holder, int position) {
        Review review = reviews.get(position);

        holder.textViewReviewAuthor.append(" " + review.getAuthor());

        holder.textViewReview.setText(review.getReview());

        String type = review.getType();
        holder.textViewReviewType.append(" " + type);
        int colorId;
        switch (type){
            case TYPE_POSITIVE:
                colorId = android.R.color.holo_green_light;
                break;
            case TYPE_NEUTRAL:
                colorId = android.R.color.holo_orange_light;
                break;
            case TYPE_NEGATIVE:
                colorId = android.R.color.holo_red_light;
                break;
            default:
                colorId = android.R.color.holo_blue_light;
        }
        int color = ContextCompat.getColor(holder.itemView.getContext(),colorId);
        holder.constraintLayoutReviewItem.setBackgroundColor(color);
    }

    @Override
    public int getItemCount() {
        return reviews.size();
    }

    static class ReviewViewHolder extends RecyclerView.ViewHolder {

        private final TextView textViewReviewAuthor;
        private final TextView textViewReviewType;
        private final TextView textViewReview;
        private final ConstraintLayout constraintLayoutReviewItem;

        public ReviewViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewReviewAuthor = itemView.findViewById(R.id.textViewReviewAuthor);
            textViewReviewType = itemView.findViewById(R.id.textViewReviewType);
            textViewReview = itemView.findViewById(R.id.textViewReview);
            constraintLayoutReviewItem = itemView.findViewById(R.id.constraintLayoutReviewItem);
        }
    }
}
