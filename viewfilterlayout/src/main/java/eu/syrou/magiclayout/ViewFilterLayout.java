package eu.syrou.magiclayout;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

/**
 * Created by Syrou on 2016-09-18.
 */
public class ViewFilterLayout extends RelativeLayout {

    private static final String TAG = ViewFilterLayout.class.getSimpleName();

    SparseArray<View> mMagicLayoutChilden = new SparseArray<>();
    int priorityIterator = 1;


    public ViewFilterLayout(Context context) {
        super(context);
    }

    public ViewFilterLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initAttributes(context, attrs);
    }

    public ViewFilterLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttributes(context, attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public ViewFilterLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initAttributes(context, attrs);
    }

    @Override
    public RelativeLayout.LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new ViewFilterLayout.LayoutParams(getContext(), attrs);
    }

    @Override
    protected boolean checkLayoutParams(ViewGroup.LayoutParams p) {
        return p instanceof ViewFilterLayout.LayoutParams;
    }

    @Override
    protected ViewGroup.LayoutParams generateDefaultLayoutParams() {
        return new LayoutParams(getContext(), null);
    }

    private void initAttributes(Context context, AttributeSet attributeSet) {

        TypedArray arr = context.obtainStyledAttributes(attributeSet, R.styleable.ViewFilterLayout);
        //int priority = arr.getInteger(R.styleable.MagicLayout_hidePriority, 0);

        // do something here with your custom property

        arr.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //********************
        //Initialize
        //********************
        int rw = MeasureSpec.getSize(widthMeasureSpec);
        int rh = MeasureSpec.getSize(heightMeasureSpec);
        int totalChildHeight = 0;

        int numOfChildren = this.getChildCount();
        for (int i = 0; i < numOfChildren; i++) {
            View child = this.getChildAt(i);
            this.measureChild(child, widthMeasureSpec, heightMeasureSpec);
            int vw = child.getMeasuredWidth();
            int vh = child.getMeasuredHeight();
            if (child.getVisibility() != GONE) {
                totalChildHeight = totalChildHeight + vh;
            }

            if (child.getLayoutParams() instanceof ViewFilterLayout.LayoutParams) {
                ViewFilterLayout.LayoutParams layoutParams = (LayoutParams) child.getLayoutParams();
                if (layoutParams.hidePriority != -1 && child.getVisibility() != GONE) {
                    mMagicLayoutChilden.put(layoutParams.hidePriority, child);
                }
            }
        }

        applyMagic(totalChildHeight, rh);

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    private void applyMagic(int totalChildHeight, int layoutHeight) {
        if (mMagicLayoutChilden != null) {
            if (totalChildHeight > layoutHeight) {
                for (int i = priorityIterator; i < mMagicLayoutChilden.size() + 1; i++) {
                    View child = mMagicLayoutChilden.get(i);
                    if (child != null && child.getVisibility() != GONE) {
                        child.setVisibility(View.GONE);
                    }
                }
                //Log.d(TAG, "applyMagic toldChildHeight is larger");
            } else if (totalChildHeight < layoutHeight) {
                for (int i = mMagicLayoutChilden.size() + 1; i > priorityIterator; i--) {
                    View child = mMagicLayoutChilden.get(i);
                    if (child != null && child.getVisibility() == GONE) {
                        child.setVisibility(VISIBLE);
                    }
                }
            } else {
                return;
            }
            priorityIterator = 1;
        }
    }

    public class LayoutParams extends RelativeLayout.LayoutParams {

        private int hidePriority = -1;

        public LayoutParams(Context context, AttributeSet attrs) {
            super(context, attrs);
            TypedArray arr = context.obtainStyledAttributes(attrs, R.styleable.ViewFilterLayout_LayoutParams);
            hidePriority = arr.getInteger(R.styleable.ViewFilterLayout_LayoutParams_filterPriority, -1);
            arr.recycle();
        }

    }

}
