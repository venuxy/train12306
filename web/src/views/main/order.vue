//所有的session key都在这里统一定义，可以避免多个功能使用冲突
SESSION_ORDER = 'SESSION_ORDER';
SESSION_TICKET_PARAMS = 'SESSION_TICKET_PARAMS';
<template>
<!--  <div>{{ dailyTrainTicket }}</div>-->
  <div class="order-train">
    <span class="order-train-main">{{ dailyTrainTicket.date }}</span>&nbsp;
    <span class="order-train-main">{{ dailyTrainTicket.trainCode }}</span>次&nbsp;
    <span class="order-train-main">{{ dailyTrainTicket.start }}</span>站&nbsp;
    <span class="order-train-main">{{ dailyTrainTicket.startTime }}</span>&nbsp;
    <span class="order-train-main">----</span>&nbsp;
    <span class="order-train-main">{{ dailyTrainTicket.end }}</span>站&nbsp;
    <span class="order-train-main">{{ dailyTrainTicket.endTime }}</span>&nbsp;

    <div class="order-train-ticket">
      <span v-for="item in seatTypes" :key="item.type">
        <span>{{item.desc}}</span>
        <span class="order-train-ticket-main">{{item.price}}</span>元&nbsp;
        <span class="order-train-ticket-main">{{item.count}}</span>&nbsp;张票&nbsp;&nbsp;
      </span>
    </div>
  </div>
  <a-divider></a-divider>
  {{passengers}}
</template>
<script>
import {defineComponent, onMounted, ref} from 'vue';
import passenger from "./passenger.vue";
import axios from "axios";

export default defineComponent({
  name: 'order-view',
  computed: {
    passenger() {
      return passenger
    }
  },
  setup() {
    const passengers = ref([]);
    const  dailyTrainTicket = SessionStorage.get(SESSION_ORDER) || {};
    console.log("下单的车次信息",dailyTrainTicket);
    const SEAT_TYPE = window.SEAT_TYPE;
    console.log(SEAT_TYPE)
    // 本车次提供的座位类型seatTypes，含票价，余票等信息，例：
    // {
    //   type: "YDZ",
    //   code: "1",
    //   desc: "一等座",
    //   count: "100",
    //   price: "50",
    // }
    // 关于SEAT_TYPE[KEY]：当知道某个具体的属性xxx时，可以用obj.xxx，当属性名是个变量时，可以使用obj[xxx]
    const seatTypes = [];
    for (let KEY in SEAT_TYPE) {
      let key = KEY.toLowerCase();
      if (dailyTrainTicket[key] >= 0) {
        seatTypes.push({
          type: KEY,
          code: SEAT_TYPE[KEY]["code"],
          desc: SEAT_TYPE[KEY]["desc"],
          count: dailyTrainTicket[key],
          price: dailyTrainTicket[key + 'Price'],
        })
      }
    }
    console.log("本车次提供的座位：", seatTypes)

    const handleQueryPassenger = () => {
      axios.get("/member/passenger/query-mine").then((response) => {
        let data = response.data;
        if (data.success) {
          passengers.value = data.content;
          // passengers.value = data.content;
          // passengers.value.forEach((item) => passengerOptions.value.push({
          //   label: item.name,
          //   value: item
          // }))
        } else {
          notification.error({description: data.message});
        }
      });
    };

    onMounted(() => {
      handleQueryPassenger();
    });

    return {
      dailyTrainTicket,
      seatTypes,
      passengers
    };
  },
});
</script>

<style scoped>
.order-train {
  font-size: 20px;
  color: #fff;
  margin-top: 20px;
}

.order-train-main {
  color: #fff;
}

.order-train .order-train-ticket {
  margin-top: 15px;
}
.order-train .order-train-ticket .order-train-ticket-main {
  color: red;
  font-size: 18px;
}
</style>