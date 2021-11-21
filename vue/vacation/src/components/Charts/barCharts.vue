<template>
  <div :class="className" :style="{height:height,width:width}" :data="data"/>
</template>

<script>
import echarts from "echarts";
require("echarts/theme/macarons"); // echarts theme
import resize from "./mixins/resize";

const animationDuration = 6000;

export default {
  mixins: [resize],
  props: {
    className: {
      type: String,
      default: "chart",
    },
    width: {
      type: String,
      default: "100%",
    },
    height: {
      type: String,
      default: "300px",
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
  mounted() {
    // this.$nextTick(() => {
    //   this.initChart();
    // });
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
      this.chart = echarts.init(this.$el, "macarons");

      this.chart.setOption({
        legend: {
          type: "plain", // 图列类型，默认为 'plain'
          top: "0%", // 图列相对容器的位置 top\bottom\left\right
          selected: {
            // '销量': true  // 图列选择，图形加载出来会显示选择的图列，默认为true
          },
          textStyle: {
            // 图列内容样式
            color: "#0D1444", // 字体颜色
          },
          tooltip: {
            // 图列提示框，默认不显示
            show: true,
            color: "red",
          },
        },
        tooltip: {
          trigger: "axis",
          axisPointer: {
            // 坐标轴指示器，坐标轴触发有效
            type: "shadow", // 默认为直线，可选为：'line' | 'shadow'
          },
        },
        grid: {
          top: 10,
          left: "2%",
          right: "2%",
          top:"15%",
          bottom: "3%",
          containLabel: true,
        },
        xAxis: [
          {
            type: "category",
            data: this.data.companyArry,
            axisTick: {
              alignWithLabel: true,
            },
            axisLine: {
              lineStyle: {
                type: "solid",
                color: "#eee", //左边线的颜色
                width: "1", //坐标线的宽度
              },
            },
            axisLabel: {
              show: true,
                // interval:3,
                rotate:30,
              textStyle: {
                color: "#0D1444", //坐标值得具体的颜色
              },
            },
          },
        ],
        yAxis: [
          {
            type: "value",
            axisTick: {
              show: false,
            },
            axisLine: {
              lineStyle: {
                type: "solid",
                color: "#eee", //左边线的颜色
                width: "1", //坐标线的宽度
              },
            },
            axisLabel: {
              
              textStyle: {
                color: "#0D1444", //坐标值得具体的颜色
              },
            },
          },
        ],
        series: [
          {
            name: "业绩达标门店数量",
            type: "bar",
            stack: "vistors",
            barWidth: "50%",
            data: this.data.shopReachArry,
            animationDuration,
            color: ["#4860FB"],
          },
          {
            name: "门店数量",
            type: "bar",
            stack: "vistors",
            barWidth: "50%",
            data: this.data.shopArry,
            animationDuration,
            color: ["#E4E6F6"],
          },
        ],
      });
    },
  },
};
</script>
