package com.example.android.udacity_baking_app;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.example.android.udacity_baking_app.data.RecipeSteps;

public class RecipeDetailActivity extends AppCompatActivity implements RecipeDetailFragment.OnRecipeStepClickListener{

    // Track whether to display a two-pane or single-pane UI
    // A single-pane display refers to phone screens, and two-pane to larger tablet screens
    private boolean mTwoPane;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);

        // Determine if you're creating a two-pane or single-pane display
        if(findViewById(R.id.recipe_step_linear_layout) != null) {

            // This LinearLayout will only initially exist in the two-pane tablet case
            mTwoPane = true;
            // Getting rid of the "Next" button that appears on phones for launching a separate activity
            // Button nextButton = (Button) findViewById(R.id.next_button);
            // nextButton.setVisibility(View.GONE);

            if(savedInstanceState == null) {
                // In two-pane mode, add initial description fragment to the screen
                FragmentManager fragmentManager = getSupportFragmentManager();

                // Creating a new head fragment
                RecipeStepFragment descriptionFragment = new RecipeStepFragment();

                // Add the fragment to its container using a transaction
                fragmentManager.beginTransaction()
                        .add(R.id.recipe_step_instruction_container, descriptionFragment)
                        .commit();
            }
        } else {
            // We're in single-pane mode and displaying fragments on a phone in separate activities
            mTwoPane = false;
        }
    }

    @Override
    public void onRecipeStepSelected(RecipeSteps recipeSteps) {

        Toast.makeText(RecipeDetailActivity.this,"Recipe Steps",Toast.LENGTH_SHORT).show();
    }
}
