package co.helpdesk.faveo.frontend.activities;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.github.ppamorim.dragger.DraggerActivity;

import co.helpdesk.faveo.R;


public class HelpingActivity extends DraggerActivity {
    ImageView closeImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_helping);
        Window window = HelpingActivity.this.getWindow();

// clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

// add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

// finally change the color
        window.setStatusBarColor(ContextCompat.getColor(HelpingActivity.this,R.color.faveo));
        setTension(0.1f);
        //Tension of the animation. This represent with the friction, how much time the animation will be executed.
        //setFriction(1.1f);
        //Friction of the animation. This represent with the tension, how much friction is applied at the tension animation.
        closeImage= (ImageView) findViewById(R.id.closeButton);
        closeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();

            }
        });

    }
}

