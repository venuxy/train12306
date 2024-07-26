//所有的session key都在这里统一定义，可以避免多个功能使用冲突
SESSION_ORDER = 'SESSION_ORDER';
SESSION_TICKET_PARAMS = 'SESSION_TICKET_PARAMS';
<template>
  <div>{{ dailyTrainTicket }}</div>
</template>
<script>
import { defineComponent } from 'vue';

export default defineComponent({
  name: 'order-view',
  setup() {
    const  dailyTrainTicket = SessionStorage.get(SESSION_ORDER) || {};
    console.log("下单的车次信息",dailyTrainTicket);
    return {
      dailyTrainTicket
    };
  },
});
</script>