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

import com.example.android.udacity_baking_app.data.RecipeContract;
import com.example.android.udacity_baking_app.data.RecipeProvider;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final String TAG = MainActivity.class.getSimpleName();
    private RecyclerView mRecyclerView;
    private RecipeAdapter mRecipeAdapter;
    public static final int INDEX_RECIPE_ID = 1;
    public static final int INDEX_RECIPE_NAME = 2;
    private static final int RECIPE_LOADER_ID = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        boolean isPhone = getResources().getBoolean(R.bool.is_phone);
       //Get recycler view reference from xml
        mRecyclerView = (RecyclerView)findViewById(R.id.recycler_view_recipe);
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
                   public void onClick(long recipeId) {
                      Intent intent = new Intent(MainActivity.this,RecipeDetailActivity.class);
                      intent.putExtra(Intent.EXTRA_TEXT,recipeId);
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
            FetchRecipes recipes = new FetchRecipes(MainActivity.this);
            recipes.execute();
        }
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
