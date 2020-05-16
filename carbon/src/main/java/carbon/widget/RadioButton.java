package carbon.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.SoundEffectConstants;
import android.view.ViewDebug;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.Checkable;
import android.widget.CompoundButton;

import androidx.annotation.AttrRes;
import androidx.annotation.NonNull;
import androidx.annotation.StyleRes;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.core.view.ViewCompat;

import carbon.Carbon;
import carbon.R;
import carbon.drawable.ButtonGravity;
import carbon.drawable.ripple.RippleDrawable;

public class RadioButton extends TextView implements Checkable {
    private Drawable drawable;
    private float drawablePadding;
    private ButtonGravity buttonGravity;

    public RadioButton(Context context) {
        super(context, null, android.R.attr.radioButtonStyle);
        initRadioButton(null, android.R.attr.radioButtonStyle, R.style.carbon_RadioButton);
    }

    public RadioButton(Context context, AttributeSet attrs) {
        super(context, attrs, android.R.attr.radioButtonStyle);
        initRadioButton(attrs, android.R.attr.radioButtonStyle, R.style.carbon_RadioButton);
    }

    public RadioButton(Context context, AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initRadioButton(attrs, defStyleAttr, R.style.carbon_RadioButton);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public RadioButton(Context context, AttributeSet attrs, @AttrRes int defStyleAttr, @StyleRes int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initRadioButton(attrs, defStyleAttr, defStyleRes);
    }

    public void initRadioButton(AttributeSet attrs, @AttrRes int defStyleAttr, @StyleRes int defStyleRes) {
        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.RadioButton, defStyleAttr, defStyleRes);

        setButtonDrawable(Carbon.getDrawable(this, a, R.styleable.RadioButton_android_button, R.drawable.carbon_radio_anim));

        for (int i = 0; i < a.getIndexCount(); i++) {
            int attr = a.getIndex(i);
            if (attr == R.styleable.RadioButton_android_drawablePadding) {
                drawablePadding = a.getDimension(attr, 0);
            } else if (attr == R.styleable.RadioButton_android_checked) {
                setChecked(a.getBoolean(attr, false));
            } else if (attr == R.styleable.RadioButton_carbon_buttonGravity) {
                buttonGravity = ButtonGravity.values()[a.getInt(attr, 0)];
            }
        }

        a.recycle();
    }

    private boolean isLayoutRtl() {
        return ViewCompat.getLayoutDirection(this) == ViewCompat.LAYOUT_DIRECTION_RTL;
    }

    private boolean isButtonOnTheLeft() {
        return buttonGravity == ButtonGravity.LEFT ||
                !isLayoutRtl() && buttonGravity == ButtonGravity.START ||
                isLayoutRtl() && buttonGravity == ButtonGravity.END;
    }


    private boolean checked;
    private boolean mBroadcasting;

    private OnCheckedChangeListener onCheckedChangeListener;
    private OnCheckedChangeListener mOnCheckedChangeWidgetListener;

    private static final int[] CHECKED_STATE_SET = {
            android.R.attr.state_checked
    };

    public void toggle() {
        setChecked(!isChecked());
    }

    @Override
    public boolean performClick() {
        setChecked(true);

        if (onCheckedChangeListener != null)
            onCheckedChangeListener.onCheckedChanged(this, checked);

        final boolean handled = super.performClick();
        if (!handled) {
            // View only makes a sound effect if the onClickListener was
            // called, so we'll need to make one here instead.
            playSoundEffect(SoundEffectConstants.CLICK);
        }

        return handled;
    }

    @ViewDebug.ExportedProperty
    public boolean isChecked() {
        return checked;
    }

    /**
     * <p>Changes the checked state of this button.</p>
     *
     * @param checked true to check the button, false to uncheck it
     */
    public void setChecked(boolean checked) {
        if (this.checked != checked) {
            this.checked = checked;
            refreshDrawableState();
            //notifyViewAccessibilityStateChangedIfNeeded(
            //      AccessibilityEvent.CONTENT_CHANGE_TYPE_UNDEFINED);

            // Avoid infinite recursions if setChecked() is called from a listener
            if (mBroadcasting) {
                return;
            }

            mBroadcasting = true;
            if (mOnCheckedChangeWidgetListener != null) {
                mOnCheckedChangeWidgetListener.onCheckedChanged(this, checked);
            }

            mBroadcasting = false;
        }
    }

    /**
     * Register a callback to be invoked when the checked state of this button changes.
     *
     * @param listener the callback to call on checked state change
     */
    public void setOnCheckedChangeListener(OnCheckedChangeListener listener) {
        onCheckedChangeListener = listener;
    }

    /**
     * Register a callback to be invoked when the checked state of this button changes. This
     * callback is used for internal purpose only.
     *
     * @param listener the callback to call on checked state change
     * @hide
     */
    void setOnCheckedChangeWidgetListener(OnCheckedChangeListener listener) {
        mOnCheckedChangeWidgetListener = listener;
    }

    /**
     * Set the button graphic to a given Drawable
     *
     * @param d The Drawable to use as the button graphic
     */
    public void setButtonDrawable(Drawable d) {
        if (drawable != d) {
            if (drawable != null) {
                drawable.setCallback(null);
                unscheduleDrawable(drawable);
            }

            drawable = d;

            if (d != null) {
                drawable = DrawableCompat.wrap(d);
                d.setCallback(this);
                //d.setLayoutDirection(getLayoutDirection());
                if (d.isStateful()) {
                    d.setState(getDrawableState());
                }
                d.setVisible(getVisibility() == VISIBLE, false);
                setMinHeight(d.getIntrinsicHeight());
                applyTint();
            }
        }
    }

    public ButtonGravity getButtonGravity() {
        return buttonGravity;
    }

    public void setButtonGravity(ButtonGravity buttonGravity) {
        this.buttonGravity = buttonGravity;
    }

    @Override
    protected void updateTint() {
        super.updateTint();
        if (drawable != null && tint != null && tintMode != null)
            drawable.setColorFilter(new PorterDuffColorFilter(tint.getColorForState(getDrawableState(), tint.getDefaultColor()), tintMode));
    }

    @Override
    protected void applyTint() {
        super.applyTint();
        if (drawable != null) {
            if (tint != null && tintMode != null) {
                Carbon.setTintListMode(drawable, tint, tintMode);
            } else {
                Carbon.clearTint(drawable);
            }

            // The drawable (or one of its children) may not have been
            // stateful before applying the tint, so let's try again.
            if (drawable.isStateful())
                drawable.setState(getDrawableState());
        }
    }

    @Override
    public CharSequence getAccessibilityClassName() {
        return RadioButton.class.getSimpleName();
    }

    @Override
    public void onInitializeAccessibilityEvent(AccessibilityEvent event) {
        super.onInitializeAccessibilityEvent(event);
        event.setChecked(isChecked());
    }

    @Override
    public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo info) {
        super.onInitializeAccessibilityNodeInfo(info);
        info.setCheckable(true);
        info.setChecked(isChecked());
    }

    @Override
    public int getCompoundPaddingLeft() {
        int padding = super.getCompoundPaddingLeft();
        if (isButtonOnTheLeft()) {
            final Drawable buttonDrawable = drawable;
            if (buttonDrawable != null) {
                padding += buttonDrawable.getIntrinsicWidth() + drawablePadding;
            }
        }
        return padding;
    }

    @Override
    public int getCompoundPaddingRight() {
        int padding = super.getCompoundPaddingRight();
        if (!isButtonOnTheLeft()) {
            final Drawable buttonDrawable = drawable;
            if (buttonDrawable != null) {
                padding += buttonDrawable.getIntrinsicWidth() + drawablePadding;
            }
        }
        return padding;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        final Drawable buttonDrawable = drawable;
        if (buttonDrawable != null) {
            final int verticalGravity = getGravity() & Gravity.VERTICAL_GRAVITY_MASK;
            final int drawableHeight = buttonDrawable.getIntrinsicHeight();
            final int drawableWidth = buttonDrawable.getIntrinsicWidth();

            final int top;
            switch (verticalGravity) {
                case Gravity.BOTTOM:
                    top = getHeight() - drawableHeight;
                    break;
                case Gravity.CENTER_VERTICAL:
                    top = (getHeight() - drawableHeight) / 2;
                    break;
                default:
                    top = 0;
            }
            final int bottom = top + drawableHeight;
            final int left = isButtonOnTheLeft() ? getPaddingLeft() : getWidth() - drawableWidth - getPaddingRight();
            final int right = isButtonOnTheLeft() ? drawableWidth + getPaddingLeft() : getWidth() - getPaddingRight();

            buttonDrawable.setBounds(left, top, right, bottom);

            final Drawable background = getBackground();
            if (background != null && background instanceof RippleDrawable) {
                //TODO: hotspotBounds
                // ((RippleDrawable)background).setHotspotBounds(left, top, right, bottom);
            }
        }

        super.onDraw(canvas);

        if (buttonDrawable != null) {
            final int scrollX = getScrollX();
            final int scrollY = getScrollY();
            if (scrollX == 0 && scrollY == 0) {
                buttonDrawable.draw(canvas);
            } else {
                canvas.translate(scrollX, scrollY);
                buttonDrawable.draw(canvas);
                canvas.translate(-scrollX, -scrollY);
            }
        }
    }

    @Override
    protected int[] onCreateDrawableState(int extraSpace) {
        int[] drawableState = super.onCreateDrawableState(extraSpace);
        if (isChecked()) {
            int[] state = new int[drawableState.length + 1];
            System.arraycopy(drawableState, 0, state, 0, drawableState.length);
            drawableState = state;
            mergeDrawableStates(drawableState, CHECKED_STATE_SET);
        }
        return drawableState;
    }

    @Override
    protected void drawableStateChanged() {
        super.drawableStateChanged();

        Drawable d = drawable;
        if (d != null && d.isStateful()
                && d.setState(getDrawableState())) {
            invalidateDrawable(d);
        }
    }

    @Override
    protected boolean verifyDrawable(@NonNull Drawable who) {
        return super.verifyDrawable(who) || who == drawable;
    }

    @Override
    public void jumpDrawablesToCurrentState() {
        super.jumpDrawablesToCurrentState();
        if (drawable != null) drawable.jumpToCurrentState();
    }

    static class SavedState extends BaseSavedState {
        boolean checked;

        /**
         * Constructor called from {@link CompoundButton#onSaveInstanceState()}
         */
        SavedState(Parcelable superState) {
            super(superState);
        }

        /**
         * Constructor called from {@link #CREATOR}
         */
        private SavedState(Parcel in) {
            super(in);
            checked = (Boolean) in.readValue(getClass().getClassLoader());
        }

        @Override
        public void writeToParcel(Parcel out, int flags) {
            super.writeToParcel(out, flags);
            out.writeValue(checked);
        }

        @Override
        public String toString() {
            return "RadioButton.SavedState{"
                    + Integer.toHexString(System.identityHashCode(this))
                    + " checked=" + checked + "}";
        }

        public static final Parcelable.Creator<SavedState> CREATOR
                = new Parcelable.Creator<SavedState>() {
            public SavedState createFromParcel(Parcel in) {
                return new SavedState(in);
            }

            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }
        };
    }

    @Override
    public Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();

        SavedState ss = new SavedState(superState);

        ss.checked = isChecked();
        return ss;
    }

    @Override
    public void onRestoreInstanceState(Parcelable state) {
        SavedState ss = (SavedState) state;

        super.onRestoreInstanceState(ss.getSuperState());
        setChecked(ss.checked);
        requestLayout();
    }
}
