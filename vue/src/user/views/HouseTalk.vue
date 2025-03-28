<template>
  <div style="height: 80px"></div>
  <div>
    <header class="talk-navbar">
      <h2>正在显示 {{ talk_total }} 个租友帖子中的 {{ talk_tableData.length }} 个</h2>
      <div class="talk-search-bar">
        <input v-model="talk_search" class="talk-search-input" placeholder="输入关键词" type="text"/>
        <button class="talk-search-button" @click="talk_load">搜索</button>
        <button class="talk-clear-button" @click="this.talk_search=''">清空</button>
      </div>
    </header>

    <div class="talk-item-list">
      <div v-for="item in talk_tableData" :key="item.id" class="talk-item-card">
        <div class="talk-item-left">
          <img :src="item.pictures[0] || talk_defaultImage" alt="封面图片" class="talk-item-image"/>
        </div>
        <div class="talk-item-middle">
          <div class="talk-item-name">{{ item.title }}</div>
          <div style="margin-top: 10px">
            <strong>标签：</strong>
            <span v-for="(tip, index) in item.tips" :key="index" class="talk-tip">{{ tip }}</span>
          </div>
          <div class="talk-item-user">
            <img :src="item.image || talk_defaultAvatar" alt="头像" class="talk-user-avatar"/>
            <span class="talk-user-name">{{ item.name.length > 55 ? item.name.slice(0, 52) + '...' : item.name }}</span>
          </div>
        </div>
        <div class="talk-item-right">
          <div class="talk-item-content">
            {{ item.content.length > 253 ? item.content.slice(0, 250) + '...' : item.content }}
            <span
                class="talk-view-more"
                @click.stop="talk_showDetails(item)"
            >
            【查看详情】
            </span>
          </div>
        </div>
      </div>
    </div>

    <div v-if="talk_vis" class="talk-modal-overlay" @click="talk_closeDetails">
      <div class="talk-modal" @click.stop>
        <div class="talk-modal-header">
          <h1>{{ talk_form.title }}</h1>
          <button class="talk-close-button" @click="talk_closeDetails">&times;</button>
        </div>
        <div style="margin-top: 10px">
          <strong>标签：</strong>
          <span v-for="(tip, index) in talk_form.tips" :key="index" class="talk-tip">{{ tip }}</span>
        </div>
        <div class="talk-modal-body">
          <div class="talk-details">
            <div class="talk-details-images">
              <div v-for="(pic, index) in talk_form.pictures.slice(0, 12)" :key="index" class="talk-image-wrapper">
                <img v-if="pic && pic.length" :src="pic" alt="图片" class="talk-details-image"/>
              </div>
            </div>
            <div class="talk-user-info">
              <img :src="talk_form.image || talk_defaultImage" alt="用户头像" class="talk-user-avatar"/>
              <div class="talk-user-details">
                <div class="talk-user-name">{{ talk_form.name.length > 103 ? talk_form.name.slice(0, 100) + '...' : talk_form.name }}</div>
                <div class="talk-user-date">编辑于 {{ talk_form.date }}</div>
              </div>
            </div>
            <p class="talk-content">{{ talk_form.content }}</p>
            <div style="margin: 30px">
              <h2>评论</h2>
              <div class="talk-comment-input">
                <img :src="talk_user.image || talk_defaultImage" alt="用户头像" class="talk-user-avatar" />
                <div class="talk-input-container">
                  <div class="talk-user-name">{{ talk_user.name }}</div>
                  <textarea
                      v-model="talk_newComment.content"
                      placeholder="请输入评论"
                      rows="3"
                      class="talk-comment-textarea"
                  ></textarea>
                  <button @click="talk_postComment" class="talk-submit-comment-button">发表</button>
                </div>
              </div>
              <div v-for="item in talk_talkData">
                <div class="talk-user-info" style="position: relative;">
                  <img :src="item.image || talk_defaultImage" alt="用户头像" class="talk-user-avatar"
                       style="position: absolute;top: 0"/>
                  <div class="talk-user-details" style="margin-left: 70px">
                    <div class="talk-user-name">{{item.name.length > 93 ? item.name.slice(0, 90) + '...' : item.name }}</div>
                    <div class="talk-user-talk">
                      <template v-if="item.showFull">
                        {{ item.content }}
                        <span
                            class="talk-view-more"
                            @click="item.showFull=false"
                        >
                          【收起全文】
                        </span>
                      </template>
                      <template v-else>
                        {{ item.content.length > 203 ? item.content.slice(0, 200) + '...' : item.content }}
                        <span
                            v-if="item.content.length > 203"
                            class="talk-view-more"
                            @click="item.showFull=true"
                        >
                          【显示全文】
                        </span>
                      </template>
                    </div>
                    <div class="talk-user-date">
                      编辑于 {{ item.date }}
                      <span v-if="item.author === talk_user.id" class="talk-delete-action" @click="talk_deleteComment(item.id)">删除</span>
                    </div>
                  </div>
                </div>
              </div>
              <el-pagination
                  :current-page="talk_talkCurrentPage"
                  :page-size="10"
                  :page-sizes="[10]"
                  :total="talk_talkTotal"
                  background
                  layout="total,  prev, pager, next, jumper"
                  style="margin-top: 10px; margin-bottom: 40px; display: block; justify-content: center;"
                  @current-change="talk_handleTalkCurrentChange">
              </el-pagination>
            </div>
          </div>
        </div>
      </div>
    </div>
    <el-pagination
        :current-page="talk_currentPage"
        :page-size="talk_pageSize"
        :page-sizes="[5, 10, 20]"
        :total="talk_total"
        background
        layout="total, sizes, prev, pager, next, jumper"
        style="margin-top: 10px; margin-bottom: 40px; display: flex; justify-content: center;"
        @size-change="talk_handleSizeChange"
        @current-change="talk_handleCurrentChange">
    </el-pagination>
  </div>
</template>

<script>
import request from "@/utils/request";
export default {
  data() {
    return {
      talk_user: {},
      talk_search: "",
      talk_tableData: [],
      talk_currentPage: 1,
      talk_pageSize: 5,
      talk_total: 0,
      talk_vis: false,
      talk_form: {},
      talk_defaultImage: '/api/files/150',
      talk_defaultAvatar: 'https://via.placeholder.com/50',
      talk_talkData: [],
      talk_talkTotal: 0,
      talk_talkCurrentPage: 1,
      talk_newComment: {
        content: "",
      },
      talk_editCommentContent: "",
      talk_editingCommentId: null,
    };
  },
  mounted() {
    window.addEventListener("keydown", this.talk_handleKeyPress);
  },
  beforeDestroy() {
    window.removeEventListener("keydown", this.talk_handleKeyPress);
    document.body.style.overflow = "";
  },
  created() {
    this.talk_user = JSON.parse(sessionStorage.getItem("user") || "{}");
    this.talk_load();
  },
  methods: {
    talk_load() {
      request.get(`/api/house_talk`, {
        params: {
          pageNum: this.talk_currentPage,
          pageSize: this.talk_pageSize,
          search: this.talk_search,
        },
      }).then((res) => {
        this.talk_tableData = res.data.records;
        this.talk_total = res.data.total;
      });
    },
    talk_talkLoad() {
      request.get(`/api/talk_item`, {
        params: {
          pageNum: this.talk_talkCurrentPage,
          pageSize: 10,
          belongItem: this.talk_form.id
        },
      }).then((res) => {
        this.talk_talkData = res.data.records;
        this.talk_talkTotal = res.data.total;
      });
    },
    talk_postComment() {
      if (!this.talk_newComment.content.trim()) {
        this.$message.error("评论内容不能为空");
        return;
      }
      console.log(this.talk_user)
      request.post("/api/talk_item", {
        content: this.talk_newComment.content,
        belongItem: this.talk_form.id,
        author: this.talk_user.id
      })
          .then((res) => {
            if (res.code === "0") {
              this.$message.success("评论成功");
              this.talk_newComment.content = "";
              this.talk_talkLoad();
            } else {
              this.$message.error(res.msg);
            }
          })
          .catch((error) => {
            console.error("评论失败:", error);
            this.$message.error("评论失败，请稍后再试");
          });
    },
    talk_deleteComment(commentId) {
      this.$confirm("此操作将永久删除该评论, 是否继续?", "提示", {
        confirmButtonText: "是",
        cancelButtonText: "否",
        type: "warning",
      }).then(() => {
        request
            .delete("/api/talk_item/", { params: { id: commentId } })
            .then((res) => {
              if (res.code === "0") {
                this.$message.success("删除成功");
                this.talk_talkLoad();
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
    talk_showDetails(post) {
      this.talk_vis = true;
      this.talk_form = post;
      this.talk_talkLoad();
    },
    talk_closeDetails() {
      this.talk_vis = false;
      this.talk_form = {};
      this.talk_talkData = [];
    },
    talk_handleKeyPress(event) {
      if (event.key === "Escape") {
        this.talk_closeDetails();
      }
    },
    talk_handleSizeChange(pageSize) {
      this.talk_pageSize = pageSize;
      this.talk_load();
    },
    talk_handleCurrentChange(pageNum) {
      this.talk_currentPage = pageNum;
      this.talk_load();
    },
    talk_handleTalkCurrentChange(pageNum) {
      this.talk_talkCurrentPage = pageNum;
      this.talk_talkLoad();
    }
  }
};
</script>


<style scoped>
.talk-navbar {
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

.talk-search-bar {
  display: flex;
  gap: 10px;
}

.talk-search-input {
  padding: 10px;
  border: 1px solid #ddd;
  border-radius: 5px;
}

.talk-clear-button {
  padding: 10px 20px;
  background-color: #7cc400;
  color: white;
  border: none;
  border-radius: 5px;
  cursor: pointer;
}

.talk-search-button {
  padding: 10px 20px;
  background-color: #ff5a5f;
  color: white;
  border: none;
  border-radius: 5px;
  cursor: pointer;
}

.talk-item-list {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.talk-item-card {
  display: flex;
  width: 100%;
  height: 200px;
  max-height: 300px;
  border: 1px solid #ddd;
  border-radius: 8px;
  overflow: hidden;
  padding: 10px;
  background-color: #fff;
}

.talk-item-left {
  flex: 0 0 250px;
  height: 180px;
  width: 120px;
}

.talk-item-image {
  width: 100%;
  height: 100%;
  border-radius: 8px;
  object-fit: cover;
}

.talk-item-middle {
  flex: 0 0 100px;
  display: flex;
  flex-direction: column;
  justify-content: space-between;
  margin-left: 40px;
  width: 600px;
  min-width: 600px;
  height: 180px;
}

.talk-item-name {
  font-size: 18px;
  font-weight: bold;
  margin-bottom: 10px;
}

.talk-item-user {
  display: flex;
  align-items: center;
  margin-top: 5px;
}

.talk-user-info {
  display: flex;
  align-items: center;
  margin-top: 10px;
}

.talk-user-avatar {
  width: 50px;
  height: 50px;
  border-radius: 50%;
  margin-right: 10px;
}

.talk-user-name {
  font-size: 14px;
  font-weight: bold;
  color: #333;
}

.talk-user-talk {
  font-size: 16px;
  color: #333;
  margin-top: 10px;
  margin-bottom: 20px;
  white-space: pre-wrap;
  word-wrap: break-word;
}

.talk-user-date {
  font-size: 12px;
  color: #333;
  margin-top: 5px;
}

.talk-item-right {
  flex: 1;
  font-size: 16px;
  color: #555;
  line-height: 1.6;
  margin-left: 20px;
}

.talk-item-content {
  height: 100%;
  overflow: hidden;
  text-overflow: ellipsis;
}

.talk-view-more {
  color: #5656e1;
  text-decoration: underline;
  cursor: pointer;
}

.talk-modal-overlay {
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

.talk-modal {
  background: white;
  border-radius: 12px;
  width: 60%;
  max-height: 95%;
  height: auto;
  overflow-y: auto;
  box-shadow: 0 4px 10px rgba(0, 0, 0, 0.2);
  padding: 20px;
}

.talk-modal-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.talk-modal-header h2 {
  font-size: 18px;
  margin: 0;
}

.talk-close-button {
  background: none;
  border: none;
  font-size: 24px;
  cursor: pointer;
}

.talk-modal-body {
  font-size: 14px;
  color: #333;
  margin-top: 20px;
}

.talk-details-images {
  display: flex;
  flex-wrap: wrap;
  gap: 10px;
  margin: 15px 0;
}

.talk-image-wrapper {
  flex: 0 0 calc(33.33% - 10px);
  box-sizing: border-box;
}

.talk-details-image {
  width: 100%;
  height: auto;
  border-radius: 8px;
  object-fit: cover;
}

.talk-content {
  margin: 15px 0;
  line-height: 1.6;
}

.talk-tip {
  display: inline-block;
  margin-right: 8px;
  padding: 5px 10px;
  background: #f5f5f5;
  border-radius: 4px;
  font-size: 12px;
  color: #555;
}

.talk-comment-input {
  display: flex;
  align-items: flex-start;
  margin-bottom: 20px;
}

.talk-input-container {
  flex: 1;
  margin-left: 10px;
}

.talk-comment-textarea {
  width: 100%;
  height: auto;
  padding: 10px;
  border: 1px solid #ddd;
  border-radius: 5px;
  resize: none;
  margin-top: 5px;
}

.talk-submit-comment-button {
  margin-top: 10px;
  padding: 8px 20px;
  background-color: #7cc400;
  color: white;
  border: none;
  border-radius: 5px;
  cursor: pointer;
}

.talk-delete-action {
  margin-left: 10px;
  color: #007bff;
  cursor: pointer;
  text-decoration: underline;
}

.talk-delete-action:hover {
  color: #0056b3;
}

</style>
