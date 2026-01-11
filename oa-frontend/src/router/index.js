import { createRouter, createWebHistory } from 'vue-router'

const routes = [
    {
        path: '/',
        redirect: '/login'
    },
    {
        path: '/login',
        name: 'Login',
        component: () => import('../views/Login.vue')
    },
    {
        path: '/register',
        name: 'Register',
        component: () => import('../views/Register.vue')
    },
    {
        path: '/dashboard',
        name: 'Dashboard',
        component: () => import('../views/Dashboard.vue'),
        children: [
            {
                path: 'meeting/rooms',
                name: 'MeetingRooms',
                component: () => import('../views/meeting/RoomList.vue')
            },
            {
                path: 'material/list',
                name: 'MaterialList',
                component: () => import('../views/material/MaterialList.vue')
            },
            {
                path: 'meeting/my',
                name: 'MyBookings',
                component: () => import('../views/meeting/MyBookings.vue')
            },
            {
                path: 'material/my',
                name: 'MyApplyList',
                component: () => import('../views/material/MyApplyList.vue')
            },
            {
                path: 'system/employees',
                name: 'EmployeeList',
                component: () => import('../views/system/EmployeeList.vue')
            },
            {
                path: 'workflow/done',
                name: 'DoneList',
                component: () => import('../views/workflow/DoneList.vue')
            },
            {
                path: 'workflow/todo',
                name: 'TodoList',
                component: () => import('../views/workflow/TodoList.vue')
            },

            {
                path: 'contacts',
                name: 'AddressBook',
                component: () => import('../views/system/AddressBook.vue')
            },
            {
                path: 'system/roles',
                name: 'RoleList',
                component: () => import('../views/system/RoleList.vue')
            },
            {
                path: 'workflow/apply',
                name: 'ApplyProcess',
                component: () => import('../views/workflow/ApplyProcess.vue')
            },
            {
                path: 'workflow/my',
                name: 'MyWorkflowList',
                component: () => import('../views/workflow/MyWorkflowList.vue')
            },
            {
                path: 'profile',
                name: 'Profile',
                component: () => import('../views/Profile.vue')
            },
            {
                path: 'notifications',
                name: 'Notifications',
                component: () => import('../views/Notifications.vue')
            },
            {
                path: 'administrative/cars',
                name: 'CarList',
                component: () => import('../views/administrative/CarList.vue')
            },

            {
                path: 'attendance/clock',
                name: 'AttendanceClock',
                component: () => import('../views/attendance/AttendanceClock.vue')
            },
            {
                path: 'attendance/records',
                name: 'MyAttendance',
                component: () => import('../views/attendance/MyAttendance.vue')
            },
            {
                path: 'attendance/leave',
                name: 'LeaveRequest',
                component: () => import('../views/attendance/LeaveRequest.vue')
            },
            {
                path: 'attendance/overtime',
                name: 'OvertimeRequest',
                component: () => import('../views/attendance/OvertimeRequest.vue')
            },
            {
                path: 'attendance/trip',
                name: 'BusinessTrip',
                component: () => import('../views/attendance/BusinessTrip.vue')
            },
            {
                path: 'attendance/stats',
                name: 'AttendanceStats',
                component: () => import('../views/attendance/AttendanceStats.vue')
            },
            {
                path: 'hr/departments',
                name: 'DepartmentList',
                component: () => import('../views/hr/DepartmentList.vue')
            },
            {
                path: 'hr/org-chart',
                name: 'OrgChart',
                component: () => import('../views/hr/OrgChart.vue')
            },
            {
                path: 'hr/employees',
                name: 'EmployeeProfile',
                component: () => import('../views/hr/EmployeeProfile.vue')
            },
            {
                path: 'finance/claim',
                name: 'ExpenseClaim',
                component: () => import('../views/finance/ExpenseClaim.vue')
            },
            {
                path: 'finance/my-expenses',
                name: 'MyExpenses',
                component: () => import('../views/finance/MyExpenses.vue')
            },
            {
                path: 'report/dashboard',
                name: 'ReportDashboard',
                component: () => import('../views/report/ReportDashboard.vue')
            },
            {
                path: 'cloud/drive',
                name: 'CloudDrive',
                component: () => import('../views/cloud/CloudDrive.vue')
            },
            {
                path: 'message/email',
                name: 'EmailSystem',
                component: () => import('../views/message/EmailSystem.vue')
            },
            {
                path: 'system/projects',
                name: 'ProjectManage',
                component: () => import('../views/system/ProjectManage.vue')
            },
            {
                path: 'system/announcements',
                name: 'AnnouncementManage',
                component: () => import('../views/system/AnnouncementManage.vue')
            }
        ]
    }
]

const router = createRouter({
    history: createWebHistory(),
    routes
})

export default router
