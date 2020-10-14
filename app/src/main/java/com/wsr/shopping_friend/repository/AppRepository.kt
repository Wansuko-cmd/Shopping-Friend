package com.wsr.shopping_friend.repository

import androidx.lifecycle.LiveData
import com.wsr.shopping_friend.info_list_database.InfoList
import com.wsr.shopping_friend.info_list_database.InfoListDao

class AppRepository(private val infoListDao: InfoListDao) {
    val infoList: LiveData<MutableList<InfoList>> = infoListDao.getAll()

    //ローカルデータベースへの操作内容
    //データを挿入するための関数
    fun insert(infoList: InfoList){
        infoListDao.insert(infoList)
    }

    fun getHelp(): List<InfoList>{
        return infoListDao.getHelp()
    }

    /*
    fun update(id: String, number: Int, check: Boolean, item: String){
        infoListDao.update(id, number, check, item)
    }

    //チェックの状態を変更するための関数
    fun changeCheck(id: String, check: Boolean){
        infoListDao.changeCheck(id, check)
    }

    //指定したId名のものを削除する関数
    fun deleteWithId(id: String){
        infoListDao.deleteWithId(id)
    }*/

    //タイトルを変更するための関数
    fun changeTitle(oldTitle: String, newTitle: String){
        infoListDao.changeTitle(oldTitle, newTitle)
    }

    //指定したタイトル名のものを削除する関数
    fun deleteWithTitle(title:String){
        infoListDao.deleteWithTitle(title)
    }
}