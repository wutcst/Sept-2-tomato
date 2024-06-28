<template>
    <el-dialog
            title="添加联系人"
            :visible.sync="dialogVisible"
            width="450px"
            :before-close="handleClose">

        <el-input
                placeholder="请输入歪歪号"

                v-model="searchValue"
        >
            <el-button slot="append" @click="searchAllPeople" icon="el-icon-search"></el-button>
        </el-input>
        <div v-if="showResult">
            <!--搜索结果-->
            <div v-if="searchResult" class="search_people">
                歪歪号：{{searchResult.no}} &emsp;
                姓名：{{searchResult.name}} &emsp;
                性别：{{searchResult.sex}}&emsp;
                <el-button size="small" @click="applySubmit">申请</el-button>
            </div>
            <div v-else>查无此人</div>
        </div>


        <span slot="footer" class="dialog-footer">
            <el-button type="primary" @click="dialogVisible = false">确 定</el-button>
        </span>
    </el-dialog>
</template>

<script>
  import { addApply } from '../../../api/apply';
  import { getByNo } from '../../../api/user';

  export default {
    data() {
      return {
        user: JSON.parse(localStorage.getItem('sessionUser')),
        searchValue: '',
        dialogVisible: false,
        searchResult: null,
        showResult: false,
      };
    },
    methods: {
      applySubmit() {
        this.$prompt('请输入申请信息?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
        }).then(({ value }) => {
          let apply = {
            userno: this.user.no,
            applyno: this.searchResult.no,
            remarke: value,
            status: 0
          };
          addApply(apply).then(res => {
            if (res.success) {
              this.$message.success('已成功发送申请');
            } else {
              this.$message.error(res.msg);
            }
          });
        }).catch(() => {

        });
      },
      //搜索联系人
      searchAllPeople() {
        if (this.searchValue.trim() && this.searchValue.trim() != this.user.no) {
          //输入了才有值
          getByNo(this.searchValue).then(res => {
            if (res.success) {
              this.searchResult = res.data;
            }
          });
        }
        this.showResult = true;
      },
      open() {
        this.dialogVisible = true;
      },
      handleClose(done) {
        this.searchValue = '';
        this.searchResult = {};
        this.showResult = false;
        this.dialogVisible = false;
      }
    }
  };
</script>
<style scoped>
    .search_people {
        background-color: #caecff;
        padding: 15px 0px;
    }
</style>
