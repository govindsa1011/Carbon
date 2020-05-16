package carbon;

import android.animation.AnimatorInflater;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.Html;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;

import androidx.annotation.AttrRes;
import androidx.annotation.NonNull;
import androidx.appcompat.view.ContextThemeWrapper;
import androidx.appcompat.view.SupportMenuInflater;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.graphics.drawable.TintAwareDrawable;

import com.google.android.material.shape.CutCornerTreatment;
import com.google.android.material.shape.RoundedCornerTreatment;
import com.google.android.material.shape.ShapeAppearanceModel;

import java.lang.ref.WeakReference;
import java.security.InvalidParameterException;
import java.util.concurrent.atomic.AtomicBoolean;

import carbon.animation.AnimUtils;
import carbon.animation.AnimatedColorStateList;
import carbon.animation.AnimatedView;
import carbon.drawable.AlphaDrawable;
import carbon.drawable.AlphaWithParentDrawable;
import carbon.drawable.ColorStateListDrawable;
import carbon.drawable.ColorStateListFactory;
import carbon.drawable.VectorDrawable;
import carbon.drawable.ripple.RippleDrawable;
import carbon.drawable.ripple.RippleView;
import carbon.view.AutoSizeTextView;
import carbon.view.InsetView;
import carbon.view.MaxSizeView;
import carbon.view.RevealView;
import carbon.view.ShadowView;
import carbon.view.ShapeModelView;
import carbon.view.StateAnimatorView;
import carbon.view.StrokeView;
import carbon.view.TextAppearanceView;
import carbon.view.TintedView;
import carbon.view.TouchMarginView;
import carbon.widget.AutoSizeTextMode;

public class Carbon {
    private static final long DEFAULT_REVEAL_DURATION = 200;
    private static long defaultRevealDuration = DEFAULT_REVEAL_DURATION;

    public static final boolean IS_LOLLIPOP_OR_HIGHER = Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;

    public static final boolean IS_PIE_OR_HIGHER = Build.VERSION.SDK_INT >= Build.VERSION_CODES.P;

    public static PorterDuffXfermode CLEAR_MODE = new PorterDuffXfermode(PorterDuff.Mode.CLEAR);

    public static final int INVALID_INDEX = -1;

    private Carbon() {
    }

    public static float getDip(Context context) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1, context.getResources().getDisplayMetrics());
    }

    public static float getSp(Context context) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 1, context.getResources().getDisplayMetrics());
    }

    public static ColorStateList getDefaultColorStateList(View view, TypedArray a, int id) {
        if (!a.hasValue(id))
            return null;
        try {
            if (a.getColor(id, 0) != view.getResources().getColor(R.color.carbon_defaultColorControl))
                return null;
        } catch (Resources.NotFoundException e) {
            return null;
        }

        Context context = view.getContext();
        int resourceId = a.getResourceId(id, 0);

        if (resourceId == R.color.carbon_defaultColorPrimary) {
            return ColorStateListFactory.INSTANCE.makePrimary(context);
        } else if (resourceId == R.color.carbon_defaultColorPrimaryInverse) {
            return ColorStateListFactory.INSTANCE.makePrimaryInverse(context);
        } else if (resourceId == R.color.carbon_defaultColorSecondary) {
            return ColorStateListFactory.INSTANCE.makeSecondary(context);
        } else if (resourceId == R.color.carbon_defaultColorSecondaryInverse) {
            return ColorStateListFactory.INSTANCE.makeSecondaryInverse(context);

        } else if (resourceId == R.color.carbon_defaultColorControl) {
            return ColorStateListFactory.INSTANCE.makeControl(context);
        } else if (resourceId == R.color.carbon_defaultColorControlInverse) {
            return ColorStateListFactory.INSTANCE.makeControlInverse(context);
        } else if (resourceId == R.color.carbon_defaultColorControlPrimary) {
            return ColorStateListFactory.INSTANCE.makeControlPrimary(context);
        } else if (resourceId == R.color.carbon_defaultColorControlPrimaryInverse) {
            return ColorStateListFactory.INSTANCE.makeControlPrimaryInverse(context);
        } else if (resourceId == R.color.carbon_defaultColorControlSecondary) {
            return ColorStateListFactory.INSTANCE.makeControlSecondary(context);
        } else if (resourceId == R.color.carbon_defaultColorControlSecondaryInverse) {
            return ColorStateListFactory.INSTANCE.makeControlSecondaryInverse(context);

        } else if (resourceId == R.color.carbon_defaultHighlightColor) {
            return ColorStateListFactory.INSTANCE.makeHighlight(context);
        } else if (resourceId == R.color.carbon_defaultHighlightColorSecondary) {
            return ColorStateListFactory.INSTANCE.makeHighlightSecondary(context);
        } else if (resourceId == R.color.carbon_defaultHighlightColorPrimary) {
            return ColorStateListFactory.INSTANCE.makeHighlightPrimary(context);

        } else if (resourceId == R.color.carbon_defaultIconColor) {
            return ColorStateListFactory.INSTANCE.makeIcon(context);
        } else if (resourceId == R.color.carbon_defaultIconColorInverse) {
            return ColorStateListFactory.INSTANCE.makeIconInverse(context);
        } else if (resourceId == R.color.carbon_defaultIconColorSecondary) {
            return ColorStateListFactory.INSTANCE.makeIconSecondary(context);
        } else if (resourceId == R.color.carbon_defaultIconColorSecondaryInverse) {
            return ColorStateListFactory.INSTANCE.makeIconSecondaryInverse(context);
        } else if (resourceId == R.color.carbon_defaultIconColorPrimary) {
            return ColorStateListFactory.INSTANCE.makeIconPrimary(context);
        } else if (resourceId == R.color.carbon_defaultIconColorPrimaryInverse) {
            return ColorStateListFactory.INSTANCE.makeIconPrimaryInverse(context);

        } else if (resourceId == R.color.carbon_defaultTextPrimaryColor) {
            return ColorStateListFactory.INSTANCE.makePrimaryText(context);
        } else if (resourceId == R.color.carbon_defaultTextSecondaryColor) {
            return ColorStateListFactory.INSTANCE.makeSecondaryText(context);
        } else if (resourceId == R.color.carbon_defaultTextPrimaryColorInverse) {
            return ColorStateListFactory.INSTANCE.makePrimaryTextInverse(context);
        } else if (resourceId == R.color.carbon_defaultTextSecondaryColorInverse) {
            return ColorStateListFactory.INSTANCE.makeSecondaryTextInverse(context);
        } else if (resourceId == R.color.carbon_defaultTextColorPrimary) {
            return ColorStateListFactory.INSTANCE.makeTextPrimary(context);
        } else if (resourceId == R.color.carbon_defaultTextColorPrimaryInverse) {
            return ColorStateListFactory.INSTANCE.makeTextPrimaryInverse(context);
        } else if (resourceId == R.color.carbon_defaultTextColorSecondary) {
            return ColorStateListFactory.INSTANCE.makeTextSecondary(context);
        } else if (resourceId == R.color.carbon_defaultTextColorSecondaryInverse) {
            return ColorStateListFactory.INSTANCE.makeTextSecondaryInverse(context);

        } else if (resourceId == R.color.carbon_defaultRippleColorPrimary) {
            int c = Carbon.getThemeColor(context, R.attr.colorPrimary);
            return ColorStateList.valueOf(0x12000000 | (c & 0xffffff));
        } else if (resourceId == R.color.carbon_defaultRippleColorSecondary) {
            int c = Carbon.getThemeColor(context, R.attr.colorSecondary);
            return ColorStateList.valueOf(0x12000000 | (c & 0xffffff));
        }

        return null;
    }

    public static ColorStateList getColorStateList(View view, TypedArray a, int id) {
        ColorStateList color = getDefaultColorStateList(view, a, id);
        if (color == null)
            color = a.getColorStateList(id);
        return color;
    }

    public static Drawable getDefaultColorDrawable(View view, TypedArray a, int id) {
        ColorStateList color = getDefaultColorStateList(view, a, id);
        if (color != null) {
            Drawable d = new ColorStateListDrawable(AnimatedColorStateList.fromList(color, animation -> view.postInvalidate()));
            if (color instanceof AlphaWithParentDrawable.AlphaWithParentColorStateList)
                return new AlphaWithParentDrawable(view, d);
            return d;
        } else {
            Context context = view.getContext();
            int resourceId = a.getResourceId(id, 0);
            if (resourceId == R.color.carbon_defaultMenuSelectionDrawable) {
                return ColorStateListFactory.INSTANCE.makeMenuSelection(context);
            } else if (resourceId == R.color.carbon_defaultMenuSelectionDrawablePrimary) {
                return ColorStateListFactory.INSTANCE.makeMenuSelectionPrimary(context);
            } else if (resourceId == R.color.carbon_defaultMenuSelectionDrawableSecondary) {
                return ColorStateListFactory.INSTANCE.makeMenuSelectionSecondary(context);
            }
        }
        return null;
    }

    public static void initDefaultBackground(View view, TypedArray a, int id) {
        Drawable d = getDefaultColorDrawable(view, a, id);
        if (d != null)
            view.setBackgroundDrawable(d);
    }

    public static void initDefaultTextColor(TextAppearanceView view, TypedArray a, int id) {
        ColorStateList color = getDefaultColorStateList((View) view, a, id);
        if (color != null)
            view.setTextColor(color);
    }

    public static void initRippleDrawable(RippleView rippleView, TypedArray a, int[] ids) {
        int carbon_rippleColor = ids[0];
        int carbon_rippleStyle = ids[1];
        int carbon_rippleHotspot = ids[2];
        int carbon_rippleRadius = ids[3];

        View view = (View) rippleView;
        if (view.isInEditMode())
            return;

        ColorStateList color = getColorStateList(view, a, carbon_rippleColor);

        if (color != null) {
            RippleDrawable.Style style = RippleDrawable.Style.values()[a.getInt(carbon_rippleStyle, RippleDrawable.Style.Background.ordinal())];
            boolean useHotspot = a.getBoolean(carbon_rippleHotspot, true);
            int radius = (int) a.getDimension(carbon_rippleRadius, -1);

            rippleView.setRippleDrawable(RippleDrawable.create(color, style, view, useHotspot, radius));
        }
    }

    public static void initTouchMargin(TouchMarginView view, TypedArray a, int[] ids) {
        int carbon_touchMargin = ids[0];
        int carbon_touchMarginLeft = ids[1];
        int carbon_touchMarginTop = ids[2];
        int carbon_touchMarginRight = ids[3];
        int carbon_touchMarginBottom = ids[4];

        int touchMarginAll = (int) a.getDimension(carbon_touchMargin, 0);
        int left = (int) a.getDimension(carbon_touchMarginLeft, touchMarginAll);
        int top = (int) a.getDimension(carbon_touchMarginTop, touchMarginAll);
        int right = (int) a.getDimension(carbon_touchMarginRight, touchMarginAll);
        int bottom = (int) a.getDimension(carbon_touchMarginBottom, touchMarginAll);
        view.setTouchMargin(left, top, right, bottom);
    }

    public static void initInset(InsetView view, TypedArray a, int[] ids) {
        int carbon_inset = ids[0];
        int carbon_insetLeft = ids[1];
        int carbon_insetTop = ids[2];
        int carbon_insetRight = ids[3];
        int carbon_insetBottom = ids[4];
        int carbon_insetColor = ids[5];

        int insetAll = (int) a.getDimension(carbon_inset, InsetView.INSET_NULL);
        int left = (int) a.getDimension(carbon_insetLeft, insetAll);
        int top = (int) a.getDimension(carbon_insetTop, insetAll);
        int right = (int) a.getDimension(carbon_insetRight, insetAll);
        int bottom = (int) a.getDimension(carbon_insetBottom, insetAll);
        view.setInset(left, top, right, bottom);

        view.setInsetColor(a.getColor(carbon_insetColor, 0));
    }

    public static void initMaxSize(MaxSizeView view, TypedArray a, int[] ids) {
        int carbon_maxWidth = ids[0];
        int carbon_maxHeight = ids[1];

        int width = (int) a.getDimension(carbon_maxWidth, Integer.MAX_VALUE);
        int height = (int) a.getDimension(carbon_maxHeight, Integer.MAX_VALUE);
        view.setMaxWidth(width);
        view.setMaxHeight(height);
    }

    public static void initTint(TintedView view, TypedArray a, int[] ids) {
        int carbon_tint = ids[0];
        int carbon_tintMode = ids[1];
        int carbon_backgroundTint = ids[2];
        int carbon_backgroundTintMode = ids[3];
        int carbon_animateColorChanges = ids[4];

        if (a.hasValue(carbon_tint)) {
            ColorStateList color = getDefaultColorStateList((View) view, a, carbon_tint);
            if (color == null)
                color = a.getColorStateList(carbon_tint);
            if (color != null)
                view.setTintList(color);
        }
        view.setTintMode(TintedView.modes[a.getInt(carbon_tintMode, 1)]);

        if (a.hasValue(carbon_backgroundTint)) {
            ColorStateList color = getDefaultColorStateList((View) view, a, carbon_backgroundTint);
            if (color == null)
                color = a.getColorStateList(carbon_backgroundTint);
            if (color != null)
                view.setBackgroundTintList(color);
        }
        view.setBackgroundTintMode(TintedView.modes[a.getInt(carbon_backgroundTintMode, 1)]);

        if (a.hasValue(carbon_animateColorChanges))
            view.setAnimateColorChangesEnabled(a.getBoolean(carbon_animateColorChanges, false));
    }

    public static void initAnimations(AnimatedView view, TypedArray a, int[] ids) {
        if (((View) view).isInEditMode())
            return;

        int carbon_inAnimation = ids[0];
        if (a.hasValue(carbon_inAnimation)) {
            TypedValue typedValue = new TypedValue();
            a.getValue(carbon_inAnimation, typedValue);
            if (typedValue.resourceId != 0) {
                view.setInAnimator(AnimatorInflater.loadAnimator(((View) view).getContext(), typedValue.resourceId));
            } else {
                view.setInAnimator(AnimUtils.Style.values()[typedValue.data].getInAnimator());
            }
        }

        int carbon_outAnimation = ids[1];
        if (a.hasValue(carbon_outAnimation)) {
            TypedValue typedValue = new TypedValue();
            a.getValue(carbon_outAnimation, typedValue);
            if (typedValue.resourceId != 0) {
                view.setOutAnimator(AnimatorInflater.loadAnimator(((View) view).getContext(), typedValue.resourceId));
            } else {
                view.setOutAnimator(AnimUtils.Style.values()[typedValue.data].getOutAnimator());
            }
        }
    }

    public static void initElevation(ShadowView view, TypedArray a, int[] ids) {
        int carbon_elevation = ids[0];
        int carbon_shadowColor = ids[1];
        int carbon_ambientShadowColor = ids[2];
        int carbon_spotShadowColor = ids[3];

        float elevation = a.getDimension(carbon_elevation, 0);
        view.setElevation(elevation);
        if (elevation > 0)
            AnimUtils.setupElevationAnimator(((StateAnimatorView) view).getStateAnimator(), view);
        ColorStateList shadowColor = a.getColorStateList(carbon_shadowColor);
        view.setElevationShadowColor(shadowColor != null ? shadowColor.withAlpha(255) : null);
        if (a.hasValue(carbon_ambientShadowColor)) {
            ColorStateList ambientShadowColor = a.getColorStateList(carbon_ambientShadowColor);
            view.setOutlineAmbientShadowColor(ambientShadowColor != null ? ambientShadowColor.withAlpha(255) : null);
        }
        if (a.hasValue(carbon_spotShadowColor)) {
            ColorStateList spotShadowColor = a.getColorStateList(carbon_spotShadowColor);
            view.setOutlineSpotShadowColor(spotShadowColor != null ? spotShadowColor.withAlpha(255) : null);
        }
    }

    public static void initHtmlText(TextAppearanceView textView, TypedArray a, int id) {
        String string = a.getString(id);
        if (string != null)
            textView.setText(Html.fromHtml(string));
    }

    /**
     * @param context context
     * @param attr    attribute to get from the current theme
     * @return color from the current theme
     */
    public static int getThemeColor(Context context, int attr) {
        Resources.Theme theme = context.getTheme();
        TypedValue typedValue = new TypedValue();
        theme.resolveAttribute(attr, typedValue, true);
        return typedValue.resourceId != 0 ? context.getResources().getColor(typedValue.resourceId) : typedValue.data;
    }

    public static Drawable getThemeDrawable(Context context, int attr) {
        Resources.Theme theme = context.getTheme();
        TypedValue typedValue = new TypedValue();
        theme.resolveAttribute(attr, typedValue, true);
        return typedValue.resourceId != 0 ? ContextCompat.getDrawable(context, typedValue.resourceId) : null;
    }

    public static Drawable getDrawable(View view, TypedArray a, int attr, int defaultValue) {
        if (!view.isInEditMode()) {
            int resId = a.getResourceId(attr, 0);
            if (resId != 0) {
                if (view.getContext().getResources().getResourceTypeName(resId).equals("raw")) {
                    return new VectorDrawable(view.getResources(), resId);
                } else {
                    return ContextCompat.getDrawable(view.getContext(), resId);
                }
            }
        } else {
            try {
                return a.getDrawable(attr);
            } catch (Exception e) {
                return view.getResources().getDrawable(defaultValue);
            }
        }

        return null;
    }

    public static Context getThemedContext(Context context, AttributeSet attributeSet, int[] attrs, @AttrRes int defStyleAttr, int attr) {
        TypedArray a = context.obtainStyledAttributes(attributeSet, attrs, defStyleAttr, 0);
        if (a.hasValue(attr)) {
            int themeId = a.getResourceId(attr, 0);
            a.recycle();
            return new ContextThemeWrapper(context, themeId);
        }
        return context;
    }

    public static int getDrawableAlpha(Drawable background) {
        if (background == null)
            return 255;
        background = background.getCurrent();
        if (background instanceof ColorDrawable)
            return ((ColorDrawable) background).getAlpha();
        if (background instanceof AlphaDrawable)
            return ((AlphaDrawable) background).getAlpha();
        return 255;
    }

    public static float getBackgroundTintAlpha(View child) {
        if (!(child instanceof TintedView))
            return 255;
        ColorStateList tint = ((TintedView) child).getBackgroundTint();
        if (tint == null)
            return 255;
        int color = tint.getColorForState(child.getDrawableState(), tint.getDefaultColor());
        return (color >> 24) & 0xff;
    }

    public static long getDefaultRevealDuration() {
        return defaultRevealDuration;
    }

    public static void setDefaultRevealDuration(long defaultRevealDuration) {
        Carbon.defaultRevealDuration = defaultRevealDuration;
    }

    public static void initStroke(StrokeView strokeView, TypedArray a, int[] ids) {
        int carbon_stroke = ids[0];
        int carbon_strokeWidth = ids[1];

        View view = (View) strokeView;
        ColorStateList color = getDefaultColorStateList(view, a, carbon_stroke);
        if (color == null)
            color = a.getColorStateList(carbon_stroke);

        if (color != null)
            strokeView.setStroke(AnimatedColorStateList.fromList(color, animation -> view.postInvalidate()));
        strokeView.setStrokeWidth(a.getDimension(carbon_strokeWidth, 0));
    }

    public static void initCornerCutRadius(ShapeModelView shapeModelView, TypedArray a, int[] ids) {
        int carbon_cornerRadiusTopStart = ids[0];
        int carbon_cornerRadiusTopEnd = ids[1];
        int carbon_cornerRadiusBottomStart = ids[2];
        int carbon_cornerRadiusBottomEnd = ids[3];
        int carbon_cornerRadius = ids[4];
        int carbon_cornerCutTopStart = ids[5];
        int carbon_cornerCutTopEnd = ids[6];
        int carbon_cornerCutBottomStart = ids[7];
        int carbon_cornerCutBottomEnd = ids[8];
        int carbon_cornerCut = ids[9];

        float cornerRadius = Math.max(a.getDimension(carbon_cornerRadius, 0), 0.1f);
        float cornerRadiusTopStart = a.getDimension(carbon_cornerRadiusTopStart, cornerRadius);
        float cornerRadiusTopEnd = a.getDimension(carbon_cornerRadiusTopEnd, cornerRadius);
        float cornerRadiusBottomStart = a.getDimension(carbon_cornerRadiusBottomStart, cornerRadius);
        float cornerRadiusBottomEnd = a.getDimension(carbon_cornerRadiusBottomEnd, cornerRadius);
        float cornerCut = a.getDimension(carbon_cornerCut, 0);
        float cornerCutTopStart = a.getDimension(carbon_cornerCutTopStart, cornerCut);
        float cornerCutTopEnd = a.getDimension(carbon_cornerCutTopEnd, cornerCut);
        float cornerCutBottomStart = a.getDimension(carbon_cornerCutBottomStart, cornerCut);
        float cornerCutBottomEnd = a.getDimension(carbon_cornerCutBottomEnd, cornerCut);
        ShapeAppearanceModel model = ShapeAppearanceModel.builder()
                .setTopLeftCorner(cornerCutTopStart >= cornerRadiusTopStart ? new CutCornerTreatment(cornerCutTopStart) : new RoundedCornerTreatment(cornerRadiusTopStart))
                .setTopRightCorner(cornerCutTopEnd >= cornerRadiusTopEnd ? new CutCornerTreatment(cornerCutTopEnd) : new RoundedCornerTreatment(cornerRadiusTopEnd))
                .setBottomLeftCorner(cornerCutBottomStart >= cornerRadiusBottomStart ? new CutCornerTreatment(cornerCutBottomStart) : new RoundedCornerTreatment(cornerRadiusBottomStart))
                .setBottomRightCorner(cornerCutBottomEnd >= cornerRadiusBottomEnd ? new CutCornerTreatment(cornerCutBottomEnd) : new RoundedCornerTreatment(cornerRadiusBottomEnd))
                .build();
        shapeModelView.setShapeModel(model);
    }

    public static void initAutoSizeText(AutoSizeTextView view, TypedArray a, int[] ids) {
        int carbon_autoSizeText = ids[0];
        int carbon_autoSizeMinTextSize = ids[1];
        int carbon_autoSizeMaxTextSize = ids[2];
        int carbon_autoSizeStepGranularity = ids[3];
        view.setAutoSizeText(AutoSizeTextMode.values()[a.getInt(carbon_autoSizeText, 0)]);
        view.setMinTextSize(a.getDimension(carbon_autoSizeMinTextSize, 0));
        view.setMaxTextSize(a.getDimension(carbon_autoSizeMaxTextSize, 0));
        view.setAutoSizeStepGranularity(a.getDimension(carbon_autoSizeStepGranularity, 1));
    }

    public static float getThemeDimen(Context context, int attr) {
        Resources.Theme theme = context.getTheme();
        TypedValue typedValueAttr = new TypedValue();
        theme.resolveAttribute(attr, typedValueAttr, true);
        return typedValueAttr.getDimension(context.getResources().getDisplayMetrics());
    }

    public static int getThemeResId(Context context, int attr) {
        Resources.Theme theme = context.getTheme();
        TypedValue typedValueAttr = new TypedValue();
        theme.resolveAttribute(attr, typedValueAttr, true);
        return typedValueAttr.resourceId;
    }

    public static Menu getMenu(Context context, int resId) {
        Context contextWrapper = context;
        Menu menu = new MenuBuilder(contextWrapper);
        MenuInflater inflater = new SupportMenuInflater(contextWrapper);
        inflater.inflate(resId, menu);
        return menu;
    }

    public static float getRevealRadius(View view, int x, int y, float radius) {
        if (radius >= 0)
            return radius;
        if (radius != RevealView.MAX_RADIUS)
            throw new InvalidParameterException("radius should be RevealView.MAX_RADIUS, 0.0f or a positive float");
        int w = Math.max(view.getWidth() - x, x);
        int h = Math.max(view.getHeight() - y, y);
        return (float) Math.sqrt(w * w + h * h);
    }

    public static void setTint(Drawable drawable, int tint) {
        if (Carbon.IS_LOLLIPOP_OR_HIGHER) {
            drawable.setTint(tint);
        } else if (drawable instanceof TintAwareDrawable) {
            ((TintAwareDrawable) drawable).setTint(tint);
        } else {
            drawable.setColorFilter(new PorterDuffColorFilter(tint, PorterDuff.Mode.SRC_IN));
        }
    }

    public static void setTintListMode(Drawable drawable, ColorStateList tint, PorterDuff.Mode mode) {
        if (Carbon.IS_LOLLIPOP_OR_HIGHER) {
            drawable.setTintList(tint);
            drawable.setTintMode(mode);
        } else if (drawable instanceof TintAwareDrawable) {
            ((TintAwareDrawable) drawable).setTintList(tint);
            ((TintAwareDrawable) drawable).setTintMode(mode);
        } else {
            drawable.setColorFilter(tint == null ? null : new PorterDuffColorFilter(tint.getColorForState(drawable.getState(), tint.getDefaultColor()), mode));
        }
    }

    public static void clearTint(Drawable drawable) {
        if (Carbon.IS_LOLLIPOP_OR_HIGHER) {
            drawable.setTintList(null);
        } else if (drawable instanceof TintAwareDrawable) {
            ((TintAwareDrawable) drawable).setTintList(null);
        } else {
            drawable.setColorFilter(null);
        }
    }

    public static boolean isShapeRect(ShapeAppearanceModel model, RectF bounds) {
        return model.getTopLeftCornerSize().getCornerSize(bounds) <= 0.2f &&
                model.getTopRightCornerSize().getCornerSize(bounds) <= 0.2f &&
                model.getBottomLeftCornerSize().getCornerSize(bounds) <= 0.2f &&
                model.getBottomRightCornerSize().getCornerSize(bounds) <= 0.2f;
    }

    public static void logReflectionError(Exception e) {
        StackTraceElement cause = e.getStackTrace()[0];
        StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[3];
        Log.e("Carbon", "This feature is implemented using reflection. " +
                "If you see this exception, something in your setup may be nonstandard. " +
                "If you believe so, please create an issue on https://github.com/ZieIony/Carbon/issues. " +
                "Please provide at least the following information: \n" +
                " - device: " + Build.MANUFACTURER + " " + Build.MODEL + ", API " + Build.VERSION.SDK_INT + "\n" +
                " - method: " + stackTraceElement.getClassName() + "." + stackTraceElement.getMethodName() + "(...)\n" +
                " - cause: " + e.getClass().getName() + ": " + e.getMessage() + " at " + cause.getMethodName() + "(" + cause.getFileName() + ":" + cause.getLineNumber() + ")\n", e);
    }

    public static void handleFontAttribute(TextAppearanceView textView, TypedArray appearance, int textStyle, int fontWeight, int attributeId) {
        WeakReference<TextAppearanceView> textViewWeak = new WeakReference<>(textView);
        AtomicBoolean asyncFontPending = new AtomicBoolean();
        ResourcesCompat.FontCallback replyCallback = new ResourcesCompat.FontCallback() {
            @Override
            public void onFontRetrieved(@NonNull Typeface typeface) {
                if (asyncFontPending.get()) {
                    TextAppearanceView textView = textViewWeak.get();
                    if (textView != null)
                        textView.setTypeface(typeface, textStyle);
                }
            }

            @Override
            public void onFontRetrievalFailed(int reason) {
            }
        };
        try {
            int resourceId = appearance.getResourceId(attributeId, 0);
            TypedValue mTypedValue = new TypedValue();
            Typeface typeface = carbon.internal.ResourcesCompat.getFont(((View) textView).getContext(), resourceId, mTypedValue, textStyle, fontWeight, replyCallback);
            if (typeface != null) {
                asyncFontPending.set(true);
                textView.setTypeface(typeface, textStyle);
            }
        } catch (UnsupportedOperationException | Resources.NotFoundException ignored) {
        }
    }

    public static void setTextAppearance(TextAppearanceView tv, int resid, boolean hasTextColor, boolean readAll) {
        TypedArray appearance = ((View) tv).getContext().obtainStyledAttributes(resid, R.styleable.TextAppearance);

        int textStyle = appearance.getInt(R.styleable.TextAppearance_android_textStyle, 0);
        int fontWeight = appearance.getInt(R.styleable.TextAppearance_carbon_fontWeight, 400);

        if (readAll) {
            for (int i = 0; i < appearance.getIndexCount(); i++) {
                int attr = appearance.getIndex(i);
                if (attr == R.styleable.TextAppearance_android_textSize) {
                    tv.setTextSize(appearance.getDimension(attr, 12));
                } else if (attr == R.styleable.TextAppearance_android_textColor) {
                    if (appearance.getColor(attr, 0) != ((View) tv).getResources().getColor(R.color.carbon_defaultColorControl))
                        tv.setTextColor(appearance.getColorStateList(attr));
                }
            }
        }

        for (int i = 0; i < appearance.getIndexCount(); i++) {
            int attr = appearance.getIndex(i);
            if (attr == R.styleable.TextAppearance_carbon_font) {
                Carbon.handleFontAttribute(tv, appearance, textStyle, fontWeight, attr);
            } else if (attr == R.styleable.TextAppearance_android_textAllCaps) {
                tv.setAllCaps(appearance.getBoolean(attr, true));
            } else if (!hasTextColor && attr == R.styleable.TextAppearance_android_textColor) {
                Carbon.initDefaultTextColor(tv, appearance, attr);
            }
        }

        appearance.recycle();
    }

}
