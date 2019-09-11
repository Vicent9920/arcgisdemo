package cn.cddcjt;

import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.esri.arcgisruntime.data.ServiceFeatureTable;
import com.esri.arcgisruntime.geometry.Point;
import com.esri.arcgisruntime.layers.FeatureLayer;
import com.esri.arcgisruntime.layers.WebTiledLayer;
import com.esri.arcgisruntime.mapping.ArcGISMap;
import com.esri.arcgisruntime.mapping.Basemap;
import com.esri.arcgisruntime.mapping.view.BackgroundGrid;
import com.esri.arcgisruntime.mapping.view.Graphic;
import com.esri.arcgisruntime.mapping.view.GraphicsOverlay;
import com.esri.arcgisruntime.mapping.view.LocationDisplay;
import com.esri.arcgisruntime.mapping.view.MapView;
import com.esri.arcgisruntime.symbology.PictureMarkerSymbol;

import java.util.HashMap;
import java.util.Map;


public class TestActivity extends AppCompatActivity {
    MapView mMapView;
    private ArcGISMap map;
    private Basemap.Type baseMapType = Basemap.Type.STREETS_VECTOR;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        BackgroundGrid mainBackgroundGrid = new BackgroundGrid();
        mainBackgroundGrid.setColor(0xffffffff);
        mainBackgroundGrid.setGridLineColor(0xffffffff);
        mainBackgroundGrid.setGridLineWidth(0);
        mMapView = findViewById(R.id.mapView);
        mMapView.setBackgroundGrid(mainBackgroundGrid);
        int levelOfDetail = 16;
        map = new ArcGISMap(baseMapType, 30.671475859566514,//纬度
                104.07567785156248, //精度
                levelOfDetail);//缩放级别
        map.getBasemap().getBaseLayers().clear();
        WebTiledLayer webTiledLayer = TianDiTuMethodsClass.createTianDiTuTiledLayer(2000);
        WebTiledLayer webTiledLayerCh = TianDiTuMethodsClass.createTianDiTuTiledLayer(2001);
        map.getBasemap().getBaseLayers().add(webTiledLayer);
        map.getBasemap().getBaseLayers().add(webTiledLayerCh);
        mMapView.setMap(map);

        GraphicsOverlay mGraphicsOverlay = new GraphicsOverlay();
//            createPointGraphics(val);
        mMapView.getGraphicsOverlays().add(mGraphicsOverlay);


        LocationDisplay mLocationDisplay = mMapView.getLocationDisplay();
        mLocationDisplay.setAutoPanMode(LocationDisplay.AutoPanMode.RECENTER);
        mLocationDisplay.startAsync();
        addTrafficLayer();

        BitmapDrawable pinStarBlueDrawable = (BitmapDrawable) ContextCompat.getDrawable(this, R.mipmap.icon_marker_blue);
        PictureMarkerSymbol campsiteSymbol = new PictureMarkerSymbol(pinStarBlueDrawable);
        campsiteSymbol.loadAsync();
        Map<String, Object> attributes = new HashMap<>();
        Graphic pointGraphic = new Graphic(new Point(104.07567785156248, 30.671475859566514), attributes, campsiteSymbol);
        mGraphicsOverlay.getGraphics().add(pointGraphic);
    }

    private void addTrafficLayer() {
        String url = "http://80.2.21.21/arcgis/rest/services/Hosted/KD_RTIC1210/FeatureServer/0";//康定
        ServiceFeatureTable serviceFeatureTable = new ServiceFeatureTable(url);
        FeatureLayer featureLayer = new FeatureLayer(serviceFeatureTable);
        map.getOperationalLayers().add(featureLayer);
    }

    @Override
    protected void onPause() {
        if (mMapView != null) {
            mMapView.pause();
        }
        super.onPause();
    }

    @Override
    protected void onResume() {

        if (mMapView != null) {
            mMapView.resume();
        }
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        if (mMapView != null) {
            mMapView.dispose();
        }
        super.onDestroy();
    }
}
