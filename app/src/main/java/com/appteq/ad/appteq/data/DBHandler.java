package com.appteq.ad.appteq.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import com.appteq.ad.appteq.PaymentActivity;
import com.appteq.ad.appteq.model.AnswerModel;
import com.appteq.ad.appteq.model.ChapterModel;
import com.appteq.ad.appteq.model.PassageModel;
import com.appteq.ad.appteq.model.PaymentModel;
import com.appteq.ad.appteq.model.QuestionModel;
import com.appteq.ad.appteq.model.SubjectModel;
import com.appteq.ad.appteq.model.TestModel;
import com.appteq.ad.appteq.model.UserModel;

import java.sql.SQLWarning;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class DBHandler  extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "edumonk_testapp.db";
    public static final String PAYMENT_TABLE = "payment";
    private Context context;
    private static DBHandler instance;
    //Test
    public static final String TABLE_TEST = "test";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_TEST_ID = "test_id";
    public static final String COLUMN_TEST_NAME = "test_name";
    public static final String COLUMN_SUBJECT_ID = "subject_id";
    public static final String COLUMN_CHAPTER_ID = "chapter_id";
    public static final String COLUMN_START_TIME = "test_start_time";
    public static final String COLUMN_END_TIME = "test_end_time";
    public static final String COLUMN_CLASS_ID = "class_id";
    public static final String COLUMN_IS_ACTIVE = "is_active";
    public static final String COLUMN_TEST_INS = "test_ins";
    // question
    public static final String TABLE_QUESTIONS = "questions";
    public static final String QUESTIONS_ID = "ques_ID";
    public static final String QUESTIONS_TEXT = "text";
    public static final String QUESTIONS_HAS_MULTIPLE_ANS = "has_multiple";
    public static final String QUESTION_SUBJECT_ID="subject_id";
    public static final String QUESTION_CLASS_ID="class_id";
    public static final String QUESTION_TEST_ID="test_id";
    public static final String QUESTION_IS_FILL_UP="is_fillUp";
    public static final String QUESTIONS_PASSAGE_ID = "passage_id";

    //answer(id, que_id, test_id, ans_text, hsa_url, is_right)
//Answer
    public static final String TABLE_ANSWERS = "answers";
    public static final String ANSWER_ID = "ans_id";
    public static final String ANSWER_TEXT = "ans_text";
    public static final String ANSWER_HAS_URL = "ans_has_url";
    public static final String ANSWER_IS_RIGHT = "is_right";
    public static final String ANSWER_QUESTION_ID = "ques_id";

    //Score(test_id, test_name, test_score, test_date,Sub_id,class_id)
//SCORE
    public static final String TABLE_SCORE = "score";
    public static final String SCORE_TEST_ID = "test_id";
    public static final String SCORE_CHAPTER_ID = "chapter_id";
    public static final String SCORE_TEST_SCORE = "test_score";
    public static final String SCORE_TEST_DATE = "score_test_date";
    public static final String SCORE_CLASS_ID = "class_id";
    public static final String SCORE_SUBJECT_ID = "subject_id";
    public static final String SCORE_QUESTIONS = "total_questions";
    //answer
// PAYMENT TABLE COLUMN
    public static final String PAYMENT_USER = "user_id";
    public static final String PAYMENT_AMOUNT = "amount";
    public static final String PAYMENT_DATE = "payment_date";
    public static final String PAYMENT_TRANSACTION = "payment_txn";
    public static final String PAYMENT_BANK_TRANSACTION = "payment_bank_txn";
    public static final String PAYMENT_STATUS = "status";
    public static final String PAYMENT_CURRENEY = "currency";
    public static final String NEXT_PAYMENT_DATE  = "payment_valid_date";
    public static final String PAYMENT_PACKAGE = "payment_package";
    public static final String PAYMENT_MODE = "payment_mode";

// User auth data

    public static final String TABLE_USER_AUTH = "user_auth";
    public static final String USER_AUTH_ID = "user_id";
    public static final String USER_TOKEN = "user_auth_token";
    public static final String CURRENT_TIME = "auth_current_time";
    public static final String LAST_LOGIN = "last_login";

    // User data
    public static final String TABLE_USER = "user";
    public static final String USER_ID = "ID";
    public static final String USER_NAME = "name";
    public static final String USER_AUTH_NAME = "username";
    public static final String USER_PASSWORD= "passwd";
    public static final String USER_PIC_URL = "user_image";
    public static final String USER_REGISTER_PHONE = "user_phone";
    public static final String USER_CLASS_ID = "class_id";
    public static final String USER_CLASS_NAME = "class_name";
    public static final String USER_PARENT_NAME = "parent_name";
    public static final String USER_PARENT_PHONE = "parent_phone";
    public static final String USER_MEDIUM = "user_class_med";
    public static final String DOB = "user_dob";

    // User data
    public static final String TABLE_USER_SUBJECT = "user_subject";
    public static final String SUBJECT_USER_ID = "user_id";
    public static final String SUBJECT_ID = "subject_id";
    public static final String SUBJECT_NAME = "subject_name";

    // User passage
    public static final String TABLE_PASSAGE = "passage";
    public static final String PASSAGE_TEXT = "passage_text";
    public static final String PASSAGE_ID = "passage_id";
    public static final String COLUMN_TEST_IDS = "test_id";

    // user chapters
    public static final String CHAPTER_TABLE = "user_chapter";
    public static final String USER_CHAPTER = "chapter_id";
    public static final String USER_CHAPTER_NAME = "chapter_name";
    public static final String CHAPTER_USER_ID = "user_id";
    public static final String CHAPTER_CLASS_ID  = "class_id";
    public static final String CHAPTER_SUBJECT_ID = "subject_id";

    public DBHandler(Context context){
        super(context,DATABASE_NAME,null,1);
    }

    public DBHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
        this.context = context;
    }

    public static synchronized DBHandler getHelper(Context context)
    {
        if (instance == null)
            instance = new DBHandler(context);

        return instance;
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "create table "+ TABLE_TEST + "(" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COLUMN_TEST_ID + " INTEGER," +
                COLUMN_TEST_NAME + " TEXT," +
                COLUMN_TEST_INS + " TEXT,"+
                COLUMN_CLASS_ID + " INTEGER," +
                COLUMN_SUBJECT_ID + " INTEGER," +
                COLUMN_CHAPTER_ID + " INTEGER," +
                COLUMN_IS_ACTIVE + " INTEGER," +
                COLUMN_START_TIME + " TEXT,"+
                COLUMN_END_TIME + " TEXT);";
        db.execSQL(query);

        String createTableQuestions = "create table "+ TABLE_QUESTIONS + "(" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                QUESTIONS_ID + " INTEGER," +
                QUESTIONS_TEXT + " TEXT," +
                QUESTION_TEST_ID + " INTEGER," +
                QUESTION_SUBJECT_ID + " INTEGER," +
                QUESTION_CLASS_ID + " INTEGER," +
                QUESTIONS_HAS_MULTIPLE_ANS + " INTEGER," +
                QUESTION_IS_FILL_UP + " INTEGER,"+
                QUESTIONS_PASSAGE_ID + " INTEGER);";
        db.execSQL(createTableQuestions);
        //Questions Table


        //Passage Table
        String createTablePassage = "create table "+ TABLE_PASSAGE + "(" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COLUMN_TEST_ID + " INTEGER," +
                PASSAGE_ID + " INTEGER," +
                PASSAGE_TEXT + " TEXT);";
        db.execSQL(createTablePassage);

        //Answer Table
        String createAnswerTable = "create table " + TABLE_ANSWERS + "(" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                ANSWER_ID + " INTEGER," +
                ANSWER_QUESTION_ID+ " INTEGER,"+
                ANSWER_TEXT + " TEXT," +
                ANSWER_HAS_URL + " INTEGER," +
                ANSWER_IS_RIGHT + " INTEGER);";
        db.execSQL(createAnswerTable);

        //Score Table
        //db.execSQL("DROP TABLE IF EXISTS "+ TABLE_SCORE);
        String createScoreTable = "create table " + TABLE_SCORE + "(" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                SCORE_TEST_ID + " INTEGER, " +
                SCORE_SUBJECT_ID + " INTEGER," +
                SCORE_CHAPTER_ID + " INTEGER," +
                SCORE_CLASS_ID + " INTEGER," +
                SCORE_QUESTIONS + " INTEGER," +
                SCORE_TEST_SCORE + " INTEGER, " +
                SCORE_TEST_DATE + " TEXT);";
        db.execSQL(createScoreTable);

        //Payment table
        String createpaymentTable = "create table " + PAYMENT_TABLE + "(" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                PAYMENT_USER + " INTEGER, " +
                PAYMENT_AMOUNT + " NUMBER," +
                PAYMENT_TRANSACTION+ " TEXT,"+
                PAYMENT_BANK_TRANSACTION+ " TEXT,"+
                PAYMENT_PACKAGE+ " TEXT,"+
                PAYMENT_DATE + " TEXT,"+
                NEXT_PAYMENT_DATE+ " TEXT,"+
                PAYMENT_STATUS+ " TEXT,"+
                PAYMENT_CURRENEY+ " TEXT,"+
                PAYMENT_MODE+ " TEXT);";
        db.execSQL(createpaymentTable);

        String userauth = "create table " + TABLE_USER_AUTH + "(" +
                USER_AUTH_ID + " INTEGER PRIMARY KEY," +
                USER_TOKEN + " TEXT, " +
                CURRENT_TIME + " TEXT,"+
                LAST_LOGIN + " TEXT);";
        db.execSQL(userauth);

        //passage

        String userdata = "create table " + TABLE_USER + "(" +
                USER_ID + " INTEGER PRIMARY KEY," +
                USER_NAME + " TEXT, " +
                USER_AUTH_NAME + " TEXT, " +
                USER_PASSWORD + " TEXT, " +
                DOB + " TEXT, " +
                USER_PIC_URL+" TEXT, "+
                USER_REGISTER_PHONE + " INTEGER," +
                USER_PARENT_NAME + " TEXT,"+
                USER_PARENT_PHONE + " INTEGER,"+
                USER_CLASS_ID + " INTEGER,"+
                USER_CLASS_NAME + " TEXT,"+
                USER_MEDIUM + " TEXT);";
        db.execSQL(userdata);

        String usersubject = "create table " + TABLE_USER_SUBJECT + "(" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                SUBJECT_USER_ID + " INTEGER, " +
                SUBJECT_ID + " INTEGER," +
                SUBJECT_NAME + " TEXT);";
        db.execSQL(usersubject);

        String userchapter = "create table " + CHAPTER_TABLE + "(" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                USER_CHAPTER + " INTEGER, " +
                USER_CHAPTER_NAME + " TEXT," +
                CHAPTER_CLASS_ID + " INTEGER,"+
                CHAPTER_SUBJECT_ID + " INTEGER," +
                CHAPTER_USER_ID + " INTEGER);";
        db.execSQL(userchapter);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TEST);
        onCreate(db);
    }

    public DBHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version, DatabaseErrorHandler errorHandler) {
        super(context, name, factory, version, errorHandler);
    }

    //Insert values in TEST TABLE
    public boolean insertTest(TestModel test,SQLiteDatabase sqLiteDatabase){
        ContentValues values = new ContentValues();
        Log.d("Test name",test.getTest_name()+test.getStart_time());
        values.put(COLUMN_TEST_ID,test.getTest_id());
        values.put(COLUMN_TEST_NAME,test.getTest_name());
        values.put(COLUMN_TEST_INS,test.getTest_ins());
        values.put(COLUMN_CLASS_ID,test.getClass_id());
        values.put(COLUMN_SUBJECT_ID,test.getSubject_id());
        values.put(COLUMN_IS_ACTIVE,test.getIs_active());
        values.put(COLUMN_START_TIME,test.getStart_time());
        values.put(COLUMN_END_TIME,test.getEnd_time());
        values.put(COLUMN_CHAPTER_ID,test.getChapter_id());
        Log.d("Test name",values.get(COLUMN_TEST_ID)+test.getStart_time());
        long result = sqLiteDatabase.insert(TABLE_TEST, null, values);
        if(result == -1) {
            return false;
        }
        else {
            return true;
        }
    }

    //Insert values in QUESTION TABLE
    public boolean insertQuestions(QuestionModel questionModel,SQLiteDatabase sqLiteDatabase){
        ContentValues values = new ContentValues();
        int has_mul  = 0;
        int is_fillUp = 0;
        if(questionModel.isIs_ml_ans()){
            has_mul = 1;
        }
        if(questionModel.isIsFillUp()){
            is_fillUp = 1;
        }
        values.put(QUESTIONS_ID,questionModel.getQues_id());
        values.put(QUESTIONS_TEXT,questionModel.getQuestion());
        values.put(QUESTIONS_HAS_MULTIPLE_ANS,has_mul);
        values.put(QUESTION_IS_FILL_UP,is_fillUp);
        values.put(QUESTION_CLASS_ID,questionModel.getClass_id());
        values.put(QUESTION_TEST_ID,questionModel.getTest_id());
        values.put(QUESTION_SUBJECT_ID,questionModel.getSubject_id());
        values.put(QUESTIONS_PASSAGE_ID,questionModel.getPassage_id());
        long result = sqLiteDatabase.insertOrThrow(TABLE_QUESTIONS, null,values);
        if(result == -1){
            return false;
        }
        else {
            return true;
        }

    }


    //Insert Passage Data
    public boolean insertPassage(PassageModel passageModel, SQLiteDatabase sqLiteDatabase){
        ContentValues values = new ContentValues();
        values.put(PASSAGE_ID,passageModel.getPassageId());
        values.put(PASSAGE_TEXT,passageModel.getPassageText());
        values.put(COLUMN_TEST_IDS,passageModel.getTestId());
        long result = sqLiteDatabase.insertOrThrow(TABLE_PASSAGE, null, values);
        if(result == -1){
            return false;
        }
        else {
            return true;
        }
    }

    //Insert values in ANSWERS TABLE
    public boolean insertAnswers(AnswerModel answerModel,SQLiteDatabase sqLiteDatabase){
        int is_right = 0;
        int is_url = 0;
        if(answerModel.isIs_right()){
            is_right = 1;
        }
        if(answerModel.isIs_url()){
            is_url = 1;
        }
        ContentValues values = new ContentValues();
        values.put(ANSWER_ID,answerModel.getId());
        values.put(ANSWER_QUESTION_ID,answerModel.getQues_id());
        values.put(ANSWER_TEXT,answerModel.getAnswer());
        values.put(ANSWER_HAS_URL,is_url);
        values.put(ANSWER_IS_RIGHT,is_right);
        long result = sqLiteDatabase.insert(TABLE_ANSWERS, null,values);
        if(result == -1){
            return false;
        }
        else {
            return true;
        }
    }

    public boolean insertScore (TestModel testModel){
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(SCORE_TEST_ID,testModel.getTest_id());
        values.put(SCORE_CLASS_ID,testModel.getClass_id());
        values.put(SCORE_SUBJECT_ID,testModel.getSubject_id());
        values.put(SCORE_CHAPTER_ID,testModel.getChapter_id());
        values.put(SCORE_QUESTIONS,testModel.getQues().size());
        values.put(SCORE_TEST_SCORE,testModel.getScore());
        values.put(SCORE_TEST_DATE,testModel.getStart_time());
        long result = sqLiteDatabase.insert(TABLE_SCORE, null,values);
        sqLiteDatabase.close();
        if(result == -1){
            return false;
        }
        else {
            return true;
        }
    }
    public boolean insertUserData(UserModel user,SQLiteDatabase db){
        ContentValues values = new ContentValues();
        values.put(USER_ID,user.getUserid());
        values.put(USER_NAME,user.getUser_fullname());
        values.put(USER_PASSWORD,user.getPasswd());
        values.put(USER_AUTH_NAME,user.getUsername());
        values.put(USER_REGISTER_PHONE,user.getPhone_no());
        values.put(DOB,user.getDob());
        values.put(USER_PARENT_NAME,user.getParent_name());
        values.put(USER_PARENT_PHONE,user.getParent_phone_no());
        values.put(USER_CLASS_ID,user.getUser_class());
        values.put(USER_CLASS_NAME,user.getUser_class_name());
        values.put(USER_MEDIUM,user.getUser_class_med());
        long result = db.insert(TABLE_USER, null,values);
        if(result == -1){
            return false;
        }
        else {
            return true;
        }
    }

    public void insertUserSubject(UserModel user, SQLiteDatabase db){
        String sql = "insert into "+TABLE_USER_SUBJECT+"("+SUBJECT_USER_ID+","+SUBJECT_ID+","+SUBJECT_NAME+") VALUES";
        ArrayList<SubjectModel> subjects = user.getSubjects();
        for (SubjectModel subjectModel:subjects) {
           sql+= "("+user.getUserid()+","+subjectModel.getSubject_id()+",'"+subjectModel.getSubject_name()+"'),";
        }
        int index = sql.lastIndexOf(",");
        sql = sql.substring(0,index);
        Log.d("Subject query",sql);
        db.execSQL(sql);
    }

    public void insertUserChapter(UserModel user, SQLiteDatabase db){
        String sql = "insert into "+CHAPTER_TABLE+"("+USER_CHAPTER+","+USER_CHAPTER_NAME+","+CHAPTER_CLASS_ID+","+CHAPTER_SUBJECT_ID+","+CHAPTER_USER_ID+") VALUES";
        ArrayList<ChapterModel> chapters = user.getChapters();
        for (ChapterModel chapterModel:chapters) {
            sql+= "("+chapterModel.getChapter_id()+",'"+chapterModel.getChapter_name()+"',"+chapterModel.getClass_id()+","+chapterModel.getSubject_id()+","+chapterModel.getUser_id()+"),";
        }
        int index = sql.lastIndexOf(",");
        sql = sql.substring(0,index);
        db.execSQL(sql);
    }

    public void insertAuthdata(UserModel user, SQLiteDatabase db){
        ContentValues values = new ContentValues();
        values.put(USER_AUTH_ID,user.getUserid());
        values.put(USER_TOKEN,user.getLogin_token());
        values.put(CURRENT_TIME,user.getUser_login_current_time());
        if(user.getUser_last_login()!=null && user.getUser_last_login()!=""){
            values.put(LAST_LOGIN,user.getUser_last_login());
        }
    // Toast.makeText(context,"data loaded",Toast.LENGTH_SHORT).show();
        long result = db.insert(TABLE_USER_AUTH, null,values);
    }

    //Cursor
    public Cursor getAllData(String sql){
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor res = sqLiteDatabase.rawQuery(sql,null);
        return res;
    }

    public boolean updateTestData(TestModel test){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TEST_ID,test.getTest_id());
        values.put(COLUMN_TEST_NAME,test.getTest_name());
        values.put(COLUMN_CLASS_ID,test.getClass_id());
        values.put(COLUMN_IS_ACTIVE,test.getIs_active());
        values.put(COLUMN_SUBJECT_ID,test.getClass_id());
        values.put(COLUMN_START_TIME,test.getStart_time());
        values.put(COLUMN_END_TIME,test.getEnd_time());
        sqLiteDatabase.update(TABLE_TEST,values, " "+COLUMN_TEST_ID+" = ?",new String[]{String.valueOf(test.getTest_id())});
        sqLiteDatabase.close();
        return  true;
    }
    public boolean updateUserData(UserModel user){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(USER_PIC_URL,user.getUserimage());
        values.put(USER_CLASS_NAME,user.getUser_class_name());
        values.put(USER_MEDIUM,user.getUser_class_med());
        sqLiteDatabase.update(TABLE_USER,values, " "+USER_ID+" = ?",new String[]{String.valueOf(user.getUserid())});
        return true;
    }
    public void deleteData(String sql,SQLiteDatabase db){
        db.execSQL(sql);
    }


    public void insertPayments(PaymentModel model){
        SQLiteDatabase db1 = this.getWritableDatabase();
        ContentValues values  = new ContentValues();
        values.put(PAYMENT_USER,model.getUser_id());
        values.put(PAYMENT_AMOUNT,model.getAmount());
        values.put(PAYMENT_TRANSACTION,model.getPayment_txn());
        values.put(PAYMENT_BANK_TRANSACTION,model.getPayment_bank_txn());
        values.put(PAYMENT_PACKAGE,model.getUser_id());
        values.put(PAYMENT_DATE,model.getPayment_date());
            if(model.getPayment_valid_date()!=null){
                values.put(NEXT_PAYMENT_DATE,model.getPayment_valid_date());
            }
        values.put(PAYMENT_STATUS,model.getStatus());
        values.put(PAYMENT_CURRENEY,model.getCurrency());
        values.put(PAYMENT_MODE,model.getPayment_mode());
        db1.insert(PAYMENT_TABLE,null,values);
        db1.close();
    }

    public void insertData(PaymentModel model){
        String amount = PaymentActivity.editTextprice.getText().toString();
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();
        SQLiteDatabase db1 = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(PAYMENT_USER,model.getUser_id());
        contentValues.put(PAYMENT_AMOUNT, model.getAmount());
        //contentValues.put(PAYMENT_DATE, dateFormat.format(model.getDate()));
        db1.insert(PAYMENT_TABLE, null, contentValues);
        db1.close();
    }
    public Cursor getuser() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from " + PAYMENT_TABLE + " ",
                null);
        return res;
    }
}
