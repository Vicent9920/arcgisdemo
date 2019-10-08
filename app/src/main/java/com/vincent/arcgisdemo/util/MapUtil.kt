package com.vincent.arcgisdemo.util

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.view.LayoutInflater
import androidx.core.content.ContextCompat
import com.esri.arcgisruntime.geometry.*
import com.esri.arcgisruntime.mapping.view.Graphic
import com.esri.arcgisruntime.mapping.view.GraphicsOverlay
import com.esri.arcgisruntime.mapping.view.MapView
import com.esri.arcgisruntime.symbology.*
import com.haoge.easyandroid.easy.EasyToast
import com.vincent.arcgisdemo.R
import kotlin.math.pow
import kotlin.math.sqrt

// 地图工具类
object MapUtil {
    val mGraphicsOverlay = GraphicsOverlay()
    // 点集合
    private val mPointCollection = PointCollection(SpatialReferences.getWebMercator())


    // 绘制点
    fun drawPoint(p: Point) {
        //SimpleMarkerSymbol.Style有如下六个值，分别代表不同形状
        // SimpleMarkerSymbol.Style.CIRCLE 圆
        // SimpleMarkerSymbol.Style.CROSS  十字符号
        // SimpleMarkerSymbol.Style.DIAMOND 钻石
        // SimpleMarkerSymbol.Style.SQUARE 方形
        // SimpleMarkerSymbol.Style.TRIANGLE 三角形
        // SimpleMarkerSymbol.Style.X       X形状
        val simpleMarkerSymbol = SimpleMarkerSymbol(SimpleMarkerSymbol.Style.CIRCLE, Color.RED, 20f)
        val graphic = Graphic(p, simpleMarkerSymbol)
        //清除上一个点
        mGraphicsOverlay.graphics.clear()
        mGraphicsOverlay.graphics.add(graphic)
    }

    // 绘制直线
    fun drawLine(p: Point) {
        // 保存点
        mPointCollection.add(p)
        val polyline = Polyline(mPointCollection)

        //点 可不绘制
        drawPoint(p)

        //线
        //SimpleLineSymbol.Style 线段形状
        // SimpleLineSymbol.Style.DASH  - - - -
        // SimpleLineSymbol.Style.DASH_DOT--·--·--
        // SimpleLineSymbol.Style.DASH_DOT_DOT --··--·----··--·--
        // SimpleLineSymbol.Style.DOT ..........
        // SimpleLineSymbol.Style.NULL 不显示
        // SimpleLineSymbol.Style.SOLID  直线
        val simpleLineSymbol = SimpleLineSymbol(SimpleLineSymbol.Style.SOLID , Color.BLUE, 3f);
        val graphic = Graphic(polyline, simpleLineSymbol)
        mGraphicsOverlay.graphics.add(graphic)
    }

    // 绘制曲线
    fun drawCurves(p1: Point, p2: Point) {
        mPointCollection.add(p1)
        mPointCollection.add(p2)
        val polyline = Polyline(mPointCollection)
        val simpleLineSymbol = SimpleLineSymbol(SimpleLineSymbol.Style.SOLID, Color.BLUE, 3f);
        val graphic = Graphic(polyline, simpleLineSymbol)
        mGraphicsOverlay.graphics.add(graphic)
    }

    // 绘制多边形
    fun drawPolygon(p: Point) {
        mGraphicsOverlay.graphics.clear()
        mPointCollection.add(p)
        // 一个点无法构成一个面
        if (mPointCollection.size == 1) {
            drawPoint(p)
            return
        }
        val polygon = Polygon(mPointCollection)
        val lineSymbol = SimpleLineSymbol(SimpleLineSymbol.Style.SOLID, Color.GREEN, 3.0f)

        val simpleFillSymbol =
            SimpleFillSymbol(SimpleFillSymbol.Style.SOLID, Color.YELLOW, lineSymbol)
        val graphic = Graphic(polygon, simpleFillSymbol)
        mGraphicsOverlay.graphics.add(graphic)
    }

    // 绘制圆
    fun drawCircle(p: Point) {
        mGraphicsOverlay.graphics.clear()
        if (mPointCollection.size == 50) mPointCollection.clear()
        mPointCollection.add(p)
        // 只能确定圆心
        if (mPointCollection.size == 1) {
            drawPoint(p)
            return
        }
        // 根据勾三股四玄五的三角函数得到两个点之间的距离作为半径
        val x = mPointCollection[0].x - mPointCollection[1].x
        val y = mPointCollection[0].y - mPointCollection[1].y
        val radius = sqrt(x.pow(2.0) + y.pow(2.0))

        val center = mPointCollection[0]
        mPointCollection.clear()
        // 根据圆心和半径获取圆周的点
        for (point in getPoints(center, radius)) {
            mPointCollection.add(point)
        }
        val polygon = Polygon(mPointCollection)
        // 边线
        val lineSymbol = SimpleLineSymbol(SimpleLineSymbol.Style.SOLID, Color.GREEN, 3.0f)
        // 填充风格 填充颜色 填充边框
        val simpleFillSymbol =
            SimpleFillSymbol(SimpleFillSymbol.Style.SOLID, Color.YELLOW, lineSymbol)
        val graphic = Graphic(polygon, simpleFillSymbol)
        mGraphicsOverlay.graphics.add(graphic)
    }

    /**
     * 通过中心点和半径计算得出圆形的边线点集合
     *
     * @param center
     * @param radius
     * @return
     */
    private fun getPoints(center: Point, radius: Double): Array<Point?> {
        val points = arrayOfNulls<Point>(50)
        var sin: Double
        var cos: Double
        var x: Double
        var y: Double
        for (i in 0..49) {
            sin = kotlin.math.sin(Math.PI * 2.0 * i / 50)
            cos = kotlin.math.cos(Math.PI * 2.0 * i / 50)
            x = center.x + radius * sin
            y = center.y + radius * cos
            points[i] = Point(x, y)
        }
        return points
    }

    // 绘制图形marker
    fun drawMarker(p: Point, context: Context) {
        // 获取 drawable 资源
        val pinStarBlueDrawable =
            ContextCompat.getDrawable(context, R.mipmap.icon_marker_red) as BitmapDrawable?
        // 生成图片标记符号
//         val campsiteSymbol = PictureMarkerSymbol("图片网络地址")
        val campsiteSymbol = PictureMarkerSymbol.createAsync(pinStarBlueDrawable).get()

        // 异步加载
        campsiteSymbol.loadAsync()
        val attributes = HashMap<String, Any>()
        // 生成图画内容
        val pointGraphic =
            Graphic(p, attributes, campsiteSymbol)
        // 添加到图层
        mGraphicsOverlay.graphics.add(pointGraphic)
    }

    // 绘制文字
    fun drawText(p: Point) {
        // 水平方向有左 中 右
        // 水平左 TextSymbol.HorizontalAlignment.LEFT
        // 水平中 TextSymbol.HorizontalAlignment.CENTER
        // 水平右 TextSymbol.HorizontalAlignment.RIGHT
        // 垂直方向支持上中下
        // 垂直上 TextSymbol.VerticalAlignment.TOP
        // 垂直中 TextSymbol.VerticalAlignment.MIDDLE
        // 垂直下 TextSymbol.VerticalAlignment.BOTTOM
        val textSymbol = TextSymbol(
            20f, "标记文字", Color.RED,
            TextSymbol.HorizontalAlignment.CENTER, TextSymbol.VerticalAlignment.MIDDLE
        )
        // 生成绘画内容
        val graphic = Graphic(p, textSymbol)
        // 清除之前的内容
        mGraphicsOverlay.graphics.clear()
        // 添加到图层
        mGraphicsOverlay.graphics.add(graphic)
    }

    fun drawCallout(p: Point,mapView: MapView,context: Context) {
        val callout = mapView.callout
        if(callout.isShowing){
            callout.dismiss()
        }
        val view = LayoutInflater.from(context).inflate(R.layout.callout_delete_layout, null, false)
        view.setOnClickListener {
            callout.dismiss()
            EasyToast.DEFAULT.show("关闭标记")
        }
        callout.location = p
        callout.content = view
        callout.show()
    }

    fun restDrawStatus(){
        mGraphicsOverlay.graphics.clear()
        mPointCollection.clear()
    }
}