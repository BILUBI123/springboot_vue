<template>

    <div class="loginBody">
      <el-form
        :model="loginForm"
        ref="formRef"
        label-width="80px"
        class="form"
        :rules="loginRules"
      >
        <h2 class="header">用户登录</h2>
        <div class="main">
          <el-form-item
            class="inpbox"
            label="用户名"
            prop="username"
          >

            <el-input
              v-model="loginForm.username"
              ref="username"
              name="username"
              autocomplete="off"
              placeholder="请输入用户名"
            ></el-input>
          </el-form-item>

          <el-form-item
            class="inpbox"
            label="密码"
            prop="userPwd"
          >
            <el-input
              type="password"
              v-model="loginForm.userPwd"
              autocomplete="off"
              placeholder="请输入密码"
            ></el-input>
          </el-form-item>
        </div>
        <div class="action">
          <el-form-item>
            <el-button
              class="btn"
              type="success"
              @click.native.prevent="login"
            >登录</el-button>
            <el-button
              class="btn"
              type="primary"
              @click="toRegister"
            >注册</el-button>
          </el-form-item>
        </div>
      </el-form>
    </div>
</template>

<script lang="ts">
import { defineComponent } from "vue";
export default defineComponent({
  name: "Login",
});
</script>
<script lang="ts" setup>
import { ref } from "vue";
import { useUserInfoStore } from "../../stores/userInfo";

import type { FormInstance } from "element-plus";
import { useRouter } from "vue-router";
const userInfoStore = useUserInfoStore();
const router = useRouter();
const formRef = ref<FormInstance>();
const loading = ref(false);
//账号密码参数
const loginForm = ref({
  username: "zhangsan",
  userPwd: "123456",
});
// 校验规则
const validateUsername = (rule: any, value: any, callback: any) => {
  if (value.length < 4) {
    callback(new Error("用户名长度不能小于4位"));
  } else {
    callback();
  }
};
// 校验规则
const validatePassword = (rule: any, value: any, callback: any) => {
  if (value.length < 6) {
    callback(new Error("密码长度不能小于6位"));
  } else {
    callback();
  }
};
// 校验规则
const loginRules = {
  username: [{ required: true, validator: validateUsername }],
  userPwd: [{ required: true, trigger: "blur", validator: validatePassword }],
};
//点击登录的回调
const login = async () => {
  // console.log('点击登录');
  await formRef.value?.validate();
  loading.value = true;
  try {
    // await getUserInfo(loginForm.value)
    await userInfoStore.login(loginForm.value);
    router.push({ name: "HeadlineNews" });
  } finally {
    loading.value = false;
  }
};

const toRegister = () => {
  router.push({ name: "Register" });
};
</script>

<style>
* {
  margin: 0;
  padding: 0;
}

.loginBody {
  height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  
  content: '';
	inset: 5px;
	background: url("/src/assets/img/bg1.jpg");background-attachment: fixed;
	background-size: cover;
	background-position: center;
  
}

.form {
  width: 300px;
  background-color: rgb(41, 45, 62);
  color: #fff;
  border-radius: 2px;
  padding: 50px 80px 80px;
  border-radius: 10px;
  box-shadow: 2px 2px 2px rgba(0, 0, 0, 0.4);
}

.header {
  text-align: center;
  font-size: 35px;
  text-transform: uppercase;
  line-height: 100px;
}

.main .inpbox .el-input {
  background-color: rgb(41, 45, 62);
  border: 0;
  width: 100%;
  text-align: center;
  font-size: 15px;
  color: #fff;
  outline: none;
  height: calc(100% - 4px);
  width: calc(100% - 6px);
  border-radius: 30px;
}

.main .inpbox {
  background: linear-gradient(90deg, #03a9f4, #f441a5, #ffeb3b, #03a9f4);
  background-size: 400%;
  width: 100%;
  height: 50px;
  margin-bottom: 20px;
  border-radius: 30px;
  display: flex;
  align-items: center;
  justify-content: center;
  transition: all 1s;
}

.action {
  display: flex;
}

.action .btn {
  text-transform: uppercase;
  border: 2px solid #0e92b3;
  text-align: left;
  line-height: 50px;
  border-radius: 30px;
  cursor: pointer;
  transition: all 1s;
}

.main .inpbox:has(input:focus) {
  animation: animate 5s linear infinite;
  transform: scale(1.2);
}

@keyframes animate {
  0% {
    background-position: 0 0;
  }

  100% {
    background-position: 400% 0;
  }
}

.action .btn:hover {
  transform: scale(1.2);
  background-color: #0e92b3;
}
</style>