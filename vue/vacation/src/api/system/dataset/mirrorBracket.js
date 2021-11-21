import request from '@/utils/request'

// 查询镜架参数列表
export function listMirrorBracketParams(query) {
    return request({
        url: '/glasses/mirrorBracketParams/list',
        method: 'get',
        params: query
    })
}

// 查询镜架参数详细
export function getMirrorBracketParams(type) {
    return request({
        url: '/glasses/mirrorBracketParams/getListByType?type=' + type,
        method: 'get'
    })
}

// 新增镜架参数
export function addMirrorBracketParams(data) {
    return request({
        url: '/glasses/mirrorBracketParams',
        method: 'post',
        data: data
    })
}

// 修改镜架参数
export function updateMirrorBracketParams(data) {
    return request({
        url: '/glasses/mirrorBracketParams',
        method: 'put',
        data: data
    })
}

// 删除镜架参数
export function delMirrorBracketParams(id) {
    return request({
        url: '/glasses/mirrorBracketParams/' + id,
        method: 'delete'
    })
}

// 导出镜架参数
export function exportMirrorBracketParams(query) {
    return request({
        url: '/glasses/mirrorBracketParams/export',
        method: 'get',
        params: query
    })
}