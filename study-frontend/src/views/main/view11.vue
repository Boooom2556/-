
<template>
  <div id="aa">
    <input id='tipinput' type="text"/>
    <div id="container" ref="amap"></div>
    <div id="panel"></div>
  </div>
</template>

<script>
import AMapLoader from '@amap/amap-jsapi-loader'
export default {
  data() {
    return {
      map: null,
      placeSearch: null,
      mapModule: null // AMap
    }
  },

  mounted() {
    window._AMapSecurityConfig = {
      securityJsCode: '614a0782cadb00a563d903b64c497cf7' // 申请key对应的秘钥 -> 注意了，如果不搭配密钥使用，搜索将没有结果
    }
    // 初始化地图
    this.initAMap()
  },
  destroyed() {
    // 销毁地图
    this.map.destroy()
  },
  methods: {
    // 初始化地图函数
    initAMap() {
      const _this = this
      AMapLoader.load({
        key: '8c48365ba9de9fac5d6267f98098ba0c', // 申请好的Web端开发者Key，首次调用 load 时必填
        version: '2.0', // 指定要加载的 JSAPI 的版本，缺省时默认为 1.4.15
        plugins: []
      })
          .then((AMap) => {
            // 保存AMap实例
            _this.mapModule = AMap
            const map = new AMap.Map('container', {
              // 设置地图容器id
              viewMode: '3D', // 默认2d地图模式
              zoom: 12, // 初始化地图级别
              zooms: [5, 30],
              // 可以设置初始化当前位置
              center: [116.397428, 39.90923],
              resizeEnable: true
            })
            // 添加控件
            AMap.plugin(
                [
                  'AMap.ElasticMarker',
                  'AMap.Scale',
                  'AMap.ToolBar',
                  'AMap.HawkEye',
                  'AMap.MapType',
                  'AMap.Geolocation',
                  'AMap.AutoComplete',
                  'AMap.PlaceSearch'
                ],
                () => {
                  map.addControl(new AMap.ElasticMarker())
                  map.addControl(new AMap.Scale())
                  map.addControl(new AMap.ToolBar())
                  map.addControl(new AMap.HawkEye())
                  map.addControl(new AMap.MapType())
                  map.addControl(new AMap.Geolocation())
                }
            )
            _this.map = map
            // 搜索功能
            _this.toSearch(

            )
          })
          .catch((e) => {
            console.log(e)
          })
    },
    toSearch() {
      const _this = this

      // 实例化AutoComplete
      const autoOptions = {
        // input 为绑定输入提示功能的input的DOM ID,注意这个id要个搜索输入框的id一致
        input: 'tipinput',

      }
      const autoComplete = new _this.mapModule.AutoComplete(autoOptions)
      autoComplete.on('select', _this.select)// 注册监听，当选中某条记录时会触发
    },

    select(e) {
      const _this = this
      // console.log(111)
      // 无需再手动执行search方法，autoComplete会根据传入input对应的DOM动态触发search
      // { map: _this.map } ==> 这个对象是能配置的 --> 根据官方文档配置即可，需要什么配置什么
      const placeSearch = new _this.mapModule.PlaceSearch({
        map: _this.map
      })
      placeSearch.setCity(e.poi.adcode)
      placeSearch.search(e.poi.name) // 关键字查询查询
    }
  }
}
</script>
<style  scoped>
#tipinput{
  position: absolute;
  width: 150px;
  height: 20px;
  z-index: 99999;
}
#container {
  width: 1250px;
  height: 690px;
}
</style>
