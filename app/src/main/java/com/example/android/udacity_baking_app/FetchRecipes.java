package com.example.android.udacity_baking_app;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.example.android.udacity_baking_app.utilities.NetworkUtils;
import com.example.android.udacity_baking_app.utilities.OpenRecipeJsonUtils;

import java.net.URL;

/**
 * Created by Deep on 6/19/2017.
 */

public class FetchRecipes extends AsyncTask<Void, Void, Void> {
    private static final String TAG = FetchRecipes.class.getSimpleName();
    private Context mContext ;

    FetchRecipes(Context context)
    {
        mContext = context;
    }
    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
    }

    @Override
    protected Void doInBackground(Void... voids) {
        URL recipeRequestUrl = NetworkUtils.buildUrl(mContext);
        try {
            String jsonRecipeResponse = NetworkUtils
                    .getResponseFromHttpUrl(recipeRequestUrl);
            Log.d(TAG,"Results: "+jsonRecipeResponse);
            OpenRecipeJsonUtils.getRecipeDataFromJson(jsonRecipeResponse,mContext);
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return null;
    }
}
