package com.ranranx.aolie.application.page.service;

import com.ranranx.aolie.application.page.dto.PageDetailDto;
import com.ranranx.aolie.application.page.dto.PageInfo;
import com.ranranx.aolie.application.page.dto.PageInfoDto;
import com.ranranx.aolie.core.common.CommonUtils;
import com.ranranx.aolie.core.common.Constants;
import com.ranranx.aolie.core.common.IdGenerator;
import com.ranranx.aolie.core.common.SessionUtils;
import com.ranranx.aolie.core.ds.dataoperator.DataOperatorFactory;
import com.ranranx.aolie.core.ds.definition.*;
import com.ranranx.aolie.core.exceptions.InvalidException;
import com.ranranx.aolie.core.handler.param.condition.Criteria;
import com.ranranx.aolie.core.tree.LevelProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * @Author xxl
 * @Description
 * @Date 2020/10/31 20:08
 * @Version V0.0.1
 **/
@Service
@Transactional(readOnly = true)
public class PageService {

    @Autowired
    private DataOperatorFactory factory;

    /**
     * 取得页面列表
     *
     * @param schemaId
     * @return
     */
    public List<PageInfoDto> findPageInfos(Long schemaId) {
        QueryParamDefinition queryParamDefinition = new QueryParamDefinition();
        queryParamDefinition.setTableDtos(PageInfoDto.class);
        queryParamDefinition.appendCriteria().andEqualTo("schema_id", schemaId)
                .andEqualTo("version_code", SessionUtils.getLoginVersion());
        queryParamDefinition.addOrder(new FieldOrder(PageInfoDto.class, "lvl_code", true, 1));
        return factory.getDefaultDataOperator().select(queryParamDefinition, PageInfoDto.class);

    }

    /**
     * 取得一页面的详细配置信息
     *
     * @param pageId
     * @return
     */
    public List<PageDetailDto> findPageDetail(Long pageId) {
        QueryParamDefinition queryParamDefinition = new QueryParamDefinition();
        queryParamDefinition.setTableDtos(PageDetailDto.class);
        queryParamDefinition.appendCriteria().andEqualTo("page_id", pageId)
                .andEqualTo("version_code", SessionUtils.getLoginVersion());
        return factory.getDefaultDataOperator().select(queryParamDefinition, PageDetailDto.class);
    }

    /**
     * 增加页面
     *
     * @param pageName
     * @param schemaId
     * @param parentId
     * @return
     */
    @Transactional(readOnly = false)
    public long addPage(String pageName, Long schemaId, Long parentId) {
        if (CommonUtils.isEmpty(pageName) || pageName.trim().equals("")) {
            throw new InvalidException("页面名称不可以为空!");
        }
        if (schemaId == null || schemaId < 1) {
            throw new InvalidException("方案没有指定");
        }

        PageInfoDto dto = new PageInfoDto();
        dto.setPageId(IdGenerator.getNextId(PageInfoDto.class.getName()));
        dto.setPageName(pageName);
        dto.setSchemaId(schemaId);
        dto.setLayoutType(Constants.LayoutType.BORDER_LAYOUT);
        dto.setLvlCode(getNextCode(parentId, schemaId));
        dto.setCanDrag((short) 1);
        dto.setVersionCode(SessionUtils.getLoginVersion());
        InsertParamDefinition insertParamDefinition = new InsertParamDefinition();
        insertParamDefinition.setNeedConvertToUnderLine(true);
        insertParamDefinition.setObject(dto);
        factory.getDefaultDataOperator().insert(insertParamDefinition);
        return dto.getPageId();
    }

    private String getNextCode(Long parentId, Long schemaId) {
        String lvlParent = "";
        if (parentId != null && parentId > 0) {
            PageInfoDto pageInfo = findPageInfo(parentId, schemaId);
            if (pageInfo != null) {
                lvlParent = pageInfo.getLvlCode();
            }
        }
        LevelProvider provider = new LevelProvider(lvlParent);

        QueryParamDefinition queryParamDefinition = new QueryParamDefinition();
        queryParamDefinition.setTableDtos(PageInfoDto.class);
        Field field = new Field();
        field.setFieldName("lvl_code");
        field.setGroupType(Constants.GroupType.MAX);
        queryParamDefinition.addField(field);
        Criteria criteria = queryParamDefinition.appendCriteria().andEqualTo("schema_id", schemaId)
                .andCondition("length(lvl_code)=", provider.getFirstSubCode().length())
                .andEqualTo("version_code", SessionUtils.getLoginVersion());
        List<Map<String, Object>> lstData = factory.getDefaultDataOperator().select(queryParamDefinition);
        if (lstData == null || lstData.isEmpty()) {
            return lvlParent + "001";
        }
        String lvlCode = CommonUtils.getStringField(lstData.get(0), "lvl_code");
        if (CommonUtils.isEmpty(lvlCode)) {
            return lvlParent + "001";
        }
        provider.setSCurrentCode(lvlCode);
        return provider.getNextCode();
    }


    private PageInfoDto findPageInfo(Long pageId, Long schemaId) {
        QueryParamDefinition queryParamDefinition = new QueryParamDefinition();
        queryParamDefinition.setTableDtos(PageInfoDto.class);
        queryParamDefinition.appendCriteria().andEqualTo("schema_id", schemaId)
                .andEqualTo("version_code", SessionUtils.getLoginVersion())
                .andEqualTo("page_id", pageId);
        return factory.getDefaultDataOperator().selectOne(queryParamDefinition, PageInfoDto.class);
    }

    /**
     * 删除指定 页面信息
     *
     * @param pageId
     * @return
     */
    @Transactional(readOnly = false)
    public String deletePage(long pageId) {
        if (pageId < 1) {
            return "指定的数据不存在";
        }
        DeleteParamDefinition deleteParamDefinition = new DeleteParamDefinition();
        deleteParamDefinition.setTableDto(PageDetailDto.class);
        deleteParamDefinition.getCriteria().andEqualTo("page_id", pageId)
                .andEqualTo("version_code", SessionUtils.getLoginVersion());

        factory.getDefaultDataOperator().delete(deleteParamDefinition);
        deleteParamDefinition.setTableDto(PageInfoDto.class);
        factory.getDefaultDataOperator().delete(deleteParamDefinition);
        return null;
    }

    /**
     * 取得完整的页面信息
     *
     * @param pageId
     * @param schemaId
     * @return
     */
    public PageInfo findPageFullInfo(long pageId, long schemaId) {
        PageInfoDto pageInfo = this.findPageInfo(pageId, schemaId);
        if (pageInfo == null) {
            return null;
        }
        List<PageDetailDto> pageDetail = findPageDetail(pageId);
        PageInfo pageFullInfo = new PageInfo();
        pageFullInfo.setLstPageDetail(pageDetail);
        return pageFullInfo;
    }

    /**
     * 保存页面信息
     *
     * @param pageInfo
     */
    @Transactional(readOnly = false)
    public void savePageFullInfo(PageInfo pageInfo) {
        deletePage(pageInfo.getPageInfoDto().getPageId());
        InsertParamDefinition insertParamDefinition = new InsertParamDefinition();
        insertParamDefinition.setNeedConvertToUnderLine(true);
        insertParamDefinition.setObject(pageInfo.getPageInfoDto());
        factory.getDefaultDataOperator().insert(insertParamDefinition);
        List<PageDetailDto> lstPageDetail = pageInfo.getLstPageDetail();

        if (lstPageDetail != null && lstPageDetail.size() > 0) {
            long pageId = pageInfo.getPageInfoDto().getPageId();
            long schemaId = pageInfo.getPageInfoDto().getSchemaId();
            String versionCode = SessionUtils.getLoginVersion();
            for (PageDetailDto dto : lstPageDetail) {
                dto.setPageId(pageId);
                dto.setSchemaId(schemaId);
                dto.setVersionCode(versionCode);
                if (dto.getPageDetailId() == null || dto.getPageDetailId() < 1) {
                    dto.setPageDetailId(IdGenerator.getNextId(PageDetailDto.class.getName()));
                }
            }
        }
        insertParamDefinition = new InsertParamDefinition();
        insertParamDefinition.setNeedConvertToUnderLine(true);
        insertParamDefinition.setObjects(pageInfo.getLstPageDetail());
        factory.getDefaultDataOperator().insert(insertParamDefinition);
    }

    /**
     * 更新页面的层次设置
     *
     * @param mapIdToCode
     */
    @Transactional(readOnly = false)
    public void updatePageLevel(Map<Long, String> mapIdToCode, long schemaId) {
        if (mapIdToCode == null || mapIdToCode.isEmpty()) {
            return;
        }
        UpdateParamDefinition updateParamDefinition;
        updateParamDefinition = new UpdateParamDefinition();
        updateParamDefinition.setSelective(true);
        updateParamDefinition.setTableNameByDto(PageInfoDto.class);
        updateParamDefinition.setIdField("page_id,schema_id,version_code");

        updateParamDefinition.setLstRows(toMap(mapIdToCode, schemaId));
        factory.getDefaultDataOperator().update(updateParamDefinition);
    }

    private List<Map<String, Object>> toMap(Map<Long, String> mapIdToCode, long schemaId) {
        Iterator<Map.Entry<Long, String>> iterator = mapIdToCode.entrySet().iterator();

        List<Map<String, Object>> lstResult = new ArrayList<>();
        while (iterator.hasNext()) {
            Map.Entry<Long, String> next = iterator.next();
            Map<String, Object> map = new HashMap<>();
            map.put("version_code", SessionUtils.getLoginVersion());
            map.put("page_id", next.getKey());
            map.put("schema_id", schemaId);
            map.put("lvl_code", next.getValue());
            lstResult.add(map);
        }
        return lstResult;
    }
}
