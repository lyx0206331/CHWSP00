package com.chwishay.chwsp00.views

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.annotation.IntDef

object Gravity {
    const val TOP = 0
    const val BOTTOM = 1
    const val CENTER = 2
}
class RangeSeekBar(context: Context, val attrs: AttributeSet? = null): View(context, attrs) {

    companion object {
        const val SEEKBAR_MODE_SINGLE = 1
        const val SEEKBAR_MODE_RANGE = 2

        const val TICK_MARK_MODE_NUMBER = 0
        const val TICK_MARK_MODE_OTHER = 1

        const val TICK_MARK_GRAVITY_LEFT = 0
        const val TICK_MARK_GRAVITY_CENTER = 1
        const val TICK_MARK_GRAVITY_RIGHT = 2
    }

    /**
     * @hide
     */
    @IntDef(SEEKBAR_MODE_SINGLE, SEEKBAR_MODE_RANGE)
    @Retention(AnnotationRetention.SOURCE)
    annotation class SeekBarModeDef

    /**
     * @hide
     */
    @IntDef(TICK_MARK_MODE_NUMBER, TICK_MARK_MODE_OTHER)
    @Retention(AnnotationRetention.SOURCE)
    annotation class TickMarkModeDef

    /**
     * @hide
     */
    @IntDef(TICK_MARK_GRAVITY_LEFT, TICK_MARK_GRAVITY_CENTER, TICK_MARK_GRAVITY_RIGHT)
    @Retention(AnnotationRetention.SOURCE)
    annotation class TickMarkGravityDef

    /**
     * @hide
     */
    @IntDef(Gravity.TOP, Gravity.BOTTOM)
    @Retention(AnnotationRetention.SOURCE)
    annotation class TickMarkLayoutGravityDef

    /**
     * @hide
     */
    @IntDef(Gravity.BOTTOM, Gravity.CENTER, Gravity.TOP)
    @Retention(AnnotationRetention.SOURCE)
    annotation class GravityDef
}