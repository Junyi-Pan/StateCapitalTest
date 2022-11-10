package edu.uga.cs.statecapitalquiz;

import java.util.Random;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import java.util.ArrayList;

public class QuizActivity extends AppCompatActivity {

    private static final String TAG = "QuizActivity";

    SectionsPagerAdapter mSectionsPagerAdapter;
    ViewPager mViewPager;
    ActionBar mActionBar;

    public ArrayList<Integer> getRandomQuestions(){

        ArrayList<Integer> toReturn = new ArrayList<>();
        Random randomGenerator = new Random();
        int randomInt = randomGenerator.nextInt(50) + 1;
        toReturn.add(randomInt);

        while(toReturn.size() < 6){
            randomInt = randomGenerator.nextInt(50) + 1;
            if(!toReturn.contains(randomInt))
                toReturn.add(randomInt);
        }
        return toReturn;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.quiz_layout);

        mActionBar = getSupportActionBar();
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(), 6);
        mActionBar.setTitle(mSectionsPagerAdapter.getPageTitle(0));
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);
    }

    public void loadView(TextView textView, String state,
                         RadioButton Capital, String capital,
                         RadioButton City1, String city1,
                         RadioButton City2, String city2) {
        textView.setText(state);
        Capital.setText(capital);
        City1.setText(city1);
        City2.setText(city2);
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {
        private final int mSize;
        private ArrayList<Integer> questions = getRandomQuestions();
        int counter = -1;

        public SectionsPagerAdapter(FragmentManager fm, int size) {
            super(fm);
            this.mSize = size;
        }

        @Override
        public Fragment getItem(int position) {
            counter ++;
            //System.out.println("before counter");
            //System.out.println("COUNTER: " + counter);
            //System.out.println("QUESTION INDEX: " + questions.get(counter));
            return PlaceholderFragment.newInstance(position + 1, questions.get(counter));

        }

        @Override
        public int getCount() {
            return mSize;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            int questionNum = position + 1;
            if(questionNum == 7){
                return String.valueOf("Quiz Results");
            }
            return String.valueOf("Question " + questionNum);
        }
    }

    public static class PlaceholderFragment extends Fragment {
        private AppData appData = null;
        private ArrayList<Question> questionsList = new ArrayList<>();

        private static final String ARG_SECTION_NUMBER = "section_number";
        private static final String ARG_Q = "quest";

        private int questionNum;
        private int questionId;
        private TextView mTextView;
        private RadioButton button1;
        private RadioButton button2;
        private RadioButton button3;

        public static PlaceholderFragment newInstance(int sectionNumber, int question) {

            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            args.putInt(ARG_Q, question);
            fragment.setArguments(args);
            return fragment;
        }

        public PlaceholderFragment() {
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            if (getArguments() != null) {
                questionNum = getArguments().getInt(ARG_SECTION_NUMBER);
                questionId = getArguments().getInt(ARG_Q);

            } else {
                questionNum = -1;
            }

            appData = new AppData(getActivity());
            appData.open();
            questionsList = appData.retrieveAllQuestions();
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.quiz_pager_layout, container, false);
            mTextView = (TextView) rootView.findViewById(R.id.section_label);
            button1 = (RadioButton) rootView.findViewById(R.id.radio1);
            button2 = (RadioButton) rootView.findViewById(R.id.radio2);
            button3 = (RadioButton) rootView.findViewById(R.id.radio3);
            return rootView;
        }

        @Override
        public void onViewCreated(View view, Bundle savedInstanceState) {
            super.onActivityCreated(savedInstanceState);
            System.out.println("STATE: " + questionsList.get(0).getState());
            if (QuizActivity.class.isInstance(getActivity())) {
                System.out.println("STATE: " + questionId);
                System.out.println("STATE: " + questionsList.get(questionId-1).getState());
                final String state = questionsList.get(questionId-1).getState();

                final String capital = questionsList.get(questionId-1).getCapital();
                final String city1 = questionsList.get(questionId-1).getCity1();
                final String city2 = questionsList.get(questionId-1).getCity2();
                ((QuizActivity) getActivity()).loadView(mTextView, state, button1, capital, button2, city1, button3, city2);
            }
        }

        @Override
        public void onResume() {
            super.onResume();

            // Open the database
            if (appData != null && !appData.isDBOpen()) {
                appData.open();
                Log.d(TAG, "ReviewJobLeadsFragment.onResume(): opening DB");
            }

            // Update the app name in the Action Bar to be the same as the app's name
            ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(getResources().getString(R.string.app_name));
        }

        @Override
        public void onPause() {
            super.onPause();

            // close the database in onPause
            if (appData != null) {
                appData.close();
                Log.d(TAG, "ReviewJobLeadsFragment.onPause(): closing DB");
            }
        }
    }
}