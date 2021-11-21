import request from '@/utils/request'

// 用户数据分析
export function userTopData() {
    return request({
        url: '/system/user/topData',
        method: 'get',
    })
}