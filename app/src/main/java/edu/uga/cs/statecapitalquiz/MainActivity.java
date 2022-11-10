package edu.uga.cs.statecapitalquiz;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    public static final String DEBUG_TAG = "MainActivity";
    private AppData appData;
    final String TAG = "CSVReading";
    private Button beginQuiz;
    public static final String APP_DATA = "edu.uga.cs.statecapitalsquiz.AppData";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        appData = new AppData(this);
        appData.open();
        appData.deleteQuestions();
        new QuestionDBWriter().execute();
        beginQuiz = findViewById( R.id.continueButton );

        beginQuiz.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent( v.getContext(), QuizActivity.class );
                //intent.putExtra(APP_DATA,appData);
                startActivity( intent );

            }
        });
    }

    public class QuestionDBWriter extends AsyncTask<Question, Question> {

        protected ArrayList<Question> doInBackground( ) {
            ArrayList<Question> allQuestions = new ArrayList<>();
            try {
                InputStream in_s = getAssets().open("state_capitals.csv");
                BufferedReader reader = new BufferedReader(new InputStreamReader(in_s));
                String csv_line = reader.readLine();
                while((csv_line = reader.readLine()) != null) {
                    String[] values = csv_line.split(",");
                    Question newQuestion = new Question();
                    newQuestion.setState(values[0]);
                    newQuestion.setCapital(values[1]);
                    newQuestion.setCity1(values[2]);
                    newQuestion.setCity2(values[3]);

                    allQuestions.add(newQuestion);
                    System.out.println("NEW QUESTION: " + newQuestion.toString());
                    appData.storeQuestion(newQuestion);
                }


            }catch (Exception e){
                Log.e(TAG, e.toString());
            }

            return allQuestions;
        }

        @Override
        protected void onPostExecute( ArrayList<Question> questions ) {
            // Show a quick confirmation message
            Toast.makeText( getApplicationContext(), questions.size() + " questions stored.",
                    Toast.LENGTH_SHORT).show();


            Log.d( DEBUG_TAG, questions.size() + " questions stored." );
        }

    }

    @Override
    protected void onResume() {
        Log.d( DEBUG_TAG, "MainActivity.onResume()" );
        // open the database in onResume
        if( appData != null )
            appData.open();
        super.onResume();
    }

    @Override
    protected void onPause() {
        Log.d( DEBUG_TAG, "MainActivity.onPause()" );
        // close the database in onPause
        if( appData != null )
            appData.close();
        super.onPause();
    }

    // The following activity callback methods are not needed and are for
    // educational purposes only.
    @Override
    protected void onStart() {
        Log.d( DEBUG_TAG, "MainActivity.onStart()" );
        super.onStart();
    }

    @Override
    protected void onStop() {
        Log.d( DEBUG_TAG, "MainActivity.onStop()" );
        super.onStop();


    }

    @Override
    protected void onDestroy() {
        Log.d( DEBUG_TAG, "MainActivity.onDestroy()" );
        super.onDestroy();

    }

    @Override
    protected void onRestart() {
        Log.d( DEBUG_TAG, "MainActivity.onRestart()" );
        super.onRestart();
    }


}