package rs.webnet.splitscreenanim;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.Interpolator;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;

/**
 * Created by Nebojsa Bozic  on 4/10/14.
 * <p/>
 * Class purpose:
 */
public class SplitScreenEffect {
    public static final int DIRECTION_LEFT = -1;
    public static final int DIRECTION_RIGHT = 1;
    private ViewGroup container;
    private ImageView background;
    private ImageView leftMask;
    private ImageView rightMask;
    private Activity activity;
    private Interpolator interpolator;
    private int animationDuration = 100;

    /**
     * C-tor for SplitScreenEffect. Pass int current activity and root view of your activity.
     * @param activity Activity that should be used for starting another activity and setting values in lib.
     * @param container Root container of your activity.
     */
    public SplitScreenEffect(Activity activity, ViewGroup container){
        this.activity = activity;
        this.container = container;
        this.interpolator = new AccelerateDecelerateInterpolator();
    }

    /**
     * C-tor for SplitScreenEffect. Pass int current activity and root view of your activity.
     * @param activity Activity that should be used for starting another activity and setting values in lib.
     * @param container Root container of your activity.
     * @param animationDuration set animation duration in milliseconds. 500ms is default value;
     */
    public SplitScreenEffect(Activity activity, ViewGroup container, int animationDuration){
        this.activity = activity;
        this.container = container;
        this.animationDuration = animationDuration;
        this.interpolator = new AccelerateDecelerateInterpolator();
    }

    /**
     * Start new activity with SplitScreen Animation. You should pass in intent with transparent background for getting full effect.
     * @param intent Intent you want to start with animation.
     */
    public void startActivity(final Intent intent) {
        Bitmap screenBitmap = this.getScreenBitmap(this.container);

        this.container.addView(this.createBackground());

        int width = DeviceUtil.getScreenWidth(activity);
        int height = DeviceUtil.getScreenHeight(activity);

        createLeftMask(screenBitmap, width, height);
        createRightMask(screenBitmap, width, height);
        screenBitmap.recycle();//Has to be after createLeftMaskImageView and createRightMaskImageView

        startLeftMaskAnimation(intent, width);
        startRightMaskAnimation(width);
    }


    private ImageView createBackground() {
        background = new ImageView(activity);
        background.setBackgroundColor(Color.BLACK);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT);
        background.setLayoutParams(params);
        return background;
    }
    private Bitmap getScreenBitmap(ViewGroup container) {
        View containersRootView = container.getRootView();
        Bitmap bitmap = null;
        if (containersRootView != null) {
            containersRootView.setDrawingCacheEnabled(true);
            bitmap = Bitmap.createBitmap(containersRootView.getDrawingCache());
            containersRootView.setDrawingCacheEnabled(false);
        }
        return bitmap;
    }

    private Animation generateInAnimationLeft(int width, int duration) {
        return generateAnimation(width, duration, DIRECTION_LEFT);
    }
    private Animation generateInAnimationRight(int width, int duration) {
        return generateAnimation(width, duration, DIRECTION_RIGHT);
    }
    private Animation generateAnimation(int width, int duration, int direction) {
        Animation animation = new TranslateAnimation(0, direction * (width / 3), 0, 0);
        animation.setDuration(duration);
        animation.setFillAfter(true);
        animation.setFillEnabled(true);
        animation.setInterpolator(interpolator);
        return animation;
    }

    private void createRightMask(Bitmap screenBitmap, int width, int height) {
        Bitmap right = Bitmap.createBitmap(screenBitmap, width / 2, 0, width / 2, height);
        this.rightMask = this.createRightMaskImageView(right);
        this.container.addView(this.rightMask);
    }
    private void createLeftMask(Bitmap screenBitmap, int width, int height) {
        Bitmap left = Bitmap.createBitmap(screenBitmap, 0, 0, width / 2, height);
        this.leftMask = this.createLeftMaskImageView(left);
        this.container.addView(this.leftMask);
    }
    private ImageView createLeftMaskImageView(Bitmap bitmap) {
        ImageView imageViewMask = new ImageView(activity);
        imageViewMask.setImageBitmap(bitmap);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.FILL_PARENT);
        params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        imageViewMask.setLayoutParams(params);
        return imageViewMask;
    }
    private ImageView createRightMaskImageView(Bitmap bitmap) {
        ImageView imageViewMask = new ImageView(activity);
        imageViewMask.setImageBitmap(bitmap);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.FILL_PARENT);
        params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        imageViewMask.setLayoutParams(params);
        return imageViewMask;
    }
    private void startRightMaskAnimation(int width) {
        Animation animationToRight = this.generateInAnimationRight(width, animationDuration);
        this.rightMask.startAnimation(animationToRight);
    }
    private void startLeftMaskAnimation(Intent intent, int width) {
        Animation animationToLeft = this.generateInAnimationLeft(width, animationDuration);
        animationToLeft.setAnimationListener(createAnimationStartedListener(intent));
        this.leftMask.startAnimation(animationToLeft);
    }


    /**
     * Call in onResume method of activity to complete closing part of SplitScreen animation.
     */
    public void onResume() {
        if (animationPerformedBeforeActivityHitedOnResume()) {
            this.finishAnimation(container);
        }
    }

    private boolean animationPerformedBeforeActivityHitedOnResume() {
        return this.leftMask != null && this.rightMask != null;
    }
    private void finishAnimation(final ViewGroup container) {

        float delta = calculateMasksDelta();

        Animation animationForLeftMask = generateOutAnimationForLeftMask(delta);
        Animation animationForRightMask = generateOutAnimationForRightMask(delta);

        animationForLeftMask.setAnimationListener(createAnimationFinishedListener(container));

        this.leftMask.startAnimation(animationForLeftMask);
        this.rightMask.startAnimation(animationForRightMask);
    }

    private Animation generateOutAnimationForRightMask(float delta) {
        Animation animationToLeft = new TranslateAnimation(delta / 2, -delta / 2, 0, 0);
        animationToLeft.setDuration(animationDuration);
        animationToLeft.setFillAfter(true);
        animationToLeft.setFillEnabled(true);
        animationToLeft.setInterpolator(interpolator);
        return animationToLeft;
    }
    private Animation generateOutAnimationForLeftMask(float delta) {
        Animation animationToRight = new TranslateAnimation(0, delta, 0, 0);
        animationToRight.setDuration(animationDuration);
        animationToRight.setFillAfter(true);
        animationToRight.setFillEnabled(true);
        animationToRight.setInterpolator(interpolator);
        return animationToRight;
    }

    private float calculateMasksDelta() {
        int width = DeviceUtil.getScreenWidth(activity);
        float delta = width / 3;
        this.leftMask.setX(-delta);
        this.rightMask.setX(2 * delta);
        return delta;
    }
    private Animation.AnimationListener createAnimationStartedListener(final Intent intent) {
        return new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                activity.startActivity(intent);
                activity.overridePendingTransition(R.anim.dialog_enter, R.anim.dialog_exit);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        };
    }
    private Animation.AnimationListener createAnimationFinishedListener(final ViewGroup container) {
        return new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                container.removeView(background);
                container.removeView(leftMask);
                container.removeView(rightMask);

                leftMask = null;
                rightMask = null;
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        };
    }

    public int getAnimationDuration() {
        return animationDuration;
    }
    public void setAnimationDuration(int animationDuration) {
        this.animationDuration = animationDuration;
    }
}
