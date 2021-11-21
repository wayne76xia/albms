<template>
  <div :id="id" :class="className" :style="{height:height,width:width}" :data="data"/>
</template>

<script>
import echarts from "echarts";
import resize from "@/components/Charts/mixins/resize";

export default {
  mixins: [resize],
  props: {
    className: {
      type: String,
      default: "pieChart",
    },
    id: {
      type: String,
      default: "pieChart",
    },
    width: {
      type: String,
      default: "200px",
    },
    height: {
      type: String,
      default: "200px",
    },
    data: {},
  },
  data() {
    return {
      chart: null,
      data_num: "",
    };
  },
  watch: {
    data(newVal, oldVal) {
      if (newVal != "") {
        var sum = 0;
        for (let i = 0; i < this.data.length; i++) {
          sum += this.data[i].value;
        }
        this.data_num = sum;
        this.initChart();
      }
    },
     deep: true,
  },

  beforeDestroy() {
    if (!this.chart) {
      return;
    }
    this.chart.dispose();
    this.chart = null;
  },
  methods: {
    initChart() {
      var data = this.data;

      this.chart = echarts.init(document.getElementById(this.id));
      this.chart.setOption({
        tooltip: {
          trigger: "item",
          formatter: "{a} <br/>{b}: {c} ({d}%)",
        },
        legend: {
          icon: "circle",
          orient: "vertical",
          left: 140,
          bottom: 20,
          selectedMode: false, //取消图例上的点击事件
          itemWidth: 9,
          itemHeight: 9,
          // formatter: function (name) {
          //   var total = 0;
          //   var target;
          //   for (var i = 0, l = data.length; i < l; i++) {
          //     total += data[i].value;
          //     if (data[i].name == name) {
          //       target = data[i].ratio;
          //     }
          //   }
          //   return (
          //     name + "\t\t\t" + target + "同上月对比"
          //   );
          // },
        },

        legend: {
          icon: "circle",
          orient: "vertical",
          left: 40,
          bottom: 120,
          selectedMode: false, //取消图例上的点击事件
          itemWidth: 9,
          itemHeight: 9,
          data:["镜架", "成镜", "兑换商品", "赠送商品"],
        },

        //环形颜色
        color: ["#4860FB", "#5ECC59", "#745BEB", "#E8522A"],
        
        // 设置环形中间的数据
        graphic: [
          {
            type: "text",
            left: "58%",
            top: "44%",
            z: 10,
            style: {
              fill: "#717880",
              text: this.data_num + "\n商品总数",
              font: "16px Microsoft YaHei",
              textAlign: "middle",
              textVerticalAlign: "middle",
            },
          },
        ],
        series: [
          {
            hoverAnimation: false,
            name: "商品占比分析",
            type: "pie",
             radius: ["35%", "48%"], //两个表示环：内半径,外半径
            center: ["65%", "48%"], //左右，上下
            avoidLabelOverlap: true,
            label: {
              normal: {
                show: false,
                position: "outer",
                //模板变量有 {a}, {b}，{c}，{d}，{e}，分别表示系列名，数据名，数据值等。
                formatter: "{c_set|{c}\t\t}人" + "\n\n占比{d}%",
                borderWidth: 20,
                borderRadius: 4,
                padding: [90, -50],
                rich: {
                  a_set: {
                    color: "#1a1a1a",
                    lineHeight: 20,
                    align: "center",
                  },
                  // c_set: {
                  //   color: "#808080",
                  // },
                },
              },
              // emphasis: {
              //   show: false,
              //   textStyle: {
              //     fontSize: "12",
              //   },
              // },
            },
            //牵引线条颜色
            // labelLine: {
            //   normal: {
            //     show: false,
            //     length: 30,
            //     length2: 50,
            //   },
            // },
            data: this.data,
          },
        ],
      });
    },
  },
};
</script>
