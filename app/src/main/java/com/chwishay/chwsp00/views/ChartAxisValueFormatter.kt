package com.chwishay.chwsp00.views

import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.formatter.ValueFormatter
import java.text.DecimalFormat

//  ┏┓　　　┏┓
//┏┛┻━━━┛┻┓
//┃　　　　　　　┃
//┃　　　━　　　┃
//┃　┳┛　┗┳　┃
//┃　　　　　　　┃
//┃　　　┻　　　┃
//┃　　　　　　　┃
//┗━┓　　　┏━┛
//   ┃　　　┃   神兽保佑
//   ┃　　　┃   代码无BUG！
//   ┃　　　┗━━━┓
//   ┃　　　　　　　┣┓
//   ┃　　　　　　　┏┛
//   ┗┓┓┏━┳┓┏┛
//     ┃┫┫　┃┫┫
//     ┗┻┛　┗┻┛
/**
 * Author:RanQing
 * Create time:20-8-4 下午4:52
 * Description:
 **/
class ChartAxisValueFormatter(val suffix: String): ValueFormatter() {

    private val format: DecimalFormat = DecimalFormat("###,###,###,##0.0")

    override fun getFormattedValue(value: Float): String {
        return "${format.format(value)}${suffix}"
    }

    override fun getAxisLabel(value: Float, axis: AxisBase?): String {
        return when {
            axis is XAxis -> format.format(value)
            value > 0 -> "${format.format(value)}$suffix"
            else -> format.format(value)
        }
    }

}