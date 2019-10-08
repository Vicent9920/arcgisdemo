package com.vincent.arcgisdemo.util

import com.esri.arcgisruntime.arcgisservices.LevelOfDetail
import com.esri.arcgisruntime.arcgisservices.TileInfo
import com.esri.arcgisruntime.geometry.Envelope
import com.esri.arcgisruntime.geometry.Point
import com.esri.arcgisruntime.geometry.SpatialReference
import com.esri.arcgisruntime.layers.WebTiledLayer

object TianDiTuMethodsClass {
    val key = "58a2d8db46a9ea6d009aca88bc8d2a53"
    private val SubDomain = arrayOf("t0", "t1", "t2", "t3", "t4", "t5", "t6", "t7")
    private val URL_VECTOR_2000 =
        "http://{subDomain}.tianditu.com/DataServer?T=vec_c&x={col}&y={row}&l={level}&tk=$key"
    private val URL_VECTOR_ANNOTATION_CHINESE_2000 =
        "http://{subDomain}.tianditu.com/DataServer?T=cva_c&x={col}&y={row}&l={level}&tk=$key"
    private val URL_VECTOR_ANNOTATION_ENGLISH_2000 =
        "http://{subDomain}.tianditu.com/DataServer?T=eva_c&x={col}&y={row}&l={level}&tk=$key"
    private val URL_IMAGE_2000 =
        "http://{subDomain}.tianditu.com/DataServer?T=img_c&x={col}&y={row}&l={level}&tk=$key"
    private val URL_IMAGE_ANNOTATION_CHINESE_2000 =
        "http://{subDomain}.tianditu.com/DataServer?T=cia_c&x={col}&y={row}&l={level}&tk=$key"
    private val URL_IMAGE_ANNOTATION_ENGLISH_2000 =
        "http://{subDomain}.tianditu.com/DataServer?T=eia_c&x={col}&y={row}&l={level}&tk=$key"
    private val URL_TERRAIN_2000 =
        "http://{subDomain}.tianditu.com/DataServer?T=ter_c&x={col}&y={row}&l={level}&tk=$key"
    private val URL_TERRAIN_ANNOTATION_CHINESE_2000 =
        "http://{subDomain}.tianditu.com/DataServer?T=cta_c&x={col}&y={row}&l={level}&tk=$key"

    private val URL_VECTOR_MERCATOR =
        "http://{subDomain}.tianditu.com/DataServer?T=vec_w&x={col}&y={row}&l={level}&tk=$key"
    private val URL_VECTOR_ANNOTATION_CHINESE_MERCATOR =
        "http://{subDomain}.tianditu.com/DataServer?T=cva_w&x={col}&y={row}&l={level}&tk=$key"
    private val URL_VECTOR_ANNOTATION_ENGLISH_MERCATOR =
        "http://{subDomain}.tianditu.com/DataServer?T=eva_w&x={col}&y={row}&l={level}&tk=$key"
    private val URL_IMAGE_MERCATOR =
        "http://{subDomain}.tianditu.com/DataServer?T=img_w&x={col}&y={row}&l={level}&tk=$key"
    private val URL_IMAGE_ANNOTATION_CHINESE_MERCATOR =
        "http://{subDomain}.tianditu.com/DataServer?T=cia_w&x={col}&y={row}&l={level}&tk=$key"
    private val URL_IMAGE_ANNOTATION_ENGLISH_MERCATOR =
        "http://{subDomain}.tianditu.com/DataServer?T=eia_w&x={col}&y={row}&l={level}&tk=$key"
    private val URL_TERRAIN_MERCATOR =
        "http://{subDomain}.tianditu.com/DataServer?T=ter_w&x={col}&y={row}&l={level}&tk=$key"
    private val URL_TERRAIN_ANNOTATION_CHINESE_MERCATOR =
        "http://{subDomain}.tianditu.com/DataServer?T=cta_w&x={col}&y={row}&l={level}&tk=$key"

    private val DPI = 96
    private val minZoomLevel = 1
    private val maxZoomLevel = 18
    private val tileWidth = 256
    private val tileHeight = 256
    private val LAYER_NAME_VECTOR = "vec"
    private val LAYER_NAME_VECTOR_ANNOTATION_CHINESE = "cva"
    private val LAYER_NAME_VECTOR_ANNOTATION_ENGLISH = "eva"
    private val LAYER_NAME_IMAGE = "img"
    private val LAYER_NAME_IMAGE_ANNOTATION_CHINESE = "cia"
    private val LAYER_NAME_IMAGE_ANNOTATION_ENGLISH = "eia"
    private val LAYER_NAME_TERRAIN = "ter"
    private val LAYER_NAME_TERRAIN_ANNOTATION_CHINESE = "cta"

    private val SRID_2000 = SpatialReference.create(4490)
    private val SRID_MERCATOR = SpatialReference.create(102100)
    private val X_MIN_2000 = -180.0
    private val Y_MIN_2000 = -90.0
    private val X_MAX_2000 = 180.0
    private val Y_MAX_2000 = 90.0

    private val X_MIN_MERCATOR = -20037508.3427892
    private val Y_MIN_MERCATOR = -20037508.3427892
    private val X_MAX_MERCATOR = 20037508.3427892
    private val Y_MAX_MERCATOR = 20037508.3427892
    private val ORIGIN_2000 = Point(-180.0, 90.0, SRID_2000)
    private val ORIGIN_MERCATOR = Point(-20037508.3427892, 20037508.3427892, SRID_MERCATOR)
    private val ENVELOPE_2000 = Envelope(X_MIN_2000, Y_MIN_2000, X_MAX_2000, Y_MAX_2000, SRID_2000)
    private val ENVELOPE_MERCATOR =
        Envelope(X_MIN_MERCATOR, Y_MIN_MERCATOR, X_MAX_MERCATOR, Y_MAX_MERCATOR, SRID_MERCATOR)

    private val SCALES = doubleArrayOf(
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
    )
    private val RESOLUTIONS_MERCATOR = doubleArrayOf(
        78271.51696402048, 39135.75848201024,
        19567.87924100512, 9783.93962050256,
        4891.96981025128, 2445.98490512564,
        1222.99245256282, 611.49622628141,
        305.748113140705, 152.8740565703525,
        76.43702828517625, 38.21851414258813,
        19.109257071294063, 9.554628535647032,
        4.777314267823516, 2.388657133911758,
        1.194328566955879, 0.5971642834779395,
        0.298582141738970
    )

    private val RESOLUTIONS_2000 = doubleArrayOf(
        0.7031249999891485, 0.35156249999999994,
        0.17578124999999997, 0.08789062500000014,
        0.04394531250000007, 0.021972656250000007,
        0.01098632812500002, 0.00549316406250001,
        0.0027465820312500017, 0.0013732910156250009,
        0.000686645507812499, 0.0003433227539062495,
        0.00017166137695312503, 0.00008583068847656251,
        0.000042915344238281406, 0.000021457672119140645,
        0.000010728836059570307, 0.000005364418029785169
    )

    fun CreateTianDiTuTiledLayer(layerType: String): WebTiledLayer {
        return CreateTianDiTuTiledLayer(getTianDiTuLayerType(layerType));
    }

    fun CreateTianDiTuTiledLayer(layerType: LayerType): WebTiledLayer {
        var webTiledLayer: WebTiledLayer? = null
        var mainUrl = ""
        var mainName = ""
        var mainTileInfo: TileInfo? = null
        var mainEnvelope: Envelope? = null
        var mainIs2000 = false
        when (layerType) {
            LayerType.TIANDITU_VECTOR_2000 -> {
                mainUrl = URL_VECTOR_2000
                mainName = LAYER_NAME_VECTOR
                mainIs2000 = true
            }
            LayerType.TIANDITU_VECTOR_MERCATOR -> {
                mainUrl = URL_VECTOR_MERCATOR
                mainName = LAYER_NAME_VECTOR
            }
            LayerType.TIANDITU_IMAGE_2000 -> {
                mainUrl = URL_IMAGE_2000
                mainName = LAYER_NAME_IMAGE
                mainIs2000 = true
            }
            LayerType.TIANDITU_IMAGE_ANNOTATION_CHINESE_2000 -> {
                mainUrl = URL_IMAGE_ANNOTATION_CHINESE_2000
                mainName = LAYER_NAME_IMAGE_ANNOTATION_CHINESE
                mainIs2000 = true
            }
            LayerType.TIANDITU_IMAGE_ANNOTATION_ENGLISH_2000 -> {
                mainUrl = URL_IMAGE_ANNOTATION_ENGLISH_2000
                mainName = LAYER_NAME_IMAGE_ANNOTATION_ENGLISH
                mainIs2000 = true
            }
            LayerType.TIANDITU_IMAGE_ANNOTATION_CHINESE_MERCATOR -> {
                mainUrl = URL_IMAGE_ANNOTATION_CHINESE_MERCATOR;
                mainName = LAYER_NAME_IMAGE_ANNOTATION_CHINESE;
            }
            LayerType.TIANDITU_IMAGE_ANNOTATION_ENGLISH_MERCATOR -> {
                mainUrl = URL_IMAGE_ANNOTATION_ENGLISH_MERCATOR
                mainName = LAYER_NAME_IMAGE_ANNOTATION_ENGLISH
            }
            LayerType.TIANDITU_IMAGE_MERCATOR -> {
                mainUrl = URL_IMAGE_MERCATOR
                mainName = LAYER_NAME_IMAGE
            }
            LayerType.TIANDITU_VECTOR_ANNOTATION_CHINESE_2000 -> {
                mainUrl = URL_VECTOR_ANNOTATION_CHINESE_2000
                mainName = LAYER_NAME_VECTOR_ANNOTATION_CHINESE
                mainIs2000 = true
            }
            LayerType.TIANDITU_VECTOR_ANNOTATION_ENGLISH_2000 -> {
                mainUrl = URL_VECTOR_ANNOTATION_ENGLISH_2000
                mainName = LAYER_NAME_VECTOR_ANNOTATION_ENGLISH
                mainIs2000 = true
            }
            LayerType.TIANDITU_VECTOR_ANNOTATION_CHINESE_MERCATOR -> {
                mainUrl = URL_VECTOR_ANNOTATION_CHINESE_MERCATOR
                mainName = LAYER_NAME_VECTOR_ANNOTATION_CHINESE
            }
            LayerType.TIANDITU_VECTOR_ANNOTATION_ENGLISH_MERCATOR -> {
                mainUrl = URL_VECTOR_ANNOTATION_ENGLISH_MERCATOR
                mainName = LAYER_NAME_VECTOR_ANNOTATION_ENGLISH
            }
            LayerType.TIANDITU_TERRAIN_2000 -> {
                mainUrl = URL_TERRAIN_2000
                mainName = LAYER_NAME_TERRAIN
                mainIs2000 = true
            }
            LayerType.TIANDITU_TERRAIN_ANNOTATION_CHINESE_2000 -> {
                mainUrl = URL_TERRAIN_ANNOTATION_CHINESE_2000
                mainName = LAYER_NAME_TERRAIN_ANNOTATION_CHINESE
                mainIs2000 = true
            }
            LayerType.TIANDITU_TERRAIN_MERCATOR -> {
                mainUrl = URL_TERRAIN_MERCATOR
                mainName = LAYER_NAME_TERRAIN
            }
            LayerType.TIANDITU_TERRAIN_ANNOTATION_CHINESE_MERCATOR -> {
                mainUrl = URL_TERRAIN_ANNOTATION_CHINESE_MERCATOR
                mainName = LAYER_NAME_TERRAIN_ANNOTATION_CHINESE
            }
        }

        val mainLevelOfDetail = mutableListOf<LevelOfDetail>();
        var mainOrigin: Point? = null
        if (mainIs2000) {
            for (i in minZoomLevel..maxZoomLevel) {
                val item = LevelOfDetail(i, RESOLUTIONS_2000[i - 1], SCALES[i - 1])
                mainLevelOfDetail.add(item)
            }
            mainEnvelope = ENVELOPE_2000
            mainOrigin = ORIGIN_2000
        } else {
            for (i in minZoomLevel..maxZoomLevel) {
                val item = LevelOfDetail(i, RESOLUTIONS_MERCATOR[i - 1], SCALES[i - 1])
                mainLevelOfDetail.add(item);
            }
            mainEnvelope = ENVELOPE_MERCATOR;
            mainOrigin = ORIGIN_MERCATOR;
        }
        mainTileInfo = TileInfo(
            DPI,
            TileInfo.ImageFormat.PNG24,
            mainLevelOfDetail,
            mainOrigin,
            mainOrigin.getSpatialReference(),
            tileHeight,
            tileWidth
        )
        webTiledLayer = WebTiledLayer(
            mainUrl,
            SubDomain.toList(),
            mainTileInfo,
            mainEnvelope
        );
        webTiledLayer.setName(mainName)
        webTiledLayer.loadAsync()

        return webTiledLayer
    }

    fun getTianDiTuLayerType(layerType: String): LayerType {
        return when (layerType) {
            // 天地图矢量墨卡托投影地图服务
            "TIANDITU_VECTOR_MERCATOR" -> LayerType.TIANDITU_VECTOR_MERCATOR
            // 天地图矢量墨卡托中文标注
            "TIANDITU_VECTOR_ANNOTATION_CHINESE_MERCATOR" -> LayerType.TIANDITU_VECTOR_ANNOTATION_CHINESE_MERCATOR
            // 天地图矢量墨卡托英文标注
            "TIANDITU_VECTOR_ANNOTATION_ENGLISH_MERCATOR" -> LayerType.TIANDITU_VECTOR_ANNOTATION_ENGLISH_MERCATOR
            // 天地图影像墨卡托投影地图服务
            "TIANDITU_IMAGE_MERCATOR" -> LayerType.TIANDITU_IMAGE_MERCATOR
            // 天地图影像墨卡托投影中文标注
            "TIANDITU_IMAGE_ANNOTATION_CHINESE_MERCATOR" -> LayerType.TIANDITU_IMAGE_ANNOTATION_CHINESE_MERCATOR
            // 天地图影像墨卡托投影英文标注
            "TIANDITU_IMAGE_ANNOTATION_ENGLISH_MERCATOR" -> LayerType.TIANDITU_IMAGE_ANNOTATION_ENGLISH_MERCATOR
            // 天地图地形墨卡托投影地图服务
            "TIANDITU_TERRAIN_MERCATOR" -> LayerType.TIANDITU_TERRAIN_MERCATOR
            // 天地图地形墨卡托投影中文标注
            "TIANDITU_TERRAIN_ANNOTATION_CHINESE_MERCATOR" -> LayerType.TIANDITU_TERRAIN_ANNOTATION_CHINESE_MERCATOR
            // 天地图矢量国家2000坐标系地图服务
            "TIANDITU_VECTOR_2000" -> LayerType.TIANDITU_VECTOR_2000
            // 天地图矢量国家2000坐标系中文标注
            "TIANDITU_VECTOR_ANNOTATION_CHINESE_2000" -> LayerType.TIANDITU_VECTOR_ANNOTATION_CHINESE_2000
            // 天地图矢量国家2000坐标系英文标注
            "TIANDITU_VECTOR_ANNOTATION_ENGLISH_2000" -> LayerType.TIANDITU_VECTOR_ANNOTATION_ENGLISH_2000
            // 天地图影像国家2000坐标系地图服务
            "TIANDITU_IMAGE_2000" -> LayerType.TIANDITU_IMAGE_2000
            // 天地图影像国家2000坐标系中文标注
            "TIANDITU_IMAGE_ANNOTATION_CHINESE_2000" -> LayerType.TIANDITU_IMAGE_ANNOTATION_CHINESE_2000
            // 天地图影像国家2000坐标系英文标注
            "TIANDITU_IMAGE_ANNOTATION_ENGLISH_2000" -> LayerType.TIANDITU_IMAGE_ANNOTATION_ENGLISH_2000
            // 天地图地形国家2000坐标系地图服务
            "TIANDITU_TERRAIN_2000" -> LayerType.TIANDITU_TERRAIN_2000
            // 天地图地形国家2000坐标系中文标注
            "TIANDITU_TERRAIN_ANNOTATION_CHINESE_2000" -> LayerType.TIANDITU_TERRAIN_ANNOTATION_CHINESE_2000
            else -> LayerType.TIANDITU_VECTOR_2000
        }

    }
}

enum class LayerType {
    /**
     * 天地图矢量墨卡托投影地图服务
     */
    TIANDITU_VECTOR_MERCATOR,
    /**
     * 天地图矢量墨卡托中文标注
     */
    TIANDITU_VECTOR_ANNOTATION_CHINESE_MERCATOR,
    /**
     * 天地图矢量墨卡托英文标注
     */
    TIANDITU_VECTOR_ANNOTATION_ENGLISH_MERCATOR,
    /**
     * 天地图影像墨卡托投影地图服务
     */
    TIANDITU_IMAGE_MERCATOR,
    /**
     * 天地图影像墨卡托投影中文标注
     */
    TIANDITU_IMAGE_ANNOTATION_CHINESE_MERCATOR,
    /**
     * 天地图影像墨卡托投影英文标注
     */
    TIANDITU_IMAGE_ANNOTATION_ENGLISH_MERCATOR,
    /**
     * 天地图地形墨卡托投影地图服务
     */
    TIANDITU_TERRAIN_MERCATOR,
    /**
     * 天地图地形墨卡托投影中文标注
     */
    TIANDITU_TERRAIN_ANNOTATION_CHINESE_MERCATOR,
    /**
     * 天地图矢量国家2000坐标系地图服务
     */
    TIANDITU_VECTOR_2000,
    /**
     * 天地图矢量国家2000坐标系中文标注
     */
    TIANDITU_VECTOR_ANNOTATION_CHINESE_2000,
    /**
     * 天地图矢量国家2000坐标系英文标注
     */
    TIANDITU_VECTOR_ANNOTATION_ENGLISH_2000,
    /**
     * 天地图影像国家2000坐标系地图服务
     */
    TIANDITU_IMAGE_2000,
    /**
     * 天地图影像国家2000坐标系中文标注
     */
    TIANDITU_IMAGE_ANNOTATION_CHINESE_2000,
    /**
     * 天地图影像国家2000坐标系英文标注
     */
    TIANDITU_IMAGE_ANNOTATION_ENGLISH_2000,
    /**
     * 天地图地形国家2000坐标系地图服务
     */
    TIANDITU_TERRAIN_2000,
    /**
     * 天地图地形国家2000坐标系中文标注
     */
    TIANDITU_TERRAIN_ANNOTATION_CHINESE_2000

}