import request from '@/utils/request'


// 获取验证码
export function getCodes(phone, dealType, templateType) {
    return request({
        url: '/sendSMS?phone=' + phone + '&dealType=' + dealType + '&templateType=' + templateType,
        method: 'post'
    })
}

// 忘记密码
export function forgetPassword(data) {
    return request({
        url: '/forgetPassword',
        method: 'post',
        data: data
    })
}