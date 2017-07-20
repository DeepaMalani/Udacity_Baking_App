package com.example.android.udacity_baking_app;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.android.udacity_baking_app.data.RecipeContract;
import com.example.android.udacity_baking_app.data.RecipeProvider;
import com.example.android.udacity_baking_app.utilities.NetworkUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final String TAG = MainActivity.class.getSimpleName();

    @BindView(R.id.recycler_view_recipe)
    RecyclerView mRecyclerView;
    @BindView(R.id.tv_error_message_display)
    TextView mErrorMessageDisplay;
    @BindView(R.id.button_refresh)
    Button mButtonRefresh;

    private RecipeAdapter mRecipeAdapter;
    public static final int INDEX_RECIPE_ID = 1;
    public static final int INDEX_RECIPE_NAME = 2;
    public static final int INDEX_SERVINGS = 3;
    private static final int RECIPE_LOADER_ID = 0;

    public static final String RECIPE_ID = "recipe_id";
    public static final String RECIPE_NAME = "recipe_name";
    public static final String SERVINGS = "servings";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        boolean isPhone = getResources().getBoolean(R.bool.is_phone);

        //Set recycler view liner layout manager for phones and grid layout manager for tablets
        if (isPhone) {
            //Set linear layout manager for recycler view
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MainActivity.this);
            linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            mRecyclerView.setLayoutManager(linearLayoutManager);
        }
        else
        {
            //Set grid layout manager for recycler view
            GridLayoutManager gridLayoutManager
                    = new GridLayoutManager(MainActivity.this,2);

            gridLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            mRecyclerView.setLayoutManager(gridLayoutManager);
        }

        //RecipeAdapter is responsible for linking the data with recycler views
        mRecipeAdapter = new RecipeAdapter(MainActivity.this,null);
        //Attach adapter to recycler view
        mRecyclerView.setAdapter(mRecipeAdapter);
        mRecipeAdapter.setOnItemClickListener(new RecipeAdapter.RecipeAdapterOnClickHandler() {
                  @Override
                   public void onClick(long recipeId,String recipeName,String servings) {
                      Intent intent = new Intent(MainActivity.this,RecipeDetailActivity.class);
                      intent.putExtra(RECIPE_ID,recipeId);
                      intent.putExtra(RECIPE_NAME,recipeName);
                      intent.putExtra(SERVINGS,servings);
                      startActivity(intent);
                    }
        });
                //Initialize loader
                getSupportLoaderManager().initLoader(RECIPE_LOADER_ID, null, MainActivity.this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Cursor cursor = getContentResolver().query(RecipeProvider.Recipes.CONTENT_URI,
                    null,
                    null
                    ,null,
                    null);
            int countRows = cursor.getCount();
            if (countRows==0)
            {
                if (NetworkUtils.isOnline(MainActivity.this)) {
               /* Load the  data if phone is connected to internet. */
                    FetchRecipes recipes = new FetchRecipes(MainActivity.this);
                    recipes.execute();
                }
                else {
                    showErrorMessage(getString(R.string.network_msg));
                }
            }
            else
            {
                showRecipeDataView();
            }
    }
    /**
     * This method will make the error message visible and hide the recipe list
     * View.
     */
    private void showErrorMessage(String message) {
        /* First, hide the currently visible data */
        mRecyclerView.setVisibility(View.INVISIBLE);
        /* Then, show the error and refresh button*/
        mErrorMessageDisplay.setText(message);
        mErrorMessageDisplay.setVisibility(View.VISIBLE);
        mButtonRefresh.setVisibility(View.VISIBLE);
        //Set refresh button click event
        mButtonRefresh.setOnClickListener(new Button.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                onStart();
            }
        });
    }

    /**
     * This method hide error message and display recipe data
     */
    private void showRecipeDataView() {


        /* First, make sure the error and refresh button is invisible */
        mErrorMessageDisplay.setVisibility(View.INVISIBLE);
        mButtonRefresh.setVisibility(View.INVISIBLE);
        /* Then, make sure the  data is visible */
        mRecyclerView.setVisibility(View.VISIBLE);

    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Uri recipeUri = RecipeProvider.Recipes.CONTENT_URI;
        return new CursorLoader(MainActivity.this,
                recipeUri,
                null,
                null,
                null,
                RecipeContract.RecipeEntry.COLUMN_RECIPE_ID + " ASC");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mRecipeAdapter.swapCursor(data);

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
    mRecipeAdapter.swapCursor(null);
    }


}
