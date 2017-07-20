package com.example.android.udacity_baking_app.widget;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.example.android.udacity_baking_app.MainActivity;
import com.example.android.udacity_baking_app.R;
import com.example.android.udacity_baking_app.data.RecipeContract;
import com.example.android.udacity_baking_app.data.RecipeProvider;
import com.example.android.udacity_baking_app.fragments.RecipeDetailFragment;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Deep on 6/29/2017.
 */

public class IngredientsListRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {

    private Context mContext;
    private Cursor mCursor;
    private long mRecipeId;
    private String mRecipeName;
    private String mServings;

    public IngredientsListRemoteViewsFactory(Context context)
    {
        mContext = context;
    }
    @Override
    public void onCreate() {

    }

    @Override
    public int getCount() {
       if(mCursor==null) return 0;
        return mCursor.getCount();
    }

    //called on start and when notifyAppWidgetViewDataChanged is called
    @Override
    public void onDataSetChanged() {

        Uri ingredientUri = RecipeProvider.Ingredients.CONTENT_URI;
        getRecipeIdAndName();
        if (mCursor != null) mCursor.close();
        mCursor = mContext.getContentResolver().query(
                ingredientUri,
                null,
                RecipeContract.RecipeIngredientEntry.COLUMN_RECIPE_ID + " = ?",
                new String[] {String.valueOf(mRecipeId)},
                null
        );
    }

    private void getRecipeIdAndName()
    {
        SharedPreferences prefs = mContext.getSharedPreferences(RecipeDetailFragment.RECIPE_ID_PREF_NAME, MODE_PRIVATE);
        mRecipeId = prefs.getLong("recipeId",0);
        mRecipeName = prefs.getString("recipeName","");
        mServings = prefs.getString("servings","");
    }
    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public RemoteViews getViewAt(int position) {
       if(mCursor==null || mCursor.getCount()==0) return null;
        mCursor.moveToPosition(position);
        int quantityIndex = mCursor.getColumnIndex(RecipeContract.RecipeIngredientEntry.COLUMN_QUANTITY);
        int measureIndex = mCursor.getColumnIndex(RecipeContract.RecipeIngredientEntry.COLUMN_MEASURE);
        int ingredientIndex   = mCursor.getColumnIndex(RecipeContract.RecipeIngredientEntry.COLUMN_INGREDIENT);

        String quantity = mCursor.getString(quantityIndex) + " ";
        String measure =   mCursor.getString(measureIndex) + " ";
        String ingredient = mCursor.getString(ingredientIndex);

        RemoteViews views = new RemoteViews(mContext.getPackageName(), R.layout.recipe_ingredients_widget);
        // Set text for text view
        views.setTextViewText(R.id.widget_text_view_ingredients, quantity+measure+ingredient);

        // Fill in the onClick PendingIntent Template using the specific plant Id for each item individually
        Bundle extras = new Bundle();
        extras.putLong(MainActivity.RECIPE_ID, mRecipeId);
        extras.putString(MainActivity.RECIPE_NAME,mRecipeName);
        extras.putString(MainActivity.SERVINGS,mServings);
        Intent fillInIntent = new Intent();
        fillInIntent.putExtras(extras);
        views.setOnClickFillInIntent(R.id.widget_text_view_ingredients, fillInIntent);



        return  views;

    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public void onDestroy() {

         mCursor.close();
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }
}
