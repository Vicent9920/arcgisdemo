package com.vincent.arcgisdemo.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.esri.arcgisruntime.layers.ArcGISSceneLayer
import com.esri.arcgisruntime.mapping.ArcGISScene
import com.esri.arcgisruntime.mapping.ArcGISTiledElevationSource
import com.esri.arcgisruntime.mapping.Basemap
import com.esri.arcgisruntime.mapping.view.Camera
import kotlinx.android.synthetic.main.activity_scene_view.*


class SceneViewActivity : AppCompatActivity() {

    private val brest_buildings =
        " http://tiles.arcgis.com/tiles/P3ePLMYs2RVChkJx/arcgis/rest/services/Buildings_Brest/SceneServer"
    private val elevation_image_service =
        "http://elevation3d.arcgis.com/arcgis/rest/services/WorldElevation3D/Terrain3D/ImageServer"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.vincent.arcgisdemo.R.layout.activity_scene_view)
        val arcGISScene = ArcGISScene()
//        val arcGISTiledLayer =  ArcGISTiledLayer(
//            "https://services.arcgisonline.com/ArcGIS/rest/services/World_Imagery/MapServer")
//        arcGISScene.basemap = Basemap(arcGISTiledLayer)
//        sceneview.scene = arcGISScene

        arcGISScene.basemap = Basemap.createImagery()
        sceneview.scene = arcGISScene

        // add a scene service to the scene for viewing buildings
        val sceneLayer = ArcGISSceneLayer(brest_buildings)
        val elevationSource = ArcGISTiledElevationSource(elevation_image_service)
        arcGISScene.baseSurface.elevationSources.add(elevationSource)
//        arcGISScene.operationalLayers.add(sceneLayer)

        // 设置三维场景视角镜头（camera）
        //latitude——纬度;可能是负的
        //
        //longitude——经度;可能是负的
        //
        //altitude-海拔;可能是负的
        //
        //heading——镜头水平朝向;可能是负的
        //
        //pitch——镜头垂直朝向;不一定是负的
        //
        //roll-转动的角度
//        val camera =  Camera(48.378, -4.494, 200.0, 345.0, 65.0, 0.0)
        val camera = Camera(28.4, 83.9, 10010.0, 10.0, 80.0, 0.0)
        sceneview.setViewpointCamera(camera)


    }


    override fun onResume() {
        super.onResume()
        sceneview.resume()
    }


    override fun onPause() {
        super.onPause()
        sceneview.pause()
    }

    override fun onDestroy() {
        super.onDestroy()
        sceneview.dispose()
    }
}
