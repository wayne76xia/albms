<template>
  <div>
    <div class="member">
      <div class="top_four flex aui-padded-10">
        <div class="top_four_item bg-white top_four_item1 flex bg-radius bg_shadow">
          <Chart1 id="chart1" height="100%" width="100%" :data1="data1" :data2="data2"/>
          <div class="top_four_item_text aui-padded-10">
            <p>总公司员工数量</p>
            <div class="flex flex_start" style="align-items:flex-end">
              <p class="font-size-24 fw_bold aui-padded-t-5 aui-padded-r-5">45,632</p>
              <img src="../../../assets/image/member_up.png" alt />
            </div>
            <p class="aui-padded-t-5 font-size-14">
              <span class="text-green">+4,632%</span>
              <span class="aui-padded-l-5">同上月对比</span>
            </p>
          </div>
        </div>
        <div class="top_four_item bg-white top_four_item2 flex bg-radius bg_shadow">
          <Chart2 id="chart2" height="100%" width="100%" :data1="data1" :data2="data2"/>
          <div class="top_four_item_text aui-padded-10">
            <p>子公司数量</p>
            <div class="flex flex_start" style="align-items:flex-end;">
              <p class="font-size-24 fw_bold aui-padded-t-5 aui-padded-r-5">45,632</p>
              <img src="../../../assets/image/member_down.png" alt />
            </div>
            <p class="aui-padded-t-5 font-size-14">
              <span class="text-or">-4,632%</span>
              <span class="aui-padded-l-5">同上月对比</span>
            </p>
          </div>
        </div>
        <div class="top_four_item bg-white top_four_item3 flex bg-radius bg_shadow">
          <Chart3 id="chart3" height="100%" width="100%" :data1="data1" :data2="data2"/>
          <div class="top_four_item_text aui-padded-10">
            <p>门店数量</p>
            <div class="flex flex_start" style="align-items:flex-end">
              <p class="font-size-24 fw_bold aui-padded-t-5 aui-padded-r-5">45,632</p>
              <img src="../../../assets/image/member_up.png" alt />
            </div>
            <p class="aui-padded-t-5 font-size-14">
              <span class="text-green">+4,632%</span>
              <span class="aui-padded-l-5">同上月对比</span>
            </p>
          </div>
        </div>
         <div class="top_four_item bg-white top_four_item2 flex bg-radius bg_shadow">
          <Chart4 id="chart4" height="100%" width="100%" :data1="data1" :data2="data2"/>
          <div class="top_four_item_text aui-padded-10">
            <p>验光师数量</p>
            <div class="flex flex_start" style="align-items:flex-end;">
              <p class="font-size-24 fw_bold aui-padded-t-5 aui-padded-r-5">45,632</p>
              <img src="../../../assets/image/member_down.png" alt />
            </div>
            <p class="aui-padded-t-5 font-size-14">
              <span class="text-or">-4,632%</span>
              <span class="aui-padded-l-5">同上月对比</span>
            </p>
          </div>
        </div>
      </div>
      <!-- 会员信息
      <el-dialog title="会员信息" :visible.sync="dialogVisible" width="40%" :before-close="handleClose">
        <div>
          <div class="text-grayer aui-padded-t-15 flex">
            <div>基本资料</div>
            <div class="line_dash"></div>
          </div>
          <div class="aui-padded-15 font-size-14">
            <el-row>
              <el-col :span="12" class="aui-padded-b-10">
                <span>下单用户：</span>
                <span class="aui-padded-l-5 text-graybc">随风</span>
              </el-col>
              <el-col :span="12" class="aui-padded-b-10">
                <span>性别：</span>
                <span class="aui-padded-l-5 text-graybc">男</span>
              </el-col>
              <el-col :span="12" class="aui-padded-b-10">
                <span>电话：</span>
                <span class="aui-padded-l-5 text-graybc">17868879654</span>
              </el-col>
              <el-col :span="12" class="aui-padded-b-10">
                <span>年龄：</span>
                <span class="aui-padded-l-5 text-graybc">22</span>
              </el-col>
              <el-col :span="12" class="aui-padded-b-10">
                <span>所在区域：</span>
                <span class="aui-padded-l-5 text-graybc">陕西省 西安市 雁塔区</span>
              </el-col>
              <el-col :span="12" class="aui-padded-b-10">
                <span>详细地址：</span>
                <span class="aui-padded-l-5 text-graybc">雁塔区和盛京光中心</span>
              </el-col>
              <el-col :span="12" class="aui-padded-b-10">
                <span>护照号：</span>
                <span class="aui-padded-l-5 text-graybc">3423</span>
              </el-col>
              <el-col :span="12" class="aui-padded-b-10">
                <span>所在门店：</span>
                <span class="aui-padded-l-5 text-graybc">敬意门店</span>
              </el-col>
              <el-col :span="12" class="aui-padded-b-10">
                <span>介绍人：</span>
                <span class="aui-padded-l-5 text-graybc">刘敏俊</span>
              </el-col>
              <el-col :span="12" class="aui-padded-b-10">
                <span>预约来源：</span>
                <span class="aui-padded-l-5 text-graybc">刘敏俊</span>
              </el-col>
              <el-col :span="12" class="aui-padded-b-10">
                <span>预约人：</span>
                <span class="aui-padded-l-5 text-graybc">刘敏俊</span>
              </el-col>
              <el-col :span="12" class="aui-padded-b-10">
                <span>客户备注：</span>
                <span class="aui-padded-l-5 text-graybc">这个客户很和善，也有购买意愿，望跟进！</span>
              </el-col>
            </el-row>
          </div>
          <div class="text-grayer flex">
            <div>数据统计</div>
            <div class="line_dash"></div>
          </div>
          <div class="aui-padded-15 font-size-14 aui-padded-b-0">
            <el-row>
              <el-col :span="12" class="aui-padded-b-10">
                <span>建档时间：</span>
                <span class="aui-padded-l-5 text-graybc">2020.02.02</span>
              </el-col>
              <el-col :span="12" class="aui-padded-b-10">
                <span>来源：</span>
                <span class="aui-padded-l-5 text-graybc">电话咨询</span>
              </el-col>
              <el-col :span="12" class="aui-padded-b-10">
                <span>会员等级：</span>
                <span class="aui-padded-l-5 text-graybc">v1</span>
              </el-col>
              <el-col :span="12" class="aui-padded-b-10">
                <span>累计消费：</span>
                <span class="aui-padded-l-5 text-graybc">34.874元</span>
              </el-col>
              <el-col :span="12" class="aui-padded-b-10">
                <span>消费次数：</span>
                <span class="aui-padded-l-5 text-theme">3</span>
              </el-col>
              <el-col :span="12" class="aui-padded-b-10">
                <span>上次消费时间：</span>
                <span class="aui-padded-l-5 text-graybc">2020.03.14</span>
              </el-col>
              <el-col :span="12" class="aui-padded-b-10">
                <span>转介绍次数：</span>
                <span class="aui-padded-l-5 text-theme">2</span>
              </el-col>
              <el-col :span="12" class="aui-padded-b-10">
                <span>累计积分：</span>
                <span class="aui-padded-l-5 text-graybc">234</span>
              </el-col>
              <el-col :span="12" class="aui-padded-b-10">
                <span>剩余积分：</span>
                <span class="aui-padded-l-5 text-graybc">345</span>
              </el-col>
            </el-row>
          </div>
          <div class="text-grayer flex">
            <div>回访记录</div>
            <div class="line_dash"></div>
          </div>
          <div class="aui-padded-t-10">
            <el-input
              type="textarea"
              placeholder="点击此处添加回访记录"
              v-model="textarea"
              
              show-word-limit
            ></el-input>
            <el-button class="aui-margin-t-10" type="primary">提交</el-button>
          </div>
          <div class="aui-margin-t-10 font-size-12">
            <div class="aui-padded-b-10">
              <p class="font-size-14">这个客户很有购买意愿，要一直跟进</p>
              <p class="text-graybc aui-padded-t-5"> 
                <span>业务员：刘海卡</span>
                <span class="aui-padded-l-10 ">2020.03.20 12:45</span>
              </p>
            </div>
            <div class="aui-padded-b-10">
              <p class="font-size-14">这个客户很有购买意愿，要一直跟进。这个客户很有购买意愿，要一直跟进。这个客户很有购买意愿，要一直跟进。这个客户很有购买意愿，要一直跟进。</p>
              <p class="text-graybc aui-padded-t-5"> 
                <span>业务员：刘海卡</span>
                <span class="aui-padded-l-10 ">2020.03.20 12:45</span>
              </p>
            </div>
          </div>
        </div>
      </el-dialog> -->
    </div>
  </div>
</template>

<script>
import Chart1 from "@/components/Charts/memberLineChart1";
import Chart2 from "@/components/Charts/memberLineChart2";
import Chart3 from "@/components/Charts/memberLineChart3";
export default {
  name: "member",
  components: {
    Chart1,
    Chart2,
    Chart3,
    Chart4
  },
  data() {
    return {
      dialogVisible: false,
       data1: ["01月", "02月", "03月", "04月", "05月", "06月"],
      data2: [80, 90, 200, 400, 125, 200],
    };
  },
  methods: {
    handleClose(done) {
      this.$confirm("确认关闭？")
        .then((_) => {
          done();
        })
        .catch((_) => {});
    },
  },
};
</script>

<style  >
.member .top_four {
  height: 250px;
}
.member .top_four_item {
  width: 24%;
  height: 100%;
  border-radius: 10px;
  position: relative;
}
.member .top_four_item_text {
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

.member .line_dash {
  border-top: 1px dashed #eeeeee;
  width: 90%;
}
</style>