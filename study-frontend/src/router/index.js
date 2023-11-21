import { createRouter, createWebHistory } from 'vue-router'
import {useStore} from "../stores";
import view2 from "../views/main/view2.vue";
import view1 from "../views/main/view1.vue";
import view4 from "../views/main/view4.vue"
import view3 from "../views/main/view3.vue";
import index1 from "../views/main/index1.vue";
import view11 from "../views/main/view11.vue";
import index3 from "../views/main/index3.vue";


const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'welcome',
      component: () =>import('../views/WelcomeView.vue'),
      children:[
        {
          path:'',
          name:'welcome-login',
          component: () =>import('../components/welcome/LoginPage.vue'),

        },
        {
          path:'register',
          name:'welcome-register',
          component: () =>import('../components/welcome/RegisterPage.vue')
        },
        {
          path:'forget',
          name:'welcome-forget',
          component: () =>import('../components/welcome/ForgetPage.vue')
        }
      ]
    },{
    path:'/index',
      name:'index',
      component:()=>import('../views/IndexView.vue'),
      children:[
        {
          path:'/view1',
          name:'view1',
          component: view1,
        },
        {
          path:'/view2',
          name:'view2',
          component: view2,

        },
        {
          path:'/view3',
          name:'view3',
          component: view3,
        },
        {
          path:'/view4',
          name:'view4',
          component: view4,
        },
        {
          path:'/index1',
          name:'index1',
          component: index1,
        },
        {
          path: '/view11',
          name:'view11',
          component: view11,

        },
        {
          path: '/index3',
          name:'index3',
          component: index3,
        }

      ]
    },


  ]
})

router.beforeEach((to,from,next)=>{
  const store = useStore()
    if (store.auth.user !=null && to.name.startsWith('welcome-')){
      next('/index')
  }else if (store.auth.user == null && to.fullPath.startsWith('/index')){
      next('/')
    }else if (to.matched.length === 0){
      next('/index')
    }else {
      next()
    }
})





export default router
