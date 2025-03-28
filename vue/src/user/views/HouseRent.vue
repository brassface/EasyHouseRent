<template>
  <div style="height: 80px"></div>
  <div>
    <header class="rent-navbar">
      <h2>正在显示 {{ rent_total }} 个房源信息中的 {{ rent_tableData.length }} 个</h2>

      <div class="rent-search-bar">
        <div class="search-dropdowns">
          <select v-model="form.province" class="search-dropdown">
            <option value="" disabled>选择省份</option>
            <option value="">全部</option>
            <option v-for="province in staticPlace" :key="province.name" :value="province.name">
              {{ province.name }}
            </option>
          </select>

          <select v-model="form.city" :disabled="cities.length === 0" class="search-dropdown">
            <option value="" disabled>选择城市</option>
            <option value="">全部</option>
            <option v-for="city in cities" :key="city.name" :value="city.name">
              {{ city.name }}
            </option>
          </select>

          <select v-model="form.town" :disabled="towns.length === 0" class="search-dropdown">
            <option value="" disabled>选择区/县</option>
            <option value="">全部</option>
            <option v-for="town in towns" :key="town" :value="town">
              {{ town }}
            </option>
          </select>
        </div>

        <input type="text" v-model="rent_search" placeholder="输入关键词" class="rent-search-input" />
        <button class="rent-search-button" @click="rent_load">搜索</button>
        <button class="rent-clear-button" @click="this.rent_search=''">清空</button>
      </div>
    </header>
    <main>
      <div class="rent-listings">
        <div class="rent-listing"
             v-for="item in rent_tableData"
             :key="item.id"
             @click="rent_showDetails(item)"
             style="cursor: pointer;">
          <div class="rent-image-container" style="position: relative;">
            <img class="rent-listing-image" :src="item.pictures[0] || defaultImage" alt="图片" />
          </div>
          <div class="rent-listing-info">
            <div class="rent-info-top">
              <h3>{{ item.province + ' ' + item.city + ' ' + item.town }}</h3>
            </div>
            <div class="rent-info-bottom">
              <div class="rent-info-left">
                <p>{{ item.price }} 元/月</p>
                <p>面积 {{ item.area }} 平米</p>
              </div>
              <div class="rent-user-info">
                <img class="rent-user-avatar" :src="item.image || defaultImage" alt="用户头像" />
                <div class="rent-user-details">
                  <div class="rent-user-name">{{ item.name.length > 15 ? item.name.slice(0, 12) + '...' : item.name }}</div>
                  <div class="rent-user-phone">{{ item.phone }}</div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </main>

    <div v-if="rent_vis" class="rent-modal-overlay" @click="rent_closeDetails">
      <div class="rent-modal" @click.stop>
        <div class="rent-modal-header">
          <h1>{{ rent_form.title }}</h1>
          <button class="rent-close-button" @click="rent_closeDetails">&times;</button>
        </div>
        <div class="rent-modal-body">
          <div class="rent-details">
            <div class="rent-details-images">
              <div v-for="(pic, index) in rent_form.pictures.slice(0, 12)" :key="index" class="rent-image-wrapper">
                <img v-if="pic && pic.length" :src="pic" alt="房源图片" class="rent-details-image" />
              </div>
            </div>
            <h1>{{ rent_form.province }} {{ rent_form.city }} {{ rent_form.town }}</h1>
            <h2 style="margin-top: 10px">
              <strong>￥{{ rent_form.price }} / 月</strong>
              · {{ rent_form.area }} 平米
            </h2>
            <div style="margin-top: 10px">
              <strong>标签：</strong>
              <span v-for="(tip, index) in rent_form.tips" :key="index" class="rent-tip">{{ tip }}</span>
            </div>
            <div class="rent-user-info">
              <img class="rent-user-avatar" :src="rent_form.image || defaultImage" alt="用户头像" />
              <div class="rent-user-details">
                <div class="rent-user-name">{{ rent_form.name.length > 15 ? rent_form.name.slice(0, 12) + '...' : rent_form.name }}</div>
                <div class="rent-user-phone">{{ rent_form.phone }}</div>
              </div>
            </div>
            <p class="rent-content">{{ rent_form.content }}</p>
          </div>
        </div>
      </div>
    </div>
    <el-pagination
        background
        @current-change="rent_handleCurrentChange"
        :current-page="rent_currentPage"
        :page-size="8"
        layout="prev, pager, next, jumper"
        :total="rent_total"
        style="margin-top: 10px; margin-bottom: 40px; display: flex; justify-content: center;">
    </el-pagination>
  </div>
</template>

<script>
import request from "@/utils/request";
import {chinaCities} from "@/utils/chinaCities";
export default {
  data() {
    return {
      rent_currentPage: 1,
      rent_pageSize: 8,
      rent_search: '',
      rent_total: 0,
      rent_tableData: [],
      defaultImage: '/api/files/150',
      rent_vis: false,
      rent_form: {},
      staticPlace: chinaCities,
      cities: [],
      towns: [],
      form: {
        province:'',
        city:'',
        town:''
      }
    };
  },
  mounted() {
    window.addEventListener("keydown", this.rent_handleKeyPress);
  },
  beforeDestroy() {
    window.removeEventListener("keydown", this.rent_handleKeyPress);
    document.body.style.overflow = "";
  },
  created() {
    this.rent_load();
  },
  watch: {
    'form.province'(newVal) {
      const selectedProvince = this.staticPlace.find((p) => p.name === newVal);
      this.cities = selectedProvince ? selectedProvince.city : [];
      this.form.city = '';
      this.towns = [];
      this.form.town = '';
    },
    'form.city'(newVal) {
      const selectedCity = this.cities.find((c) => c.name === newVal);
      this.towns = selectedCity ? selectedCity.town : [];
      this.form.town = '';
    },
  },
  methods: {
    rent_load() {
      request.get(`/api/house_rent`, {
        params: {
          pageNum: this.rent_currentPage,
          pageSize: this.rent_pageSize,
          search: this.rent_search,
          province: this.form.province,
          city: this.form.city,
          town: this.form.town
        },
      }).then((res) => {
        if (res.code==='0') {
          this.rent_tableData = res.data.records;
          this.rent_total = res.data.total;
        }else {
          this.$message.warning(res.msg);
        }
      });
    },
    rent_showDetails(item) {
      this.rent_form = item;
      this.rent_vis = true;
      document.body.style.overflow = "hidden";
    },
    rent_handleCurrentChange(pageNum) {
      this.rent_currentPage = pageNum;
      this.rent_load();
    },
    rent_closeDetails() {
      this.rent_vis = false;
      document.body.style.overflow = ""; // 恢复背景滚动
    },
    rent_handleKeyPress(event) {
      if (event.key === "Escape") {
        this.rent_closeDetails();
      }
    },
  },
};
</script>


<style scoped>
.rent-navbar {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 20px;
  background-color: white;
  box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
  position: sticky;
  top: 0;
  z-index: 1000;
}
.rent-search-bar {
  display: flex;
  gap: 10px;
}

.search-dropdowns {
  display: flex;
  gap: 10px;
}

.search-dropdown {
  padding: 5px 10px;
  border-radius: 4px;
  border: 1px solid #ccc;
}

.rent-search-input {
  padding: 10px;
  border: 1px solid #ddd;
  border-radius: 5px;
}
.rent-clear-button {
  padding: 10px 20px;
  background-color: #7cc400;
  color: white;
  border: none;
  border-radius: 5px;
  cursor: pointer;
}

.rent-search-button {
  padding: 10px 20px;
  background-color: #ff5a5f;
  color: white;
  border: none;
  border-radius: 5px;
  cursor: pointer;
}

.rent-listing {
  margin-bottom: 20px;
}

.rent-listing-image {
  width: 100%;
  height: auto;
  border-radius: 8px;
  object-fit: cover;
}

.rent-listing-info {
  padding: 15px;
  text-align: left;
}

.rent-info-top {
  margin-bottom: 10px;
}

.rent-info-bottom {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
}

.rent-info-left {
  font-size: 14px;
  color: #555;
  margin-top: 5px;
}

.rent-info-left p {
  margin: 5px 0;
}

.rent-user-info {
  display: flex;
  align-items: center;
  margin-top: 10px;
}

.rent-user-avatar {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  object-fit: cover;
  margin-right: 10px;
}

.rent-user-details {
  text-align: left;
}

.rent-user-name {
  font-size: 16px;
  font-weight: bold;
}

.rent-user-phone {
  font-size: 14px;
}

.rent-listings {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
  gap: 20px;
  padding: 20px;
}
.rent-listing {
  border: 1px solid #ddd;
  border-radius: 8px;
  overflow: hidden;
  background-color: white;
  box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
  transition: transform 0.3s ease, box-shadow 0.3s ease;
}

.rent-listing:hover {
  transform: translateY(-5px);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.2);
}

.rent-listing-image {
  width: 100%;
  height: 200px;
  object-fit: cover;
  transition: transform 0.3s ease-in-out;
}

.rent-listing:hover .rent-listing-image {
  transform: scale(1.05);
}

.footer-links a {
  margin: 0 10px;
  color: #007bff;
  text-decoration: none;
}
.footer-links a:hover {
  text-decoration: underline;
}

.rent-modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: rgba(0, 0, 0, 0.5);
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 1000;
}

.rent-modal {
  background: white;
  border-radius: 12px;
  width: 90%;
  max-height: 90%;
  height: auto;
  overflow-y: auto;
  box-shadow: 0 4px 10px rgba(0, 0, 0, 0.2);
  padding: 20px;
}

.rent-modal-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.rent-modal-header h2 {
  font-size: 18px;
  margin: 0;
}

.rent-close-button {
  background: none;
  border: none;
  font-size: 24px;
  cursor: pointer;
}

.rent-modal-body {
  font-size: 14px;
  color: #333;
  margin-top: 20px;
}

.rent-details-images {
  display: flex;
  flex-wrap: wrap;
  gap: 10px;
  margin: 15px 0;
}

.rent-image-wrapper {
  flex: 0 0 calc(33.33% - 10px);
  box-sizing: border-box;
}

.rent-details-image {
  width: 100%;
  height: auto;
  border-radius: 8px;
  object-fit: cover;
}

.rent-content {
  margin: 15px 0;
  line-height: 1.6;
}

.rent-tip {
  display: inline-block;
  margin-right: 8px;
  padding: 5px 10px;
  background: #f5f5f5;
  border-radius: 4px;
  font-size: 12px;
  color: #555;
}
</style>