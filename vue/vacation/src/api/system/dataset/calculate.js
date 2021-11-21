import request from '@/utils/request'

// 查询业绩考核计算设置列表
export function listCalculate(query) {
    return request({
        url: '/glasses/calculate/list',
        method: 'get',
        params: query
    })
}

// 查询业绩考核计算设置详细
export function getCalculate(id) {
    return request({
        url: '/glasses/calculate/' + id,
        method: 'get'
    })
}

// 新增业绩考核计算设置
export function addCalculate(data) {
    return request({
        url: '/glasses/calculate',
        method: 'post',
        data: data
    })
}

// 修改业绩考核计算设置
export function updateCalculate(data) {
    return request({
        url: '/glasses/calculate',
        method: 'put',
        data: data
    })
}

// 删除业绩考核计算设置
export function delCalculate(id) {
    return request({
        url: '/glasses/calculate/' + id,
        method: 'delete'
    })
}

// 导出业绩考核计算设置
export function exportCalculate(query) {
    return request({
        url: '/glasses/calculate/export',
        method: 'get',
        params: query
    })
}
