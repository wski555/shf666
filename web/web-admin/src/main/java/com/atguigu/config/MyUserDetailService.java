package com.atguigu.config;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.entity.Admin;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import service.AdminService;
import service.PermissionService;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class MyUserDetailService implements UserDetailsService {
    @Reference
    private AdminService adminService;
    @Reference
    private PermissionService permissionService;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Admin admin = adminService.getByUsername(username);
        if(admin == null){
            throw new UsernameNotFoundException("用户名不存在！");
        }
        List<String> stringList = permissionService.getCodeListByAdminId(admin.getId());
        Set<GrantedAuthority> authoritySet = new HashSet<>();
        for (String s : stringList) {
            authoritySet.add(new SimpleGrantedAuthority(s));
        }
        return new User(username,admin.getPassword(),authoritySet);
    }
}
