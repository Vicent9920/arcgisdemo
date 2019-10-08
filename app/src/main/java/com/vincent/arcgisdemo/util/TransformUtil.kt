package com.vincent.arcgisdemo.util

import com.esri.arcgisruntime.geometry.Point
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

/**
 * <p>文件描述：坐标转换工具类<p>
 * <p>@author 烤鱼<p>
 * <p>@date 2019/10/1 0001 <p>
 * <p>@update 2019/10/1 0001<p>
 * <p>版本号：1<p>
 *
 */
object TransformUtil {

    /**
     * 是否在国内
     */
    private fun outOfChina(lat: Double, lng: Double): Boolean {
        if (lng < 72.004 || lng > 137.8347) {
            return true
        }
        return lat < 0.8293 || lat > 55.8271
    }

    // 解密纬度
    private fun transformLat(x: Double, y: Double): Double {
        var ret =
            -100.0 + 2.0 * x + 3.0 * y + 0.2 * y * y + 0.1 * x * y + 0.2 * sqrt(abs(x))
        ret += (20.0 * sin(6.0 * x * Math.PI) + 20.0 * sin(2.0 * x * Math.PI)) * 2.0 / 3.0
        ret += (20.0 * sin(y * Math.PI) + 40.0 * sin(y / 3.0 * Math.PI)) * 2.0 / 3.0
        ret += (160.0 * sin(y / 12.0 * Math.PI) + 320 * sin(y * Math.PI / 30.0)) * 2.0 / 3.0
        return ret
    }
    // 解密经度
    private fun transformLon(x: Double, y: Double): Double {
        var ret = 300.0 + x + 2.0 * y + 0.1 * x * x + 0.1 * x * y + 0.1 * sqrt(abs(x))
        ret += (20.0 * sin(6.0 * x * Math.PI) + 20.0 * sin(2.0 * x * Math.PI)) * 2.0 / 3.0
        ret += (20.0 * sin(x * Math.PI) + 40.0 * sin(x / 3.0 * Math.PI)) * 2.0 / 3.0
        ret += (150.0 * sin(x / 12.0 * Math.PI) + 300.0 * sin(x / 30.0 * Math.PI)) * 2.0 / 3.0
        return ret
    }

    /**
     * 测算经纬度差值
     * @param lat 纬度
     * @param lng 经度
     * @return delta[0] 是纬度差，delta[1]是经度差
     */
    private fun delta(lat: Double, lng: Double): DoubleArray {
        val delta = DoubleArray(2)
        val a = 6378137.0
        val ee = 0.00669342162296594323
        val dLat = transformLat(lng - 105.0, lat - 35.0)
        val dLng = transformLon(lng - 105.0, lat - 35.0)
        val radLat = lat / 180.0 * Math.PI
        var magic = sin(radLat)
        magic = 1 - ee * magic * magic
        val sqrtMagic = sqrt(magic)
        delta[0] = dLat * 180.0 / (a * (1 - ee) / (magic * sqrtMagic) * Math.PI)
        delta[1] = dLng * 180.0 / (a / sqrtMagic * cos(radLat) * Math.PI)
        return delta
    }

    /**
     * WSG84 转 GCJ02 坐标
     * @param lat 纬度
     * @param lng 经度
     */
    fun  wsG84toGCJ02Point(latitude:Double,longitude:Double) : Point {
        if (outOfChina(latitude, longitude)) {
            return Point(latitude, longitude)
        }
        val delta = delta(latitude, longitude)
          return Point(latitude + delta[0], longitude + delta[1])
    }

    /**
     * GCJo2 转 WGS84 坐标
     * @param lat 纬度
     * @param lng 经度
     */
    fun  gcJo2toWGS84Point(latitude:Double,longitude:Double):Point {
        if (TransformUtil.outOfChina(latitude, longitude)) {
            return Point(latitude, longitude)
        }
        val delta = delta(latitude, longitude)
        return Point(latitude - delta[0], longitude - delta[1])
    }

}