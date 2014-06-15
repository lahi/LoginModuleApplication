package com.module.login.app;

import java.security.MessageDigest;
import java.util.Locale;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.support.v7.app.ActionBarActivity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.model.GraphUser;
import com.ppierson.t4jtwitterlogin.T4JTwitterLoginActivity;


public class LoginActivity extends ActionBarActivity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    ViewPager mViewPager;

    private static final int TWITTER_LOGIN_REQUEST_CODE = 400;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);



        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        //facebook
        // Add code to print out the key hash
        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "com.module.login.app",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    //facebook
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.d("TAG", "ON ACTIVITY RESULT!");
        if(requestCode == TWITTER_LOGIN_REQUEST_CODE){ //twitter
            try {
                Log.d("TAG", "TWITTER LOGIN REQUEST CODE");
                if(resultCode == T4JTwitterLoginActivity.TWITTER_LOGIN_RESULT_CODE_SUCCESS){
                    Log.d("TAG", "TWITTER LOGIN SUCCESS");
                }else if(resultCode == T4JTwitterLoginActivity.TWITTER_LOGIN_RESULT_CODE_FAILURE){
                    Log.d("TAG", "TWITTER LOGIN FAIL");
                }else{
                    //
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            //facebook
            Session.getActiveSession().onActivityResult(this, requestCode, resultCode, data);
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return PlaceholderFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 1;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            Locale l = Locale.getDefault();
            switch (position) {
                case 0:
                    return getString(R.string.title_section1).toUpperCase(l);
                case 1:
                    return getString(R.string.title_section2).toUpperCase(l);
                case 2:
                    return getString(R.string.title_section3).toUpperCase(l);
            }
            return null;
        }
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
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
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            final View rootView = inflater.inflate(R.layout.fragment_login, container, false);

            //button
            Button facebookBtn =(Button)rootView.findViewById(R.id.fb_login_btn);
            facebookBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // start Facebook Login
                    Session.openActiveSession(getActivity(), true, new Session.StatusCallback() {

                        // callback when session changes state
                        @Override
                        public void call(Session session, SessionState state, Exception exception) {
                            if (session.isOpened()) {

                                // make request to the /me API
                                Request.newMeRequest(session, new Request.GraphUserCallback() {

                                    // callback after Graph API response with user object
                                    @Override
                                    public void onCompleted(GraphUser user, Response response) {
                                        Log.d("AAA", "USER :::" + user);

                                        if (user != null) {
                                            TextView welcome = (TextView) rootView.findViewById(R.id.section_label);
                                            welcome.setText("Hello " + user.getName() + "!");
                                        }

                                    }
                                }).executeAsync();
                            }
                        }
                    });
                }
            });

            Button twitterBtn = (Button)rootView.findViewById(R.id.twitter_login_btn);
            twitterBtn.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {

                    Log.d("tag", "here");
                    //http://www.localwisdom.com/blog/2013/02/simple-twitter-login-and-tweet-posting-on-android-using-the-twitter4j-library/
                    if (!T4JTwitterLoginActivity.isConnected(getActivity())){

                        Intent twitterLoginIntent = new Intent(getActivity(), T4JTwitterLoginActivity.class);
                        twitterLoginIntent.putExtra(T4JTwitterLoginActivity.TWITTER_CONSUMER_KEY, "Fzf0yX6zkEy9Xv1HgJd7uw");
                        twitterLoginIntent.putExtra(T4JTwitterLoginActivity.TWITTER_CONSUMER_SECRET, "1159G2uqQzVCEgkPUkaGawMlWE5QVg4IZk0Vjzoq90M");
                        startActivityForResult(twitterLoginIntent, TWITTER_LOGIN_REQUEST_CODE);
                    }

                }
            });

            return rootView;
        }
    }

}
