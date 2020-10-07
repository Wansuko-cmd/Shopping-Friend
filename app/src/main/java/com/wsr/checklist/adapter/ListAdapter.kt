package com.wsr.checklist.adapter

import android.content.Context
import android.graphics.Color
import android.text.Editable
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.recyclerview.widget.RecyclerView
import com.wsr.checklist.view_model.AppViewModel
import com.wsr.checklist.view_holder.ListViewHolder
import com.wsr.checklist.R
import com.wsr.checklist.info_list_database.InfoList
import com.wsr.checklist.type_file.CustomTextWatcher
import com.wsr.checklist.view_model.EditViewModel
import java.util.*

class ListAdapter(
    private val context: Context,
    var title: String,
    private val viewModel: AppViewModel,
    private val editViewModel: EditViewModel):
    RecyclerView.Adapter<ListViewHolder>() {

    //チェックリストのタイトルを全て格納する変数
    var titleList = mutableListOf<String>()

    //新しくアイテム欄を作成した際に使用される変数
    var focus: Int = -1

    //ViewHolderのインスタンスを形成
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.add_checklist, parent, false)
        return ListViewHolder(view)
    }

    //LiveDataの入っている変数の長さを返す関数
    override fun getItemCount(): Int {
        return editViewModel.getList().size
    }

    //ViewHolderのインスタンスの保持する値を変更
    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        //データベースの情報を格納するためのプロセス
        for (i in editViewModel.getList()) {
            if (holder.adapterPosition == i.number) {
                holder.check.isChecked = editViewModel.getCheck(i.id)
                holder.item.setText(editViewModel.getItem(i.id))
                break
            }
        }

        //チェックのついてないところを色付けするためのプロセス
        if (holder.check.isChecked) {
            holder.view.setBackgroundColor(Color.parseColor("#FFFFFF"))
        } else {
            holder.view.setBackgroundColor(Color.parseColor("#AFEEEE"))
        }

        //新しく作成したアイテム欄にカーソルを合わせる処理
        if (focus == holder.adapterPosition) {
            holder.item.requestFocus()
            focus = -1
        }

        //アイテムが変更されたときにeditViewModelの保持する値を変更するためのプロセス
        holder.item.addTextChangedListener(object : CustomTextWatcher {
            override fun afterTextChanged(p0: Editable?) {
                for (i in editViewModel.getList()) {
                    if (holder.adapterPosition == i.number) {
                        //一番下の要素の中でエンターキーを押した際に新しく空欄を作る機能
                        if (p0.toString().endsWith("\n") && (editViewModel.checkEmpty(i.id))) {
                            addElements()
                        }
                        else if (p0.toString() != i.item) editViewModel.changeItem(i.id, p0.toString())
                        break
                    }
                }
            }
        })

        //チェックの状態が変更したときにデータベースに保存するためのプロセス
        holder.check.setOnClickListener {
            for (i in editViewModel.getList()) {
                if (holder.adapterPosition == i.number) {
                    viewModel.changeCheck(i.id, holder.check.isChecked)
                    break
                }
            }
        }

        holder.delete.setOnClickListener {
            for (i in editViewModel.getList()) {
                if (holder.adapterPosition == i.number) {
                    editViewModel.delete(i.id)
                    notifyDataSetChanged()
                    break
                }
            }
        }
    }

    //LiveDataの値が変更した際に実行される関数
    internal fun setInfoList(lists: MutableList<InfoList>) {
        lists.sortBy { it.number }
        for (numOfTitle in lists) {
            if (!titleList.contains(numOfTitle.title)) {
                titleList.add(numOfTitle.title)
            }
        }
        //最初は代入、それ以降はチェックの値の更新をする
        if (editViewModel.getList() == emptyList<InfoList>()) {
            for (numOfTitle in lists) {
                if (numOfTitle.title == title) {
                    editViewModel.insert(numOfTitle)
                }
            }
        } else {
            for (numOfTitle in lists) {
                if (numOfTitle.title == title) {
                    editViewModel.changeCheck(numOfTitle.id, numOfTitle.check)
                }
            }
        }
        notifyDataSetChanged()
    }

    fun addElements(){
        val id = UUID.randomUUID().toString()
        viewModel.insert(InfoList(id, editViewModel.getList().size, title, false, ""))
        editViewModel.insert(InfoList(id, editViewModel.getList().size, title, false, ""))
        //recyclerView.scrollToPosition(adapter.list.size-1)
        focus = editViewModel.getList().size
        //notifyDataSetChanged()
    }
}