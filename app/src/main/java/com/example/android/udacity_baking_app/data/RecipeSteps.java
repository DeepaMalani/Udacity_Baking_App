package com.example.android.udacity_baking_app.data;

/**
 * Created by Deep on 6/21/2017.
 */

public class RecipeSteps  {

    public final String shortDesc;
    public final String description;
    public final String videoUrl;
    public final String thumbnailUrl;

    /**
     * Create recipe steps from discrete values
     */
    public RecipeSteps(String shortDesc,String description,String videoUrl,String thumbnailUrl)
    {
      this.shortDesc = shortDesc;
      this.description = description;
      this.videoUrl = videoUrl;
      this.thumbnailUrl = thumbnailUrl;
    }

}
