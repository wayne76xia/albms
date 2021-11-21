import request from '@/utils/request'

// 查询生成表数据
export function getAllSubsidiaryCompany() {
    return request({
        url: '/system/dept/getAllSubsidiaryCompany',
        method: 'get',
    })
}
// 子公司列表数据
export function subsidiaryCompanyList(data) {
    console.log(data)
    return request({
        url: '/system/dept/subsidiaryCompanyList',
        method: 'get',
        params: data
    })
}
// 子公司列表数据导出
export function subsidiaryCompanyListExport(data) {
    console.log(data)
    return request({
        url: '/system/dept/subsidiaryCompanyListExport',
        method: 'get',
        params: data
    })
}

// 下载子公司导入模板
export function importTemplate() {
    return request({
        url: '/system/dept/importTemplate',
        method: 'get',
    })
}

// 子公司详细信息
export function subsidiaryCompanyInfo(deptId) {
    return request({
        url: '/system/dept/subsidiaryCompanyInfo',
        method: 'get',
        params: { deptId }
    })
}
// 查询用户详情
export function userInfo(userId) {
    return request({
        url: '/system/user/userInfo',
        method: 'get',
        params: { userId }
    })
}


// 根据子公司id查询门店列表
export function getShop(ids) {
    return request({
        url: '/commonApi/getShopListByCompanyIds?ids=' + ids,
        method: 'get'
    })
}




// 门店接口
// 获取门店列表
export function shopList(data) {
    return request({
        url: '/system/dept/shopList',
        method: 'get',
        params: data
    })
}

// 查询门店信息
export function shopQuery(deptId) {
    return request({
        url: '/system/dept/' + deptId,
        method: 'get',
    })
}

// 门店详细信息
export function shopInfo(deptId) {
    return request({
        url: '/system/dept/shopInfo',
        method: 'get',
        params: { deptId }
    })
}
// 新增门店
export function addShop(data) {
    return request({
        url: '/system/dept',
        method: 'post',
        data: data
    })
}

// 修改门店
export function updateShop(data) {
    return request({
        url: '/system/dept',
        method: 'put',
        data: data
    })
}
// 删除门店
export function delShop(id) {
    return request({
        url: '/system/dept/' + id,
        method: 'delete'
    })
}
// 下载门店导入模板
export function importShopTemplate(data) {
    return request({
        url: '/system/dept/importShopTemplate',
        method: 'get',
        params: data
    })
}
// 门店数据导出
export function shopListExport(data) {
    console.log(data)
    return request({
        url: '/system/dept/shopListExport',
        method: 'get',
        params: data
    })
}


// 获取部门下员工列表
export function deptEmployeesList(deptId) {
    return request({
        url: '/system/dept/deptEmployeesList?deptId=' + deptId,
        method: 'get',
    })
}