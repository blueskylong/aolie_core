package com.ranranx.aolie.application.sysconfig.service;

import com.ranranx.aolie.application.right.RightNode;
import com.ranranx.aolie.application.sysconfig.dto.SysConfigDto;
import com.ranranx.aolie.application.sysconfig.model.CycleDetector;
import com.ranranx.aolie.application.sysconfig.model.DirectedGraph;
import com.ranranx.aolie.application.user.dto.RightRelationDto;
import com.ranranx.aolie.application.user.dto.RightResourceDto;
import com.ranranx.aolie.application.user.service.UserService;
import com.ranranx.aolie.core.common.CommonUtils;
import com.ranranx.aolie.core.common.Constants;
import com.ranranx.aolie.core.common.SessionUtils;
import com.ranranx.aolie.core.common.SystemParam;
import com.ranranx.aolie.core.datameta.dto.VersionDto;
import com.ranranx.aolie.core.ds.dataoperator.DataOperatorFactory;
import com.ranranx.aolie.core.ds.definition.QueryParamDefinition;
import com.ranranx.aolie.core.exceptions.InvalidConfigException;
import com.ranranx.aolie.core.handler.HandleResult;
import com.ranranx.aolie.core.handler.HandlerFactory;
import com.ranranx.aolie.core.handler.param.QueryParam;
import com.ranranx.aolie.core.interfaces.ISystemIniter;
import com.ranranx.aolie.core.interfaces.ISystemParamGenerator;
import com.ranranx.aolie.core.runtime.LoginUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * @author xxl
 * @version V0.0.1
 * @date 2021/2/17 0017 17:51
 **/
@Service
@Transactional(readOnly = true)
public class SysConfigService implements ISystemParamGenerator, ISystemIniter {

    private Logger logger = LoggerFactory.getLogger(SysConfigService.class);
    @Autowired
    private HandlerFactory factory;

    @Autowired
    private UserService userService;

    @Autowired
    private DataOperatorFactory operatorFactory;

    /**
     * 查询系统配置信息
     *
     * @param versionCode 可以指定,也可以不指定
     * @return
     */
    public List<SysConfigDto> findAllSysConfig(String versionCode) {
        QueryParam param = new QueryParam();
        param.setResultClass(SysConfigDto.class);
        param.setTableDtos(Constants.DEFAULT_SYS_SCHEMA, SessionUtils.getDefaultVersion()
                , SysConfigDto.class);
        if (CommonUtils.isNotEmpty(versionCode)) {
            param.appendCriteria().andEqualTo(Constants.FixColumnName.VERSION_CODE, versionCode);
        }
        HandleResult result = factory.handleQuery(param);
        if (result.isSuccess()) {
            return (List<SysConfigDto>) result.getData();
        }
        return null;
    }

    @Override
    public List<SystemParam> getUserParams(LoginUser user) {
        return null;
    }

    @Override
    public List<SystemParam> getConstParams() {
        List<SysConfigDto> lstConfig = findAllSysConfig(null);
        if (lstConfig == null || lstConfig.isEmpty()) {
            return null;
        }
        List<SystemParam> lstResult = new ArrayList<>();
        lstConfig.forEach((dto -> {
            lstResult.add(toSystemParam(dto));
        }));
        return lstResult;
    }

    /**
     * 转换成系统全局参数
     *
     * @param configDto
     * @return
     */
    private SystemParam toSystemParam(SysConfigDto configDto) {
        return new SystemParam(configDto.getName(), String.valueOf(configDto.getValueType()),
                configDto.getConfigValue(), configDto.getConfigId(), configDto.getVersionCode());

    }


    /**
     * 初始化权限结构
     */
    @Override
    public void init() {
        logger.info("---初始化系统权限结构");
        SessionUtils.setDefaultVersion(findDefaultVersion());
        List<RightRelationDto> lstRelation = userService.findAllRelationDto(null);
        if (lstRelation == null || lstRelation.isEmpty()) {
            return;
        }
        List<RightResourceDto> lstRightResource = userService.findAllRightSourceDto(null);
        splitAndInit(lstRelation, lstRightResource);
        logger.info("---初始化系统权限结构 结束");
    }

    @Override
    public int getOrder() {
        return 200;
    }

    private String findDefaultVersion() {
        QueryParamDefinition queryParamDefinition = new QueryParamDefinition();
        queryParamDefinition.setTableDtos(VersionDto.class);
        queryParamDefinition.appendCriteria().andEqualTo("is_default", 1);
        VersionDto dto = operatorFactory.getDefaultDataOperator().selectOne(queryParamDefinition, VersionDto.class);
        if (dto == null) {
            logger.error("没有指定默认版本号");
            throw new InvalidConfigException("没有指定默认版本号");
        }
        return dto.getVersionCode();
    }

    /**
     * 分版本初始化
     *
     * @param lstRelation
     * @param lstSource   权限资源
     */
    private void splitAndInit(List<RightRelationDto> lstRelation, List<RightResourceDto> lstSource) {
        Map<String, List<RightResourceDto>> mapResource = new HashMap<>();
        lstSource.forEach(el -> {
            CommonUtils.addMapListValue(mapResource, el.getVersionCode(), el);
        });
        //分开
        Map<String, List<RightRelationDto>> mapRelation = new HashMap<>();
        lstRelation.forEach(el -> {
            CommonUtils.addMapListValue(mapRelation, el.getVersionCode(), el);
        });
        //生成
        Map<String, RightNode> rightStruct = new HashMap<>();
        Iterator<Map.Entry<String, List<RightResourceDto>>> iterator = mapResource.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, List<RightResourceDto>> next = iterator.next();
            String version = next.getKey();
            rightStruct.put(version, initOneVersionRelationStruct(mapRelation.get(version), next.getValue()));
        }
        //设置权限结构
        SessionUtils.setMapRightStruct(rightStruct);
    }

    private RightNode initOneVersionRelationStruct(List<RightRelationDto> lstRelation,
                                                   List<RightResourceDto> lstResource) {
        DirectedGraph<RightResourceDto> graph = new DirectedGraph<>();
        //先生成图,并检查是不是存在循环的情况
        lstResource.forEach(el -> {
            graph.addNode(el);
        });
        lstRelation.forEach(el -> {
            graph.addEdge(findResourceById(lstResource, el.getRsIdFrom())
                    , findResourceById(lstResource, el.getRsIdTo()));
        });
        CycleDetector<RightResourceDto> detector = new CycleDetector<>(graph);
        if (detector.containsCycle()) {
            List<RightResourceDto> lstCycleResource = detector.getVerticesInCycles();
            StringBuilder sb = new StringBuilder(":");
            lstCycleResource.forEach(el -> {
                sb.append(el.getRsName()).append(",");
            });
            throw new InvalidConfigException("权限关系存在循环,循环点是"
                    + sb.substring(0, sb.length() - 1) + ".");
        }
        return toNode(graph);
    }

    private RightNode toNode(DirectedGraph<RightResourceDto> graph) {
        RightNode root = new RightNode(-1L, "权限关系");
        Map<Long, RightNode> mapNode = new HashMap<>();
        //第一遍,生成node
        graph.forEach(el -> {
            RightNode node = new RightNode(el);
            node.setDto(el);
            mapNode.put(node.getRightId(), node);
        });
        //再次遍历,生成层次
        Iterator<Map.Entry<Long, RightNode>> itNodes =
                mapNode.entrySet().iterator();
        while (itNodes.hasNext()) {
            Map.Entry<Long, RightNode> next = itNodes.next();
            RightNode node = next.getValue();
            Set<RightResourceDto> lstToResource = graph.edgesFrom(node.getDto());
            if (lstToResource != null && !lstToResource.isEmpty()) {
                //如果有子节点,则要加入到父节点上
                lstToResource.forEach(el -> {
                    node.addSubNode(mapNode.get(el.getRsId()));
                });
            }
            Set<RightResourceDto> lstFromResource = graph.edgesTo(node.getDto());
            if (lstFromResource != null && !lstFromResource.isEmpty()) {
                lstFromResource.forEach(el -> {
                    node.addParentNode(mapNode.get(el.getRsId()));
                });
            } else {
                //如果没有你节点,则直接挂接到根节点
                root.addSubNode(node);
                node.addParentNode(root);
            }
        }
        return root;
    }

    /**
     * 根据 ID查询资源信息
     *
     * @param lstResource
     * @param id
     * @return
     */
    private RightResourceDto findResourceById(List<RightResourceDto> lstResource, Long id) {
        for (RightResourceDto dto : lstResource) {
            if (dto.getRsId().equals(id)) {
                return dto;
            }
        }
        return null;
    }


}
