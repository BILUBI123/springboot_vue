<template>
  <div class="registerBody">
    <!-- :model="registerForm" 指定表单数据 -->
    <el-form
      class="form"
      :model="registerForm"
      ref="formRef"
      label-width="80px"
      :rules="registerRules"
    >
      <!-- autocomplete="off": 是否开启单字段的自动填充行为。 -->
      <h2 class="header">用户注册</h2>
      <div class="main">
        <div class="inpbox">
          <input
            v-model="registerForm.nickName"
            autocomplete="off"
            ref="nickName"
            name="nickName"
            placeholder="请输入姓名"
          >
        </div>
        <div class="inpbox">
          <input
            v-model="registerForm.username"
            autocomplete="off"
            ref="username"
            name="username"
            placeholder="请输入用户名"
          >
        </div>
        <div class="inpbox">
          <input
            type="password"
            v-model="registerForm.userPwd"
            ref="userPwd"
            name="userPwd"
            autocomplete="off"
            placeholder="请输入密码"
          >
          <!-- prop="confirmPassword" -->
        </div>
        <div class="inpbox">
          <input
            type="password"
            v-model="registerForm.confirmPassword"
            autocomplete="off"
            ref="confirmPassword"
            name="confirmPassword"
            placeholder="请确认密码"
          >
        </div>
      </div>
      <div class="action">
        <el-form-item>
          <el-button
            type="primary"
            round
            @click="register"
          >注册</el-button>
          <el-button
            type="danger"
            round
            @click="resetForm"
          >重置</el-button>
          <el-button
            type="success"
            round
            @click="goLogin"
          >去登录</el-button>
        </el-form-item>
      </div>
    </el-form>
  </div>
</template>

<script lang="ts">
import { defineComponent } from "vue";
export default defineComponent({
  name: "Register",
});
</script>
<script lang="ts" setup>
import { ref } from "vue";
import { useRouter } from "vue-router";
import { ElMessage, FormInstance } from "element-plus";
import { registerValidateApi, registerApi } from "../../api/index";
const router = useRouter();
// 初始化注册参数
const registerForm = ref({
  username: "",
  userPwd: "",
  confirmPassword: "",
  nickName: "",
});
const formRef = ref<FormInstance>();
// 校验规则
const validateUsername = (rule: any, value: any, callback: any) => {
  if (value.length < 2 && value.length != null) {
    callback(new Error("用户名长度不能小于2位"));
  } else {
    callback();
  }
};
// 校验规则
const validatePassword = (rule: any, value: any, callback: any) => {
  if (value.length < 6 && value.length != null) {
    callback(new Error("密码长度不能小于6位"));
  } else {
    callback();
  }
};
// 校验规则
const validateConfirmPassword = (rule: any, value: any, callback: any) => {
  if (value.length < 6 && value.length != null) {
    callback(new Error("密码长度不能小于6位"));
  } else {
    callback();
  }
};
// 校验规则
const validateNickName = (rule: any, value: any, callback: any) => {
  if (value.length >= 2 && value.length <= 6) {
    callback();
  } else {
    callback(new Error("姓名必须在2-6位"));
  }
};
// 校验规则
const registerRules = {
  nickName: [{ required: true, trigger: "blur", validator: validateNickName }],
  username: [{ required: true, validator: validateUsername }],
  userPwd: [{ required: true, trigger: "blur", validator: validatePassword }],
  confirmPassword: [
    { required: true, trigger: "blur", validator: validateConfirmPassword },
  ],
};

//点击注册的回调
const register = async () => {
  await formRef.value?.validate();
  if (registerForm.value.userPwd == registerForm.value.confirmPassword) {
    // 调用用户名校验接口
    await registerValidateApi(registerForm.value.username);
    //  整理参数
    const obj = {
      username: "",
      userPwd: "",
      nickName: "",
    };
    obj.username = registerForm.value.username;
    obj.userPwd = registerForm.value.userPwd;
    obj.nickName = registerForm.value.nickName;
    //  调用注册接口
    await registerApi(obj);
    formRef.value?.resetFields();
    ElMessage.success("注册成功");
  } else {
    return ElMessage.error("密码和确定密码必须一致");
  }
};
//点击去登录的回调
const goLogin = () => {
  router.push({ path: "/login" });
};

//点击重置的回调
const resetForm = () => {
  //重置表单
  formRef.value?.resetFields();
};
</script>

<style scoped>
* {
  margin: 0;
  padding: 0;
}

.registerBody {
  height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;

  content: "";
  inset: 5px;
  background: url("/src/assets/img/bg2.jpg");
  background-attachment: fixed;
  background-size: cover;
  background-position: center;
}

.form {
  width: 350px;
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

.main .inpbox input {
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

.action .el-button:hover {
  transform: scale(1.2);
}
</style>
