<template>
  <div style="height: 80px"></div>
  <div class="profile-container">
    <div class="profile-main">
      <div class="profile-header">
        <div class="avatar-container">
          <img :src="user.image || defaultAvatar" alt="用户头像" class="avatar" />
          <button @click="showAvatarModal = true; editUser = { ...user }" class="change-avatar-button">更改头像</button>
        </div>
        <div class="user-info">
          <h2>{{ user.name }}</h2>
          <div class="contact-info">
            <div class="left">
              <p>电话：{{ user.phone }}</p>
              <p style="margin-top: 10px">地址：{{ user.address }}</p>
            </div>
            <div class="right">
              <button @click="showEditForm = true; editUser = { ...user }" class="edit-button" style="background-color: #00bc15">修改个人信息</button>
              <button @click="showChangePasswordModal = true" class="edit-button" style="background-color: #b69100">修改密码</button>
              <button @click="this.$router.push('/user/login')" class="edit-button" style="background-color: #b60000">退出登录</button>
            </div>
          </div>
        </div>
      </div>

      <div v-if="showAvatarModal" class="avatar-modal">
        <div class="modal-content">
          <h3>修改头像</h3>
          <div class="file-upload">
            <input type="file" ref="avatarFile" @change="handleAvatarChange" />
            <div v-if="editUser.image" class="avatar-preview">
              <img :src="editUser.image || defaultAvatar" alt="头像预览" class="avatar" />
            </div>
          </div>
          <button @click="submitAvatar" class="submit-button">上传头像</button>
          <button @click="showAvatarModal = false" class="cancel-button">取消</button>
        </div>
      </div>

      <div v-if="showEditForm" class="avatar-modal">
        <div class="modal-content">
          <h3>修改个人信息</h3>
          <form @submit.prevent="submitForm">
            <div class="form-group">
              <label for="name">用户名</label>
              <input v-model="editUser.name" id="name" type="text" required />
            </div>
            <div class="form-group">
              <label for="phone">联系电话</label>
              <input v-model="editUser.phone" id="phone" type="text" required />
            </div>
            <div class="form-group">
              <label for="address">地址</label>
              <input v-model="editUser.address" id="address" type="text" required />
            </div>
            <div class="button-group">
              <button type="submit" class="submit-button">保存</button>
              <button type="button" @click="showEditForm = false" class="cancel-button">取消</button>
            </div>
          </form>
        </div>
      </div>

      <div v-if="showChangePasswordModal" class="avatar-modal">
        <div class="modal-content">
          <h3>修改密码</h3>
          <form @submit.prevent="submitChangePassword">
            <div class="form-group">
              <label for="oldPassword">旧密码</label>
              <input v-model="passwords.old" id="oldPassword" type="password" required />
            </div>
            <div class="form-group">
              <label for="newPassword">新密码</label>
              <input v-model="passwords.new" id="newPassword" type="password" required />
            </div>
            <div class="form-group">
              <label for="confirmPassword">确认新密码</label>
              <input v-model="passwords.confirm" id="confirmPassword" type="password" required />
            </div>
            <div class="button-group">
              <button type="submit" class="submit-button">修改密码</button>
              <button type="button" @click="showChangePasswordModal = false" class="cancel-button">取消</button>
            </div>
          </form>
        </div>
      </div>

      <div class="user-posts">
        <h3>我发布的内容</h3>
        <div class="tab-and-batch-container">
          <div class="tabs">
            <span :class="{'active-tab': activeTab === 'house'}" @click="activeTab = 'house';this.rent_load()">我发布的房源信息</span>
            <span :class="{'active-tab': activeTab === 'demand'}" @click="activeTab = 'demand';this.need_load()">我发布的租房需求</span>
            <span :class="{'active-tab': activeTab === 'talk'}" @click="activeTab = 'talk';this.talk_load()">我发布的租友帖子</span>
          </div>

          <div class="batch-delete-container">
            <div class="publish-buttons">
              <button @click="openModal('house')" class="publish-button" v-if="this.activeTab === 'house'">发布房源信息</button>
              <button @click="openModal('demand')" class="publish-button" v-if="this.activeTab === 'demand'">发布租房需求</button>
              <button @click="openModal('talk')" class="publish-button" v-if="this.activeTab === 'talk'">发布租友帖子</button>
            </div>
            <button v-if="!isBatchDeleting" @click="startBatchDelete" class="batch-delete-button">
              批量删除
            </button>
            <div v-else class="batch-delete-actions">
              <button @click="selectAllItems" class="batch-select-all-button">全选当前页面</button>
              <button @click="deleteSelectedItems" class="batch-delete-confirm-button">删除</button>
              <button @click="cancelBatchDelete" class="batch-cancel-button">取消</button>
            </div>
          </div>
        </div>
        <div v-if="activeTab === 'house'">
          <h4>我的房源信息</h4>
          <main>
            <div class="rent-listings">
              <div class="rent-listing"
                   v-for="item in rent_tableData"
                   :key="item.id"
                   @click="rent_showDetails(item)"
                   style="cursor: pointer;">
                <div class="rent-image-container">
                  <input
                      v-if="isBatchDeleting"
                      type="checkbox"
                      class="item-checkbox"
                      :checked="selectedItems.includes(item.id)"
                      @click.stop="toggleSelectItem(item.id)"
                  />
                  <img class="rent-listing-image" :src="item.pictures[0] || rent_defaultImage" alt="图片" />
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
                      <img class="rent-user-avatar" :src="item.image || rent_defaultImage" alt="用户头像" />
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
                    <img class="rent-user-avatar" :src="rent_form.image || rent_defaultImage" alt="用户头像" />
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

        <div v-if="activeTab === 'demand'">
          <h4>我的租房需求</h4>
          <main>
            <div class="need-listings">
              <div class="need-listing"
                   v-for="item in need_tableData"
                   :key="item.id"
                   @click="need_showDetails(item)"
                   style="cursor: pointer;">
<!--                <div class="rent-image-container">-->
<!--                  <input-->
<!--                      v-if="isBatchDeleting"-->
<!--                      type="checkbox"-->
<!--                      class="item-checkbox"-->
<!--                      :checked="selectedItems.includes(item.id)"-->
<!--                      @click.stop="toggleSelectItem(item.id)"-->
<!--                  />-->
<!--                  <img class="need-listing-image" :src="item.pictures[0] || need_defaultImage" alt="图片" />-->
<!--                </div>-->
                <div class="need-listing-info">
                  <div class="need-info-top">
                    <h3>{{ item.province + ' ' + item.city + ' ' + item.town }}</h3>
                  </div>
                  <div class="need-info-bottom">
                    <div class="need-info-left">
                      <p>期望价格 {{ item.price }} 元/月</p>
                      <p>期望面积 {{ item.area }} 平米</p>
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
<!--                  <div class="need-details-images">-->
<!--                    <div v-for="(pic, index) in need_form.pictures.slice(0, 12)" :key="index" class="need-image-wrapper">-->
<!--                      <img v-if="pic && pic.length" :src="pic" alt="图片" class="need-details-image" />-->
<!--                    </div>-->
<!--                  </div>-->
                  <h1>{{ need_form.province }} {{ need_form.city }} {{ need_form.town }}</h1>
                  <h2 style="margin-top: 10px">
                    <strong>期望价格 ￥{{ need_form.price }} / 月</strong>
                    ·期望面积 {{ need_form.area }} 平米
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

        <div v-if="activeTab === 'talk'">
          <h4>我的租友帖子</h4>
          <div class="talk-item-list">
            <div v-for="item in talk_tableData" :key="item.id" class="talk-item-card" @click="talk_showDetails(item)">
              <input
                  v-if="isBatchDeleting"
                  type="checkbox"
                  class="item-checkbox"
                  :checked="selectedItems.includes(item.id)"
                  @click.stop="toggleSelectItem(item.id)"
              />
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
                  {{ item.content.length > 153 ? item.content.slice(0, 150) + '...' : item.content }}
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
                      <img :src="user.image || talk_defaultImage" alt="用户头像" class="talk-user-avatar" />
                      <div class="talk-input-container">
                        <div class="talk-user-name">{{ user.name }}</div>
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
                            <span v-if="item.author === this.user.id" class="talk-delete-action" @click="talk_deleteComment(item.id)">删除</span>
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

        <div v-if="showModal" class="push-modal-overlay">
          <div class="modal-content">
            <h3>{{ modalTitles[activeModal] }}</h3>
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
                <label for="content">{{this.activeTab === 'house' ? '房源描述' : this.activeTab === 'demand' ? '需求描述' : '帖子详情'}}:</label>
                <textarea
                    v-model="form.content"
                    id="content"
                    placeholder="请输入内容"
                    rows="4"
                    required
                ></textarea>
              </div>
              <div class="form-group" v-if="this.activeTab === 'house' || this.activeTab === 'demand'">
                <label for="price">价格 (元/月):</label>
                <input
                    v-model="form.price"
                    type="number"
                    id="price"
                    placeholder="请输入价格"
                    required
                    step="0.01"
                />
              </div>
              <div class="form-group" v-if="this.activeTab === 'house' || this.activeTab === 'demand'">
                <label for="area">面积 (平方米):</label>
                <input
                    v-model="form.area"
                    type="number"
                    id="area"
                    placeholder="请输入面积"
                    step="0.01"
                    required
                />
              </div>
              <div class="form-group">
                <label for="province">省份:</label>
                <select
                    v-model="form.province"
                    id="province"
                    class="form-control"
                    required
                >
                  <option value="" disabled>请选择省份</option>
                  <option v-for="province in staticPlace" :key="province.name" :value="province.name">
                    {{ province.name }}
                  </option>
                </select>
              </div>
              <div class="form-group" v-if="cities.length > 0">
                <label for="city">城市:</label>
                <select
                    v-model="form.city"
                    id="city"
                    class="form-control"
                    required
                >
                  <option value="" disabled>请选择城市</option>
                  <option v-for="city in cities" :key="city.name" :value="city.name">
                    {{ city.name }}
                  </option>
                </select>
              </div>
              <div class="form-group" v-if="towns.length > 0">
                <label for="town">城区:</label>
                <select
                    v-model="form.town"
                    id="town"
                    class="form-control"
                    required
                >
                  <option value="" disabled>请选择城区</option>
                  <option v-for="town in towns" :key="town" :value="town">
                    {{ town }}
                  </option>
                </select>
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
              <div v-if="this.activeTab === 'house' || this.activeTab === 'talk'" class="form-group">
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
      </div>
    </div>
  </div>
</template>

<script>
import request from "@/utils/request";
import { chinaCities } from '@/utils/chinaCities';

export default {
  data() {
    return {
      user: JSON.parse(sessionStorage.getItem('user')) || {},
      defaultAvatar: '/api/files/150',
      showAvatarModal: false,
      showEditForm: false,
      showChangePasswordModal: false,
      passwords: {
        old: '',
        new: '',
        confirm: ''
      },
      passwordVisible: false,
      activeTab: 'house',
      editUser: {...this.user},

      rent_currentPage: 1,
      rent_pageSize: 8,
      rent_search: '',
      rent_total: 0,
      rent_tableData: [],
      rent_defaultImage: '/api/files/150',
      rent_vis: false,
      rent_form: {},
      need_currentPage: 1,
      need_pageSize: 8,
      need_search: '',
      need_total: 0,
      need_tableData: [],
      need_defaultImage: '/api/files/150',
      need_vis: false,
      need_form: {},
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
      selectedItems: [],
      isBatchDeleting: false,
      showModal: false,
      activeModal: "",
      modalTitles: {
        house: "发布房源信息",
        demand: "发布租房需求",
        talk: "发布租友帖子",
      },
      form: {
        area: "", // 面积
        tips: [], // 标签列表
        pictures: ['']
      },
      staticPlace: chinaCities,
      cities: [],
      towns: [],
    };
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
  created() {
    this.refreshData();
  },
  computed: {
    isAreaValid() {
      if (this.activeModal === "house") {
        return /^\d+(\.\d{1,2})?$/.test(this.form.area);
      } else {
        return true;
      }
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
  methods: {
    handleAvatarChange(event) {
      const file = event.target.files[0];
      if (file) {
        this.editUser.image = URL.createObjectURL(file);
        const reader = new FileReader();
        reader.onload = (e) => {
          this.newAvatar = e.target.result;
        };
        reader.readAsDataURL(file);
      }
    },
    async submitAvatar() {
      if (this.newAvatar) {
        const formData = new FormData();
        formData.append('file', this.$refs.avatarFile.files[0]);
        try {
          request.post('/api/files/upload', formData, {
            headers: {
              'Content-Type': 'multipart/form-data',
            },
          }).then(res => {
            if (res.code === '0') {
              this.$message.success('头像上传成功');
              this.user.image = res.data;
              sessionStorage.setItem("user", JSON.stringify(this.user));
              this.updateImage();
            } else {
              this.$message.error(res.msg);
            }
          });
          this.showAvatarModal = false;
          this.newAvatar = null;
        } catch (error) {
          console.error('头像上传失败', error);
        }
      }
    },
    updateImage() {
      request.put("/api/user", {
        id: this.user.id,
        image: this.user.image
      }).then(res => {
        if (res.code === '0') {
          this.$message.success('头像更新成功');
          sessionStorage.setItem("user", JSON.stringify(this.user));
        } else {
          this.$message.error(res.msg);
        }
      });
    },
    update() {
      const phoneRegex = /^[1][3-9][0-9]{9}$/;
      if (!phoneRegex.test(this.editUser.phone)) {
        this.$message.error('请输入有效的11位电话号码');
        return;
      }
      request.put("/api/user", this.editUser).then(res => {
        if (res.code === '0') {
          this.$message.success('更新成功');
          this.user = this.editUser;
          sessionStorage.setItem("user", JSON.stringify(this.user));
        } else {
          this.$message.error(res.msg);
        }
      });
    },
    submitForm() {
      this.update();
      this.showEditForm = false;
    },
    submitChangePassword() {
      if (this.passwords.new !== this.passwords.confirm) {
        this.$message.error('新密码与确认密码不一致');
        return;
      }
      if (this.passwords.old === this.passwords.new) {
        this.$message.error('新密码不能与旧密码相同');
        return;
      }
      if (this.passwords.new.length < 8) {
        this.$message.error('密码长度必须至少8位');
        return;
      }
      const passwordRegex = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)[a-zA-Z\d]{8,}$/;
      if (!passwordRegex.test(this.passwords.new)) {
        this.$message.error('密码必须包含至少一个大写字母、一个小写字母和一个数字');
        return;
      }
      const specialCharacterRegex = /[!@#$%^&*(),.?":{}|<>]/;
      if (specialCharacterRegex.test(this.passwords.new)) {
        this.$message.error('密码不能包含特殊字符');
        return;
      }
      const params = {
        id: this.user.id,
        oldPw: this.passwords.old,
        newPw: this.passwords.new
      };
      request.post('/api/user/password', params).then(res => {
        if (res.code === '0') {
          this.$message.success('修改成功');
        } else {
          this.$message.error(res.msg);
        }
      });
      this.showChangePasswordModal = false;
    },
    handleKeyPress(event) {
      if (event.key === "Escape") {
        this.showAvatarModal = false;
        this.showEditForm = false;
        this.showChangePasswordModal = false;
        this.rent_closeDetails();
        this.need_closeDetails();
        this.talk_closeDetails();
        this.showModal = false;
      }
    },

    rent_load() {
      request.get(`/api/house_rent`, {
        params: {
          pageNum: this.rent_currentPage,
          pageSize: this.rent_pageSize,
          author: this.user.id
        },
      }).then((res) => {
        this.rent_tableData = res.data.records;
        this.rent_total = res.data.total;
        this.scrollToTop();
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
    need_load() {
      request.get(`/api/house_need`, {
        params: {
          pageNum: this.need_currentPage,
          pageSize: this.need_pageSize,
          author: this.user.id
        },
      }).then((res) => {
        this.need_tableData = res.data.records;
        this.need_total = res.data.total;
        this.scrollToTop();
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
    talk_load() {
      request.get(`/api/house_talk`, {
        params: {
          pageNum: this.talk_currentPage,
          pageSize: this.talk_pageSize,
          author: this.user.id
        },
      }).then((res) => {
        this.talk_tableData = res.data.records;
        this.talk_total = res.data.total;
        this.scrollToTop();
      });
    },
    talk_talkLoad() {
      console.log(this.talk_form.id)
      request.get(`/api/talk_item`, {
        params: {
          pageNum: this.talk_talkCurrentPage,
          pageSize: 10,
          belongItem: this.talk_form.id
        },
      }).then((res) => {
        this.talk_talkData = res.data.records;
        console.log(this.talk_talkData[0].author)
        console.log(this.user.id)
        this.talk_talkTotal = res.data.total;
      });
    },
    talk_postComment() {
      if (!this.talk_newComment.content.trim()) {
        this.$message.error("评论内容不能为空");
        return;
      }
      request.post("/api/talk_item", {
        content: this.talk_newComment.content,
        belongItem: this.talk_form.id,
        author: this.user.id
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
            .delete("/api/talk_item/", {params: {id: commentId}})
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
    },
    startBatchDelete() {
      this.isBatchDeleting = true;
      this.selectedItems = [];
    },
    cancelBatchDelete() {
      this.isBatchDeleting = false;
      this.selectedItems = [];
    },
    selectAllItems() {
      if (this.activeTab === 'house') {
        this.selectedItems = this.rent_tableData.map(item => item.id);
      } else if (this.activeTab === 'demand') {
        this.selectedItems = this.need_tableData.map(item => item.id);
      } else if (this.activeTab === 'talk') {
        this.selectedItems = this.talk_tableData.map(item => item.id);
      }
    },
    toggleSelectItem(id) {
      const index = this.selectedItems.indexOf(id);
      if (index === -1) {
        this.selectedItems.push(id);
      } else {
        this.selectedItems.splice(index, 1);
      }
    },
    async deleteSelectedItems() {
      if (!this.selectedItems.length) {
        this.$message.warning("请选择要删除的内容！");
        return;
      }
      try {
        const type = this.activeTab === 'house' ? 1 : this.activeTab === 'demand' ? 2 : 3;
        const apiMap = {
          1: '/api/house_rent/batch',
          2: '/api/house_need/batch',
          3: '/api/house_talk/batch',
        };
        let response = await request.delete(apiMap[type], {
          data: this.selectedItems,
        })
        if (response.code === '0') {
          this.$message.success("删除成功！");
          this.refreshData();
          this.cancelBatchDelete();
        } else {
          this.$message.error("删除失败，请重试！");
        }
      } catch (error) {
        this.$message.error("删除失败，请重试！");
      }
    },
    refreshData() {
      if (this.activeTab === 'house') {
        this.rent_load();
      } else if (this.activeTab === 'demand') {
        this.need_load();
      } else if (this.activeTab === 'talk') {
        this.talk_load();
      }
    },
    scrollToTop() {
      window.scrollTo({top: 0, behavior: 'smooth'});
    },
    openModal(type) {
      this.activeModal = type;
      this.showModal = true;
      document.body.style.overflow = "hidden";
      this.resetForm();
    },

    closeModal() {
      document.body.style.overflow = "";
      this.showModal = false;
    },

    async handleSubmit() {
      if (this.activeModal === "house" && !this.isAreaValid) {
        alert("请输入有效的面积，最多两位小数。");
        return;
      }
      if (this.activeModal === "house" && this.form.pictures.length === 0) {
        alert("请上传至少一张图片。");
        return;
      }
      try {
        const type = this.activeTab === 'house' ? 1 : this.activeTab === 'demand' ? 2 : 3;
        const apiMap = {
          1: '/api/house_rent',
          2: '/api/house_need',
          3: '/api/house_talk',
        };
        this.form.author = this.user.id;
        this.form.name = this.user.name;
        this.form.image = this.user.image;
        if (this.activeTab !== 'talk'){
          this.form.phone = this.user.phone;
        }
        let response = await request.post(apiMap[type], JSON.stringify(this.form))
        console.log(response)
        if (response.code === '0') {
          this.$message.success("发布成功！");
          this.refreshData();
          this.cancelBatchDelete();
          this.closeModal();
        } else {
          this.$message.error(response.code);
        }
      } catch (error) {
        this.$message.error("发布失败，请重试！");
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
    resetForm() {
      this.form = {
        title: "",
        content: "",
        tips: [],
        pictures: []
      };
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
  }
};
</script>

<style scoped>
.profile-container {
  display: flex;
  margin-top: 20px;
  justify-content: center;
}

.profile-main {
  width: 60%;
  padding: 20px;
  background-color: #f9f9f9;
  border-radius: 8px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
}

.profile-header {
  display: flex;
  align-items: flex-start;
  margin-bottom: 20px;
}

.avatar-container {
  position: relative;
  margin-right: 20px; /* 头像和右侧信息之间的间隔 */
}

.avatar {
  width: 100px;
  height: 100px;
  border-radius: 50%;
}

.change-avatar-button {
  position: absolute;
  bottom: 0;
  left: 0;
  width: 100%;
  background-color: #007bff;
  color: white;
  border: none;
  padding: 8px;
  cursor: pointer;
}

.user-info {
  display: flex;
  flex-direction: column;
  justify-content: space-between;
  width: 100%;
}

.user-info h2 {
  font-size: 24px;
  margin: 0;
}

.contact-info {
  display: flex;
  justify-content: space-between;
  margin-top: 20px;
}

.contact-info .left {
  display: flex;
  flex-direction: column;
  justify-content: flex-start;
}

.contact-info .right {
  display: flex;
  flex-direction: row;
  justify-content: flex-start;
  width: 50%;
  margin-bottom: 10px;
}

.edit-button {
  color: white;
  border: none;
  padding: 10px 20px;
  cursor: pointer;
  margin-left: 20px;
}


.avatar-modal {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(0, 0, 0, 0.5); /* 半透明背景 */
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 1000;
}

h3 {
  font-size: 22px;
  color: #333;
  margin-bottom: 20px;
  font-weight: bold;
}

.file-upload {
  margin-bottom: 20px;
}

.file-input {
  display: none;
}

.avatar-preview {
  margin-top: 20px;
}

.avatar {
  width: 120px;
  height: 120px;
  border-radius: 50%;
  object-fit: cover;
  border: 2px solid #ddd;
  transition: all 0.3s ease;
}

.avatar-preview:hover .avatar {
  transform: scale(1.05);
  border-color: #007bff;
}

.button-group {
  display: flex;
  justify-content: space-between;
  margin-top: 20px;
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

h3 {
  font-size: 22px;
  color: #333;
  margin-bottom: 20px;
  font-weight: bold;
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

.button-group {
  display: flex;
  justify-content: space-between;
  margin-top: 20px;
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

input[type="file"] {
  margin-bottom: 20px;
}

.cancel-button {
  background-color: #ccc;
  color: white;
  border: none;
  padding: 10px 20px;
  cursor: pointer;
}

.tabs {
  margin-bottom: 20px;
}

.tabs span {
  margin-right: 20px;
  cursor: pointer;
  padding-bottom: 5px;
}

.active-tab {
  border-bottom: 2px solid #007bff;
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
}

.rent-listing-image {
  width: 100%;
  height: 200px;
  object-fit: cover;
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
  z-index: 998;
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

.rent-image-container {
  position: relative;
  overflow: hidden;
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

.need-listing {
  margin-bottom: 20px;
}

.need-listing-image {
  width: 100%;
  height: auto;
  border-radius: 8px;
  object-fit: cover;
}

.need-listing-info {
  padding: 15px;
  text-align: left;
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
  z-index: 998;
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

.talk-item-list {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.talk-item-card {
  display: flex;
  width: 100%;
  height: auto;
  max-height: 300px;
  border: 1px solid #ddd;
  border-radius: 8px;
  overflow: hidden;
  padding: 10px;
  background-color: #fff;
  position: relative;
}

.talk-item-left {
  flex: 0 0 250px;
}

.talk-item-image {
  width: 100%;
  height: auto;
  border-radius: 8px;
}

.talk-item-middle {
  flex: 0 0 250px;
  display: flex;
  flex-direction: column;
  justify-content: space-between;
  margin-left: 40px;
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

.talk-view-more {
  color: #5656e1;
  text-decoration: underline;
  cursor: pointer;
}

.talk-modal-overlay {
  position: fixed;
  top: 60px;
  left: 0;
  width: 100%;
  height: 95%;
  background: rgba(0, 0, 0, 0.5);
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 998;
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

.tab-and-batch-container {
  display: flex;
  justify-content: space-between; /* 左右分布 */
  align-items: center; /* 垂直居中 */
  margin-bottom: 16px; /* 和下面的内容留出间距 */
}

.tabs {
  display: flex;
  gap: 16px; /* 标签之间的间距 */
}

.active-tab {
  font-weight: bold;
  color: #007bff; /* 激活标签颜色 */
  cursor: pointer;
}

.tabs span {
  padding: 4px 8px;
  border-radius: 4px;
  cursor: pointer;
}

.tabs span:hover {
  background-color: #f0f0f0;
}

.batch-delete-container {
  display: flex;
  gap: 8px; /* 按钮之间的间距 */
}

.batch-select-all-button {
  padding: 6px 12px;
  border: none;
  border-radius: 4px;
  background-color: #f0ad4e; /* 黄色按钮 */
  color: white;
  cursor: pointer;
}

.batch-select-all-button:hover {
  background-color: #ec971f; /* 深黄色悬停 */
}

.batch-delete-confirm-button {
  padding: 6px 12px;
  border: none;
  border-radius: 4px;
  background-color: #d9534f; /* 红色按钮 */
  color: white;
  cursor: pointer;
}

.batch-delete-confirm-button:hover {
  background-color: #c9302c; /* 深红色悬停 */
}

.batch-cancel-button {
  padding: 6px 12px;
  border: none;
  border-radius: 4px;
  background-color: #5bc0de; /* 绿色按钮 */
  color: white;
  cursor: pointer;
}

.batch-cancel-button:hover {
  background-color: #31b0d5; /* 深绿色悬停 */
}

.batch-delete-actions {
  display: flex;
  gap: 8px; /* 操作按钮之间的间距 */
}

.item-checkbox {
  position: absolute;
  top: 10px;
  right: 10px;
  transform: translate(-5px, 5px);
  width: 25px;
  height: 25px;
  appearance: none;
  border: 2px solid #dcdfe6;
  border-radius: 50%;
  background-color: white;
  cursor: pointer;
  transition: all 0.3s ease;
  z-index: 10;
}

.item-checkbox:checked {
  background-color: #409eff;
  border-color: #409eff;
}

.item-checkbox:checked::after {
  content: "";
  position: absolute;
  top: 6px;
  left: 8px;
  width: 6px;
  height: 12px;
  border: solid #fff;
  border-width: 0 2px 2px 0;
  transform: rotate(45deg);
}

.item-checkbox:hover {
  border-color: #85d7ff;
}

.batch-delete-container {
  display: flex;
  align-items: center;
  gap: 10px; /* 按钮之间的间距 */
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

.publish-buttons {
  display: flex;
  gap: 10px;
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
}

.publish-button {
  background-color: #007bff;
  color: white;
  border: none;
  padding: 10px 20px;
  cursor: pointer;
  transition: background-color 0.3s;
  border-radius: 8px;
}

.publish-button:hover {
  background-color: #0056b3;
}

.batch-delete-button {
  background-color: #c90000;
  color: white;
  border: none;
  padding: 10px 20px;
  cursor: pointer;
  transition: background-color 0.3s;
  border-radius: 8px;
}

.batch-delete-button:hover {
  background-color: #960202;
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

/* 添加标签按钮样式 */
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

</style>
