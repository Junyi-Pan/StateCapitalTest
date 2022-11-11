package edu.uga.cs.statecapitalquiz;

import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.List;

/**
 * This is an adapter class for the RecyclerView to show all scores.
 */
public class QuizRecyclerAdapter extends RecyclerView.Adapter<QuizRecyclerAdapter.QuizHolder> {

    public static final String DEBUG_TAG = "QuizRecyclerAdapter";

    private List<Score> scoreList;
    private QuizData score = null;

    public QuizRecyclerAdapter( List<Score> scoreList ) {
        this.scoreList = scoreList;
    }

    // The adapter must have a ViewHolder class to "hold" one item to show.
    class QuizHolder extends RecyclerView.ViewHolder {

        TextView date;
        TextView score;

        public QuizHolder(View itemView ) {
            super(itemView);

            date = (TextView) itemView.findViewById( R.id.date );
            score = (TextView) itemView.findViewById( R.id.score );
        }
    }

    @Override
    public QuizHolder onCreateViewHolder( ViewGroup parent, int viewType ) {
        // We need to make sure that all CardViews have the same, full width, allowed by the parent view.
        // This is a bit tricky, and we must provide the parent reference (the second param of inflate)
        // and false as the third parameter (don't attach to root).
        // Consequently, the parent view's (the RecyclerView) width will be used (match_parent).


        View view = LayoutInflater.from( parent.getContext()).inflate( R.layout.recycler, parent, false );
        return new QuizHolder( view );
    }

    // This method fills in the values of a holder to show a Score.
    // The position parameter indicates the position on the list of scores list.
    @Override
    public void onBindViewHolder(QuizHolder holder, int position) {
        Score score = scoreList.get( position );

        //Log.d( DEBUG_TAG, "onBindViewHolder: " + score );

        holder.date.setText(score.getDate());
        holder.score.setText(Integer.toString(score.getScore()) + "/6");
    }

    @Override
    public int getItemCount() {
        return scoreList.size();
    }
}