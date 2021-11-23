import request from '@/utils/request'

// 假期列表
export function holidayApprovalList(data) {
  return request({
    url: '/vacation/holiday/approval/list',
    method: 'GET',
    params: data
  })
}
