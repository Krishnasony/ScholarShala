package com.appteq.ad.appteq;

import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.view.GravityCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.FrameLayout;
import com.appteq.ad.appteq.data.DBHandler;
import com.appteq.ad.appteq.model.SubjectModel;
import java.util.ArrayList;

public class SubjectActivity extends NavigationActivity {
        CardView cardViesubject;
        private ArrayList<SubjectModel> subjects;
        RecyclerView subjectrec;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FrameLayout contentFrameLayout = (FrameLayout) findViewById(R.id.content_frame); //Remember this is the FrameLayout area within your activity_main.xml
        getLayoutInflater().inflate(R.layout.activity_subject, contentFrameLayout);
        final Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        CollapsingToolbarLayout collapsingToolbar = findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle("SUBJECTS");
        collapsingToolbar.setCollapsedTitleTypeface(ResourcesCompat.getFont(this,R.font.proximanova));
        collapsingToolbar.setExpandedTitleTypeface(ResourcesCompat.getFont(this,R.font.proximanova));
        int colors[] = {R.color.appcolor1,R.color.appcolor2,R.color.appcolor3,R.color.appcolor4,R.color.appcolor5,R.color.screen1};
        subjectrec = findViewById(R.id.subjectrecycler_view);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        subjectrec.setLayoutManager(llm);
        subjects = new ArrayList<SubjectModel>();
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (drawerLayout.isDrawerVisible(GravityCompat.START)) {
                    drawerLayout.closeDrawer(GravityCompat.START);
                } else {
                    drawerLayout.openDrawer(GravityCompat.START);
                }
            }
        });
        DBHandler mdbhandler = DBHandler.getHelper(this);
        if(is_user_payment_valid){
            String sql = "select * from "+DBHandler.TABLE_USER_SUBJECT;
            Cursor subjectcursor = mdbhandler.getAllData(sql);
            if(subjectcursor!=null){
                int subc =0;
                int totalrow = subjectcursor.getCount();
                while(subjectcursor.moveToNext()){
                    SubjectModel subj = new SubjectModel();
                    String subjectname = subjectcursor.getString(subjectcursor.getColumnIndex(DBHandler.SUBJECT_NAME));
                    int id =subjectcursor.getInt(subjectcursor.getColumnIndex(DBHandler.SUBJECT_ID));
                    subj.setSubtype("subject");
                    subj.setSubject_id(id);
                    subj.setSubject_name(subjectname);
                    if(subc == subjectcursor.getCount()){
                        subc = 0;
                    }
                    if (subjectname.equalsIgnoreCase("mathematics")){
                        subj.setSubjectimge(R.drawable.maths_icon);
                        subj.setSubject_desc(getResources().getString(R.string.mathematics));
                        subj.setSubjectback(colors[subc]);
                    }
                    if (subjectname.equalsIgnoreCase("social science")){
                        subj.setSubjectimge(R.drawable.social_icon);
                        subj.setSubject_desc(getResources().getString(R.string.socialscience));
                        subj.setSubjectback(colors[subc]);
                    }
                    if (subjectname.equalsIgnoreCase("science")){
                        subj.setSubjectimge(R.drawable.science_icon);
                        subj.setSubject_desc(getResources().getString(R.string.science));
                        subj.setSubjectback(colors[subc]);
                    }
                    if (subjectname.equalsIgnoreCase("english")){
                        subj.setSubjectimge(R.drawable.english_icon);
                        subj.setSubject_desc(getResources().getString(R.string.english));
                        subj.setSubjectback(colors[subc]);
                    }
                    if (subjectname.equalsIgnoreCase("hindi")){
                        subj.setSubjectimge(R.drawable.hindi_icon);
                        subj.setSubject_desc(getResources().getString(R.string.hindi));
                        subj.setSubjectback(colors[subc]);
                    }
                    if (subjectname.equalsIgnoreCase("physics")){
                        subj.setSubjectimge(R.drawable.physics_icon);
                        subj.setSubject_desc(getResources().getString(R.string.physics));
                        subj.setSubjectback(colors[subc]);
                    }
                    if (subjectname.equalsIgnoreCase("chemistry")){
                        subj.setSubjectimge(R.drawable.chemistry_icon);
                        subj.setSubject_desc(getResources().getString(R.string.chemistry));
                        subj.setSubjectback(colors[subc]);
                    }
                    if (subjectname.equalsIgnoreCase("biology")){
                        subj.setSubjectimge(R.drawable.biology_icon);
                        subj.setSubject_desc(getResources().getString(R.string.biology));
                        subj.setSubjectback(colors[subc]);
                    }

/*                    card.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent chapterintent = new Intent(SubjectActivity.this,ChapterActivity.class);
                            Bundle b = new Bundle();
                            int sid = view.getId();
                            b.putInt("subject_id",sid);
                            b.putString("subject_name",subjects.get(sid));
                            chapterintent.putExtra("subjectdata",b);
                            startActivity(chapterintent);
                        }
                    });*/
                    // Initialize a new CardView
                    subjects.add(subj);
                    subc++;
                }
                subjectcursor.close();
        }else{
                SubjectModel subj = new SubjectModel();
                subj.setSubject_name("No Subject found");
                subj.setSubject_desc(getResources().getString(R.string.nosubjecterr));
                subj.setSubjectimge(R.drawable.virus);
                subj.setSubjectback(colors[0]);
                subjects.add(subj);
            }
        }else{
            SubjectModel subj = new SubjectModel();
            subj.setSubject_name("Payment Error");
            subj.setSubject_desc(getResources().getString(R.string.paymentvaliderr));
            subj.setSubjectimge(R.drawable.paytm);
            subj.setSubjectback(colors[0]);
            subj.setSubtype("payment");
            subjects.add(subj);
        }
        SubjectAdapter adapter = new SubjectAdapter(this, subjects,user);

        //setting adapter to recyclerview
        subjectrec.setAdapter(adapter);
    }
}
