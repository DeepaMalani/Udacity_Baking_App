package com.example.android.udacity_baking_app.loader;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.content.AsyncTaskLoader;

import com.example.android.udacity_baking_app.fragments.RecipeDetailFragment;
import com.example.android.udacity_baking_app.data.RecipeContract;
import com.example.android.udacity_baking_app.data.RecipeProvider;
import com.example.android.udacity_baking_app.data.Recipe_Ingredients;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Deep on 6/22/2017.
 */

public class IngredientsLoader extends AsyncTaskLoader<List<Recipe_Ingredients>> {

    private final String LOG_TAG = RecipeStepsLoader.class.getSimpleName();
    private final Context mContext;
    private final long mRecipeId;
    private List<Recipe_Ingredients> mIngredient;
    public IngredientsLoader(Context context,long recipeId) {
        super(context);
        mContext = context;
        mRecipeId = recipeId;
    }

    @Override
    public List<Recipe_Ingredients> loadInBackground() {
        mIngredient  = new ArrayList<Recipe_Ingredients>();
        Uri ingredientUri = RecipeProvider.Ingredients.CONTENT_URI;
        Cursor ingredientCursor = mContext.getContentResolver().query(
                ingredientUri,
                null,
                RecipeContract.RecipeIngredientEntry.COLUMN_RECIPE_ID + " = ?",
                new String[] {String.valueOf(mRecipeId)},
                null
        );

        for(ingredientCursor.moveToFirst(); !ingredientCursor.isAfterLast(); ingredientCursor.moveToNext()) {
            // The Cursor is now set to the right position
            String quantity = ingredientCursor.getString(RecipeDetailFragment.INDEX_QUANTITY);
            String measure = ingredientCursor.getString(RecipeDetailFragment.INDEX_MEASURE);
            String ingredients =  ingredientCursor.getString(RecipeDetailFragment.INDEX_INGREDIENTS);

            mIngredient.add(new Recipe_Ingredients(mRecipeId,quantity,measure,ingredients));
        }
        return mIngredient;
    }

    @Override
    public void deliverResult(List<Recipe_Ingredients> data) {
        super.deliverResult(data);
        mIngredient = data;
    }


}
