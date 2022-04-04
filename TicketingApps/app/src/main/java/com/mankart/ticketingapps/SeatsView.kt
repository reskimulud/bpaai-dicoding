package com.mankart.ticketingapps

import android.annotation.SuppressLint
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.graphics.Rect
import android.view.MotionEvent
import android.view.MotionEvent.ACTION_DOWN
import android.view.View
import androidx.core.content.res.ResourcesCompat

class SeatsView : View {
    private var seats: ArrayList<Seat> = arrayListOf()

    var seat: Seat? = null

    private val backgroundPaint = Paint()
    private val armrestPaint = Paint()
    private val bottomSeatPaint = Paint()
    private val mBounds = Rect()
    private val numberSeatPaint = Paint(Paint.FAKE_BOLD_TEXT_FLAG)
    private val titlePaint = Paint(Paint.FAKE_BOLD_TEXT_FLAG)

    constructor(context: android.content.Context) : super(context) {
        init()
    }
    constructor(context: android.content.Context, attrs: android.util.AttributeSet) : super(context, attrs) {
        init()
    }
    constructor(context: android.content.Context, attrs: android.util.AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
    }

    private fun init() {
        seats.apply {
            add(Seat(id = 1, name = "A1", isBooked = true))
            add(Seat(id = 2, name =  "A2", isBooked = false))
            add(Seat(id = 3, name = "B1", isBooked = false))
            add(Seat(id = 4, name = "B2", isBooked = false))
            add(Seat(id = 5, name =  "C1", isBooked = false))
            add(Seat(id = 6, name = "C2", isBooked = false))
            add(Seat(id = 7, name = "D1", isBooked = false))
            add(Seat(id = 8, name = "D2", isBooked = false))
        }
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        for (seat in seats) {
            drawSeat(canvas, seat)
        }

        val text = "Pick a seat"
        titlePaint.apply {
            textSize = 50F
        }
        canvas?.drawText(
            text,
            (width / 2) - (titlePaint.measureText(text) / 2),
            100F,
            titlePaint
        )
    }

    private fun drawSeat(canvas: Canvas?, seat: Seat) {
        // set color if seat is booked
        if (seat.isBooked) {
            backgroundPaint.color = ResourcesCompat.getColor(resources, R.color.grey_200, null)
            armrestPaint.color = ResourcesCompat.getColor(resources, R.color.grey_200, null)
            bottomSeatPaint.color = ResourcesCompat.getColor(resources, R.color.grey_200, null)
            numberSeatPaint.color = ResourcesCompat.getColor(resources, R.color.black, null)
        } else {
            backgroundPaint.color = ResourcesCompat.getColor(resources, R.color.blue_500, null)
            armrestPaint.color = ResourcesCompat.getColor(resources, R.color.blue_700, null)
            bottomSeatPaint.color = ResourcesCompat.getColor(resources, R.color.blue_500, null)
            numberSeatPaint.color = ResourcesCompat.getColor(resources, R.color.grey_200, null)
        }

        // save state
        canvas?.save()

        // background
        canvas?.translate(seat.x as Float, seat.y as Float)

        val backgroundPath = Path()
        backgroundPath.addRect(0F, 0F, 200F, 200F, Path.Direction.CCW)
        backgroundPath.addCircle(100F, 50F, 75F, Path.Direction.CCW)
        canvas?.drawPath(backgroundPath, backgroundPaint)

        // armrest
        val armrestPath = Path()
        armrestPath.addRect(0F, 0F, 50F, 200F, Path.Direction.CCW)
        canvas?.drawPath(armrestPath, armrestPaint)
        canvas?.translate(150F, 0F)
        armrestPath.addRect(0F, 0F, 50F, 200F, Path.Direction.CCW)
        canvas?.drawPath(armrestPath, armrestPaint)

        // bottom seat
        canvas?.translate(-150F, 175F)
        val bottomSeatPath = Path()
        bottomSeatPath.addRect(0F, 0F, 200F, 25F, Path.Direction.CCW)
        canvas?.drawPath(bottomSeatPath, bottomSeatPaint)

        // number seat
        canvas?.translate(0F, -175F)
        numberSeatPaint.apply {
            textSize = 50F
            numberSeatPaint.getTextBounds(
                seat.name,
                0,
                seat.name.length,
                mBounds
            )
        }
        canvas?.drawText(
            seat.name,
            100F - mBounds.centerX(),
            100F,
            numberSeatPaint
        )

        // restore state
        canvas?.restore()

    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val width = getDefaultSize(suggestedMinimumWidth, widthMeasureSpec)
        val height = getDefaultSize(suggestedMinimumHeight, heightMeasureSpec)

        val halfOfWidth = width / 2
        val halfOfHeight = height / 2
        var value = -600F

        for (i in 0..7) {
            if (i % 2 == 0) {
                seats[i].apply {
                    x = halfOfWidth - 300F
                    y = halfOfHeight + value
                }
            } else {
                seats[i].apply {
                    x = halfOfWidth + 100F
                    y = halfOfHeight + value
                }
                value += 300F
            }
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        val halfOfHeight = height / 2
        val halfOfWidth = width / 2

        val widthColumnOne = (halfOfWidth - 300F)..(halfOfWidth - 100F)
        val widthColumnTwo = (halfOfWidth + 100F)..(halfOfWidth + 300F)

        val heightRowOne = (halfOfHeight - 600F)..(halfOfHeight - 400F)
        val heightRowTwo = (halfOfHeight - 300F)..(halfOfHeight - 100F)
        val heightRowThree = (halfOfHeight + 0F)..(halfOfHeight + 200F)
        val heightRowFour = (halfOfHeight + 300F)..(halfOfHeight + 500F)

        when (event?.action) {
            ACTION_DOWN -> {
                if (event.x in widthColumnOne && event.y in heightRowOne) {
                    booking(0)
                } else if (event.x in widthColumnTwo && event.y in heightRowOne) {
                    booking(1)
                } else if (event.x in widthColumnOne && event.y in heightRowTwo) {
                    booking(2)
                } else if (event.x in widthColumnTwo && event.y in heightRowTwo) {
                    booking(3)
                } else if (event.x in widthColumnOne && event.y in heightRowThree) {
                    booking(4)
                } else if (event.x in widthColumnTwo && event.y in heightRowThree) {
                    booking(5)
                } else if (event.x in widthColumnOne && event.y in heightRowFour) {
                    booking(6)
                } else if (event.x in widthColumnTwo && event.y in heightRowFour) {
                    booking(7)
                }
            }
        }

        return true
    }

    private fun booking(id: Int) {
        for (seat in seats) {
            seat.isBooked = false
        }
        seats[id].apply {
            seat = this
            isBooked = true
        }
        invalidate()
    }
}