<template>
  <div class="app-container">
    <div class>
      <chart />
    </div>
    <div class="title_box aui-padded-t-15">
      <p class="font-size-20">子公司管理</p>
      <div
        class="addnew bg-theme text-white aui-margin-l-10 border-radius font-size-14"
        @click="handleAdd"
      >新增</div>
    </div>
    <div class="table_data">
      <div class="bg-white bg_shadow bg-radius">
        <div class="aui-padded-15 aui-padded-t-10 aui-padded-b-0">
          <el-form :model="queryParams" ref="queryForm" :inline="true">
            <el-row>
              <el-col :span="20">
                <el-form-item label-width="130px" label="子公司名称/代码：" prop="deptName">
                  <el-input
                    v-model="queryParams.deptName"
                    placeholder="请输入子公司名称或代码"
                    clearable
                    style="width: 240px"
                    @keyup.enter.native="handleQuery"
                  />
                </el-form-item>
                <el-form-item label-width="150px" label="负责人/负责人电话：" prop="leader">
                  <el-input
                    v-model="queryParams.leader"
                    placeholder="请输入负责人姓名或电话"
                    clearable
                    style="width: 240px"
                    @keyup.enter.native="handleQuery"
                  />
                </el-form-item>
                <el-form-item label="区域：">
                  <el-cascader
                  style="width: 240px"
                    :options="options"
                    :props="props"
                    v-model="selectedOptions"
                    @change="handleChange"
                    clearable
                    filterable
                  ></el-cascader>
                </el-form-item>
                <el-form-item>
                  <el-button type="primary" size="medium" @click="handleQuery">查询</el-button>
                  <el-button size="medium" @click="resetQuery">重置</el-button>
                </el-form-item>
              </el-col>
              <el-col :span="4">
                <div class>
                  <div class="flex export_box" style="float:right">
                    <div class="text-black font-size-14 aui-padded-r-10">共{{total}}条数据</div>
                    <el-button size="mini" @click="handleImport">
                      <img src="../../../assets/image/daoru.png" alt />
                      <p style="margin-top:2px">导入</p>
                    </el-button>
                    <el-button size="mini" @click="handleExport" v-hasPermi="['system:dept:subsidiaryCompanyListExport']">
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
          :data="allSubsidiaryCompany"
          @selection-change="handleSelectionChange"
          :cell-class-name="getCellClass"
          header-cell-class-name="headerColor"
          stripe
        >
          <el-table-column type="selection" width="80" align="center" />
          <el-table-column label="代码" align="center" prop="deptCode" width="120"/>
          <el-table-column label="公司名称" align="center" prop="deptName" :show-overflow-tooltip="true" />
          <el-table-column label="负责人" align="center" prop="leader" :show-overflow-tooltip="true" />
          <el-table-column
            label="负责人电话"
            align="center"
            prop="leaderPhone"
            :show-overflow-tooltip="true"
          />
          <el-table-column label="门店数量" align="center" width="120">
            <template slot-scope="scope">
              <span
              v-if="scope.row.shopNum>0"
                class="text-theme udline"
                @click="checkShop(scope.row)"
              >{{scope.row.shopNum}}</span>
               <span
               v-else
                class="text-black "
              >{{scope.row.shopNum}}</span>
            </template>
          </el-table-column>
          <el-table-column label="所在区域" :show-overflow-tooltip="true">
            <template
              slot-scope="scope"
            >{{ scope.row.province }} {{ scope.row.city }} {{ scope.row.district }}</template>
          </el-table-column>
          <el-table-column label="详细地址" align="center" prop="address" width="160">
            <template slot-scope="scope">
              <span>{{ scope.row.address }}</span>
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
                @click="handleShow(scope.row)"
                v-hasPermi="['system:dept:subsidiaryCompanyInfo']"
                title="查看详情"
              >
                <img src="../../../assets/image/chaxun.png" alt />
              </el-button>
              <el-button
                size="mini"
                type="text"
                icon="el-icon-edit"
                style="color:#5ECC59 !important;font-size:16px"
                @click="handleUpdate(scope.row)"
                v-hasPermi="['system:dept:edit']"
                title="编辑"
              ></el-button>
              <el-button
                v-if="scope.row.userId !== 1"
                size="mini"
                type="text"
                icon="el-icon-delete"
                style="color: #E8522A !important;font-size:16px"
                @click="handleDelete(scope.row)"
                v-hasPermi="['system:dept:remove']"
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
        @pagination="subsidiaryCompanyList"
      />
    </div>

    <!-- 门店数据 -->
    <el-dialog class="table_data" :title="titleShop" :visible.sync="open1" width="65%" append-to-body>
      <el-table :data="shopData" header-cell-class-name="headerColor" stripe>
        <el-table-column label="代码" align="left" prop="deptCode" />
        <el-table-column label="门店名称" align="left" prop="deptName" :show-overflow-tooltip="true" />
        <el-table-column label="门店类型" align="left" prop="deptType" :show-overflow-tooltip="true">
          <template slot-scope="scope">{{ scope.row.deptType == '1' ? '直营店':'加盟店' }}</template>
        </el-table-column>

        <el-table-column label="店长" align="left" prop="leader" :show-overflow-tooltip="true" />
        <el-table-column
          label="店长电话"
          align="left"
          prop="phone"
          :show-overflow-tooltip="true"
        />
        <el-table-column label="门店电话" align="left" prop="deptPhone"  />
        <el-table-column label="所在区域" :show-overflow-tooltip="true" width='200'>
          <template
            slot-scope="scope"
          >
          <div style="width:100%">
          {{ scope.row.province }} {{ scope.row.city }} {{ scope.row.district }}</div></template>
        </el-table-column>
        <el-table-column
          label="详细地址"
          :show-overflow-tooltip="true"
          align="left"
          prop="address"
          width="160"
        >
          <template slot-scope="scope">
            <span>{{ scope.row.address }}</span>
          </template>
        </el-table-column>
      </el-table>
    </el-dialog>

    <!-- 添加或修改参数配置对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="40%" append-to-body>
      <div class="info_box" v-if="title == '子公司信息'">
        <div class="text-grayer flex aui-padded-b-10">
          <div>基本资料</div>
          <div class="line_dash"></div>
        </div>

        <el-row>
          <el-col :span="12">
            <div class="info_index">
              <p>代码：</p>
              <span v-text="form.deptCode"></span>
            </div>
          </el-col>
          <el-col :span="12">
            <div class="info_index">
              <p>名称：</p>
              <span v-text="form.deptName"></span>
            </div>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <div class="info_index">
              <p>负责人：</p>
              <span v-text="form.leader"></span>
            </div>
          </el-col>
          <el-col :span="12">
            <div class="info_index">
              <p>负责人电话：</p>
              <span v-text="form.leaderPhone"></span>
            </div>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <div class="info_index">
              <p>所在地区：</p>
              <span>{{form.province}}{{form.city}}{{form.district}}</span>
            </div>
          </el-col>
          <el-col :span="12">
            <div class="info_index">
              <p>详细地址：</p>
              <span v-text="form.address"></span>
            </div>
          </el-col>
        </el-row>
        <div class="text-grayer flex aui-padded-b-10">
          <div>数据统计</div>
          <div class="line_dash"></div>
        </div>
        <el-row>
          <el-col :span="12">
            <div class="info_index">
              <p>添加时间：</p>
              <span v-text="form.createDate"></span>
            </div>
          </el-col>
          <el-col :span="12">
            <div class="info_index">
              <p>门店：</p>
              <span v-text="form.totalNum"></span>
            </div>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <div class="info_index">
              <p>会员：</p>
              <span v-text="form.memberNum"></span>
            </div>
          </el-col>
          <el-col :span="12">
            <div class="info_index">
              <p>准会员：</p>
              <span v-text="form.associateMemberNum"></span>
            </div>
          </el-col>
        </el-row>
        <div class="text-grayer flex aui-padded-b-10">
          <div>门店</div>
          <div class="line_dash"></div>
        </div>
        <el-row justify="start" type="flex" :gutter="20">
          <el-col>
            <span class="shopName" v-for="(item,i) in form.shopList" :key="i">{{item.deptName}}</span>
          </el-col>
        </el-row>
      </div>
      <el-form v-if="title != '子公司信息'" ref="form" :model="form" :rules="rules" label-width="120px">
        <el-row>
          <el-col :span="24" v-if="form.parentId !== 0">
            <el-form-item label="上级部门：" prop="parentId">
              <treeselect
                v-model="form.parentId"
                :options="deptOptions"
                :normalizer="normalizer"
                placeholder="选择上级部门"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="代码：" prop="deptCode">
              <el-input v-model="form.deptCode" placeholder="请输入子公司代码" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="名称：" prop="deptName">
              <el-input v-model="form.deptName" placeholder="请输入子公司名称" />

            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="负责人：" prop="leader">
              <el-input v-model="form.leader" placeholder="请输入负责人名称" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="负责人电话：" prop="leaderPhone">
              <el-input v-model="form.phone" placeholder="请输入负责人电话" maxlength="11" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="所在地区：">
              <el-cascader
                :class="{'cascader' : selectedName != ''} "
                :options="options"
                :props="props"
                v-model="selectedOptions1"
                @change="handleChange1"
                :placeholder="selectedName != '' ? selectedName : '请选择'"
                clearable
                filterable
                style="width:100%"
              ></el-cascader>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="详细地址：" prop="address">
              <el-input v-model="form.address" placeholder="请输入详细地址" type="text" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item class label="显示排序：" prop="orderNum">
              <el-input v-model="form.orderNum" placeholder="请输入序号" oninput="value=value.replace(/^\.+|[^\d.]/g,'')"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <span
              class="font-size-14 text-graybc aui-padded-l-10"
              style="line-height:36px"
            >顺序排序，可用来控制子公司排序</span>
          </el-col>
        </el-row>
        <el-row>


        </el-row>
      </el-form>
      <div v-if="title != '子公司信息'" slot="footer" class="dialog-footer">
        <el-button @click="cancel">取 消</el-button>
        <el-button type="primary" @click="submitForm">确 定</el-button>
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
          <el-checkbox v-model="upload.updateSupport" />是否更新已经存在的子公司数据
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
import chart from "../chart/index";
import { regionData, CodeToText } from "element-china-area-data";
import { getToken } from "@/utils/auth";
import {
  subsidiaryCompanyList,
  subsidiaryCompanyListExport,
  importTemplate,
  subsidiaryCompanyInfo,
  getShop,
} from "@/api/user/index";
import {
  updateDept,
  getDept,
  addDept,
  delDept,
  listDept,
  treeselect,
} from "@/api/system/dept";
import Treeselect from "@riophae/vue-treeselect";
import "@riophae/vue-treeselect/dist/vue-treeselect.css";

export default {
  name: "subsidiaryCompany",
  components: { Treeselect, chart },
  data() {
    return {
      titleShop: "",
      open1: false,
      shopData:[],
      props: {},
      selectedOptions: [],
      options: regionData,
      selectedName: "",
      selectedOptions1: [], // 修改中选择器得值
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
      // 部门
      allSubsidiaryCompany: null,
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
        url:
          process.env.VUE_APP_BASE_API +
          "/system/dept/subsidiaryCompanyListImport",
      },
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        deptName: undefined,
        leader: undefined,
        province: undefined,
        city: undefined,
        district: undefined,
      },
      // 表单校验
      rules: {
        orderNum: [
          { required: true, message: "排序不能为空", trigger: "blur" },
        ],
        parentId: [
          { required: true, message: "上级部门不能为空", trigger: "blur" },
        ],
        deptCode: [
          { required: true, message: "子公司代码不能为空", trigger: "blur" },
        ],
        deptName: [
          { required: true, message: "子公司名称不能为空", trigger: "blur" },
        ],
        leader: [
          { required: true, message: "负责人不能为空", trigger: "blur" },
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
    this.subsidiaryCompanyList();
    this.getDicts("sys_normal_disable").then((response) => {
      this.statusOptions = response.data;
    });
    this.getDicts("sys_user_sex").then((response) => {
      this.sexOptions = response.data;
    });
    this.getConfigKey("sys.user.initPassword").then((response) => {
      this.initPassword = response.msg;
    });
  },
  methods: {
    checkShop(row) {
      const ids = row.deptId;
      getShop(ids).then((response) => {
        if (response.code == 200) {
          this.shopData = response.data;
          this.titleShop=row.deptName
          this.open1 = true;
        }
      });
    },
    handleChange() {
      var loc = "";
      let params = ["province", "city", "district"];
      if (this.selectedOptions.length > 0) {
        for (let i = 0; i < this.selectedOptions.length; i++) {
          this.queryParams[params[i]] = CodeToText[this.selectedOptions[i]];
          loc += CodeToText[this.selectedOptions[i]];
        }
      } else {
        for (let i = 0; i < params.length; i++) {
          this.queryParams[params[i]] = undefined;
        }
      }
    },
    handleChange1() {
      var loc = "";
      let params = ["province", "city", "district"];
      if (this.selectedOptions1.length > 0) {
        for (let i = 0; i < this.selectedOptions1.length; i++) {
          this.form[params[i]] = CodeToText[this.selectedOptions1[i]];
          loc += CodeToText[this.selectedOptions1[i]];
        }
      } else {
        for (let i = 0; i < params.length; i++) {
          this.form[params[i]] = undefined;
        }
      }
    },
    getCellClass({ row, column, rowIndex, columnIndex }) {
      if (columnIndex == 5) {
        return "underB";
      }
    },
    subsidiaryCompanyList() {
      let data = {};
      this.queryParams.deptName
        ? (data.deptName = this.queryParams.deptName)
        : "";
      this.queryParams.leader ? (data.leader = this.queryParams.leader) : "";
      this.queryParams.province
        ? (data.province = this.queryParams.province)
        : "";
      this.queryParams.city ? (data.city = this.queryParams.city) : "";
      this.queryParams.district
        ? (data.district = this.queryParams.district)
        : "";
      this.queryParams.pageNum ? (data.pageNum = this.queryParams.pageNum) : "";
      this.queryParams.pageSize
        ? (data.pageSize = this.queryParams.pageSize)
        : "";
      subsidiaryCompanyList(data).then((response) => {
        this.allSubsidiaryCompany = response.rows;
        this.total = response.total;
        this.loading = false;
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
      this.subsidiaryCompanyList();
    },

    // 取消按钮
    cancel() {
      this.open = false;
      this.reset();
    },
    // 表单重置
    reset() {
      this.form = {
        deptName: undefined,
        leader: undefined,
        address: undefined,
        city: undefined,
        deptId: undefined,
        leaderPhone: undefined,
        memberNum: undefined,
        associateMemberNum: undefined,
        province: undefined,
        totalNum: undefined,
        district: undefined,
        deptCode: undefined,
        email: undefined,
        shopList: [],
        type: 1,
      };
      this.selectedName = "";
      this.selectedOptions1 = [];
      this.resetForm("form");
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.page = 1;
      this.subsidiaryCompanyList();
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.selectedOptions = [];
      this.queryParams = {
        province: undefined,
        totalNum: undefined,
        district: undefined,
      };
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
      this.subsidiaryCompanyList();

      listDept().then((response) => {
        this.deptOptions = this.handleTree(response.data, "deptId");
        this.open = true;
        this.title = "添加子公司";
      });


    },
    normalizer(node) {
      if (node.children && !node.children.length) {
        delete node.children;
      }
      return {
        id: node.deptId,
        label: node.deptName,
        children: node.children,
      };
    },
    /** 查看按钮操作 */
    handleShow(row) {
      this.reset();
      const deptId = row.deptId;
      subsidiaryCompanyInfo(deptId).then((response) => {
        this.form = response.data;
        this.open = true;
        this.title = "子公司信息";
      });
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const deptId = row.deptId;
      listDept().then((response) => {
        this.deptOptions = this.handleTree(response.data, "deptId");



        getDept(deptId).then((response) => {
          console.log(response);
          this.form = response.data;
          this.selectedName = [
            response.data.province,
            response.data.city,
            response.data.district,
          ].join("/");
          this.open = true;
          this.title = "编辑子公司";
        });
      });
    },

    /** 提交按钮 */
    submitForm: function () {
      this.$refs["form"].validate((valid) => {
        if (valid) {

          if (this.title == "编辑子公司") {
            updateDept(this.form).then((response) => {
              if (response.code === 200) {
                this.msgSuccess("修改成功");
                this.open = false;
                this.subsidiaryCompanyList();
              }
            });
          } else {
            this.form.type = 1;
            addDept(this.form).then((response) => {
              if (response.code === 200) {
                this.msgSuccess("新增成功");
                this.open = false;
                this.subsidiaryCompanyList();
              }
            });
          }
        }
      });
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const deptId = row.deptId || this.ids;
      this.$confirm('是否确认删除所选数据?', "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      })
        .then(function () {
          return delDept(deptId);
        })
        .then(() => {
          this.subsidiaryCompanyList();
          this.msgSuccess("删除成功");
        })
        .catch(function () {});
    },
    /** 导出按钮操作 */
    handleExport() {
      const queryParams = this.queryParams;
      this.$confirm("是否确认导出所有数据项?", "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      })
        .then(function () {
          return subsidiaryCompanyListExport(queryParams);
        })
        .then((response) => {
          this.download(response.msg);
        })
        .catch(function () {});
    },
    /** 导入按钮操作 */
    handleImport() {
      this.upload.title = "子公司导入";
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
      this.upload.isUploading = true;
    },
    // 文件上传成功处理
    handleFileSuccess(response, file, fileList) {
      this.upload.open = false;
      this.upload.isUploading = false;
      this.$refs.upload.clearFiles();
      this.$alert(response.msg, "导入结果", { dangerouslyUseHTMLString: true });
      this.subsidiaryCompanyList();
    },
    // 提交上传文件
    submitFileForm() {
      this.$refs.upload.submit();
    },
  },
};
</script>
<style lang="scss" scoped>
$color: #0d1444;

/deep/.cascader {
  /deep/input::-webkit-input-placeholder {
    color: $color !important;
  }
  /deep/input::-moz-placeholder {
    /* Mozilla Firefox 19+ */
    color: $color !important;
  }
  /deep/input:-moz-placeholder {
    /* Mozilla Firefox 4 to 18 */
    color: $color !important;
  }
  /deep/input:-ms-input-placeholder {
    /* Internet Explorer 10-11 */
    color: $color !important;
  }
}

@import "@/assets/styles/new.scss";
.app-container1 {
  box-shadow: 0px 0px 8px rgba($color: #000000, $alpha: 0.2);
  border-radius: 6px;
  padding-top: 12px;
  overflow: hidden;
  //   width: 96%;
  //   margin-left: 2%;
  .from {
    width: 96%;
    margin-left: 2%;
  }

  .left_shu {
    font-size: 0.8em;
    @include leftShu(2em, 2em);
  }
}
</style>

