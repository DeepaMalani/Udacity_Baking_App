package com.example.android.udacity_baking_app.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.TextView;

import com.example.android.udacity_baking_app.MainActivity;
import com.example.android.udacity_baking_app.R;
import com.example.android.udacity_baking_app.RecipeDetailAdapter;
import com.example.android.udacity_baking_app.data.RecipeSteps;
import com.example.android.udacity_baking_app.data.Recipe_Ingredients;
import com.example.android.udacity_baking_app.loader.IngredientsLoader;
import com.example.android.udacity_baking_app.loader.RecipeStepsLoader;
import com.example.android.udacity_baking_app.widget.UpdateIngredientsService;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Deep on 6/21/2017.
 */

public class RecipeDetailFragment extends Fragment {
    //implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final String TAG = RecipeDetailFragment.class.getSimpleName();

    @BindView(R.id.recycler_view_recipe_steps)
    RecyclerView mRecyclerView;
    @BindView(R.id.text_view_servings)
    TextView mServings;

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
    public static  String RECIPE_ID_PREF_NAME = "recipe_id_pref";

    public RecipeDetailFragment() {

    }

    // Define a new interface OnImageClickListener that triggers a callback in the host activity
    OnRecipeStepClickListener mCallback;

    // OnImageClickListener interface, calls a method in the host activity named onImageSelected
    public interface OnRecipeStepClickListener {
        void onRecipeStepSelected(List<RecipeSteps> recipeSteps,int position,String recipeName);
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

        ButterKnife.bind(this, rootView);
        mRecipeStep = new ArrayList<RecipeSteps>();
        mIngredients = new ArrayList<Recipe_Ingredients>();

        Intent intent = getActivity().getIntent();
        if (intent != null && intent.hasExtra(MainActivity.RECIPE_ID)) {
            mRecipeId = intent.getLongExtra(MainActivity.RECIPE_ID, 0);
            final String recipeName = intent.getStringExtra(MainActivity.RECIPE_NAME);

            String servings = getString(R.string.servings) + ": " + intent.getStringExtra(MainActivity.SERVINGS);
            mServings.setText(servings);

            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
            linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            mRecyclerView.setLayoutManager(linearLayoutManager);
            //RecipeStepsAdapter is  for linking the data with recycler views
            mRecipeStepsAdapter = new RecipeDetailAdapter(mIngredients, mRecipeStep, getContext());
            //Attach adapter to recycler
            mRecyclerView.setAdapter(mRecipeStepsAdapter);
            //On click listener
            mRecipeStepsAdapter.setOnItemClickListener(new RecipeDetailAdapter.RecipeStepsOnClickHandler() {
                @Override
                public void onClick(List<RecipeSteps> recipeSteps, int position) {

                    // Trigger the callback method and pass in the position that was clicked
                    mCallback.onRecipeStepSelected(recipeSteps, position,recipeName);
                }
            });
            //Set activity label for selected recipe
            getActivity().setTitle(recipeName);

            saveRecipeIdAndNameInPreference(mRecipeId,recipeName,servings);
        }
        return rootView;
    }

    private void saveRecipeIdAndNameInPreference(long recipeId,String recipeName,String servings)
    {
        SharedPreferences.Editor editor =getActivity().getSharedPreferences(RECIPE_ID_PREF_NAME, MODE_PRIVATE).edit();
        editor.putLong("recipeId", recipeId);
        editor.putString("recipeName", recipeName);
        editor.putString("servings",servings);
        editor.commit();
        UpdateIngredientsService.startActionUpdateIngredientWidgets(getActivity());
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
