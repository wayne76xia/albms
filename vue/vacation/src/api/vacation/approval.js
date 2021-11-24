import request from '@/utils/request'

// 假期审批列表
export function holidayApprovalList(data) {
  return request({
    url: '/vacation/holiday/approval/list',
    method: 'get',
    params: data
  })
}

// 角色列表
export function roleList() {
  return request({
    url: '/vacation/holiday/approval/roleList',
    method: 'get'
  })
}

// 假期审批详细信息
export function getHolidayApprovalInfo(id) {
  return request({
    url: '/vacation/holiday/approval/' + id,
    method: 'get'
  })
}

// 新增假期审批
export function holidayApprovalAdd(data) {
  return request({
    url: '/vacation/holiday/approval',
    method: 'post',
    data: data
  })
}

// 修改假期审批
export function holidayApprovalUpdate(data) {
  return request({
    url: '/vacation/holiday/approval',
    method: 'put',
    data: data
  })
}

// 删除假期审批
export function holidayApprovalRemove(id) {
  return request({
    url: '/vacation/holiday/approval/' + id,
    method: 'delete'
  })
}
