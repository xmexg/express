// pages/index/index.ts
// const wxLogin = require('./../../utils/Login')//存在即合理
Page({

  /**
   * 页面的初始数据
   */
  data: {
    token: wx.getStorageSync("token"),
    user_type: wx.getStorageSync("user_type")
  },

  btn_express(){
    wx.navigateTo({
      url: "/pages/myserver/bookup_server/bookup_server"
    })
  },

  btn_network(){
    wx.navigateTo({
      url: '/pages/myserver/network_server/network_server',
    })
  },

  /**
   * 点击登录按钮时
   */
  loginBottonClick(page){
    wx.login({
      success: res => {
        console.log('获取到code:'+res.code)
        // 发送 res.code 到后台换取 openId, sessionKey, unionId
        if (res.code) {
          //发起网络请求
          wx.request({
            url: getApp().globalData.serverip+'/user/login',
            data: {
              code: res.code
            },
            success(res){
              const strres = JSON.stringify(res.data)
              const arr = JSON.parse(strres)
              wx.setStorageSync("token", arr.user_token);
              wx.setStorageSync("user_id", arr.user_id);
              wx.setStorageSync("user_type", arr.user_type)
              wx.showToast({title: '登录成功',icon: 'none'})
            },
            fail(){
              wx.showToast({title:"登录失败",icon:"error"});
            },
            complete(){  
              console.log('登录后获取到的token：'+wx.getStorageSync('token'))
            }
          })
        } else {
          wx.showToast({title:"登录失败",icon:"error"});
        }
      },
    })
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad(page) {
  },

  /**
   * 生命周期函数--监听页面初次渲染完成
   */
  onReady(page) {
  },

  /**
   * 生命周期函数--监听页面显示
   */
  onShow() {

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

  }
})