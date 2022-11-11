package edu.uga.cs.statecapitalquiz;

import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

/**
 * This class is used for the quiz portion of this app.
 * */
public class QuizActivity extends AppCompatActivity {
    SectionsPagerAdapter mSectionsPagerAdapter;
    ViewPager mViewPager;
    ActionBar mActionBar;
    public final static Question [] questionArr = new Question[6];
    ArrayList<String> arrayList = new ArrayList<>();
    public static ArrayList<Integer> questionNum = new ArrayList<>();
    public static int [] questionAnswers = new int[6];
    private QuizData quizData = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);
        quizData = new QuizData( this );
        quizData.open();
        int count=0;
        while(questionNum.size()<6){
            Random randomGenerator = new Random();
            int random_int = randomGenerator.nextInt(50) + 1;
            if(!(questionNum.contains(random_int))){
                questionArr[count]=quizData.retrieveAllQuestions().get(random_int);
                questionNum.add(random_int);
                count++;
            }

        }

        mActionBar = getSupportActionBar();
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(), 6);
        mActionBar.setTitle(mSectionsPagerAdapter.getPageTitle(0));
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                mActionBar.setTitle(mSectionsPagerAdapter.getPageTitle(position));
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    // Check radio button input
    public boolean checkAnswers(TextView questions, int question ,RadioGroup radioGroup) {
        if (radioGroup.getCheckedRadioButtonId() == -1)
        {
            return false;
        }
        else
        {
            int radioButtonID = radioGroup.getCheckedRadioButtonId();

            RadioButton radioButton = (RadioButton) radioGroup.findViewById(radioButtonID);

            String selectedText = (String) radioButton.getText();

            if(selectedText.matches(questionArr[question].getCapital()))
            {
                questionAnswers[question] = 1;
            }
            else
            {
                questionAnswers[question] = 0;
            }
            Intent reg = new
                    Intent(getApplicationContext(), ResultActivity.class);
            startActivity(reg);
            return true;
        }
    }

    public void loadView(TextView questions, int question ,RadioGroup radioGroup) {
        checkAnswers(questions,question ,radioGroup);
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add(questionArr[question].getCapital());
        arrayList.add(questionArr[question].getCity2());
        arrayList.add(questionArr[question].getCity3());
        Collections.shuffle(arrayList);
        questions.setText(questionArr[question].getState());
        for (int i = 0; i < radioGroup .getChildCount(); i++) {
            ((RadioButton) radioGroup.getChildAt(i)).setText(arrayList.get(i));
        }
    }


    public class SectionsPagerAdapter extends FragmentPagerAdapter {
        private final int mSize;

        public SectionsPagerAdapter(FragmentManager fm, int size) {
            super(fm);
            this.mSize = size;
        }

        @Override
        public Fragment getItem(int position) {
            return PlaceholderFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            return mSize;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            int imageNum = position + 1;
            return String.valueOf("Question " + imageNum);
        }
    }

    public static class PlaceholderFragment extends Fragment {
        private static final String ARG_SECTION_NUMBER = "section_number";
        private int mImageNum;
        private RadioGroup radioGroup;
        private TextView State;

        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public PlaceholderFragment() {
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            if (getArguments() != null) {
                mImageNum = getArguments().getInt(ARG_SECTION_NUMBER);
            } else {
                mImageNum = -1;
            }
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_quiz, container, false);
            State = (TextView) rootView.findViewById(R.id.section_label);
            radioGroup = (RadioGroup) rootView.findViewById(R.id.radiogroup);

            return rootView;
        }


        @Override
        public void onViewCreated(View view, Bundle savedInstanceState) {
            super.onActivityCreated(savedInstanceState);
            if (QuizActivity.class.isInstance(getActivity())) {
                final int resId = mImageNum - 1;
                ((QuizActivity) getActivity()).loadView(State,resId,radioGroup);
                radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

                    public void onCheckedChanged(RadioGroup group, int checkedId) {
                        if (checkedId!= -1) {
                            int radioButtonID = radioGroup.getCheckedRadioButtonId();
                            RadioButton radioButton = (RadioButton) radioGroup.findViewById(radioButtonID);
                            String selectedText = (String) radioButton.getText();
                            if(selectedText.matches(questionArr[resId].getCapital()))
                            {
                                questionAnswers[resId] = 1;
                            }
                            else
                            {
                                questionAnswers[resId] = 0;
                            }
                            if(resId==5){
                                Intent reg = new
                                        Intent(getActivity(), ResultActivity.class);
                                startActivity(reg);
                            }


                        }
                    }
                });
            }
        }
    }
}