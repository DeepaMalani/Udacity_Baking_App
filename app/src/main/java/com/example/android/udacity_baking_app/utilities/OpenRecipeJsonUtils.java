package com.example.android.udacity_baking_app.utilities;

import android.content.ContentValues;
import android.content.Context;

import com.example.android.udacity_baking_app.data.RecipeContract;
import com.example.android.udacity_baking_app.data.RecipeProvider;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Utility functions to handle Recipe JSON data.
 */

   public final class OpenRecipeJsonUtils {
    private static final String TAG = OpenRecipeJsonUtils.class.getSimpleName();


    /**
     * Take the String representing the complete recipe data in JSON Format and
     * pull out the data we need to construct the Strings needed for the wireframes.
     * @param recipeJsonStr
     * @param context
     * @return
     * @throws JSONException
     */
    public static void getRecipeDataFromJson(String recipeJsonStr, Context context)
           throws JSONException {
        //These are the name of the objects, we need to be extract.
        final String ID = "id";
        final String RECIPE_NAME = "name";
        final String INGREDIENTS = "ingredients";
        final String QUANTITY = "quantity";
        final String MEASURE= "measure";
        final String INGREDIENT = "ingredient";
        final String STEPS = "steps";
        final String SHORT_DESC = "shortDescription";
        final String DESCRIPTION = "description";
        final String VIDEO_URL = "videoURL";
        final String THUMBNAIL_URL = "thumbnailURL";
        final String SERVINGS = "servings";

        ContentValues[] ingredientContentValues;
        ContentValues[] stepContentValues;

        JSONArray recipeArray = new JSONArray(recipeJsonStr);
        for (int i = 0; i < recipeArray.length(); i++) {

         JSONObject recipeJson = recipeArray.getJSONObject(i);
         long recipe_id = recipeJson.getLong(ID);
        String recipe_name = recipeJson.getString(RECIPE_NAME);
        String servings =  recipeJson.getString(SERVINGS);


        ContentValues recipeContentValue = new ContentValues();
        recipeContentValue.put(RecipeContract.RecipeEntry.COLUMN_RECIPE_ID,recipe_id);
        recipeContentValue.put(RecipeContract.RecipeEntry.COLUMN_NAME,recipe_name);
        recipeContentValue.put(RecipeContract.RecipeEntry.COLUMN_SERVINGS,servings);

        context.getContentResolver().insert(RecipeProvider.Recipes.CONTENT_URI,recipeContentValue);

            //Get ingredients array from recipe Json object
            JSONArray ingredientArray = recipeJson.getJSONArray(INGREDIENTS);
             ingredientContentValues = new ContentValues[ingredientArray.length()];
            //Get steps array from recipe Json object
            JSONArray stepArray = recipeJson.getJSONArray(STEPS);
            stepContentValues = new ContentValues[stepArray.length()];
            //Get data from ingredient array
            for (int j = 0; j < ingredientArray.length(); j++) {

                double quantity;
                String measure;
                String ingredient;

                // Get the JSON object representing the ingredient result
                JSONObject resultIngredient = ingredientArray.getJSONObject(j);
                quantity = resultIngredient.getDouble(QUANTITY);
                measure = resultIngredient.getString(MEASURE);
                ingredient = resultIngredient.getString(INGREDIENT);

                ContentValues ingredientValues = new ContentValues();
                ingredientValues.put(RecipeContract.RecipeIngredientEntry.COLUMN_RECIPE_ID, recipe_id);
                ingredientValues.put(RecipeContract.RecipeIngredientEntry.COLUMN_QUANTITY, quantity);
                ingredientValues.put(RecipeContract.RecipeIngredientEntry.COLUMN_MEASURE, measure);
                ingredientValues.put(RecipeContract.RecipeIngredientEntry.COLUMN_INGREDIENT, ingredient);

                ingredientContentValues[j] = ingredientValues;
                //context.getContentResolver().insert(RecipeProvider.Ingredients.CONTENT_URI,ingredientValues);

            }

            //Get data from recipe steps array
            for (int k = 0; k < stepArray.length(); k++) {

                long id;
                String shortDescription;
                String description;
                String videoURL;
                String thumbnailURL;

                // Get the JSON object representing the ingredient result
                JSONObject resultRecipeSteps = stepArray.getJSONObject(k);
                id = resultRecipeSteps.getLong(ID);
                shortDescription = resultRecipeSteps.getString(SHORT_DESC);
                description = resultRecipeSteps.getString(DESCRIPTION);
                videoURL = resultRecipeSteps.getString(VIDEO_URL);
                thumbnailURL = resultRecipeSteps.getString(THUMBNAIL_URL);

                ContentValues stepValues = new ContentValues();
                stepValues.put(RecipeContract.RecipeStepsEntry.COLUMN_RECIPE_ID, recipe_id);
                stepValues.put(RecipeContract.RecipeStepsEntry.COLUMN_RECIPE_STEP_ID, id);
                stepValues.put(RecipeContract.RecipeStepsEntry.COLUMN_SHORT_DESC, shortDescription);
                stepValues.put(RecipeContract.RecipeStepsEntry.COLUMN_DESCRIPTION, description);
                stepValues.put(RecipeContract.RecipeStepsEntry.COLUMN_VIDEO_URL, videoURL);
                stepValues.put(RecipeContract.RecipeStepsEntry.COLUMN_THUMBNAIL_URL, thumbnailURL);

                stepContentValues[k] = stepValues;
               // context.getContentResolver().insert(RecipeProvider.Steps.CONTENT_URI, stepValues);

            }

            String uri = String.valueOf(RecipeProvider.Ingredients.CONTENT_URI);
            if (ingredientContentValues != null && ingredientContentValues.length != 0) {
            /* Insert recipe ingredients into table */
                    context.getContentResolver().bulkInsert(RecipeProvider.Ingredients.CONTENT_URI, ingredientContentValues);

                }
                if (stepContentValues != null && stepContentValues.length != 0) {
            /* Insert recipe ingredients into table */
                    context.getContentResolver().bulkInsert(RecipeProvider.Steps.CONTENT_URI, stepContentValues);

                }


       }
    }
}
