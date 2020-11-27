package com.chwishay.commonlib.tools

import jxl.Workbook
import jxl.WorkbookSettings
import jxl.format.Alignment
import jxl.format.Border
import jxl.format.BorderLineStyle
import jxl.format.Colour
import jxl.write.Label
import jxl.write.WritableCellFormat
import jxl.write.WritableFont
import java.io.File
import java.io.FileInputStream

//                       _ooOoo_
//                      o8888888o
//                      88" . "88
//                      (| -_- |)
//                       O\ = /O
//                   ____/`---'\____
//                 .   ' \\| |// `.
//                  / \\||| : |||// \
//                / _||||| -:- |||||- \
//                  | | \\\ - /// | |
//                | \_| ''\---/'' | |
//                 \ .-\__ `-` ___/-. /
//              ______`. .' /--.--\ `. . __
//           ."" '< `.___\_<|>_/___.' >'"".
//          | | : `- \`.;`\ _ /`;.`/ - ` : | |
//            \ \ `-. \_ __\ /__ _/ .-` / /
//    ======`-.____`-.___\_____/___.-`____.-'======
//                       `=---='
//
//    .............................................
//             佛祖保佑             永无BUG
/**
 * author:RanQing
 * date:2020/11/26 0026 11:10
 * description:
 */
class ExcelUtil {

    companion object {
        val instance: ExcelUtil by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) { ExcelUtil() }
    }

    private val sheetFont: WritableFont =
        WritableFont(WritableFont.ARIAL, 14, WritableFont.BOLD).apply {
            colour = Colour.LIGHT_BLUE
        }
    private val sheetFormat = WritableCellFormat(sheetFont).apply {
        alignment = Alignment.CENTRE
        setBorder(Border.ALL, BorderLineStyle.THIN)
        setBackground(Colour.VERY_LIGHT_YELLOW)
    }

    private val titleFont = WritableFont(WritableFont.ARIAL, 10, WritableFont.BOLD)
    private val titleFormat = WritableCellFormat(titleFont).apply {
        alignment = Alignment.CENTRE
        setBorder(Border.ALL, BorderLineStyle.THIN)
        setBackground(Colour.GRAY_25)
    }

    private val contentFont = WritableFont(WritableFont.ARIAL, 12)
    private val contentFormat = WritableCellFormat(contentFont).apply {
        setBorder(Border.ALL, BorderLineStyle.THIN)
    }

    private var fileName: String? = null

    fun initExcel(fileName: String, sheetName: String, colNames: Array<String>): ExcelUtil {
        this.fileName = fileName
        val dir = File(fileName.substring(0, fileName.lastIndexOf("/")))
        val file = File(fileName)
        if (!file.exists() || file.isDirectory) {
            file.createNewFile()
        }
        val workbook = Workbook.createWorkbook(file)
        val sheet = workbook.createSheet(sheetName, 0).apply {
            addCell(Label(0, 0, fileName, sheetFormat))
            colNames.forEachIndexed { index, s ->
                addCell(Label(index, 0, s, titleFormat))
            }
            setRowView(0, 340)
        }
        workbook.write()
        workbook.close()
        return this
    }

    fun <T> export2Excel(dataList: List<T>) {
        if (fileName.isNullOrEmpty()) {
            throw NullPointerException("请输入正确的文件名")
        }
        dataList.apply {
            WorkbookSettings().encoding = "UTF-8"
            FileInputStream(File(fileName)).let {
                val workbook = Workbook.getWorkbook(it)
                val writebook = Workbook.createWorkbook(File(fileName), workbook)
                val sheet = writebook.getSheet(0)
                dataList.forEachIndexed { j, t ->
                    if (t is TestModel) {
                        val itemList = arrayListOf(t.name, t.age.toString(), t.gender)
                        itemList.forEachIndexed { i, s ->
                            sheet.addCell(Label(i, j, s, contentFormat))
                            sheet.setColumnView(i, s.length + (if (s.length > 4) 5 else 8))
                        }
                        sheet.setRowView(j + 1, 350)
                    }
                }
                writebook.write()
                writebook.close()
                it.close()
            }
        }
    }
}

class TestModel(val name: String, val age: Int, val gender: String)