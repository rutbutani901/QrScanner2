package scanner.qrcodescanner.qrcodereader.barcodescanner.barcodereader.util

import android.widget.RelativeLayout
import android.util.AttributeSet
import android.content.Context

class SquareRelLayout : RelativeLayout {
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {}
    constructor(context: Context?) : super(context) {}

    override fun onMeasure(width: Int, height: Int) {

        val widthSize = MeasureSpec.getSize(width)
        val widthMode = MeasureSpec.getMode(width)
        val heightMode = MeasureSpec.getMode(height)
        val heightSize = MeasureSpec.getSize(height)

        val size = if (widthMode == MeasureSpec.EXACTLY && widthSize > 0) {
            widthSize
        } else if (heightMode == MeasureSpec.EXACTLY && heightSize > 0) {
            heightSize
        } else {
            if (widthSize < heightSize) widthSize else heightSize
        }
        val newSpec = MeasureSpec.makeMeasureSpec(size, MeasureSpec.EXACTLY)
        super.onMeasure(newSpec, newSpec)
    }
}