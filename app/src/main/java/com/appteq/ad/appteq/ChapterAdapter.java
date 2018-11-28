package com.appteq.ad.appteq;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.appteq.ad.appteq.model.ChapterModel;

import java.util.List;

public class ChapterAdapter extends RecyclerView.Adapter<ChapterAdapter.ChapterViewHolder>{
    //this context we will use to inflate the layout
    private Context mCtx;
    private Bundle bundle;
    //we are storing all the products in a list
    private List<ChapterModel> chapterlist;

    //getting the context and product list with constructor
    public ChapterAdapter(Context mCtx, List<ChapterModel> chapterlist, Bundle bundle) {
        this.mCtx = mCtx;
        this.chapterlist = chapterlist;
        this.bundle = bundle;
    }

    @Override
    public ChapterAdapter.ChapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //inflating and returning our view holder
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.chapter_cardview, parent, false);
        ChapterAdapter.ChapterViewHolder pvh = new ChapterAdapter.ChapterViewHolder(v);
        return pvh;
    }
    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public void onBindViewHolder(ChapterAdapter.ChapterViewHolder holder, int position) {
        //getting the product of the specified position
        final ChapterModel chapter = chapterlist.get(position);
        holder.textViewTitle.setText(chapter.getChapter_name());
        if(chapter.getTest_id()==0){
            holder.test.setVisibility(View.GONE);
        }
        if(chapter.getChapter_id() ==0){
            holder.score.setVisibility(View.GONE);
        }
        holder.test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent testintent = new Intent(mCtx,InstructionsActivity.class);
                bundle.putString("test_id",chapter.getTest_id()+"");
                /*testintent.putExtra("user_id",bundle.getString("user_id"));
                testintent.putExtra("usertoken",bundle.getString("usertoken"));*/
                testintent.putExtra("userdata",bundle);
                mCtx.startActivity(testintent);
            }
        });
        holder.score.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent testintent = new Intent(mCtx,ScoreActivity.class);
                testintent.putExtra("chapter_id",chapter.getChapter_id());
                mCtx.startActivity(testintent);
            }
        });
    }


    @Override
    public int getItemCount() {
        return chapterlist.size();
    }


    class ChapterViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        TextView textViewTitle,testid;
        Button test,score;

        public ChapterViewHolder(View itemView) {
            super(itemView);
            cv = (CardView) itemView.findViewById(R.id.subjectcard);
            textViewTitle = itemView.findViewById(R.id.title);
            test = itemView.findViewById(R.id.test);
            score = itemView.findViewById(R.id.score);
        }
    }
}
