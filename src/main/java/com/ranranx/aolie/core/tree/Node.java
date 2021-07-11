package com.ranranx.aolie.core.tree;

import java.util.List;

public interface Node extends Cloneable {
    /**
     * 标识根结点的常量 结点类型：根、枝、叶
     */
    int ROOT = -1;
    /**
     * 标识枝头的常量
     */
    int BRANCH = 1;
    /**
     * 标识叶结点的常量
     */
    int LEAF = 0;

    /**
     * 获得结点类型
     *
     * @return 结点类型
     */

    int getNodeType();

    /**
     * 获得父结点
     *
     * @return 父结点
     */
    Node getParent();

    /**
     * 取得自定义值，可以保存任意对象
     *
     * @return
     */
    Object getCustomObject();

    /**
     * 保存自定义对象
     *
     * @return
     */
    void setCustomObject(Object obj);

    /**
     * 获得结点值
     *
     * @return 结点值
     */
    Object getValue();

    /**
     * 得到第i个子结点
     *
     * @param i 下标
     * @return 子结点
     */
    Node getChildAt(int i);

    /**
     * 得到子结点数
     *
     * @return 子结点数
     */
    int getChildrenCount();

    Object getIdentifier();

    Object getSortByValue();

    /**
     * 添加一个子结点
     *
     * @param child 子结点
     */
    void append(Node child);

    /**
     * 指定下标处插入一个子结点
     *
     * @param child
     * @param i
     */
    void insert(Node child, int i);

    /**
     * Clone一个结点
     *
     * @return clone的结点
     */
    Object clone();

    /**
     * 设置结点值
     *
     * @param value 结点值
     */
    void setValue(Object value);

    /**
     * 得到结点的树层级
     *
     * @return 层级
     */
    int getLevel();

    /**
     * 获得子树的深度
     *
     * @return
     */
    int getHeight();

    /**
     * 获得子树的宽度，所有最末级子结点数
     *
     * @return
     */
    int getWidth();

    String getText();

    /**
     * 判断子结点所在的下标
     *
     * @param child
     * @return
     */
    int indexOf(Node child);

    /**
     * 将树的所有结点按照广度优先化为二维表形式，一维下标就是结点所处的层次（相对于子树的起始根结点）
     *
     * @return
     */
    Node[][] toArray();

    /**
     * 删除一个下级节点
     */
    boolean deleteSubNode(Node node);

    /**
     * 清空子节点
     *
     * @return
     */
    boolean clearChildren();

    /**
     * 取得所有直接子节点
     *
     * @return
     */
    Node[] getChildren();

    /**
     * 批量增加子节点
     *
     * @param node
     */
    void append(Node[] node);

    /**
     * 获取节点自身高度值
     *
     * @return
     */
    int getNodeHeight();

    String getAttribute(String name);

    Object getUserObject();

    List<Node> getLeafNodes();

    /**
     * 查询指定ID的子节点或孙子节点
     *
     * @param id
     * @return
     */
    Node findNode(Object id);

}