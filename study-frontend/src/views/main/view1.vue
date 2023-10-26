<template>
  <!-- 地图容器 -->
  <div id="map" class="map__x" ref="mapCom"></div>

  <!-- 弹窗容器 -->
  <div ref="popupCom" class="popup">
    <!-- 关闭按钮 -->
    <span class="icon-close" @click="closePopup">✖</span>
    <!-- 弹窗内容（展示坐标信息） -->
    <div class="content">{{currentCoordinate}}</div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { Map, View } from 'ol' // 引入容器绑定模块和视图模块
import Tile from 'ol/layer/Tile' // 瓦片加载器
import XYZ from 'ol/source/XYZ' // 引入XYZ地图格式
import Overlay from 'ol/Overlay'// 引入覆盖物模块
import 'ol/ol.css'
import {defaults, MousePosition, ScaleLine} from "ol/control";
import {createStringXY} from "ol/coordinate"; // ol提供的css样式（必须引入）

const mapCom = ref(null) // 地图容器

const popupCom = ref(null) // 弹窗容器

const map = ref(null) // 地图实例

const overlay = ref(null) // 覆盖物实例
const currentCoordinate = ref('') // 弹窗信息

// 初始化地图
function initMap() {
  // 注册一个覆盖物
  overlay.value = new Overlay({
    element: popupCom.value, // 弹窗标签，在html里
    autoPan: true, // 如果弹窗在底图边缘时，底图会移动
    autoPanAnimation: { // 底图移动动画
      duration: 250
    }
  })
  map.value = new Map({
    target: mapCom.value,
    layers: [
      new Tile({ // 加载瓦片
        name: 'defaultLayer',
        source: new XYZ({ // 瓦片底图地址
          url: 'http://map.geoq.cn/ArcGIS/rest/services/ChinaOnlineStreetPurplishBlue/MapServer/tile/{z}/{y}/{x}'
        })
      })
    ],
    view: new View({
      projection: 'EPSG:4326', // 投影坐标系
      center: [113.1206, 23.034996], // 地图中心点

      zoom: 8 // 地图缩放级别（打开页面时默认级别）
    }),
    controls:
        defaults().extend([
          new ScaleLine({
            units: "metric",
          }),
        ]),


    overlays: [overlay.value] // 绑定一个覆盖物
  })

  mapClick() // 在地图初始化完成后再绑定点击事件
}

// 点击地图事件
function mapClick() {
  map.value.on('singleclick', evt => { // 绑定一个点击事件
    const coordinate = evt.coordinate // 获取坐标
    currentCoordinate.value = coordinate // 保存坐标点
    overlay.value.setPosition(coordinate) // 设置覆盖物出现的位置
  })
}

// 关闭弹窗
function closePopup () {
  overlay.value.setPosition(undefined) // setPosition 传入undefined会隐藏弹窗元素
  currentCoordinate.value = '' // 把弹窗内容清空
}

onMounted(() => {
  // 在元素加载完之后再执行地图初始化
  initMap()
})
</script>

<style scoped>
.map__x {
  width: 100%;
  height: 100%;

}

.popup {
  width: 300px;
  height: 100px;
  background: #fff;
  position: absolute;
  top: -115px;
  left: -150px;
  box-sizing: border-box;
  padding: 10px;
}

.after {
  content: '';
  display: block;
  position: absolute;
  width: 20px;
  height: 20px;
  background: #fff;
  bottom: -10px;
  left: 50%;
  transform: translateX(-50%) rotate(45deg);
}

.icon-close {
  position: absolute;
  top: 0px;
  right: 8px;
  cursor: pointer;
}

.content {
  margin-top: 14px;
}

</style>
