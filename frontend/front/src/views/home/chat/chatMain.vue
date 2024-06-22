<template>
    <div>
        <div class="line">
            <el-row :gutter="20">
                <el-col :span="4">
                    <div class="grid-content bg-purple"></div>
                </el-col>
                <el-col :span="16">
                    <div class="grid-content bg-purple">
                        <h1>歪歪聊天室</h1>
                    </div>
                </el-col>
                <el-col :span="4">
                    <el-dropdown trigger="click" @command="handleCommand">
                      <span class="el-dropdown-link">
                        {{user.username}}<i class="el-icon-caret-bottom el-icon--right"></i>
                      </span>
                        <el-dropdown-menu slot="dropdown">
                            <el-dropdown-item command="myApply" class="clearfix">
                                我的申请
                            </el-dropdown-item>
                            <el-dropdown-item command="apply" class="clearfix">
                                消息
                                <el-badge v-if="applyList.length>0" class="mark" :value="applyList.length"/>
                            </el-dropdown-item>
                            <el-dropdown-item command="unLogin" class="clearfix">
                                退出登录
                            </el-dropdown-item>
                        </el-dropdown-menu>
                    </el-dropdown>
                </el-col>
            </el-row>
        </div>
        <div class="talk_body">
            <!--左边的-->
            <div class="talk_left">

                <!--最上面加搜索和添加联系人-->
                <div class="left_top">
                    <el-select
                            v-model="value"
                            filterable
                            remote
                            placeholder="搜索（待开发）"
                            :remote-method="remoteMethod"
                            :loading="loading"
                            @change="changePeople"
                    >
                        <el-option
                                v-for="item in options"
                                :key="item.value"
                                :label="item.label"
                                :value="item.value">
                        </el-option>
                    </el-select>
                    <div class="search_icon" @click="addPeople()">
                        <i class="el-icon-plus"></i>
                    </div>
                </div>
                <!--下面为我的联系人列表-->
                <div class="my_people_list">
                    <div class="my_people" v-for="peo in pList" :key="peo.no">
                        <div class="people_label" @click="selectPeople(peo)"
                             :style="{'background-color':peo.no==activePeo.no?' rgba(119, 136, 153, 0.2)':''}">
                            <!--头像用首字母-->
                            <div class="people_header"
                                 :style="{'background-color':peo.isRun == 1?(peo.sex=='女'?'#ff00006b':'deepskyblue'):'#bbbbbb'}">
                                {{peo.name.substring(0,1)}}
                            </div>
                            <div class="people_name">{{peo.name}}</div>
                            <el-badge class="mark" v-show="peo.noReadNum>0" :value="peo.noReadNum"/>
                        </div>
                    </div>
                </div>
            </div>
            <!--右边的-->
            <div class="talk_right">
                <!--聊天区域-->
                <div class="right_record">
                    <div class="left_top" style="padding-left: 20px">
                        {{activePeo.name}}
                    </div>
                    <!--聊天内容区域-->
                    <div class="right_talk" ref="mianscroll">
                        <div v-for="(talk) in activePeo.talkList" :key="talk.id" class="talk_label">
                            <div v-if="talk.fromno==user.no" class="self_talk">
                                <div class="self_con">{{talk.sendtext}}</div>
                                <div class="triangle"></div>
                                <div class="talk_who people_header"
                                     :style="{'background-color':user.sex=='女'?'#ff00006b':'deepskyblue'}">
                                    {{user.username.substring(0,1)}}
                                </div>
                            </div>
                            <div v-else class="other_talk">
                                <div class="talk_who people_header"
                                     :style="{'background-color':activePeo.sex=='女'?'#ff00006b':'deepskyblue'}">
                                    {{activePeo.name.substring(0,1)}}
                                </div>
                                <div class="triangle"></div>
                                <div class="self_con">{{talk.sendtext}}</div>
                            </div>
                        </div>
                    </div>
                </div>
                <!--输入内容区域-->
                <div class="right_input">
                    <el-popover
                            placement="top"
                            width="200"
                            trigger="click">
                        <div>
                            <div v-for="emo in emojiArr" :key="emo.className" class="emo_label"
                                 @click="selectEmo(emo.icon)">
                                {{emo.icon}}
                            </div>
                        </div>
                        <div class="input_icon" title="点击选择表情" slot="reference">
                            <!--表情-->
                            {{emojiArr[0].icon}}
                        </div>
                    </el-popover>
                    <div class="input_contain">
                        <!--内容输入-->
                        <el-input
                                :disabled="!!!activePeo.no"
                                type="textarea"
                                :autosize="{ minRows: 4, maxRows: 4}"
                                placeholder="请输入内容"
                                v-model="myMsg"
                        >
                        </el-input>
                    </div>
                    <div class="input_btn">
                        <!--确定按钮-->
                        <el-button size="small" @click="sendMsg()">发送</el-button>
                    </div>
                </div>
            </div>
        </div>
        <AddPeople ref="addPeople"></AddPeople>
        <MyMessage @getApplyList="getApplyList" ref="myMessage"></MyMessage>
        <MyApply ref="myApply"></MyApply>
    </div>
</template>

<script>
  import { emojiArr } from '@/assets/js/emojiArr'; //引入js文件
  import AddPeople from './modules/AddPeople';
  import MyMessage from './modules/MyMessage';
  import MyApply from './modules/MyApply';
  import { getApply } from '../../api/apply';
  import { getFriend } from '../../api/friends';
  import { getRecordByNo, updateRecord, updateRecordByNo } from '../../api/talk_record';

  export default {
    name: 'HelloWorld',
    props: {
      msg: String
    },
    components: {
      AddPeople,
      MyMessage,
      MyApply
    },
    data() {
      return {
        user: JSON.parse(localStorage.getItem('sessionUser')),
        emojiArr,
        ws: null,
        myMsg: '',
        isConnected: false,
        heartbeatInterval: null, // 保存心跳定时器的ID
        heartbeatTimeout: 50000, // 心跳间隔时间
        activePeo: {

        },//当前选中的
        pList: [],
        //搜索联系人--开始
        options: [],
        value: [],
        list: [],
        loading: false,
        states: ['Alabama', 'Alaska', 'Arizona',
          'Arkansas', 'California', 'Colorado',
          'Connecticut', 'Delaware', 'Florida',
          'Georgia', 'Hawaii', 'Idaho', 'Illinois',
          'Indiana', 'Iowa', 'Kansas', 'Kentucky',
          'Louisiana', 'Maine', 'Maryland',
          'Massachusetts', 'Michigan', 'Minnesota',
          'Mississippi', 'Missouri', 'Montana',
          'Nebraska', 'Nevada', 'New Hampshire',
          'New Jersey', 'New Mexico', 'New York',
          'North Carolina', 'North Dakota', 'Ohio',
          'Oklahoma', 'Oregon', 'Pennsylvania',
          'Rhode Island', 'South Carolina',
          'South Dakota', 'Tennessee', 'Texas',
          'Utah', 'Vermont', 'Virginia',
          'Washington', 'West Virginia', 'Wisconsin',
          'Wyoming'],
        //搜索联系人--结束
        applyList: [],//申请我的
        applyAllList: [],//所有申请
      };
    },
    mounted() {
      this.list = this.states.map(item => {
        return { value: `value:${item}`, label: `label:${item}` };
      });
      this.getApplyList();
      //加载联系人
      this.getFriendList();

      //链接websocket
      this.initConnect();
    },

    methods: {
      //查询自己的所有好友
      getFriendList() {
        getFriend().then(res => {
          if (res.success) {
            this.pList = res.data;
          }
        });
      },

      getApplyList() {
        //我的申请
        getApply().then(res => {
          if (res.success) {
            this.applyAllList = res.data;
            this.applyList = res.data.filter(item => item.status == 0);
          }
        });
      },
      //弹出新增联系人页面
      addPeople() {
        this.$refs.addPeople.open();
      },
      //列表选中
      selectPeople(obj) {
        // if(obj.no = this.activePeo.no){
        //   return
        // }
        this.activePeo = {
          no:obj.no,
          sex:obj.sex,
          name:obj.name,
          talkList:[]
        };
        let param = {
          pageNo:1,
          pageSize:9999,
          sortField:'sendtime',
          sortOrder:'descend',
          search: `(fromno=${this.user.no} and tono=${obj.no}) or (fromno=${obj.no} and tono=${this.user.no}) `
        }
        //查询聊天记录
        getRecordByNo(param).then(res=>{
          if(res.success){
            this.activePeo.talkList = res.data.data
          }
          this.$forceUpdate();
          this.$nextTick(() => {
            let scrollEl = this.$refs.mianscroll;
            scrollEl.scrollTo({ top: scrollEl.scrollHeight, behavior: 'smooth' });
          });
          console.log("this.activePeo",this.activePeo);
        });
        //选择当前的人，将当前发送者为选中的人，接受者为自己的记录全部改成已读
        updateRecordByNo({fromno:obj.no,isread:1}).then(res=>{
          this.pList.map(item=>{
            if(item.no == obj.no){
              item.noReadNum = 0;
            }
          });
          this.$forceUpdate();
          console.log(res);
        })
      },
      //搜索的时候选中的
      changePeople(e) {
        console.log('选中；了', e);
      },
      remoteMethod(query) {
        if (query !== '') {
          this.loading = true;
          setTimeout(() => {
            this.loading = false;
            this.options = this.list.filter(item => {
              return item.label.toLowerCase()
                .indexOf(query.toLowerCase()) > -1;
            });
          }, 200);
        } else {
          this.options = [];
        }
      },
      sendMsg() {
        //如果没有输入则不发信息
        if (!this.myMsg.trim()) return;
        this.activePeo.talkList.push({
          fromno: this.user.no,
          sendtext: this.myMsg,
          tono:this.activePeo.no
        });

        this.$nextTick(() => {
          let scrollEl = this.$refs.mianscroll;
          scrollEl.scrollTo({ top: scrollEl.scrollHeight, behavior: 'smooth' });
        });
        let sendObj = {
          to:this.activePeo.no,
          text:this.myMsg.trim()
        };
        this.ws.send(JSON.stringify(sendObj));
        this.myMsg = '';
        this.$nextTick(() => {
          let scrollEl = this.$refs.mianscroll;
          scrollEl.scrollTo({ top: scrollEl.scrollHeight, behavior: 'smooth' });
        });
      },
      initConnect() {
        this.ws = new WebSocket(`ws://localhost:8080/talk/imserver/` + this.user.no);//websocket连接地址
        this.ws.onopen = () => {
          console.log('WebSocket连接已建立');
          this.isConnected = true;
          // 建立心跳定时器
          this.heartbeatInterval = setInterval(() => {
            if (this.ws.readyState === 1) {
              this.ws.send(JSON.stringify({ type: 'heartbeat' }));
              console.log('心跳开启');
            }
          }, this.heartbeatTimeout);
        };
        this.ws.onmessage = (event) => {
          const dataMessage = JSON.parse(event.data);
          if (dataMessage.type && dataMessage.type == 'heartbeat') {
            //心跳
            console.log('接收的是心跳');
            return;
          } else if (dataMessage.type && dataMessage.type == 'connect') {
            //连接成功的信息
            return;
          } else if (dataMessage.type && dataMessage.type == 'up') {
            //好友上线的信息
            this.pList.map(item=>{
              if(item.no == dataMessage.no){
                item.isRun = 1;
                this.$notify({
                  title: '上线通知',
                  message: '您的好友"'+item.name+'"已上线',
                  position: 'bottom-right',
                  type:'success'
                });
              }
            });
            return;
          } else if (dataMessage.type && dataMessage.type == 'down') {
            //好友下线的信息
            this.pList.map(item=>{
              if(item.no == dataMessage.no){
                item.isRun = 0;
              }
            });
            return;
          }
          //如果当前聊天的人不是发信息的人，则需要给下标且好友中的noReadNum需要加1
          //如果是当前聊天的人  则发请求将聊天记录未读信息设置为已读
          //当前聊天的id由后台给前端dataMessage.id
          console.log("this.activePeo",this.activePeo);
          if(dataMessage.fromno != this.activePeo.no){
            this.pList.map(item=>{
              if(item.no == dataMessage.fromno){
                item.noReadNum += 1;
              }
            })
          }else{
            //接收的是内容 from  text
            this.activePeo.talkList.push(dataMessage);
            updateRecord({id:dataMessage.id,isread:1}).then(res=>{
                console.log(res);
            })
          }

          this.$forceUpdate();
          this.$nextTick(() => {
            let scrollEl = this.$refs.mianscroll;
            scrollEl.scrollTo({ top: scrollEl.scrollHeight, behavior: 'smooth' });
          });
          console.log(dataMessage, '接收到的信息');
        };
        this.ws.onerror = () => {
          console.error('WebSocket连接发生错误');
        };
        this.ws.onclose = () => {
          console.log('WebSocket连接已关闭');
          this.ws.send('close');
          this.isConnected = false;
          // 清除心跳定时器
          clearInterval(this.heartbeatInterval);
        };
      },
      selectEmo(icon) {
        this.myMsg += icon;
      },
      //下拉菜单点击指令
      handleCommand(command) {
        switch (command) {
          case 'unLogin':
            this.unLogin();
            break;
          case 'myApply':
            this.myApply();
            break;
          case 'apply':
            this.apply();
            break;
          default:
            console.log('指令不存在');
            break;
        }
      },
      //查看我的申请
      myApply() {
        this.$refs.myApply.open()
      },
      //查看我的消息
      apply() {
        this.$refs.myMessage.open();
      },
      //退出登录
      unLogin() {
        this.$confirm('确定退出登录吗?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          localStorage.removeItem('sessionUser');
          localStorage.removeItem('token');
          this.$router.push('/login');
          this.ws.onclose()
        }).catch(() => {

        });
      }
    }
  };
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
    * {
        padding: 0;
        margin: 0;
    }

    .talk_body {
        margin: 5% auto auto;
        box-shadow: 4px 4px 10px 10px #dfdede73;
        border-radius: 10px;
        display: flex;
        width: 800px;
        height: 600px;
        padding: 10px
    }

    .talk_left {
        width: 25%;
        height: 100%;
        border-right: 1px #e7e7e7 solid;
    }

    .left_top {
        height: 50px;
        text-align: left;
        display: flex;
        align-items: center;
        border-bottom: 1px #dfdede73 solid;
    }

    .search_icon {
        background-color: #a1a1a194;
        border-radius: 5px;
        width: 25px;
        height: 25px;
        text-align: center;
        line-height: 1.6;
        cursor: pointer;
        margin-left: 20px;
    }

     .left_top .el-select {
        width: 70%;
    }

   .left_top .el-select .el-input input {
        width: 100%;
        height: 30px;
        background: #ededed4f;
    }

    .talk_right {
        width: 75%;
        height: 100%;
    }

    .right_record {
        height: 75%;
        border-bottom: 1px #e7e7e7 solid;
    }

    .right_input {
        height: 25%;
        padding: 5px
    }

    .input_icon {
        text-align: left;
        width: fit-content;
    }

   .input_contain textarea {
        border: none;
    }

    .input_btn {
        text-align: right;
    }

    .my_people {
        margin: 2px 0px;
    }

    .people_label {
        text-align: left;
        display: flex;
        padding: 3px;
        cursor: pointer;
    }

    .people_label:hover {
        background-color: rgba(119, 136, 153, 0.2);
    }

    .people_header {
        color: #fff;
        padding: 8px;
        border-radius: 5px;
        width: 25px;
        height: 25px;
        font-size: 20px;
        text-align: center;
    }

    .people_name {
        margin-left: 10px;
        align-self: center;
    }

    .right_talk {
        height: 400px;
        overflow-y: scroll;
        padding: 0px 5px 0px 10px;

    }

    .talk_label {
        margin: 15px 0px;
        width: 100%;
    }

    /*聊天内容*/
    .self_con {
        background-color: #caecff;
        padding: 8px;
        border-radius: 5px;
        align-self: center;
    }

    .self_talk {
        width: 100%;
        text-align: right;
        display: flex;
        justify-content: flex-end;
    }

    .other_talk {
        width: 100%;
        text-align: left;
        display: flex
    }

    .self_talk .triangle {
        width: 0px;
        height: 0px;
        border: 8px solid transparent;
        margin-top: 10px;
        border-left-color: #caecff;
    }

    .other_talk .triangle {
        width: 0px;
        height: 0px;
        border: 8px solid transparent;
        margin-top: 10px;
        border-right-color: #caecff;
    }

    .emo_label {
        float: left;
        cursor: pointer;
    }

    .my_people_list .el-badge {
        align-self: center;
        margin-left: auto;
    }

    .el-row {
        margin-bottom: 20px;

    &
    :last-child {
        margin-bottom: 0;
    }

    }
    .el-col {
        border-radius: 4px;
    }

    .grid-content {
        border-radius: 4px;
        min-height: 36px;
    }

    .row-bg {
        padding: 10px 0;
    }

    h1 {
        font-family: fangsong;
        letter-spacing: 10px;
        font-weight: bold;
        font-size: 40px;
        color: cornflowerblue;
    }
</style>
