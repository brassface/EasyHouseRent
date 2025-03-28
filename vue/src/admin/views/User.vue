<template>
  <div style="padding-left:10px; padding-right: 10px; width: calc(100vw - 200px)">
    <h2 style="color: #7bc3f6; font-family: Georgia; margin-bottom: 20px;">
      用户管理
    </h2>
    <div style="margin: 10px 0">
      <el-input v-model="search" clearable placeholder="请输入关键字" style="width: 20%"></el-input>
      <el-button plain style="margin-left: 5px; background-color: #5cfcfe; color: white" @click="load">查询</el-button>
      <el-button plain type="warning" @click="add">新增</el-button>
    </div>
    <el-table :data="tableData" border stripe style="width: 100%">
      <el-table-column
          label="用户名"
          prop="username">
      </el-table-column>
      <el-table-column
          label="姓名"
          prop="name">
      </el-table-column>
      <el-table-column
          label="地址"
          prop="address">
      </el-table-column>
      <el-table-column fixed="right" label="操作">
        <template #default="scope">
          <div style="display: flex; align-content: space-between">
            <el-button type="primary" @click="details(scope.row)">详情</el-button>
            <el-button v-if="user.role===1" type="primary" @click="handleEdit(scope.row)">编辑</el-button>
            <el-popconfirm
                v-if="user.role===1" title="确定删除吗？" @confirm="handleDelete(scope.row.id)">
              <template #reference>
                <el-button type="danger">删除</el-button>
              </template>
            </el-popconfirm>
          </div>
        </template>
      </el-table-column>
    </el-table>
    <el-pagination
        :current-page="currentPage"
        :page-size="pageSize"
        :page-sizes="[5, 10, 20]"
        :total="total"
        background
        layout="total, sizes, prev, pager, next, jumper"
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange">
    </el-pagination>
    <el-dialog
        v-model="dialogVisible"
        title=""
        width="70%">
      <el-form :model="form" label-width="120px">
        <el-form-item label="用户名">
          <el-input v-model="form.username" style="width: 50%"></el-input>
        </el-form-item>
        <el-form-item label="姓名">
          <el-input v-model="form.name" style="width: 50%"></el-input>
        </el-form-item>
        <el-form-item label="手机号码">
          <el-input v-model="form.phone" style="width: 50%"></el-input>
        </el-form-item>
        <el-form-item label="地址">
          <el-input v-model="form.address" style="width: 50%"></el-input>
        </el-form-item>
      </el-form>
      <el-form :model="form" label-width="120px">
        <el-form-item label="头像">
          <el-upload ref="upload" :on-success="filesUploadSuccess" action="/api/files/upload">
            <el-button type="primary">点击上传</el-button>
          </el-upload>
        </el-form-item>
      </el-form>
      <template #footer>
          <span class="dialog-footer">
            <el-button @click="dialogVisible = false">取 消</el-button>
            <el-button type="primary" @click="save">确 定</el-button>
          </span>
      </template>
    </el-dialog>
    <el-dialog
        v-model="vis"
        title="详情"
        width="40%">
      <el-form :model="form" label-width="80px">
        <el-form-item label="用户名">
          <el-input v-model="form.username" readonly style="width: 80%"></el-input>
        </el-form-item>
        <el-form-item label="姓名">
          <el-input v-model="form.name" readonly style="width: 80%"></el-input>
        </el-form-item>
        <el-form-item label="手机号码">
          <el-input v-model="form.phone" readonly style="width: 80%"></el-input>
        </el-form-item>
        <el-form-item label="地址">
          <el-input v-model="form.address" readonly style="width: 80%"></el-input>
        </el-form-item>
        <el-form-item label="头像">
          <el-image :src="form.image" style="width: 50%"></el-image>
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
            <el-button @click="vis = false">取 消</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script>
import request from "../../utils/request";

export default {
  name: 'User',
  data() {
    return {
      search: '',
      tableData: [],
      total: 0,
      currentPage: 1,
      pageSize: 10,
      form: {},
      user: {},
      detail: {},
      vis: false,
      dialogVisible: false,
    }
  },
  created() {
    const userStr = sessionStorage.getItem("user") || '{}';
    this.user = JSON.parse(userStr);
    this.load();
  },
  methods: {
    load() {
      request.get(`/api/user`, {
        params: {
          pageNum: this.currentPage,
          pageSize: this.pageSize,
          search: this.search
        }
      }).then(res => {
        this.tableData = res.data.records;
        this.total = res.data.total;
      });
    },
    add() {
      this.dialogVisible = true;
      this.form = {};
      this.$nextTick(() => {
        if (this.$refs['upload']) {
          this.$refs['upload'].clearFiles();//清楚历史图片
        }
      });
    },
    handleEdit(row) {
      this.form = JSON.parse((JSON.stringify(row)));
      this.dialogVisible = true;
      this.$nextTick(() => {
        if (this.$refs['upload']) {
          this.$refs['upload'].clearFiles();//清楚历史图片
        }
      });
    },
    save() {
      if (this.form.id) {//更新
        request.put("/api/user", this.form).then(res => {
          console.log(res);
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
        request.post("/api/user", this.form).then(res => {
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
          this.dialogVisible = false;
        });
      }
    },
    handleDelete(id) {
      request.delete(`/api/user/` + id).then(res => {
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
    filesUploadSuccess(res) {
      this.form.image = res.data;
      this.load();
    },
  }
}
</script>

<style scoped>
</style>
