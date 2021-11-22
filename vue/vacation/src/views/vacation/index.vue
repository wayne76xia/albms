<template>
  <div>
    <div class="holiday">
      <!-- 底部表格 -->
      <div class="aui-padded-15 aui-padded-t-0 table_data">

<!--        新增按钮-->
        <div class="flex flex_start aui-padded-b-10 aui-margin-t-10">
          <p class="font-size-20">假期管理</p>
          <div class="aui-padded-l-10 flex">
            <div
              class="addnew bg-theme text-white border-radius font-size-14"
              @click="handleAdd"
              v-hasPermi="['vacation:holiday:add']"
            >新增</div>
          </div>
        </div>

<!--        搜索框 + 列表-->
        <div class="bg-white bg_shadow bg-radius">
          <div class="aui-padded-15 aui-padded-t-10 aui-padded-b-0">
            <el-form :model="queryParams" ref="queryForm" :inline="true" label-width="100px">
              <el-row>
                <el-col :span="20">
<!--                  <el-form-item label="申请人：" prop="proposerName"-->
<!--                                v-hasPermi="['vacation:holiday:query']">-->
<!--                    <el-input-->
<!--                      v-model="queryParams.proposerName"-->
<!--                      placeholder="请输入申请人"-->
<!--                      clearable-->
<!--                      size="small"-->
<!--                      style="width: 200px"-->
<!--                      @keyup.enter.native="handleQuery"-->
<!--                    />-->
<!--                  </el-form-item>-->
<!--                  <el-form-item label="当前审批人：" prop="currentApproverName"-->
<!--                                v-hasPermi="['vacation:holiday:query']">-->
<!--                    <el-input-->
<!--                      v-model="queryParams.currentApproverName"-->
<!--                      placeholder="请输入当前审批人"-->
<!--                      clearable-->
<!--                      size="small"-->
<!--                      style="width: 200px"-->
<!--                      @keyup.enter.native="handleQuery"-->
<!--                    />-->
<!--                  </el-form-item>-->
                  <el-form-item label="假期类型：" prop="holidayType">
                    <el-select
                      v-model="queryParams.holidayType"
                      placeholder="请选择"
                      clearable
                      filterable
                      size="small"
                      style="width: 200px"
                    >
                      <el-option
                        v-for="item in holidayTypeList"
                        :key="item.type"
                        :label="item.name"
                        :value="item.type"
                      />
                    </el-select>
                  </el-form-item>
                  <el-form-item label="时间：">
                    <el-date-picker
                      v-model="dateRange"
                      style="width: 240px"
                      value-format="yyyy-MM-dd"
                      type="daterange"
                      range-separator="-"
                      start-placeholder="开始日期"
                      end-placeholder="结束日期"
                    ></el-date-picker>
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
          <el-table v-loading="loading" :data="holidayList"   @selection-change="handleSelectionChange" stripe >
            <el-table-column type="selection" width="80" align="center" />
            <el-table-column label="申请人" prop="proposerName" align="center" />
            <el-table-column label="当前审批人" prop="currentApproverName" align="center"/>
            <el-table-column label="假期类型" prop="holidayTypeName" align="center" />
            <el-table-column label="假期开始时间" prop="holidayStartDate" align="center" />
            <el-table-column label="假期结束时间" prop="holidayEndDate" align="center" />
            <el-table-column label="假期时长" prop="holidayDuration" align="center" />
            <el-table-column label="当前审批状态" align="center">
              <template slot-scope="scope">
                <span v-if="scope.row.status === 0">审批中</span>
                <span v-if="scope.row.status === 1">通过</span>
                <span v-if="scope.row.status === 2">驳回</span>
              </template>
            </el-table-column>
            <el-table-column label="操作" align="center" width="200">
              <template slot-scope="scope">
                <el-button
                  size="mini"
                  type="text"
                  @click="handleDataScope(scope.row)"
                  style="font-size:16px"
                  v-hasPermi="['vacation:holiday:detail']"
                  title="查看详情"
                >
                  <img src="../../assets/image/chaxun.png" alt />
                </el-button>
                <el-button
                  size="mini"
                  type="text"
                  icon="el-icon-edit"
                  @click="handleUpdate(scope.row)"
                  v-hasPermi="['vacation:holiday:edit']"
                  style="color:#5ECC59 !important;font-size:16px"
                  title="编辑"
                ></el-button>
                <el-button
                  size="mini"
                  type="text"
                  icon="el-icon-delete"
                  @click="handleDelete(scope.row)"
                  style="color: #E8522A !important;font-size:16px"
                  v-hasPermi="['vacation:holiday:remove']"
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
          @pagination="getHolidayList"
        />

        <!-- 添加或修改窗 -->
        <el-dialog class :title="title" :visible.sync="open" width="40%" append-to-body>
          <el-form ref="form" :model="form" :rules="rules" label-width="100px">
            <div class="text-grayer aui-padded-t-15 aui-padded-b-15 flex">
              <div class="font-size-12">基本资料</div>
              <div class="line_dash"></div>
            </div>
            <el-row>
              <el-col :span="12">
                <el-form-item label="申请人：" prop="proposerName">
                  <el-input v-model="form.proposerName"/>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="假期类型：">
                  <el-select
                    v-model="form.holidayType"
                    placeholder="请选择"
                    clearable
                    style="width:100%"
                  >
                    <el-option
                      v-for="item in holidayTypeList"
                      :key="item.type"
                      :label="item.name"
                      :value="item.type"
                    ></el-option>
                  </el-select>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="时间：">
                  <el-date-picker
                    v-model="dateRange"
                    style="width: 240px"
                    value-format="yyyy-MM-dd"
                    type="daterange"
                    range-separator="-"
                    start-placeholder="开始日期"
                    end-placeholder="结束日期"
                  ></el-date-picker>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="下一级审批人：">
                  <el-select
                    v-model="form.currentApproverId"
                    clearable
                    filterable
                    placeholder="请输入姓名"
                    style="width:100%"
                    :filter-method="brandKeyChange"
                    @change="fnEdit"
                  >
                    <el-option
                      v-for="dict in userList"
                      :key="dict.userId"
                      :label="dict.nickName"
                      :value="dict.userId"
                    ></el-option>
                  </el-select>
                </el-form-item>
              </el-col>
            </el-row>
            <el-form-item label="请假说明：">
              <el-input type="textarea" v-model="form.holidayInstruction"></el-input>
            </el-form-item>
          </el-form>
          <div slot="footer" class="dialog-footer">
            <el-button type="primary" @click="submitForm">确 定</el-button>
            <el-button @click="cancel">取 消</el-button>
          </div>
        </el-dialog>

        <!-- 信息窗 -->
        <el-dialog
          title="假期信息"
          :visible.sync="dialogVisible"
          width="40%"
          :before-close="handleClose"
        >
          <div>
            <div class="text-grayer flex">
              <div>假期详情</div>
              <div class="line_dash"></div>
            </div>
            <div class="aui-padded-15 font-size-14">
              <el-row>
                <el-col :span="12" class="aui-padded-b-10">
                  <span>申请人：</span>
                  <span class="aui-padded-l-5 text-graybc">{{holidayDetail.proposerName}}</span>
                </el-col>
                <el-col :span="12" class="aui-padded-b-10">
                  <span>假期类型：</span>
                  <span class="aui-padded-l-5 text-graybc">{{holidayDetail.holidayTypeName}}</span>
                </el-col>
                <el-col :span="12" class="aui-padded-b-10">
                  <span>假期时长：</span>
                  <span class="aui-padded-l-5 text-graybc">{{holidayDetail.holidayDuration}}</span>
                </el-col>
                <el-col :span="12" class="aui-padded-b-10">
                  <span>假期开始时间：</span>
                  <span class="aui-padded-l-5 text-graybc">{{holidayDetail.holidayStartDate}}</span>
                </el-col>
                <el-col :span="12" class="aui-padded-b-10">
                  <span>假期结束时间：</span>
                  <span class="aui-padded-l-5 text-graybc">{{holidayDetail.holidayEndDate}}</span>
                </el-col>
                <el-col :span="12" class="aui-padded-b-10">
                  <span>当前审批状态：</span>
                  <span class="aui-padded-l-5 text-graybc">{{holidayDetail.status === 0 ? '审批中' : holidayDetail.status === 1 ? '已通过' : '已驳回'}}</span>
                </el-col>
                <el-col :span="12" class="aui-padded-b-10">
                  <span>客户备注：</span>
                  <span class="aui-padded-l-5 text-graybc">{{holidayDetail.holidayRemark}}</span>
                </el-col>
              </el-row>
            </div>
            <div class="text-grayer flex">
              <div>审批详情</div>
              <div class="line_dash"></div>
            </div>
            <div class="aui-padded-15 font-size-14 aui-padded-b-0">
              <el-table :data="holidayDetail.holidayList" stripe >
                <el-table-column label="审批人" prop="proposerName" align="center" />
                <el-table-column label="审批状态" align="center">
                  <template slot-scope="scope">
                    <span v-if="scope.row.status === 0">审批中</span>
                    <span v-if="scope.row.status === 1">通过</span>
                    <span v-if="scope.row.status === 2">驳回</span>
                  </template>
                </el-table-column>
                <el-table-column label="审批时间" prop="holidayTypeName" align="center" />
                <el-table-column label="审批说明" prop="holidayTypeName" align="center" />
              </el-table>
            </div>
          </div>
        </el-dialog>

      </div>
    </div>
  </div>
</template>

<script>
import location from "@/components/address";
import age from "@/components/age";

export default {
  name: "holiday",
  components: {
  },
  data() {
    return {
      dialogVisible: false,

      addressList: [],

      // 会员列表
      title: "",
      open: false,
      loading: false,
      total: 0,
      dateRange: "",
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        status: 1,
        holidayName: undefined,
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
      },
      // 表单参数
      form: {},
      // 用户下拉列表
      userList: [],
      // 预约来源列表
      sourcesList: [],
      holidayTypeList: [
        {
          name: '调休',
          type: 1
        },
        {
          name: '事假',
          type: 2
        },
        {
          name: '年假',
          type: 3
        },
        {
          name: '婚假',
          type: 4
        },
        {
          name: '病假',
          type: 5
        },
        {
          name: '哺乳假',
          type: 6
        },
        {
          name: '产假',
          type: 7
        },
        {
          name: '陪产假',
          type: 8
        },
        {
          name: '丧假',
          type: 9
        },
        {
          name: '公假',
          type: 10
        },
      ],
      ageList: [],
      addressForm: [],
      holidayList: [],
      rules: {
        holidayName: [
          { required: true, message: "姓名不能为空", trigger: "blur" },
        ],
        holidaySex: [
          { required: true, message: "性别不能为空", trigger: "blur" },
        ],
        holidayPhone: [
          { required: true, message: "手机号不能为空", trigger: "blur" },
        ],
        passportNumber: [
          { required: true, message: "护照号不能为空", trigger: "blur" },
        ],
        sourceType: [
          { required: true, message: "请选择会员来源", trigger: "blur" },
        ],
        shopId: [{ required: true, message: "门店不能为空", trigger: "blur" }],
      },
      // 客户详情
      textarea: "",
      holidayDetail: {
        visitList: [],
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
    this.addressList = location; //地址列表
    this.ageList = age;
    this.getHolidayList();
    this.getuserList();
    this.getshopList();
    this.holidaylevelList();
    this.sourceList();
  },
  methods: {
    // 多选框选中数据
    handleSelectionChange(e) {
      this.ids = selection.map((item) => item.roleId);
      this.single = selection.length != 1;
      this.multiple = !selection.length;
    },
    // 根据手机号或护照号搜索客户
    brandKeyChange: function (inputKey) {
      //inputKey为当前输入的数据
      getClientByPhone(inputKey).then((response) => {
        if (response.code == 200) {
          this.clientList = response.data;
        }
      });
    },
    // 审批人选择
    fnEdit(vId) {
      this.form.parentHolidayId = vId;
    },
    // 查看客户详细信息
    handleDataScope(row) {
      this.infoHoliday = row;
      this.infoCheck();
    },
    infoCheck() {
      const id = this.infoHoliday.holidayId;
      holidayDetail(id).then((response) => {
        if (response.code == 200) {
          this.holidayDetail = response.data;
          this.dialogVisible = true;
        }
      });
    },
    // 新增确定按钮
    submitForm() {
      this.form.province = this.addressForm[0]; //省
      this.form.city = this.addressForm[1]; //市
      this.form.district = this.addressForm[2]; //区
      this.$refs["form"].validate((valid) => {
        if (valid) {
          // let regs = /^((13[0-9])|(17[0-1,6-8])|(15[^4,\\D])|(18[0-9]))\d{8}$/;
          let regs = /^1([358][0-9]|4[579]|66|7[0135678]|9[89])[0-9]{8}$/
          if (!regs.test(this.form.holidayPhone)) {
            this.msgError("手机号格式有误");
            return;
          }
          if (this.form.holidayId != undefined) {
            updateHoliday(this.form).then((response) => {
              if (response.code === 200) {
                this.msgSuccess("修改成功");
                this.open = false;
                this.getHolidayList();
              }
            });
          } else {
            addHoliday(this.form).then((response) => {
              if (response.code === 200) {
                this.msgSuccess("新增成功");
                this.open = false;
                this.getHolidayList();
              }
            });
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
      const name = row.holidayName;
      const ids = row.holidayId;
      this.$confirm('是否确认删除会员名为"' + name + '"的数据项?', "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      })
        .then(function () {
          return delHoliday(ids);
        })
        .then((res) => {
          console.log(res);
          this.getHolidayList();
          this.msgSuccess("删除成功");
        })
        .catch(function () {});
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加会员信息";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.holidayId;
      getHolidayDetail(id).then((response) => {
        this.addressForm.push(response.data.province);
        this.addressForm.push(response.data.city);
        this.addressForm.push(response.data.district);
        this.form = response.data;
        this.title = "修改会员信息";
        this.open = true;
      });
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
    // 查询会员列表
    getHolidayList() {
      this.loading = true;
      listHoliday(this.queryParams).then((response) => {
        console.log(response);
        if (response.code == 200) {
          this.holidayList = response.rows;
          this.total = response.total;
          this.loading = false;
        }
      });
    },
    //获取用户下拉列表
    getuserList() {
      listUser().then((response) => {
        if (response.code == 200) {
          this.userList = response.data;
        }
      });
    },
    //获取所有的门店列表
    getshopList() {
      listAllShop().then((response) => {
        if (response.code == 200) {
          this.allshopList = response.data;
        }
      });
    },
    /** 查询会员等级设置列表 */
    holidaylevelList() {
      listMemberLevel().then((response) => {
        if (response.code == 200) {
          this.holidaylevList = response.data;
        }
      });
    },
    /** 预约来源列表 */
    sourceList() {
      listParamsData().then((response) => {
        if (response.code == 200) {
          console.log(response.data)
          this.sourcesList = response.data;
        }
      });
    },

    // 重置表单
    resetQuery() {
      this.addressSel = "";
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
      this.getHolidayList();
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.queryParams.beginTime = this.dateRange[0]; //开始时间
      this.queryParams.endTime = this.dateRange[1]; //结束时间
      this.queryParams.province = this.addressSel[0]; //省
      this.queryParams.city = this.addressSel[1]; //市
      this.queryParams.district = this.addressSel[2]; //区
      if (
        this.queryParams.minConsumptionNum >= this.queryParams.maxConsumptionNum
      ) {
        this.msgError("最大消费次数应大于最小消费次数");
        return;
      }
      this.getHolidayList();
    },
    /** 导出按钮操作 */
    handleExport() {
      const queryParams = this.queryParams;
      this.$confirm("是否确认导出所选会员列表?", "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      })
        .then(function () {
          return exportHoliday(queryParams);
        })
        .then((res) => {
          this.download(res.msg);
        })
        .catch(function () {});
    },
    // 导入按钮操作
    handleImport() {
      this.upload.title = "用户导入";
      this.upload.open = true;
    },
    /** 下载模板操作 */
    importTemplate() {
      importHoliday().then((response) => {
        this.download(response.msg);
      });
    },

    // 提交上传文件
    submitFileForm() {
      this.$refs.upload.submit();
    },
    // 文件上传中处理
    handleFileUploadProgress(event, file, fileList) {
      this.upload.isUploading = true;
    },
    // 文件上传成功处理
    handleFileSuccess(response, file, fileList) {
      this.upload.open = false;
      this.upload.isUploading = false;
      this.$refs.upload.clearFiles();
      this.$alert(response.msg, "导入结果", { dangerouslyUseHTMLString: true });
      this.getHolidayList();
    },
    // 关闭页面
    handleClose(done) {
      done();
    },
    // 用户配镜快捷键
    handlePj(row) {
      const holidayPhone = row.holidayPhone;
      this.$router.push({
        path: "/sell/sellAdd",
        query: { holidayPhone: holidayPhone },
      });
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

