package edu.uga.cs.statecapitalquiz;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

/**
 * This class displays the past quiz scores
 * */
public class ScoresActivity extends AppCompatActivity {
    public static final String DEBUG_TAG = "ScoreActivity";

    private RecyclerView recyclerView;
    private QuizRecyclerAdapter recyclerAdapter;

    private QuizData quizData = null;
    private List<Score> scoreList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scores);
        recyclerView = findViewById( R.id.recyclerView );

        // use a linear layout manager for the recycler view
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        // Create a QuizData instance, since we will need to save a new S to the dn.
        // Note that even though more activities may create their own instances of the QuizData
        // class, we will be using a single instance of the QuizDBHelper object, since
        // that class is a singleton class.
        quizData = new QuizData(this);

        // Execute the retrieval of the scores in an asynchronous way,
        // without blocking the main UI thread.
        new QuizDBReader().execute();
    }

    // This is an AsyncTask class (it extends AsyncTask) to perform DB reading of scores, asynchronously.
    private class QuizDBReader extends AsyncTask<Void, List<Score>> {
        // This method will run as a background process to read from db.
        // It returns a list of retrieved SCore objects.
        // It will be automatically invoked by Android, when we call the execute method
        // in the onCreate callback (the scores review activity is started).
        @Override
        protected List<Score> doInBackground(Void... params) {
            quizData.open();
            scoreList = quizData.retrieveAllScores();

            Log.d(DEBUG_TAG, "QuizDBReaderTask: Scores retrieved: " + scoreList.size() );

            return scoreList;
        }

        // This method will be automatically called by Android once the db reading
        // background process is finished.  It will then create and set an adapter to provide
        // values for the RecyclerView.
        // onPostExecute is like the notify method in an asynchronous method call discussed in class.
        @Override
        protected void onPostExecute( List<Score> scoreList) {
            recyclerAdapter = new QuizRecyclerAdapter(scoreList);
            recyclerView.setAdapter( recyclerAdapter );
        }
    }
}
