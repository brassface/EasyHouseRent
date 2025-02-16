<template>
  <div style="height: 80px"></div>
  <div>
    <div class="section-header">
      <h4 class="section-title">
        <i class="el-icon-house" style="font-size: 24px; margin-right: 8px;"></i>
        房源信息
      </h4>
    </div>
    <main>
      <div class="listings">
        <div v-for="item in rent_tableData" :key="item.id" class="listing" style="cursor: pointer;">
          <div class="image-container">
            <img :src="item.pictures[0] || defaultImage" alt="图片" class="listing-image"/>
          </div>
          <div class="listing-info">
            <div class="info-top">
              <h3>{{ item.province + ' ' + item.city + ' ' + item.town }}</h3>
            </div>
            <div class="info-bottom">
              <div class="info-left">
                <p>{{ item.price }} 元/月</p>
                <p>面积 {{ item.area }} 平米</p>
              </div>
              <div class="user-info">
                <img :src="item.image || defaultImage" alt="用户头像" class="user-avatar"/>
                <div class="user-details">
                  <div class="user-name">{{ item.name.length > 15 ? item.name.slice(0, 12) + '...' : item.name }}</div>
                  <div class="user-phone">{{ item.phone }}</div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
      <div class="view-more">
        <button @click="viewMore('house_rent')">查看更多</button>
      </div>
    </main>
  </div>

  <div>
    <div class="section-header">
      <h4 class="section-title">
        <i class="el-icon-key" style="font-size: 24px; margin-right: 8px;"></i>
        租房需求
      </h4>
    </div>
    <main>
      <div class="listings">
        <div v-for="item in need_tableData" :key="item.id" class="listing" style="cursor: pointer;">
          <div class="image-container">
            <img :src="item.pictures[0] || defaultImage" alt="图片" class="listing-image"/>
          </div>
          <div class="listing-info">
            <div class="info-top">
              <h3>{{ item.province + ' ' + item.city + ' ' + item.town }}</h3>
            </div>
            <div class="info-bottom">
              <div class="info-left">
                <p>最高 {{ item.price }} 元/月</p>
                <p>最低面积 {{ item.area }} 平米</p>
              </div>
              <div class="user-info">
                <img :src="item.image || defaultImage" alt="用户头像" class="user-avatar"/>
                <div class="user-details">
                  <div class="user-name">{{ item.name.length > 15 ? item.name.slice(0, 12) + '...' : item.name }}</div>
                  <div class="user-phone">{{ item.phone }}</div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
      <div class="view-more">
        <button @click="viewMore('house_need')">查看更多</button>
      </div>
    </main>
  </div>

  <div>
    <div class="section-header" style="margin-bottom: 0">
      <h4 class="section-title">
        <i class="el-icon-user" style="font-size: 24px; margin-right: 8px;"></i>
        租友帖子
      </h4>
    </div>
    <div class="talk-item-list">
      <div v-for="item in talk_tableData" :key="item.id" class="talk-item-card">
        <div class="talk-item-left">
          <img :src="item.pictures[0] || defaultImage" alt="封面图片" class="talk-item-image"/>
        </div>
        <div class="talk-item-middle">
          <div class="talk-item-name">{{ item.title }}</div>
          <div style="margin-top: 10px">
            <strong>标签：</strong>
            <span v-for="(tip, index) in item.tips" :key="index" class="talk-tip">{{ tip }}</span>
          </div>
          <div class="talk-item-user">
            <img :src="item.image || defaultImage" alt="头像" class="talk-user-avatar"/>
            <span class="talk-user-name">{{ item.name.length > 55 ? item.name.slice(0, 52) + '...' : item.name }}</span>
          </div>
        </div>
      </div>
    </div>
    <div class="view-more">
      <button @click="viewMore('house_talk')">查看更多</button>
    </div>
  </div>
  <div style="height: 50px"></div>
</template>

<style scoped>
.section-header {
  display: flex;
  align-items: center;
  padding-left: 30px;
  margin-top: 10px;
  margin-bottom: -20px;
}

.section-title {
  font-size: 28px;
  font-weight: bold;
  color: #8a33cf;
  position: relative;
  display: flex;
  align-items: center;
}

h4 {
  font-size: 24px;
  color: #333;
  margin-bottom: 20px;
}

.listings {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
  gap: 20px;
  padding: 20px;
}

.listing {
  border: 1px solid #ddd;
  border-radius: 8px;
  overflow: hidden;
  background-color: #fff;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  transition: box-shadow 0.3s ease-in-out;
}

.listing:hover {
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.2);
}

.listing-image {
  width: 100%;
  height: 200px;
  border-radius: 8px;
  object-fit: cover;
  transition: transform 0.3s ease;
}

.listing-info {
  padding: 15px;
  text-align: left;
}

.info-top h3 {
  font-size: 20px;
  color: #333;
  font-weight: bold;
}

.info-bottom {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  margin-top: 10px;
}

.info-left p {
  font-size: 14px;
  color: #555;
}

.user-info {
  display: flex;
  align-items: center;
}

.user-avatar {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  object-fit: cover;
  margin-right: 10px;
}

.user-name {
  font-size: 16px;
  font-weight: bold;
}

.user-phone {
  font-size: 14px;
  color: #777;
}

.talk-item-list {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.talk-item-card {
  display: flex;
  align-items: center;
  border: 1px solid #ddd;
  border-radius: 8px;
  padding: 10px;
  background-color: #fff;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  transition: box-shadow 0.3s ease-in-out;
}

.talk-item-card:hover {
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.2);
}

.talk-item-left {
  flex: 0 0 120px;
  height: 120px;
  width: 120px;
}

.talk-item-image {
  width: 100%;
  height: 100%;
  border-radius: 8px;
  object-fit: cover;
}

.talk-item-middle {
  flex: 1;
  padding-left: 20px;
}

.talk-item-name {
  font-size: 18px;
  font-weight: bold;
  margin-bottom: 10px;
}

.talk-item-user {
  display: flex;
  align-items: center;
}

.talk-user-avatar {
  width: 50px;
  height: 50px;
  border-radius: 50%;
  margin-right: 10px;
}

.talk-user-name {
  font-size: 14px;
  color: #333;
}

.talk-tip {
  display: inline-block;
  margin-right: 8px;
  padding: 5px 10px;
  background: #f0f0f0;
  border-radius: 4px;
  font-size: 12px;
  color: #555;
}

.view-more {
  text-align: center;
  margin-top: 20px;
}

.view-more button {
  padding: 10px 20px;
  background-color: #007bff;
  color: white;
  border: none;
  border-radius: 5px;
  cursor: pointer;
  font-size: 16px;
  transition: background-color 0.3s ease;
}

.view-more button:hover {
  background-color: #0056b3;
}

.listing, .talk-item-card {
  transition: transform 0.3s ease, box-shadow 0.3s ease;
}

.listing:hover, .talk-item-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 4px 10px rgba(0, 0, 0, 0.2);
}

.listing-image, .talk-item-image {
  transition: transform 0.3s ease-in-out;
}

.listing:hover .listing-image, .talk-item-card:hover .talk-item-image {
  transform: scale(1.05);
}
</style>


<script>
import request from "@/utils/request";
export default {
  data() {
    return {
      user: JSON.parse(sessionStorage.getItem('user')) || {},
      defaultImage: '/api/files/150',
      rent_tableData: [],
      need_tableData: [],
      talk_tableData: [],
    };
  },
  created() {
    this.refreshData();
  },
  methods: {
    viewMore(section) {
      this.$router.push('/user/'+section);
    },
    rent_load() {
      request.get(`/api/house_rent`, {
        params: {
          pageSize: 4,
        },
      }).then((res) => {
        this.rent_tableData = res.data.records;
        this.rent_total = res.data.total;
      });
    },
    need_load() {
      request.get(`/api/house_need`, {
        params: {
          pageSize: 4,
        },
      }).then((res) => {
        this.need_tableData = res.data.records;
        this.need_total = res.data.total;
      });
    },
    talk_load() {
      request.get(`/api/house_talk`, {
        params: {
          pageSize: 4,
        },
      }).then((res) => {
        this.talk_tableData = res.data.records;
        this.talk_total = res.data.total;
      });
    },
    refreshData() {
      this.rent_load();
      this.need_load();
      this.talk_load();
      this.scrollToTop();
    },
    scrollToTop() {
      window.scrollTo({top: 0, behavior: 'smooth'});
    },
  }
};
</script>