import request from '@/utils/request'


// 假期列表
export function holidayList(data) {
  return request({
    url: '/vacation/holiday/list',
    method: 'GET',
    params: data
  })
}

// 假期详细信息
export function getHolidayInfo(id) {
    return request({
        url: '/vacation/holiday/' + id,
        method: 'GET'
    })
}

// 新增假期
export function holidayAdd(data) {
    return request({
        url: '/vacation/holiday',
        method: 'POST',
        data: data
    })
}

// 修改假期
export function holidayUpdate(data) {
    return request({
        url: '/vacation/holiday',
        method: 'PUT',
        data: data
    })
}

// 删除假期
export function holidayRemove(id) {
    return request({
        url: '/vacation/holiday/' + id,
        method: 'DELETE'
    })
}

export function userListForVacation(data) {
  return request({
    url: '/vacation/holiday/user/list',
    method: 'GET',
    params: data
  })
}

