
<template>
  <div>
    <el-table :data="tableData" style="width: 150%" height="650">

      <el-table-column fixed prop="name" label="公司名称" width="270"></el-table-column>
      <el-table-column prop="id" label="ID" width="120"></el-table-column>
      <el-table-column prop="wname" label="温度" width="120"></el-table-column>
      <el-table-column prop="aname" label="氨氮含量" width="120"></el-table-column>
      <el-table-column prop="xname" label="锌含量" width="120"></el-table-column>
      <el-table-column prop="rname" label="溶解氧含量" width="120"></el-table-column>
      <el-table-column prop="lname" label="磷含量" width="120"></el-table-column>
      <el-table-column prop="lat" label="纬度" width="180"></el-table-column>
      <el-table-column prop="lng" label="经度" width="180"></el-table-column>

    </el-table>
  </div>
</template>

<script>
import axios from 'axios';
import { ElTable, ElTableColumn } from 'element-plus';

export default {
  components: {
    ElTable,
    ElTableColumn
  },
  data() {
    return {
      tableData: [] // 初始化tableData为空数组，用于存储从后端获取的数据
    };
  },
  mounted() {
    axios.get("data/list")
        .then(response => {
          this.tableData = response.data; // 将从后端获取的数据赋值给tableData
          console.log(this.tableData); // 打印数据，确保获取到了正确的数据
        })
        .catch(error => {
          console.error('Error fetching data:', error);
        });
  }
};
</script>



