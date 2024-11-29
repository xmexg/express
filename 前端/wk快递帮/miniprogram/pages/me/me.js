// pages/me/me.ts
Page({

  /**
   * 页面的初始数据
   */
  data: {
    user_id: wx.getStorageSync('user_id'),
    user_openid: wx.getStorageSync('user_openid'),
    user_type: wx.getStorageSync("user_type"), // 用户账号类型：1配送员,2用户(默认),9管理员
    // 普通用户显示以下信息
    mydata_user: [
      { name: "已下单", value: "0", id: "1"},
      { name: "已完成", value: "0", id: "2"}
    ],
    // 派送员和管理员额外显示以下内容
    mydata_woker: [
      { name: "派送中", value: "0", id: "3"},
      { name: "已派送", value: "0", id: "4"}
    ]
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad() {
  },

  /**
   * 生命周期函数--监听页面初次渲染完成
   */
  onReady() {

  },

  /**
   * 生命周期函数--监听页面显示
   */
  onShow() {
    this.get_barStatus()
  },

  /**
   * 生命周期函数--监听页面隐藏
   */
  onHide() {

  },

  /**
   * 生命周期函数--监听页面卸载
   */
  onUnload() {

  },

  /**
   * 页面相关事件处理函数--监听用户下拉动作
   */
  onPullDownRefresh() {
    // 重新获取用户信息
    this.get_userInfo()
    this.get_barStatus()
  },

  /**
   * 页面上拉触底事件的处理函数
   */
  onReachBottom() {

  },

  /**
   * 用户点击右上角分享
   */
  onShareAppMessage() {

  },

  /**
   * 点击了关于按钮
   */
  goto_about() {
    wx.navigateTo({
      url: '/pages/about_new/about_new',
    })
  },

  /**
   * 点击了“已下单或已完成”按钮
   */
  goto_orderBook(){
    console.log("点击了已下单或已完成按钮")
    wx.navigateTo({
      url: '/pages/booked/booked',
    })
  },
  /**
   * 点击了“派送中或已派送”按钮
   */
  goto_orderDeliver(){
    console.log("点击了派送中或已派送按钮")
    wx.navigateTo({
      url: '/pages/delivered/delivered',
    })
  },
  /**
   * 点击了“管理”按钮
   */
  goto_manage(){
    wx.navigateTo({
      url: '/pages/manage/manage',
    })
  },

  /**
   * 重新获取用户基本信息（user_id, user_type, user_openid, user_creattime）
   * 
   */
  get_userInfo(){
    let that = this
    wx.request({
      method: 'POST',
      url: getApp().globalData.serverip+'/user/get_userInfo',
      header: {
        token: wx.getStorageSync('token')
      },
      success(res){
        if(res.data.code==200){
          console.log("更新用户信息", res.data, res.data.data.user_type)
          wx.setStorageSync('user_id', res.data.data.user_id)
          wx.setStorageSync('user_creattime', res.data.data.user_creattime)
          wx.setStorageSync('user_openid', res.data.data.user_openid)
          wx.setStorageSync('user_session_key', res.data.data.user_session_key)
          wx.setStorageSync('user_type', res.data.data.user_type)
        } else {
          wx.showToast({
            title: res.data.data,
            icon: 'none'
          })
        }
      },
      fail(res){
        wx.showToast({
          title: '更新用户信息失败',
          icon: 'error'
        })
      }
    })
  },

  /**
   * 根据用户身份更新顶栏状态信息
   */
  get_barStatus(){
    this.getNum_order()
    if(this.data.user_type == 1 || this.data.user_type == 9){
      this.getNum_delivery()
    }
  },
   
  /**
   * 获取用户已下单和已完成订单数量
   */
  getNum_order(){
    let that = this
    wx.request({
      method: 'POST',
      url: getApp().globalData.serverip+'/order/getNum_order',
      header: {
        token: wx.getStorageSync('token')
      },
      success(res){
        if(res.data.code == 200){
          console.log("获取用户订单信息", res.data.data)
          let mydata_user = that.data.mydata_user
          mydata_user[0].value = res.data.data[0]
          mydata_user[1].value = res.data.data[1]
          that.setData({'mydata_user': mydata_user})
        }
      }
    })
  },

  /**
  * 获取快递员派送中和已派送订单数量 
  */
  getNum_delivery(){
    let that = this
    wx.request({
      method: 'POST',
      url: getApp().globalData.serverip+'/order/getNum_delivery',
      header: {
        token: wx.getStorageSync('token')
      },
      success(res){
        if(res.data.code == 200){
          console.log("获取派送员订单信息", res.data.data)
          let mydata_woker = that.data.mydata_woker
          mydata_woker[0].value = res.data.data[0]
          mydata_woker[1].value = res.data.data[1]
          that.setData({'mydata_woker': mydata_woker})
        }
      }
    })
  }
})