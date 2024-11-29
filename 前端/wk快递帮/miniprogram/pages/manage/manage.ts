// pages/booking/booking.ts
Page({

  /**
   * 页面的初始数据
   */
  data: {
    userType_index: 0, // 索引
    userType_list: [
      { name: "用户", value: 2 },
      { name: "配送员", value: 1 },
      { name: "管理员", value: 9 }
    ],
    target_openid: ''
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

  },

  /**
   * 改变职位框时
   */
  bindPickerChange(e){
    console.log('picker职位框发送选择改变，携带值为', e.detail.value)
    this.setData({'userType_index': e.detail.value})
  },

  /**
   * 用户输入openid时
   */
  bindKeyInput: function (e) {
    this.setData({
      target_openid: e.detail.value
    })
  },

  /**
   * 确定更改用户职位按钮
   */
  change_userType(){
    let that = this
    wx.request({
      method: 'POST',
      url: getApp().globalData.serverip+"/user/change_userType",
      header: {
        token: wx.getStorageSync('token')
      },
      data: {
        user_openid: that.data.target_openid,
        user_type: that.data.userType_list[that.data.userType_index].value
      },
      success(res){
        if(res.data.code == 200){
          wx.showToast({
            title: '更改成功',
            icon: 'success'
          })
        } else {
          console.log(res.data)
          wx.showToast({
            title: '更改失败:'+res.data.message,
            icon: 'none'
          })
        }
      },
      fail(){
        wx.showToast({
          title: '更改失败,请稍后重试',
          icon: 'none'
        })
      }
    })
  }
})