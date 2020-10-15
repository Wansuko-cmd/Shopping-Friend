package com.wsr.shopping_friend.repository

import android.icu.text.IDNA
import androidx.appcompat.widget.ActivityChooserView
import androidx.lifecycle.LiveData
import com.wsr.shopping_friend.info_list_database.InfoList
import com.wsr.shopping_friend.info_list_database.InfoListDao
import java.util.*

class AppRepository(private val infoListDao: InfoListDao) {
    val infoList: LiveData<MutableList<InfoList>> = infoListDao.getAll()

    //ローカルデータベースへの操作内容
    //データを挿入するための関数
    suspend fun insert(infoList: MutableList<InfoList>){
        infoListDao.insert(infoList)
    }

    fun getHelp(): List<InfoList>{
        return infoListDao.getHelp()
    }

    //タイトルを変更するための関数
    fun changeTitle(oldTitle: String, newTitle: String){
        infoListDao.changeTitle(oldTitle, newTitle)
    }

    //特定の要素を削除する関数
    suspend fun delete(infoList: InfoList){
        infoListDao.delete(infoList)
    }

    //リストに記載されている要素を削除する関数
    suspend fun deleteList(infoList: MutableList<InfoList>){
        infoListDao.deleteList(infoList)
    }

    //指定したタイトル名のものを削除する関数
    suspend fun deleteWithTitle(title:String){
        infoListDao.deleteWithTitle(title)
    }

    fun deleteAll(){
        infoListDao.deleteAll()
    }
}