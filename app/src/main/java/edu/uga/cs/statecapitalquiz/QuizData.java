package edu.uga.cs.statecapitalquiz;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * This class is facilitates storing and restoring questions and scores.
 */
public class QuizData {

    public static final String DEBUG_TAG = "QuizData";

    // this is a reference to our database; it is used later to run SQL commands
    private SQLiteDatabase db;
    private final SQLiteOpenHelper QuizDbHelper;
    private static final String[] allQuestionColumns = {
            QuizDBHelper.QUESTIONS_COLUMN_ID,
            QuizDBHelper.QUESTIONS_COLUMN_STATE,
            QuizDBHelper.QUESTIONS_COLUMN_CAPITAL,
            QuizDBHelper.QUESTIONS_COLUMN_CITY2,
            QuizDBHelper.QUESTIONS_COLUMN_CITY3
    };
    private static final String[] allScoreColumns = {
            QuizDBHelper.SCORES_COLUMN_ID,
            QuizDBHelper.SCORES_COLUMN_DATE,
            QuizDBHelper.SCORES_COLUMN_SCORE
    };

    public QuizData( Context context ) {
        this.QuizDbHelper = QuizDBHelper.getInstance( context );
    }

    // Opens the database
    public void open() {
        db = QuizDbHelper.getWritableDatabase();
        Log.d( DEBUG_TAG, "QuizData: db open" );
    }

    // Close the database
    public void close() {
        if( QuizDbHelper != null ) {
            QuizDbHelper.close();
            Log.d(DEBUG_TAG, "QuizData: db closed");
        }
    }
    // This is how we restore persistent objects stored as rows in the questions table in the database.
    // For each retrieved row, we create a new Question (Java POJO object) instance and add it to the list.
    public List<Question> retrieveAllQuestions() {
        ArrayList<Question> questionList = new ArrayList<>();
        Cursor cursor = null;
        int columnIndex;

        try {
            // Execute the select query and get the Cursor to iterate over the retrieved rows
            cursor = db.query(QuizDBHelper.TABLE_QUESTIONS, allQuestionColumns,
                    null, null, null, null, null );

            // collect all questions into a List
            if( cursor != null && cursor.getCount() > 0 ) {

                while( cursor.moveToNext() ) {

                    if( cursor.getColumnCount() >= 5) {
                        // get all attribute values of this question
                        columnIndex = cursor.getColumnIndex(QuizDBHelper.QUESTIONS_COLUMN_ID);
                        long id = cursor.getLong(columnIndex);
                        columnIndex = cursor.getColumnIndex(QuizDBHelper.QUESTIONS_COLUMN_STATE);
                        String state = cursor.getString(columnIndex);
                        columnIndex = cursor.getColumnIndex(QuizDBHelper.QUESTIONS_COLUMN_CAPITAL);
                        String capital = cursor.getString(columnIndex);
                        columnIndex = cursor.getColumnIndex(QuizDBHelper.QUESTIONS_COLUMN_CITY2);
                        String city2 = cursor.getString(columnIndex);
                        columnIndex = cursor.getColumnIndex(QuizDBHelper.QUESTIONS_COLUMN_CITY3);
                        String city3 = cursor.getString(columnIndex);

                        // create a new Question object and set its state to the retrieved values
                        Question question = new Question(state, capital, city2, city3);
                        question.setId(id); // set the id (the primary key) of this object
                        // add it to the list
                        questionList.add(question);
                        Log.d(DEBUG_TAG, "Retrieved question: " + question);
                    }
                }
            }
            if (cursor != null) {
                Log.d(DEBUG_TAG, "Number of records from DB: " + cursor.getCount());
            }
            else {
                Log.d(DEBUG_TAG, "Number of records from DB: 0");
            }
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
        // return a list of retrieved questions
        return questionList;
    }

    // Store a new question in the database
    public Question storeQuestion(Question question) {

        // Prepare the values for all of the necessary columns in the table
        // and set their values to the variables of the Question argument.
        // This is how we are providing persistence to a Question (Java object) instance
        // by storing it as a new row in the database table representing scores.
        ContentValues values = new ContentValues();
        values.put(QuizDBHelper.QUESTIONS_COLUMN_STATE, question.getState());
        values.put(QuizDBHelper.QUESTIONS_COLUMN_CAPITAL, question.getCapital());
        values.put(QuizDBHelper.QUESTIONS_COLUMN_CITY2, question.getCity2());
        values.put(QuizDBHelper.QUESTIONS_COLUMN_CITY3, question.getCity3());

        // Insert the new row into the database table;
        // The id (primary key) is automatically generated by the database system
        // and returned as from the insert method call.
        long id = db.insert(QuizDBHelper.TABLE_QUESTIONS, null, values);

        // store the id (the primary key) in the Question instance, as it is now persistent
        question.setId(id);

        Log.d( DEBUG_TAG, "Stored new Question with id: " + String.valueOf(question.getId()));

        return question;
    }

    // Retrieve all scores and return them as a List.
    // This is how we restore persistent objects stored as rows in the scores table in the database.
    // For each retrieved row, we create a new Score (Java POJO object) instance and add it to the list.
    public List<Score> retrieveAllScores() {
        ArrayList<Score> scoreList = new ArrayList<>();
        Cursor cursor = null;
        int columnIndex;

        try {
            // Execute the select query and get the Cursor to iterate over the retrieved rows
            cursor = db.query(QuizDBHelper.TABLE_SCORES, allScoreColumns, null, null, null, null, null);

            // collect all scores into a List
            if( cursor != null && cursor.getCount() > 0 ) {

                while( cursor.moveToNext() ) {

                    if( cursor.getColumnCount() >= 3) {
                        // get all attribute values of this score
                        columnIndex = cursor.getColumnIndex(QuizDBHelper.SCORES_COLUMN_ID);
                        long id = cursor.getLong(columnIndex);
                        columnIndex = cursor.getColumnIndex(QuizDBHelper.SCORES_COLUMN_DATE);
                        String date = cursor.getString(columnIndex);
                        columnIndex = cursor.getColumnIndex(QuizDBHelper.SCORES_COLUMN_SCORE);
                        int number = cursor.getInt(columnIndex);

                        // create a new Score object and set its state to the retrieved values
                        Score score = new Score(date, number);
                        score.setId(id); // set the id (the primary key) of this object
                        // add it to the list
                        scoreList.add(score);
                        Log.d(DEBUG_TAG, "Retrieved Score: " + score);
                    }
                }
            }
            if (cursor != null) {
                Log.d(DEBUG_TAG, "Number of records from DB: " + cursor.getCount());
            }
            else {
                Log.d(DEBUG_TAG, "Number of records from DB: 0");
            }
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
        // return a list of retrieved scores
        return scoreList;
    }

    // storing quiz results into table
    public Score storeScore(Score score){

        // Prepare the values for all of the necessary columns in the table
        // and set their values to the variables of the Score argument.
        // This is how we are providing persistence to a Score (Java object) instance
        // by storing it as a new row in the database table representing scores.
        ContentValues values = new ContentValues();
        values.put(QuizDBHelper.SCORES_COLUMN_DATE, score.getDate());
        values.put(QuizDBHelper.SCORES_COLUMN_SCORE, score.getScore());

        // Insert the new row into the database table;
        // The id (primary key) is automatically generated by the database system
        // and returned as from the insert method call.
        long id = db.insert(QuizDBHelper.TABLE_SCORES, null, values);

        // store the id (the primary key) in the Score instance, as it is now persistent
        score.setId(id);
        Log.d(DEBUG_TAG, "Stored new score with id: " + String.valueOf(score.getId()));

        return score;

    }


}