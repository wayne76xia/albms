<template>
  <div :id="id" :class="className" :style="{height:height,width:width}" :data="data"/>
</template>

<script>
import echarts from 'echarts'
import resize from '@/components/Charts/mixins/resize'

export default {
  mixins: [resize],
  props: {
    className: {
      type: String,
      default: 'pieChart'
    },
    id: {
      type: String,
      default: 'memberPieChart'
    },
    width: {
      type: String,
      default: '200px'
    },
    height: {
      type: String,
      default: '200px'
    },
    clientStatus: {
      type: Number,
      required: true
    },
    data: {}
  },
  data() {
    return {
      chart: null,
      data_num: '6.5万'
    }
  },
  watch: {
    data(newVal, oldVal) {
      if (newVal != '') {
        this.initChart()
      }
    },
    deep: true
  },
  mounted() {
  },

  beforeDestroy() {
    if (!this.chart) {
      return
    }
    this.chart.dispose()
    this.chart = null
  },
  methods: {
    initChart() {
      let clientTitle = this.clientStatus === 0 ? '准会员' : '会员'

      this.chart = echarts.init(document.getElementById(this.id))
      this.chart.setOption({
        tooltip: {
          trigger: 'item',
          formatter: '{a} <br/>{b}: {c} ({d}%)'
        },
        legend: {
          icon: 'circle',
          orient: 'vertical',
          left: 20,
          top: 50,
          selectedMode: false, //取消图例上的点击事件
          itemWidth: 9,
          itemHeight: 9
          // formatter: function (name) {
          //   var total = 0;
          //   var target;
          //   for (var i = 0, l = data.length; i < l; i++) {
          //     total += data[i].value;
          //     if (data[i].name == name) {
          //       target = data[i].value;
          //     }
          //   }
          //   return (
          //     name + "\t\t\t" + target
          //   );
          // },
        },
        //环形颜色
        color: ['#4860FB', '#E8522A', '#01ff79', '#745BEB', '#ffa200', '#ff0000', '#ad9ae9', '#ee83ab', '#47d1ea', '#fcff00', '#05f52c'],
        series: [
          {
            hoverAnimation: false,
            name: clientTitle + '来源',
            type: 'pie',
            radius: ['40%', '60%'], //两个表示环：内半径,外半径
            center: ['70%', '53%'], //左右，上下
            avoidLabelOverlap: true,
            label: {
              normal: {
                show: false
              }

            },

            // //牵引线条颜色
            labelLine: {
              normal: {
                show: false
              }
            },
            data: this.data
          }
        ]
      })
    }
  }
}
</script>
