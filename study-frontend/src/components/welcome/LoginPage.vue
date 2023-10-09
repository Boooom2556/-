<template>

      <div style="text-align:center;margin: 0 20px">
        <div style="margin-top: 150px">
          <div style="font-size: 25px;font-weight: bold">登录</div>
          <div style="font-size: 14px">在进入系统之前请先输入用户名和密码</div>
        </div>
        <div style="margin-top: 30px">
          <el-input v-model="form.username" type="text" placeholder="请输入用户名或邮箱">
            <template #prefix><el-icon><User /></el-icon></template>
          </el-input>
          <el-input v-model="form.password" type="password" style="margin-top: 10px" placeholder="请输入密码">
            <template #prefix><el-icon><Lock /></el-icon></template>
          </el-input>
        </div>
        <div style="margin-top: 10px;">
          <el-row>
            <el-col :span="12" style="text-align: left">
              <el-checkbox v-model="form.remember" label="记住密码"/>
            </el-col>
            <el-col :span="12" style="text-align: right">
              <el-link @click="router.push('/forget')">忘记密码？</el-link>
            </el-col>
          </el-row>
        </div>
      </div>
      <div style="margin-top: 40px">
        <el-button @click="login()" style="width: 270px;margin-left: 60px" type="success" plain>登录</el-button>
      </div>
      <el-divider>
        <span style="color:grey;font-size: 12px">没有账号？</span>
      </el-divider>
      <div style="margin-top: 30px">
        <el-button @click="router.push('/register')" style="width: 270px;margin-left: 60px" type="success" plain>立即注册</el-button>
      </div>

</template>

<script setup>
import {User,Lock} from '@element-plus/icons-vue'
import {reactive} from "vue";
import {ElMessage} from "element-plus";
import {get, post} from "../../net";
import router from "../../router";
import {useStore} from "../../stores";

const form = reactive({
  username:'',
  password:'',
  remember:false
})
const store = useStore()

const login = ()=>{
  if (!form.username || !form.password){ //未填写，警告
    ElMessage.warning('请填写用户名和密码！')
  } else{
    post('/api/auth/login',{
      username:form.username,
      password:form.password,
      remember:form.remember
    },(message)=>{
      ElMessage.success(message)//登录成功，实现跳转
        get('/api/user/me',(message) =>{
          store.auth.user = message
          router.push('/index')
        },() =>{
          store.auth.user = null
         })

     //  router.push('/index')
    })

  }

}
</script>

<style scoped>

</style>