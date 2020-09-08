package com.chwishay.chwsp00.btUtil

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Switch
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.chwishay.chwsp00.R
import com.chwishay.commonlib.tools.orDefault
import com.chwishay.commonlib.tools.showShortToast
import com.clj.fastble.data.BleDevice
import org.jetbrains.anko.find
import org.jetbrains.anko.sdk27.coroutines.onCheckedChange
import org.jetbrains.anko.sdk27.coroutines.onClick

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
 * date:2020/9/8 0008 16:23
 * description:
 */
class NotifyAdapter(val saveListener: ((String) -> Unit)) :
    RecyclerView.Adapter<NotifyAdapter.NotifyViewHolder>() {

    class NotifyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvDevName = itemView.findViewById<TextView>(R.id.tvDeviceName)
        val tvReceiveSpeed = itemView.find<TextView>(R.id.tvReceiveSpeed)
        val tvTotalData = itemView.find<TextView>(R.id.tvTotalData)
        val switchNotify = itemView.find<Switch>(R.id.switchNotify)
        val etFileName = itemView.find<EditText>(R.id.etFileName)
        val btnSave = itemView.find<Button>(R.id.btnSave)
        val btnClear = itemView.find<Button>(R.id.btnClear)
        val tvData = itemView.find<TextView>(R.id.tvData)
    }

    var devices: ArrayList<BleDevice>? = null
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    var totalData = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotifyViewHolder {
        return NotifyViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_bt_notify, parent, false)
        )
    }

    override fun onBindViewHolder(holder: NotifyViewHolder, position: Int) {
        devices?.get(position)?.apply {
            holder.tvDevName.text = "$name($mac)"
        }
        holder.btnSave.onClick {
            val fileName = holder.etFileName.text
            if (fileName.isNullOrEmpty() || fileName.trim().isNullOrEmpty()) {
                holder.btnSave.context.showShortToast("请输入文件名")
            } else {
                saveListener.invoke(fileName.trim().toString())
            }
        }
        holder.btnClear.onClick {
            holder.tvData.text = ""
        }
        holder.switchNotify.onCheckedChange { buttonView, isChecked ->
//            isChecked
        }
    }

    override fun getItemCount(): Int {
        return devices?.size.orDefault()
    }
}