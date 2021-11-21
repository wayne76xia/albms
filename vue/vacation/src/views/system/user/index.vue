<template>
  <div class="app-container">
    <div class>
      <chart />
    </div>
    <el-row :gutter="20" class="aui-margin-t-15">
      <!--部门数据-->
      <el-col :span="4" :xs="24">
        <div class="head-container">
          <el-input
            v-model="deptName"
            placeholder="请输入部门名称"
            clearable
            size="small"
            prefix-icon="el-icon-search"
            style="margin-bottom: 20px"
          />
        </div>
        <div class="head-container">
          <el-tree
            :data="deptOptions"
            :props="defaultProps"
            :expand-on-click-node="false"
            :filter-node-method="filterNode"
            ref="tree"
            default-expand-all
            @node-click="handleNodeClick"
          />
        </div>
      </el-col>
      <!--用户数据-->
      <el-col :span="20" :xs="24">
        <div class="flex flex_start title_box aui-padded-b-15">
          <p class="font-size-20">用户管理</p>
          <div
            class="addnew bg-theme text-white aui-margin-l-10 border-radius font-size-14"
            @click="handleAdd"
            v-hasPermi="['system:user:add']"
          >新增</div>
          <div
            class="addnew delnew text-white aui-margin-l-10 border-radius font-size-14"
            @click="handleDelete"
            v-hasPermi="['system:user:remove']"
          >删除</div>
        </div>
        <div class="table_data">
          <div class="bg-white bg_shadow bg-radius">
            <div class="aui-padded-15 aui-padded-t-10 aui-padded-b-0">
              <el-form :model="queryParams" ref="queryForm" :inline="true" label-width="100px">
                <el-row>
                  <el-col :span="20">
                    <el-form-item label="用户名称：" prop="userName">
                      <el-input
                        v-model="queryParams.userName"
                        placeholder="请输入用户名称"
                        clearable
                        size="small"
                        @keyup.enter.native="handleQuery"
                      />
                    </el-form-item>
                    <el-form-item label="手机号码：" prop="phonenumber">
                      <el-input
                        v-model="queryParams.phonenumber"
                        placeholder="请输入手机号码"
                        clearable
                        size="small"
                        @keyup.enter.native="handleQuery"
                      />
                    </el-form-item>
                    <el-form-item label="岗位：">

                      <el-select v-model="queryParams.postId"  placeholder="请选择" style="width:100%"  clearable filterable>
                        <el-option
                          v-for="item in postOptions"
                          :key="item.postId"
                          :label="item.postName"
                          :value="item.postId"
                          :disabled="item.status == 1"

                        ></el-option>
                      </el-select>
                    </el-form-item>
                    <el-form-item label="创建时间：">
                      <el-date-picker
                        v-model="dateRange"
                        size="small"
                        style="width: 240px"
                        value-format="yyyy-MM-dd"
                        type="daterange"
                        range-separator="-"
                        start-placeholder="开始日期"
                        end-placeholder="结束日期"
                      ></el-date-picker>
                    </el-form-item>
                    <el-form-item>
                      <el-button type="primary" size="medium" @click="handleQuery">查询</el-button>
                      <el-button size="medium" @click="resetQuery">重置</el-button>
                    </el-form-item>
                  </el-col>
                  <el-col :span="4">
                    <!-- <el-button
                      type="primary"
                      icon="el-icon-plus"
                      size="mini"
                      @click="handleAdd"
                      v-hasPermi="['system:user:add']"
                    >新增</el-button>
                    <el-button
                      type="success"
                      icon="el-icon-edit"
                      size="mini"
                      :disabled="single"
                      @click="handleUpdate"
                      v-hasPermi="['system:user:edit']"
                    >修改</el-button>
                    <el-button
                      type="danger"
                      icon="el-icon-delete"
                      size="mini"
                      :disabled="multiple"
                      @click="handleDelete"
                      v-hasPermi="['system:user:remove']"
                    >删除</el-button>-->
                    <!-- <el-button
                      type="info"
                      icon="el-icon-upload2"
                      size="mini"
                      @click="handleImport"
                      v-hasPermi="['system:user:import']"
                    >导入</el-button>
                    <el-button
                      type="warning"
                      icon="el-icon-download"
                      size="mini"
                      @click="handleExport"
                      v-hasPermi="['system:user:export']"
                    >导出</el-button>-->

                    <div class>
                      <div class="flex export_box" style="float:right">
                        <div class="text-black font-size-14 aui-padded-r-10">共{{total}}条数据</div>
                        <el-button
                          size="mini"
                          @click="handleImport"
                          v-hasPermi="['system:user:import']"
                        >
                          <img src="../../../assets/image/daoru.png" alt />
                          <p style="margin-top:2px">导入</p>
                        </el-button>
                        <el-button
                          size="mini"
                          @click="handleExport"
                          v-hasPermi="['system:user:export']"
                        >
                          <img src="../../../assets/image/daochu.png" alt />
                          <p style="margin-top:2px">导出</p>
                        </el-button>
                      </div>
                    </div>
                  </el-col>
                </el-row>
              </el-form>
            </div>
            <el-table
              v-loading="loading"
              :data="userList"
              @selection-change="handleSelectionChange"
              stripe
            >
              <el-table-column type="selection" width="80" align="center" />
              <el-table-column label="用户工号" align="center" prop="userCode" />
              <el-table-column
                label="用户名称"
                align="center"
                prop="userName"
                :show-overflow-tooltip="true"
              />
              <el-table-column
                label="用户昵称"
                align="center"
                prop="nickName"
                :show-overflow-tooltip="true"
              />
              <el-table-column
                label="部门"
                align="center"
                prop="dept.deptName"
                :show-overflow-tooltip="true"
              />
              <el-table-column label="手机号码" align="center" prop="phonenumber" width="120" />
              <el-table-column label="状态" align="center">
                <template slot-scope="scope">
                  <el-switch
                    v-model="scope.row.status"
                    active-value="0"
                    inactive-value="1"
                    @change="handleStatusChange(scope.row)"
                  ></el-switch>
                </template>
              </el-table-column>
              <el-table-column label="创建时间" align="center" prop="createTime" width="160">
                <template slot-scope="scope">
                  <span>{{ parseTime(scope.row.createTime) }}</span>
                </template>
              </el-table-column>
              <el-table-column
                label="操作"
                align="center"
                width="180"
                class-name="small-padding fixed-width"
              >
                <template slot-scope="scope">
                  <el-button
                    size="mini"
                    type="text"
                    icon="el-icon-edit"
                    @click="handleUpdate(scope.row)"
                    v-hasPermi="['system:user:edit']"
                    style="color:#5ECC59 !important;font-size:16px"
                    title="编辑"
                  ></el-button>
                  <el-button
                    v-if="scope.row.userId !== 1"
                    size="mini"
                    type="text"
                    icon="el-icon-delete"
                    @click="handleDelete(scope.row)"
                    v-hasPermi="['system:user:remove']"
                    style="color: #E8522A !important;font-size:16px"
                    title="删除"
                  ></el-button>
                  <el-button
                    size="mini"
                    type="text"
                    icon="el-icon-key"
                    @click="handleResetPwd(scope.row)"
                    v-hasPermi="['system:user:resetPwd']"
                    style="font-size:16px"
                    title="重置密码"
                  ></el-button>
                </template>
              </el-table-column>
            </el-table>
          </div>
          <pagination
            v-show="total>10"
            :total="total"
            :page.sync="queryParams.pageNum"
            :limit.sync="queryParams.pageSize"
            @pagination="getList"
          />
        </div>
      </el-col>
    </el-row>

    <!-- 添加或修改参数配置对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="40%" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="120px">
        <el-row>
          <el-col :span="12">
            <el-form-item label="用户昵称：" prop="nickName">
              <el-input v-model="form.nickName" placeholder="请输入用户昵称" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="用户工号：" prop="userCode">
              <el-input v-model="form.userCode" placeholder="系统自动分配" :disabled="true"/>
            </el-form-item>
          </el-col>

        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="归属部门：" prop="deptId">
              <treeselect
                v-model="form.deptId"
                :options="deptOptions"
                :show-count="true"
                placeholder="请选择归属部门"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="手机号码：" prop="phonenumber">
              <el-input v-model="form.phonenumber" placeholder="请输入手机号码" maxlength="11" />
            </el-form-item>
          </el-col>

        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="邮箱：" prop="email">
              <el-input v-model="form.email" placeholder="请输入邮箱" maxlength="50" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item v-if="form.userId == undefined" label="用户名称：" prop="userName">
              <el-input v-model="form.userName" placeholder="请输入用户名称" />
            </el-form-item>
          </el-col>

        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item v-if="form.userId == undefined" label="用户密码：" prop="password">
              <el-input v-model="form.password" placeholder="请输入用户密码" type="password" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="用户性别：">
              <el-select v-model="form.sex" placeholder="请选择" style="width:100%">
                <el-option
                  v-for="dict in sexOptions"
                  :key="dict.dictValue"
                  :label="dict.dictLabel"
                  :value="dict.dictValue"
                ></el-option>
              </el-select>
            </el-form-item>
          </el-col>

        </el-row>
        <el-row>

          <el-col :span="12">
            <el-form-item label="岗位：">
              <el-select v-model="postIds"  placeholder="请选择" style="width:100%"  @change="postSel">
                <el-option
                  v-for="item in postOptions"
                  :key="item.postId"
                  :label="item.postName"
                  :value="item.postId"
                  :disabled="item.status == 1"
                ></el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="角色：">
              <el-select v-model="form.roleIds" multiple placeholder="请选择" style="width:100%">
                <el-option
                  v-for="item in roleOptions"
                  :key="item.roleId"
                  :label="item.roleName"
                  :value="item.roleId"
                  :disabled="item.status == 1"
                ></el-option>
              </el-select>
            </el-form-item>
          </el-col>

        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="状态：">
              <el-radio-group v-model="form.status">
                <el-radio
                  v-for="dict in statusOptions"
                  :key="dict.dictValue"
                  :label="dict.dictValue"
                >{{dict.dictLabel}}</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>

          <el-col :span="24">
            <el-form-item label="备注：">
              <el-input v-model="form.remark" type="textarea" placeholder="请输入内容"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

    <!-- 用户导入对话框 -->
    <el-dialog :title="upload.title" :visible.sync="upload.open" width="400px" append-to-body>
      <el-upload
        ref="upload"
        :limit="1"
        accept=".xlsx, .xls"
        :headers="upload.headers"
        :action="upload.url + '?updateSupport=' + upload.updateSupport"
        :disabled="upload.isUploading"
        :on-progress="handleFileUploadProgress"
        :on-success="handleFileSuccess"
        :auto-upload="false"
        drag
      >
        <i class="el-icon-upload"></i>
        <div class="el-upload__text">
          将文件拖到此处，或
          <em>点击上传</em>
        </div>
        <div class="el-upload__tip" slot="tip">
          <el-checkbox v-model="upload.updateSupport" />是否更新已经存在的用户数据
          <el-link type="info" style="font-size:12px" @click="importTemplate">下载模板</el-link>
        </div>
        <div class="el-upload__tip" style="color:red" slot="tip">提示：仅允许导入“xls”或“xlsx”格式文件！</div>
      </el-upload>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitFileForm">确 定</el-button>
        <el-button @click="upload.open = false">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import chart from "../../user/chart/index";
import {
  listUser,
  getUser,
  delUser,
  addUser,
  updateUser,
  exportUser,
  resetUserPwd,
  changeUserStatus,
  importTemplate,
} from "@/api/system/user";
import { getToken } from "@/utils/auth";
import { treeselect } from "@/api/system/dept";
import Treeselect from "@riophae/vue-treeselect";
import "@riophae/vue-treeselect/dist/vue-treeselect.css";

export default {
  name: "User",
  components: { Treeselect, chart },
  data() {
    return {
      // 遮罩层
      loading: true,
      // 选中数组
      ids: [],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 总条数
      total: 0,
      // 用户表格数据
      userList: null,
      // 弹出层标题
      title: "",
      // 部门树选项
      deptOptions: undefined,
      // 是否显示弹出层
      open: false,
      // 部门名称
      deptName: undefined,
      // 默认密码
      initPassword: undefined,
      // 日期范围
      dateRange: [],
      // 状态数据字典
      statusOptions: [],
      // 性别状态字典
      sexOptions: [],
      // 岗位选项
      postOptions: [],
      // 角色选项
      roleOptions: [],
      // 表单参数
      form: {},
      defaultProps: {
        children: "children",
        label: "label",
      },
      // 用户导入参数
      upload: {
        // 是否显示弹出层（用户导入）
        open: false,
        // 弹出层标题（用户导入）
        title: "",
        // 是否禁用上传
        isUploading: false,
        // 是否更新已经存在的用户数据
        updateSupport: 0,
        // 设置上传的请求头部
        headers: { Authorization: "Bearer " + getToken() },
        // 上传的地址
        url: process.env.VUE_APP_BASE_API + "/system/user/importData",
      },
      postIds:undefined,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        userName: undefined,
        phonenumber: undefined,
        status: undefined,
        deptId: undefined,
        postId:undefined,
        beginTime:undefined,
        endTime:undefined

      },
      // 表单校验
      rules: {
        userName: [
          { required: true, message: "用户名称不能为空", trigger: "blur" },
        ],
        // userCode: [
        //   { required: true, message: "工号不能为空", trigger: "blur" },
        // ],
        nickName: [
          { required: true, message: "用户昵称不能为空", trigger: "blur" },
        ],
        deptId: [
          { required: true, message: "归属部门不能为空", trigger: "blur" },
        ],
        password: [
          { required: true, message: "用户密码不能为空", trigger: "blur" },
        ],
        email: [
          { required: true, message: "邮箱地址不能为空", trigger: "blur" },
          {
            type: "email",
            message: "'请输入正确的邮箱地址",
            trigger: ["blur", "change"],
          },
        ],
        phonenumber: [
          { required: true, message: "手机号码不能为空", trigger: "blur" },
          {
            pattern: /^1[3|4|5|6|7|8|9][0-9]\d{8}$/,
            message: "请输入正确的手机号码",
            trigger: "blur",
          },
        ],
      },
    };
  },
  watch: {

    // 根据名称筛选部门树
    deptName(val) {
      this.$refs.tree.filter(val);
    },
  },
  created() {
    this.getList();
    this.getTreeselect();
    this.getDicts("sys_normal_disable").then((response) => {
      this.statusOptions = response.data;
    });
    this.getDicts("sys_user_sex").then((response) => {
      this.sexOptions = response.data;
    });
    this.getConfigKey("sys.user.initPassword").then((response) => {
      this.initPassword = response.msg;
    });
     getUser().then((response) => {
        this.postOptions = response.posts;
      });
  },
  methods: {
     postSel(id){
      this.form.postIds=[]
      this.form.postIds.push(id)


    },
    /** 查询用户列表 */
    getList() {
      this.loading = true;
      listUser(this.queryParams).then(
        (response) => {
          // console.log(response, )
          this.userList = response.rows;
          this.total = response.total;
          this.loading = false;
        }
      );
    },
    /** 查询部门下拉树结构 */
    getTreeselect() {
      treeselect().then((response) => {
        this.deptOptions = response.data;
      });
    },
    // 筛选节点
    filterNode(value, data) {
      if (!value) return true;
      return data.label.indexOf(value) !== -1;
    },
    // 节点单击事件
    handleNodeClick(data) {
      this.queryParams.deptId = data.id;
      this.getList();
    },
    // 用户状态修改
    handleStatusChange(row) {
      let text = row.status === "0" ? "启用" : "停用";
      this.$confirm(
        '确认要"' + text + '""' + row.userName + '"用户吗?',
        "警告",
        {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning",
        }
      )
        .then(function () {
          return changeUserStatus(row.userId, row.status);
        })
        .then(() => {
          this.msgSuccess(text + "成功");
        })
        .catch(function () {
          row.status = row.status === "0" ? "1" : "0";
        });
    },
    // 取消按钮
    cancel() {
      this.open = false;
      this.reset();
    },
    // 表单重置
    reset() {
      this.form = {
        userId: undefined,
        deptId: undefined,
        userName: undefined,
        userCode:"",
        nickName: undefined,
        password: undefined,
        phonenumber: undefined,
        email: undefined,
        sex: undefined,
        status: "0",
        remark: undefined,
        postIds: [],
        roleIds: [],
      };
      this.resetForm("form");
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.queryParams.beginTime = this.dateRange[0]
      this.queryParams.endTime = this.dateRange[1]

      this.getList();
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.dateRange = [];
      this.queryParams= {
        pageNum: 1,
        pageSize: 20,
        userName: undefined,
        phonenumber: undefined,
        status: undefined,
        deptId: undefined,
        postId:undefined,
        beginTime:undefined,
        endTime:undefined

      },
      this.resetForm("queryForm");
      this.handleQuery();
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map((item) => item.userId);
      this.single = selection.length != 1;
      this.multiple = !selection.length;
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.getTreeselect();
      getUser().then((response) => {
        this.postOptions = response.posts;
        this.roleOptions = response.roles;
        this.open = true;
        this.title = "添加用户";
        this.form.password = this.initPassword;
      });
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      this.getTreeselect();
      const userId = row.userId || this.ids;
      getUser(userId).then((response) => {
        this.postIds=response.postIds[0];
        this.form = response.data;
        this.postOptions = response.posts;
        this.roleOptions = response.roles;
        this.form.postIds = response.postIds;
        this.form.roleIds = response.roleIds;
        this.open = true;
        this.title = "修改用户";
        this.form.password = "";
      });
    },
    /** 重置密码按钮操作 */
    handleResetPwd(row) {
      this.$prompt('请输入"' + row.userName + '"的新密码', "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
      })
        .then(({ value }) => {
          resetUserPwd(row.userId, value).then((response) => {
            if (response.code === 200) {
              this.msgSuccess("修改成功，新密码是：" + value);
            }
          });
        })
        .catch(() => {});
    },
    /** 提交按钮 */
    submitForm: function () {
      this.$refs["form"].validate((valid) => {
        if (valid) {
          if (this.form.userId != undefined) {
            updateUser(this.form).then((response) => {
              if (response.code === 200) {
                this.msgSuccess("修改成功");
                this.open = false;
                this.getList();
              }
            });
          } else {
            addUser(this.form).then((response) => {
              if (response.code === 200) {
                this.msgSuccess("新增成功");
                this.open = false;
                this.getList();
              }
            });
          }
        }
      });
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const userIds = row.userId || this.ids;
      this.$confirm(
        '是否确认删除用户编号为"' + userIds + '"的数据项?',
        "警告",
        {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning",
        }
      )
        .then(function () {
          return delUser(userIds);
        })
        .then(() => {
          this.getList();
          this.msgSuccess("删除成功");
        })
        .catch(function () {});
    },
    /** 导出按钮操作 */
    handleExport() {
      const queryParams = this.queryParams;
      this.$confirm("是否确认导出所有用户数据项?", "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      })
        .then(function () {
          return exportUser(queryParams);
        })
        .then((response) => {
          this.download(response.msg);
        })
        .catch(function () {});
    },
    /** 导入按钮操作 */
    handleImport() {
      this.upload.title = "用户导入";
      this.upload.open = true;
    },
    /** 下载模板操作 */
    importTemplate() {
      importTemplate().then((response) => {
        this.download(response.msg);
      });
    },
    // 文件上传中处理
    handleFileUploadProgress(event, file, fileList) {
      console.log(file)
      this.upload.isUploading = true;
    },
    // 文件上传成功处理
    handleFileSuccess(response, file, fileList) {
      console.log(file)
      this.upload.open = false;
      this.upload.isUploading = false;
      this.$refs.upload.clearFiles();
      this.$alert(response.msg, "导入结果", { dangerouslyUseHTMLString: true });
      this.getList();
    },
    // 提交上传文件
    submitFileForm() {
      this.$refs.upload.submit();
    },
  },
};
</script>
<style>
.el-button--primary {
  background-color: #4860fb !important;
  border-color: #4860fb !important;
}
</style>
