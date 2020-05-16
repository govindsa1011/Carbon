package tk.zielony.carbonsamples.feature

import android.os.Bundle
import android.util.TypedValue
import kotlinx.android.synthetic.main.activity_autosizetext.*
import tk.zielony.carbonsamples.SampleAnnotation
import tk.zielony.carbonsamples.R
import tk.zielony.carbonsamples.ThemedActivity

@SampleAnnotation(layoutId = R.layout.activity_autosizetext, titleId = R.string.autoSizeTextActivity_title)
class AutoSizeTextActivity : ThemedActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initToolbar()

        autoSizeText.addOnLayoutChangeListener { v, left, top, right, bottom, oldLeft, oldTop, oldRight, oldBottom ->
            textSize.setTextSize(TypedValue.COMPLEX_UNIT_PX, autoSizeText.textSize)
            textSize.text = "${autoSizeText.textSize / resources.displayMetrics.scaledDensity}sp"
        }
    }
}
