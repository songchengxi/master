package org.jeecgframework.web.system.pojo.base;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.jeecgframework.core.common.entity.IdEntity;
import org.jeecgframework.poi.excel.annotation.Excel;

import java.io.Serializable;

/**
 * 公司--角色表
 */
@Entity
@Table(name = "t_s_role")
public class TSRole extends IdEntity implements Serializable {

	@Excel(name = "角色名称",width = 20)
	private String roleName;//角色名称

	@Excel(name = "角色编码",width = 20)
	private String roleCode;//角色编码

	@Column(name = "rolename", nullable = false, length = 100)
	public String getRoleName() {
		return this.roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	@Column(name = "rolecode", length = 10)
	public String getRoleCode() {
		return this.roleCode;
	}

	public void setRoleCode(String roleCode) {
		this.roleCode = roleCode;
	}

    @Override
    public String toString() {
        return "TSRole{" +
				"id='" + super.getId() + '\'' +
				", roleName='" + roleName + '\'' +
				", roleCode='" + roleCode + '\'' +
                '}';
    }
}