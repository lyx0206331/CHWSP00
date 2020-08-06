package com.chwishay.chwsp00.activity

import android.content.Context
import android.content.Intent
import android.graphics.Color
import com.chwishay.chwsp00.R
import com.chwishay.chwsp00.baseComponent.BaseActivity
import com.chwishay.chwsp00.views.ChartAxisValueFormatter
import com.chwishay.commonlib.tools.CommUtil
import com.chwishay.commonlib.tools.orDefault
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.formatter.StackedValueFormatter
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import kotlinx.android.synthetic.main.activity_chart.*

class ChartActivity : BaseActivity() {

    companion object {
        @JvmStatic
        fun startActivity(context: Context) {
            context.startActivity(Intent(context, ChartActivity::class.java))
        }
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_chart
    }

    override fun initVariables() {
    }

    override fun initViews() {
        barChart.description.isEnabled = false
        barChart.setMaxVisibleValueCount(40)
        barChart.setPinchZoom(false)
        barChart.setDrawGridBackground(false)
        barChart.setDrawBarShadow(false)
        barChart.setDrawValueAboveBar(false)
        barChart.isHighlightFullBarEnabled = false

        val leftAxis = barChart.axisLeft
        leftAxis.valueFormatter = ChartAxisValueFormatter("k")
        leftAxis.axisMinimum = 0f
        barChart.axisRight.isEnabled = false

        val xLabels = barChart.xAxis
        xLabels.position = XAxis.XAxisPosition.BOTTOM

        val legend = barChart.legend
        legend.verticalAlignment = Legend.LegendVerticalAlignment.BOTTOM
        legend.horizontalAlignment = Legend.LegendHorizontalAlignment.RIGHT
        legend.orientation = Legend.LegendOrientation.HORIZONTAL
        legend.setDrawInside(false)
        legend.formSize = 8f
        legend.formToTextSpace = 4f
        legend.xEntrySpace = 6f

        barChart.setOnChartValueSelectedListener(object : OnChartValueSelectedListener {
            override fun onNothingSelected() {
            }

            override fun onValueSelected(e: Entry?, h: Highlight?) {
                e?.let {
                    val entry = e as BarEntry
                    if (entry.yVals != null) {
                        CommUtil.logE("VAL SELECTED", "Value:${entry.yVals[h?.stackIndex.orDefault()]}")
                    } else {
                        CommUtil.logE("VAL SELECTED", "Value:${entry.y}")
                    }
                }
            }
        })
    }

    override fun loadData() {
        test()
        barChart.animateY(1000)
    }

    private fun test() {
        val values = arrayListOf<BarEntry>()
        for (i in 0 .. 20) {
            values.add(BarEntry(i+0f, floatArrayOf((Math.random() * 15).toFloat(), (Math.random() * 15).toFloat(), (Math.random() * 15).toFloat()), null))
        }

        var dataSet: BarDataSet
        if (barChart.data != null && barChart.data.dataSetCount > 0) {
            dataSet = barChart.data.getDataSetByIndex(0) as BarDataSet
            dataSet.values = values
            barChart.data.notifyDataChanged()
            barChart.notifyDataSetChanged()
        } else {
            dataSet = BarDataSet(values, "Test Data")
            dataSet.setDrawIcons(false)
            dataSet.setColors(Color.CYAN, Color.LTGRAY, Color.YELLOW)
            dataSet.stackLabels = arrayOf("data1", "data2", "data3")

            val dataSets = arrayListOf<IBarDataSet>()
            dataSets.add(dataSet)

            val data = BarData(dataSets)
            data.setValueFormatter(StackedValueFormatter(false, "", 1))
            data.setValueTextColor(Color.BLUE)
//            data.setDrawValues(false)

            barChart.data = data
        }

        barChart.setFitBars(true)
        barChart.invalidate()
    }
}