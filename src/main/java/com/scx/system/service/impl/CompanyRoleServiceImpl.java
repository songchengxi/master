package com.scx.system.service.impl;

import com.scx.system.entity.CompanyRole;
import com.scx.system.entity.CompanyRoleFunction;
import com.scx.system.entity.CompanyRoleUser;
import com.scx.system.service.CompanyRoleService;
import org.hibernate.Criteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.jeecgframework.core.common.hibernate.qbc.CriteriaQuery;
import org.jeecgframework.core.common.service.impl.CommonServiceImpl;
import org.jeecgframework.web.system.pojo.base.TSRoleFunction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service("companyRoleService")
@Transactional
public class CompanyRoleServiceImpl extends CommonServiceImpl implements CompanyRoleService {

    @Override
    public int getChildRole(String roleId) {
        Criteria criteria = getSession().createCriteria(CompanyRole.class);
        criteria.add(Restrictions.eq("parentRole.id", roleId));
        int allCounts = ((Long) criteria.setProjection(
                Projections.rowCount()).uniqueResult()).intValue();
        return allCounts;
    }

    @Override
    public int getUsersOfThisRole(String roleId) {
        Criteria criteria = getSession().createCriteria(CompanyRoleUser.class);
        criteria.add(Restrictions.eq("TSRole.id", roleId));
        int allCounts = ((Long) criteria.setProjection(
                Projections.rowCount()).uniqueResult()).intValue();
        return allCounts;
    }

    @Override
    public Set<String> getOperationCodesByRoleIdAndruleDataId(String roleId, String functionId) {
        Set<String> operationCodes = new HashSet<String>();
        CompanyRole role = commonDao.get(CompanyRole.class, roleId);
        CriteriaQuery cq1 = new CriteriaQuery(CompanyRoleFunction.class);
        cq1.eq("TSRole.id", role.getId());
        cq1.eq("TSFunction.id", functionId);
        cq1.add();
        List<TSRoleFunction> rFunctions = getListByCriteriaQuery(cq1, false);
        if (null != rFunctions && rFunctions.size() > 0) {
            TSRoleFunction tsRoleFunction = rFunctions.get(0);
            if (null != tsRoleFunction.getDataRule()) {
                String[] operationArry = tsRoleFunction.getDataRule().split(",");
                for (int i = 0; i < operationArry.length; i++) {
                    operationCodes.add(operationArry[i]);
                }
            }
        }
        return operationCodes;
    }

    @Override
    public Set<String> getOperationCodesByRoleIdAndFunctionId(String roleId, String functionId) {
        Set<String> operationCodes = new HashSet<String>();
        CompanyRole role = commonDao.get(CompanyRole.class, roleId);
        CriteriaQuery cq1 = new CriteriaQuery(CompanyRoleFunction.class);
        cq1.eq("TSRole.id", role.getId());
        cq1.eq("TSFunction.id", functionId);
        cq1.add();
        List<TSRoleFunction> rFunctions = getListByCriteriaQuery(cq1, false);
        if (null != rFunctions && rFunctions.size() > 0) {
            TSRoleFunction tsRoleFunction = rFunctions.get(0);
            if (null != tsRoleFunction.getOperation()) {
                String[] operationArry = tsRoleFunction.getOperation().split(",");
                for (int i = 0; i < operationArry.length; i++) {
                    operationCodes.add(operationArry[i]);
                }
            }
        }
        return operationCodes;
    }
}
