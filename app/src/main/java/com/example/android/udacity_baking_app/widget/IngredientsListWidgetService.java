package com.example.android.udacity_baking_app.widget;

import android.content.Intent;
import android.widget.RemoteViewsService;

/**
 * Created by Deep on 6/29/2017.
 */

public class IngredientsListWidgetService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new IngredientsListRemoteViewsFactory(this.getApplicationContext());
    }
}
