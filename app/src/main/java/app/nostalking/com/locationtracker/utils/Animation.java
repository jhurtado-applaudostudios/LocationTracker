package app.nostalking.com.locationtracker.utils;

import android.view.View;

/**
 * Created by Juan Hurtado on 5/21/2015.
 */
public abstract class Animation {
    public static final int DIRECTION_LEFT = 1;
    public static final int DIRECTION_RIGHT = 2;
    public static final int DIRECTION_UP = 3;
    public static final int DIRECTION_DOWN = 4;

    public static final int DURATION_DEFAULT = 300;
    public static final int DURATION_SHORT = 100;
    public static final int DURATION_LONG = 500;

    View view;

    public abstract void animate();
}
