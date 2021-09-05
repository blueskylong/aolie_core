
package com.ranranx.aolie.core.tree;


import com.ranranx.aolie.core.common.CommonUtils;

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

    public <T> Node<T> generateByCode(List<T> ds, String idName, String codeField, String textName,
                                      SysCodeRule codeRule) {
        Map<String, Node<T>> nodeCache = createNodesByCode(ds, idName, codeField, textName);
        return parseTreeByCode(ds, nodeCache, codeField, codeRule);
    }

    public <T> Node<T> generateById(List<T> ds, String idName, String textName,
                                    String parentIdName, String sortField) throws Exception {
        Map<String, Node<T>> nodeCache = createNodesById(ds, idName, textName, sortField);
        return parseTreeById(ds, nodeCache, idName, parentIdName);
    }

//    public String checkNodeStruct(List<?> ds, String idName, String textName, String codeField,
//                                  SysCodeRule codeRule) throws Exception {
//        Map<String, Node> nodeCache = createNodesByCode(ds, idName, codeField, textName);
//        Node[] nodes = parseTreeByCode(ds, nodeCache, idName, codeRule);
//        StringBuffer sb = new StringBuffer();
//        if (nodes[1].getChildrenCount() > 0) {
//            for (int i = 0; i < nodes[1].getChildrenCount(); i++) {
//                Node node = nodes[1].getChildAt(i);
//                sb.append(node.getAttribute(textName));
//                if (i >= 3) {
//                    sb.append("等，");
//                    break;
//                } else {
//                    sb.append(",");
//                }
//            }
//            return sb.delete(sb.length() - 1, sb.length()).append("没有找到父节点信息").toString();
//        }
//        return null;
//    }

//    public Node generateObject(List<?> ds, String idName, String codeField, String textName,
//                               SysCodeRule codeRule, String sortKey) throws Exception {
//        Map<String, Node> nodeCache = createNodesByCode(ds, idName, textName, sortKey);
//        return parseTreeByCode(ds, nodeCache, codeField, codeRule)[0];
//    }

    private <T> Map<String, Node<T>> createNodesByCode(List<T> ds, String idName, String codeField, String textName) {
        Map<String, Node<T>> nodeCache = new HashMap<String, Node<T>>();
        int iCount = ds.size();
        for (int i = 0; i < iCount; i++) {
            T nodeData = ds.get(i);

            String id = nonNullStr(CommonUtils.getObjectValue(nodeData, idName));

            String sortKeyValue = id;

            Object obj = CommonUtils.getObjectValue(nodeData, codeField);
            if (!isNullStr(obj)) {
                sortKeyValue = obj.toString();
            }

            String text = null;
            if (textName != null) {
                Object obj1 = CommonUtils.getObjectValue(nodeData, textName);
                if (!isNullStr(obj1)) {
                    text = obj1.toString();
                }
            }

            Node<T> node = TreeFactory.createTreeNode(id, text, sortKeyValue, ds.get(i), nodeData);
            nodeCache.put(sortKeyValue, node);
        }
        return nodeCache;
    }

    private <T> Map<String, Node<T>> createNodesById(List<T> ds, String idName, String textName, String sortKey) {
        Map<String, Node<T>> nodeCache = new HashMap<String, Node<T>>();
        int iCount = ds.size();
        for (int i = 0; i < iCount; i++) {
            T nodeData = ds.get(i);

            String id = nonNullStr(CommonUtils.getObjectValue(nodeData, idName));
            String sortKeyValue = id;
            Object obj = CommonUtils.getObjectValue(nodeData, sortKey);
            if (!isNullStr(obj)) {
                sortKeyValue = obj.toString();
            }

            String text = null;
            if (textName != null) {
                Object obj1 = CommonUtils.getObjectValue(nodeData, textName);
                if (!isNullStr(obj1)) {
                    text = obj1.toString();
                }
            }

            Node<T> node = TreeFactory.createTreeNode(id, text, sortKeyValue, ds.get(i), nodeData);
            nodeCache.put(id, node);
        }
        return nodeCache;
    }

    /**
     * @param nodeCache 结点缓冲
     * @param codeField 结点ID字段名
     * @param codeRule  描述树结点父子关系编码规则
     *                  排序关键字字段名
     * @return 返回生成树的根节点
     */
    protected <T> Node<T> parseTreeByCode(List<T> ds, Map<String, Node<T>> nodeCache, String codeField,
                                          SysCodeRule codeRule) {
        Node root = treeFactory.createTreeNode(null);

        for (Iterator<Node<T>> it = nodeCache.values().iterator(); it.hasNext(); ) {
            Node node = it.next();
            String id = node.getAttribute(codeField);

            String parentId = getParentId(id, codeRule);
            Node parent = null;
            if (isNullStr(parentId)) {
                parent = root;
            } else {
                parent = nodeCache.get(parentId);
            }
            if (parent != null) {
                addChildNode(parent, node);
            } else if (CommonUtils.isNotEmpty(parentId)) {//设置错误找不到父节点
                addChildNode(root, node);
            }
        }
        return root;
    }

    /**
     * @param nodeCache 结点缓冲
     *                  排序关键字字段名
     * @return 返回生成树的根节点
     */
    protected <T> Node<T> parseTreeById(List<T> ds, Map<String, Node<T>> nodeCache, String idField,
                                        String parentIdName) throws Exception {
        Node root = treeFactory.createTreeNode(null);

        for (Iterator<Node<T>> it = nodeCache.values().iterator(); it.hasNext(); ) {
            Node node = it.next();
            String id = node.getAttribute(idField);

            String parentId = node.getAttribute(parentIdName);
            Node parent = null;
            if (isNullStr(parentId)) {
                parent = root;
            } else {
                parent = nodeCache.get(parentId);
            }
            if (parent != null) {
                addChildNode(parent, node);
            } else if (CommonUtils.isNotEmpty(parentId)) {//设置错误找不到父节点
                addChildNode(root, node);
            }
        }
        return root;
    }

    protected <T> void addChildNode(Node<T> parent, Node<T> newChild) {
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

    protected String getParentId(String id, SysCodeRule codeRule) {
        return codeRule.previous(id);
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
