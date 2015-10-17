package com.hojjat.untiaddiction;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.Window;
import android.view.WindowManager;

import static android.content.Intent.*;

/**
 * Created by hojjat on 9/21/15.
 */
public class ActivityBlock extends Activity {
    BlockHelper blockHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //must be before setContentView
        setContentView(R.layout.activity_block);
        //must be after setContentView :|
        getWindow().getAttributes().width = WindowManager.LayoutParams.MATCH_PARENT;
        getWindow().getAttributes().height = WindowManager.LayoutParams.MATCH_PARENT;

        blockHelper = new BlockHelper(this);
    }

    @Override
    public void onBackPressed() {
        if (blockHelper.getLimitType() == BlockHelper.LimitType.Block)
            goToHomePage();
    }

    private void goToHomePage() {
        Intent startMain = new Intent(ACTION_MAIN);
        startMain.addCategory(CATEGORY_HOME);
        startMain.setFlags(FLAG_ACTIVITY_NEW_TASK);
        startActivity(startMain);
        finish();
    }
}