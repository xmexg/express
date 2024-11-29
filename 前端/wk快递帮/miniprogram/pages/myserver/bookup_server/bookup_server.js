// pages/bookup/bookup.ts
Page({

  /**
   * 页面的初始数据
   */
  data: {
    // havetoken: false,
    PickListIndex: 1,
    PickList: [],
    PriceNegChoose: [
      { name: '确认价格后取货', value: '0', checked: 'true' },
      { name: '直接取货', value: '1' }
    ],
    UnpaidOrder: []  // 未支付订单
  },

  /**
   * 取货点
   */
  Pick: function (e) {
    console.log('取货地点发送选择改变，携带值为', e.detail.value)
    this.setData({
      PickListIndex: e.detail.value
    })
  },

  /**
   * 价格协商单选
   */
  PriceNeg: function (e1) {
    console.log('价格协商携带value值为：', e1.detail.value)
  },

  /**
   * 获取快递点
   */
  get_point: function() {
    var that = this
    this.PickList=['a','b']
    wx.request({
      method: 'GET',
      url: getApp().globalData.serverip+'/express/point',
      success(res){
        that.setData({
          'PickList': res.data
        })
      }
    })
  },

  /**
   * 获取未付款的订单
   */
  getUnpaidOrder: function() {
    let that = this
    wx.request({
      url: getApp().globalData.serverip+'/order/getUnpaidOrder',
      method: 'POST',
      header: {
        token: wx.getStorageSync('token')
      },
      success(res){
        console.log("获取未支付订单",res.data)
        that.setData({'UnpaidOrder': res.data.data})
        return res.data
      }
    })
  },

  /**
   * 点击提交按钮
   */
  formSubmit: (e) => {
    wx.request({
      url: getApp().globalData.serverip+'/order/addOrder',
      method: "POST",
      header:{
        token: wx.getStorageSync('token')
      },
      data: e.detail.value,
      success(res){
        const resu = JSON.stringify(res.data)
        console.log(resu)
        if(res.data.code==200){
          wx.showToast({title:"下单成功",icon:'success'})
        } else {
          wx.showToast({title:"下单失败",icon:'error'})
        }
      },
      fail(){
        wx.showToast({title:"下单失败,请稍后重试",icon:"error"})
      },
      complete(){
      }
    })
  },
  formReset: function () {
    console.log('form发生了reset事件')
  },

  /**
   * 当点击未支付订单的支付按钮时
   */
  UnpaidOrder_pay: function() {
    console.log("付费未支付订单", this.data.UnpaidOrder[0])
    let that = this
    wx.request({
      url: getApp().globalData.serverip+'/order/payOrder_fake',
      method: 'POST',
      header: {
        token: wx.getStorageSync('token')
      },
      data: {
        orderid: that.data.UnpaidOrder[0].orderid
      },
      success(res){
        console.log(res.data)
        if(res.data.code == 200){
          wx.showToast({
            title: res.data.data,
            icon: 'success'
          })
          that.getUnpaidOrder()
        }
      },
      fail(){
        wx.showToast({
          title: '支付失败',
          icon: 'error'
        })
      }
    })
  },

  /**
   * 当点击未支付订单的支付按钮时
   * 跳转回主页
   */
  UnpaidOrder_cancel: function() {
    wx.switchTab({
      url: '/pages/index/index'
    })
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
    // 获取快递点
    if(!this.PickList){
      console.log("自动获取快递地址列表")
      this.get_point()
    }
    // 获取未支付信息
    this.getUnpaidOrder()
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