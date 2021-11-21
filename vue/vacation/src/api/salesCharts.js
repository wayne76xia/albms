import request from '@/utils/request'


// 获取验证码
export function salesTopData(type) {
    return request({
        url: '/glasses/salesOrder/topData?type=' + type,
        method: 'get'
    })
}