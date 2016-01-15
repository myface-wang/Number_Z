package cn.bmob.otaku.number_z.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * An {@link ImageView} layout that maintains a consistent width to height aspect ratio.
 */
public class MyImageView extends ImageView {

    private int mHeightRatio;
    private int mWidthRatio;

    public MyImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyImageView(Context context) {
        super(context);
    }

    public void setHeightRatio(int ratio,int width) {
        if (ratio != mHeightRatio) {
            mHeightRatio = ratio;
            mWidthRatio = width;
            requestLayout();
        }
    }

    public double getHeightRatio() {
        return mHeightRatio;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (mHeightRatio > 0.0) {
            // set the image views size

            int width =mWidthRatio;
            int height = mHeightRatio;
            int finwidth = MeasureSpec.getSize(widthMeasureSpec);
            int finheight=(finwidth*height)/width;

//            if (mHeightRatio<mWidthRatio){
//
//                width=mWidthRatio;
//            }else {
//                width = MeasureSpec.getSize(widthMeasureSpec);
//            }

//            int height = (int) (width * mHeightRatio);
            if (width>finwidth){
                setMeasuredDimension(finwidth, finheight);
            }
            else {
                setMeasuredDimension(width, height);
            }

        }
        else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
    }
}
