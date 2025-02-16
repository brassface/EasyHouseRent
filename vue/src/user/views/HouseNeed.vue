<template>
  <div>
    <header class="need-navbar">
      <h2>正在显示 {{ need_total }} 个租房需求中的 {{ need_tableData.length }} 个</h2>
      <div class="need-search-bar">
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
        <input type="text" v-model="need_search" placeholder="输入关键词" class="need-search-input" />
        <button class="need-search-button" @click="need_load">搜索</button>
        <button class="need-clear-button" @click="this.need_search=''">清空</button>
      </div>
    </header>
    <main>
      <div class="need-listings">
        <div class="need-listing"
             v-for="item in need_tableData"
             :key="item.id"
             @click="need_showDetails(item)"
             style="cursor: pointer;">
          <div class="need-image-container" style="position: relative;">
            <img class="need-listing-image" :src="item.pictures[0] || need_defaultImage" alt="图片" />
          </div>
          <div class="need-listing-info">
            <div class="need-info-top">
              <h3>{{ item.province + ' ' + item.city + ' ' + item.town }}</h3>
            </div>
            <div class="need-info-bottom">
              <div class="need-info-left">
                <p>最高 {{ item.price }} 元/月</p>
                <p>最低面积 {{ item.area }} 平米</p>
              </div>
              <div class="need-user-info">
                <img class="need-user-avatar" :src="item.image || need_defaultImage" alt="用户头像" />
                <div class="need-user-details">
                  <div class="need-user-name">{{ item.name.length > 15 ? item.name.slice(0, 12) + '...' : item.name }}</div>
                  <div class="need-user-phone">{{ item.phone }}</div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </main>

    <div v-if="need_vis" class="need-modal-overlay" @click="need_closeDetails">
      <div class="need-modal" @click.stop>
        <div class="need-modal-header">
          <h1>{{ need_form.title }}</h1>
          <button class="need-close-button" @click="need_closeDetails">&times;</button>
        </div>
        <div class="need-modal-body">
          <div class="need-details">
            <div class="need-details-images">
              <div v-for="(pic, index) in need_form.pictures.slice(0, 12)" :key="index" class="need-image-wrapper">
                <img v-if="pic && pic.length" :src="pic" alt="房源图片" class="need-details-image" />
              </div>
            </div>
            <h1>{{ need_form.province }} {{ need_form.city }} {{ need_form.town }}</h1>
            <h2 style="margin-top: 10px">
              <strong>最高 ￥{{ need_form.price }} / 月</strong>
              ·最低 {{ need_form.area }} 平米
            </h2>
            <div style="margin-top: 10px">
              <strong>标签：</strong>
              <span v-for="(tip, index) in need_form.tips" :key="index" class="need-tip">{{ tip }}</span>
            </div>
            <div class="need-user-info">
              <img class="need-user-avatar" :src="need_form.image || need_defaultImage" alt="用户头像" />
              <div class="need-user-details">
                <div class="need-user-name">{{ need_form.name.length > 15 ? need_form.name.slice(0, 12) + '...' : need_form.name }}</div>
                <div class="need-user-phone">{{ need_form.phone }}</div>
              </div>
            </div>
            <p class="need-content">{{ need_form.content }}</p>
          </div>
        </div>
      </div>
    </div>
    <el-pagination
        background
        @current-change="need_handleCurrentChange"
        :current-page="need_currentPage"
        :page-size="8"
        layout="prev, pager, next, jumper"
        :total="need_total"
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
      need_currentPage: 1,
      need_pageSize: 8,
      need_search: '',
      need_total: 0,
      need_tableData: [],
      need_defaultImage: '/api/files/150',
      need_vis: false,
      need_form: {},
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
    window.addEventListener("keydown", this.need_handleKeyPress);
  },
  beforeDestroy() {
    window.removeEventListener("keydown", this.need_handleKeyPress);
    document.body.style.overflow = "";
  },
  created() {
    this.need_load();
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
    need_load() {
      request.get(`/api/house_need`, {
        params: {
          pageNum: this.need_currentPage,
          pageSize: this.need_pageSize,
          search: this.need_search,
          province: this.form.province,
          city: this.form.city,
          town: this.form.town
        },
      }).then((res) => {
        if (res.code==='0') {
          this.need_tableData = res.data.records;
          this.need_total = res.data.total;
        }else {
          this.$message.warning(res.msg);
        }
      });
    },
    need_showDetails(item) {
      this.need_form = item;
      this.need_vis = true;
      document.body.style.overflow = "hidden";
    },
    need_handleCurrentChange(pageNum) {
      this.need_currentPage = pageNum;
      this.need_load();
    },
    need_closeDetails() {
      this.need_vis = false;
      document.body.style.overflow = ""; // 恢复背景滚动
    },
    need_handleKeyPress(event) {
      if (event.key === "Escape") {
        this.need_closeDetails();
      }
    },
  },
};
</script>

<style scoped>
.need-navbar {
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

.need-search-bar {
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

.need-search-input {
  padding: 10px;
  border: 1px solid #ddd;
  border-radius: 5px;
}

.need-clear-button {
  padding: 10px 20px;
  background-color: #7cc400;
  color: white;
  border: none;
  border-radius: 5px;
  cursor: pointer;
}

.need-search-button {
  padding: 10px 20px;
  background-color: #ff5a5f;
  color: white;
  border: none;
  border-radius: 5px;
  cursor: pointer;
}

.need-listings {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
  gap: 20px;
  padding: 20px;
}

.need-listing {
  border: 1px solid #ddd;
  border-radius: 8px;
  overflow: hidden;
  background-color: white;
  box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
  transition: transform 0.3s ease, box-shadow 0.3s ease;
}

.need-listing:hover {
  transform: translateY(-5px);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.2);
}

.need-listing-image {
  width: 100%;
  height: 200px;
  object-fit: cover;
  transition: transform 0.3s ease-in-out;
}

.need-listing-info {
  padding: 15px;
  text-align: left;
}

.need-listing:hover .need-listing-image {
  transform: scale(1.05);
}

.need-info-top {
  margin-bottom: 10px;
}

.need-info-bottom {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
}

.need-info-left {
  font-size: 14px;
  color: #555;
  margin-top: 5px;
}

.need-info-left p {
  margin: 5px 0;
}

.need-user-info {
  display: flex;
  align-items: center;
  margin-top: 10px;
}

.need-user-avatar {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  object-fit: cover;
  margin-right: 10px;
}

.need-user-details {
  text-align: left;
}

.need-user-name {
  font-size: 16px;
  font-weight: bold;
}

.need-user-phone {
  font-size: 14px;
}

.need-listings {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
  gap: 20px;
  padding: 20px;
}

.need-listing {
  border: 1px solid #ddd;
  border-radius: 8px;
  overflow: hidden;
  background-color: white;
  box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
}

.need-listing-image {
  width: 100%;
  height: 200px;
  object-fit: cover;
}

.need-footer-links a {
  margin: 0 10px;
  color: #007bff;
  text-decoration: none;
}

.need-footer-links a:hover {
  text-decoration: underline;
}

.need-modal-overlay {
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

.need-modal {
  background: white;
  border-radius: 12px;
  width: 90%;
  max-height: 90%;
  height: auto;
  overflow-y: auto;
  box-shadow: 0 4px 10px rgba(0, 0, 0, 0.2);
  padding: 20px;
}

.need-modal-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.need-modal-header h2 {
  font-size: 18px;
  margin: 0;
}

.need-close-button {
  background: none;
  border: none;
  font-size: 24px;
  cursor: pointer;
}

.need-modal-body {
  font-size: 14px;
  color: #333;
  margin-top: 20px;
}

.need-details-images {
  display: flex;
  flex-wrap: wrap;
  gap: 10px;
  margin: 15px 0;
}

.need-image-wrapper {
  flex: 0 0 calc(33.33% - 10px);
  box-sizing: border-box;
}

.need-details-image {
  width: 100%;
  height: auto;
  border-radius: 8px;
  object-fit: cover;
}

.need-content {
  margin: 15px 0;
  line-height: 1.6;
}

.need-tip {
  display: inline-block;
  margin-right: 8px;
  padding: 5px 10px;
  background: #f5f5f5;
  border-radius: 4px;
  font-size: 12px;
  color: #555;
}
</style>