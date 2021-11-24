import request from '@/utils/request'


// 假期列表
export function holidayList(data) {
  return request({
    url: '/vacation/holiday/list',
    method: 'get',
    params: data
  })
}

// 假期审批列表
export function holidayListNeedApproval(data) {
  return request({
    url: '/vacation/holiday/approvalList',
    method: 'get',
    params: data
  })
}

// 假期详细信息
export function getHolidayInfo(id) {
    return request({
        url: '/vacation/holiday/' + id,
        method: 'get'
    })
}

// 新增假期
export function holidayAdd(data) {
    return request({
        url: '/vacation/holiday',
        method: 'post',
        data: data
    })
}

// 修改假期
export function holidayUpdate(data) {
    return request({
        url: '/vacation/holiday',
        method: 'put',
        data: data
    })
}

// 删除假期
export function holidayRemove(id) {
    return request({
        url: '/vacation/holiday/' + id,
        method: 'delete'
    })
}

export function userListForVacation(data) {
  return request({
    url: '/vacation/holiday/user/list',
    method: 'get',
    params: data
  })
}

export function hasNextApproved(id) {
  return request({
    url: '/vacation/holiday/next/' + id,
    method: 'get'
  })
}


