import request from '@/utils/request'

// 查询公告查看记录列表
export function listNoticeRecord(query) {
    return request({
        url: '/glasses/noticeRecord/list',
        method: 'get',
        params: query
    })
}

// 查询公告查看记录详细
export function getNoticeRecord(id) {
    return request({
        url: '/glasses/noticeRecord/' + id,
        method: 'get'
    })
}

// 新增公告查看记录
export function addNoticeRecord(data) {
    return request({
        url: '/glasses/noticeRecord',
        method: 'post',
        data: data
    })
}

// 修改公告查看记录
export function updateNoticeRecord(data) {
    return request({
        url: '/glasses/noticeRecord',
        method: 'put',
        data: data
    })
}

// 删除公告查看记录
export function delNoticeRecord(id) {
    return request({
        url: '/glasses/noticeRecord/' + id,
        method: 'delete'
    })
}

// 导出公告查看记录
export function exportNoticeRecord(query) {
    return request({
        url: '/glasses/noticeRecord/export',
        method: 'get',
        params: query
    })
}