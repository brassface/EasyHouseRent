<template>
  <div style="padding-left: 10px; padding-right: 10px; width: calc(100vw - 200px)">
    <h2 style="color: #7bc3f6; font-family: Georgia; margin-bottom: 20px;">
      租友帖子管理
    </h2>
    <div class="search-bar">
      <input type="text" v-model="search" placeholder="输入关键词" class="search-input" />
      <button class="search-button" @click="load">搜索</button>
      <button class="clear-button" @click="this.search=this.form.province=this.form.city=this.form.town=''">清空</button>
      <button class="add-button" @click="add">新增</button>
    </div>

    <el-table :data="tableData" border stripe style="width: 100%;margin-top: 10px">
      <el-table-column prop="title" label="标题">
        <template #default="scope">
          <span>{{ scope.row.title }}</span>
        </template>
      </el-table-column>
      <el-table-column label="封面图片" width="120">
        <template #default="scope">
          <img
              :src="scope.row.pictures[0] || defaultImage"
              alt="封面图片"
              class="item-image"
              style="width: 100px; height: auto; object-fit: cover;"
          />
        </template>
      </el-table-column>
      <el-table-column prop="author" label="发布者id" width="100">
        <template #default="scope">
          <span>{{ scope.row.author }}</span>
        </template>
      </el-table-column>
      <el-table-column label="发布者信息">
        <template #default="scope">
          <div class="item-user">
            <img
                :src="scope.row.image || defaultAvatar"
                alt="头像"
                class="user-avatar"
                style="width: 40px; height: 40px; border-radius: 50%; margin-right: 10px;"
            />
            <span>
          {{ scope.row.name.length > 20 ? scope.row.name.slice(0, 17) + '...' : scope.row.name }}
        </span>
          </div>
        </template>
      </el-table-column>
      <el-table-column fixed="right" label="操作">
        <template #default="scope">
          <div style="display: flex; align-content: space-between">
            <el-button type="primary" @click="showDetails(scope.row)">详情</el-button>
            <el-button type="primary" @click="handleEdit(scope.row)">编辑</el-button>
            <el-popconfirm
                title="确定删除吗？" @confirm="handleDelete(scope.row.id)">
              <template #reference>
                <el-button type="danger">删除</el-button>
              </template>
            </el-popconfirm>
          </div>
        </template>
      </el-table-column>
    </el-table>

    <div v-if="vis" class="modal-overlay" @click="closeDetails">
      <div class="modal" @click.stop>
        <div class="modal-header">
          <h1>{{ form.title }}</h1>
          <button class="close-button" @click="closeDetails">&times;</button>
        </div>
        <div style="margin-top: 10px">
          <strong>标签：</strong>
          <span v-for="(tip, index) in form.tips" :key="index" class="tip">{{ tip }}</span>
        </div>
        <div class="modal-body">
          <div class="details">
            <div class="details-images">
              <div v-for="(pic, index) in form.pictures.slice(0, 12)" :key="index" class="image-wrapper">
                <img v-if="pic && pic.length" :src="pic" alt="图片" class="details-image"/>
              </div>
            </div>
            <div class="user-info">
              <img :src="form.image || defaultImage" alt="用户头像" class="user-avatar"/>
              <div class="user-details">
                <div class="user-name">{{ form.name.length > 103 ? form.name.slice(0, 100) + '...' : form.name }}</div>
                <div class="user-date">编辑于 {{ form.date }}</div>
              </div>
            </div>
            <p class="content">{{ form.content }}</p>
            <div style="margin: 30px">
              <h2>评论</h2>
              <div class="comment-input">
                <img :src="user.image || defaultImage" alt="用户头像" class="user-avatar" />
                <div class="input-container">
                  <div class="user-name">{{ user.name }}</div>
                  <textarea
                      v-model="newComment.content"
                      placeholder="请输入评论"
                      rows="3"
                      class="comment-textarea"
                  ></textarea>
                  <button @click="postComment" class="submit-comment-button">发表</button>
                </div>
              </div>
              <div v-for="item in talkData">
                <div class="user-info" style="position: relative;">
                  <img :src="item.image || defaultImage" alt="用户头像" class="user-avatar"
                       style="position: absolute;top: 0"/>
                  <div class="user-details" style="margin-left: 70px">
                    <div class="user-name">{{item.name.length > 93 ? form.name.slice(0, 90) + '...' : form.name }}</div>
                    <div class="user-talk">
                      <template v-if="item.showFull">
                        {{ item.content }}
                        <span
                            class="view-more"
                            @click="item.showFull=false"
                        >
                          【收起全文】
                        </span>
                      </template>
                      <template v-else>
                        {{ item.content.length > 203 ? item.content.slice(0, 200) + '...' : item.content }}
                        <span
                            v-if="item.content.length > 203"
                            class="view-more"
                            @click="item.showFull=true"
                        >
                          【显示全文】
                        </span>
                      </template>
                    </div>
                    <div class="user-date">
                      编辑于 {{ item.date }}
                      <span v-if="item.author === user.id || user.role===1" class="delete-action" @click="deleteComment(item.id)">删除</span>
                    </div>
                  </div>
                </div>
              </div>
              <el-pagination
                  :curpage="talkCurrentPage"
                  :page-size="10"
                  :page-sizes="[10]"
                  :total="talkTotal"
                  background
                  layout="total,  prev, pager, next, jumper"
                  style="margin-top: 10px; margin-bottom: 40px; display: block; justify-content: center;"
                  @curchange="handleTalkCurrentChange">
              </el-pagination>
            </div>
          </div>
        </div>
      </div>
    </div>

    <div v-if="showModal" class="push-modal-overlay">
      <div class="modal-content">
        <h3>输入帖子信息</h3>
        <form @submit.prevent="handleSubmit">
          <div class="form-group">
            <label for="title">标题:</label>
            <input
                v-model="form.title"
                type="text"
                id="title"
                placeholder="请输入标题"
                required
            />
          </div>
          <div class="form-group">
            <label for="content">帖子详情:</label>
            <textarea
                v-model="form.content"
                id="content"
                placeholder="请输入帖子详情"
                rows="4"
                required
                style="width: 100%;min-width: 100%;max-width: 100%"
            ></textarea>
          </div>
          <div class="form-group">
            <label>标签:</label>
            <div v-for="(tip, index) in form.tips" :key="index" class="tag-input-group">
              <div class="tag-input-wrapper">
                <input v-model="form.tips[index]" type="text" placeholder="请输入标签" />
                <button type="button" class="remove-tag-button" @click="removeTip(index)">
                  <i class="fa fa-times"></i>X
                </button>
              </div>
            </div>
            <button
                type="button"
                class="add-tag-button"
                :disabled="!canAddTag"
                @click="addTip"
            >
              <i class="fa fa-plus"></i> 添加标签
            </button>
          </div>
          <div class="form-group">
            <label>图片上传:</label>
            <input type="file" multiple @change="handleFileUpload" />
            <div class="image-preview">
              <div v-for="(picture, index) in form.pictures" class="image-container">
                <img
                    :key="index"
                    :src="picture"
                    alt="预览图片"
                    class="preview-image"
                />
                <button type="button" class="remove-image-button" @click="removePicture(index)">
                  <i class="fa fa-times"></i>X
                </button>
              </div>
            </div>
          </div>
          <div class="form-actions">
            <button type="submit" class="submit-button">提交</button>
            <button type="button" class="cancel-button" @click="closeModal">取消</button>
          </div>
        </form>
      </div>
    </div>

    <el-pagination
        :curpage="currentPage"
        :page-size="pageSize"
        :page-sizes="[5, 10, 20]"
        :total="total"
        background
        layout="total, sizes, prev, pager, next, jumper"
        style="margin-top: 10px; margin-bottom: 40px; display: flex; justify-content: center;"
        @size-change="handleSizeChange"
        @curchange="handleCurrentChange">
    </el-pagination>
  </div>
</template>

<script>
import request from "@/utils/request";
export default {
  data() {
    return {
      user: {},
      search: "",
      tableData: [],
      currentPage: 1,
      pageSize: 5,
      total: 0,
      vis: false,
      form: {},
      defaultImage: '/api/files/150',
      defaultAvatar: 'https://via.placeholder.com/50',
      talkData: [],
      talkTotal: 0,
      talkCurrentPage: 1,
      newComment: {
        content: "",
      },
      editCommentContent: "",
      editingCommentId: null,
      showModal: false,
    };
  },
  mounted() {
    window.addEventListener("keydown", this.handleKeyPress);
  },
  beforeDestroy() {
    window.removeEventListener("keydown", this.handleKeyPress);
    document.body.style.overflow = "";
  },
  created() {
    this.user = JSON.parse(sessionStorage.getItem("user") || "{}");
    this.load();
  },
  computed: {
    canAddTag() {
      if (this.form.tips.length === 0) {
        return true;
      }
      const lastTip = this.form.tips[this.form.tips.length - 1] || "";
      return lastTip.trim() !== "";
    },
  },
  methods: {
    load() {
      request.get(`/api/house_talk`, {
        params: {
          pageNum: this.currentPage,
          pageSize: this.pageSize,
          search: this.search,
        },
      }).then((res) => {
        this.tableData = res.data.records;
        this.total = res.data.total;
      });
    },
    add() {
      this.form = {
        tips:[],
        pictures:[]
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
      request.delete(`/api/house_talk/` + id).then(res => {
        if (res.code === '0') {
          this.$message.success("删除成功");
          this.load();
        } else {
          this.$message.error(res.msg);
        }
      });
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
      if (this.form.tips.length > 9){
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
    talkLoad() {
      request.get(`/api/talk_item`, {
        params: {
          pageNum: this.talkCurrentPage,
          pageSize: 10,
          belongItem: this.form.id
        },
      }).then((res) => {
        this.talkData = res.data.records;
        this.talkTotal = res.data.total;
        console.log(this.talkTotal)
      });
    },
    postComment() {
      if (!this.newComment.content.trim()) {
        this.$message.error("评论内容不能为空");
        return;
      }
      request.post("/api/talk_item", {
        content: this.newComment.content,
        belongItem: this.form.id,
        author: this.user.id
      })
          .then((res) => {
            if (res.code === "0") {
              this.$message.success("评论成功");
              this.newComment.content = "";
              this.talkLoad();
            } else {
              this.$message.error(res.msg);
            }
          })
          .catch((error) => {
            console.error("评论失败:", error);
            this.$message.error("评论失败，请稍后再试");
          });
    },
    deleteComment(commentId) {
      this.$confirm("此操作将永久删除该评论, 是否继续?", "提示", {
        confirmButtonText: "是",
        cancelButtonText: "否",
        type: "warning",
      }).then(() => {
        request
            .delete("/api/talk_item/", {params: {id: commentId}})
            .then((res) => {
              if (res.code === "0") {
                this.$message.success("删除成功");
                this.talkLoad();
              } else {
                this.$message.error(res.msg);
              }
            })
            .catch((error) => {
              console.error("删除评论失败:", error);
              this.$message.error("删除评论失败，请稍后再试");
            });
      })
          .catch(() => {
            this.$message.info("已取消删除");
          });
    },
    showDetails(post) {
      this.vis = true;
      this.form = post;
      this.talkLoad();
    },
    closeDetails() {
      this.vis = false;
      this.form = {
        tips: [],
        pictures: []
      };
      this.talkData = [];
    },
    handleKeyPress(event) {
      if (event.key === "Escape") {
        this.closeDetails();
        this.closeModal();
      }
    },
    handleSizeChange(pageSize) {
      this.pageSize = pageSize;
      this.load();
    },
    handleCurrentChange(pageNum) {
      this.currentPage = pageNum;
      this.load();
    },
    handleTalkCurrentChange(pageNum) {
      this.talkCurrentPage = pageNum;
      this.talkLoad();
    }
  }
};
</script>


<style scoped>
.navbar {
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

.search-bar {
  display: flex;
  gap: 10px;
}

.search-input {
  padding: 10px;
  border: 1px solid #ddd;
  border-radius: 5px;
}

.clear-button {
  padding: 10px 20px;
  background-color: #7cc400;
  color: white;
  border: none;
  border-radius: 5px;
  cursor: pointer;
}

.search-button {
  padding: 10px 20px;
  background-color: #ff5a5f;
  color: white;
  border: none;
  border-radius: 5px;
  cursor: pointer;
}

.item-image {
  width: 100%;
  height: 100%;
  border-radius: 8px;
  object-fit: cover;
}

.item-user {
  display: flex;
  align-items: center;
  margin-top: 5px;
}

.user-info {
  display: flex;
  align-items: center;
  margin-top: 10px;
}

.user-avatar {
  width: 50px;
  height: 50px;
  border-radius: 50%;
  margin-right: 10px;
}

.user-name {
  font-size: 14px;
  font-weight: bold;
  color: #333;
}

.user-talk {
  font-size: 16px;
  color: #333;
  margin-top: 10px;
  margin-bottom: 20px;
  white-space: pre-wrap;
  word-wrap: break-word;
}

.user-date {
  font-size: 12px;
  color: #333;
  margin-top: 5px;
}

.view-more {
  color: #5656e1;
  text-decoration: underline;
  cursor: pointer;
}

.modal-overlay {
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

.modal {
  background: white;
  border-radius: 12px;
  width: 60%;
  max-height: 95%;
  height: auto;
  overflow-y: auto;
  box-shadow: 0 4px 10px rgba(0, 0, 0, 0.2);
  padding: 20px;
}

.modal-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.modal-header h2 {
  font-size: 18px;
  margin: 0;
}

.close-button {
  background: none;
  border: none;
  font-size: 24px;
  cursor: pointer;
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

.comment-input {
  display: flex;
  align-items: flex-start;
  margin-bottom: 20px;
}

.input-container {
  flex: 1;
  margin-left: 10px;
}

.comment-textarea {
  width: 100%;
  height: auto;
  padding: 10px;
  border: 1px solid #ddd;
  border-radius: 5px;
  resize: none;
  margin-top: 5px;
}

.submit-comment-button {
  margin-top: 10px;
  padding: 8px 20px;
  background-color: #7cc400;
  color: white;
  border: none;
  border-radius: 5px;
  cursor: pointer;
}

.delete-action {
  margin-left: 10px;
  color: #007bff;
  cursor: pointer;
  text-decoration: underline;
}

.delete-action:hover {
  color: #0056b3;
}

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
