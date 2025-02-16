<template>
  <div style="padding: 10px;background: linear-gradient(90deg, #695656, #34292b);">
    <h2 style="color: black; font-family: Verdana; margin-bottom: 20px;">首页</h2>
    <div
        style="margin-bottom: 40px; padding: 10px; border: 2px solid #4b9f6e; border-radius: 10px;background-color: white">
      <h3>用户信息</h3>
      <div class="item-grid">
        <div v-for="item in userData" :key="item.id" class="item-card" style="background-color: white">
          <img :src="item.image || defaultImage" alt="封面图片" class="item-image"/>
          <div class="item-name">{{ item.name }}</div>
        </div>
      </div>
      <div style="text-align: center;">
        <el-button
            style="
    margin-top: 20px;
    width: 150px;
    padding: 10px 20px;
    background: linear-gradient(90deg, #4b9f6e, #6fdb9f);
    color: white;
    font-size: 16px;
    border-radius: 30px;
    box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
    transition: all 0.3s ease;
    text-transform: uppercase;
  "
            type="primary"
            @click="$router.push('/user/user')"
        >
          查看更多详情
        </el-button>
      </div>
    </div>
    <div
        style="margin-bottom: 40px; padding: 10px; border: 2px solid #4b9f6e; border-radius: 10px;background-color: white">
      <h3>房源信息信息</h3>
      <div class="item-grid">
        <div v-for="item in house_rentData" :key="item.id" class="item-card" style="background-color: white">
          <img :src="item.image || defaultImage" alt="封面图片" class="item-image"/>
          <div class="item-name">{{ item.title }}</div>
        </div>
      </div>
      <div style="text-align: center;">
        <el-button
            style="
    margin-top: 20px;
    width: 150px;
    padding: 10px 20px;
    background: linear-gradient(90deg, #4b9f6e, #6fdb9f);
    color: white;
    font-size: 16px;
    border-radius: 30px;
    box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
    transition: all 0.3s ease;
    text-transform: uppercase;
  "
            type="primary"
            @click="$router.push('/user/house_rent')"
        >
          查看更多详情
        </el-button>
      </div>
    </div>
    <div
        style="margin-bottom: 40px; padding: 10px; border: 2px solid #4b9f6e; border-radius: 10px;background-color: white">
      <h3>租房需求信息</h3>
      <div class="item-grid">
        <div v-for="item in house_needData" :key="item.id" class="item-card" style="background-color: white">
          <img :src="item.image || defaultImage" alt="封面图片" class="item-image"/>
          <div class="item-name">{{ item.title }}</div>
        </div>
      </div>
      <div style="text-align: center;">
        <el-button
            style="
    margin-top: 20px;
    width: 150px;
    padding: 10px 20px;
    background: linear-gradient(90deg, #4b9f6e, #6fdb9f);
    color: white;
    font-size: 16px;
    border-radius: 30px;
    box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
    transition: all 0.3s ease;
    text-transform: uppercase;
  "
            type="primary"
            @click="$router.push('/user/house_need')"
        >
          查看更多详情
        </el-button>
      </div>
    </div>
    <div
        style="margin-bottom: 40px; padding: 10px; border: 2px solid #4b9f6e; border-radius: 10px;background-color: white">
      <h3>租友社区信息</h3>
      <div class="item-grid">
        <div v-for="item in house_talkData" :key="item.id" class="item-card" style="background-color: white">
          <img :src="item.image || defaultImage" alt="封面图片" class="item-image"/>
          <div class="item-name">{{ item.title }}</div>
        </div>
      </div>
      <div style="text-align: center;">
        <el-button
            style="
    margin-top: 20px;
    width: 150px;
    padding: 10px 20px;
    background: linear-gradient(90deg, #4b9f6e, #6fdb9f);
    color: white;
    font-size: 16px;
    border-radius: 30px;
    box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
    transition: all 0.3s ease;
    text-transform: uppercase;
  "
            type="primary"
            @click="$router.push('/user/house_talk')"
        >
          查看更多详情
        </el-button>
      </div>
    </div>
  </div>
</template>

<script>
import request from "../../utils/request";

export default {
  name: "Home",
  data() {
    return {
      userData: [],
      house_rentData: [],
      house_needData: [],
      house_talkData: [],
      defaultImage: '/api/files/150', // 默认封面图片
    };
  },
  created() {
    this.loadUserData();
    this.loadHouseRentData();
    this.loadHouseNeedData();
    this.loadHouseTalkData();
  },
  methods: {
    loadUserData() {
      request.get(`/api/user`, {
        params: {
          pageNum: 1,
          pageSize: 12,
        }
      }).then((res) => {
        this.userData = res.data.records;
      });
    },
    loadHouseRentData() {
      request.get(`/api/house_rent`, {
        params: {
          pageNum: 1,
          pageSize: 12,
        }
      }).then((res) => {
        this.house_rentData = res.data.records;
      });
    },
    loadHouseNeedData() {
      request.get(`/api/house_need`, {
        params: {
          pageNum: 1,
          pageSize: 12,
        }
      }).then((res) => {
        this.house_needData = res.data.records;
      });
    },
    loadHouseTalkData() {
      request.get(`/api/house_talk`, {
        params: {
          pageNum: 1,
          pageSize: 12,
        }
      }).then((res) => {
        this.house_talkData = res.data.records;
      });
    },
  },
};
</script>

<style scoped>
.item-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(200px, 1fr));
  gap: 20px;
}

.item-card {
  border: 1px solid #ddd;
  border-radius: 8px;
  padding: 10px;
  text-align: center;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.item-image {
  width: 100%;
  height: 150px;
  object-fit: cover;
  border-radius: 4px;
  margin-bottom: 10px;
}

.item-name {
  font-size: 16px;
  font-weight: bold;
  margin-bottom: 10px;
}
</style>
