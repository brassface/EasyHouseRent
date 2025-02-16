<template>
  <div class="statistics-page">
    <el-row :gutter="30">
      <el-col :span="8" style="min-width: 100%">
        <el-card>
          <h3>房源城市统计</h3>
          <div id="rent-chart" style="height: 300px"></div>
        </el-card>
      </el-col>
      <el-col :span="8" style="min-width: 100%">
        <el-card>
          <h3>房源标签统计</h3>
          <div id="rent-tips-chart" style="height: 300px;"></div>
        </el-card>
      </el-col>
      <el-col :span="8" style="min-width: 100%">
        <el-card>
          <h3>需求城市统计</h3>
          <div id="need-chart" style="height: 300px;"></div>
        </el-card>
      </el-col>
      <el-col :span="8" style="min-width: 100%">
        <el-card>
          <h3>需求标签统计</h3>
          <div id="need-tips-chart" style="height: 300px;"></div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>


<script>
import * as echarts from 'echarts';
import request from '@/utils/request';

export default {
  name: "StatisticsPage",
  data() {
    return {
      charts: {},
    };
  },
  async mounted() {
    await this.loadStatistics('rent');
    await this.loadStatistics('need');
  },
  methods: {
    async loadStatistics(tableName) {
      try {
        const res = await request.get(`/api/house_${tableName}/statistics`);
        if (res.code === '0') {
          this.renderChart(tableName+'-chart', res.data);
          this.renderChart(tableName+'-tips-chart', res.data);
        } else {
          this.$message.error(`获取 ${tableName} 统计数据失败: ${res.message}`);
        }
      } catch (error) {
        console.error(error);
        this.$message.error(`获取 ${tableName} 统计数据时发生错误`);
      }
    },
    renderChart(chartId, data) {
      const chart = echarts.init(document.getElementById(chartId));
      if (chartId === 'rent-tips-chart') {
        const option = {
          tooltip: { trigger: 'axis' },
          xAxis: {
            type: 'category',
            data: Object.keys(data.tips_count),
          },
          yAxis: { type: 'value' },
          series: [
            {
              name: '标签名称',
              type: 'bar',
              data: Object.values(data.tips_count),
            },
          ],
        };
        chart.setOption(option);
      } else if (chartId === 'need-tips-chart') {
        const option = {
          tooltip: { trigger: 'axis' },
          xAxis: {
            type: 'category',
            data: Object.keys(data.tips_count),
          },
          yAxis: { type: 'value' },
          series: [
            {
              name: '标签名称',
              type: 'bar',
              data: Object.values(data.tips_count),
            },
          ],
        };
        chart.setOption(option);
      } else if (chartId === 'rent-chart') {
        const option = {
          tooltip: { trigger: 'item' },
          xAxis: {
            type: 'category',
            data: data.city_count.map(item => item.city),
          },
          yAxis: { type: 'value' },
          series: [
            {
              name: '城市名称',
              type: 'bar',
              data: data.city_count.map(item => item.count),
            },
          ],
        };
        chart.setOption(option);
      } else if (chartId === 'need-chart') {
        const option = {
          tooltip: { trigger: 'item' },
          xAxis: {
            type: 'category',
            data: data.city_count.map(item => item.city),
          },
          yAxis: { type: 'value' },
          series: [
            {
              name: '城市名称',
              type: 'bar',
              data: data.city_count.map(item => item.count),
            },
          ],
        };
        chart.setOption(option);
      }
      this.charts[chartId] = chart;
    }


  },
};
</script>

<style scoped>
.statistics-page {
  padding: 20px;
}
</style>
