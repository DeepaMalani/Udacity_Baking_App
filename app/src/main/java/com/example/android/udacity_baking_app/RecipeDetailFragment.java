package com.example.android.udacity_baking_app;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.android.udacity_baking_app.data.RecipeSteps;
import com.example.android.udacity_baking_app.data.Recipe_Ingredients;
import com.example.android.udacity_baking_app.loader.IngredientsLoader;
import com.example.android.udacity_baking_app.loader.RecipeStepsLoader;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Deep on 6/21/2017.
 */

public class RecipeDetailFragment extends Fragment {
    //implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final String TAG = RecipeDetailFragment.class.getSimpleName();
    private RecyclerView mRecyclerView;
    private long mRecipeId;
    private RecipeDetailAdapter mRecipeStepsAdapter;
    //Declare loader id for ingredients and recipe steps
    private static final int INGREDIENTS_LOADER_ID = 1;
    private static final int RECIPE_STEP_LOADER_ID = 2;

    //Column index for recipe steps
    public static final int INDEX_SHORT_DESC = 3;
    public static final int INDEX_DESCRIPTION = 4;
    public static final int INDEX_VIDEO_URL = 5;
    public static final int INDEX_THUMBNAIL_URL = 6;

    //Column index for recipe ingredient
    public static final int INDEX_QUANTITY = 2;
    public static final int INDEX_MEASURE = 3;
    public static final int INDEX_INGREDIENTS = 4;

    //Declare list for recipe steps and ingredients;
    private List<RecipeSteps> mRecipeStep;
    private List<Recipe_Ingredients> mIngredients;


    public RecipeDetailFragment() {

    }

    // Define a new interface OnImageClickListener that triggers a callback in the host activity
    OnRecipeStepClickListener mCallback;

    // OnImageClickListener interface, calls a method in the host activity named onImageSelected
    public interface OnRecipeStepClickListener {
        void onRecipeStepSelected(RecipeSteps recipeSteps);
    }

    // Override onAttach to make sure that the container activity has implemented the callback
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        // This makes sure that the host activity has implemented the callback interface
        // If not, it throws an exception
        try {
            mCallback = (OnRecipeStepClickListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement OnImageClickListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle bundle) {

        View rootView = inflater.inflate(R.layout.fragment_recipe_detail, container, false);

        mRecipeStep = new ArrayList<RecipeSteps>();
        mIngredients = new ArrayList<Recipe_Ingredients>();

        Intent intent = getActivity().getIntent();
        if (intent != null && intent.hasExtra(Intent.EXTRA_TEXT)) {
            mRecipeId = intent.getLongExtra(Intent.EXTRA_TEXT, 0);
            Toast.makeText(getActivity(), "Id: " + String.valueOf(mRecipeId), Toast.LENGTH_SHORT).show();
        }
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view_recipe_steps);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        //RecipeStepsAdapter is  for linking the data with recycler views
        mRecipeStepsAdapter = new RecipeDetailAdapter(mIngredients, mRecipeStep,getContext());
        //Attach adapter to recycler
        mRecyclerView.setAdapter(mRecipeStepsAdapter);
        //On click listener
        mRecipeStepsAdapter.setOnItemClickListener(new RecipeDetailAdapter.RecipeStepsOnClickHandler()
        {
            @Override
            public void onClick(RecipeSteps recipeSteps) {

                // Trigger the callback method and pass in the position that was clicked
                mCallback.onRecipeStepSelected(recipeSteps);
            }
        });
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //Initialize loader
        getLoaderManager().initLoader(INGREDIENTS_LOADER_ID, null, ingredientsLoaderListener).forceLoad();
        getLoaderManager().initLoader(RECIPE_STEP_LOADER_ID, null, recipeStepsLoaderListener).forceLoad();

    }

    private LoaderManager.LoaderCallbacks<List<Recipe_Ingredients>> ingredientsLoaderListener;

    {
        ingredientsLoaderListener = new LoaderManager.LoaderCallbacks<List<Recipe_Ingredients>>() {

            @Override
            public Loader<List<Recipe_Ingredients>> onCreateLoader(int id, Bundle args) {
                return new IngredientsLoader(getContext(), mRecipeId);
            }

            @Override
            public void onLoadFinished(Loader<List<Recipe_Ingredients>> loader, List<Recipe_Ingredients> ingredients) {

                mIngredients = ingredients;
                mRecipeStepsAdapter.updateIngredients(ingredients);
            }

            @Override
            public void onLoaderReset(Loader<List<Recipe_Ingredients>> loader) {

            }
        };
    }

    private LoaderManager.LoaderCallbacks<List<RecipeSteps>> recipeStepsLoaderListener;

    {
        recipeStepsLoaderListener = new LoaderManager.LoaderCallbacks<List<RecipeSteps>>() {

            @Override
            public Loader<List<RecipeSteps>> onCreateLoader(int id, Bundle args) {

                return new RecipeStepsLoader(getContext(), mRecipeId);
            }

            @Override
            public void onLoadFinished(Loader<List<RecipeSteps>> loader, List<RecipeSteps> recipeSteps) {

                mRecipeStep = recipeSteps;
                mRecipeStepsAdapter.updateRecipeSteps(recipeSteps);
            }

            @Override
            public void onLoaderReset(Loader<List<RecipeSteps>> loader) {

            }
        };
    }


}
