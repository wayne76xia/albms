<template>
  <div :id="id" :class="className" :style="{height:height,width:width}" />
</template>

<script>
import echarts from "echarts";
import resize from "./mixins/resize";

export default {
  mixins: [resize],
  props: {
    className: {
      type: String,
      default: "LineGraph",
    },
    id: {
      type: String,
      default: "LineGraph",
    },
    width: {
      type: String,
      default: "200px",
    },
    height: {
      type: String,
      default: "200px",
    },
  },
  data() {
    return {
      chart: null,
    };
  },
  mounted() {
    this.initChart();
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
        xAxis: {
          type: "category",
          boundaryGap: false,
          axisLine: {
            lineStyle: {
              // 设置x轴颜色
              color: '#999',
            }
          },
        },
        yAxis: {
          type: "value",
          boundaryGap: [0, "30%"],
           axisLine: {
            lineStyle: {
              // 设置x轴颜色
              color: '#999',
            }
          },
        },
        visualMap: {
          type: "piecewise",
          show: false,
          dimension: 0,
          seriesIndex: 0,
        
        },
        series: [
          {
            type: "line",
            smooth: false,
            symbol: "circle",
            symbolSize: 10,
            itemStyle: {
              normal: {
                color: "#999",
                borderColor:'#5962ff',  //拐点边框颜色
                
              },
            },
            lineStyle: {
                  color: "#5962ff",
                  width: 1,
                },
            
            markLine: {
              symbol: ["none", "none"],
              label: { show: false },
              data: [
                { xAxis: 1 },
                { xAxis: 2 },
                { xAxis: 3 },
                { xAxis: 4 },
                { xAxis: 5 },
                { xAxis: 6 },
                { xAxis: 7 },
                { xAxis: 8 },
                { xAxis: 9 },
                { xAxis: 10 },
                { xAxis: 11 },
                { xAxis: 12 },
              ],
            },
            areaStyle: {
              color: "none",
            },
            data: [
              ["1月", 200],
              ["2月", 400],
              ["3月", 650],
              ["4月", 500],
              ["5月", 250],
              ["6月", 300],
              ["7月", 450],
              ["8月", 300],
              ["9月", 100],
              ["10月", 300],
              ["11月", 200],
              ["12月", 100],
            ],
          },
        ],
      });
    },
  },
};
</script>
