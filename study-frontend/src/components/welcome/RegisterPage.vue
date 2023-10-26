<template>
  <div style="text-align: center;margin: 0 20px">
    <div style="margin-top: 150px">
      <div style="font-size: 25px;font-weight: bold">注册新用户</div>
      <div style="font-size: 14px;color:grey;">请在下方填写相关信息</div>
    </div>
    <div style="margin-top: 50px">
      <el-form :model="form" :rules="rules" @validate="onValidate" ref="formRef">
        <el-form-item prop="username">
          <el-input v-model="form.username" :maxlength="5" type="text" placeholder="用户名">
            <template #prefix><el-icon><User /></el-icon></template>
          </el-input>
        </el-form-item>
        <el-form-item prop="password">
          <el-input v-model="form.password" :maxlength="6" type="password" placeholder="密码" >
            <template #prefix><el-icon><Lock /></el-icon></template>
          </el-input>
        </el-form-item>
        <el-form-item prop="password_repeat">
          <el-input v-model="form.password_repeat" type="password" placeholder="确认密码" >
            <template #prefix><el-icon><Lock /></el-icon></template>
          </el-input>
        </el-form-item>
        <el-form-item prop="email">
          <el-input v-model="form.email" type="email" placeholder="电子邮件地址" >
            <template #prefix><el-icon><message /></el-icon></template>
          </el-input>
        </el-form-item>
        <el-form-item prop="code ">
          <el-row :gutter="10">
            <el-col :span="17">
              <el-input v-model="form.code" :maxlength="6" type="email" placeholder="请输入电子邮件验证码">
                <template #prefix><el-icon><EditPen /></el-icon></template>
              </el-input>
            </el-col>
            <el-col :span="7">
              <el-button type="success" @click="validateEmail" style="text-align: right"
                         :disabled="!isEmailValid || coldTime > 0">{{coldTime > 0 ?'请稍后' + coldTime + '秒' : '获取验证码'}}</el-button>
            </el-col>
          </el-row>
        </el-form-item>
      </el-form>
    </div>

    <div style="margin-top: 80px">
      <el-button style="width: 270px" type="warning" @click="register" plain>立即注册</el-button>
    </div>
    <div>
      <el-link  style="font-size: 8px;margin-top: 15px" type="primary" @click="router.push('/')">已有账号？回到登录界面</el-link>
    </div>
  </div>
</template>

<script setup>
import router from "../../router";
import {reactive, ref} from "vue";
import {ElMessage} from "element-plus";
import {post} from "../../net";

const form = reactive({
  username:'',
  password:'',
  password_repeat:'',
  email:'',
  code:''
})
//  用户名校验
const validateUsername = (rule,value,callback)=>{
  if(value===''){
    callback(new Error('请输入用户名'))
  }else if(!/^[a-zA-Z0-9\u4e00-\u9fa5]+$/.test(value)){
    callback(new Error('用户名不能包含特殊字符，只能是中文/英文'))
  }else {
    callback()
  }
}
//重复密码校验
const validatePassword = (rule,value,callback)=>{
  if(value === ''){
    callback(new Error('请重新输入密码'))
  }else if (value !== form.password){
    callback(new Error('两次输入的密码不一致'))
  }else{
    callback()
  }
}

const rules = {
      username:[
        {validator: validateUsername, trigger:['blur','change']},
        {min:2,max:5,message: '长度为2到5个字符',trigger:['blur','change']}
      ],
  password:[
      { required:true,message: '密码不能为空',trigger: 'blur'},
      {min:5,max:6,message: '密码长度为5到6个字符',trigger:['blur','change']}
  ],
  password_repeat:[
    {validator:validatePassword,trigger:['blur','change']}
  ],
  email:[
    { required:true,message: '电子邮件不能为空',trigger: 'blur'},
    {type:'email',message:'请输入正确的电子邮件',trigger:['blur','change']}
  ],
  code:[
    { required:true,message: '请输入验证码',trigger: 'blur'},
  ]
}

const formRef = ref()
//判断电子邮件是否有效
const isEmailValid = ref(false)  //默认为否

const coldTime = ref(0) //邮箱发送冷却时间

const onValidate= (prop,isValid) =>{
   if (prop === 'email')
     isEmailValid.value = isValid
}

const register = () =>{
    formRef.value.validate((isValid)=>{
  if (isValid){
       post('/api/auth/register',{
         username: form.username,
         password: form.password,
         email: form.email,
         code: form.code
       },(message)=>{
         ElMessage.success(message)
         router.push("/")
       })
  }else {
    ElMessage.warning('请将表单填写完整！')
     }
   })
}

const validateEmail = () =>{
  post('/api/auth/valid-register-email',{
    email:form.email
  },(message) => {
    ElMessage.success(message)
    coldTime.value = 30
    setInterval(()=>coldTime.value--,1000)
  })
}

</script>

<style scoped>

</style>