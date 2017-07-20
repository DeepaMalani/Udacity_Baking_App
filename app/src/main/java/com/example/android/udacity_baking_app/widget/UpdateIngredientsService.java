package com.example.android.udacity_baking_app.widget;

import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;

import com.example.android.udacity_baking_app.R;

/**
 * Created by Deep on 6/29/2017.
 */

public class UpdateIngredientsService extends IntentService {

    public static final String ACTION_UPDATE_INGREDIENT_WIDGETS = "com.example.android.udacity_baking_app.action.update_ingredient_widgets";
    public static final String EXTRA_RECIPE_ID = "com.example.android.mygarden.extra.RECIPE_ID";

    public UpdateIngredientsService()
    {
        super("UpdateIngredientsService");
    }
    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

        if (intent != null) {
            final String action = intent.getAction();
             if (ACTION_UPDATE_INGREDIENT_WIDGETS.equals(action)) {
                 handleActionUpdateIngredientWidgets();
            }
        }
    }

    /**
     * Starts this service to perform UpdateIngredientWidgets action with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    public static void startActionUpdateIngredientWidgets(Context context) {
        Intent intent = new Intent(context, UpdateIngredientsService.class);
        intent.setAction(ACTION_UPDATE_INGREDIENT_WIDGETS);
        //intent.putExtra(EXTRA_RECIPE_ID, recipeId);
        context.startService(intent);
    }

    /**
     * Handle action UpdateIngredientWidgets in the provided background thread
     */
    private void handleActionUpdateIngredientWidgets() {

        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this, RecipeIngredientsWidget.class));
        //Trigger data update to handle the ingredients widgets and force a data refresh
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.widget_list_view);
        //Now update all widgets
        RecipeIngredientsWidget.updateIngredientWidgets(this, appWidgetManager,appWidgetIds);
    }

}
