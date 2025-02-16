import { createRouter, createWebHistory } from 'vue-router';

const routes = [
    {
        path: '/admin/user',
        name: 'AdminUser',
        component: () => import("./admin/views/User")
    },
    {
        path: '/admin/house_rent',
        name: 'AdminHouseRent',
        component: () => import("./admin/views/HouseRent")
    },
    {
        path: '/admin/house_need',
        name: 'AdminHouseNeed',
        component: () => import("./admin/views/HouseNeed")
    },
    {
        path: '/admin/house_talk',
        name: 'AdminHouseTalk',
        component: () => import("./admin/views/HouseTalk")
    },
    {
        path: '/admin/statics',
        name: 'StatisticsPage',
        component: () => import("./admin/views/StatisticsPage")
    },
    {
        path: '/user/login',
        name: 'UserLogin',
        component: () => import('./user/views/Login.vue'),
    },
    {
        path: '/user/register',
        name: 'UserRegister',
        component: () => import('./user/views/Register.vue'),
    },
    {
        path: '/user/home',
        name: 'UserHome',
        component: () => import("./user/views/Home")
    },
    {
        path: '/user/person',
        name: 'UserPerson',
        component: () => import("./user/views/Person")
    },
    {
        path: '/user/house_rent',
        name: 'UserHouseRent',
        component: () => import("./user/views/HouseRent")
    },
    {
        path: '/user/house_need',
        name: 'UserHouseNeed',
        component: () => import("./user/views/HouseNeed")
    },
    {
        path: '/user/house_talk',
        name: 'UserHouseTalk',
        component: () => import("./user/views/HouseTalk")
    },
    {
        path: '/admin/',
        redirect: '/admin/user',
    },
    {
        path: '/user/',
        redirect: '/user/home',
    },
    {
        path: '/',
        redirect: '/user/home',
    },
    {
        path: '/login/',
        redirect: '/user/login',
    },
];

const router = createRouter({
    history: createWebHistory(process.env.BASE_URL),
    routes,
});

export default router;
