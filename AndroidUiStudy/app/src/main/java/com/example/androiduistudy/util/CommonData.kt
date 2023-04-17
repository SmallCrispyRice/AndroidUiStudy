package com.example.androiduistudy.util

object CommonData {
    /** 获取一组string类型数据 */
    fun getStrList(): MutableList<String> {
        val datas = mutableListOf<String>()
        for (i in 0..200){
            datas.add("第${i}数据")
        }
        return datas
    }

    fun getStrList(size:Int = 200): MutableList<String> {
        val datas = mutableListOf<String>()
        for (i in 0..size){
            datas.add("第${i}数据")
        }
        return datas
    }

    /** 返回A-Z字符数组 */
    fun getStrAToZ(): Array<String> {
        return arrayOf(
            "A", "B", "C", "D", "E", "F",
            "G", "H", "I", "J", "K", "L",
            "M", "N", "O", "P", "Q", "R",
            "S", "T", "U", "V", "W", "X",
            "Y", "Z"
        )
    }
}