package rs.webnet.splitscreenanim;

import android.app.Activity;
import android.graphics.Point;
import android.util.DisplayMetrics;
import android.view.Display;

/**
 * Created by kursulla on 4/4/14.
 * <p/>
 * Class purpose:
 */
public class DeviceUtil {
    public static int getScreenWidth(Activity activity){
        Display display = activity.getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        return width;
    }
    public static int getScreenHeight(Activity activity){
        Display display = activity.getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int height = size.y;
        return height;
    }
    public static float getScreenDencity(Activity activity){
        DisplayMetrics metrics = activity.getResources().getDisplayMetrics();
        return metrics.density;

    }
}

