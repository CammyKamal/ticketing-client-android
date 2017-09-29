package com.chandigarhadmin.ui;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.Html;

import com.chandigarhadmin.R;
import com.chandigarhadmin.session.SessionManager;
import com.daimajia.androidanimations.library.Techniques;

import wail.splacher.com.splasher.lib.SplasherActivity;
import wail.splacher.com.splasher.models.SplasherConfig;
import wail.splacher.com.splasher.utils.Const;

public class MainActivity extends SplasherActivity {
    private SessionManager sessionManager;

    @Override
    public void initSplasher(SplasherConfig config) {
        config.setReveal_start(Const.START_TOP_LEFT)
                //---------------
                .setAnimationDuration(3000)
                //---------------
                .setLogo(R.drawable.chdlogo)
                .setLogo_animation(Techniques.BounceIn)
                .setAnimationLogoDuration(2000)
                .setLogoWidth(500)
                //---------------
                .setTitle(Html.fromHtml(getString(R.string.chandigarh_smart)).toString())
                .setTitleColor(Color.parseColor("#ffffff"))
                .setTitleAnimation(Techniques.Bounce)
                .setTitleSize(20)
                //---------------
                .setSubtitle(getString(R.string.city_beautiful))
                .setSubtitleColor(Color.parseColor("#ffffff"))
                .setSubtitleAnimation(Techniques.FadeIn)
                .setSubtitleSize(16)
                //---------------
                .setSubtitleTypeFace(Typeface.createFromAsset(getAssets(), "diana.otf"))
                .setTitleTypeFace(Typeface.createFromAsset(getAssets(), "stc.otf"));

    }

    @Override
    public void onSplasherFinished() {
        sessionManager = new SessionManager(this);
        Intent it;
        if (!sessionManager.isLoggedIn()) {
            it = new Intent(MainActivity.this, LoginActivity.class);
        } else {
            it = new Intent(MainActivity.this, AdminAgentActivity.class);
        }
        startActivity(it);
        finish();
    }
}
