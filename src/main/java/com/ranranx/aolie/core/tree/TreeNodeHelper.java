
package com.ranranx.aolie.core.tree;


import com.ranranx.aolie.core.common.CommonUtils;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


/**
 * 生成树节点信息
 */
public class TreeNodeHelper {
    /**
     * 树结点的创建工具
     */
    private TreeFactory treeFactory = TreeFactory.getInstance();

    protected TreeNodeHelper() {
    }

    private static TreeNodeHelper inst;

    public static TreeNodeHelper getInstance() {
        return inst == null ? inst = new TreeNodeHelper() : inst;
    }

    public Node generate(List<Object> ds, String idName, String textName, String parentIdName,
                         SysCodeRule codeRule, String sortKey, String nodeHeightName) throws Exception {
        Map<String, Node> nodeCache = createNodes(ds, idName, textName, sortKey);
        return parseTree(ds, nodeCache, idName, parentIdName, codeRule)[0];
    }

    public String checkNodeStruct(List<Object> ds, String idName, String textName, String parentIdName,
                                  SysCodeRule codeRule, String sortKey, String nodeHeightName) throws Exception {
        Map<String, Node> nodeCache = createNodes(ds, idName, textName, sortKey);
        Node[] nodes = parseTree(ds, nodeCache, idName, parentIdName, codeRule);
        StringBuffer sb = new StringBuffer();
        if (nodes[1].getChildrenCount() > 0) {
            for (int i = 0; i < nodes[1].getChildrenCount(); i++) {
                Node node = nodes[1].getChildAt(i);
                sb.append(node.getAttribute(textName));
                if (i >= 3) {
                    sb.append("等，");
                    break;
                } else {
                    sb.append(",");
                }
            }
            return sb.delete(sb.length() - 1, sb.length()).append("没有找到父节点信息").toString();
        }
        return null;
    }

    public Node generateObject(List<Object> ds, String idName, String textName, String parentIdName,
                               SysCodeRule codeRule, String sortKey, String nodeHeightName) throws Exception {
        Map<String, Node> nodeCache = createNodes(ds, idName, textName, sortKey);
        return parseTree(ds, nodeCache, idName, parentIdName, codeRule)[0];
    }

    private Map<String, Node> createNodes(List<Object> ds, String idName, String textName, String sortKey) {
        Map<String, Node> nodeCache = new HashMap<String, Node>();
        int iCount = ds.size();
        for (int i = 0; i < iCount; i++) {
            Object mapRow = ds.get(i);
            if (!(mapRow instanceof Map)) {
                mapRow = CommonUtils.toMap(mapRow, false);
            }
            String id = nonNullStr(getAStringField(mapRow, idName));

            String sortKeyValue = id;
            if (sortKey != null) {
                Object obj = getAStringField(mapRow, sortKey);
                if (!isNullStr(obj)) {
                    sortKeyValue = obj.toString();
                }
            }
            String text = null;
            if (textName != null) {
                Object obj = getAStringField(mapRow, textName);
                if (!isNullStr(obj)) {
                    text = obj.toString();
                }
            }

            int nodeHeight = 0;

            Node node = TreeFactory.createTreeNode(id, text, sortKeyValue, null, nodeHeight, mapRow);
            nodeCache.put(id, node);
        }
        return nodeCache;
    }

    /**
     * @param nodeCache    结点缓冲
     * @param idName       结点ID字段名
     * @param parentIdName 父结点ID的字段名
     * @param codeRule     描述树结点父子关系编码规则
     *                     排序关键字字段名
     * @return 返回生成树的根节点
     */
    protected Node[] parseTree(List<Object> ds, Map<String, Node> nodeCache, String idName,
                               String parentIdName, SysCodeRule codeRule) throws Exception {
        Node root = treeFactory.createTreeNode(null);
        Node noParent = treeFactory.createTreeNode(null);

        for (Iterator<Node> it = nodeCache.values().iterator(); it.hasNext(); ) {
            Node node = it.next();
            String id = (String) node.getAttribute(idName);

            String parentId = getParentId(id, node.getAttribute(parentIdName), codeRule);
            Node parent = null;
            if (isNullStr(parentId)) {
                parent = root;
            } else {
                parent = (Node) nodeCache.get(parentId);
            }
            if (parent != null) {
                addChildNode(parent, node);
            } else if (CommonUtils.isNotEmpty(parentId)) {//设置错误找不到父节点
                addChildNode(noParent, node);
            }
        }
        return new Node[]{root, noParent};
    }

    protected void addChildNode(Node parent, Node newChild) {
        for (int i = 0; i < parent.getChildrenCount(); i++) {
            Node child = parent.getChildAt(i);
            String childSortVal = nonNullStr(child.getSortByValue());
            String newChildSortValue = nonNullStr(newChild.getSortByValue());
            if (childSortVal.compareTo(newChildSortValue) > 0) {
                parent.insert(newChild, i);
                return;
            }
        }
        parent.append(newChild);
    }

    protected String getParentId(String id, String parentId, SysCodeRule codeRule) throws Exception {
        if (codeRule != null) {
            return codeRule.previous(id);
        } else {
            return parentId;
        }
    }

    /**
     * 由一个对象返回非空的字符串
     *
     * @param o 传入的对象
     * @return 生成的字符串
     */
    public static String nonNullStr(Object o) {
        return nonNullStr(o, "");
    }

    public static String nonNullStr(Object o, String def) {
        String s = o == null ? def : o.toString();
        if (s.equalsIgnoreCase("null")) {
            s = "";
        }
        return s;
    }

    public static String nonNullStr2(Object o, String def) {
        if (isNullStr(o)) {
            return def;
        }
        return o.toString();
    }


    /**
     * 取得一Map中的一字段的字符值 XXL
     *
     * @param aData
     * @param sField
     * @return
     */
    public static String getAStringField(Object aData, String sField) {
        if (aData == null) {
            return null;
        }
        if (aData instanceof Map) {
            return CommonUtils.getStringField((Map) aData, sField);
        } else if (aData instanceof List) {
            Map map = (Map) ((List) aData).get(0);
            return getAStringField(map, sField);
        }
        try {
            Method method = aData.getClass().getMethod(sField);
            if (method != null) {
                return nonNullStr(aData.getClass().getMethod(sField).invoke(aData));
            } else {
                method = aData.getClass().getMethod("get" + sField.substring(0, 1).toLowerCase() + sField.substring(1));
                if (method != null) {
                    return nonNullStr(aData.getClass().getMethod(sField).invoke(aData));
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }

    /**
     * 判断一个空字符串（null或者""）
     *
     * @param s 待判断的字符串
     * @return 判断结果
     */
    public static boolean isNullStr(String s) {
        return s == null || s.trim().length() <= 0 || s.trim().equals("null");
    }

    public static boolean isNullStr(Object s) {
        return s == null || s.toString() == null || (s.toString().trim().length()) <= 0
                || s.toString().trim().equals("null");
    }
}
