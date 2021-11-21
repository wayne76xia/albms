<template>
  <div class="aui-padded-15">
    <div class="flex flex_start aui-padded-b-10">
      <p class="font-size-20 fw_bold">公告查看记录</p>
    </div>
    <div class="table_data">
      <div class="bg-white bg_shadow bg-radius">
        <div class="aui-padded-15 aui-padded-t-10 aui-padded-b-0">
          <el-form
            :model="queryParams"
            ref="queryForm"
            :inline="true"
            label-width="68px"
          >
            <el-row>
              <el-col :span="20">
                <el-form-item label="公告名称" prop="noticeName">
                  <el-input
                    v-model="queryParams.noticeName"
                    placeholder="请输入公告名称"
                    clearable
                    size="small"
                    @keyup.enter.native="handleQuery"
                  />
                </el-form-item>
                <el-form-item label="手机号" prop="userPhone">
                  <el-input
                    v-model="queryParams.userPhone"
                    placeholder="请输入手机号"
                    clearable
                    size="small"
                    @keyup.enter.native="handleQuery"
                  />
                </el-form-item>
                <el-form-item label="创建时间" prop="createDate">
                  <el-date-picker
                    clearable
                    size="small"
                    style="width: 200px"
                    v-model="queryParams.createDate"
                    type="date"
                    value-format="yyyy-MM-dd"
                    placeholder="选择创建时间"
                  >
                  </el-date-picker>
                </el-form-item>
             
                <el-form-item class="aui-padded-l-15">
                  <el-button
                    type="primary"
                    size="mini"
                    @click="handleQuery"
                    class="find_btn"
                    >查询</el-button
                  >
                  <el-button size="mini" @click="resetQuery" class="reset_btn"
                    >重置</el-button
                  >
                </el-form-item>
              </el-col>

              <el-col :span="4">
                <div class>
                  <div class="flex export_box" style="float: right">
                    <div class="text-black font-size-14 aui-padded-r-10">
                      共{{ total }}条数据
                    </div>
                  </div>
                </div>
              </el-col>
            </el-row>
          </el-form>
        </div>
        <el-table
          v-loading="loading"
          :data="noticeRecordList"
          @selection-change="handleSelectionChange"
          stripe 
        >
          <!-- <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="ID" align="center" prop="id" />
      <el-table-column label="用户id" align="center" prop="userId" /> -->
          <el-table-column label="公告标题" align="center" prop="noticeName" />
          <el-table-column label="创建者" align="center" prop="userName" />
          <el-table-column label="手机号" align="center" prop="userPhone" />
          <el-table-column label="创建时间" align="center" prop="createDate">
            <template slot-scope="scope">
              <span>{{ scope.row.createDate }}</span>
            </template>
          </el-table-column>
        </el-table>
      </div>
      <pagination
        v-show="total > 0"
        :total="total"
        :page.sync="queryParams.pageNum"
        :limit.sync="queryParams.pageSize"
        @pagination="getList"
      />
    </div>
  </div>
</template>

<script>
import {
  listNoticeRecord,
} from "@/api/system/noticeRecord";

export default {
  name: "NoticeRecord",
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
      // 公告查看记录表格数据
      noticeRecordList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        userId: undefined,
        sysNoticeId: undefined,
        createDate: undefined,
        userPhone: undefined,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        userId: [
          { required: true, message: "用户id不能为空", trigger: "blur" },
        ],
        sysNoticeId: [
          { required: true, message: "公告id不能为空", trigger: "blur" },
        ],
        createDate: [
          { required: true, message: "创建时间不能为空", trigger: "blur" },
        ],
      },
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询公告查看记录列表 */
    getList() {
      this.loading = true;
      listNoticeRecord(this.queryParams).then((response) => {
        console.log(response.rows);
        this.noticeRecordList = response.rows;
        this.total = response.total;
        this.loading = false;
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
        id: undefined,
        userId: undefined,
        sysNoticeId: undefined,
        createDate: undefined,
      };
      this.resetForm("form");
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getList();
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.resetForm("queryForm");
      this.handleQuery();
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map((item) => item.id);
      this.single = selection.length != 1;
      this.multiple = !selection.length;
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加公告查看记录";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids;
      getNoticeRecord(id).then((response) => {
        this.form = response.data;
        this.open = true;
        this.title = "修改公告查看记录";
      });
    },
    /** 提交按钮 */
    submitForm: function () {
      this.$refs["form"].validate((valid) => {
        if (valid) {
          if (this.form.id != undefined) {
            updateNoticeRecord(this.form).then((response) => {
              if (response.code === 200) {
                this.msgSuccess("修改成功");
                this.open = false;
                this.getList();
              }
            });
          } else {
            addNoticeRecord(this.form).then((response) => {
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
      const ids = row.id || this.ids;
      this.$confirm(
        '是否确认删除公告查看记录编号为"' + ids + '"的数据项?',
        "警告",
        {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning",
        }
      )
        .then(function () {
          return delNoticeRecord(ids);
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
      this.$confirm("是否确认导出所有公告查看记录数据项?", "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      })
        .then(function () {
          return exportNoticeRecord(queryParams);
        })
        .then((response) => {
          this.download(response.msg);
        })
        .catch(function () {});
    },
  },
};
</script>