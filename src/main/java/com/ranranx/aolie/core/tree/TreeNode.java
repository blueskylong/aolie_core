package com.ranranx.aolie.core.tree;


import com.ranranx.aolie.core.common.CommonUtils;

import java.io.Serializable;
import java.util.*;


public class TreeNode<T> implements Node<T>, Cloneable, Serializable {

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 1943970746276746542L;

    T attributes;

    Object value;

    Object customObj;

    int level = 0;

    /**
     * 子树集合
     */
    ArrayList<Node> children;

    /**
     * 父结点
     */
    Node parent;

    private int height = -1;

    private int width = 0;

    private Map<Object, List<Node>> levelNodes = new HashMap<Object, List<Node>>();

    private Set<Node> levelNodesExist = new HashSet<Node>();

    private Object id;

    private Object sortValue;

    private String text;

    TreeNode(Object id, String text, Object sortValue, Object value, T attributes) {
        this.value = value;
        this.attributes = attributes;
        this.id = id;
        this.sortValue = sortValue;
        this.text = text;
    }

    TreeNode(T attributes) {
        this(null, null, null, null, attributes);
    }

    @Override
    public void append(Node node) {
        if (children == null) {
            children = new ArrayList<Node>();
        }
        ((TreeNode) node).parent = this;
        adjustChildLevel((TreeNode) node);
        children.add(node);
    }

    @Override
    public void append(Node[] nodes) {
        if (nodes == null || nodes.length < 1) {
            return;
        }
        for (Node node : nodes) {
            append(node);
        }
    }

    @Override
    public Object getValue() {
        return this.value;
    }

    @Override
    public void setValue(Object value) {
        this.value = value;
    }

    @Override
    public int getNodeType() {
        return parent == null ? Node.ROOT : ((children != null && children.size() > 0) ? Node.BRANCH : Node.LEAF);
    }

    @Override
    public int getChildrenCount() {
        return children == null ? 0 : children.size();
    }

    @Override
    public Node getChildAt(int i) {
        return (Node) this.children.get(i);
    }

    @Override
    public Node getParent() {
        return this.parent;
    }

    @Override
    public int getLevel() {
        return level;
    }

    @Override
    public Object clone() {
        Node cloned = new TreeNode(this.id, this.text, this.sortValue, this.value, this.attributes);
        for (int i = 0; i < getChildrenCount(); i++) {
            cloned.append((Node) getChildAt(i).clone());
        }
        return cloned;
    }

    @Override
    public int getHeight() {
        doHeightStatistic(this);
        return height;
    }

    protected void doHeightStatistic(Node node) {
        int currentLevel = node.getLevel();
        addLevelNode(currentLevel, node);
        if ((node.getChildrenCount() == 0) && currentLevel > height) {
            if (node != this) {
                height = currentLevel;
            }
        } else {
            for (int i = 0; i < node.getChildrenCount(); i++) {
                doHeightStatistic(node.getChildAt(i));
            }
        }
    }

    protected void addLevelNode(int level, Node node) {
        Integer key = new Integer(level);
        List<Node> nodeList = null;
        if (this.levelNodes.containsKey(key)) {
            nodeList = (List<Node>) levelNodes.get(key);
        } else {
            nodeList = new ArrayList<Node>();
        }
        if (!levelNodesExist.contains(node)) {
            nodeList.add(node);
            levelNodesExist.add(node);
        }
        levelNodes.put(key, nodeList);
    }

    protected void doWidthStatistic(Node node) {
        if (node.getNodeType() == Node.LEAF) {
            width++;
        } else {
            for (int i = 0; i < node.getChildrenCount(); i++) {
                doWidthStatistic(node.getChildAt(i));
            }
        }
    }

    @Override
    public int getWidth() {
        width = 0;
        doWidthStatistic(this);
        return width;
    }

    @Override
    public Node[][] toArray() {
        levelNodes.clear();
        levelNodesExist.clear();
        doHeightStatistic(this);
        Node[][] nodes = new Node[levelNodes.size()][];
        for (int i = 0; i < nodes.length; i++) {
            List<Node> list = levelNodes.get(new Integer(i));
            if (list != null) {
                nodes[i] = new Node[list.size()];
                System.arraycopy(list.toArray(), 0, nodes[i], 0, nodes[i].length);
            }
        }
        return nodes;
    }

    @Override
    public void insert(Node child, int i) {
        if (this.children == null) {
            children = new ArrayList<Node>();
        }
        ((TreeNode) child).parent = this;
        adjustChildLevel((TreeNode) child);
        children.add(i, child);
    }

    @Override
    public int indexOf(Node child) {
        return this.children == null ? -1 : children.indexOf(child);
    }

    protected void adjustChildLevel(TreeNode node) {
        node.level = ((TreeNode) node.parent).level + 1;
        for (int i = 0; i < node.getChildrenCount(); i++) {
            adjustChildLevel((TreeNode) node.getChildAt(i));
        }
    }

    @Override
    public Object getIdentifier() {
        return this.id;
    }

    @Override
    public Object getSortByValue() {
        return this.sortValue;
    }

    @Override
    public String getText() {
        return this.text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return getText();
    }

    @Override
    public boolean deleteSubNode(Node node) {
        if (node == null) {
            return false;
        }
        int iCount = getChildrenCount();
        if (iCount <= 0) {
            return false;
        }
        for (int i = 0; i < iCount; i++) {
            if (getChildAt(i) == node) {
                children.remove(i);
                return true;
            }
        }
        return false;
    }


    @Override
    public int getNodeHeight() {
        int i = 0;
        Node node = this;
        while (node.getParent() != null) {
            i++;
            node = node.getParent();
        }
        return i;
    }

    @Override
    public String getAttribute(String name) {
        return nonNullStr(CommonUtils.getObjectValue(attributes, name));
    }

    @Override
    public T getUserObject() {
        return attributes;
    }

    @Override
    public List<Node<T>> getLeafNodes() {
        List<Node<T>> lstNode = new ArrayList<>();
        if (this.getChildrenCount() == 0) {
            lstNode.add(this);
        } else {
            for (Node node : children) {
                getSubLeafNode(node, lstNode);
            }
        }
        return lstNode;
    }

    /**
     * 查询指定ID的子节点或孙子节点
     *
     * @param id
     * @return
     */
    @Override
    public Node findNode(Object id) {
        if (id == null) {
            return null;
        }
        Node[][] nodes = toArray();
        for (int i = 0; i < nodes.length; i++) {
            for (int j = 0; j < nodes[i].length; j++) {
                if (nodes[i][j].getIdentifier() != null && nodes[i][j].getIdentifier().equals(id)) {
                    return nodes[i][j];
                }
            }
        }
        return null;
    }


    private void getSubLeafNode(Node node, List<Node<T>> lstLeafNode) {
        if (node.getChildrenCount() == 0) {
            lstLeafNode.add(node);
        } else {
            for (Node child : node.getChildren()) {
                getSubLeafNode(child, lstLeafNode);
            }
        }
    }

    @Override
    public Object getCustomObject() {
        return customObj;
    }

    @Override
    public void setCustomObject(Object obj) {
        this.customObj = obj;
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
        if (CommonUtils.isEmpty(o)) {
            return def;
        }
        return o.toString();
    }

    @Override
    public Node[] getChildren() {
        if (children == null || children.isEmpty()) {
            return new Node[0];
        }
        Node[] nodes = new Node[children.size()];
        for (int i = 0; i < nodes.length; i++) {
            nodes[i] = children.get(i);

        }
        return nodes;
    }

    @Override
    public boolean clearChildren() {
        if (children != null) {
            children.clear();
        }
        return true;
    }
}