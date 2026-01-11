// src/utils/request.js
import axios from 'axios'
import { ElMessage } from 'element-plus'
import router from '../router'

const service = axios.create({
    baseURL: '/api',
    timeout: 10000
})

// 请求拦截器
service.interceptors.request.use(
    config => {
        // 添加token
        const token = localStorage.getItem('token')
        if (token) {
            config.headers['Authorization'] = `Bearer ${token}`
        }
        return config
    },
    error => {
        console.error('请求错误:', error)
        return Promise.reject(error)
    }
)

// 响应拦截器
service.interceptors.response.use(
    response => {
        const res = response.data

        // 如果响应不成功
        if (!res.success) {
            ElMessage.error(res.message || '请求失败')

            // 如果是401未授权,跳转到登录页
            if (res.code === 401) {
                router.push('/login')
            }

            return Promise.reject(new Error(res.message || 'Error'))
        }

        return res.data // 直接返回data部分
    },
    error => {
        console.error('响应错误:', error)

        let message = '网络错误'
        if (error.response) {
            switch (error.response.status) {
                case 401:
                    message = '未授权,请登录'
                    router.push('/login')
                    break
                case 403:
                    message = '拒绝访问'
                    break
                case 404:
                    message = '请求地址不存在'
                    break
                case 500:
                    message = '服务器错误'
                    break
                default:
                    message = error.response.data?.message || '请求失败'
            }
        }

        ElMessage.error(message)
        return Promise.reject(error)
    }
)

export default service
