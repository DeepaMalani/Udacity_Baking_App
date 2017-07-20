package com.example.android.udacity_baking_app;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.udacity_baking_app.data.RecipeSteps;
import com.example.android.udacity_baking_app.data.Recipe_Ingredients;

import java.util.List;

/**
 * Created by Deep on 6/22/2017.
 */

public class RecipeDetailAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private final int VIEW_TYPE_INGREDIENTS_HEADER = 0;
    private final int VIEW_TYPE_INGREDIENTS = 1;
    private final int VIEW_TYPE_RECIPE_STEPS_HEADER = 2;
    private final int VIEW_TYPE_RECIPE_STEP = 3;


    private List<RecipeSteps> recipeSteps;
    private List<Recipe_Ingredients> ingredients;
    private Context context;

   public RecipeDetailAdapter(List<Recipe_Ingredients> ingredients,List<RecipeSteps> recipeSteps,Context context)
   {
       this.recipeSteps = recipeSteps;
       this.ingredients = ingredients;
       this.context = context;
   }

    @Override
    public int getItemViewType(int position) {

        if(position == 0)
        {
            return VIEW_TYPE_INGREDIENTS_HEADER;
        }
        if ( position > 0 && position <= ingredients.size()) {

            return VIEW_TYPE_INGREDIENTS;
        }

            if (position == ingredients.size()+1)
                return VIEW_TYPE_RECIPE_STEPS_HEADER;

        else {
            if ((position-1) - ingredients.size() <= recipeSteps.size()) {
                return VIEW_TYPE_RECIPE_STEP;
            }
        }
        return -1;
    }

    @Override
    public int getItemCount() {
        return 1 + ingredients.size()+ 1 + recipeSteps.size();
    }
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        switch (holder.getItemViewType()) {

            case VIEW_TYPE_INGREDIENTS:
                ViewHolderIngredient viewHolderIngredient = (ViewHolderIngredient) holder;
                String quantity = ingredients.get(position - 1).quantity + " ";
                String measure = ingredients.get(position - 1).measure + " ";
                String ingredient = ingredients.get(position - 1).ingredient;
                viewHolderIngredient.mRecipeIngredients.setText(quantity + measure + ingredient);
                break;
            case VIEW_TYPE_RECIPE_STEP:
                ViewHolderRecipeSteps viewHolderRecipeSteps = (ViewHolderRecipeSteps) holder;
                String recipeShortDesc = recipeSteps.get(position -2 - ingredients.size()).shortDesc;
                viewHolderRecipeSteps.mRecipeShortDesc.setText(recipeShortDesc);

        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (viewType == VIEW_TYPE_INGREDIENTS_HEADER) {
            View itemView = LayoutInflater.from(context)
                    .inflate(R.layout.ingredient_list_header, parent, false);
            return new ViewHolderIngredient(itemView);
        }

         else if (viewType == VIEW_TYPE_INGREDIENTS) {
            View itemView = LayoutInflater.from(context)
                    .inflate(R.layout.ingredient_list_item, parent, false);
            return new ViewHolderIngredient(itemView);

        }
        else if (viewType == VIEW_TYPE_RECIPE_STEPS_HEADER) {

            View itemView = LayoutInflater.from(context)
                    .inflate(R.layout.recipe_steps_header, parent, false);
            return new ViewHolderIngredient(itemView);
        }
        else {
            View itemView = LayoutInflater.from(context)
                    .inflate(R.layout.recipe_step_list, parent, false);
            return new ViewHolderRecipeSteps(itemView);
        }
    }



    public class ViewHolderIngredient extends RecyclerView.ViewHolder {

        private final TextView mRecipeIngredients;
        public ViewHolderIngredient(View itemView) {
            super(itemView);
            mRecipeIngredients = (TextView) itemView.findViewById(R.id.recipe_ingredient);
        }
    }
        /*
      * An on-click handler that we've defined to make it easy for an Activity to interface with
      * our RecyclerView
      */
    private static RecipeStepsOnClickHandler mClickHandler;

    // Define the method that allows the parent activity  to define the listener
    public void setOnItemClickListener(RecipeStepsOnClickHandler clickHandler) {
        mClickHandler = clickHandler;
    }

    /**
     * The interface that receives onClick messages.
     */
    public interface RecipeStepsOnClickHandler {
        void onClick(List<RecipeSteps> recipeStepsList, int position);
    }
    public class ViewHolderRecipeSteps extends RecyclerView.ViewHolder implements View.OnClickListener{

        private final TextView mRecipeShortDesc;
        public ViewHolderRecipeSteps(View itemView) {
            super(itemView);
            mRecipeShortDesc = (TextView)itemView.findViewById(R.id.text_view_short_description);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {

            //To get recipe step position minus ingredient header, steps header and ingredients list
            int adapterPosition = getAdapterPosition() - 2 - ingredients.size() ;

            mClickHandler.onClick(recipeSteps,adapterPosition);
        }
    }
    public void updateIngredients(List<Recipe_Ingredients> ingredients) {
        this.ingredients = ingredients;
        notifyDataSetChanged();
    }

    public void updateRecipeSteps(List<RecipeSteps> recipeSteps) {
        this.recipeSteps = recipeSteps;
        notifyDataSetChanged();
    }

    }



