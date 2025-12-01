package cn.isqing.icloud.starter.admin.service.menu;

import cn.isqing.icloud.common.api.dto.Response;
import cn.isqing.icloud.starter.admin.api.dto.MenuDto;

import java.util.List;

public interface MenuService {
    Response<List<MenuDto>> getAllMenus();

    Response<List<MenuDto>> getMenusByRoleId(Long roleId);

    Response<Boolean> addMenu(MenuDto menuDto);

    Response<Boolean> updateMenu(MenuDto menuDto);

    Response<Boolean> deleteMenu(Long menuId);
}