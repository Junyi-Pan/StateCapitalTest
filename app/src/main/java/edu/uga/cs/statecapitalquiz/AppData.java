package edu.uga.cs.statecapitalquiz;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * This class is facilitates storing and restoring questions and quizzes stored.
 */
public class AppData implements Serializable {

    public static final String DEBUG_TAG = "AppData";

    // this is a reference to our database; it is used later to run SQL commands
    private SQLiteDatabase db;
    private final SQLiteOpenHelper myDBHelper;
    private static final String[] questionColumns = {
            DBHelper.QUESTIONS_COLUMN_ID,
            DBHelper.QUESTIONS_COLUMN_STATE,
            DBHelper.QUESTIONS_COLUMN_CAPITAL,
            DBHelper.QUESTIONS_COLUMN_CITY1,
            DBHelper.QUESTIONS_COLUMN_CITY2
    };
    private static final String[] quizColumns = {
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

    public boolean isDBOpen()
    {
        return db.isOpen();
    }

    public ArrayList<Question> retrieveAllQuestions() {
        ArrayList<Question> questions = new ArrayList<>();
        Cursor cursor = null;
        int columnIndex;

        try {
            // Execute the select query and get the Cursor to iterate over the retrieved rows
            cursor = db.query(DBHelper.TABLE_QUESTIONS, questionColumns,
                    null, null, null, null, null );

            // collect all job leads into a List
            if( cursor != null && cursor.getCount() > 0 ) {

                while( cursor.moveToNext() ) {

                    if( cursor.getColumnCount() >= 5) {

                        // get all attribute values of this job lead
                        columnIndex = cursor.getColumnIndex( DBHelper.QUESTIONS_COLUMN_ID );
                        long id = cursor.getLong( columnIndex );
                        columnIndex = cursor.getColumnIndex( DBHelper.QUESTIONS_COLUMN_STATE );
                        String state = cursor.getString( columnIndex );
                        columnIndex = cursor.getColumnIndex( DBHelper.QUESTIONS_COLUMN_CAPITAL );
                        String capital = cursor.getString( columnIndex );
                        columnIndex = cursor.getColumnIndex( DBHelper.QUESTIONS_COLUMN_CITY1 );
                        String city1 = cursor.getString( columnIndex );
                        columnIndex = cursor.getColumnIndex( DBHelper.QUESTIONS_COLUMN_CITY2 );
                        String city2 = cursor.getString( columnIndex );

                        // create a new Question object and set its state to the retrieved values
                        Question question = new Question(state, capital, city1, city2);
                        question.setId(id); // set the id (the primary key) of this object
                        // add it to the list
                        questions.add( question );
                        Log.d(DEBUG_TAG, "Retrieved Question: " + question);
                    }
                }
            }
            if( cursor != null )
                Log.d( DEBUG_TAG, "Number of records from DB: " + cursor.getCount() );
            else
                Log.d( DEBUG_TAG, "Number of records from DB: 0" );
        }
        catch( Exception e ){
            Log.d( DEBUG_TAG, "Exception caught: " + e );
        }
        finally{
            // we should close the cursor
            if (cursor != null) {
                cursor.close();
            }
        }
        // return a list of retrieved job leads
        return questions;
    }

    public void deleteQuestionsTable(){
        db.delete(DBHelper.TABLE_QUESTIONS,null,null);
        db.execSQL("DELETE FROM SQLITE_SEQUENCE WHERE NAME = '" + DBHelper.TABLE_QUESTIONS + "'" );
    }

}
