package com.example.android.udacity_baking_app.data;

import net.simonvt.schematic.annotation.AutoIncrement;
import net.simonvt.schematic.annotation.DataType;
import net.simonvt.schematic.annotation.NotNull;
import net.simonvt.schematic.annotation.PrimaryKey;

/**
 * Uses the Schematic (https://github.com/SimonVT/schematic) library to define the columns in a
 * content provider baked by a database
 */

public final class RecipeContract {
    public static final class RecipeEntry
    {
        @DataType(DataType.Type.INTEGER)
        @PrimaryKey
        @AutoIncrement
        public static final String COLUMN_ID = "_id";

        @DataType(DataType.Type.INTEGER)
        @NotNull
        public static final String COLUMN_RECIPE_ID = "recipe_id";

        @DataType(DataType.Type.TEXT)
        @NotNull
        public static final String COLUMN_NAME = "name";

        @DataType(DataType.Type.TEXT)
        @NotNull
        public static final String COLUMN_SERVINGS = "servings";
    }

    public static final class RecipeIngredientEntry
    {
        @DataType(DataType.Type.INTEGER)
        @PrimaryKey
        @AutoIncrement
        public static final String COLUMN_ID = "_id";

        @DataType(DataType.Type.INTEGER)
        @NotNull
        public static final String COLUMN_RECIPE_ID = "recipe_id";

        @DataType(DataType.Type.REAL)
        @NotNull
        public static final String COLUMN_QUANTITY = "quantity";

        @DataType(DataType.Type.TEXT)
        @NotNull
        public static final String COLUMN_MEASURE = "measure";

        @DataType(DataType.Type.TEXT)
        @NotNull
        public static final String COLUMN_INGREDIENT = "ingredient";
    }

    public static final class RecipeStepsEntry
    {
        @DataType(DataType.Type.INTEGER)
        @PrimaryKey
        @AutoIncrement
        public static final String COLUMN_ID = "_id";

        @DataType(DataType.Type.INTEGER)
        @NotNull
        public static final String COLUMN_RECIPE_ID = "recipe_id";

        @DataType(DataType.Type.INTEGER)
        @NotNull
        public static final String COLUMN_RECIPE_STEP_ID = "recipe_step_id";

        @DataType(DataType.Type.TEXT)
        @NotNull
        public static final String COLUMN_SHORT_DESC = "short_description";

        @DataType(DataType.Type.TEXT)
        @NotNull
        public static final String COLUMN_DESCRIPTION = "description";

        @DataType(DataType.Type.TEXT)
        public static final String COLUMN_VIDEO_URL = "video_url";

        @DataType(DataType.Type.TEXT)
        public static final String COLUMN_THUMBNAIL_URL = "thumbnail_url";
    }
}
