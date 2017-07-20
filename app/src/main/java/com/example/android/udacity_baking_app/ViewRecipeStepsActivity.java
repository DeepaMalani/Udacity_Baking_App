package com.example.android.udacity_baking_app;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.Button;

import com.example.android.udacity_baking_app.data.RecipeSteps;
import com.example.android.udacity_baking_app.fragments.RecipeStepFragment;

import java.util.List;

public class ViewRecipeStepsActivity extends AppCompatActivity {

    private List<RecipeSteps>  mRecipeSteps;
    private int mlistIndex;
    private String mRecipeName;
    private Button mButtonNext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_recipe_steps);
        Intent intent = getIntent();
        if(intent!= null && intent.hasExtra(RecipeDetailActivity.RECIPE_STEP_LIST))
        {
            mRecipeSteps = intent.getParcelableArrayListExtra(RecipeDetailActivity.RECIPE_STEP_LIST);
            mlistIndex = intent.getIntExtra(RecipeDetailActivity.RECIPE_STEP_LIST_INDEX,0);
            mRecipeName = intent.getStringExtra(MainActivity.RECIPE_NAME);
         }

        // Only create new fragments when there is no previously saved state
        if(savedInstanceState == null) {

            RecipeStepFragment newFragment = new RecipeStepFragment();
            newFragment.setRecipeStepsList(mRecipeSteps);
            newFragment.setRecipeStepsListIndex(mlistIndex);
            newFragment.setRecipeName(mRecipeName);
            newFragment.setTwoPane(false);
            // Replace the old  fragment with a new one
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.recipe_step_container, newFragment)
                    .commit();

        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id)
        {
            case android.R.id.home:
                // This takes the user 'back', as if they pressed the left-facing triangle icon on the main android toolbar.
                ViewRecipeStepsActivity.this.onBackPressed();
                return true;

        }
        return super.onOptionsItemSelected(item);
    }
}
