package wail.splacher.com.splasher.models;

import android.graphics.Typeface;
import android.view.View;

import com.daimajia.androidanimations.library.Techniques;

import wail.splacher.com.splasher.utils.Const;

/**
 * Created by wail babou on 2017-09-17.
 */

public class SplasherConfig {
    //Circular Reveal
    private int reveal_start= Const.START_BOTTOM_RIGHT;
    //ImageViews
    private int logo=Const.NOT_SET;
    private Techniques logo_animation;
    private int logoWidth =Const.NOT_SET;

    //Titles
    private String title;
    private int titleColor=Const.NOT_SET;
    private Techniques titleAnimation;
    private String subtitle;
    private int subtitleColor=Const.NOT_SET;
    private Techniques subtitleAnimation;

    //Typefaces
    private Typeface titleTypeFace;
    private Typeface subtitleTypeFace;

    //Text size
    private int titleSize=Const.NOT_SET;
    private int subtitleSize=Const.NOT_SET;

    //Custom splash
    private int customView=Const.NOT_SET;

    public int getReveal_start() {
        return reveal_start;
    }

    public SplasherConfig setReveal_start(int reveal_start) {
        this.reveal_start = reveal_start;
        return this;
    }
    public SplasherConfig setAnimationDuration(int duration){
        Const.ANIMATION_DURATION=duration;
        return this;
    }
    public SplasherConfig setAnimationLogoDuration(int duration){
        Const.ANIMATION_LOGO_DURATION=duration;
        return this;
    }

    public int getLogo() {
        return logo;
    }

    public SplasherConfig setLogo(int logo) {
        this.logo = logo;
        return this;
    }

    public Techniques getLogo_animation() {
        return logo_animation;
    }

    public SplasherConfig setLogo_animation(Techniques logo_animation) {
        this.logo_animation = logo_animation;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public SplasherConfig setTitle(String title) {
        this.title = title;
        return this;
    }

    public int getTitleColor() {
        return titleColor;
    }

    public SplasherConfig setTitleColor(int titleColor) {
        this.titleColor = titleColor;
        return this;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public SplasherConfig setSubtitle(String subtitle) {
        this.subtitle = subtitle;
        return this;
    }

    public int getSubtitleColor() {
        return subtitleColor;
    }

    public SplasherConfig setSubtitleColor(int subtitleColor) {
        this.subtitleColor = subtitleColor;
        return this;
    }

    public Techniques getTitleAnimation() {
        return titleAnimation;
    }

    public SplasherConfig setTitleAnimation(Techniques titleAnimation) {
        this.titleAnimation = titleAnimation;
        return this;
    }

    public Techniques getSubtitleAnimation() {
        return subtitleAnimation;
    }

    public SplasherConfig setSubtitleAnimation(Techniques subtitleAnimation) {
        this.subtitleAnimation = subtitleAnimation;
        return this;
    }

    public Typeface getTitleTypeFace() {
        return titleTypeFace;
    }

    public SplasherConfig setTitleTypeFace(Typeface titleTypeFace) {
        this.titleTypeFace = titleTypeFace;
        return this;
    }

    public Typeface getSubtitleTypeFace() {
        return subtitleTypeFace;
    }

    public SplasherConfig setSubtitleTypeFace(Typeface subtitleTypeFace) {
        this.subtitleTypeFace = subtitleTypeFace;
        return this;
    }

    public int getCustomView() {
        return customView;
    }

    public SplasherConfig setCustomView(int customView) {
        this.customView = customView;
        return this;
    }

    public int getTitleSize() {
        return titleSize;
    }

    public SplasherConfig setTitleSize(int titleSize) {
        this.titleSize = titleSize;
        return this;
    }

    public int getSubtitleSize() {
        return subtitleSize;
    }

    public SplasherConfig setSubtitleSize(int subtitleSize) {
        this.subtitleSize = subtitleSize;
        return this;
    }

    public int getLogoWidth() {
        return logoWidth;
    }

    public SplasherConfig setLogoWidth(int logoWidth) {
        this.logoWidth = logoWidth;
        return this;
    }
}
