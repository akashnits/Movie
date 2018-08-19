package com.example.android.popularmovies.displayUtilities

import android.content.Context
import android.util.AttributeSet
import android.widget.ImageView

class MoviesImageView : ImageView {
  constructor(context: Context) : super(context) {}

  constructor(
    context: Context,
    attrs: AttributeSet
  ) : super(context, attrs) {
  }

  constructor(
    context: Context,
    attrs: AttributeSet,
    defStyleAttr: Int
  ) : super(context, attrs, defStyleAttr) {
  }

  override fun onMeasure(
    widthMeasureSpec: Int,
    heightMeasureSpec: Int
  ) {
    super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    setMeasuredDimension(measuredWidth, measuredHeight)
  }
}
