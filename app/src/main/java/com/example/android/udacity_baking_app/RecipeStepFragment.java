package com.example.android.udacity_baking_app;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Deep on 6/22/2017.
 */

public class RecipeStepFragment extends Fragment {
    /**
     * Inflates the fragment layout file and sets the correct resource for the text to display
     */

    public  RecipeStepFragment()
    {

    }
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_recipe_step,container,false);
        return rootView;
    }
}
