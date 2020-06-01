---
一个具备远程访问功能的音频分享软件，软件应具备在线听歌， 在线听故事，在线分享等功能的音频分享app

---

# DailyListen

### 项目简介

- 整个项目分为7个模块：用户模块、订阅模块，推荐模块，历史模块，搜索模块，播放模块，详情模块。

  1. 用户模块：用户登录和用户注册
  2. 推荐模块：显示推荐内容和上拉加载
  3. 详情模块：试听、订阅、显示专辑下的音频列表
  4. 订阅模块：保存用户订阅的专辑、删除订阅
  5. 历史模块：保存用户播放记录、删除历史
  6. 搜索模块：热词显示、自动生成联想词、按关键字搜索
  7. 播放模块：切换播放模式、显示音频列表、快进、上一首等

- 后端数据使用喜马拉雅开源SDK

- 用户数据保存在bmob后端云中

  ##### UI显示：

  登陆界面UI：		

<img src="https://i.loli.net/2020/06/01/UcgzYH59pX16IO3.png" alt="登录模块UI" style="zoom:67%;" />

​			推荐模块UI：		

![推荐模块UI](https://i.loli.net/2020/06/01/2zyhNnuYqfGOW9Q.png)

​	详情模块UI：

![image-20200601145609721](https://i.loli.net/2020/06/01/JuwqYtShUKTfngI.png)

播放模块UI：

![image-20200601145647458](https://i.loli.net/2020/06/01/RYj6ZnF5wbPykuX.png)

订阅模块UI：

![image-20200601145721390](https://i.loli.net/2020/06/01/WT1zqikNgar9Khe.png )

历史模块UI：

![image-20200601150249434](https://i.loli.net/2020/06/01/SC2IYRcvHnuhoJ9.png)

搜索模块UI：

![image-20200601150328604](https://i.loli.net/2020/06/01/dyNUFHveI15zMgS.png)

UILoader在所有加载数据的页面中都使用到了，主要是根据数据加载的不同结果来显示不同的布局，这里是一个历史模块的例子：

![image-20200601150505469](https://i.loli.net/2020/06/01/9NLayDhBuz56G1i.png)










  

  

  


