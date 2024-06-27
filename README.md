··[![Review Assignment Due Date](https://classroom.github.com/assets/deadline-readme-button-22041afd0340ce965d47ae6ef1cefeee28c7c493a6346c4f15d667ab976d596c.svg)](https://classroom.github.com/a/JSWKa4hr)
# 软件工程实训任务二：小组协同开发

## 任务目的
* 巩固强化软件编程规范
* 提高面向对象软件建模与抽象能力
* 培养小组协同开发能力
* 掌握基于Maven的软件项目管理机制
* 掌握基于Github的小组协同开发工具和平台
* 了解DevOps软件开发流程

## 任务内容
* 创建软件开发小组
* 针对样例代码工程进行小组讨论，确定功能扩充需求点
* 基于Github中的issue管理功能明确工作任务并为组员分配工作任务
* 基于小组商定的分支模型进行软件功能开发，并按开发流程进行代码测试、提交、归并和同步
* 代码提交到远程仓库后，应进行自动化代码格式规范检查和测试以确保功能符合需求设计
* 完成前述各项任务后，可尝试进行代码自动化打包，自动生成可供执行的jar文件

## 任务要求
1. 创建软件开发小组
    * 每个开发小组人数3-5人，推选一人作为组长，负责组织、协调和领导团队开发；
    * 所有小组成员应按操作步骤在github开发平台上加入同一小组，共用同一代码仓库；
    
2. 开展小组讨论，确定功能扩充点
    * 样例工程“world-of-zuul”具备最基本的程序功能，该项目具有极大的扩展空间，开发小组内可进行沟通讨论，确定系统结构优化需求或功能扩充需求，结构优化或功能扩充项不能少于5项；

> 可供参考的结构优化或功能扩充项包括但不限于以下内容：
>
> 1. 扩展游戏，使得一个房间里可以存放任意数量的物件，每个物件可以有一个描述和一个重量值，玩家进入一个房间后，可以通过“look”命令查看当前房间的信息以及房间内的所有物品信息；
> 2. 在游戏中实现一个“back”命令，玩家输入该命令后会把玩家带回上一个房间；
> 3. 在游戏中实现一个更高级的“back”命令，重复使用它就可以逐层回退几个房间，直到把玩家带回到游戏的起点；
> 4. 在游戏中增加具有传输功能的房间，每当玩家进入这个房间，就会被随机地传输到另一个房间；
> 5. 在游戏中新建一个独立的Player类用来表示玩家，并实现下列功能需求：
>    * 一个玩家对象应该保存玩家的姓名等基本信息，也应该保存玩家当前所在的房间；
>    * 玩家可以随身携带任意数量的物件，但随身物品的总重量不能操过某个上限值；
>    * 在游戏中增加两个新的命令“take”和“drop”，使得玩家可以拾取房间内的指定物品或丢弃身上携带的某件或全部物品，当拾取新的物件时超过了玩家可携带的重量上限，系统应给出提示；
>    * 在游戏中增加一个新的命令“items”, 可以打印出当前房间内所有的物件及总重量，以及玩家随身携带的所有物件及总重量；
>    * 在某个或某些房间中随机增加一个magic cookie（魔法饼干）物件，并增加一个“eat cookie”命令，如果玩家找到并吃掉魔法饼干，就可以增长玩家的负重能力；
> 6. 扩充游戏基本架构，使其支持网络多人游戏模式，具备玩家登陆等功能；
> 7. 为单机或网络版游戏增加图形化用户界面，用过可以通过图形化界面执行游戏功能；
> 8. 可以为游戏增加数据库功能，用于保存游戏状态和用户设置；
> 9. ......

3. 基于Github中的issue管理功能明确工作任务并为组员分配工作任务
    * 将工作任务拆分细化后，明确版本开发计划和里程碑时间节点；
    
    * 在github平台创建任务issue并为所有组员分配任务；
    
    * 每位组员可以分别承担不同的开发任务，也可以按照小组角色分别承担开发、测试、集成等工作任务；
    
    * 工作任务的划分是最终衡量小组成员工作量的重要依据；
    
4. 基于小组商定的分支模型进行软件功能开发，并按开发流程进行代码测试、提交、归并和同步

    * 小组成员按照小组商定的分支模型在各自的工作分支进行进行开发任务；
    * 工作分支在合并前应同步到远程仓库供教师检查每人的开发工作完成情况；
    * 提交代码时应按照小组约定的规范格式填写代码提交说明，代码提交说明也将作为评分的重要依据；

5. 代码提交到远程仓库后，应进行自动化代码格式规范检查和测试以确保功能符合需求设计；

    * 可以利用github平台的actions功能在代码提交时自动触发代码格式检查，对于不符合规范的代码系统将给出提交失败提示；
    * 可以利用github平台的actions功能在代码提交时自动触发测试用例检查，对于不能通过测试检查的代码系统将给出提交失败提示；

6. 可尝试进行代码自动化打包，自动生成可供执行的jar文件

    * 结合github平台的actions功能和maven编译脚本，在代码通过规范性检查和测试用例后，进行自动化打包，生成可供直接执行的jar文件用于系统发布


## 任务输出
1. 以上所有工作基于github平台完成，以项目小组为单位在项目根目录下创建一个名称为REPORT.md的文件，对项目实训过程进行报告，报告内容应包含各工作内容的设计过程及完成情况的说明；
2. 所有源代码应包含规范化标注；
3. 每位同学以word文档格式提交个人实训报告；

