const getters = {
    sidebar: state => state.app.sidebar,
    size: state => state.app.size,
    device: state => state.app.device,
    visitedViews: state => state.tagsView.visitedViews,
    cachedViews: state => state.tagsView.cachedViews,
    token: state => state.user.token,
    avatar: state => state.user.avatar,

    name: state => state.user.name,
    introduction: state => state.user.introduction,
    roles: state => state.user.roles,
    permissions: state => state.user.permissions,
    permission_routes: state => state.permission.routes,
    //新增
    remark: state => state.user.remark,
    infonum: state => state.user.infonum,
    rolesname: state => state.user.rolesname,
}
export default getters