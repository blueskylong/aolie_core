package com.ranranx.aolie.core.tree;

import java.util.Map;

public class TreeFactory {
    /**
     * 类的全局句柄
     */
    private static TreeFactory instance;

    protected TreeFactory() {
    }

    /**
     * 获得TreeFactory的实例
     *
     * @return 实例句柄
     */
    public static TreeFactory getInstance() {
        return instance == null ? instance = new TreeFactory() : instance;
    }

    /**
     * 用属性集合创建一个树结点
     *
     * @param attributes 属性集
     * @return 树结点
     */
    public Node createTreeNode(Map<String, Object> attributes) {
        return createTreeNode(null, attributes);
    }

    /**
     * 用结点值、属性集合创建一个树结点
     *
     * @param value      结点值
     * @param attributes 属性集
     * @return 树结点
     */
    public Node createTreeNode(Object value, Map<String, Object> attributes) {
        return new TreeNode(null, "根节点", null, value, attributes);
    }

    /**
     * 用结点值、属性集合创建一个树结点
     */
    public Node createTreeNode(Object id, Object sortValue, Object value, Map<String, Object> attributes) {
        return new TreeNode(id, "根节点", sortValue, value, attributes);
    }

    /**
     * 用结点值、属性集合、文本内容创建一个树结点
     */
    public static Node createTreeNode(Object id, String text, Object sortValue, Object value,
                                      Map<String, Object> attributes) {
        return new TreeNode(id, text, sortValue, value, attributes);
    }

    public static Node createTreeNode(Object id, String text, Object sortValue, Object value,
                                      Object attributes) {
        return new TreeNode(id, text, sortValue, value, attributes);
    }
}