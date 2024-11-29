// pages/booked/booked.ts
Page({

  /**
   * 页面的初始数据
   */
  data: {
    orders: []
  },

  /**
   * 订单状态
   * @param status 订单状态
   */
  getStatusText(status: any) {
    switch (status) {
      case 1:
        return "已下单";
      case 2:
        return "已揽件";
      case 11:
        return "已完成";
      case 12:
        return "已取消";
      default:
        return "未知状态";
    }
  },

  /**
   * 时间戳转时间
   * @param timestamp 时间
   */
  timestampToTime(timestamp: string | number | Date) {
    if(!timestamp) return ''
    var date = new Date(timestamp);
    var Y = date.getFullYear() + '-';
    var M = (date.getMonth()+1 < 10 ? '0'+(date.getMonth()+1) : date.getMonth()+1) + '-';
    var D = date.getDate() + ' ';
    var h = date.getHours() + ':';
    var m = date.getMinutes() + ':';
    var s = date.getSeconds();
    return Y+M+D+h+m+s;
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
    this.get_AllOrder()
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
    this.get_AllOrder()
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
   * 获取用户所有订单
   */
  get_AllOrder(){
    let that =this
    wx.request({
      url: getApp().globalData.serverip+'/order/myorder_all',
      header: {
        token: wx.getStorageSync('token')
      },
      success(res){
        console.log("获取用户全部订单", res)
        if(res.data.code == 200){
          let orders = res.data.data
          orders.forEach(element => {
            element.status_text = that.getStatusText(element.status)
            if(element.ordertime) element.ordertime_text = that.timestampToTime(element.ordertime)
            if(element.paytime) element.paytime_text = that.timestampToTime(element.paytime)
          });
          console.log(orders)
          that.setData({
            orders: orders
          })
        }
      }
    })
  },

  /**
   * 取消订单
   */
  order_cancel(e: { currentTarget: { dataset: { orderid: any; }; }; }){
    wx.request({
      method: 'POST',
      url: getApp().globalData.serverip+'/order/order_cancel',
      header: {
        token: wx.getStorageSync('token')
      },
      data: {
        orderid: e.currentTarget.dataset.orderid
      },
      success(res){
        console.log("取消订单", res.data)
        if(res.data.code==200){
          wx.showToast({title:JSON.stringify(res.data.data.reason),icon:"none"})
        } else{
          wx.showToast({title:JSON.stringify(res.data),icon:"none"})
        }
      },
      fail(){
        wx.showToast({title:"取消失败,请稍后重试",icon:"error"})
      }
    })
  }
})