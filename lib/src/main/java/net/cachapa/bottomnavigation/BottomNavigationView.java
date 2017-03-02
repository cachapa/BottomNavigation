package net.cachapa.bottomnavigation;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.util.AttributeSet;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Interpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;

import com.transitionseverywhere.ChangeBounds;
import com.transitionseverywhere.Fade;
import com.transitionseverywhere.Transition;
import com.transitionseverywhere.TransitionManager;
import com.transitionseverywhere.TransitionSet;
import com.transitionseverywhere.TransitionValues;

import java.util.ArrayList;
import java.util.List;

public class BottomNavigationView extends LinearLayout implements View.OnClickListener {
    private static final int MAX_WIDTH = 600; // DPs
    private static final int DURATION = 400;
    private static final int WEIGHT_DESELECTED = 2;
    private static final int WEIGHT_SELECTED = 3;

    private static final Interpolator INTERPOLATOR = new FastOutSlowInInterpolator();

    private static final String SUPER_STATE = "super_state";
    private static final String SELECTED = "selectedIndex";

    private int selectedIndex;
    private List<BottomNavigationItemHolder> items;
    private OnMenuItemClickListener onMenuItemClickListener;

    private Rect insets = new Rect();

    private Transition changeBounds;
    private TransitionSet visibility;

    private int[] colors;
    private ArrayList<Circle> circles = new ArrayList<>();
    private Paint paint;

    public BottomNavigationView(Context context, AttributeSet attrs) {
        super(context, attrs);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setElevation(DisplayHelper.dpToPx(context, 8));
        }

        setWillNotDraw(false);
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStyle(Paint.Style.FILL);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.BottomNavigationView);
        int menuRes = a.getResourceId(R.styleable.BottomNavigationView_bnv_menu, 0);
        int colorListRes = a.getResourceId(R.styleable.BottomNavigationView_bnv_colors, 0);
        a.recycle();

        // Load menu
        PopupMenu popupMenu = new PopupMenu(context, null);
        popupMenu.getMenuInflater().inflate(menuRes, popupMenu.getMenu());
        items = new ArrayList<>(popupMenu.getMenu().size());

        // Load color list
        if (!isInEditMode() && colorListRes != 0) {
            a = context.getResources().obtainTypedArray(colorListRes);
            colors = new int[a.length()];
            for (int i = 0; i < a.length(); i++) {
                colors[i] = a.getColor(i, 0);
            }
            a.recycle();
        }

        for (int i = 0; i < popupMenu.getMenu().size(); i++) {
            MenuItem menuItem = popupMenu.getMenu().getItem(i);

            BottomNavigationItemHolder item = new BottomNavigationItemHolder(this, menuItem);
            item.getRootView().setOnClickListener(this);

            items.add(item);
            addView(item.getRootView());
        }

        changeBounds = new ChangeBounds()
                .setInterpolator(INTERPOLATOR)
                .setDuration(DURATION);

        visibility = new TransitionSet()
                .addTransition(new Alpha())
                .addTransition(new Fade())
                .setInterpolator(INTERPOLATOR)
                .setDuration(DURATION);

        updateSelectedItem();
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        Bundle state = new Bundle();

        state.putParcelable(SUPER_STATE, super.onSaveInstanceState());
        state.putInt(SELECTED, selectedIndex);

        return state;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        if (state instanceof Bundle) {
            Bundle bundle = (Bundle) state;
            state = bundle.getParcelable(SUPER_STATE);
            selectedIndex = bundle.getInt(SELECTED);
            updateSelectedItem();
        }
        super.onRestoreInstanceState(state);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int w = MeasureSpec.getSize(widthMeasureSpec) - insets.left - insets.right;

        float widthInDps = DisplayHelper.pxToDp(getContext(), w);
        int horizontalPadding = widthInDps > MAX_WIDTH
                ? (int) DisplayHelper.dpToPx(getContext(), (widthInDps - MAX_WIDTH) / 2)
                : 0;
        setPadding(horizontalPadding + insets.left, 0, horizontalPadding + insets.right, insets.bottom);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @SuppressWarnings("deprecation")
    @Override
    protected boolean fitSystemWindows(Rect insets) {
        this.insets = insets;
        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        for (Circle circle : circles) {
            int x = circle.view.getLeft() + circle.view.getWidth() / 2;
            int y = circle.view.getHeight() / 2;
            
            if (circle.color != 0) {
                int radius = Math.max(x, getWidth() - x);
                radius *= 1.05f; // Slightly increase radius to reach the corners
                paint.setColor(circle.color);
                canvas.drawCircle(x, y, radius * circle.progress, paint);
            }

            // Show selected feedback
            paint.setColor(Color.WHITE);
            paint.setAlpha((int) ((1 - circle.progress) * 100));
            canvas.drawCircle(x, y, circle.view.getWidth() / 2f * circle.progress, paint);
            paint.setAlpha(255);
        }
    }

    @Override
    public void onClick(View v) {
        setSelectedIndex(indexOfChild(v));
    }

    private class CircleAnimatorListener implements Animator.AnimatorListener, ValueAnimator.AnimatorUpdateListener {
        private Circle circle;

        CircleAnimatorListener(View view, int color) {
            circle = new Circle(view, color, 0f);
        }

        @Override
        public void onAnimationStart(Animator animation) {
            circles.add(circle);
        }

        @Override
        public void onAnimationEnd(Animator animation) {
            circles.remove(circle);
            setBackgroundColor(circle.color);
        }

        @Override
        public void onAnimationCancel(Animator animation) {
            circles.remove(circle);
        }

        @Override
        public void onAnimationRepeat(Animator animation) {
        }

        @Override
        public void onAnimationUpdate(ValueAnimator animation) {
            circle.progress = animation.getAnimatedFraction();
            invalidate();
        }
    }

    public void setOnMenuItemClickListener(OnMenuItemClickListener onMenuItemClickListener) {
        this.onMenuItemClickListener = onMenuItemClickListener;
    }

    public void setSelectedIndex(int index) {
        CircleAnimatorListener listener = new CircleAnimatorListener(getChildAt(index), getColor(index));
        ValueAnimator animator = ValueAnimator.ofFloat(0, 1).setDuration(DURATION);
        animator.setInterpolator(new FastOutSlowInInterpolator());
        animator.addListener(listener);
        animator.addUpdateListener(listener);
        animator.start();

        if (index == selectedIndex) {
            if (onMenuItemClickListener != null) {
                onMenuItemClickListener.onMenuItemReselected();
            }
            return;
        }

        // Prepare to animate all item locations
        for (int i = 0; i < getChildCount(); i++) {
            BottomNavigationItemHolder item = items.get(i);

            // Animate changing bounds for all views
            TransitionManager.beginDelayedTransition(item.getRootView(), changeBounds);

            if (i == selectedIndex || i == index) {
                // Animate visibility changes for selected and deselected views
                TransitionManager.beginDelayedTransition(item.getContainer(), visibility);
            }
        }

        selectedIndex = index;
        updateSelectedItem();

        if (onMenuItemClickListener != null) {
            onMenuItemClickListener.onMenuItemSelected(items.get(index).getIitem());
        }
    }

    public int getSelectedIndex() {
        return selectedIndex;
    }

    public MenuItem getSelectedItem() {
        return items.get(selectedIndex).getIitem();
    }

    @Override
    public void setBackgroundColor(int color) {
        if (color != 0) {
            super.setBackgroundColor(color);
        }
    }

    private void updateSelectedItem() {
        for (int i = 0; i < getChildCount(); i++) {
            BottomNavigationItemHolder item = items.get(i);
            item.setSelected(i == selectedIndex);
            ((LayoutParams) item.getRootView().getLayoutParams()).weight = i == selectedIndex ? WEIGHT_SELECTED : WEIGHT_DESELECTED;
        }

        if (circles.isEmpty()) {
            setBackgroundColor(getColor(selectedIndex));
        }
    }

    private int getColor(int index) {
        return colors == null || colors.length < index ? 0 : colors[index];
    }

    public interface OnMenuItemClickListener {
        void onMenuItemSelected(MenuItem item);

        void onMenuItemReselected();
    }

    private class Alpha extends Transition {
        @Override
        public void captureStartValues(TransitionValues transitionValues) {
            captureValues(transitionValues);
        }

        @Override
        public void captureEndValues(TransitionValues transitionValues) {
            captureValues(transitionValues);
        }

        private void captureValues(TransitionValues transitionValues) {
            transitionValues.values.put("alpha", transitionValues.view.getAlpha());
        }

        @Override
        public Animator createAnimator(ViewGroup sceneRoot, TransitionValues startValues, TransitionValues endValues) {
            if (startValues.view instanceof ImageView) {
                return ObjectAnimator.ofFloat(startValues.view, View.ALPHA, (float) startValues.values.get("alpha"), (float) endValues.values.get("alpha"));
            }
            return null;
        }
    }
}
