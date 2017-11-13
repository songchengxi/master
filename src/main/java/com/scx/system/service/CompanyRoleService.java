package com.scx.system.service;

import org.jeecgframework.core.common.service.CommonService;

import java.util.Set;

public interface CompanyRoleService extends CommonService {

    /**
     * 子角色数量
     */
    int getChildRole(String roleId);

    /**
     * 判断这个角色是不是还有用户使用
     */
    int getUsersOfThisRole(String roleId);

    /**
     * @Description:根据角色id 和 菜单Id 获取 具有操作权限的数据规则
     * @param @param roleId
     * @param @param functionId
     * @param @return    设定文件
     * @return Set<String>    返回类型
     * @throws
     */
    Set<String> getOperationCodesByRoleIdAndruleDataId(String roleId, String functionId);

    /**
     * 根据角色ID 和 菜单Id 获取 具有操作权限的按钮Codes
     */
    Set<String> getOperationCodesByRoleIdAndFunctionId(String roleId,String functionId);

}