package com.example.android.udacity_baking_app.data;

import android.net.Uri;

import net.simonvt.schematic.annotation.ContentProvider;
import net.simonvt.schematic.annotation.ContentUri;
import net.simonvt.schematic.annotation.TableEndpoint;

/**
 * Uses the Schematic (https://github.com/SimonVT/schematic) to create a content provider and
 * define
 * URIs for the provider
 */

@ContentProvider(
        authority = RecipeProvider.AUTHORITY,
        database = RecipeDataBase.class)
public class RecipeProvider {
    public static final String AUTHORITY = "com.example.android.udacity_baking_app";


    @TableEndpoint(table = RecipeDataBase.RECIPES)
    public static class Recipes {

        @ContentUri(
                path = "recipes",
                type = "vnd.android.cursor.dir/recipes",
                defaultSort = RecipeContract.RecipeEntry.COLUMN_RECIPE_ID + " DESC")
        public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/recipes");
    }

    @TableEndpoint(table = RecipeDataBase.RECIPE_INGREDIENTS)
    public static class Ingredients {

        @ContentUri(
                path = "recipe_ingredients",
                type = "vnd.android.cursor.dir/recipe_ingredients",
                defaultSort = RecipeContract.RecipeIngredientEntry.COLUMN_RECIPE_ID + " DESC")
        public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/recipe_ingredients");
    }

    @TableEndpoint(table = RecipeDataBase.RECIPE_STEPS)
    public static class Steps {

        @ContentUri(
                path = "recipe_steps",
                type = "vnd.android.cursor.dir/recipe_steps",
                defaultSort = RecipeContract.RecipeStepsEntry.COLUMN_ID + " DESC")
        public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/recipe_steps");
    }
}
