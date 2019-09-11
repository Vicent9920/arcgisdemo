package cn.cddcjt;

import com.esri.arcgisruntime.arcgisservices.LevelOfDetail;
import com.esri.arcgisruntime.arcgisservices.TileInfo;
import com.esri.arcgisruntime.geometry.Envelope;
import com.esri.arcgisruntime.geometry.Point;
import com.esri.arcgisruntime.geometry.SpatialReference;
import com.esri.arcgisruntime.layers.WebTiledLayer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by 曹星 on 2018/11/21.
 */

public class TianDiTuMethodsClass {
    private static final List<String> SubDomain = Arrays.asList(new String[]{"t0", "t1", "t2", "t3", "t4", "t5", "t6", "t7"});
    private static final String URL_VECTOR_2000 = "http://map.pgis.sc/PGIS_S_TileMapServer/Maps/201812SL/EzMap?Service=getImage&Type=RGB&V=0.3&Col={col}&Row={row}&ZoomOffset={level}";
    private static final String URL_VECTOR_ANNOTATION_CHINESE_2000 = "http://map.pgis.sc/PGIS_S_TileMapServer/Maps/201812SL/EzMap?Service=getImage&Type=RGB&V=0.3&Col={col}&Row={row}&ZoomOffset={level}";
    private static final int DPI = 96;
    private static final int minZoomLevel = 1;
    private static final int maxZoomLevel = 18;
    private static final int tileWidth = 256;
    private static final int tileHeight = 256;
    private static final String LAYER_NAME_VECTOR = "vec";
    private static final String LAYER_NAME_VECTOR_ANNOTATION_CHINESE = "cva";


    private static final SpatialReference SRID_2000 = SpatialReference.create(4490);
    private static final double X_MIN_2000 = -180;
    private static final double Y_MIN_2000 = -90;
    private static final double X_MAX_2000 = 180;
    private static final double Y_MAX_2000 = 90;

    private static final Point ORIGIN_2000 = new Point(-180, 90, SRID_2000);
    private static final Envelope ENVELOPE_2000 = new Envelope(X_MIN_2000, Y_MIN_2000, X_MAX_2000, Y_MAX_2000, SRID_2000);

    private static final double[] SCALES = {
            2.958293554545656E8, 1.479146777272828E8,
            7.39573388636414E7, 3.69786694318207E7,
            1.848933471591035E7, 9244667.357955175,
            4622333.678977588, 2311166.839488794,
            1155583.419744397, 577791.7098721985,
            288895.85493609926, 144447.92746804963,
            72223.96373402482, 36111.98186701241,
            18055.990933506204, 9027.995466753102,
            4513.997733376551, 2256.998866688275,
            1128.4994333441375
    };

    private static final double[] RESOLUTIONS_2000 = {
            0.7031249999891485, 0.35156249999999994,
            0.17578124999999997, 0.08789062500000014,
            0.04394531250000007, 0.021972656250000007,
            0.01098632812500002, 0.00549316406250001,
            0.0027465820312500017, 0.0013732910156250009,
            0.000686645507812499, 0.0003433227539062495,
            0.00017166137695312503, 0.00008583068847656251,
            0.000042915344238281406, 0.000021457672119140645,
            0.000010728836059570307, 0.000005364418029785169};


    public static WebTiledLayer createTianDiTuTiledLayer(int layerType) {
        WebTiledLayer webTiledLayer = null;
        String mainUrl = "";
        String mainName = "";
        try {
            switch (layerType) {
                case 2000:
                    mainUrl = URL_VECTOR_2000;
                    mainName = LAYER_NAME_VECTOR;
                    break;
                case 2001:
                    mainUrl = URL_VECTOR_ANNOTATION_CHINESE_2000;
                    mainName = LAYER_NAME_VECTOR_ANNOTATION_CHINESE;
                    break;

            }
            List<LevelOfDetail> mainLevelOfDetail = new ArrayList<LevelOfDetail>();
            Point mainOrigin = null;

            for (int i = minZoomLevel; i <= maxZoomLevel; i++) {
                LevelOfDetail item = new LevelOfDetail(i, RESOLUTIONS_2000[i - 1], SCALES[i - 1]);
                mainLevelOfDetail.add(item);
            }
            Envelope mainEnvelope = ENVELOPE_2000;
            mainOrigin = ORIGIN_2000;

            TileInfo mainTileInfo = new TileInfo(
                    DPI,
                    TileInfo.ImageFormat.PNG24,
                    mainLevelOfDetail,
                    mainOrigin,
                    mainOrigin.getSpatialReference(),
                    tileHeight,
                    tileWidth
            );
            webTiledLayer = new WebTiledLayer(
                    mainUrl,
                    SubDomain,
                    mainTileInfo,
                    mainEnvelope);
            webTiledLayer.setName(mainName);
            webTiledLayer.loadAsync();
        } catch (Exception e) {
            e.getCause();
        }
        return webTiledLayer;
    }


}
