package cn.isqing.icloud.common.utils.kit;

import java.util.*;

/**
 * 拓扑排序/顶点排序
 * DFS:depth first search  深度优先搜索
 * 注意要先调用环检测器，有环就请不要调用排序
 *
 * @author songqingwei@aliyun.com
 * @version 1.0
 **/
public class DigraphDFSSort<T> {
    /**
     * 邻接表 adjacency list
     */
    private Map<T, Set<T>> adj;

    // 记录顶点是否已被搜索
    private Map<T, Boolean> searched;
    // 记录顶点是否已被搜索
    private Deque<T> deque = new ArrayDeque<>();

    public DigraphDFSSort(Map<T, Set<T>> adj) {
        this.adj = adj;
    }

    public Deque<T> sort() {
        adj.forEach((k, v) -> {
            if (searched.get(k) == null) {
                dfs(k);
            }
        });
        return deque;
    }


    private void dfs(T t) {
        searched.put(t, Boolean.TRUE);
        Set<T> set = adj.getOrDefault(t, Collections.emptySet());
        set.forEach(v -> {
            if (searched.get(v) == null) {
                dfs(v);
            }
        });
        deque.push(t);
    }


}
