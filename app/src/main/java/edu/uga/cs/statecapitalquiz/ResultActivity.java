package edu.uga.cs.statecapitalquiz;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * This activity shows the final score.
 * */
public class ResultActivity extends AppCompatActivity {
    public static final String DEBUG_TAG = "ResultActivity";

    private TextView textView4;
    private Button button;
    private Button button2;
    private QuizData scores = null;
    private Score quiz = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String date = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
        setContentView(R.layout.activity_result);
        button = findViewById(R.id.button);
        button.setOnClickListener(new QuizButtonClickListener());
        button2 = findViewById(R.id.button2);
        textView4 = findViewById(R.id.textView4);
        button2.setOnClickListener(new QuizButtonClickListener());
        // Calculate the quiz result and set it in the database
        int count =0;
        for(int i=0;i<QuizActivity.questionAnswers.length;i++){
            if(QuizActivity.questionAnswers[i]==1){
                count++;
            }
        }
        quiz = new Score(date, count);
        scores = new QuizData(this);
        scores.open();
        textView4.setText(Integer.toString(count) + "/6");

    }

    // This is an AsyncTask class (it extends AsyncTask) to perform DB writing of a score, asynchronously.
    public class ScoreDBWriter extends AsyncTask<Score, Score> {

        // This method will run as a background process to write into db.
        // It will be automatically invoked by Android, when we call the execute method
        // in the onClick listener of the Save button.
        @Override
        protected Score doInBackground(Score... score) {
            scores.storeScore(score[0]);
            return score[0];
        }

        // This method will be automatically called by Android once the writing to the database
        // in a background process has finished.  Note that doInBackground returns a Score object.
        // That object will be passed as argument to onPostExecute.
        // onPostExecute is like the notify method in an asynchronous method call discussed in class.
        @Override
        protected void onPostExecute(Score score) {
            // Show a quick confirmation message
            //Toast.makeText( getApplicationContext(), "Score created for " ,
            //        Toast.LENGTH_SHORT).show();



            //Log.d(DEBUG_TAG, "Score saved: " + score );
        }
    }

    private class QuizButtonClickListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            Intent intent = null;
            switch(view.getId()){
                case R.id.button2:
                    intent = new Intent(view.getContext(), ScoresActivity.class);
                    new ScoreDBWriter().execute(quiz);
                    break;
                case R.id.button:
                    intent = new Intent(view.getContext(), MainActivity.class);
                    new ScoreDBWriter().execute(quiz);
            }
            startActivity( intent );
        }
    }
}