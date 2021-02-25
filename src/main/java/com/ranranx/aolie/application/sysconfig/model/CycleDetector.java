package com.ranranx.aolie.application.sysconfig.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 循环检查,摘自 https://www.tuicool.com/articles/ERfiym
 *
 * @version V0.0.1
 * @modified by  xxl
 * @date 2021/2/20 0020 15:22
 **/
public class CycleDetector<T> {
    private static final String marked = "marked";

    private static final String complete = "complete";

    private DirectedGraph<T> graph;

    private Map<T, String> marks;

    private List<T> verticesInCycles;

    public CycleDetector(DirectedGraph<T> graph) {
        this.graph = graph;
        marks = new HashMap<T, String>();
        verticesInCycles = new ArrayList<T>();
    }

    public boolean containsCycle() {
        for (T v : graph) {
            // 如果v正在遍历或者遍历完成,不需要进入mark(),因为mark是一个递归调用，使用的是深度优先搜索算法;
            // 这是为了保证1个顶点只会遍历一次
            if (!marks.containsKey(v)) {
                if (mark(v)) {
                    // return true;
                }
            }
        }

        return !verticesInCycles.isEmpty();
    }

    //DFS算法,遍历顶点vertex
    // @return 当前顶点是否在环上
    private boolean mark(T vertex) {
        List<T> localCycles = new ArrayList<T>();

        // 当前顶点vertex,遍历开始
        marks.put(vertex, marked);

        for (T u : graph.edgesFrom(vertex)) {
            // u的遍历还没有结束,说明存在u->vertex的通路,也存在vertex->u的通路,形成了循环
            if (marks.containsKey(u) && marks.get(u).equals(marked)) {
                localCycles.add(vertex);
                // return true;
            } else if (!marks.containsKey(u)) {
                if (mark(u)) {
                    localCycles.add(vertex);
                    // return true;
                }
            }
        }

        // 当前顶点vertex,遍历完成
        marks.put(vertex, complete);

        verticesInCycles.addAll(localCycles);
        return !localCycles.isEmpty();
    }

    public List<T> getVerticesInCycles() {
        return verticesInCycles;
    }
}
