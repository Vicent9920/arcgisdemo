package cn.cddcjt.ui

import android.annotation.SuppressLint
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import cn.cddcjt.R
import cn.cddcjt.TianDiTuMethodsClass
import com.esri.arcgisruntime.data.ServiceFeatureTable
import com.esri.arcgisruntime.geometry.Point
import com.esri.arcgisruntime.layers.FeatureLayer
import com.esri.arcgisruntime.mapping.ArcGISMap
import com.esri.arcgisruntime.mapping.Basemap
import com.esri.arcgisruntime.mapping.view.BackgroundGrid
import com.esri.arcgisruntime.mapping.view.Graphic
import com.esri.arcgisruntime.mapping.view.GraphicsOverlay
import com.esri.arcgisruntime.mapping.view.LocationDisplay
import com.esri.arcgisruntime.symbology.PictureMarkerSymbol
import kotlinx.android.synthetic.main.activity_main.*
import java.util.HashMap

@SuppressLint("Registered")
class MainActivity :AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val mainBackgroundGrid = BackgroundGrid()
        mainBackgroundGrid.color = -0x1
        mainBackgroundGrid.gridLineColor = -0x1
        mainBackgroundGrid.gridLineWidth = 0f

        mapView.backgroundGrid = mainBackgroundGrid
        val levelOfDetail = 16
        val map = ArcGISMap(
            Basemap.Type.STREETS_VECTOR, 30.671475859566514, //纬度
            104.07567785156248, //精度
            levelOfDetail
        )//缩放级别
        map.basemap.baseLayers.clear()
        val webTiledLayer = TianDiTuMethodsClass.createTianDiTuTiledLayer(2000)
        val webTiledLayerCh = TianDiTuMethodsClass.createTianDiTuTiledLayer(2001)
        map.basemap.baseLayers.add(webTiledLayer)
        map.basemap.baseLayers.add(webTiledLayerCh)
        mapView.map = map

        val mGraphicsOverlay = GraphicsOverlay()
        mapView.graphicsOverlays.add(mGraphicsOverlay)


        val mLocationDisplay = mapView.locationDisplay
        mLocationDisplay.autoPanMode = LocationDisplay.AutoPanMode.RECENTER
        mLocationDisplay.startAsync()

        val url = "http://80.2.21.21/arcgis/rest/services/Hosted/KD_RTIC1210/FeatureServer/0"
        val serviceFeatureTable = ServiceFeatureTable(url)
        val featureLayer = FeatureLayer(serviceFeatureTable)
        map.operationalLayers.add(featureLayer)

        val pinStarBlueDrawable =
            ContextCompat.getDrawable(this, R.mipmap.icon_marker_blue) as BitmapDrawable?
        val campsiteSymbol = PictureMarkerSymbol(pinStarBlueDrawable!!)
        campsiteSymbol.loadAsync()
        val attributes = HashMap<String, Any>()
        val pointGraphic =
            Graphic(Point(104.07567785156248, 30.671475859566514), attributes, campsiteSymbol)
        mGraphicsOverlay.graphics.add(pointGraphic)
    }
}