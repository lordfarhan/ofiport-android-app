package org.ofiport.util

import android.content.Context
import android.net.Uri
import android.util.Log
import android.widget.Toast
import org.apache.poi.hssf.usermodel.HSSFWorkbook
import org.apache.poi.ss.usermodel.*
import org.apache.poi.ss.util.WorkbookUtil
import org.apache.poi.xssf.usermodel.XSSFSheet
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import java.io.InputStream
import java.io.OutputStream


/**
 * @author farhan
 * created at at 18:48 on 17/10/2020.
 */
object ExcelUtil {
  private val TAG = ExcelUtil::class.java.simpleName
  fun readExcelNew(context: Context, uri: Uri, filePath: String?): List<Map<Int, Any>?>? {
    var list: MutableList<Map<Int, Any>?>? = null
    val wb: Workbook
    if (filePath == null) {
      return null
    }
    if (!filePath.endsWith(".xls") && !filePath.endsWith(".xlsx")) {
      Log.e(TAG, "Please select the correct Excel file")
      return null
    }
    val extString: String = filePath.substring(filePath.lastIndexOf("."))
    val `is`: InputStream
    try {
      `is` = context.contentResolver.openInputStream(uri)!!
      Log.i(TAG, "readExcel: $extString")
      wb = when (extString) {
        ".xls" -> {
          HSSFWorkbook(`is`)
        }
        ".xlsx" -> {
          XSSFWorkbook(`is`)
        }
        else -> {
          HSSFWorkbook(`is`)
        }
      }
      // used to store data
      list = ArrayList()
      // get the first sheet
      val sheet: Sheet = wb.getSheetAt(0)
      // get the first line header
      val rowHeader: Row = sheet.getRow(0)
      val cellsCount: Int = rowHeader.physicalNumberOfCells
      //store header to the map
      val headerMap: MutableMap<Int, Any> = HashMap()
      for (c in 0 until cellsCount) {
        val value = getCellFormatValue(rowHeader.getCell(c))
        val cellInfo = "header ; c:$c; v:$value"
        Log.i(TAG, "readExcelNew: $cellInfo")
        headerMap[c] = value
      }
      //add  headermap to list
      list.add(headerMap)

      // get the maximum number of rows
      val rownum: Int = sheet.physicalNumberOfRows
      // get the maximum number of columns
      val colnum = headerMap.size
      //index starts from 1,exclude header.
      //if you want read line by line, index should from 0.
      for (i in 1 until rownum) {
        val row: Row = sheet.getRow(i)
        //storing subcontent
        val itemMap: MutableMap<Int, Any> = HashMap()
        for (j in 0 until colnum) {
          val value = getCellFormatValue(row.getCell(j))
          val cellInfo = "r: $i; c:$j; v:$value"
          Log.i(TAG, "readExcelNew: $cellInfo")
          itemMap[j] = value
        }
        list.add(itemMap)
      }
    } catch (e: Exception) {
      e.printStackTrace()
      Log.e(TAG, "readExcelNew: import error $e")
      Toast.makeText(context, "import error $e", Toast.LENGTH_SHORT).show()
    }
    return list
  }

  fun writeExcelNew(context: Context, exportExcel: List<Map<Int, Any>>, uri: Uri) {
    try {
      val workbook = XSSFWorkbook()
      val sheet: XSSFSheet = workbook.createSheet(WorkbookUtil.createSafeSheetName("Sheet1"))
      val colums = exportExcel[0].size
      for (i in 0 until colums) {
        //set the cell default width to 15 characters
        sheet.setColumnWidth(i, 15 * 256)
      }
      for (i in exportExcel.indices) {
        val row: Row = sheet.createRow(i)
        val integerObjectMap = exportExcel[i]
        for (j in 0 until colums) {
          val cell: Cell = row.createCell(j)
          cell.setCellValue(integerObjectMap[j].toString())
        }
      }
      val outputStream: OutputStream = context.contentResolver.openOutputStream(uri)!!
      workbook.write(outputStream)
      outputStream.flush()
      outputStream.close()
      Log.i(TAG, "writeExcel: export successful")
      Toast.makeText(context, "export successful", Toast.LENGTH_SHORT).show()
    } catch (e: Exception) {
      e.printStackTrace()
      Log.e(TAG, "writeExcel: error$e")
      Toast.makeText(context, "export error$e", Toast.LENGTH_SHORT).show()
    }
  }

  /**
   * get single cell data
   *
   * @param cell >
   * @return cell
   */
  private fun getCellFormatValue(cell: Cell?): Any {
    val cellValue: Any
    if (cell != null) {
      // 判断cell类型
      when (cell.cellType) {
        Cell.CELL_TYPE_BOOLEAN -> cellValue = cell.booleanCellValue
        Cell.CELL_TYPE_NUMERIC -> {
          cellValue = java.lang.String.valueOf(cell.numericCellValue)
        }
        Cell.CELL_TYPE_FORMULA -> {

          // determine if the cell is in date format
          cellValue = if (DateUtil.isCellDateFormatted(cell)) {
            // Convert to date format YYYY-mm-dd
            cell.dateCellValue
          } else {
            // Numeric
            java.lang.String.valueOf(cell.numericCellValue)
          }
        }
        Cell.CELL_TYPE_STRING -> {
          cellValue = cell.richStringCellValue.string
        }
        else -> cellValue = ""
      }
    } else {
      cellValue = ""
    }
    return cellValue
  }
}