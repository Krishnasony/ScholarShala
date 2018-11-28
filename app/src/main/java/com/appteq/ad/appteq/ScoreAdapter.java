package com.appteq.ad.appteq;

import android.content.Context;
import android.graphics.PorterDuff;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.appteq.ad.appteq.model.ScoreModel;

import java.util.List;

public class ScoreAdapter extends RecyclerView.Adapter<ScoreAdapter.ScoreViewHolder>{
    //this context we will use to inflate the layout
    private Context mCtx;
    //we are storing all the products in a list
    private List<ScoreModel> scorelist;

    //getting the context and product list with constructor
    public ScoreAdapter(Context mCtx, List<ScoreModel> scorelist) {
        this.mCtx = mCtx;
        this.scorelist = scorelist;
    }

    @Override
    public ScoreAdapter.ScoreViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //inflating and returning our view holder
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.score_cardview, parent, false);
        ScoreAdapter.ScoreViewHolder pvh = new ScoreAdapter.ScoreViewHolder(v);
        return pvh;
    }
    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public void onBindViewHolder(ScoreAdapter.ScoreViewHolder holder, int position) {
        //getting the product of the specified position
        final ScoreModel score = scorelist.get(position);
        //binding the data with the viewholder views
        holder.textViewTitle.setText(score.getTestname());
        holder.textViewscore.setText(score.getScore()+"%");
        holder.progressBar.getProgressDrawable().setColorFilter(mCtx.getResources().getColor(R.color.appcolor3), PorterDuff.Mode.SRC_IN);
        holder.progressBar.setProgress(score.getScore());
    }


    @Override
    public int getItemCount() {
        return scorelist.size();
    }


    class ScoreViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        TextView textViewTitle, textViewscore;
        ProgressBar progressBar;

        public ScoreViewHolder(View itemView) {
            super(itemView);
            cv = (CardView) itemView.findViewById(R.id.subjectcard);
            textViewTitle = itemView.findViewById(R.id.title);
            textViewscore = itemView.findViewById(R.id.score);
            progressBar = itemView.findViewById(R.id.progressbar);
        }
    }
}
