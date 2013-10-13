package com.cyberinsane.pocollections.customview;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ImageView;

import com.cyberinsane.pocollections.R;

public class BobbleHeadActivity extends Activity {

    ImageView imageView;
    int mImageWidth = 0;
    int mImageHeght = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bobblehead_activity);

    }
}
