<template>
  <el-table :data="tableData" style="width: 100%" height="650">
    <el-table-column fixed prop="name" label="公司名称" width="230" />
    <el-table-column prop="id" label="编号" width="100" />
    <el-table-column prop="wname" label="温度" width="100" />
    <el-table-column prop="aname" label="氨氮含量" width="120" />
    <el-table-column prop="xname" label="锌含量" width="120" />
    <el-table-column prop="rname" label="溶解氧含量" width="120" />
    <el-table-column prop="lname" label="磷含量" width="120" />
    <el-table-column prop="lat" label="纬度" width="150" />
    <el-table-column prop="lng" label="经度" width="150" />
  </el-table>
</template>

<script setup>
import axios from "axios";

import Data from "../data/data1.json"


const tableData = Data.data

 // const tableData = []

    axios.get("data/list").then(data=>{
      console.log(data)
      })
    axios.post("data/listP",{}).then(res=>res.data).then(res=>{
      console.log(res)
      this.tableData = res
    })

</script>
<style>

</style>