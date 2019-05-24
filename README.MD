# 项目名称

搜索工具Everything
 
# 项目意义
 
+ 解决Windows命令行下文件搜索问题（Windows下的文件搜索有盘符限制）
+ 可以跨平台使用，如Windows、Linux系统


# 项目使用技术

+ JavaSE（Stream流、文件相关操作、多线程）
+ 数据库连接池Database（嵌入式数据库H2）
+ JDBC编程（用于数据库与Java语言的交流）
+ 文件系统监听（Apache Commons IO）
+ Lombok库（IDEA安装Lombok插件）

# 项目功能

+ 用户可配置 用户可以自己配置搜索目录、输出顺序
+ 文件名模糊查询 项目中使用的是前后模糊，即你输入"简历"输出包含简历字样的所有文件地址
+ 指定文件类型查询 支持文档类DOC 图片类IMG等，当搜索时若输入文件类型，只输出相应类型的文件
+ 文件监听 当本地文件新增或者删除，数据库会随着一起更改

## 项目详情
https://blog.csdn.net/zhao_miao/article/details/90516748#2_3
