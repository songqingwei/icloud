//package cn.isqing.icloud.common.utils.controller;
//
//import cn.isqing.icloud.common.api.dto.PageReqDto;
//import cn.isqing.icloud.common.api.dto.Response;
//import cn.isqing.icloud.common.utils.annotation.TableAction;
//import cn.isqing.icloud.common.utils.enums.ActionType;
//import io.swagger.v3.oas.annotations.tags.Tag;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
///**
// * 表操作注解使用示例Controller
// * 演示如何使用@TableAction注解实现快速增删改查
// *
// * @author songqingwei
// * @version 1.0
// */
//@Slf4j
//@RestController
//@RequestMapping("/tableOperationDemo")
//@Tag(name = "表操作注解使用示例", description = "演示如何使用@TableAction注解")
//public class TableOperationDemoController {
//
//    /**
//     * 分页查询示例
//     *
//     * @param request 分页请求参数
//     * @return 查询结果
//     */
//    @TableAction(
//        tableName = "sys_user",
//        action = ActionType.PAGE_QUERY,
//        voClass = Object.class
//    )
//    @PostMapping("/pageQuery")
//    public Response<Object> pageQuery(@RequestBody PageReqDto<Object> request) {
//        // 实际逻辑由@TableAction注解的切面处理
//        return null;
//    }
//
//    /**
//     * 插入数据示例
//     *
//     * @param data 数据对象
//     * @return 插入结果
//     */
//    @TableAction(
//        tableName = "sys_user",
//        action = ActionType.INSERT,
//        voClass = Object.class
//    )
//    @PostMapping("/insert")
//    public Response<Object> insert(@RequestBody Object data) {
//        // 实际逻辑由@TableAction注解的切面处理
//        // 插入成功后会自动发布TableChangeEvent事件
//        return null;
//    }
//
//    /**
//     * 更新数据示例
//     *
//     * @param data 数据对象
//     * @return 更新结果
//     */
//    @TableAction(
//        tableName = "sys_user",
//        action = ActionType.UPDATE,
//        voClass = Object.class
//    )
//    @PostMapping("/update")
//    public Response<Object> update(@RequestBody Object data) {
//        // 实际逻辑由@TableAction注解的切面处理
//        // 更新成功后会自动发布TableChangeEvent事件
//        return null;
//    }
//
//    /**
//     * 删除数据示例
//     *
//     * @param data 数据对象
//     * @return 删除结果
//     */
//    @TableAction(
//        tableName = "sys_user",
//        action = ActionType.DELETE,
//        voClass = Object.class
//    )
//    @PostMapping("/delete")
//    public Response<Object> delete(@RequestBody Object data) {
//        // 实际逻辑由@TableAction注解的切面处理
//        // 删除成功后会自动发布TableChangeEvent事件
//        return null;
//    }
//}
