<template>
  <div :id="id" :class="className" :style="{height:height,width:width}" :data="data"/>
</template>

<script>
import echarts from "echarts";
import resize from "./mixins/resize";

export default {
  mixins: [resize],
  props: {
    className: {
      type: String,
      default: "Histogram",
    },
    id: {
      type: String,
      default: "Histogram",
    },
    width: {
      type: String,
      default: "200px",
    },
    height: {
      type: String,
      default: "200px",
    },
    data:{}
  },
  data() {
    return {
      chart: null,
      
    };
  },
  watch: {
    data(newVal, oldVal) {
      if (newVal != "") {
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
      this.chart = echarts.init(document.getElementById(this.id));
      this.chart.setOption({
        legend: {
          x: 30,
          data: [
            // 图列内容
            {
              name: "预约单",
              icon: "rect",
              textStyle: {
                color: "#95a0c4", // 单独设置某一个图列的颜色
                backgroundColor: "#fff", // 单独设置某一个图列的字体背景色
              },
            },
            {
              name: "销售单",
              icon: "rect",
              textStyle: {
                color: "#95a0c4", // 单独设置某一个图列的颜色
                backgroundColor: "#fff", // 单独设置某一个图列的字体背景色
              },
            },
          ],
        },
        dataset: {
          dimensions:["product", "预约单", "销售单"],
          source: this.data
        },
        xAxis: {
          type: "category",
          axisLine: {
            lineStyle: {
              color: "#999",
            },
          },
        },
        yAxis: {
          show: true,
          axisLine: {
            show: true,
            lineStyle: {
              color: "#fff",
            }
          },
          axisLabel : {
                            formatter: '{value}',
                            textStyle: {
                                color: '#666'
                            }
                        }
        },
     
        series: [
          {
            type: "bar",
            barWidth: 18,
            itemStyle: {
              normal: {
                color: "#4860FB",
              },
            },
          },
          {
            type: "bar",
            barWidth: 18,
            itemStyle: {
              normal: {
                color: "#5ECC59",
              },
            },
          },
        ],
      });
    },
  },
};
</script>
