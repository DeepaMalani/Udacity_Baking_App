package com.example.android.udacity_baking_app;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by Deep on 6/20/2017.
 */

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecipeAdapterViewHolder>{

    private Context mContex;
    private Cursor mCursor;
      /*
        * An on-click handler that we've defined to make it easy for an Activity to interface with
        * our RecyclerView
        */
    private static RecipeAdapterOnClickHandler mClickHandler;

    public RecipeAdapter(Context context,Cursor cursor) {
        mContex = context;
        mCursor = cursor;

    }

    // Define the method that allows the parent activity  to define the listener
    public void setOnItemClickListener(RecipeAdapterOnClickHandler clickHandler) {
        mClickHandler = clickHandler;
    }

    /**
     * The interface that receives onClick messages.
     */
    public interface RecipeAdapterOnClickHandler {
        void onClick(long recipeId,String recipeName,String servings);
    }
    /**
     * Cache of the children views for a list item.
     */
    public class RecipeAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final TextView mRecipeName;
        RecipeAdapterViewHolder(View view)
        {
            super(view);
            mRecipeName = (TextView)view.findViewById(R.id.text_view_recipe_name);
            view.setOnClickListener(this);
        }
        @Override
        public void onClick(View view) {
            int adapterPosition = getAdapterPosition() ;
            mCursor.moveToPosition(adapterPosition );
            long recipeId = mCursor.getLong(MainActivity.INDEX_RECIPE_ID);
            String recipeName = mCursor.getString(MainActivity.INDEX_RECIPE_NAME);
            String servings =  mCursor.getString(MainActivity.INDEX_SERVINGS);

            mClickHandler.onClick(recipeId,recipeName,servings);
        }
    }

    @Override
    public int getItemCount() {
        if (null == mCursor) return 0;
        return mCursor.getCount();
    }

    @Override
    public RecipeAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        mContex = viewGroup.getContext();
        int layoutIdForGridItem = R.layout.recipe_list_item;
        LayoutInflater inflater = LayoutInflater.from(mContex);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForGridItem, viewGroup, shouldAttachToParentImmediately);
        return new RecipeAdapterViewHolder(view);
    }



    @Override
    public void onBindViewHolder(RecipeAdapterViewHolder recipeAdapterHolder, int position) {
        mCursor.moveToPosition(position);
        String recipeName = mCursor.getString(MainActivity.INDEX_RECIPE_NAME);
        recipeAdapterHolder.mRecipeName.setText(recipeName);

    }
    /**
     * Swaps the cursor used by the RecipeAdapter for its  data. This method is called by
     * MainActivity after a load has finished, as well as when the Loader responsible for loading
     * the  data is reset. When this method is called, we assume we have a completely new
     * set of data, so we call notifyDataSetChanged to tell the RecyclerView to update.
     *
     * @param newCursor the new cursor to use as Adapter's data source
     */
    void swapCursor(Cursor newCursor) {
        if(newCursor!=null) {
            mCursor = newCursor;
            notifyDataSetChanged();
        }
    }
}
