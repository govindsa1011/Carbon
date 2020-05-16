package tk.zielony.carbonsamples.feature;

import android.os.Bundle;
import android.widget.FrameLayout;

import carbon.view.ShadowView;
import carbon.widget.Button;
import tk.zielony.carbonsamples.SampleAnnotation;
import tk.zielony.carbonsamples.R;
import tk.zielony.carbonsamples.ThemedActivity;

@SampleAnnotation(layoutId = R.layout.activity_zorder, titleId = R.string.zOrderActivity_title)
public class ZOrderActivity extends ThemedActivity {
    boolean flag = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initToolbar();

        final FrameLayout layout = findViewById(R.id.layout);
        Button button = findViewById(R.id.button);
        button.setOnClickListener(view -> {
            ((ShadowView) layout.getChildAt(0)).setElevation(getResources().getDimension(R.dimen.carbon_1dip) * (flag ? 2 : 3));
            ((ShadowView) layout.getChildAt(1)).setElevation(getResources().getDimension(R.dimen.carbon_1dip) * (flag ? 3 : 2));
            flag = !flag;
            layout.postInvalidate();
        });
    }
}
