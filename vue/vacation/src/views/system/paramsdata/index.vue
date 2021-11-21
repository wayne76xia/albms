<template>
  <div class="aui-padded-15">
    <div class="map">
      <el-input
        v-model="addressKeyword"
        placeholder="请输入地址查找相关位置"
        clearable
        style="margin-bottom: 20px"
      >
        <el-button
          slot="append"
          icon="el-icon-search"
          @click="getAddressKeyword"
        ></el-button>
      </el-input>
      <div id="container" style="width: 100%; height: 500px"></div>
      <div class="aui-padded-t-15 aui-padded-b-15 flex flex_start">
        <div>
          <span>经度：</span>
          <input class="aui-padded-5" type="text" v-model="shopInfo.lng" />
        </div>
        <div class="aui-padded-l-15">
          <span>纬度：</span>
          <input class="aui-padded-5" type="text" v-model="shopInfo.lat" />
        </div>

        <!-- <span class="aui-padded-l-15">纬度：{{shopInfo.lat}}</span> -->
      </div>
    </div>
  </div>
</template>

<script>
import maps from "qqmap";
export default {
  name: "qqmap",
  data() {
    return {
      //腾讯地图
      map: null,
      getAddress: null,
      getAddCode: null,
      addressKeyword: "",
      lat: "34.230684",
      lon: "108.886903",
      shopInfo: {
        lng: "",
        lat: "",
      },
    };
  },
  watch: {
    showModal: function (newValue) {
      if (newValue) {
        this.init();
      }
    },
  },
  created() {
    this.init();
  },

  methods: {
    //初始化地图
    init() {
      var that = this;
      maps.init("WW5BZ-LCN3U-VLPVA-4KQIT-PO5RO-ZVBOK", () => {
        var myLatlng = new qq.maps.LatLng(that.lat, that.lon);
        var myOptions = {
          zoom: 16,
          center: myLatlng,
          mapTypeId: qq.maps.MapTypeId.ROADMAP,
        };
        that.map = new qq.maps.Map(
          document.getElementById("container"),
          myOptions
        );
        //获取点击后的地址
        qq.maps.event.addListener(that.map, "click", function (event) {
          // 获取点击后的地图坐标
          that.shopInfo.lng = event.latLng.getLng();
          that.shopInfo.lat = event.latLng.getLat();
          console.log(that.shopInfo);
          that.getAddressCode();
        });
        //调用地址显示地图位置并设置地址
        that.getAddress = new qq.maps.Geocoder({
          complete: function (result) {
            that.map.setCenter(result.detail.location);
            console.log(result.detail.location);
            that.shopInfo.lng = result.detail.location.lng;
            that.shopInfo.lat = result.detail.location.lat;
            var marker = new qq.maps.Marker({
              map: that.map,
              position: result.detail.location,
            });
          },
        });
        //通过坐标来显示地图地址
        that.getAddCode = new qq.maps.Geocoder({
          complete: function (result) {
            that.addressKeyword = result.detail.address;
          },
        });
      });
    },

    //通过地址获得位置
    getAddressKeyword() {
      console.log(this.getAddress);
      //通过getLocation();方法获取位置信息值
      this.getAddress.getLocation(this.addressKeyword); //调用自带的接口
    },
    // 通过坐标获得地址
    getAddressCode() {
      var lat = parseFloat(this.shopInfo.lat);
      var lng = parseFloat(this.shopInfo.lng);
      var latLng = new qq.maps.LatLng(lat, lng);
      //调用获取位置方法
      this.getAddCode.getAddress(latLng);
    },
  },
};
</script>

<style >
</style>