// src/store/user.js
import { defineStore } from 'pinia'

export const useUserStore = defineStore('user', {
    state: () => ({
        userId: null,
        username: '',
        token: '',
        roles: [],
        permissions: []
    }),

    getters: {
        isLoggedIn: (state) => !!state.token,
        getUserId: (state) => state.userId,
        getUsername: (state) => state.username
    },

    actions: {
        // 设置用户信息
        setUserInfo(userInfo) {
            this.userId = userInfo.userId
            this.username = userInfo.username
            this.roles = userInfo.roles || []
            this.permissions = userInfo.permissions || []
        },

        // 设置Token
        setToken(token) {
            this.token = token
            localStorage.setItem('token', token)
        },

        // 登录
        async login(credentials) {
            try {
                // 调用登录API
                const response = await fetch('/api/auth/login', {
                    method: 'POST',
                    headers: { 'Content-Type': 'application/json' },
                    body: JSON.stringify(credentials)
                })

                const data = await response.json()

                if (data.success) {
                    this.setToken(data.data.token)
                    this.setUserInfo(data.data.user)
                    return true
                }
                return false
            } catch (error) {
                console.error('登录失败:', error)
                return false
            }
        },

        // 登出
        logout() {
            this.userId = null
            this.username = ''
            this.token = ''
            this.roles = []
            this.permissions = []
            localStorage.removeItem('token')
        },

        // 从localStorage恢复登录状态
        restoreLogin() {
            const token = localStorage.getItem('token')
            if (token) {
                this.token = token
                // TODO: 调用API验证token并获取用户信息
            }
        }
    }
})
