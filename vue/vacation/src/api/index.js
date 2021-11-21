import request from '@/utils/request'


// 首页上面统计数据
export function getTopData() {
    return request({
        url: '/index/topData',
        method: 'get'
    })
}


// 销售额分析
export function salePriceAnalyse() {
    return request({
        url: '/index/salePriceAnalyse',
        method: 'get'
    })
}


// 销售记录分析
export function saleNumAnalyse() {
    return request({
        url: '/index/saleNumAnalyse',
        method: 'get'
    })
}


// 会员分析
export function clientMemberLevelAnalyse() {
    return request({
        url: '/index/clientMemberLevelAnalyse',
        method: 'get'
    })
}

//待审核售后订单
export function afterSalesOrderToBeReviewed() {
    return request({
        url: '/index/afterSalesOrderToBeReviewed',
        method: 'get'
    })
}

//待审核积分变更
export function afterIntegralOrderToBeReviewed() {
    return request({
        url: '/index/afterIntegralOrderToBeReviewed',
        method: 'get'
    })
}


//待审核库存调拨
export function afterAllotOrderToBeReviewed() {
    return request({
        url: '/index/afterAllotOrderToBeReviewed',
        method: 'get'
    })
}

//待处理事项
export function pendingMatters() {
    return request({
        url: '/index/pendingMatters',
        method: 'get'
    })
}

//待处理事项
export function indexPopUpWindows() {
    return request({
        url: '/index/indexPopUpWindows',
        method: 'get'
    })
}

//根据条件获取未读条目数
export function getNoReadNum() {
    return request({
        url: '/glasses/messages/getNoReadNum',
        method: 'get'
    })
}