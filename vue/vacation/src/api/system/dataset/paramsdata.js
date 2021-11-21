import request from '@/utils/request'

// 查询参数设置列表
export function listParamsData(query) {
  return request({
    url: '/vacation/paramsData/list',
    method: 'get',
    params: query
  })
}

// 查询参数设置详细
export function getParamsData(id) {
  return request({
    url: '/vacation/paramsData/' + id,
    method: 'get'
  })
}

// 查询参数设置详细
export function getBackGroundPic(id) {
  return request({
    url: '/vacation/paramsData/backGroundPic/' + id,
    method: 'get'
  })
}

// 新增参数设置
export function addParamsData(data) {
  return request({
    url: '/vacation/paramsData',
    method: 'post',
    data: data
  })
}

// 修改参数设置
export function updateParamsData(data) {
  return request({
    url: '/vacation/paramsData',
    method: 'put',
    data: data
  })
}

// 查询参数设置详细
export function updateBackGroundPic(data) {
  return request({
    url: '/vacation/paramsData/backGroundPic',
    method: 'put',
    data: data
  })
}

// 删除参数设置
export function delParamsData(id) {
  return request({
    url: '/vacation/paramsData/' + id,
    method: 'delete'
  })
}

// 导出参数设置
export function exportParamsData(query) {
  return request({
    url: '/vacation/paramsData/export',
    method: 'get',
    params: query
  })
}
