package com.appteq.ad.appteq;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.appteq.ad.appteq.model.SubjectModel;
import com.appteq.ad.appteq.model.UserModel;

import java.util.List;

public class SubjectAdapter extends RecyclerView.Adapter<SubjectAdapter.SubjectViewHolder>{
    //this context we will use to inflate the layout
    private Context mCtx;
    private UserModel user;
    //we are storing all the products in a list
    private List<SubjectModel> subjectlist;

    //getting the context and product list with constructor
    public SubjectAdapter(Context mCtx, List<SubjectModel> subjectlist, UserModel user) {
        this.mCtx = mCtx;
        this.subjectlist = subjectlist;
        this.user = user;
    }

    @Override
    public SubjectViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //inflating and returning our view holder
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.subject_cardview, parent, false);
        SubjectViewHolder pvh = new SubjectViewHolder(v);
        return pvh;
    }
    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public void onBindViewHolder(SubjectViewHolder holder, int position) {
        //getting the product of the specified position
        final SubjectModel subject = subjectlist.get(position);
        //binding the data with the viewholder views
        holder.textViewTitle.setText(subject.getSubject_name());
        if (subject.getSubject_name().equalsIgnoreCase("mathematics")){
            holder.cardLayout.setBackgroundResource(R.color.card_1);
        }
        if (subject.getSubject_name().equalsIgnoreCase("social science")){
            holder.cardLayout.setBackgroundResource(R.color.card_2);
        }
        if (subject.getSubject_name().equalsIgnoreCase("science")){
            holder.cardLayout.setBackgroundResource(R.color.card_3);
        }
        if (subject.getSubject_name().equalsIgnoreCase("hindi")){
            holder.cardLayout.setBackgroundResource(R.color.card_4);
        }
        if (subject.getSubject_name().equalsIgnoreCase("english")){
            holder.cardLayout.setBackgroundResource(R.color.card_5);
        }
        if (subject.getSubject_name().equalsIgnoreCase("biology")){
            holder.cardLayout.setBackgroundResource(R.color.card_6);
        }
        if (subject.getSubject_name().equalsIgnoreCase("physics")){
            holder.cardLayout.setBackgroundResource(R.color.card_7);
        }
        if (subject.getSubject_name().equalsIgnoreCase("chemistry")){
            holder.cardLayout.setBackgroundResource(R.color.card_8);
        }
        holder.textViewShortDesc.setText(subject.getSubject_desc());
        holder.imageView.setImageDrawable(mCtx.getResources().getDrawable(subject.getSubjectimge()));
        holder.subjectid.setText(subject.getSubject_id()+"");
        if(subject.getSubtype()!=null){
            holder.cv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                   Intent intent = null;
                    if(subject.getSubtype().equalsIgnoreCase("payment")){
                          intent = new Intent(mCtx,PaymentActivity.class);
                    }else if(subject.getSubtype().equalsIgnoreCase("subject")){
                        intent = new Intent(mCtx,ChapterActivity.class);
                    }
                    Bundle b = new Bundle();
                    b.putInt("subject_id",subject.getSubject_id());
                    b.putString("subject_name",subject.getSubject_name());
                    b.putString("user_id",user.getUserid()+"");
                    b.putString("usertoken",user.getLogin_token()+"");
                    intent.putExtra("subjectdata",b);
                    mCtx.startActivity(intent);
                }
            });
        }

    }


    @Override
    public int getItemCount() {
        return subjectlist.size();
    }


    class SubjectViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        TextView textViewTitle, textViewShortDesc,subjectid;
        ImageView imageView;
        LinearLayout cardLayout;

        public SubjectViewHolder(View itemView) {
            super(itemView);
            cv = (CardView) itemView.findViewById(R.id.subjectcard);
            textViewTitle = itemView.findViewById(R.id.title);
            textViewShortDesc = itemView.findViewById(R.id.desc);
            imageView = itemView.findViewById(R.id.subimage);
            subjectid = itemView.findViewById(R.id.subjectid);
            cardLayout = itemView.findViewById(R.id.subject_cardLayout);
        }
    }
}
