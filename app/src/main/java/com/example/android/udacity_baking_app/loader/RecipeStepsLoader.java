package com.example.android.udacity_baking_app.loader;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.content.AsyncTaskLoader;

import com.example.android.udacity_baking_app.fragments.RecipeDetailFragment;
import com.example.android.udacity_baking_app.data.RecipeContract;
import com.example.android.udacity_baking_app.data.RecipeProvider;
import com.example.android.udacity_baking_app.data.RecipeSteps;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Deep on 6/22/2017.
 */

public class RecipeStepsLoader extends AsyncTaskLoader<List<RecipeSteps>> {

    private final String LOG_TAG = RecipeStepsLoader.class.getSimpleName();
    private final Context mContext;
    private final long mRecipeId;
    private List<RecipeSteps> mRecipeStep;

    public RecipeStepsLoader(Context context,long recipeId) {
        super(context);
        mContext = context;
        mRecipeId = recipeId;
    }

    @Override
    public List<RecipeSteps> loadInBackground() {
           mRecipeStep  = new ArrayList<RecipeSteps>();
           Uri recipeUri = RecipeProvider.Steps.CONTENT_URI;
           Cursor recipeStepCursor = mContext.getContentResolver().query(
           recipeUri,
            null,
            RecipeContract.RecipeStepsEntry.COLUMN_RECIPE_ID + " = ?",
             new String[] {String.valueOf(mRecipeId)},
             RecipeContract.RecipeStepsEntry.COLUMN_RECIPE_STEP_ID
           );

        for(recipeStepCursor.moveToFirst(); !recipeStepCursor.isAfterLast(); recipeStepCursor.moveToNext()) {
            // The Cursor is now set to the right position
            String shortDesc = recipeStepCursor.getString(RecipeDetailFragment.INDEX_SHORT_DESC);
            String description = recipeStepCursor.getString(RecipeDetailFragment.INDEX_DESCRIPTION);
            String video_url =  recipeStepCursor.getString(RecipeDetailFragment.INDEX_VIDEO_URL);
            String thumbnailUrl = recipeStepCursor.getString(RecipeDetailFragment.INDEX_THUMBNAIL_URL);
            mRecipeStep.add(new RecipeSteps(shortDesc,description,video_url,thumbnailUrl));
        }
        return mRecipeStep;
    }

    @Override
    public void deliverResult(List<RecipeSteps> data) {
        mRecipeStep = data;
        super.deliverResult(data);
    }
}
