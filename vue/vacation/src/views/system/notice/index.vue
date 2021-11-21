<template>
  <div class="aui-padded-15">
    <div class="flex flex_start aui-padded-b-10">
      <p class="font-size-20 fw_bold">公告通知</p>
      <div class="aui-padded-l-15 flex" v-hasPermi="['system:notice:add']">
        <div class="flex">
          <div
            class="addnew bg-theme text-white border-radius font-size-14"
            @click="handleAdd"
          >
            新增
          </div>
        </div>
      </div>
    </div>
    <div class="table_data">
      <div class="bg-white bg_shadow bg-radius">
        <div class="aui-padded-15 aui-padded-t-10 aui-padded-b-0">
          <el-form
            :model="queryParams"
            ref="queryForm"
            :inline="true"
            label-width="100px"
          >
            <el-row>
              <el-col :span="20">
                <el-form-item label="公告标题" prop="noticeTitle">
                  <el-input
                    v-model="queryParams.noticeTitle"
                    placeholder="请输入公告标题"
                    clearable
                    size="small"
                    @keyup.enter.native="handleQuery"
                  />
                </el-form-item>
                <el-form-item label="操作人员" prop="createBy">
                  <el-input
                    v-model="queryParams.createBy"
                    placeholder="请输入操作人员"
                    clearable
                    size="small"
                    @keyup.enter.native="handleQuery"
                  />
                </el-form-item>
                <el-form-item label="类型" prop="noticeType">
                  <el-select
                    v-model="queryParams.noticeType"
                    placeholder="公告类型"
                    clearable
                    size="small"
                  >
                    <el-option
                      v-for="dict in typeOptions"
                      :key="dict.dictValue"
                      :label="dict.dictLabel"
                      :value="dict.dictValue"
                    />
                  </el-select>
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
          :data="noticeList"
          stripe
          @selection-change="handleSelectionChange"
        >
          <el-table-column
            label="序号"
            align="center"
            prop="noticeId"
            width="100"
          />
          <el-table-column
            label="公告标题"
            align="center"
            prop="noticeTitle"
            :show-overflow-tooltip="true"
          />
          <el-table-column
            label="公告类型"
            align="center"
            prop="noticeType"
            :formatter="typeFormat"
           
          />
          <el-table-column
            label="状态"
            align="center"
            prop="status"
            :formatter="statusFormat"
            
          />
          <el-table-column
            label="创建者"
            align="center"
            prop="createBy"
           
          />
          <el-table-column
            label="创建时间"
            align="center"
            prop="createTime"
            
          >
            <template slot-scope="scope">
              <span>{{ parseTime(scope.row.createTime, "{y}-{m}-{d}") }}</span>
            </template>
          </el-table-column>
          <el-table-column
            label="操作"
            align="center"
            class-name="small-padding fixed-width"
          >
            <template slot-scope="scope">
              <el-button
                size="mini"
                type="text"
                icon="el-icon-edit"
                @click="handleUpdate(scope.row)"
                v-hasPermi="['system:notice:edit']"
                style="color: #5ecc59 !important; font-size: 16px"
                title="编辑"
              ></el-button>
              <el-button
                size="mini"
                type="text"
                icon="el-icon-delete"
                @click="handleDelete(scope.row)"
                style="color: #e8522a !important; font-size: 16px"
                v-hasPermi="['system:notice:remove']"
                title="删除"
              ></el-button>
              <!-- <el-button
                size="mini"
                type="text"
                icon="el-icon-edit"
                @click="handleUpdate(scope.row)"
                v-hasPermi="['system:notice:edit']"
                >修改</el-button
              >
              <el-button
                size="mini"
                type="text"
                icon="el-icon-delete"
                @click="handleDelete(scope.row)"
                v-hasPermi="['system:notice:remove']"
                >删除</el-button
              > -->
            </template>
          </el-table-column>
        </el-table>
      </div>

      <pagination
        v-show="total > 20"
        :total="total"
        :page.sync="queryParams.pageNum"
        :limit.sync="queryParams.pageSize"
        @pagination="getList"
      />
    </div>

    <!-- 添加或修改公告对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="780px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-row>
          <el-col :span="12">
            <el-form-item label="公告标题" prop="noticeTitle">
              <el-input
                v-model="form.noticeTitle"
                placeholder="请输入公告标题"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="公告类型" prop="noticeType">
              <el-select v-model="form.noticeType" placeholder="请选择">
                <el-option
                  v-for="dict in typeOptions"
                  :key="dict.dictValue"
                  :label="dict.dictLabel"
                  :value="dict.dictValue"
                ></el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="状态">
              <el-radio-group v-model="form.status">
                <el-radio
                  v-for="dict in statusOptions"
                  :key="dict.dictValue"
                  :label="dict.dictValue"
                  >{{ dict.dictLabel }}</el-radio
                >
              </el-radio-group>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="内容">
              <Editor v-model="form.noticeContent" />
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <div slot="footer" class="dialog-footer" style="padding-top: 20px">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {
  listNotice,
  getNotice,
  delNotice,
  addNotice,
  updateNotice,
  exportNotice,
} from "@/api/system/notice";
import Editor from "@/components/Editor";

export default {
  name: "Notice",
  components: {
    Editor,
  },
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
      // 公告表格数据
      noticeList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 类型数据字典
      statusOptions: [],
      // 状态数据字典
      typeOptions: [],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 20,
        noticeTitle: undefined,
        createBy: undefined,
        status: undefined,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        noticeTitle: [
          { required: true, message: "公告标题不能为空", trigger: "blur" },
        ],
        noticeType: [
          { required: true, message: "公告类型不能为空", trigger: "blur" },
        ],
      },
    };
  },
  created() {
    this.getList();
    this.getDicts("sys_notice_status").then((response) => {
      this.statusOptions = response.data;
    });
    this.getDicts("sys_notice_type").then((response) => {
      this.typeOptions = response.data;
    });
  },
  methods: {
    /** 查询公告列表 */
    getList() {
      this.loading = true;
      listNotice(this.queryParams).then((response) => {
        this.noticeList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    // 公告状态字典翻译
    statusFormat(row, column) {
      return this.selectDictLabel(this.statusOptions, row.status);
    },
    // 公告状态字典翻译
    typeFormat(row, column) {
      return this.selectDictLabel(this.typeOptions, row.noticeType);
    },
    // 取消按钮
    cancel() {
      this.open = false;
      this.reset();
    },
    // 表单重置
    reset() {
      this.form = {
        noticeId: undefined,
        noticeTitle: undefined,
        noticeType: undefined,
        noticeContent: undefined,
        status: "0",
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
      this.ids = selection.map((item) => item.noticeId);
      this.single = selection.length != 1;
      this.multiple = !selection.length;
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加公告";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const noticeId = row.noticeId || this.ids;
      getNotice(noticeId).then((response) => {
        this.form = response.data;
        this.open = true;
        this.title = "修改公告";
      });
    },
    /** 提交按钮 */
    submitForm: function () {
      this.$refs["form"].validate((valid) => {
        if (valid) {
          if (this.form.noticeId != undefined) {
            updateNotice(this.form).then((response) => {
              if (response.code === 200) {
                this.msgSuccess("修改成功");
                this.open = false;
                this.getList();
              }
            });
          } else {
            addNotice(this.form).then((response) => {
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
      const noticeIds = row.noticeId || this.ids;
      this.$confirm(
        '是否确认删除公告编号为"' + noticeIds + '"的数据项?',
        "警告",
        {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning",
        }
      )
        .then(function () {
          return delNotice(noticeIds);
        })
        .then(() => {
          this.getList();
          this.msgSuccess("删除成功");
        })
        .catch(function () {});
    },
  },
};
</script>