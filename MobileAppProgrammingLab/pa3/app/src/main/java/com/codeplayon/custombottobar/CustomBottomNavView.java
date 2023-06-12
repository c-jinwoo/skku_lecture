package com.codeplayon.custombottobar;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.util.AttributeSet;

import androidx.core.content.ContextCompat;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import edu.skku.cs.pa3.R;

public class CustomBottomNavView extends BottomNavigationView {
    private Path path;
    private Paint paint;
    private final int CURVE_CIRCLE_RADIUS = 128 / 2;
    private Point firstCurveStartPoint = new Point();
    private Point firstCurveEndPoint = new Point();
    private Point firstCurveControlPoint1 = new Point();
    private Point firstCurveControlPoint2 = new Point();
    @SuppressWarnings("FieldCanBeLocal")
    private Point secondCurveStartPoint = new Point();
    private Point secondCurveEndPoint = new Point();
    private Point secondCurveControlPoint1 = new Point();
    private Point secondCurveControlPoint2 = new Point();
    private int navigationBarWidth;
    private int navigationBarHeight;

    public CustomBottomNavView(Context context) {
        super(context);
        init();
    }
    public CustomBottomNavView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }
    public CustomBottomNavView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }
    private void init() {
        path = new Path();
        paint = new Paint();
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        paint.setColor(ContextCompat.getColor(getContext(), R.color.black));
        setBackgroundColor(Color.TRANSPARENT);
    }
    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        // Get width and height of the navigation bar
        // Navigation bar bounds (width & height)
        navigationBarWidth = getWidth();
        navigationBarHeight = getHeight();
        // The coordinates (x,y) of the start point before curve
        firstCurveStartPoint.set((navigationBarWidth / 2) - (CURVE_CIRCLE_RADIUS * 2) - (CURVE_CIRCLE_RADIUS / 3), 0);
        // The coordinates (x,y) of the end point after curve
        firstCurveEndPoint.set(navigationBarWidth / 2, CURVE_CIRCLE_RADIUS + (CURVE_CIRCLE_RADIUS / 4));
        // Same thing for the second curve
        secondCurveStartPoint = firstCurveEndPoint;
        secondCurveEndPoint.set((navigationBarWidth / 2) + (CURVE_CIRCLE_RADIUS * 2) + (CURVE_CIRCLE_RADIUS / 3), 0);
        // The coordinates (x,y) of the 1st control point on a cubic curve
        firstCurveControlPoint1.set(firstCurveStartPoint.x + CURVE_CIRCLE_RADIUS + (CURVE_CIRCLE_RADIUS / 4), firstCurveStartPoint.y);
        // The coordinates (x,y) of the 2nd control point on a cubic curve
        firstCurveControlPoint2.set(firstCurveEndPoint.x - (CURVE_CIRCLE_RADIUS * 2) + CURVE_CIRCLE_RADIUS, firstCurveEndPoint.y);
        secondCurveControlPoint1.set(secondCurveStartPoint.x + (CURVE_CIRCLE_RADIUS * 2) - CURVE_CIRCLE_RADIUS, secondCurveStartPoint.y);
        secondCurveControlPoint2.set(secondCurveEndPoint.x - (CURVE_CIRCLE_RADIUS + (CURVE_CIRCLE_RADIUS / 4)), secondCurveEndPoint.y);
        path.reset();
        path.moveTo(0, 0);
        path.lineTo(firstCurveStartPoint.x, firstCurveStartPoint.y);
        path.cubicTo(firstCurveControlPoint1.x, firstCurveControlPoint1.y,
                firstCurveControlPoint2.x, firstCurveControlPoint2.y,
                firstCurveEndPoint.x, firstCurveEndPoint.y);
        path.cubicTo(secondCurveControlPoint1.x, secondCurveControlPoint1.y,
                secondCurveControlPoint2.x, secondCurveControlPoint2.y,
                secondCurveEndPoint.x, secondCurveEndPoint.y);
        path.lineTo(navigationBarWidth, 0);
        path.lineTo(navigationBarWidth, navigationBarHeight);
        path.lineTo(0, navigationBarHeight);
        path.close();
    }
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawPath(path, paint);
    }
}