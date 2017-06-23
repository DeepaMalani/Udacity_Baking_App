package com.example.android.udacity_baking_app.data;

/**
 * Created by Deep on 6/22/2017.
 */

public class Recipe_Ingredients {
    public final long recipeId;
    public final String quantity;
    public final String measure;
    public final String ingredient;

    public Recipe_Ingredients(long recipeId, String quantity, String measure, String ingredient)
    {
        this.recipeId = recipeId;
        this.quantity = quantity;
        this.measure = measure;
        this.ingredient = ingredient;
    }
}
