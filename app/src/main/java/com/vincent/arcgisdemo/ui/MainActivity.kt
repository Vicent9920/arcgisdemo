package com.vincent.arcgisdemo.ui

import android.Manifest
import android.annotation.SuppressLint
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.view.MotionEvent
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.esri.arcgisruntime.geometry.*
import com.esri.arcgisruntime.io.RequestConfiguration
import com.esri.arcgisruntime.layers.ArcGISTiledLayer
import com.esri.arcgisruntime.layers.WebTiledLayer
import com.esri.arcgisruntime.mapping.ArcGISMap
import com.esri.arcgisruntime.mapping.Basemap
import com.esri.arcgisruntime.mapping.view.*
import com.esri.arcgisruntime.symbology.PictureMarkerSymbol
import com.esri.arcgisruntime.symbology.SimpleFillSymbol
import com.esri.arcgisruntime.symbology.SimpleLineSymbol
import com.esri.arcgisruntime.symbology.SimpleMarkerSymbol
import com.haoge.easyandroid.EasyAndroid
import com.haoge.easyandroid.easy.EasyLog
import com.haoge.easyandroid.easy.EasyPermissions
import com.haoge.easyandroid.easy.EasyToast
import com.lxj.xpopup.XPopup
import com.vincent.arcgisdemo.util.LayerType
import com.vincent.arcgisdemo.util.MapUtil
import com.vincent.arcgisdemo.util.MapUtil.mGraphicsOverlay
import com.vincent.arcgisdemo.util.TianDiTuMethodsClass
import kotlinx.android.synthetic.main.activity_main.*
import kotlin.math.roundToInt


@SuppressLint("Registered")
class MainActivity : AppCompatActivity() {

    private var drawType = -1
    // 草图编辑器
    private val mSketchEditor = SketchEditor()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.vincent.arcgisdemo.R.layout.activity_main)
        EasyAndroid.init(this)
        val mainBackgroundGrid = BackgroundGrid()
        mainBackgroundGrid.color = -0x1
        mainBackgroundGrid.gridLineColor = -0x1
        mainBackgroundGrid.gridLineWidth = 0f
        mapView.backgroundGrid = mainBackgroundGrid

        val levelOfDetail = 16
        val map = ArcGISMap(
            Basemap.Type.TOPOGRAPHIC, 30.671475859566514, //纬度
            104.07567785156248,//精度
            levelOfDetail//缩放级别（只能设置，不能获取，且必须大于0）
        )
        val url = "https://www.arcgis.com/home/item.html?id=7675d44bb1e4428aa2c30a9b68f97822"
        map.basemap.baseLayers.add(ArcGISTiledLayer(url))

        val webTiledLayer = TianDiTuMethodsClass.CreateTianDiTuTiledLayer(LayerType.TIANDITU_VECTOR_2000)
        val webTiledLayer1 = TianDiTuMethodsClass.CreateTianDiTuTiledLayer(LayerType.TIANDITU_VECTOR_ANNOTATION_CHINESE_2000)

        //注意：在100.2.0之后要设置RequestConfiguration
        val requestConfiguration =  RequestConfiguration()
        requestConfiguration.getHeaders().put("referer", "http://www.arcgis.com");
        webTiledLayer.setRequestConfiguration(requestConfiguration)
        webTiledLayer1.setRequestConfiguration(requestConfiguration)
        webTiledLayer.loadAsync()
        webTiledLayer1.loadAsync()
        val basemap =  Basemap(webTiledLayer)
        basemap.getBaseLayers().add(webTiledLayer1)
        map.basemap = basemap
        mapView.map = map

        mSketchEditor.opacity = 0.5f
        mapView.sketchEditor = mSketchEditor

        // 添加地图状态改变监听
        mapView.addDrawStatusChangedListener {
            // 绘制完成 另一种就是绘制中 DrawStatus.IN_PROGRESS
            if (it.drawStatus == DrawStatus.COMPLETED) {
                initLocal()
            }
        }

        // 未找到相关正确的资源，官网的图层已加载成功以后就异常了，未能正常演示
        mapView.addLayerViewStateChangedListener {
            if (it.layerViewStatus.iterator().next() == LayerViewStatus.OUT_OF_SCALE) {
                EasyLog.DEFAULT.e("LayerViewStatus.OUT_OF_SCALE")
            }
            EasyLog.DEFAULT.e("${it.layerViewStatus.iterator().next()}")
        }

        mapView.graphicsOverlays.add(MapUtil.mGraphicsOverlay)

        initEvent()
    }

    /**
     * 开始监听
     * 官方文档是需要下面两种权限，示例中是三种权限，区别在于 Manifest.permission.ACCESS_COARSE_LOCATION
     */
    private fun initLocal() = EasyPermissions.create(
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION
    )
        .callback {
            if (it) {
                val mLocationDisplay = mapView.locationDisplay
                mLocationDisplay.autoPanMode = LocationDisplay.AutoPanMode.OFF
                mLocationDisplay.startAsync()
                val pinStarBlueDrawable =
                    ContextCompat.getDrawable(
                        this,
                        com.vincent.arcgisdemo.R.mipmap.icon_marker_blue
                    ) as BitmapDrawable?
                val campsiteSymbol = PictureMarkerSymbol.createAsync(pinStarBlueDrawable).get()
                mLocationDisplay.addLocationChangedListener { event ->
                    // 查看返回的定位信息
//                    EasyLog.DEFAULT.e(event.location.position.toString())
                    mLocationDisplay.defaultSymbol = campsiteSymbol
//                    mLocationDisplay.isShowLocation = false//隐藏符号
//                    mLocationDisplay.isShowPingAnimation = false//隐藏位置更新的符号动画
//                    if (mLocationDisplay.isStarted) {
//                        mLocationDisplay.stop()
//                    }

//                    showMarker(event.location.position.x, event.location.position.y)


                }
            } else {
                EasyToast.DEFAULT.show("请打开相关所需要的权限，供后续测试")
            }
        }
        .request(this)


    private fun initEvent() {
        // 放大地图
        iv_add.setOnClickListener {
            mapView.setViewpointScaleAsync(mapView.mapScale * 0.5)
        }
        // 缩小地图
        iv_reduction.setOnClickListener {
            mapView.setViewpointScaleAsync(mapView.mapScale * 2)
        }
        val builder = XPopup.Builder(this).watchView(btn_marker)
        btn_marker.setOnClickListener {
            builder.asAttachList(
                arrayOf("点", "直线", "曲线", "多边形", "圆", "图片", "文字", "自定义标注"), null
            ) { position, _ ->
                MapUtil.restDrawStatus()
                drawType = position
            }
                .show()
        }
        val builder2 = XPopup.Builder(this).watchView(btn_sketch)
        btn_sketch.setOnClickListener {
            builder2.asAttachList(
                arrayOf("单点", "多点", "折线", "多边形", "徒手画线", "徒手画多边形", "上一步", "下一步"), null
            ) { position, _ ->
                MapUtil.restDrawStatus()
                when (position) {
                    0 -> mSketchEditor.start(SketchCreationMode.POINT)
                    1 -> mSketchEditor.start(SketchCreationMode.MULTIPOINT)
                    2 -> mSketchEditor.start(SketchCreationMode.POLYLINE)
                    3 -> mSketchEditor.start(SketchCreationMode.POLYGON)
                    4 -> mSketchEditor.start(SketchCreationMode.FREEHAND_LINE)
                    5 -> mSketchEditor.start(SketchCreationMode.FREEHAND_POLYGON)
                    6 -> if (mSketchEditor.canUndo()) mSketchEditor.undo()
                    7 -> if (mSketchEditor.canRedo()) mSketchEditor.redo()

                }

            }
                .show()
        }
        btn_rest.setOnClickListener {
            if (drawType != -1) {
                drawType = -1
                MapUtil.restDrawStatus()
            } else {
                if (!mSketchEditor.isSketchValid) {
                    mSketchEditor.stop()
                    return@setOnClickListener
                }
                // 从草图编辑器获得几何图形
                val sketchGeometry = mSketchEditor.geometry
                mSketchEditor.stop()
                if (sketchGeometry != null) {

                    //从草图编辑器创建一个图形
                    val graphic = Graphic(sketchGeometry)

                    // 根据几何类型分配符号
                    if (graphic.geometry.geometryType == GeometryType.POLYGON) {

                        val mLineSymbol =
                            SimpleLineSymbol(SimpleLineSymbol.Style.SOLID, 0xFF8800, 4f)
                        val mFillSymbol =
                            SimpleFillSymbol(SimpleFillSymbol.Style.CROSS, 0x40FFA9A9, mLineSymbol)
                        graphic.symbol = mFillSymbol
                    } else if (graphic.geometry.geometryType == GeometryType.POLYLINE) {
                        val mLineSymbol =
                            SimpleLineSymbol(SimpleLineSymbol.Style.SOLID, 0xFF8800, 4f)
                        graphic.symbol = mLineSymbol
                    } else if (graphic.geometry.geometryType == GeometryType.POINT ||
                        graphic.geometry.geometryType == GeometryType.MULTIPOINT
                    ) {
                        val mPointSymbol = SimpleMarkerSymbol(
                            SimpleMarkerSymbol.Style.SQUARE,
                            0xFF0000, 20f
                        )
                        graphic.symbol = mPointSymbol
                    }

                    // 将图形添加到图形覆盖层
                    mGraphicsOverlay.graphics.add(graphic)
                }
            }

        }

        mapView.onTouchListener = object : DefaultMapViewOnTouchListener(this, mapView) {
            override fun onSingleTapConfirmed(e: MotionEvent?): Boolean {
                e ?: super.onSingleTapConfirmed(e)
                if (drawType != -1) {
                    e ?: return false
                    val clickPoint = mMapView.screenToLocation(
                        android.graphics.Point(
                            e.x.roundToInt(),
                            e.y.roundToInt()
                        )
                    )
                    val projectedPoint = GeometryEngine.project(
                        clickPoint,
                        SpatialReference.create(4236)
                    ) as Point
                    when (drawType) {
                        0 -> MapUtil.drawPoint(clickPoint)
                        1 -> MapUtil.drawLine(clickPoint)
                        3 -> MapUtil.drawPolygon(clickPoint)
                        4 -> MapUtil.drawCircle(clickPoint)
                        5 -> MapUtil.drawMarker(clickPoint, this@MainActivity)
                        6 -> MapUtil.drawText(clickPoint)
                        7 -> MapUtil.drawCallout(clickPoint, mapView, this@MainActivity)
                    }
                }

                return super.onSingleTapConfirmed(e)
            }

            override fun onScroll(
                e1: MotionEvent?,
                e2: MotionEvent?,
                distanceX: Float,
                distanceY: Float
            ): Boolean {
                if (drawType == 2) {
                    e1 ?: return false
                    e2 ?: return false
                    val p1 = mMapView.screenToLocation(
                        android.graphics.Point(
                            e1.x.roundToInt(),
                            e1.y.roundToInt()
                        )
                    )
                    val p2 = mMapView.screenToLocation(
                        android.graphics.Point(
                            e2.x.roundToInt(),
                            e2.y.roundToInt()
                        )
                    )
                    MapUtil.drawCurves(p1, p2)
                    return true
                }
                return super.onScroll(e1, e2, distanceX, distanceY)
            }
        }
    }

    override fun onResume() {
        mapView.resume()
        super.onResume()
    }

    override fun onPause() {
        mapView.pause()
        super.onPause()
    }

    override fun onDestroy() {
        mapView.dispose()
        super.onDestroy()
    }
}