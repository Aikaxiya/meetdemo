package com.hw.meetdemo.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hw.meetdemo.R;

/**
 * @author: Andrew chen
 * @date: 2021/6/7 19:18
 * @description:
 */
public class MyImageButton extends LinearLayout {

    private ImageView myImageButton_imageView;
    private TextView myImageButton_textView;
    private final int myImageButton_imageWidth;
    private final int myImageButton_imageHeight;
    private final int myImageButton_imageSrc;
    private final String myImageButton_textContent;
    private int myImageButton_textFontSize = 14;
    private final int myImageButton_textFontColor;

    public MyImageButton(Context context) {
        this(context, null);
    }

    public MyImageButton(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyImageButton(Context context, AttributeSet attrs, int style) {
        super(context, attrs, style);
        //读取配置参数
        @SuppressLint("CustomViewStyleable") TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.SuperImageButton);
        myImageButton_imageWidth = (int) typedArray.getDimension(R.styleable.SuperImageButton_SuperImageButton_imageWidth, ViewGroup.LayoutParams.MATCH_PARENT);
        myImageButton_imageHeight = (int) typedArray.getDimension(R.styleable.SuperImageButton_SuperImageButton_imageHeight, ViewGroup.LayoutParams.MATCH_PARENT);
        myImageButton_imageSrc = typedArray.getResourceId(R.styleable.SuperImageButton_SuperImageButton_imageSrc, R.drawable.video_back);
        myImageButton_textContent = typedArray.getString(R.styleable.SuperImageButton_SuperImageButton_textContent);
        if (typedArray.hasValue(R.styleable.SuperImageButton_SuperImageButton_textFontSize)) {
            myImageButton_textFontSize = (int) typedArray.getDimension(R.styleable.SuperImageButton_SuperImageButton_textFontSize, myImageButton_textFontSize);
            myImageButton_textFontSize = px2sp(context, myImageButton_textFontSize);
        }
        myImageButton_textFontColor = typedArray.getColor(R.styleable.SuperImageButton_SuperImageButton_textFontColor, 0xffffffff);
        typedArray.recycle();
        //loadVIew
        loadView();
    }

    private void loadView() {
        //imageView
        myImageButton_imageView = new ImageView(getContext());
        LayoutParams imageView_lp = new LayoutParams(myImageButton_imageWidth, myImageButton_imageHeight);
        imageView_lp.gravity = Gravity.CENTER;
        imageView_lp.setMargins(5, 0, 5, 0);
        myImageButton_imageView.setLayoutParams(imageView_lp);
        myImageButton_imageView.setBackgroundColor(getResources().getColor(R.color.transparent_0));
        myImageButton_imageView.setImageResource(myImageButton_imageSrc);
        //textView
        myImageButton_textView = new TextView(getContext());
        myImageButton_textView.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
        myImageButton_textView.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
        myImageButton_textView.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT));
        myImageButton_textView.setTextSize(myImageButton_textFontSize);
        myImageButton_textView.setTextColor(myImageButton_textFontColor);
        myImageButton_textView.setText(myImageButton_textContent);
        myImageButton_textView.setGravity(Gravity.CENTER);
        //addView
        //addView(myImageButton_lineLayout);
        addView(myImageButton_imageView);
        addView(myImageButton_textView);
        setOnClickListener(v -> {
        });
    }

    // 将px值转换为sp值
    private int px2sp(Context context, float pxValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }

    /**
     * 设置文本
     */
    public void setText(String text) {
        myImageButton_textView.setEllipsize(TextUtils.TruncateAt.END);
        myImageButton_textView.setMaxLines(1);
        myImageButton_textView.setLines(1);
        myImageButton_textView.setText(text);
    }

    /**
     * 获取文本
     *
     * @return CharSequence
     */
    public CharSequence getText() {
        return myImageButton_textView.getText();
    }

    /**
     * 设置字体类型
     *
     * @param typeFace typeFace
     */
    public void setFontWeight(Typeface typeFace) {
        myImageButton_textView.setTypeface(typeFace);
    }

    /**
     * 设置字体大小
     */
    public void setFontSize(int fontSize) {
        myImageButton_textView.setTextSize(fontSize);
    }

    /**
     * 设置基础父级参数
     */
    public void setWidthHeight(int width, int height) {
        int margin = (int) getResources().getDimension(R.dimen.x15);
        LayoutParams layoutParams = new LayoutParams(width, height);
        //设置左右间距
        layoutParams.setMarginStart(margin);
        layoutParams.setMarginEnd(margin);
        layoutParams.gravity = Gravity.CENTER;//相对父级view居中
        this.setGravity(Gravity.CENTER);//设置内部居中
        this.setLayoutParams(layoutParams);
    }

    /**
     * 设置图标
     */
    public void setImageIcon(int resourceId, int width, int height, int marginLeft, int marginRight) {
        LayoutParams imageView_lp = new LayoutParams(width, height);
        imageView_lp.gravity = Gravity.CENTER;
        imageView_lp.setMargins(marginLeft, 0, marginRight, 0);
        myImageButton_imageView.setLayoutParams(imageView_lp);
        myImageButton_imageView.setImageResource(resourceId);
    }

    public void setImageIcon(int resourceId, int width, int height) {
        LayoutParams imageView_lp = new LayoutParams(width, height);
        imageView_lp.gravity = Gravity.CENTER;
        imageView_lp.setMargins(5, 0, 5, 0);
        myImageButton_imageView.setLayoutParams(imageView_lp);
        myImageButton_imageView.setImageResource(resourceId);
    }

    /**
     * 设置图标,第3个参数不设置则宽高等比
     */
    public void setImageIcon(int resourceId, int size) {
        setImageIcon(resourceId, size, size);
    }

}
