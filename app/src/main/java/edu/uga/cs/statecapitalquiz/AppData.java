package edu.uga.cs.statecapitalquiz;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.Serializable;

/**
 * This class is facilitates storing and restoring questions and quizzes stored.
 */
public class AppData implements Serializable {

    public static final String DEBUG_TAG = "AppData";

    // this is a reference to our database; it is used later to run SQL commands
    private SQLiteDatabase db;
    private SQLiteOpenHelper myDBHelper;
    private static final String[] allColumns = {
            DBHelper.QUESTIONS_COLUMN_ID,
            DBHelper.QUESTIONS_COLUMN_STATE,
            DBHelper.QUESTIONS_COLUMN_CAPITAL,
            DBHelper.QUESTIONS_COLUMN_CITY1,
            DBHelper.QUESTIONS_COLUMN_CITY2,
            DBHelper.QUIZZES_COLUMN_ID,
            DBHelper.QUIZZES_COLUMN_DATE,
            DBHelper.QUIZZES_COLUMN_Q1,
            DBHelper.QUIZZES_COLUMN_Q2,
            DBHelper.QUIZZES_COLUMN_Q3,
            DBHelper.QUIZZES_COLUMN_Q4,
            DBHelper.QUIZZES_COLUMN_Q5,
            DBHelper.QUIZZES_COLUMN_Q6,
            DBHelper.QUIZZES_COLUMN_SCORE
    };

    public AppData( Context context ) {
        this.myDBHelper = DBHelper.getInstance( context );
    }

    // Open the database
    public void open() {
        db = myDBHelper.getWritableDatabase();
        Log.d( DEBUG_TAG, "AppData: db open" );
    }

    // Close the database
    public void close() {
        if( myDBHelper != null ) {
            myDBHelper.close();
            Log.d(DEBUG_TAG, "JobLeadsData: db closed");
        }
    }

    // Store a new question in the DB
    public Question storeQuestion( Question question ){
        ContentValues values = new ContentValues();
        values.put( DBHelper.QUESTIONS_COLUMN_STATE, question.getState());
        values.put( DBHelper.QUESTIONS_COLUMN_CAPITAL, question.getCapital() );
        values.put( DBHelper.QUESTIONS_COLUMN_CITY1, question.getCity1() );
        values.put( DBHelper.QUESTIONS_COLUMN_CITY2, question.getCity2() );



        long id = db.insert( DBHelper.TABLE_QUESTIONS, null, values );

        // store the id (the primary key) in the JobLead instance, as it is now persistent
        question.setId( id );

        Log.d( DEBUG_TAG, "Stored new question with id: " + String.valueOf( question.getId() ) );

        return question;
    }

    public void deleteQuestions(){
        db.delete(DBHelper.TABLE_QUESTIONS,null,null);
        db.execSQL( "DELETE FROM SQLITE_SEQUENCE WHERE NAME = '" + DBHelper.TABLE_QUESTIONS + "'" );
    }


}
