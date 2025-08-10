<template>
  <div style="padding-left: 10px; padding-right: 10px; width: calc(100vw - 200px)">
    <h2 style="color: #7bc3f6; font-family: Georgia; margin-bottom: 20px;">
      房源信息管理
    </h2>
    <div class="search-bar">
      <div class="search-dropdowns">
        <select v-model="form.province" class="search-dropdown">
          <option disabled value="">全部</option>
          <option v-for="province in staticPlace" :key="province.name" :value="province.name">
            {{ province.name }}
          </option>
        </select>

        <select v-model="form.city" :disabled="cities.length === 0" class="search-dropdown">
          <option disabled value="">全部</option>
          <option v-for="city in cities" :key="city.name" :value="city.name">
            {{ city.name }}
          </option>
        </select>

        <select v-model="form.town" :disabled="towns.length === 0" class="search-dropdown">
          <option disabled value="">全部</option>
          <option v-for="town in towns" :key="town" :value="town">
            {{ town }}
          </option>
        </select>
      </div>

      <input v-model="search" class="search-input" placeholder="输入关键词" type="text"/>
      <button class="search-button" @click="load">搜索</button>
      <button class="clear-button" @click="this.search=this.form.province=this.form.city=this.form.town=''">清空
      </button>
      <button class="add-button" @click="add">新增</button>
    </div>
    <el-table :data="tableData" border stripe style="width: 100%;margin-top: 10px">
      <el-table-column label="标题" prop="title">
        <template #default="scope">
          <div>{{ scope.row.title }}</div>
        </template>
      </el-table-column>
      <el-table-column label="图片" prop="pictures">
        <template #default="scope">
          <div style="width: 100px; height: 80px; overflow: hidden;">
            <img :src="scope.row.pictures[0] || defaultImage" alt="房源图片" style="width: 100%; height: auto;"/>
          </div>
        </template>
      </el-table-column>
      <el-table-column label="位置" prop="location">
        <template #default="scope">
          <div>{{ scope.row.province + ' ' + scope.row.city + ' ' + scope.row.town }}</div>
        </template>
      </el-table-column>
      <el-table-column label="价格" prop="price">
        <template #default="scope">
          <div>{{ scope.row.price }} 元/月</div>
        </template>
      </el-table-column>
      <el-table-column label="面积" prop="area">
        <template #default="scope">
          <div>{{ scope.row.area }} 平米</div>
        </template>
      </el-table-column>
      <el-table-column fixed="right" label="操作">
        <template #default="scope">
          <div style="display: flex; align-content: space-between">
            <el-button type="primary" @click="details(scope.row)">详情</el-button>
            <el-button v-if="user.role === 1" type="primary" @click="handleEdit(scope.row)">编辑</el-button>
            <el-popconfirm
                v-if="user.role === 1" title="确定删除吗？" @confirm="handleDelete(scope.row.id)">
              <template #reference>
                <el-button type="danger">删除</el-button>
              </template>
            </el-popconfirm>
          </div>
        </template>
      </el-table-column>
    </el-table>
    <el-pagination
        :curpage="currentPage"
        :page-size="pageSize"
        :page-sizes="[5, 10, 20]"
        :total="total"
        background
        layout="total, sizes, prev, pager, next, jumper"
        @current-change="handleCurrentChange"
        @size-change="handleSizeChange">
    </el-pagination>
    <div v-if="showModal" class="push-modal-overlay">
      <div class="modal-content">
        <h3>输入房源信息</h3>
        <form @submit.prevent="handleSubmit">
          <div class="form-group">
            <label for="title">标题:</label>
            <input
                id="title"
                v-model="form.title"
                placeholder="请输入标题"
                required
                type="text"
            />
          </div>
          <div class="form-group">
            <label for="content">房源描述:</label>
            <textarea
                id="content"
                v-model="form.content"
                placeholder="请输入房源描述"
                required
                rows="8"
                style="width: 100%;max-width: 100%;min-width: 100%"
            ></textarea>
          </div>
          <div class="form-group">
            <label for="price">价格 (元/月):</label>
            <input
                id="price"
                v-model="form.price"
                placeholder="请输入价格"
                required
                step="0.01"
                type="number"
            />
          </div>
          <div class="form-group">
            <label for="province">省份:</label>
            <select
                id="province"
                v-model="form.province"
                class="form-control"
                required
            >
              <option disabled value="">请选择省份</option>
              <option v-for="province in staticPlace" :key="province.name" :value="province.name">
                {{ province.name }}
              </option>
            </select>
          </div>

          <div v-if="cities.length > 0" class="form-group">
            <label for="city">城市:</label>
            <select
                id="city"
                v-model="form.city"
                class="form-control"
                required
            >
              <option disabled value="">请选择城市</option>
              <option v-for="city in cities" :key="city.name" :value="city.name">
                {{ city.name }}
              </option>
            </select>
          </div>

          <div v-if="towns.length > 0" class="form-group">
            <label for="town">城区:</label>
            <select
                id="town"
                v-model="form.town"
                class="form-control"
                required
            >
              <option disabled value="">请选择城区</option>
              <option v-for="town in towns" :key="town" :value="town">
                {{ town }}
              </option>
            </select>
          </div>
          <div class="form-group">
            <label for="area">面积 (平方米):</label>
            <input
                id="area"
                v-model="form.area"
                placeholder="请输入面积"
                required
                step="0.01"
                type="number"
            />
          </div>
          <div class="form-group">
            <label>标签:</label>
            <div v-for="(tip, index) in form.tips" :key="index" class="tag-input-group">
              <div class="tag-input-wrapper">
                <input v-model="form.tips[index]" placeholder="请输入标签" type="text"/>
                <button class="remove-tag-button" type="button" @click="removeTip(index)">
                  <i class="fa fa-times"></i>X
                </button>
              </div>
            </div>
            <button
                :disabled="!canAddTag"
                class="add-tag-button"
                type="button"
                @click="addTip"
            >
              <i class="fa fa-plus"></i> 添加标签
            </button>
          </div>
          <div class="form-group">
            <label>图片上传:</label>
            <input multiple type="file" @change="handleFileUpload"/>
            <div class="image-preview">
              <div v-for="(picture, index) in form.pictures" class="image-container">
                <img
                    :key="index"
                    :src="picture"
                    alt="预览图片"
                    class="preview-image"
                />
                <button class="remove-image-button" type="button" @click="removePicture(index)">
                  <i class="fa fa-times"></i>X
                </button>
              </div>
            </div>
          </div>
          <div class="form-actions">
            <button class="submit-button" type="submit">提交</button>
            <button class="cancel-button" type="button" @click="closeModal">取消</button>
          </div>
        </form>
      </div>
    </div>

    <el-dialog
        v-model="vis"
        title="房源详情"
        width="60%">
      <div class="modal-header">
        <h1>{{ form.title }}</h1>
      </div>
      <div class="modal-body">
        <div class="details">
          <div class="details-images">
            <div v-for="(pic, index) in form.pictures.slice(0, 12)" :key="index" class="image-wrapper">
              <img v-if="pic && pic.length" :src="pic" alt="房源图片" class="details-image"/>
            </div>
          </div>
          <h1>{{ form.province }} {{ form.city }} {{ form.town }}</h1>
          <h2 style="margin-top: 10px">
            <strong>￥{{ form.price }} / 月</strong>
            · {{ form.area }} 平米
          </h2>
          <div style="margin-top: 10px">
            <strong>标签：</strong>
            <span v-for="(tip, index) in form.tips" :key="index" class="tip">{{ tip }}</span>
          </div>
          <div class="user-info">
            <img :src="form.image || defaultImage" alt="用户头像" class="user-avatar"/>
            <div class="user-details">
              <div class="user-name">{{ form.name.length > 15 ? form.name.slice(0, 12) + '...' : form.name }}</div>
              <div class="user-phone">{{ form.phone }}</div>
            </div>
          </div>
          <p class="content">{{ form.content }}</p>
        </div>
      </div>
      <template #footer>
        <span class="dialog-footer">
            <el-button @click="vis = false">取消</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>


<script>
import request from "../../utils/request";
import {chinaCities} from "@/utils/chinaCities";

export default {
  name: 'HouseRent',
  data() {
    return {
      search: '',
      tableData: [],
      total: 0,
      currentPage: 1,
      pageSize: 10,
      form: {
        province: '',
        city: '',
        town: ''
      },
      user: {},
      detail: {},
      vis: false,
      showModal: false,
      staticPlace: chinaCities,
      cities: [],
      towns: [],
      defaultImage: '/api/files/150',
    }
  },
  created() {
    const userStr = sessionStorage.getItem("user") || '{}';
    this.user = JSON.parse(userStr);
    this.load();
  },
  computed: {
    isAreaValid() {
      return /^\d+(\.\d{1,2})?$/.test(this.form.area);
    },
    canAddTag() {
      if (this.form.tips.length === 0) {
        return true;
      }
      const lastTip = this.form.tips[this.form.tips.length - 1] || "";
      return lastTip.trim() !== "";
    },
  },
  mounted() {
    window.addEventListener("keydown", this.handleKeyPress);
  },
  beforeDestroy() {
    window.removeEventListener("keydown", this.handleKeyPress);
    document.body.style.overflow = "";
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
    load() {
      request.get(`/api/house_rent`, {
        params: {
          pageNum: this.currentPage,
          pageSize: this.pageSize,
          search: this.search,
          province: this.form.province,
          city: this.form.city,
          town: this.form.town
        }
      }).then(res => {
        if (res.code === '0') {
          this.tableData = res.data.records;
          this.total = res.data.total;
        } else {
          this.$message.error(res.msg);
        }
      });
    },
    add() {
      this.form = {
        tips: [],
        pictures: []
      };
      this.showModal = true;
      document.body.style.overflow = "hidden";
    },
    handleEdit(row) {
      this.form = JSON.parse((JSON.stringify(row)));
      this.showModal = true;
      document.body.style.overflow = "hidden";
    },
    handleDelete(id) {
      request.delete(`/api/house_rent/` + id).then(res => {
        if (res.code === '0') {
          this.$message.success("删除成功");
          this.load();
        } else {
          this.$message.error(res.msg);
        }
      });
    },
    handleSizeChange(pageSize) {
      this.pageSize = pageSize;
      this.load();
    },
    handleCurrentChange(pageNum) {
      this.currentPage = pageNum;
      this.load();
    },
    details(row) {
      this.form = row;
      this.vis = true;
    },
    closeModal() {
      document.body.style.overflow = "";
      this.showModal = false;
    },
    async handleSubmit() {
      if (!this.isAreaValid) {
        alert("请输入有效的面积，最多两位小数。");
        return;
      }
      if (this.form.pictures.length === 0) {
        alert("请上传至少一张图片。");
        return;
      }
      if (this.form.id) {
        request.put("/api/house_rent", this.form).then(res => {
          if (res.code === '0') {
            this.$message({
              type: "success",
              message: "更新成功"
            })
          } else {
            this.$message({
              type: "error",
              message: res.msg
            })
          }
          this.load();
          this.dialogVisible = false;
        });
      } else {
        this.form.author = this.user.id;
        this.form.name = this.user.name;
        this.form.image = this.user.image;
        this.form.phone = this.user.phone;
        request.post("/api/house_rent", this.form).then(res => {
          console.log(res);
          if (res.code === '0') {
            this.$message({
              type: "success",
              message: "新增成功"
            })
          } else {
            this.$message({
              type: "error",
              message: res.msg
            })
          }
          this.load();
          this.closeModal();
        });
      }
    },
    addTip() {
      if (this.form.tips.length > 9) {
        this.$message.error(`最多添加10条标签`);
        return;
      }
      if (this.canAddTag) {
        this.form.tips.push("");
      }
    },
    removeTip(index) {
      this.form.tips.splice(index, 1);
    },
    async handleFileUpload(event) {
      if (this.form.pictures.length > 8) {
        this.$message.warning('最多上传9张图片');
        return;
      }
      const files = Array.from(event.target.files);
      const formData = new FormData();
      for (const file of files) {
        formData.append('file', file);

        try {
          const res = await request.post('/api/files/upload', formData, {
            headers: {
              'Content-Type': 'multipart/form-data',
            },
          });
          if (res.code === '0') {
            this.form.pictures.push(res.data);
            this.$message.success('图片上传成功');
          } else {
            this.$message.error(`图片上传失败: ${res.message}`);
          }
        } catch (error) {
          console.error('图片上传异常:', error);
          this.$message.error('图片上传失败，请重试');
        }
        formData.delete('file');
      }
    },
    removePicture(index) {
      this.form.pictures.splice(index, 1);
    },
    handleKeyPress(event) {
      if (event.key === "Escape") {
        this.showModal = false;
      }
    },
  }
}
</script>

<style scoped>

.modal-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.modal-header h2 {
  font-size: 18px;
  margin: 0;
}

.modal-body {
  font-size: 14px;
  color: #333;
  margin-top: 20px;
}

.details-images {
  display: flex;
  flex-wrap: wrap;
  gap: 10px;
  margin: 15px 0;
}

.image-wrapper {
  flex: 0 0 calc(33.33% - 10px);
  box-sizing: border-box;
}

.details-image {
  width: 100%;
  height: auto;
  border-radius: 8px;
  object-fit: cover;
}

.content {
  margin: 15px 0;
  line-height: 1.6;
}

.tip {
  display: inline-block;
  margin-right: 8px;
  padding: 5px 10px;
  background: #f5f5f5;
  border-radius: 4px;
  font-size: 12px;
  color: #555;
}

.user-info {
  display: flex;
  align-items: center;
  margin-top: 10px;
}

.user-avatar {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  object-fit: cover;
  margin-right: 10px;
}

.user-details {
  text-align: left;
}

.user-name {
  font-size: 16px;
  font-weight: bold;
}

.user-phone {
  font-size: 14px;
}

.push-modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: rgba(0, 0, 0, 0.5);
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 999;
}

.modal-content {
  background-color: white;
  padding: 30px;
  max-height: 80vh;
  width: 400px;
  border-radius: 8px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
  text-align: center;
  overflow-y: auto
}

.image-preview {
  display: flex;
  flex-wrap: wrap;
  gap: 10px;
  margin-top: 10px;
}

.image-container {
  position: relative;
}

.preview-image {
  width: 100px;
  height: 100px;
  object-fit: cover;
  border-radius: 8px;
  border: 1px solid #ccc;
}

.form-group {
  margin-bottom: 20px;
  text-align: left;
}

.form-group label {
  display: block;
  font-size: 14px;
  color: #555;
  margin-bottom: 8px;
}

.form-group input {
  width: 100%;
  padding: 10px;
  font-size: 16px;
  border: 1px solid #ccc;
  border-radius: 4px;
  box-sizing: border-box;
  transition: border-color 0.3s ease;
}

.form-group input:focus {
  border-color: #007bff;
  outline: none;
}


.tag-input-group {
  display: flex;
  align-items: center;
  margin-bottom: 10px;
}

.tag-input-wrapper {
  position: relative;
  width: 100%;
}

.tag-input-wrapper input {
  width: 100%;
  padding-right: 40px; /* 为 x 按钮预留空间 */
  padding-left: 10px;
  height: 36px;
  border: 1px solid #ccc;
  border-radius: 4px;
  font-size: 14px;
  box-sizing: border-box;
}

.tag-input-wrapper .remove-tag-button {
  position: absolute;
  top: 50%;
  right: 10px;
  transform: translateY(-50%);
  background-color: #f8d7da; /* 浅红背景 */
  color: #d9534f; /* 红色字体 */
  border: none;
  border-radius: 50%;
  width: 24px;
  height: 24px;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  font-size: 14px;
  transition: all 0.3s;
}

.tag-input-wrapper .remove-tag-button:hover {
  background-color: #d9534f; /* 深红背景 */
  color: white;
}

.add-tag-button {
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 8px 12px;
  background-color: #007bff; /* 蓝色 */
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-size: 14px;
  margin-top: 10px;
}

.add-tag-button i {
  margin-right: 5px;
  font-size: 16px;
}

.add-tag-button:disabled {
  background-color: #cfe2f3; /* 浅蓝 */
  cursor: not-allowed;
}

.add-tag-button:hover:enabled {
  background-color: #0056b3; /* 深蓝 */
}


.form-control {
  width: 100%;
  padding: 8px;
  border: 1px solid #ccc;
  border-radius: 4px;
  font-size: 14px;
}

.remove-image-button {
  position: absolute;
  top: 5px;
  right: 5px;
  background-color: rgba(0, 0, 0, 0.6);
  color: white;
  border: none;
  border-radius: 50%;
  width: 20px;
  height: 20px;
  font-size: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  z-index: 10;
}

.remove-image-button:hover {
  background-color: rgba(0, 0, 0, 0.8);
}

.remove-image-button i {
  font-size: 16px;
}

.submit-button,
.cancel-button {
  padding: 12px 20px;
  font-size: 16px;
  cursor: pointer;
  border: none;
  border-radius: 4px;
  width: 48%;
}

.submit-button {
  background-color: #007bff;
  color: white;
  transition: background-color 0.3s;
}

.submit-button:hover {
  background-color: #0056b3;
}

.cancel-button {
  background-color: #ccc;
  color: white;
  transition: background-color 0.3s;
}

.cancel-button:hover {
  background-color: #999;
}

.search-bar {
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

.search-input {
  padding: 10px;
  border: 1px solid #ddd;
  border-radius: 5px;
}

.search-button {
  padding: 10px 20px;
  background-color: #ff5a5f;
  color: white;
  border: none;
  border-radius: 5px;
  cursor: pointer;
}

.clear-button {
  padding: 10px 20px;
  background-color: #7cc400;
  color: white;
  border: none;
  border-radius: 5px;
  cursor: pointer;
}

.add-button {
  padding: 10px 20px;
  background-color: #5e5ee5;
  color: white;
  border: none;
  border-radius: 5px;
  cursor: pointer;
}
</style>
