package cn.isqing.icloud.starter.admin.service.menu.impl;

import cn.isqing.icloud.common.api.dto.Response;
import cn.isqing.icloud.starter.admin.api.dto.MenuDto;
import cn.isqing.icloud.starter.admin.dao.entity.Menu;
import cn.isqing.icloud.starter.admin.dao.mapper.MenuMapper;
import cn.isqing.icloud.starter.admin.service.menu.MenuService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MenuServiceImpl implements MenuService {

    @Autowired
    private MenuMapper menuMapper;

    @Override
    public Response<List<MenuDto>> getAllMenus() {
        List<Menu> menus = menuMapper.selectAll();
        List<MenuDto> menuDtos = menus.stream().map(menu -> {
            MenuDto menuDto = new MenuDto();
            BeanUtils.copyProperties(menu, menuDto);
            return menuDto;
        }).collect(Collectors.toList());
        return Response.success(menuDtos);
    }

    @Override
    public Response<List<MenuDto>> getMenusByRoleId(Long roleId) {
        List<Menu> menus = menuMapper.selectByRoleId(roleId);
        List<MenuDto> menuDtos = menus.stream().map(menu -> {
            MenuDto menuDto = new MenuDto();
            BeanUtils.copyProperties(menu, menuDto);
            return menuDto;
        }).collect(Collectors.toList());
        return Response.success(menuDtos);
    }

    @Override
    public Response<Boolean> addMenu(MenuDto menuDto) {
        Menu menu = new Menu();
        BeanUtils.copyProperties(menuDto, menu);
        menuMapper.insert(menu);
        return Response.success(true);
    }

    @Override
    public Response<Boolean> updateMenu(MenuDto menuDto) {
        Menu menu = new Menu();
        BeanUtils.copyProperties(menuDto, menu);
        menuMapper.update(menu);
        return Response.success(true);
    }

    @Override
    public Response<Boolean> deleteMenu(Long menuId) {
        menuMapper.deleteById(menuId);
        return Response.success(true);
    }
}