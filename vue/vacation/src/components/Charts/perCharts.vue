<template>
  <div :id="id" :class="className" :style="{height:height,width:width}" :data="data" />
</template>

<script>
import echarts from "echarts";
import resize from "./mixins/resize";

export default {
  mixins: [resize],
  props: {
    className: {
      type: String,
      default: "chart",
    },
    id: {
      type: String,
      default: "chart55",
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
  mounted() {},
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
        title: {
          text: "",
        },
        tooltip: {
          trigger: "axis",
        },
        legend: {
        //   data: this.data2,
        },
        grid: {
          left: "3%",
          right: "4%",
          bottom: "3%",
          containLabel: true,
        },
        xAxis: {
          type: "category",
          boundaryGap: false,
          data: this.data.monthArray,
          axisLine: {
            lineStyle: {
              color: "#ccc",
            },
          },
          axisLabel: {
            show: true,
            textStyle: {
              color: "#222",
            },
          },
        },
        yAxis: {
          name:'百分比',
          type: "value",
          axisLine: {
            lineStyle: {
              color: "#ccc",
            },
          },
          axisLabel: {
            
            formatter:'{value}%',
            show: true,
            textStyle: {
              color: "#222",
            },
          },
        },
        series: [
          {
            name: "话务员业绩达标率",
            type: "line",
            symbolSize: 10, //拐点圆的大小
            color: "#715AE5", //折线条的颜色
            data: this.data.totalReachRatioArray,
          },
          {
            name: "子公司业绩达标率",
            type: "line",
            symbolSize: 10, //拐点圆的大小
            color: "#5ECC59", //折线条的颜色
            data: this.data.totalCompanyRatioArray,
          },
          {
            name: "门店业绩达标率",
            type: "line",
            symbolSize: 10, //拐点圆的大小
            color: "#E8522A", //折线条的颜色
            data: this.data.totalShopRatioArray,
          },
        ],
      });
    },
  },
};
</script>
