package org.jeecgframework.web.system.service;

import org.jeecgframework.core.common.service.CommonService;
import org.jeecgframework.web.system.pojo.base.MutiLangEntity;

public interface MutiLangServiceI extends CommonService {

    void initAllMutiLang();

    String getLang(String lang_key);

    String getLang(String lang_key, String args);

    void refleshMutiLangCach();

    /**
     * 更新缓存，插入缓存
     */
    void putMutiLang(MutiLangEntity mutiLangEntity);

    /**
     * 更新缓存，插入缓存
     */
    void putMutiLang(String langKey, String langCode, String langContext);
}
