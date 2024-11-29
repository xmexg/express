// pages/books/books.ts
Page({

  /**
   * 页面的初始数据
   */
  data: {
    usrty: wx.getStorageSync('user_type'),
    PAGE: 1, //当前页面,最小值为1,触顶减一,触底加一 // 不用 
    // orders: [{"pickup":"正在获取订单..."}] //当前页面显示的订单信息
    orders: []
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad() {
    var haveorder = this.GetOrders(this.data.PAGE)
    if(haveorder == "true"){
      console.log("收到订单")
    }
  },

  /**
   * 显示订单
   */
  GetOrders(page){
    var thatdata = this
    var resdata
    wx.request({
      url: getApp().globalData.serverip+'/order/showorder',
      method: 'POST',
      data: {
        token: wx.getStorageSync('token'),
        page: thatdata.data.PAGE,
      },
      header: { 
        "Content-Type": "application/x-www-form-urlencoded"//被编码过,在此添加标注
      },
      success(res){
        if(res.data){//收到了订单
          resdata = res.data
          console.log("收到订单:", resdata)
          if(resdata.length >= 10){//判断是不是{开头会报错,只能判断长度
            wx.showToast({
              title: resdata,
              icon: 'none'
            })
            return
          }
          for(var i in resdata){//转换时间
            var simpletime = new Date(resdata[i].ordertime)
            resdata[i].ordertime = simpletime.toLocaleString()
          }
          thatdata.setData({
            orders: resdata
          })
        } else {//没有订单
        }
      },
      fail(){
        wx.showToast({
          title: '获取订单列表失败',
          icon: 'none'
        })
      }
    })
  },

  GetOrders_old(page){
    var thatdata = this
    var resdata
    wx.request({
      url: getApp().globalData.serverip+'/order/showorder?page='+page,
      method: "GET",
      success:function(res){
        // console.log("请求中:"+JSON.stringify(res.data))
        if(res.data){//收到了订单
          resdata = res.data
          for(var i in resdata){//转换时间
            var simpletime = new Date(resdata[i].ordertime)
            resdata[i].ordertime = simpletime.toLocaleString()
          }
          thatdata.setData({
            orders: resdata
          })
          return true
        } else {//没有订单
          
        }
      },
      fail(){
        return false
      }
    })
  },

  /**
   * 点击 接取订单 按钮时执行
   */
  DeliveryOrder(e){
    var uty = wx.getStorageSync('user_type')
    var token = wx.getStorageSync('token')
    var orderid = e.currentTarget.dataset.deliverorderid
    var price = e.currentTarget.dataset.deliverorderprice
    if(price == null || price < 0){
      wx.showToast({
        title: '请检查代取价格',
        icon: 'none',
      })
      return
    }
    if( uty != 1 && uty != 9){//1:配送员，9:管理员
      wx.showToast({
        title: '普通用户不能接单',
        icon: 'none'
      })
    } else {
      wx.request({
        url: getApp().globalData.serverip+'/order/deliveryorder',
        method: 'POST',
        data: {
          token: token,
          orderid: orderid,
          orderamount: price,
        },
        header: { 
          "Content-Type": "application/x-www-form-urlencoded"//被编码过,在此添加标注
        },
        success(res){
          if(res.data.code==200){
            wx.showToast({
              title: "接单成功",
              icon: 'success'
            })
          } else {
            wx.showToast({
              title: res.data.message,
              icon: 'none'
            })
          }
        },
        fail(){
          wx.showToast({
            title: "接单失败,请稍后重试",
            icon: 'none'
          })
        }
      })
    }
  },

  /**
   * 设置配送价格
   */
  setDeliveryOrderPrice(e){
    console.log("金额变化", e)
    var oldorder = this.data.orders
    oldorder[e.currentTarget.dataset.chooseorderloc].orderamount = e.detail.value
    this.setData({
      orders: oldorder
    })
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
    console.log("下拉")
    var haveorder = this.GetOrders(this.data.PAGE-1)
    if(haveorder == true){
      if(this.data.PAGE > 1){
        this.data.PAGE--
      } else {
        this.data.PAGE = 1
      }
      console.log("收到订单")
    }
  },

  /**
   * 页面上拉触底事件的处理函数
   */
  onReachBottom() {
    console.log("上拉")
    var haveorder = this.GetOrders(this.data.PAGE+1)
    if(haveorder == true){
      if(this.data.PAGE > 1){
        this.data.PAGE++
      }
      console.log("收到订单")
    }
  },

  /**
   * 用户点击右上角分享
   */
  onShareAppMessage() {

  }
})