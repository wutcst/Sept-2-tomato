<template>
    <div>
    <el-dialog
            title="我的申请"
            :visible.sync="dialogVisible"
            :before-close="handleClose">
        <el-table
                :data="dataSource"
                style="width: 100%">
            <el-table-column
                    prop="userno"
                    label="申请人"
                    width="180">
                <template slot-scope="scope">
                    <div title="点击查看用户信息">
                        <el-button type="text" size="small" @click="showPeople(scope.row.applyno)">{{scope.row.applyno}}</el-button>
                    </div>
                </template>
            </el-table-column>
            <el-table-column
                    prop="inputtime"
                    label="申请时间"
                    width="180">
            </el-table-column>
            <el-table-column
                    prop="remarke"
                    label="备注">
            </el-table-column>
            <el-table-column
                    prop="status"
                    label="操作">
                <template slot-scope="scope">
                    <div>
                        <el-button type="info" plain size="small" v-if="scope.row.status == 0">未回复</el-button>
                        <el-button type="success" size="small" v-if="scope.row.status == 1">已同意</el-button>
                        <el-button type="danger" size="small" v-if="scope.row.status == 2">已拒绝</el-button>
                    </div>

                </template>
            </el-table-column>
        </el-table>
        <span slot="footer" class="dialog-footer">
            <el-button type="primary" @click="dialogVisible = false">确 定</el-button>
        </span>

    </el-dialog>
        <el-dialog
                title="个人信息"
                :visible.sync="peopleShow"
                width="400px"
        >
            <el-form :model="showPeopleObj">
                <el-form-item label="歪歪号">
                    <el-input v-model="showPeopleObj.no" readOnly autocomplete="off"></el-input>
                </el-form-item>
                <el-form-item label="用户名">
                    <el-input v-model="showPeopleObj.name" readOnly autocomplete="off"></el-input>
                </el-form-item>
                <el-form-item label="性别">
                    <el-input v-model="showPeopleObj.sex" readOnly autocomplete="off"></el-input>
                </el-form-item>
            </el-form>
            <span slot="footer" class="dialog-footer">
                    <el-button type="primary" @click="peopleShow = false">确 定</el-button>
                </span>
        </el-dialog>
    </div>
</template>
<script>
  import { getMyApply } from '../../../api/apply';
  import { getByNo } from '../../../api/user';

  export default {
    data() {
      return {
        dialogVisible: false,
        dataSource:[],
        peopleShow:false,
        showPeopleObj:{}
      };
    },
    methods: {
      showPeople(no){
        this.peopleShow = true;
        getByNo(no).then(res=>{
          this.showPeopleObj = res.data;
        })
      },
      handleClose(done) {
        done();
      },
      open(){
        this.getMyApplyList();
        this.dialogVisible = true;
      },
      getMyApplyList() {
        //加载我的消息
        getMyApply().then(res => {
          if (res.success) {
            this.dataSource = res.data;
          }
        });
      },
    }
  };
</script>
