import request from '@/utils/request'

// 登录方法
export function login(username, password, code, uuid) {
    const data = {
        username,
        password,
        code,
        uuid
    }
    return request({
        url: '/login',
        method: 'post',
        data: data
    })
}
//根据登录名获取部门

export function getUserName(user) {
    return request({
        url: '/getDeptNameByUserName?userName=' + user,
        method: 'get'
    })
}




// 获取用户详细信息
export function getInfo() {
    return request({
        url: '/getInfo',
        method: 'get'
    })
}


//获取登录页面图片及技术客服电话
export function getLoginInfo() {
    return request({
        url: '/getLoginBackGroundPicAndTechnicalPhone',
        method: 'get'
    })
}


// 退出方法
export function logout() {
    return request({
        url: '/logout',
        method: 'post'
    })
}

// 获取验证码
export function getCodeImg() {
    return request({
        url: '/captchaImage',
        method: 'get'
    })
}