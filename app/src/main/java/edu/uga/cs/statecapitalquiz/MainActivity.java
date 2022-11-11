package edu.uga.cs.statecapitalquiz;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.opencsv.CSVReader;

import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * The main activity class. It just sets listeners for the two buttons.
 * */
public class MainActivity extends AppCompatActivity {
    public static final String DEBUG_TAG = "MainActivity";
    final String TAG = "CSVReading";

    private QuizData quizData = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button buttonStart = findViewById(R.id.start);
        buttonStart.setOnClickListener(new ButtonClickListener());
        Button buttonScore = findViewById(R.id.scores);
        buttonScore.setOnClickListener(new ButtonClickListener());
        quizData = new QuizData(this);
    }

    private class ButtonClickListener implements View.OnClickListener {
        Intent intent;

        @Override
        public void onClick(View view) {

            try {
                // Open the CSV data file in the assets folder
                InputStream in_s = getAssets().open("state_capitals.csv" );

                // read the CSV data
                CSVReader reader = new CSVReader(new InputStreamReader(in_s));
                String[] nextLine;

                while((nextLine = reader.readNext()) != null ) {
                    Question row = new Question(nextLine[0],nextLine[1], nextLine[2], nextLine[3]);
                    new QuestionDBWriter().execute(row);
                    //Log.e(TAG, nextLine[0]);
                }


            } catch (Exception e) {
                //Log.e( TAG, e.toString() );

            }

            switch (view.getId()) {
                case R.id.start:
                    intent = new Intent(view.getContext(), QuizActivity.class);
                    break;
                case R.id.scores:
                    intent = new Intent(view.getContext(), ScoresActivity.class);
                    break;
            }
            startActivity(intent);
        }


    }

    // This is an AsyncTask class (it extends AsyncTask) to perform DB writing of a question, asynchronously.
    public class QuestionDBWriter extends AsyncTask<Question, Question> {

        // This method will run as a background process to write into db.
        // It will be automatically invoked by Android, when we call the execute method
        // in the onClick listener of the Save button.
        @Override
        protected Question doInBackground(Question... questions) {
            quizData.storeQuestion(questions[0]);
            return questions[0];
        }

        // This method will be automatically called by Android once the writing to the database
        // in a background process has finished.  Note that doInBackground returns a Question object.
        // That object will be passed as argument to onPostExecute.
        // onPostExecute is like the notify method in an asynchronous method call discussed in class.
        @Override
        protected void onPostExecute(Question question) {
            // Show a quick confirmation message
            Toast.makeText( getApplicationContext(), "Question created for " ,
                    Toast.LENGTH_SHORT).show();

            //Log.d( DEBUG_TAG, "Question saved: " + question );
        }
    }

    @Override
    protected void onResume() {
        //Log.d( DEBUG_TAG, "MainActivity.onResume()" );
        // open the database in onResume
        if(quizData != null)
            quizData.open();
        super.onResume();
    }

    @Override
    protected void onPause() {
        //Log.d( DEBUG_TAG, "MainActivity.onPause()" );
        // close the database in onPause
        if( quizData != null )
            quizData.close();
        super.onPause();
    }
}