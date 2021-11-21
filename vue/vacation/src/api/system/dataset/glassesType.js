import request from '@/utils/request'

// 查询镜架型号列表
export function listType(query) {
    return request({
        url: '/glasses/type/list',
        method: 'get',
        params: query
    })
}

// 查询镜架型号列表
export function getListByType(query) {
    return request({
        url: '/glasses/mirrorBracketParams/getListByType?type=' + query,
        method: 'get',
    })
}

// 查询镜架型号详细
export function getType(id) {
    return request({
        url: '/glasses/type/' + id,
        method: 'get'
    })
}

// 新增镜架型号
export function addType(data) {
    return request({
        url: '/glasses/type',
        method: 'post',
        data: data
    })
}

// 修改镜架型号
export function updateType(data) {
    return request({
        url: '/glasses/type',
        method: 'put',
        data: data
    })
}

// 删除镜架型号
export function delType(id) {
    return request({
        url: '/glasses/type/' + id,
        method: 'delete'
    })
}

// 导出镜架型号
export function exportType(query) {
    return request({
        url: '/glasses/type/export',
        method: 'get',
        params: query
    })
}