package tk.zielony.carbonsamples

import android.content.Context
import android.os.Bundle
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import carbon.dialog.MultiSelectDialog
import carbon.internal.DebugOverlay
import carbon.widget.MenuStrip
import carbon.widget.Toolbar
import java.util.*

abstract class SamplesActivity : AppCompatActivity() {

    var debugEnabled = false
    var debugOptions = arrayOf("bounds", "grid", "hit rects", "margins", "paddings", "rulers", "text sizes")

    private val overlay by lazy {
        DebugOverlay(this).apply {
            isDrawBoundsEnabled = false
            isDrawGridEnabled = false
            isDrawHitRectsEnabled = false
            isDrawMarginsEnabled = false
            isDrawPaddingsEnabled = false
            isDrawRulersEnabled = false
            isDrawTextSizesEnabled = false
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)

        javaClass.getAnnotation(SampleAnnotation::class.java)?.let {
            if (it.layoutId != 0)
                setContentView(it.layoutId)
            if (it.titleId != 0)
                title = getString(it.titleId)
        }

        if (this !is SampleListActivity && this !is ColorsActivity && this !is CodeActivity && this !is AboutActivity) {
            val preferences = getSharedPreferences("samples", Context.MODE_PRIVATE)
            preferences.edit().putString(RECENTLY_USED, javaClass.name).apply()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        javaClass.getAnnotation(SampleAnnotation::class.java)?.let {
            if (it.menuId != 0)
                menuInflater.inflate(it.menuId, menu)
        }

        return super.onCreateOptionsMenu(menu)
    }

    fun initToolbar() {
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        toolbar?.title = title
        toolbar?.setIconVisible(toolbar.icon != null)

        toolbar?.setOnMenuItemClicked { view, item, position ->
            when (item.id) {
                R.id.debug -> debugClicked()
                R.id.enabled -> enabledClicked(item as MenuStrip.CheckableItem)
            }
        }
    }

    private fun debugClicked() {
        val listDialog = MultiSelectDialog<String>(this@SamplesActivity)
        listDialog.setItems(debugOptions)
        listDialog.setTitle("Debug options")
        val initialItems = ArrayList<String>()
        if (overlay.isDrawBoundsEnabled)
            initialItems.add("bounds")
        if (overlay.isDrawGridEnabled)
            initialItems.add("grid")
        if (overlay.isDrawHitRectsEnabled)
            initialItems.add("hit rects")
        if (overlay.isDrawMarginsEnabled)
            initialItems.add("margins")
        if (overlay.isDrawPaddingsEnabled)
            initialItems.add("paddings")
        if (overlay.isDrawRulersEnabled)
            initialItems.add("rulers")
        if (overlay.isDrawTextSizesEnabled)
            initialItems.add("text sizes")
        listDialog.selectedItems = initialItems
        listDialog.show()
        listDialog.setOnDismissListener { dialogInterface ->
            val selectedItems = listDialog.selectedItems
            overlay.isDrawBoundsEnabled = selectedItems.contains("bounds")
            overlay.isDrawGridEnabled = selectedItems.contains("grid")
            overlay.isDrawHitRectsEnabled = selectedItems.contains("hit rects")
            overlay.isDrawMarginsEnabled = selectedItems.contains("margins")
            overlay.isDrawPaddingsEnabled = selectedItems.contains("paddings")
            overlay.isDrawRulersEnabled = selectedItems.contains("rulers")
            overlay.isDrawTextSizesEnabled = selectedItems.contains("text sizes")

            if (!debugEnabled && selectedItems.isNotEmpty()) {
                overlay.show()
                debugEnabled = true
            } else if (debugEnabled && selectedItems.isEmpty()) {
                overlay.dismiss()
                debugEnabled = false
            }
        }
    }

    private fun enabledClicked(item: MenuStrip.CheckableItem) {
        val views = ArrayList<View>()
        views.add(window.decorView.rootView)
        while (views.isNotEmpty()) {
            val v = views.removeAt(0)
            if (v.id == R.id.toolbar)
                continue
            v.isEnabled = item.isChecked
            if (v is ViewGroup) {
                for (i in 0 until v.childCount)
                    views.add(v.getChildAt(i))
            }
        }
    }

    companion object {
        const val RECENTLY_USED = "recentlyUsed"
    }
}
