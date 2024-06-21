import { createStore } from 'vuex'

const MEMEBER = "MEMEBER";
export default createStore({
  state: {
    member : window.SessionStorage.get(MEMEBER) || {}
  },
  getters: {
  },
  mutations: {
    setMember(state, _member) {
      state.member = _member;
      window.SessionStorage.set(MEMEBER, _member);
    }
  },
  actions: {
  },
  modules: {
  }
})
