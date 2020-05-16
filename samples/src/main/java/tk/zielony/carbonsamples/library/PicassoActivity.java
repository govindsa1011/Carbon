package tk.zielony.carbonsamples.library;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import carbon.widget.Button;
import tk.zielony.carbonsamples.SampleAnnotation;
import tk.zielony.carbonsamples.R;
import tk.zielony.carbonsamples.ThemedActivity;

@SampleAnnotation(layoutId = R.layout.activity_picasso, titleId = R.string.picassoActivity_title)
public class PicassoActivity extends ThemedActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initToolbar();

        final PicassoView image = findViewById(R.id.image);
        Button button = findViewById(R.id.button);
        button.setOnClickListener(view -> {
            Picasso.get()
                    .load("http://lorempixel.com/" + image.getWidth() + "/" + image.getHeight() + "/people/#" + System.currentTimeMillis())
                    .placeholder(new ColorDrawable(0x00000000))
                    .error(new ColorDrawable(0x00000000))
                    .into((Target) image);
        });
    }
}
