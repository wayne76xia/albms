<template>
  <div class="app-container">
    <!-- packUpUpdate 的值需要注意 -->
    <!-- 登录页图片 -->
    <div
      class="member_set aui-margin-b-10"
      v-hasPermi="['glasses:paramsData:list']"
    >
      <div class="flex flex_start aui-padded-t-10 aui-padded-b-10">
        <p class="fw_bold text-black font-size-20">登录页图片</p>
      </div>
      <el-table
        class="shadow"
        v-loading="paramsLoading"
        stripe
        :data="loginPic"
        @selection-change="handleSelectionChange"
      >
        <el-table-column label="名称" prop="name" width="240"/>
        <el-table-column label="图片" prop="value" width="240">
          <template slot-scope="scope">
            <div class="aui-padded-t-5">
              <img
                :src="scope.row.value"
                alt
                style="width: 50px; height: 50px"
              />
            </div>
          </template>
        </el-table-column>
        <el-table-column label="操作时间" prop="lastDate" width="240"/>

        <el-table-column label="操作" class-name="small-padding ">
          <template slot-scope="scope">
            <el-button
              size="mini"
              type="text"
              icon="el-icon-edit"
              style="color: #5ecc59 !important; font-size: 16px"
              @click="handleBackGroundPicUpdate(scope.row)"
              v-hasPermi="['glasses:paramsData:edit','system:config:edit']"
              title="编辑"
            ></el-button>
          </template>
        </el-table-column>
        <el-table-column prop width="240" align="right">
          <template slot="header">
            <div class="text-theme aui-padded-r-15" @click="packUpUpdate(11)">
              <span class="showdata">收起</span>
              <img
                class="showimg"
                style="width: 10px; height: 10px"
                src="../../../assets/image/display_data.png"
                alt
              />
            </div>
          </template>
        </el-table-column>
      </el-table>
    </div>
    <!-- 技术客服电话 -->
    <div
      class="member_set aui-margin-b-10"
      v-hasPermi="['glasses:paramsData:list']"
    >
      <div class="flex flex_start aui-padded-t-10 aui-padded-b-10">
        <p class="fw_bold text-black font-size-20">技术客服电话</p>
      </div>
      <el-table
        class="shadow"
        v-loading="paramsLoading"
        stripe
        :data="tecPhone"
        @selection-change="handleSelectionChange"
      >
        <el-table-column label="名称" prop="name" width="240"/>
        <el-table-column label="电话" prop="value" width="240"/>
        <el-table-column label="操作时间" prop="lastDate" width="240"/>
        <el-table-column label="操作" class-name="small-padding ">
          <template slot-scope="scope">
            <el-button
              size="mini"
              type="text"
              icon="el-icon-edit"
              style="color: #5ecc59 !important; font-size: 16px"
              @click="paramshandleUpdate(scope.row)"
              v-hasPermi="['glasses:paramsData:edit']"
              title="编辑"
            ></el-button>
          </template>
        </el-table-column>
        <el-table-column prop width="240" align="right">
          <template slot="header">
            <div class="text-theme aui-padded-r-15" @click="packUpUpdate(12)">
              <span class="showdata">收起</span>
              <img
                class="showimg"
                style="width: 10px; height: 10px"
                src="../../../assets/image/display_data.png"
                alt
              />
            </div>
          </template>
        </el-table-column>
      </el-table>
    </div>
    <!-- 参数设置 -->
    <el-dialog
      :title="paramstitle"
      :visible.sync="paramsopen"
      width="500px"
      append-to-body
    >
      <el-form
        ref="paramsform"
        :model="paramsform"
        :rules="paramsRules"
        label-width="80px"
      >
        <el-form-item label="名字" prop="name">
          <el-input v-model="paramsform.name" placeholder="请输入名字"/>
        </el-form-item>
        <div class v-if="paramsform.groupType === 5">
          <el-form-item label="值" prop="value">
            <el-upload
              action="#"
              :http-request="requestUpload"
              :show-file-list="false"
              :before-upload="beforeUpload"
            >
              <img :src="paramsform.value" class="avatar"/>
            </el-upload>
          </el-form-item>
        </div>
        <el-form-item label="值" prop="value" v-else>
          <el-input
            v-model="paramsform.value"
            placeholder="请输入值"
            disabled
            v-if="dataType"
          />
          <el-input
            v-model="paramsform.value"
            placeholder="请输入值"
            v-if="!dataType"
          />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="paramsubmitForm">确 定</el-button>
        <el-button @click="paramsCancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { mapGetters } from 'vuex'
import {
  listParamsData,
  getParamsData,
  delParamsData,
  addParamsData,
  updateParamsData,
  exportParamsData, getBackGroundPic, updateBackGroundPic
} from '@/api/system/dataset/paramsdata'

export default {
  name: 'MemberLevel',
  data() {
    return {
      dataType: false,
      backGroundPic: false,
      dynamicTags: [],
      // 收起/展开
      packTitle: '收起 ',
      packImg: '../../../assets/image/display_data.png',
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
      // 会员等级设置表格数据
      memberLevelList: [],

      // 弹出层标题
      title: '',
      // 是否显示弹出层
      open: false,
      // 删除状态0未1已字典
      delStatusOptions: [],
      // 查询参数
      queryParams: {
        // pageNum: 1,
        // pageSize: 20,
        name: undefined
      },

      // 表单参数
      form: {},

      // 表单校验
      rules: {
        name: [
          { required: true, message: '会员等级名字不能为空', trigger: 'blur' }
        ],
        condition: [
          { required: true, message: '满足条件不能为空', trigger: 'blur' }
        ],
        discount: [
          { required: true, message: '折扣比例不能为空', trigger: 'blur' }
        ],
        delStatus: [
          {
            required: true,
            message: '删除状态0未1已不能为空',
            trigger: 'blur'
          }
        ]
      },
      //参数设置
      paramsLoading: true,
      paymentList: [], //预付定金比例
      stockList: [], //库存报警设置
      performanceList: [], //业绩考核标准
      returnList: [], //回访时间设置
      resorceList: [], //预约管理来源
      loginPic: [], //登录页图片
      tecPhone: [], //技术客服电话
      paramsDataList: [],
      paramsopen: false,
      paramstitle: '',
      paramsform: {},
      // 查看二维码
      codePic: '',
      codeShow: false,
      checkDetailParams: {
        id: ''
      },
      paramsAdd: '', // 添加参数
      paramsRules: {
        name: [{ required: true, message: '名字不能为空', trigger: 'blur' }],
        value: [{ required: true, message: '值不能为空', trigger: 'blur' }]
      },
      //业绩考核计算设置
      calculateList: [],
      branchList: [], //分公司考核计算
      storeList: [], //门店考核计算
      telephoneList: [], //话务员考核计算
      optometristList: [], //验光师考核计算
      calculateLoading: false,
      calculateopen: false,
      calculatetitle: '',
      calculateform: {},
      calculaterules: {
        name: [{ required: true, message: '名字不能为空', trigger: 'blur' }]
      },

      // 镜架参数
      mirrorBracketParamsList: [],
      mirrorLoading: false,
      mirrorOpen: false,
      mirrorForm: {},
      mirrorTitle: '',
      mirrorList: [], //根据type获取镜架参数

      mirrorRules: {
        name: [
          { required: true, message: '参数名字不能为空', trigger: 'blur' }
        ],
        type: [
          {
            required: true,
            message:
              '类型（1款式2颜色3立线高度4材质5镜圈宽度6中梁宽度7前倾角8镜腿长度9面弯）不能为空',
            trigger: 'blur'
          }
        ],
        delStatus: [
          {
            required: true,
            message: '删除状态0未1已不能为空',
            trigger: 'blur'
          }
        ],
        lastDate: [
          { required: true, message: '最后操作时间不能为空', trigger: 'blur' }
        ]
      },
      //镜架型号
      glassList: [],
      glassloading: false,
      glassopen: false,
      glasstitle: '',
      glassform: {}, // 表单参数
      glassrules: {
        name: [
          { required: true, message: '型号名字不能为空', trigger: 'blur' }
        ],
        styles: [{ required: true, message: '款式不能为空', trigger: 'blur' }],
        colors: [{ required: true, message: '颜色不能为空', trigger: 'blur' }],
        verticalLineHeight: [
          { required: true, message: '立线高度不能为空', trigger: 'blur' }
        ],
        textures: [
          { required: true, message: '材质不能为空', trigger: 'blur' }
        ],
        mirrorRingWidth: [
          { required: true, message: '镜圈宽度不能为空', trigger: 'blur' }
        ],
        centreSillWidth: [
          { required: true, message: '中梁宽度不能为空', trigger: 'blur' }
        ],
        topRake: [
          { required: true, message: '前倾角不能为空', trigger: 'blur' }
        ],
        legLength: [
          { required: true, message: '镜腿长度不能为空', trigger: 'blur' }
        ],
        surfaceBending: [
          { required: true, message: '面弯不能为空', trigger: 'blur' }
        ],
        lastDate: [
          { required: true, message: '最后操作时间不能为空', trigger: 'blur' }
        ]
      },
      //添加镜架参数列表
      kuanshi_List: [],
      yanse_List: [],
      lixian_List: [],
      caizhi_List: [],
      jingquan_List: [],
      zhongliang_List: [],
      qianqing_List: [],
      jingtui_List: [],
      mianwan_List: [],

      applyFaceTypeList: [], //脸型列表
      applyFaceTypes: []
    }
  },
  computed: {
    ...mapGetters(['permissions']),
    setting: {
      get() {
        return this.$store.state.settings.showSettings
      },
      set(val) {
        this.$store.dispatch('settings/changeSetting', {
          key: 'showSettings',
          value: val
        })
      }
    }
  },
  created() {
    this.getDicts('apply_face_type').then((response) => {
      console.log(response.data)
      this.applyFaceTypeList = response.data
    })
    console.log(this.permissions)
    if (this.permissions.includes('*:*:*')) {
      this.paramsdataList()
    } else {
      if (this.permissions.includes('glasses:paramsData:list')) {
        this.paramsdataList()
      }
    }

    this.getDicts('del_status').then((response) => {
      this.delStatusOptions = response.data
    })
  },
  methods: {
    beforeUpload(file) {
      if (file.type.indexOf('image/') == -1) {
        this.msgError('文件格式错误，请上传图片类型,如：JPG，PNG后缀的文件。')
      } else {
        const reader = new FileReader()
        reader.readAsDataURL(file)
        reader.onload = () => {
          this.paramsform.value = reader.result
        }
      }
    },
    beforeUpload1(file) {
      if (file.type.indexOf('image/') == -1) {
        this.msgError('文件格式错误，请上传图片类型,如：JPG，PNG后缀的文件。')
      } else {
        const reader = new FileReader()
        reader.readAsDataURL(file)
        reader.onload = () => {
          this.glassform.coverImage = reader.result
        }
      }
    },
    requestUpload() {
    },

    // 参数设置列表
    paramsdataList() {
      this.paramsLoading = true
      listParamsData(this.queryParams).then((response) => {
        var paramsDataList = response.data
        for (let i = 0; i < paramsDataList.length; i++) {
          if (paramsDataList[i].groupType == 1) {
            this.paymentList.push(paramsDataList[i]) //预付定金比例
          } else if (paramsDataList[i].groupType == 2) {
            this.stockList.push(paramsDataList[i]) //库存报警设置
          } else if (paramsDataList[i].groupType == 3) {
            this.returnList.push(paramsDataList[i]) //回访时间设置
          } else if (paramsDataList[i].groupType == 4) {
            paramsDataList[i].checkDetail = '查看二维码'
            this.resorceList.push(paramsDataList[i]) //预约管理来源
          } else if (paramsDataList[i].groupType == 5) {
            this.loginPic.push(paramsDataList[i]) //登录页图片
          } else if (paramsDataList[i].groupType == 6) {
            this.tecPhone.push(paramsDataList[i]) //技术客服电话
          } else if (paramsDataList[i].groupType == 7) {
            this.performanceList.push(paramsDataList[i]) //业绩考核标准
          }
        }
        this.paramsLoading = false
      })
    },
    /** 修改按钮操作 */
    paramshandleUpdate(row) {
      console.log(row)
      this.paramsReset()
      const id = row.id
      getParamsData(id).then((response) => {
        console.log(response)
        console.log(2222222)

        if (response.data.type == 5) {
          this.dataType = true
          console.log(3333333)
        }
        this.paramsform = response.data
        this.paramsopen = true
        this.paramstitle = '修改参数设置'
      })
    },
    /** 修改登录页 */
    handleBackGroundPicUpdate(row) {
      this.paramsReset()
      const id = row.id
      getBackGroundPic(id).then((response) => {
        if (response.data.type === 6) { // 修改图片
          this.backGroundPic = true
        }
        this.paramsform = response.data
        this.paramsopen = true
        this.paramstitle = '修改参数设置'
      })
    },
    paramshandleDelete(row) {
      const name = row.name || this.name
      const ids = row.id || this.ids
      this.$confirm('是否确认删除镜架型号为"' + name + '"的数据项?', '警告', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      })
        .then(function() {
          return delParamsData(ids)
        })
        .then(() => {
          this.paymentList = []
          this.stockList = []
          this.performanceList = []
          this.returnList = []
          this.resorceList = []
          this.loginPic = []
          this.tecPhone = []
          this.paramsdataList()
          this.msgSuccess('删除成功')
        })
        .catch(function() {
        })
    },
    /** 添加按钮操作 */
    paramshandleAdd() {
      this.paramsReset()
      this.paramsopen = true
      this.paramstitle = '添加参数设置'
      this.paramsform.groupType = 4
      this.paramsform.type = 5
      this.paramsAdd = 1
    },
    /** 提交按钮 */
    paramsubmitForm: function() {
      this.$refs['paramsform'].validate((valid) => {
        if (valid) {
          if (this.paramsform.id != undefined) {
            if (this.backGroundPic) {
              updateBackGroundPic(this.paramsform).then((response) => {
                if (response.code === 200) {
                  this.msgSuccess('修改成功')
                  this.paramsopen = false
                  this.paymentList = []
                  this.stockList = []
                  this.performanceList = []
                  this.returnList = []
                  this.resorceList = []
                  this.loginPic = []
                  this.tecPhone = []
                  this.paramsdataList()
                  this.paramsAdd = 0
                  this.backGroundPic = false
                }
              })
            } else {
              updateParamsData(this.paramsform).then((response) => {
                if (response.code === 200) {
                  this.msgSuccess('修改成功')
                  this.paramsopen = false
                  this.paymentList = []
                  this.stockList = []
                  this.performanceList = []
                  this.returnList = []
                  this.resorceList = []
                  this.loginPic = []
                  this.tecPhone = []
                  this.paramsdataList()
                  this.paramsAdd = 0
                }
              })
            }
          } else {
            addParamsData(this.paramsform).then((response) => {
              if (response.code === 200) {
                this.msgSuccess('新增成功')
                this.paramsopen = false
                this.paramsopen = false
                this.paymentList = []
                this.stockList = []
                this.performanceList = []
                this.returnList = []
                this.resorceList = []
                this.loginPic = []
                this.tecPhone = []
                this.paramsdataList()
              }
            })
          }
        }
      })
      this.dataType = false
    },
    // 取消按钮
    paramsCancel() {
      this.dataType = false
      this.paramsopen = false
      this.paramsAdd = 0
      this.paramsReset()
    },
    // 表单重置
    paramsReset() {
      this.paramsform = {
        id: undefined,
        name: undefined,
        value: undefined,
        type: undefined,
        lastDate: undefined,
        groupType: undefined
      }
      this.paramsAdd = 0
      this.resetForm('paramsform')
    },
    /** 查询会员等级设置列表 */
    getList() {
      this.loading = true
      listMemberLevel(this.queryParams).then((response) => {
        this.memberLevelList = response.rows
        this.total = response.total
        this.loading = false
      })
    },
    // 删除状态0未1已字典翻译
    delStatusFormat(row, column) {
      return this.selectDictLabel(this.delStatusOptions, row.delStatus)
    },
    // 取消按钮
    cancel() {
      this.open = false
      this.reset()
    },
    // 表单重置
    reset() {
      this.form = {
        id: undefined,
        name: undefined,
        condition: undefined,
        discount: undefined,
        delStatus: undefined,
        lastDate: undefined
      }
      this.resetForm('form')
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1
      this.getList()
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.resetForm('queryForm')
      this.handleQuery()
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      // this.ids = selection.map((item) => item.id);
      this.single = selection.length != 1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset()
      this.open = true
      this.title = '添加会员等级设置'
    },

    /** 隐藏item操作 */
    packUpUpdate(index) {
      if (document.querySelectorAll('.el-table__body-wrapper')[index].style.display === 'none') {
        document.querySelectorAll('.el-table__body-wrapper')[index].style.display = 'block'
        document.querySelectorAll('.showdata')[index].innerHTML = '收起'
        document.querySelectorAll('.showimg')[index].src = '../../assets/image/login_top.png'
      } else {
        document.querySelectorAll('.el-table__body-wrapper')[index].style.display = 'none'
        document.querySelectorAll('.showdata')[index].innerHTML = '展开'
        document.querySelectorAll('.showimg')[index].src = '../../assets/image/login_top.png'
      }
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset()
      const id = row.id
      getMemberLevel(id).then((response) => {
        this.form = response.data
        this.open = true
        this.title = '修改会员等级设置'
      })
    },
    /** 提交按钮 */
    submitForm: function() {
      this.$refs['form'].validate((valid) => {
        if (valid) {
          if (this.form.id != undefined) {
            updateMemberLevel(this.form).then((response) => {
              if (response.code === 200) {
                this.msgSuccess('修改成功')
                this.open = false
                this.getList()
              }
            })
          } else {
            addMemberLevel(this.form).then((response) => {
              if (response.code === 200) {
                this.msgSuccess('新增成功')
                this.open = false
                this.getList()
              }
            })
          }
        }
      })
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const ids = row.id
      this.$confirm(
        '是否确认删除会员等级设置编号为"' + ids + '"的数据项?',
        '警告',
        {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }
      )
        .then(function() {
          return delMemberLevel(ids)
        })
        .then(() => {
          this.getList()
          this.msgSuccess('删除成功')
        })
        .catch(function() {
        })
    },
    /** 导出按钮操作 */
    handleExport() {
      const queryParams = this.queryParams
      this.$confirm('是否确认导出所有会员等级设置数据项?', '警告', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      })
        .then(function() {
          return exportMemberLevel(queryParams)
        })
        .then((response) => {
          this.download(response.msg)
        })
        .catch(function() {
        })
    }
  }
}
</script>
<style>
/*.checkdetail .el-table__row .checkdetail_txt .cell {*/
/*  color: #1765ff !important;*/
/*  font-size: 12px !important;*/
/*}*/

body {
  background-color: #fff !important;
}

.el-table__header-wrapper {
  background-color: #f6f8ff !important;
  height: 50px !important;
  /* line-height: 60px !important; */
}

.el-table__header-wrapper th {
  height: 50px !important;
}

.el-table__body-wrapper .el-table th,
.el-table td {
  height: 50px !important;
  padding: 0 !important;
}

/*.el-table__header-wrapper .cell {*/
/*  font-size: 14px !important;*/
/*  !*color: #a6abcc !important;*!*/
/*  font-weight: 400 !important;*/
/*  margin-left: 10px !important;*/
/*}*/

/*.member_set .el-table__header-wrapper .el-table_1_column_6 .cell {*/
/*  color: #4860fb !important;*/
/*  padding-right: 10px !important;*/
/*}*/

/*.el-table__row .cell {*/
/*  color: #0d1444 !important;*/
/*  font-size: 14px !important;*/
/*  margin-left: 10px !important;*/
/*}*/

.el-dialog__wrapper .el-form-item {
  border: none !important;
}

.avatar-uploader .el-upload {
  border: 1px dashed #d9d9d9;
  border-radius: 6px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
}

.avatar-uploader .el-upload:hover {
  border-color: #409eff;
}

.avatar-uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 178px;
  height: 178px;
  line-height: 178px;
  text-align: center;
}

.avatar {
  width: 178px;
  height: 178px;
  display: block;
}

.change_mirror .el-dialog__header {
  border-bottom: 1px solid #eeeeee;
}

.change_mirror .el-input__inner {
  border-top-right-radius: 0px !important;
  border-bottom-right-radius: 0px !important;
}

.change_mirror .qd {
  border-top-left-radius: 0px !important;
  border-bottom-left-radius: 0px !important;
}

.change_mirror .canshu_line {
  width: 86%;
  height: 1px;
  border-top: 1px dashed #eeeeee;
}

.el-button--primary {
  background-color: #4860fb !important;
  border-color: #4860fb !important;
}

.change_mirror .el-tag {
  margin-right: 10px !important;
}

.el-dialog__header {
  border-bottom: 1px solid #eeeeee;
}

.line_dash {
  border-top: 1px dashed #eeeeee;
  width: 84%;
}

.code_pics .el-dialog .el-dialog__body {
  text-align: center !important;
}
</style>
