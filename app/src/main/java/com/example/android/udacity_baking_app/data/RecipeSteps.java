package com.example.android.udacity_baking_app.data;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Deep on 6/21/2017.
 */

public class RecipeSteps implements Parcelable {

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

    /**
     * Create a new recipe step from a data Parcel
     */
    protected RecipeSteps(Parcel in) {

        this.shortDesc = in.readString();
        this.description = in.readString();
        this.videoUrl = in.readString();
        this.thumbnailUrl = in.readString();
    }
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {

        parcel.writeString(shortDesc);
        parcel.writeString(description);
        parcel.writeString(videoUrl);
        parcel.writeString(thumbnailUrl);

    }

    public static final Creator<RecipeSteps> CREATOR = new Creator<RecipeSteps>() {
        @Override
        public RecipeSteps createFromParcel(Parcel in) {
            return new RecipeSteps(in);
        }

        @Override
        public RecipeSteps[] newArray(int size) {
            return new RecipeSteps[size];
        }
    };
}
