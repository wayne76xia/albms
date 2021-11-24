<template>
  <div>
    <div class="holiday">
      <!-- 底部表格 -->
      <div class="aui-padded-15 aui-padded-t-0 table_data">

<!--        新增按钮-->
        <div class="flex flex_start aui-padded-b-10 aui-margin-t-10">
          <p class="font-size-20">审批设置</p>
          <div class="aui-padded-l-10 flex">
            <div
              class="addnew bg-theme text-white border-radius font-size-14"
              @click="handleAdd"
              v-hasPermi="['vacation:holidayApproval:add']"
            >新增</div>
          </div>
        </div>

<!--        搜索框 + 列表-->
        <div class="bg-white bg_shadow bg-radius">
          <div class="aui-padded-15 aui-padded-t-10 aui-padded-b-0">
            <el-form :model="queryParams" ref="queryForm" :inline="true" label-width="100px">
              <el-row>
                <el-col :span="20">
                  <el-form-item label="假期类型：" prop="holidayTypeId">
                    <el-select
                      v-model="queryParams.holidayTypeId"
                      placeholder="请选择"
                      clearable
                      filterable
                      size="small"
                      style="width: 200px"
                    >
                      <el-option
                        v-for="dict in holidayTypeList"
                        :key="dict.dictCode"
                        :label="dict.dictLabel"
                        :value="dict.dictCode"
                      />
                    </el-select>
                  </el-form-item>
                </el-col>
                <el-col :span="4">
                  <div class>
                    <div class="flex export_box" style="float:right">
                      <div class="text-black font-size-14 aui-padded-r-10">共{{total}}条数据</div>
                    </div>
                    <el-form-item style="margin-top:5px">
                      <el-button type="primary" size="mini" @click="handleQuery" class="find_btn">查询</el-button>
                      <el-button size="mini" @click="resetQuery" class="reset_btn">重置</el-button>
                    </el-form-item>
                  </div>
                </el-col>
              </el-row>
            </el-form>
          </div>
          <el-table v-loading="loading" :data="holidayApprovalList"   @selection-change="handleSelectionChange" stripe >
            <el-table-column type="selection" width="80" align="center" />
            <el-table-column label="当前审批序号" prop="currentApprovedIndex" align="center" />
            <el-table-column label="待审批人角色" prop="roleName" align="center" />
            <el-table-column label="审批人角色" prop="approvedRoleName" align="center"/>
            <el-table-column label="是否为最后一级" prop="nextApprovalId" align="center" >
              <template slot-scope="scope">
                <span v-if="scope.row.nextApprovalId == 0">是</span>
                <span v-if="scope.row.nextApprovalId != 0">否</span>
              </template>
            </el-table-column>
            <el-table-column label="操作" align="center" width="200">
              <template slot-scope="scope">
                <el-button
                  size="mini"
                  type="text"
                  icon="el-icon-delete"
                  @click="handleDelete(scope.row)"
                  style="color: #E8522A !important;font-size:16px"
                  v-hasPermi="['vacation:holidayApproval:remove']"
                  title="删除"
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
          @pagination="getHolidayApprovalList"
        />

        <!-- 添加或修改窗 -->
        <el-dialog class :title="title" :visible.sync="open" width="40%" append-to-body>
          <el-form ref="form" :model="form" :rules="rules" label-width="100px">
            <el-row>
              <el-col :span="12">
                <el-form-item label="假期类型：">
                  <el-select
                    v-model="form.holidayTypeId"
                    placeholder="请选择"
                    clearable
                    style="width:100%"
                  >
                    <el-option
                      v-for="dict in holidayTypeList"
                      :key="dict.dictCode"
                      :label="dict.dictLabel"
                      :value="dict.dictCode"
                    ></el-option>
                  </el-select>
                </el-form-item>
              </el-col>
              <el-col :span="24">
                <el-form-item label="待审批人角色：">
                  <el-select
                    v-model="form.roleId"
                    clearable
                    filterable
                    placeholder="请输入姓名"
                    :filter-method="brandKeyChange"
                    style="width:100%"
                    @change="fnEdit"
                  >
                    <el-option
                      v-for="role in roleList"
                      :key="role.roleId"
                      :label="role.roleName"
                      :value="role.roleId"
                    ></el-option>
                  </el-select>
                </el-form-item>
              </el-col>
              <el-col :span="24">
                <el-form-item label="审批人：">
                  <el-select
                    v-model="form.approvedRoleId"
                    clearable
                    filterable
                    placeholder="请输入姓名"
                    :filter-method="brandKeyChange"
                    style="width:100%"
                    @change="fnEdit"
                  >
                    <el-option
                      v-for="role in roleList"
                      :key="role.roleId"
                      :label="role.roleName"
                      :value="role.roleId"
                    ></el-option>
                  </el-select>
                </el-form-item>
              </el-col>
            </el-row>
          </el-form>
          <div slot="footer" class="dialog-footer">
            <el-button type="primary" @click="submitForm">确 定</el-button>
            <el-button @click="cancel">取 消</el-button>
          </div>
        </el-dialog>

      </div>
    </div>
  </div>
</template>

<script>
import {
  getHolidayApprovalInfo,
  holidayApprovalAdd,
  holidayApprovalList,
  holidayApprovalRemove,
  holidayApprovalUpdate,
  roleList
} from '../../../api/vacation/approval'
import store from '../../../store'

export default {
  name: "approvalSetting",
  components: {
  },
  data() {
    return {
      dialogVisible: false,
      // 假期列表
      title: "",
      open: false,
      loading: false,
      total: 0,
      dateRange: "",
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        status: 1,
        holidayTypeId: undefined,
      },
      // 表单参数
      form: {
      },
      // 假期类型
      holidayTypeList: [],
      ageList: [],
      holidayApprovalList: [],
      roleList: [],
      rules: {
        holidayInstruction: [
          { required: true, message: "请假说明不能为空", trigger: "blur" },
        ],
      },
      // 客户详情
      textarea: "",
      holidayInfo: {
      },
      infoHoliday: "",
      recordForm: {
        holidayId: undefined,
        type: 1,
        content: "",
      },
    };
  },
  created() {
    this.getHolidayApprovalList();
    this.getRoleList();
    this.getDicts("holiday_type").then((response) => {
      this.holidayTypeList = response.data;
    });
  },
  methods: {
    // 多选框选中数据
    handleSelectionChange(e) {
      this.ids = selection.map((item) => item.roleId);
      this.single = selection.length != 1;
      this.multiple = !selection.length;
    },
    // 审批人选择
    fnEdit(vId) {
      this.form.parentHolidayId = vId;
    },
    // 查看假期详细信息
    handleDataScope(row) {
      this.infoHoliday = row;
      this.infoCheck();
    },
    infoCheck() {
      const id = this.infoHoliday.holidayId;
      getHolidayApprovalInfo(id).then((response) => {
        if (response.code == 200) {
          this.holidayInfo = response.data;
          this.dialogVisible = true;
        }
      });
    },
    // 新增确定按钮
    submitForm() {
      this.$refs["form"].validate((valid) => {
        if (valid) {
          if (this.form.holidayId != undefined) {
            holidayApprovalUpdate(this.form).then((response) => {
              if (response.code == 200) {
                this.msgSuccess("修改成功");
                this.open = false;
                this.getHolidayApprovalList();
              }
            });
          } else {
            if (this.form.roleId !== this.form.approvedRoleId) {
              holidayApprovalAdd(this.form).then((response) => {
                if (response.code == 200) {
                  this.msgSuccess("新增成功");
                  this.open = false;
                  this.getHolidayApprovalList();
                }
              });
            } else {
              this.msgError("被审批角色和审批角色不能相同！");
            }

          }
        }
      });
    },
    // 新增取消按钮
    cancel() {
      this.open = false;
      this.reset();
    },
    handleDelete(row) {
      const ids = row.holidayApprovalId;
      this.$confirm('是否确认删除此项?', "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      })
        .then(function () {
          return holidayApprovalRemove(ids);
        })
        .then((res) => {
          console.log(res);
          this.getHolidayApprovalList();
          this.msgSuccess("删除成功");
        })
        .catch(function () {});
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset()
      this.open = true
      this.title = '添加假期审批设置'
    },
    /** 修改按钮操作 */
    // handleUpdate(row) {
    //   this.reset();
    //   const id = row.holidayId;
    //   getHolidayApprovalInfo(id).then((response) => {
    //     let data = {
    //         holidayTypeId: response.data.holidayTypeId,
    //         currentApprovedIndex: response.data.currentApprovedIndex,
    //     }
    //     userListForVacation(data).then((response2) => {
    //       if (response2.code == 200) {
    //         let expect = {
    //           holidayId: response.data.holidayId,
    //           holidayTypeId: response.data.holidayTypeId,
    //           // 可能缺参数
    //         }
    //         this.form = expect
    //         this.userList = response2.data;
    //         this.title = "修改假期信息";
    //         this.open = true;
    //       }
    //     })
    //   });
    // },
    brandKeyChange(inputKey){

    },
    // 表单重置
    reset() {
      this.addressForm = [];
      this.form = {
        status: 1,
        holidayName: undefined,
        holidaySex: undefined,
        holidayPhone: undefined,
        holidayAge: undefined,
        passportNumber: undefined,
        province: undefined,
        city: undefined,
        district: undefined,
        address: undefined,
        shopId: undefined,
        remark: undefined,
        parentHolidayId: undefined,

        // 新增
        sourceType:undefined,
        sourceTypeName:undefined
      };
      this.resetForm("form");
    },
    // 查询假期列表
    getHolidayApprovalList() {
      this.loading = true;
      holidayApprovalList(this.queryParams).then((response) => {
        if (response.code == 200) {
          this.holidayApprovalList = response.rows;
          this.total = response.total;
          this.loading = false;
        }
      });
    },
    // 查询角色列表
    getRoleList() {
      roleList().then((response) => {
        if (response.code == 200) {
          this.roleList = response.data;
        }
      });
    },
    // 重置表单
    resetQuery() {
      this.dateRange = "";
      this.queryParams = {
        pageNum: 1,
        pageSize: 20,
        status: 1,
        holidayName: undefined,
        holidayPhone: undefined,
        sourceType: undefined,
        userId: undefined,
        holidayLevel: undefined,
        minConsumptionNum: undefined,
        maxConsumptionNum: undefined,
        shopId: undefined,
        province: undefined,
        city: undefined,
        district: undefined,
        address: undefined,
        beginTime: undefined,
        endTime: undefined,
      };
      this.getHolidayApprovalList();
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.queryParams.beginTime = this.dateRange[0]; //开始时间
      this.queryParams.endTime = this.dateRange[1]; //结束时间
      this.getHolidayApprovalList();
    },
    // 关闭页面
    handleClose(done) {
      done();
    },
  },
};
</script>

<style  >
.holiday .top_four {
  height: 250px;
}
.holiday .top_four_item {
  width: 24%;
  height: 100%;
  border-radius: 10px;
  position: relative;
}
.holiday .top_four_item_text {
  position: absolute;
  left: 0;
  top: 0;
}

.el-dialog__header {
  border-bottom: 1px solid #eeeeee;
}
.el-dialog__body {
  padding: 20px;
  color: #222;
}
.el-dialog:not(.is-fullscreen) {
  margin-top: 0 !important;
}

.holiday .line_dash {
  border-top: 1px dashed #eeeeee;
  width: 88%;
}
</style>

