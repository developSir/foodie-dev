package com.imooc.controller.center;


import com.imooc.controller.BasicController;
import com.imooc.utils.IMOOCJSONResult;
import com.imooc.utils.PagedGridResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@Api(value = "用户中心我的订单",tags = {"用户中心我的订单相关的api"})
@RestController
@RequestMapping("/myorders")
public class MyOrdersController extends BasicController {


    @ApiOperation(value = "获取用户信息",notes = "获取用户信息",httpMethod = "POST")
    @PostMapping("/query")
    public IMOOCJSONResult query(
            @ApiParam(name = "userId",value = "用户id",required = true)
            @RequestParam  String userId,
            @ApiParam(name = "orderStatus",value = "订单状态",required = true)
            @RequestParam Integer orderStatus,
            @ApiParam(name = "page",value = "查询下一页的第几页",required = false)
            @RequestParam Integer page,
            @ApiParam(name = "pageSize",value = "每页显示的条数",required = false)
            @RequestParam Integer pageSize) {


        if (StringUtils.isBlank(userId)){
            return IMOOCJSONResult.errorMsg(null);
        }
        if (page == null) {
            page = 1;
        }
        if (pageSize == null) {
            pageSize = COMMON_PAGE_SIZE;
        }

        PagedGridResult pagedGridResult1 = myOrdersService.queryByOrders(userId, orderStatus, page, pageSize);

        return IMOOCJSONResult.ok(pagedGridResult1);
    }


    // 商家发货没有后端，所以这个接口仅仅只是用于模拟
    @ApiOperation(value="商家发货", notes="商家发货", httpMethod = "GET")
    @GetMapping("/deliver")
    public IMOOCJSONResult deliver(
            @ApiParam(name = "orderId", value = "订单id", required = true)
            @RequestParam String orderId) throws Exception {

        if (StringUtils.isBlank(orderId)) {
            return IMOOCJSONResult.errorMsg("订单ID不能为空");
        }
        myOrdersService.updateDeliverOrderStatus(orderId);
        return IMOOCJSONResult.ok();
    }


    // 商家发货没有后端，所以这个接口仅仅只是用于模拟
    @ApiOperation(value="用户确认收货", notes="用户确认收货", httpMethod = "POST")
    @PostMapping("/confirmReceive")
    public IMOOCJSONResult confirmReceive(
            @ApiParam(name = "orderId", value = "订单id", required = true)
            @RequestParam String orderId,
            @ApiParam(name = "userId", value = "用户id", required = true)
            @RequestParam String userId) throws Exception {

        IMOOCJSONResult imoocjsonResult = checkUserOrder(userId, orderId);
        if (!imoocjsonResult.getStatus().equals(HttpStatus.OK.value())){
            return imoocjsonResult;
        }

        boolean b = myOrdersService.updateReceiveOrderStatus(orderId);
        if (!b) {
            IMOOCJSONResult.errorMsg("订单确认收货失败");
        }
        return IMOOCJSONResult.ok();
    }


    // 商家发货没有后端，所以这个接口仅仅只是用于模拟
    @ApiOperation(value="用户删除订单", notes="用户删除订单", httpMethod = "POST")
    @PostMapping("/delete")
    public IMOOCJSONResult delete(
            @ApiParam(name = "orderId", value = "订单id", required = true)
            @RequestParam String orderId,
            @ApiParam(name = "userId", value = "用户id", required = true)
            @RequestParam String userId) throws Exception {

        IMOOCJSONResult imoocjsonResult = checkUserOrder(userId, orderId);
        if (!imoocjsonResult.getStatus().equals(HttpStatus.OK.value())){
            return imoocjsonResult;
        }

        boolean b = myOrdersService.deleteOrder(userId, orderId);
        if (!b) {
            return IMOOCJSONResult.errorMsg("删除订单失败");
        }
        return IMOOCJSONResult.ok();
    }




}
