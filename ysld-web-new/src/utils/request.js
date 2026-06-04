import Vue from 'vue'
import axios from 'axios'
import store from '@/store'
import { VueAxios } from './axios'
import {Modal, notification} from 'ant-design-vue'
import { ACCESS_TOKEN } from "@/store/mutation-types"

/**
 * 【指定 axios的 baseURL】
 * 如果手工指定 baseURL: '/jshERP-boot'
 * 则映射后端域名，通过 vue.config.js
 * @type {*|string}
 */
let apiBaseUrl = window._CONFIG['domianURL'] || "/jshERP-boot";
//console.log("apiBaseUrl= ",apiBaseUrl)
// 创建 axios 实例
const service = axios.create({
  baseURL: apiBaseUrl, // api base_url
  timeout: 300000 // 请求超时时间
})

function nowMs() {
  if (window && window.performance && window.performance.now) {
    return window.performance.now()
  }
  return Date.now()
}

function logRequestCost(config, status) {
  if (!config || !config.metadata || typeof config.metadata.startTime !== 'number') {
    return
  }
  const duration = Math.round(nowMs() - config.metadata.startTime)
  const method = (config.method || 'GET').toUpperCase()
  const url = ((config.baseURL || '') + (config.url || '')).replace(/\/{2,}/g, '/').replace(':/', '://')
  // 统一接口耗时日志，便于快速定位慢接口
  console.log('[API]', method, url, 'status:', status, 'cost:', duration + 'ms')
}

const err = (error) => {
  if (error && error.config) {
    const status = error.response ? error.response.status : 'NETWORK_ERROR'
    logRequestCost(error.config, status)
  }
  if (error.response) {
    let data = error.response.data
    const token = Vue.ls.get(ACCESS_TOKEN)
    switch (error.response.status) {
      case 403:
        notification.error({ message: '系统提示', description: '拒绝访问',duration: 4})
        break
      case 500:
        if(token && data==="loginOut"){
          Modal.error({
            title: '登录已过期',
            content: '很抱歉，登录已过期，请重新登录',
            okText: '重新登录',
            mask: false,
            onOk: () => {
              Vue.ls.remove(ACCESS_TOKEN)
              window.location.reload()
            }
          })
        }
        break
      case 404:
          notification.error({ message: '系统提示', description:'很抱歉，资源未找到!',duration: 4})
        break
      case 504:
        notification.error({ message: '系统提示', description: '网络超时'})
        break
      case 401:
        notification.error({ message: '系统提示', description:'未授权，请重新登录',duration: 4})
        if (token) {
          store.dispatch('Logout').then(() => {
            setTimeout(() => {
              window.location.reload()
            }, 1500)
          })
        }
        break
      default:
        notification.error({
          message: '系统提示',
          description: data.message,
          duration: 4
        })
        break
    }
  }
  return Promise.reject(error)
};

// request interceptor
service.interceptors.request.use(config => {
  if (!config.metadata) {
    config.metadata = {}
  }
  config.metadata.startTime = nowMs()
  const token = Vue.ls.get(ACCESS_TOKEN)
  if (token) {
    config.headers[ 'X-Access-Token' ] = token // 让每个请求携带自定义 token 请根据实际情况自行修改
  }
  return config
},(error) => {
  return Promise.reject(error)
})

// response interceptor
service.interceptors.response.use((response) => {
    logRequestCost(response.config, response.status)
    return response.data
  }, err)

const installer = {
  vm: {},
  install (Vue, router = {}) {
    Vue.use(VueAxios, router, service)
  }
}

export {
  installer as VueAxios,
  service as axios
}