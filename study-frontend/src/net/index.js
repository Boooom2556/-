import axios from "axios";
import {ElMessage} from "element-plus";

const defaultError = ()=>ElMessage.error('发生了一些错误，请联系管理员')
const defaultFailure = (message)=>ElMessage.warning(message)

function post(url,data,success,failure = defaultFailure,error = defaultError){
    axios.post(url,data,{
        headers:{
            'Content-Type':'application/x-www-form-urlencoded' //改为表单形式
        },
        withCredentials:true //请求是否携带cookie,无论post还是get，都要带,否则服务器不认识
    }).then(({data})=>{
        if (data.success)
            success(data.message,data.status)  //回调函数
        else
            failure(data.message,data.status)
    }).catch(error)
}

function get(url,success,failure = defaultFailure,error = defaultError){
    axios.get(url,{
       //不需要提交参数，因此不需要header
        withCredentials:true //请求是否携带cookie,无论post还是get，都要带,否则服务器不认识
    }).then(({data})=>{
        if (data.success)
            success(data.message,data.status)  //回调函数
        else
            failure(data.message,data.status)
    }).catch(error)
}


export { post,get }