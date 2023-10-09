<template>
  <div style="width: 100vw;height: 100vh;overflow: hidden;display: flex">
    <div style="flex: 1;">
      <el-image style="width: 100%;height: 100% " fit="cover"
                src="https://cn.bing.com/images/search?view=detailV2&ccid=dI%2f4ZYTP&id=81BAD49DF589A6D1AD5CA6ED664C5995F8863AC7&thid=OIP.dI_4ZYTPo2OEluMRWnCjpwHaFQ&mediaurl=https%3a%2f%2fimg.zcool.cn%2fcommunity%2f012f1e6042109011013f374559943d.jpg%402o.jpg&exph=2094&expw=2950&q=%e9%a3%8e%e6%99%af%e5%9b%be&simid=608042648147278274&FORM=IRPRST&ck=163913B00CDB58ABFF6DE591971CE520&selectedIndex=19&ajaxhist=0&ajaxserp=0"/>
    </div>
    <div class="welcome-title">
      <div style="font-size: 30px;font-weight: bold">欢迎来到springboot+vue3学习系统</div>
    </div>
    <div style="width: 400px;background-color: white;z-index: 1">
      <router-view v-slot="{Component}">
        <transition name="el-fade-in-linear">
          <component :is="Component" />
        </transition>
      </router-view>
    </div>
  </div>
</template>

<script setup>

</script>

<style scoped>
 .welcome-title{
   position: absolute;
   bottom: 30px;
   left: 30px;
   color: blueviolet;
   text-shadow: 0 0 10px black;
 }
</style>