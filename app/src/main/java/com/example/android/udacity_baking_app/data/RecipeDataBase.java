package com.example.android.udacity_baking_app.data;

import net.simonvt.schematic.annotation.Database;
import net.simonvt.schematic.annotation.Table;

/**
 * Uses the Schematic (https://github.com/SimonVT/schematic) library to create a database with one
 * table for messages
 */

@Database(version = RecipeDataBase.VERSION)
public class RecipeDataBase {
    public static final int VERSION = 2;

    @Table(RecipeContract.RecipeEntry.class)
    public static final String RECIPES = "recipes";

    @Table(RecipeContract.RecipeIngredientEntry.class)
    public static final String RECIPE_INGREDIENTS = "recipe_ingredients";

    @Table(RecipeContract.RecipeStepsEntry.class)
    public static final String RECIPE_STEPS = "recipe_steps";
}
