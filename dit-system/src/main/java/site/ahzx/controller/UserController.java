package site.ahzx.controller;

import cn.hutool.core.lang.tree.Tree;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import site.ahzx.domain.bo.PageBO;
import site.ahzx.domain.entity.SysDepts;
import site.ahzx.domain.entity.SysUsers;
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





}
