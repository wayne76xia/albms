import request from '@/utils/request'

// 查询会员等级设置列表
export function listMemberLevel(query) {
    return request({
        url: '/glasses/memberLevel/list',
        method: 'get',
        params: query
    })
}

// 查询会员等级设置详细
export function getMemberLevel(id) {
    return request({
        url: '/glasses/memberLevel/' + id,
        method: 'get'
    })
}

// 新增会员等级设置
export function addMemberLevel(data) {
    return request({
        url: '/glasses/memberLevel',
        method: 'post',
        data: data
    })
}
// 修改会员等级设置
export function updateMemberLevel(data) {
    return request({
        url: '/glasses/memberLevel',
        method: 'put',
        data: data
    })
}

// 删除会员等级设置
export function delMemberLevel(id) {
    return request({
        url: '/glasses/memberLevel/' + id,
        method: 'delete'
    })
}

// 导出会员等级设置
export function exportMemberLevel(query) {
    return request({
        url: '/glasses/memberLevel/export',
        method: 'get',
        params: query
    })
}


// 生成二维码
export function getQrCode(query) {
    return request({
        url: '/vacation/paramsData/getQrCode',
        method: 'get',
        params: query
    })
}
