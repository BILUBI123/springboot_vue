<template>
  <div class="container">
    <div class="listItem">
      <!-- 每一项头条列表 -->
      <div class="containerItem" v-for="item in pageData" :key="item.hid">
        <div>
          <span class="text">{{ item.title }}</span>
        </div>
        <div class="detail">
          <span>{{ item.type == 1 ? "新闻":item.type == 2 ? "体育": item.type == 3 ? "娱乐": item.type == 4 ? "科技" : "其他" }}</span>
          <span>{{item.pageViews}}浏览</span>
          <span>{{item.pastHours}}小时前</span>
        </div>
        <div>
          <el-button @click="toDetail(item.hid)" size="small"
            style="background: #198754; margin-left: 15px; color: #bbd3dc">查看全文</el-button>
          <el-popconfirm v-if="item.publisher == type" @confirm="handlerDelete(item.hid)" :title="`您确定要删除${item.title}吗?`">
            <template #reference>
              <el-button    size="small" style="background: #dc3545; color: #bbd3dc">删除</el-button>
            </template>
          </el-popconfirm>

          <el-button @click="Modify(item.hid)" v-if="item.publisher == type"  size="small" style="background: #212529; color: #bbd3dc">修改</el-button>
        </div>
        
      </div>
      <!-- 雷达扫描 -->
      <div class="radar">
    <img class="avatar" src="../pro04-html/img/delImg.png">
    <img class="girl" style="--i:0.5;--l:50px;--t:-130px;--d:0.05" src="../pro04-html/img/bg1.jpg">
    <img class="girl" style="--i:0.5;--l:50px;--t:-130px;--d:0.05" src="../pro04-html/img/bg2.jpg">
    <img class="girl" style="--i:0.5;--l:50px;--t:-130px;--d:0.05" src="../pro04-html/img/logo.png">
    <img class="girl" style="--i:0.5;--l:50px;--t:-130px;--d:0.05" src="../pro04-html/img/bg3.jpg">
    <img class="girl" style="--i:0.5;--l:50px;--t:-130px;--d:0.05" src="../pro04-html/img/bg4.jpg">

    <img class="girl" style="--i:0.5;--l:50px;--t:-130px;--d:0.05" src="../pro04-html/img/delImg.png">
    <img class="girl" style="--i:0.6;--l:130px;--t:50px;--d:0.28" src="../pro04-html/img/delImg.png">
    <img class="girl" style="--i:0.9;--l:50px;--t:50px;--d:0.36" src="../pro04-html/img/delImg.png">
    <img class="girl" style="--i:0.7;--l:-50px;--t:90px;--d:0.55" src="../pro04-html/img/delImg.png">
    <img class="girl" style="--i:0.7;--l:-80px;--t:-50px;--d:0.85" src="../pro04-html/img/delImg.png">
</div>
      <!-- 分页器 -->
      <div style="margin-top: 20px">
        <el-pagination 
          v-model:current-page="findNewsPageInfo.pageNum"
          v-model:page-size="findNewsPageInfo.pageSize" 
          @size-change="getPageList"
          @current-change="getPageList"
          :page-sizes="[5,7,10]" 
          background
          layout="prev, pager, next , ->, sizes, total" 
          :total="totalSize" />
      </div>
    </div>
  </div>
</template>

<script >
import { getfindNewsPageInfo , removeByHid } from "../../api/index"
 import { defineComponent } from 'vue'
  export default  defineComponent({
    name:'HeadlineNews'
  })
</script>
<script  setup>
import { ref, onMounted, getCurrentInstance, watch } from "vue"
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import pinia from '../../stores/index';
import { useUserInfoStore } from '../../stores/userInfo'
// getCurrentInstance: 全局挂载的方法
const  { Bus } = getCurrentInstance().appContext.config.globalProperties
const userInfoStore = useUserInfoStore(pinia)
const router = useRouter()
const type = userInfoStore.uid
const findNewsPageInfo = ref(
  {
    keyWords: "", // 搜索标题关键字
    type: 0,           // 新闻类型
    pageNum: 1,        // 页码数
    pageSize: 5,     // 页大小
  }
)
const totalSize = ref(0) //分页总数量
// 初始化列表数据
const pageData = ref([{
  hid: null,
  pageViews: null,
  pastHours: null,
  publisher: null,
  title: "",
  type: null
}])


//接收header组件用户搜索的数据
Bus.on('keyword', (keywords) => {
  findNewsPageInfo.value.keyWords = keywords
})
// header点击切换高亮的时候传递过来的tid
Bus.on('tid', (type) => {
  findNewsPageInfo.value.type = type
})
// 监视初始化参数type的变化,当type发生改变的时候重新发送请求获取列表数据
watch(() => findNewsPageInfo.value, () => {
  getPageList()
}, {
  deep: true,
})
// 初始化请求分页列表数据
const getPageList = async () => {
  let result = await getfindNewsPageInfo(findNewsPageInfo.value)
  pageData.value = result.pageInfo.pageData
 findNewsPageInfo.value.pageNum = result.pageInfo.pageNum
 findNewsPageInfo.value.pageSize = result.pageInfo.pageSize
 totalSize.value = +result.pageInfo.totalSize
}
// 组件挂载的生命周期钩子
onMounted(() => {
  getPageList()
})
// 点击查看全文的回调
const toDetail = (hid) => {
  router.push({ name: "Detail" ,query:{ hid }});
}

// 点击删除的回调
const handlerDelete = async (id) => {
  await removeByHid(id)
  ElMessage.success('删除成功!')
  //重新获取列表请求
  getPageList()
}
//点击修改的回调
const Modify = (hid) => {
  router.push({ name: "addOrModifyNews", query: { hid } });
}
</script>

<style lang="less" scoped>
.container {
  width: 1200px;
  margin: 0 auto;
  display: flex;
  flex-direction: column;
  align-items: center;

  // 列表样式
  .listItem {
    .containerItem {
      margin-top: 20px;
      border-radius: 10px;
      border: 2px solid #ebebeb;
      width: 600px;
      height: 120px;

      div {
        margin-top: 10px;
      }

      .text {
        margin-left: 15px;
        color: #353a3f;
      }

      .detail {
        span {
          margin-left: 15px;
          color: #8b778a;
          font-size: 14px;
        }
      }
    }
  }
}
divleida {
        margin: 0;
        height: 100vh;
        display: flex;
        align-items: center;
        justify-content: center;
        overflow: hidden;
        background-color: #000;
    }

    .radar {
        width: 500px;
        height: 500px;
        position: relative;
        border-radius: 50%;
        background:
            repeating-radial-gradient(transparent 0,
                transparent 27px,
                #43768e 27px,
                #43768e 30px);
        border-radius: 50%;

    }


    .radar {
        background:
            linear-gradient(90deg,
                transparent 49.75%,
                #43768e 49.75%,
                #43768e 50.25%,
                transparent 50.25%),
            linear-gradient(transparent 49.75%,
                #43768e 49.75%,
                #43768e 50.25%,
                transparent 50.25%),

            repeating-radial-gradient(transparent 0,
                transparent 27px,
                #43768e 27px,
                #43768e 30px);

    }

    .radar:before {
        content: "";
        position: absolute;
        width: 250px;
        height: 250px;
        background: linear-gradient(45deg,
                rgba(0, 0, 0, 0) 50%,
                rgba(107, 183, 208, 1) 100%);
        border-radius: 100% 0 0 0;
    }


   
    
    @keyframes scanning {
        to {
            transform: rotate(360deg);
        }
    }

    .radar:before {
        animation: scanning 5s linear infinite;
        transform-origin: right bottom;
    }

    .radar:after {
        content: '';
        widows: 0;
        height: 0;
        opacity: 0.5;
        position: absolute;
        left: 50%;
        top: 50%;
        background-color: rgba(107, 183, 208, 1);
        border-radius: 50%;
        transform: translate(-50%, -50%);
    }
    @keyframes scale{
        60%, 100% {
            width: 800px;
            height: 800px;
            opacity: 0;
        }
    }

  .radar:after{
    animation: 
    scale 5s ease-in infinite;
  }

   

    .avatar {
        position: absolute;
        left: 50%;
        top: 50%;
        transform: translate(-50%, -50%);
        width: 70px;
        height: 70px;
        border-radius: 50%;
        border: 2px solid #fff;
        z-index: 666;
    }

    .girl {
        position: absolute;
        left: calc(50% + var(--l));
        top: calc(50% + var(--t));
        transform: translate(-50%, -50%) scale(var(--i));
        width: 50px;
        height: 50px;
        border-radius: 50%;
        object-fit: cover;
        border: 2px solid #ff2972;
        transform-origin: center;
        z-index: 999;
    }

    @keyframes upDown {
        20% {
            transform: translate(-50%, -60%) scale(calc(var(--i) * 1.4));
            box-shadow: 0 0 10px #ff2972, 0 0 30px #ff2972, 0 0 50px #ff2972;
        }

        50%,
        100% {
            transform: translate(-50%, -50%) scale(var(--i));
            box-shadow: none;
        }
    }

    .girl {
        animation: upDown 5s infinite linear;
        animation-delay: calc(var(--d) * 5s);
    }
</style>
