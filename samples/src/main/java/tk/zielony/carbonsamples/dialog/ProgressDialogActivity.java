package tk.zielony.carbonsamples.dialog;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import androidx.annotation.Nullable;

import carbon.dialog.ProgressDialog;
import carbon.widget.EditText;
import tk.zielony.carbonsamples.SampleAnnotation;
import tk.zielony.carbonsamples.R;
import tk.zielony.carbonsamples.ThemedActivity;

@SampleAnnotation(layoutId = R.layout.activity_progressdialog, titleId = R.string.progressDialogActivity_title)
public class ProgressDialogActivity extends ThemedActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initToolbar();

        EditText titleText = findViewById(R.id.titleText);
        EditText messageText = findViewById(R.id.messageText);
        EditText buttonText = findViewById(R.id.buttonText);

        findViewById(R.id.showProgressDialog).setOnClickListener(view -> {
            ProgressDialog dialog = new ProgressDialog(this);
            if (titleText.length() > 0)
                dialog.setTitle(titleText.getText());
            if (buttonText.length() > 0)
                dialog.addButton(buttonText.getText().toString(), null);
            Handler handler = new Handler(Looper.getMainLooper());
            Runnable dismissRunnable = dialog::dismiss;
            dialog.setOnDismissListener(dialogInterface -> handler.removeCallbacks(dismissRunnable));
            dialog.setText(messageText.getText());
            dialog.show();
            handler.postDelayed(dialog::dismiss, 2000);
        });
    }
}
