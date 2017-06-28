package com.example.android.udacity_baking_app;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.android.udacity_baking_app.data.RecipeSteps;

import java.util.List;

public class ViewRecipeStepsActivity extends AppCompatActivity {

    private List<RecipeSteps>  mRecipeSteps;
    private int mlistIndex;
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
        }

        // Only create new fragments when there is no previously saved state
        if(savedInstanceState == null) {

            RecipeStepFragment newFragment = new RecipeStepFragment();
            newFragment.setRecipeStepsList(mRecipeSteps);
            newFragment.setRecipeStepsListIndex(mlistIndex);
            // Replace the old  fragment with a new one
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.recipe_step_container, newFragment)
                    .commit();

        }
        Toast.makeText(ViewRecipeStepsActivity.this,String.valueOf(mlistIndex),Toast.LENGTH_SHORT).show();

         mButtonNext = (Button)findViewById(R.id.button_next_step);
        if(mlistIndex == mRecipeSteps.size()-1)
        {
            mButtonNext.setVisibility(View.INVISIBLE);
        }
        else
        {
            mButtonNext.setVisibility(View.VISIBLE);
        }


        mButtonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mlistIndex = mlistIndex + 1;

               if(mlistIndex < mRecipeSteps.size()) {
                   RecipeStepFragment newFragment = new RecipeStepFragment();
                   newFragment.setRecipeStepsList(mRecipeSteps);
                   newFragment.setRecipeStepsListIndex(mlistIndex);
                   // Replace the old  fragment with a new one
                   getSupportFragmentManager().beginTransaction()
                           .replace(R.id.recipe_step_container, newFragment)
                           .commit();
               }
              if(mlistIndex == mRecipeSteps.size() - 1)
              {
                  mButtonNext.setVisibility(View.INVISIBLE);
              }
            }
        });

    }
}
