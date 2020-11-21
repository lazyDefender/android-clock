package com.example.clock.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import androidx.annotation.Nullable;

import com.example.clock.R;

import java.time.LocalTime;
import java.util.Calendar;

public class ClockView extends View {
    private int height = 0;
    private int width = 0;
    private int padding = 0;
    private int fontSize = 0;
    private int numeralSpacing = 0;
    private int handTruncation = 0;
    private int hourHandTruncation = 0;
    private int radius = 0;
    private Paint paint;
    private boolean isInit;
    private int[] numbers = {1,2,3,4,5,6,7,8,9,10,11,12};

    private Rect rect = new Rect();

    public ClockView(Context context) {
        super(context);
    }

    public ClockView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public ClockView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public ClockView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }


    private void initClock() {
        height = getHeight();
        width = getWidth();
        padding = numeralSpacing + 50;
        fontSize = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_SP,
                13,
                getResources().getDisplayMetrics()
        );

        int min = Math.min(height, width);
        radius = min / 2 - 150;
        handTruncation = min / 20;
        hourHandTruncation = min / 7;
        paint = new Paint();
        isInit = true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(!isInit) {
            initClock();
        }
//        canvas.drawColor(Color.WHITE);

//        drawCenter(canvas);
        drawNumeral(canvas);
        drawHands(canvas);
        drawCircle(canvas);

        postInvalidateDelayed(500);
        invalidate();
    }

    private void drawHand(Canvas canvas, double loc, int handLength, int color) {
        double angle = Math.PI * loc / 30 - Math.PI / 2;
        paint.setStrokeWidth(10);
        paint.setColor(color);
        canvas.drawLine(width / 2, height / 2,
                (float) (width / 2 + Math.cos(angle) * handLength),
                (float) (height / 2 + Math.sin(angle) * handLength),
                paint);
    }

    private void drawHands(Canvas canvas) {
        LocalTime time = LocalTime.now();
        int hour = time.getHour();
        int minute = time.getMinute();
        int second = time.getSecond();
        hour = hour > 12 ? hour - 12 : hour;
        int hourHandLength = radius - handTruncation - hourHandTruncation;
        int minuteHandLength = radius - (handTruncation + 40);
        int secondHandLength = radius - handTruncation;
        drawHand(canvas, (hour + minute / 60) * 5f, hourHandLength, R.color.colorAccent);
        drawHand(canvas, minute, minuteHandLength, R.color.colorAccent);
        drawHand(canvas, second, secondHandLength, Color.RED);
    }

    private void drawNumeral(Canvas canvas) {
        paint.setStrokeWidth(2);
        paint.setTextSize(fontSize);
        for(int number : numbers) {
            String top = String.valueOf(number);
            paint.getTextBounds(top, 0, top.length(), rect);
            double angle = Math.PI / 6 * (number - 3);
            int x = (int) (width / 2 + Math.cos(angle) * radius - rect.width() / 2);
            int y = (int) (height / 2 + Math.sin(angle) * radius + rect.height() / 2);
            canvas.drawText(top, x, y, paint);
        }
    }

    private void drawCenter(Canvas canvas) {
        paint.setStyle(Paint.Style.FILL);
        canvas.drawCircle(width /2, height / 2, 12, paint);
    }

    @SuppressLint("ResourceAsColor")
    private void drawCircle(Canvas canvas) {
        paint.reset();
        paint.setColor(R.color.colorAccent);
        paint.setStrokeWidth(15);
        paint.setStyle(Paint.Style.STROKE);
        paint.setAntiAlias(true);
        canvas.drawCircle(width / 2, height / 2, radius + padding - 10, paint);
    }


}
