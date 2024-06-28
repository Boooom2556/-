<template>
 <div id="map"></div>
</template>

<script>

import {LayerPopup, PointLayer, Scene} from "@antv/l7";
import {GaodeMap} from "@antv/l7";


const scene = new Scene({
  id: "map",
  map: new GaodeMap({
    // pitch: 30.210526315789465,
    style: 'darkblue',
    center: [120.288144, 35.239692],
    zoom: 6,
    token:'8c48365ba9de9fac5d6267f98098ba0c',
  }),
});
// scene.on('loaded', () => {
//   fetch(
//       'https://gw.alipayobjects.com/os/basement_prod/6c4bb5f2-850b-419d-afc4-e46032fc9f94.csv'
//   )
//       .then(res => res.text())
//       .then(data => {
//         const pointLayer = new PointLayer({})
//             .source(data, {
//               parser: {
//                 type: 'csv',
//                 x: 'Longitude',
//                 y: 'Latitude'
//               }
//             })
//             .shape('circle')
//             .size(9)
//             .active(true)
//             .color(
//                 'Magnitude', [
//                   '#0A3663',
//                   '#00ff00',
//                   '#64A5D3',
//                   '#ff0000',
//                   '#D7F9F0'
//                 ]
//             )
// console.log(data)
//
//         scene.addLayer(pointLayer);
//       });
// });
scene.on('loaded',()=>{
  fetch("/data1.json")
  .then(res => res.json())
        .then(data => {
          const pointLayer = new PointLayer({})
              .source(data,{
                type:'json',
                // x:'lng',
                // y:'lat'
              })

              .shape('circle')
              .size(15)
              .color( [
                                '#0A3663',
                                '#00ff00',
                                '#64A5D3',
                                '#ff0000',
                                '#D7F9F0'
                              ]
                          )
              .style({
                opacity:0.5
              })
              .active(true)

          scene.addLayer(pointLayer);

          pointLayer.on('click',(ev) =>{
            console.log(11)
            let {name} = ev.feature.properties;
            console.log(name)
            let popup = new LayerPopup({
              lngLat:ev.lngLat,
              html:`<p>${name}</p>`
            })
            scene.addPopup(popup)
          })
        });

});
</script>

<style scoped>
 #map{

 }
</style>