<template>
  <!-- 承载地图的容器，注意宽高一定要有，否则不显示 （后面将该文件封装为组件，以便调用） -->
  <div class="base-map" id="base-map" />
</template>

<script>
import Map from 'ol/Map'
import View from 'ol/View'
import { defaults as Defaults } from 'ol/control.js'
import TileLayer from 'ol/layer/Tile'
import XYZ from 'ol/source/XYZ'

export default {
  name: 'view2',
  data () {
    return {
      // 地图实例对象
      map: null
    }
  },
  mounted () {
    // 窗口拖拉，更新地图大小
    window.addEventListener('resize', () => {
      if (this.map) {
        this.map.updateSize()
      }
    })
    this.$nextTick(() => {
      this.initMap()
    })
  },
  methods: {
    // 加载地图
    initMap () {
      // T=vec_c表示请求的是路网数据，x 表示切片的 x 轴坐标，y 表示切片的y轴坐标，z表示切片所在的缩放级别。
      // 使用 ol.source.XYZ 加载切片，并将获取的数据初始化一个切片图层 ol.layer.Tile：
      // 天地图底图
      var source = new XYZ({
        url: 'http://t4.tianditu.com/DataServer?T=vec_w&tk=b9031f80391e6b65bd1dd80dcde1b097&x={x}&y={y}&l={z}'
      })
      var tileLayer = new TileLayer({
        title: '天地图',
        source: source
      })
      // 标注图层(就是我们所看见的行政区名称，道路)
      var sourceMark = new XYZ({
        url: 'http://t4.tianditu.com/DataServer?T=cva_w&tk=b9031f80391e6b65bd1dd80dcde1b097&x={x}&y={y}&l={z}'
      })
      var tileMark = new TileLayer({
        title: '标注图层',
        source: sourceMark
      })
      //  创建地图对象
      this.map = new Map({
        target: 'base-map', // 地图容器 对应id
        layers: [tileLayer, tileMark], // 图层
        view: new View({ // 视图
          projection: 'EPSG:4326', // 坐标系
          // 初始化地图中心 可以去地图坐标拾取网站获取想要的坐标
          center: [118.339408, 32.261271],
          // 缩放
          zoom: 8,
          // 最大缩放
          maxZoom: 18,
          // 最小缩放
          minZoom: 1
        }),
        // 地图自带控件，这里我们不需要，后续自己做类似功能
        controls: new Defaults({
          zoom: true,
          rotate: false
        })
      })
      // 将地图对象抛出去
      this.$emit('getMap', this.map)
    }
  }
}
</script>

<style scoped>
.base-map {
  width: 100%;
  height: 100%;
  z-index: -1;
}
</style>
