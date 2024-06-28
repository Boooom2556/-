<template>
  <div class="common-layout">
    <el-header>
      <div style="display: inline-block">
       <span class="s1" >WebGIS系统</span>
      </div>
      <div style="float: right;margin-top: 15px">
       <el-button class="s2" @click="logout()" type="danger" plain>退出</el-button>
      </div>
    </el-header>
    <el-container>
      <el-aside width="isCollapse ? '50px':'200px'">
        <div class="toggle-button" @click="toggleCollapse">|||</div>
          <el-menu :default-openeds="['1', '3']" unique-opened :collapse="isCollapse">

            <el-sub-menu index="1" >

              <template #title>
                <el-icon><message /></el-icon> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;地图
              </template>
              <el-menu-item-group>
                <el-menu-item index="1-1" @click="router.push('view1')">高德</el-menu-item>
                <el-menu-item index="1-2" @click="router.push('view2')">天地图</el-menu-item>
                <el-menu-item index="1-3" @click="router.push('view11')">JS API</el-menu-item>
              </el-menu-item-group>
            </el-sub-menu>
            <el-sub-menu index="2">
              <template #title>
                <el-icon><Menu /></el-icon>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;哈哈
              </template>
              <el-menu-item-group>
                <el-menu-item index="2-1" @click="router.push('view3')">地图</el-menu-item>
                <el-menu-item index="2-2" @click="router.push('view21')">Option 2</el-menu-item>
              </el-menu-item-group>
                <el-menu-item index="2-3" @click="router.push('view22')">Option 3</el-menu-item>
            </el-sub-menu>
            <el-sub-menu index="3">
              <template #title>
                <el-icon><InfoFilled /></el-icon>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;展示
              </template>
              <el-menu-item-group>
                <el-menu-item index="3-2" >Option 2</el-menu-item>
              </el-menu-item-group>
                <el-menu-item index="3-3">Option 3</el-menu-item>
              <el-sub-menu index="3-4">
                <template #title>Option 4</template>
                <el-menu-item index="3-4-1">Option 4-1</el-menu-item>
              </el-sub-menu>
            </el-sub-menu>

            <el-sub-menu index="4">
              <template #title>
                <el-icon><setting /></el-icon>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;个人页面
              </template>
              <el-menu-item-group>
                <el-menu-item index="4-2" @click="router.push('index1')">首页</el-menu-item>
              </el-menu-item-group>
              <el-menu-item index="4-3">个人中心</el-menu-item>
              <el-menu-item index="4-4-1" @click="router.push('index3')">数据管理</el-menu-item>

            </el-sub-menu>
          </el-menu>
      </el-aside>


      <!--页面加载-->
      <el-container>
        <router-view/>
      </el-container>
    </el-container>
  </div>
</template>

<script setup>
import {get} from "../net";
import {ElMessage} from "element-plus";
import router from "../router";
import {useStore} from "../stores";
import {ref} from "vue";

const store = useStore()


const logout = ()=>{
  get('/api/auth/logout',(message)=>{
    ElMessage.success(message)
    store.auth.user = null
    router.push('/')
  })
}
  const isCollapse =ref(false)


const toggleCollapse=()=>{
  console.log(111)
  isCollapse.value = !isCollapse.value
}
</script>

<style scoped>
.container{

}
.toggle-button{

 background-color: #4A5064;

 font-size:10px;

 line-height:24px;

 color:#fff;

 text-align: center;

 letter-spacing: 0.2em;

 cursor:pointer;
 }
.s1{
  color: #eaedf1;
  font-size: 30px;
  text-align: center;
  line-height: 60px;
}
.el-header{
  background-color: brown;
  height: 60px;
}
.el-aside{
  /*background-color: #333744;*/
  height: 670px;
  border-block: black;
  text-align: center;
  font-weight: bold;
}

.el-main{
  background-color: #eaedf1;

}
</style>