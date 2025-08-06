package site.ahzx.controller;

import cn.hutool.core.lang.tree.Tree;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import site.ahzx.domain.bo.PageBO;
import site.ahzx.domain.bo.SysUserBO;
import site.ahzx.domain.vo.LoginGetUserInfoVO;
import site.ahzx.domain.vo.SysUserNoPassVO;
import site.ahzx.service.DeptService;
import site.ahzx.service.UserService;
import site.ahzx.util.R;
import site.ahzx.util.TableDataInfo;

import java.util.List;

@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {
    private final UserService userService;
    private final DeptService deptService;
    public UserController(UserService userService, DeptService deptService){
        this.userService = userService;
        this.deptService = deptService;
    }

    @GetMapping("/test")
    public R<?> test() {
        return R.ok("Hello, world");
    }

    @GetMapping("/getInfo")
    public R<?> getInfo(HttpServletRequest request) {
        String username = request.getHeader("x-user-name");
        log.debug("in getInfo username: {}", username);
        LoginGetUserInfoVO loginUserInfo = userService.getLoginUserInfo(username);
        return R.ok("获取成功", loginUserInfo);
    }

    @GetMapping("/deptTree")
    public  R<List<Tree<Long>>> getDeptTree() {
        List<Tree<Long>> deptTree = deptService.getDeptTree();
        return R.ok("获取成功", deptTree);
    }

    @GetMapping("/list")
    public R<?> list(PageBO pageBO){

        TableDataInfo<SysUserNoPassVO> userList = userService.getUserList(pageBO);
        return R.ok(userList);

    }

    @PostMapping
    public R<?> addUser(@RequestBody SysUserBO userBO){
        //TODO：0.检查是否有部门数据权限
        //1.检查用户是否存在，手机号、邮箱，用户名
        if(userService.checkUserExist(userBO.getUserName())){
            return R.fail("用户名已存在");
        } else if (userService.checkPhoneExist(userBO.getPhonenumber() )){
            return R.fail("手机号已存在");
        } else if (userService.checkEmailExist(userBO.getEmail() )){
            return R.fail("邮箱已存在");
        }

        //TODO: 2.判断租户是否有多余的额度

        //3.添加用户
        //TODO: 密码加密
        Integer user = userService.addUser(userBO);
        return user > 0 ? R.ok("添加成功") : R.fail("添加失败");
    }





}
