import request from '@/utils/request'



//根据条件获取未读条目数
export function getNoReadNumStatus(status) {
    return request({
        url: '/glasses/messages/getNoReadNum?status=' + status,
        method: 'get'
    })
}

// 查询消息列表
export function messagesList(status) {
    return request({
        url: '/glasses/messages/list',
        method: 'get'
    })
}


// 查询消息列表
export function clearMessages(ids) {
    return request({
        url: '/glasses/messages/' + ids,
        method: 'put'
    })
}