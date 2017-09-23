package wail.splacher.com.splasher.utils;

import android.animation.Animator;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.animation.AccelerateDecelerateInterpolator;

/**
 * Created by wail babou on 2017-09-17.
 */

public class SplasherAnimation {
    View view;
    public SplasherAnimation(View view){
        this.view=view;
    }
    public void animationBottomRight(){
        int startRadius = 0;
        // get the final radius for the clipping circle
        int endRadius = (int) Math.hypot(view.getWidth(), view.getHeight());
        // Android native animator
        Animator animator ;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            animator = ViewAnimationUtils.createCircularReveal(view, view.getRight(), view.getBottom(), startRadius,endRadius);
            animator.setInterpolator(new AccelerateDecelerateInterpolator());
            animator.setDuration(2000);
            animator.start();
        }
    }
    public void animationBottomLeft(){
        int startRadius = 0;
        // get the final radius for the clipping circle
        int endRadius = (int) Math.hypot(view.getWidth(), view.getHeight());
        // Android native animator
        Animator animator ;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            animator = ViewAnimationUtils.createCircularReveal(view, view.getLeft(), view.getBottom(), startRadius,endRadius);
            animator.setInterpolator(new AccelerateDecelerateInterpolator());
            animator.setDuration(Const.ANIMATION_DURATION);
            animator.start();
        }
    }
    public void animationTopLeft(){
        int startRadius = 0;
        // get the final radius for the clipping circle
        int endRadius = (int) Math.hypot(view.getWidth(), view.getHeight());
        // Android native animator
        Animator animator ;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            animator = ViewAnimationUtils.createCircularReveal(view, view.getLeft(), view.getTop(), startRadius,endRadius);
            animator.setInterpolator(new AccelerateDecelerateInterpolator());
            animator.setDuration(Const.ANIMATION_DURATION);
            animator.start();
        }
    }
    public void animationTopRight(){
        int startRadius = 0;
        // get the final radius for the clipping circle
        int endRadius = (int) Math.hypot(view.getWidth(), view.getHeight());
        // Android native animator
        Animator animator ;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            animator = ViewAnimationUtils.createCircularReveal(view, view.getRight(), view.getTop(), startRadius,endRadius);
            animator.setInterpolator(new AccelerateDecelerateInterpolator());
            animator.setDuration(Const.ANIMATION_DURATION);
            animator.start();
        }
    }
    public void animationCenter(){
        int startRadius = 0;
        // get the final radius for the clipping circle
        int endRadius = (int) Math.hypot(view.getWidth(), view.getHeight());
        // Android native animator
        int X = Math.abs((view.getRight()-view.getLeft())/2);
        int Y = Math.abs((view.getBottom()-view.getTop())/2);

        Animator animator ;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            animator = ViewAnimationUtils.createCircularReveal(view, X, Y, startRadius,endRadius);
            animator.setInterpolator(new AccelerateDecelerateInterpolator());
            animator.setDuration(Const.ANIMATION_DURATION);
            animator.start();
        }
    }
}
