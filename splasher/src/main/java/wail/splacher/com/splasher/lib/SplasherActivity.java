package wail.splacher.com.splasher.lib;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.daimajia.androidanimations.library.YoYo;

import java.util.ArrayList;
import java.util.Collection;

import wail.splacher.com.splasher.R;
import wail.splacher.com.splasher.models.SplasherConfig;
import wail.splacher.com.splasher.utils.Const;
import wail.splacher.com.splasher.utils.SplasherAnimation;


/**
 * Created by wail babou on 2016-06-27.
 */
public abstract class SplasherActivity extends AppCompatActivity {
    private ImageView logo;
    private Animation anim1;
    private static int SPLASH_TIME_OUT = 6000;
    private TextView appname,byname;
    private RelativeLayout all;
    private SplasherConfig config;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        config = new SplasherConfig();
        initSplasher(config);
        initView();
    }
    public void initView(){
        setContentView(R.layout.splash_activity);
        logo= (ImageView) findViewById(R.id.flag);
        all= (RelativeLayout) findViewById(R.id.all);
        appname= (TextView) findViewById(R.id.appname);
        byname= (TextView) findViewById(R.id.byname);

        if(config.getCustomView()==Const.NOT_SET){
            initFinish();
            initReveal();
            initLogo();
            initTitle();
        }else {
            initCustom();
            initReveal();
            initFinish();
        }
    }
    public void initReveal(){
        all.post(new Runnable() {
            @Override
            public void run() {
                //create your anim here
                switch (config.getReveal_start()){
                    case Const.START_BOTTOM_RIGHT:
                        new SplasherAnimation(all).animationBottomRight();
                        break;
                    case Const.START_BOTTOM_LEFT_:
                        new SplasherAnimation(all).animationBottomLeft();
                        break;
                    case Const.START_TOP_LEFT:
                        new SplasherAnimation(all).animationTopLeft();
                        break;
                    case Const.START_TOP_RIGHT:
                        new SplasherAnimation(all).animationTopRight();
                        break;
                    case Const.START_CENTER:
                        new SplasherAnimation(all).animationCenter();
                        break;
                }
            }
        });
    }
    public void initLogo(){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(config.getLogo()!=Const.NOT_SET){
                    logo.setImageResource(config.getLogo());
                    if(config.getLogo_animation()!=null){
                        YoYo.with(config.getLogo_animation())
                                .duration(Const.ANIMATION_LOGO_DURATION)
                                .playOn(logo);
                    }
                    if(config.getLogoWidth()!=Const.NOT_SET){
                        RelativeLayout.LayoutParams layoutParams
                                = new RelativeLayout.LayoutParams(config.getLogoWidth(), config.getLogoWidth());
                        logo.setLayoutParams(layoutParams);
                    }
                }
            }
        }, 1000);
    }
    public void initTitle(){
        appname.setText(config.getTitle());
        byname.setText(config.getSubtitle());
        if(config.getTitleColor()!=Const.NOT_SET){
            appname.setTextColor(config.getTitleColor());
        }
        if(config.getTitleAnimation()!=null){
            YoYo.with(config.getTitleAnimation())
                    .duration(Const.ANIMATION_LOGO_DURATION)
                    .playOn(appname);
        }
        if(config.getSubtitleColor()!=Const.NOT_SET){
            byname.setTextColor(config.getSubtitleColor());
        }
        if(config.getSubtitleAnimation()!=null){
            YoYo.with(config.getSubtitleAnimation())
                    .duration(Const.ANIMATION_LOGO_DURATION)
                    .playOn(byname);
        }
        // init typeface
        if(config.getTitleTypeFace()!=null){
            Typeface typeface = config.getTitleTypeFace();
            appname.setTypeface(typeface);
        }
        if(config.getSubtitleTypeFace()!=null){
            Typeface typeface = config.getSubtitleTypeFace();
            byname.setTypeface(typeface);
        }
        // init Size
        if(config.getTitleSize()!=Const.NOT_SET){
            appname.setTextSize(config.getTitleSize());
        }
        if(config.getSubtitleSize()!=Const.NOT_SET){
            byname.setTextSize(config.getSubtitleSize());
        }
    }
    public void initFinish(){
        ArrayList<Integer> durations = new ArrayList<>();
        durations.add(Const.ANIMATION_DURATION);
        durations.add(Const.ANIMATION_LOGO_DURATION);
        durations.add(1000);
        int duration = max(durations);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
               onSplasherFinished();
            }
        }, duration);
    }
    public Integer max(final Collection<Integer> ints) {
        Integer max = Integer.MIN_VALUE;
        for (Integer integer : ints) {
            max = Math.max(max, integer);
        }
        return max;
    }
    public void initCustom(){
        all.removeAllViews();
        LayoutInflater vi = (LayoutInflater) getApplicationContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        vi.inflate(config.getCustomView(), all);
    }

    public abstract void initSplasher(SplasherConfig config);
    public abstract void onSplasherFinished();
    public View getCustomView(){
        return all;
    }
}
