package com.example.android.udacity_baking_app.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.media.session.MediaButtonReceiver;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.android.udacity_baking_app.MainActivity;
import com.example.android.udacity_baking_app.R;
import com.example.android.udacity_baking_app.RecipeDetailActivity;
import com.example.android.udacity_baking_app.data.RecipeSteps;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static com.example.android.udacity_baking_app.RecipeDetailActivity.RECIPE_STEP_LIST_INDEX;


/**
 * Created by Deep on 6/22/2017.
 */

public class RecipeStepFragment extends Fragment implements ExoPlayer.EventListener{
    /**
     * Inflates the fragment layout file and sets the correct resource for the text to display
     */
    // Tag for logging
    private static final String TAG = RecipeSteps.class.getSimpleName();

    @BindView(R.id.playerView) SimpleExoPlayerView mPlayerView;
    @BindView(R.id.text_view_description) TextView mRecipeDescription;
    @BindView(R.id.button_previous) Button mButtonPrevious;
    @BindView(R.id.button_next) Button mButtonNext;

    private SimpleExoPlayer mExoPlayer;
    private Unbinder unbinder;
    private List<RecipeSteps> mRecipeSteps;
    private int mListIndex;
    private String mRecipeName;
    private boolean mTwoPane = true;
    private static final String TWO_PANE = "two_pane";
    private int mState ;

    private static MediaSessionCompat mMediaSession;
    private PlaybackStateCompat.Builder mStateBuilder;

    public  RecipeStepFragment()
    {

    }
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        View rootView = inflater.inflate(R.layout.fragment_view_recipe_step,container,false);
        unbinder = ButterKnife.bind(this, rootView);

        // Load the saved state if there is one
        if(savedInstanceState != null) {
            mRecipeSteps = savedInstanceState.getParcelableArrayList(RecipeDetailActivity.RECIPE_STEP_LIST);
            mListIndex   = savedInstanceState.getInt(RecipeDetailActivity.RECIPE_STEP_LIST_INDEX);
            mTwoPane  = savedInstanceState.getBoolean(TWO_PANE);
            mRecipeName = savedInstanceState.getString(MainActivity.RECIPE_NAME);


        }
        if(mRecipeSteps!=null) {

            String videoUrl = mRecipeSteps.get(mListIndex).videoUrl;
            String thumbnailURL = mRecipeSteps.get(mListIndex).thumbnailUrl;

            if(videoUrl.isEmpty() && (!thumbnailURL.isEmpty()))
            {
               videoUrl = thumbnailURL;
            }

            if(videoUrl.isEmpty())
            {
                mPlayerView.setDefaultArtwork(BitmapFactory.decodeResource
                        (getResources(), R.drawable.video_image));

            }

          // Initialize the Media Session.
           initializeMediaSession();
            // Initialize the player.
           initializePlayer(Uri.parse(videoUrl));

            //Set description text
            mRecipeDescription.setText(mRecipeSteps.get(mListIndex).description);

        }
        //If recipe steps is null set player view invisible
        if(mRecipeSteps == null)
        {
            mPlayerView.setVisibility(View.INVISIBLE);
        }
        else
            mPlayerView.setVisibility(View.VISIBLE);

        //For tablets set next and previous button invisible
        if(mTwoPane) {
            mButtonPrevious.setVisibility(View.GONE);
            mButtonNext.setVisibility(View.GONE);
        }
        else {
            //Set next button click listener
            if (mListIndex == mRecipeSteps.size() - 1) {
                mButtonNext.setVisibility(View.GONE);
            } else {
                mButtonNext.setVisibility(View.VISIBLE);
                mButtonNext.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        viewNextRecipeStep();
                    }
                });
            }
                if (mListIndex == 0) {
                    mButtonPrevious.setVisibility(View.GONE);
                } else {
                    mButtonPrevious.setVisibility(View.VISIBLE);
                    //Set previous button click listener
                    mButtonPrevious.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            viewPreviousRecipeStep();
                        }
                    });
                }
            //Set activity label for selected recipe
            getActivity().setTitle(mRecipeName);
        }

        return rootView;
    }

    //Play video when app is in foreground.
    @Override
    public void onResume() {
        super.onResume();
        if(mRecipeSteps!=null) {
            mExoPlayer.setPlayWhenReady(true);
        }
    }

    //Pause the player when away from activity.
    @Override
    public void onPause() {
        super.onPause();
        if(mRecipeSteps!=null) {
            mExoPlayer.setPlayWhenReady(false);
        }
    }

    private void viewNextRecipeStep()
     {
              mListIndex = mListIndex + 1;
              if(mListIndex < mRecipeSteps.size()) {
                   RecipeStepFragment newFragment = new RecipeStepFragment();
                   newFragment.setRecipeStepsList(mRecipeSteps);
                   newFragment.setRecipeStepsListIndex(mListIndex);
                   newFragment.setRecipeName(mRecipeName);
                   newFragment.setTwoPane(false);
                   // Replace the old  fragment with a new one
                  getActivity().getSupportFragmentManager().beginTransaction()
                           .replace(R.id.recipe_step_container, newFragment)
                           .commit();
               }
              if(mListIndex == mRecipeSteps.size() - 1)
              {
                  mButtonNext.setVisibility(View.GONE);
              }

  }

  private void viewPreviousRecipeStep()
    {
        mListIndex = mListIndex - 1;
        if(mListIndex >= 0) {
            RecipeStepFragment newFragment = new RecipeStepFragment();
            newFragment.setRecipeStepsList(mRecipeSteps);
            newFragment.setRecipeStepsListIndex(mListIndex);
            newFragment.setRecipeName(mRecipeName);
            newFragment.setTwoPane(false);
            // Replace the old  fragment with a new one
            getActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.recipe_step_container, newFragment)
                    .commit();
        }
        if(mListIndex == 0)
        {
            mButtonPrevious.setVisibility(View.GONE);
        }
    }

    /**
     * Initializes the Media Session to be enabled with media buttons, transport controls, callbacks
     * and media controller.
     */
    private void initializeMediaSession() {

        // Create a MediaSessionCompat.
        mMediaSession = new MediaSessionCompat(getContext(), TAG);

        // Enable callbacks from MediaButtons and TransportControls.
        mMediaSession.setFlags(
                MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS |
                        MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS);

        // Do not let MediaButtons restart the player when the app is not visible.
        mMediaSession.setMediaButtonReceiver(null);

        // Set an initial PlaybackState with ACTION_PLAY, so media buttons can start the player.
        mStateBuilder = new PlaybackStateCompat.Builder()
                .setActions(
                        PlaybackStateCompat.ACTION_PLAY |
                                PlaybackStateCompat.ACTION_PAUSE |
                                PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS |
                                PlaybackStateCompat.ACTION_PLAY_PAUSE);

        mMediaSession.setPlaybackState(mStateBuilder.build());


        // MySessionCallback has methods that handle callbacks from a media controller.
        mMediaSession.setCallback(new MySessionCallback());

        // Start the Media Session since the activity is active.
        mMediaSession.setActive(true);



    }


    /**
     * Initialize ExoPlayer.
     *
     * @param mediaUri The URI of the sample to play.
     */
    private void initializePlayer(Uri mediaUri) {
        if (mExoPlayer == null) {
            // Create an instance of the ExoPlayer.
            TrackSelector trackSelector = new DefaultTrackSelector();
            LoadControl loadControl = new DefaultLoadControl();
            mExoPlayer = ExoPlayerFactory.newSimpleInstance(getContext(), trackSelector, loadControl);
            mPlayerView.setPlayer(mExoPlayer);
            // Prepare the MediaSource.
            String userAgent = Util.getUserAgent(getContext(),getString( R.string.app_name));
            MediaSource mediaSource = new ExtractorMediaSource(mediaUri, new DefaultDataSourceFactory(
                    getContext(), userAgent), new DefaultExtractorsFactory(), null, null);
            mExoPlayer.prepare(mediaSource);
            mExoPlayer.setPlayWhenReady(true);
        }
    }


    /**
     * Release ExoPlayer.
     */
    private void releasePlayer() {
        mExoPlayer.stop();
        mExoPlayer.release();
        mExoPlayer = null;
    }

    // Setter methods for recipe step
    public void setRecipeStepsList(List<RecipeSteps> recipeSteps) {
        mRecipeSteps = recipeSteps;
    }

    // Setter methods for recipe step
    public void setRecipeStepsListIndex(int position) {
        mListIndex = position;

    }

    // Setter methods for recipe step
    public void setRecipeName(String recipeName) {
        mRecipeName = recipeName;
    }

    public void setTwoPane(boolean twoPane)
    {
        mTwoPane = twoPane;
    }

    /**
     * Save the current state of this fragment
     */
    @Override
    public void onSaveInstanceState(Bundle currentState) {
        //currentState.putParcelable(RecipeDetailActivity.RECIPE_STEP_LIST, mRecipeSteps.get(mListIndex));
        currentState.putParcelableArrayList(RecipeDetailActivity.RECIPE_STEP_LIST, (ArrayList<RecipeSteps>) mRecipeSteps);
        currentState.putInt(RECIPE_STEP_LIST_INDEX,mListIndex);
        currentState.putBoolean(TWO_PANE,mTwoPane);
        currentState.putString(MainActivity.RECIPE_NAME,mRecipeName);


    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        if(mRecipeSteps!=null) {
            releasePlayer();
            mMediaSession.setActive(false);
        }
    }

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {

        if ((playbackState == ExoPlayer.STATE_READY) && playWhenReady) {
            mStateBuilder.setState(PlaybackStateCompat.STATE_PLAYING,
                    mExoPlayer.getCurrentPosition(), 1f);
        } else if ((playbackState == ExoPlayer.STATE_READY)) {
            mStateBuilder.setState(PlaybackStateCompat.STATE_PAUSED,
                    mExoPlayer.getCurrentPosition(), 1f);
        }
        mMediaSession.setPlaybackState(mStateBuilder.build());

    }

    @Override
    public void onTimelineChanged(Timeline timeline, Object manifest) {

    }

    @Override
    public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

    }

    @Override
    public void onLoadingChanged(boolean isLoading) {

    }

    @Override
    public void onPlayerError(ExoPlaybackException error) {

    }

    @Override
    public void onPositionDiscontinuity() {

    }

    /**
     * Media Session Callbacks, where all external clients control the player.
     */
    private class MySessionCallback extends MediaSessionCompat.Callback {
        @Override
        public void onPlay() {
            mExoPlayer.setPlayWhenReady(true);
        }

        @Override
        public void onPause() {
            mExoPlayer.setPlayWhenReady(false);
        }

        @Override
        public void onSkipToPrevious() {
            mExoPlayer.seekTo(0);
        }
    }
    /**
     * Broadcast Receiver registered to receive the MEDIA_BUTTON intent coming from clients.
     */
    public static class MediaReceiver extends BroadcastReceiver {

        public MediaReceiver() {
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            MediaButtonReceiver.handleIntent(mMediaSession, intent);
        }
    }
}
