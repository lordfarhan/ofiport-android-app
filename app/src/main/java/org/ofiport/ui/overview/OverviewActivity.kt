package org.ofiport.ui.overview

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.PopupMenu
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import org.ofiport.R
import org.ofiport.data.Transaction
import org.ofiport.databinding.ActivityOverviewBinding
import org.ofiport.helper.DateTimeHelper
import org.ofiport.helper.ItemDecorationHelper
import org.apache.poi.ss.usermodel.Cell
import org.apache.poi.ss.usermodel.Row
import org.apache.poi.ss.util.WorkbookUtil
import org.apache.poi.xssf.usermodel.XSSFSheet
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import java.io.OutputStream
import java.text.NumberFormat


/**
 * @author farhan
 * created at at 13:01 on 15/10/2020.
 */
class OverviewActivity : AppCompatActivity() {

  private lateinit var binding: ActivityOverviewBinding
  private lateinit var viewModel: OverviewViewModel
  private lateinit var adapter: OverviewAdapter
  private var allTransactions: List<Transaction> = ArrayList()
  private var currentMonthTransactions: List<Transaction> = ArrayList()

  companion object {
    const val CREATE_REQUEST = 0
    const val UPDATE_REQUEST = 1
    const val DIR_SELECTOR_CODE = 2

    const val ID = "org.ofiport.data.Transaction.id"
    const val TYPE = "org.ofiport.data.Transaction.type"
    const val DESCRIPTION = "org.ofiport.data.Transaction.description"
    const val AMOUNT = "org.ofiport.data.Transaction.amount"
    const val DATE = "org.ofiport.data.Transaction.date"

    const val ACTION = "action"
    const val EXPORT_OPTION = "export_option"
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    binding = DataBindingUtil.setContentView(this, R.layout.activity_overview)
    viewModel = ViewModelProvider(this).get(OverviewViewModel::class.java)

    binding.imageViewActionBack.setOnClickListener { onBackPressed() }

    binding.apply {
      textViewMonth.text = String.format(
        "%s %s",
        DateTimeHelper.getMonth(viewModel.currMonth.toInt()),
        viewModel.currYear
      )

      adapter = OverviewAdapter(this@OverviewActivity)
      recyclerViewTransaction.adapter = adapter
      recyclerViewTransaction.addItemDecoration(
        ItemDecorationHelper(
          this@OverviewActivity,
          R.dimen.medium_space
        )
      )

      subscribeInterface()

      imageViewActionPrevious.setOnClickListener {
        viewModel.prevMonth()
        textViewMonth.text = String.format(
          "%s %s",
          DateTimeHelper.getMonth(viewModel.currMonth.toInt()),
          viewModel.currYear
        )
        subscribeInterface()
      }
      imageViewActionNext.setOnClickListener {
        viewModel.nextMonth()
        textViewMonth.text = String.format(
          "%s %s",
          DateTimeHelper.getMonth(viewModel.currMonth.toInt()),
          viewModel.currYear
        )
        subscribeInterface()
      }
      imageViewActionOption.setOnClickListener { onActionMenuPressed() }
      floatingActionButtonActionExport.setOnClickListener { openFolderSelector(0) }
    }
  }

  private fun subscribeInterface() {
    viewModel.get().observe(this, {
      val monthTransactions = viewModel.getTransactionsInMonth(it)
      adapter.submitList(monthTransactions)
      currentMonthTransactions = monthTransactions
      allTransactions = viewModel.getAllTransactions(it)
    })
  }

  private fun onActionMenuPressed() {
    val popupMenu: PopupMenu = PopupMenu(this, binding.imageViewActionOption)
    popupMenu.menuInflater.inflate(R.menu.menu_overview, popupMenu.menu)
    popupMenu.setOnMenuItemClickListener { item ->
      when (item.itemId) {
        R.id.action_exportThisMonth -> openFolderSelector(0)
        R.id.action_exportAll -> openFolderSelector(1)
      }
      true
    }
    popupMenu.show()
  }

  /**
   * open the local filer and select the folder
   */
  private fun openFolderSelector(exportOption: Int) {
    val intent = Intent(Intent.ACTION_CREATE_DOCUMENT)
    intent.type = "application/*"
    intent.putExtra(EXPORT_OPTION, exportOption)

    when (exportOption) {
      0 -> {
        intent.putExtra(
          Intent.EXTRA_TITLE, String.format(
            "%s_%s_%s.xlsx",
            DateTimeHelper.getMonth(viewModel.currMonth.toInt()),
            viewModel.currYear,
            System.currentTimeMillis().toString()
          )
        )
      }
      else -> {
        intent.putExtra(
          Intent.EXTRA_TITLE, String.format(
            "SemuaTransaksi_%s.xlsx",
            System.currentTimeMillis().toString()
          )
        )
      }
    }
    startActivityForResult(intent, DIR_SELECTOR_CODE)
  }

  override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
    super.onActivityResult(requestCode, resultCode, data)

    if (requestCode == CREATE_REQUEST && resultCode == RESULT_OK) {
      val transaction = Transaction(
        type = data!!.getIntExtra(TYPE, 0),
        description = data.getStringExtra(DESCRIPTION)!!,
        date = data.getStringExtra(DATE)!!,
        amount = data.getLongExtra(AMOUNT, 0),
        createdAt = DateTimeHelper.getCurrentTimestamp(),
        updatedAt = DateTimeHelper.getCurrentTimestamp()
      )
      viewModel.insert(transaction)
      Toast.makeText(this, "Berhasil menyimpan", Toast.LENGTH_SHORT).show()
    } else if (requestCode == UPDATE_REQUEST && resultCode == RESULT_OK) {
      val id = data!!.getLongExtra(ID, -1)
      val transaction = Transaction(
        type = data.getIntExtra(TYPE, 0),
        description = data.getStringExtra(DESCRIPTION)!!,
        date = data.getStringExtra(DATE)!!,
        amount = data.getLongExtra(AMOUNT, 0),
        createdAt = DateTimeHelper.getCurrentTimestamp(),
        updatedAt = DateTimeHelper.getCurrentTimestamp()
      )
      transaction.id = id
      if (id == -1L) {
        Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show()
      } else {
        if (data.getStringExtra(ACTION) == "delete") {
          viewModel.delete(transaction)
          Toast.makeText(this, "Berhasil menghapus", Toast.LENGTH_SHORT).show()
        } else {
          viewModel.update(transaction)
          Toast.makeText(this, "Berhasil memperbarui", Toast.LENGTH_SHORT).show()
        }
      }
    } else if (requestCode == DIR_SELECTOR_CODE && resultCode == RESULT_OK) {
      val uri: Uri = data?.data ?: return
      Toast.makeText(this, "Sedang mengekspor...", Toast.LENGTH_SHORT).show()
      //you can modify readExcelList, then write to excel.
      when (val exportOption = data.getIntExtra(EXPORT_OPTION, 1)) {
        0 -> {
          writeExcel(currentMonthTransactions, uri, exportOption)
        }
        else -> {
          writeExcel(allTransactions, uri, exportOption)
        }
      }
    }
  }

  private fun writeExcel(exportExcel: List<Transaction>, uri: Uri, exportOption: Int) {
    try {
      val workbook = XSSFWorkbook()
      val sheet: XSSFSheet = when (exportOption) {
        0 -> {
          workbook.createSheet(
            WorkbookUtil.createSafeSheetName(
              String.format(
                "%s %s",
                DateTimeHelper.getMonth(viewModel.currMonth.toInt()),
                viewModel.currYear
              )
            )
          )
        }
        else -> {
          workbook.createSheet(
            WorkbookUtil.createSafeSheetName(
              "Semua Transaksi"
            )
          )
        }
      }
      val col = 6
      for (i in 0 until col) {
        //set the cell default width to 15 characters
        sheet.setColumnWidth(i, 15 * 256)
      }
      val rowHead = sheet.createRow(0)
      val cellNumber = rowHead.createCell(0)
      val cellDate = rowHead.createCell(1)
      val cellDesc = rowHead.createCell(2)
      val cellIncome = rowHead.createCell(3)
      val cellOutcome = rowHead.createCell(4)
      val cellBalance = rowHead.createCell(5)
      cellNumber.setCellValue("No.")
      cellDate.setCellValue("Tanggal")
      cellDesc.setCellValue("Deskripsi")
      cellIncome.setCellValue("Pemasukan")
      cellOutcome.setCellValue("Pengeluaran")
      cellBalance.setCellValue("Saldo")
      exportExcel.forEachIndexed { index, transaction ->
        val row: Row = sheet.createRow(index + 1)
        for (j in 0 until col) {
          val cell: Cell = row.createCell(j)
          if (transaction.type == 0) {
            when {
              j == 0 -> cell.setCellValue((index + 1).toString())
              j == 5 -> cell.setCellValue(
                String.format(
                  "Rp. %s",
                  NumberFormat.getInstance().format(viewModel.getBalance(exportExcel, index))
                )
              )
              j != 3 -> cell.setCellValue(transactionContent(transaction, j))
            }
          } else {
            when {
              j == 0 -> cell.setCellValue((index + 1).toString())
              j == 5 -> cell.setCellValue(
                String.format(
                  "Rp. %s",
                  NumberFormat.getInstance().format(viewModel.getBalance(exportExcel, index))
                )
              )
              j != 4 -> cell.setCellValue(transactionContent(transaction, j))
            }
          }
        }
      }
      val outputStream: OutputStream = contentResolver.openOutputStream(uri)!!
      workbook.write(outputStream)
      outputStream.flush()
      outputStream.close()
      Toast.makeText(this, "Ekspor berhasil", Toast.LENGTH_SHORT).show()
    } catch (e: Exception) {
      e.printStackTrace()
      Toast.makeText(this, "Ekspor gagal, kode: $e", Toast.LENGTH_SHORT).show()
    }
  }

  private fun transactionContent(transaction: Transaction, pos: Int): String {
    return when (pos) {
      1 -> DateTimeHelper.getDateFormattedFromTimestamp(transaction.date)
      2 -> transaction.description
      3 -> String.format(
        "Rp. %s",
        NumberFormat.getInstance().format(transaction.amount)
      )
      4 -> String.format(
        "Rp. %s",
        NumberFormat.getInstance().format(transaction.amount)
      )
      else -> pos.toString()
    }
  }
}