<template>
<!--  <a-layout-sider width="200" style="background: #fff">-->
    <a-menu
        v-model:selectedKeys="selectedKeys"
        :openKeys="['batch', 'base', 'business']"
        mode="inline"
        :style="{ height: '100%', borderRight: 0 }"
    >
      <a-menu-item key="/welcome">
        <router-link to="/welcome">
          <coffee-outlined /> &nbsp; 欢迎
        </router-link>
      </a-menu-item>
      <a-menu-item key="/about">
        <router-link to="/about">
          <user-outlined /> &nbsp; 关于
        </router-link>
      </a-menu-item>
      <a-sub-menu key="business">
        <template #title>
          <span>
            <UnorderedListOutlined />
            业务管理
          </span>
        </template>
        <a-menu-item key="/business/daily-train">
          <router-link to="/business/daily-train">
            <MenuUnfoldOutlined /> &nbsp; 每日车次
          </router-link>
        </a-menu-item>
        <a-menu-item key="/business/daily-train-station">
          <router-link to="/business/daily-train-station">
            <MenuUnfoldOutlined /> &nbsp; 每日经停站
          </router-link>
        </a-menu-item>
        <a-menu-item key="/business/daily-train-carriage">
          <router-link to="/business/daily-train-carriage">
            <MenuUnfoldOutlined /> &nbsp; 每日车厢
          </router-link>
        </a-menu-item>
      </a-sub-menu>
      <a-sub-menu key="base">
        <template #title>
          <span>
            <UnorderedListOutlined />
            基础数据
          </span>
        </template>
        <a-menu-item key="/base/station">
          <router-link to="/base/station">
            <MenuUnfoldOutlined /> &nbsp; 车站管理
          </router-link>
        </a-menu-item>
        <a-menu-item key="/base/train">
          <router-link to="/base/train">
            <MenuUnfoldOutlined /> &nbsp; 火车管理
          </router-link>
        </a-menu-item>
        <a-menu-item key="/base/train-station">
          <router-link to="/base/train-station">
            <MenuUnfoldOutlined /> &nbsp; 火车车站
          </router-link>
        </a-menu-item>
        <a-menu-item key="/base/train-carriage">
          <router-link to="/base/train-carriage">
            <MenuUnfoldOutlined /> &nbsp; 火车车厢
          </router-link>
        </a-menu-item>
        <a-menu-item key="/base/train-seat">
          <router-link to="/base/train-seat">
            <MenuUnfoldOutlined /> &nbsp; 火车座位
          </router-link>
        </a-menu-item>
      </a-sub-menu>

      <a-sub-menu key="batch">
        <template #title>
          <span>
            <UnorderedListOutlined />
            跑批管理
          </span>
        </template>
        <a-menu-item key="/batch/job">
          <router-link to="/batch/job">
            <MenuUnfoldOutlined /> &nbsp; 任务管理
          </router-link>
        </a-menu-item>
      </a-sub-menu>
<!--      <a-menu-item key="/station">-->
<!--        <router-link to="/station">-->
<!--          <coffee-outlined /> &nbsp; 车站管理-->
<!--        </router-link>-->
<!--      </a-menu-item>-->
<!--      <a-menu-item key="/train">-->
<!--        <router-link to="/train">-->
<!--          <coffee-outlined /> &nbsp; 火车管理-->
<!--        </router-link>-->
<!--      </a-menu-item>-->
<!--      <a-menu-item key="/train-station">-->
<!--        <router-link to="/train-station">-->
<!--          <coffee-outlined /> &nbsp; 经停站-->
<!--        </router-link>-->
<!--      </a-menu-item>-->
<!--      <a-menu-item key="/train-carriage">-->
<!--        <router-link to="/train-carriage">-->
<!--          <coffee-outlined /> &nbsp; 车厢-->
<!--        </router-link>-->
<!--      </a-menu-item>-->
<!--      <a-menu-item key="/train-seat">-->
<!--        <router-link to="/train-seat">-->
<!--          <coffee-outlined /> &nbsp; 座位-->
<!--        </router-link>-->
<!--      </a-menu-item>-->
    </a-menu>
<!--  </a-layout-sider>-->
</template>

<script>
import {defineComponent, ref, watch} from 'vue';
import router from "@/router";

export default defineComponent({
  name: "the-sider-view",
  setup() {
    const selectedKeys = ref([]);

    watch(() => router.currentRoute.value.path, (newValue) => {
      console.log('watch', newValue);
      selectedKeys.value = [];
      selectedKeys.value.push(newValue);
    }, {immediate: true});
    return {
      selectedKeys
    };
  },
});
</script>

<!--scoped指令可以让样式只在当前组件生效-->
<style scoped>

</style>
