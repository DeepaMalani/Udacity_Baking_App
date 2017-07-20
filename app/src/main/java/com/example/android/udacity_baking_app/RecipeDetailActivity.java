package com.example.android.udacity_baking_app;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import com.example.android.udacity_baking_app.data.RecipeSteps;
import com.example.android.udacity_baking_app.fragments.RecipeDetailFragment;
import com.example.android.udacity_baking_app.fragments.RecipeStepFragment;

import java.util.ArrayList;
import java.util.List;

public class RecipeDetailActivity extends AppCompatActivity implements RecipeDetailFragment.OnRecipeStepClickListener{

    // Track whether to display a two-pane or single-pane UI
    // A single-pane display refers to phone screens, and two-pane to larger tablet screens
    private boolean mTwoPane;
    // Final Strings to store state information about the recipe step object
    public static final String RECIPE_STEP_LIST = "recipe_step_list";
    public static final String RECIPE_STEP_LIST_INDEX = "list_index";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);

        Intent intent = getIntent();
        // Determine if you're creating a two-pane or single-pane display
        if(findViewById(R.id.recipe_step_linear_layout) != null) {

            // This LinearLayout will only initially exist in the two-pane tablet case
            mTwoPane = true;
            if(savedInstanceState == null) {
                // In two-pane mode, add initial description fragment to the screen
                FragmentManager fragmentManager = getSupportFragmentManager();

                // Creating a new recipe step fragment
                RecipeStepFragment recipeStepFragment = new RecipeStepFragment();

                // Add the fragment to its container using a transaction
                fragmentManager.beginTransaction()
                        .add(R.id.recipe_step_container, recipeStepFragment)
                        .commit();
            }
        } else {
            // We're in single-pane mode and displaying fragments on a phone in separate activities
            mTwoPane = false;
        }


    }

    @Override
    public void onRecipeStepSelected(List<RecipeSteps> recipeSteps,int position,String recipeName) {


        // Handle the two-pane case and replace existing fragments right when a new recipe step is selected from the master list
        if (mTwoPane) {
            // Create two=pane interaction

            RecipeStepFragment newFragment = new RecipeStepFragment();
            newFragment.setRecipeStepsList(recipeSteps);
            newFragment.setRecipeStepsListIndex(position);
            newFragment.setRecipeName(recipeName);
            newFragment.setTwoPane(true);
            // Replace the old  fragment with a new one
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.recipe_step_container, newFragment)
                    .commit();

        }

        // Handle the single-pane phone case by passing information in a Bundle attached to an Intent
        else
        {
            Bundle bundle = new Bundle();
            bundle.putParcelableArrayList(RECIPE_STEP_LIST, (ArrayList<RecipeSteps>) recipeSteps);
            bundle.putInt(RECIPE_STEP_LIST_INDEX,position);
            bundle.putString(MainActivity.RECIPE_NAME,recipeName);
            // Attach the Bundle to an intent
            final Intent intent = new Intent(this, ViewRecipeStepsActivity.class);
            intent.putExtras(bundle);
            startActivity(intent);

        }
    }
}
