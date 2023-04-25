package cn.isqing.icloud.common.utils.kit;

import java.util.*;

/**
 *
 * @author songqingwei@aliyun.com
 * @version 1.0
 **/
public class DigraphRingChecker<T> {

    /**
     * 邻接表 adjacency list
     */
    private Map<T, Set<T>> adj;

    // 记录顶点是否已被搜索
    private Map<T,Boolean> searched;
    // 多个环顶点
    private Set<T> cycles;
    // 记录是否有顶点
    private boolean hasCycle = false;
    // 存储当前路径
    private Deque<T> deque = new ArrayDeque<>();

    public DigraphRingChecker(Digraph digraph) {
        adj = digraph.getAdj();
    }

    // 是否有环
    public boolean hasCycle() {
        adj.forEach((k,v)->{
            if (searched.get(k)==null) {
                dfs(k);
            }
        });
        return hasCycle;
    }


    private void dfs(T t) {
        searched.put(t,Boolean.TRUE);
        deque.push(t);
        Set<T> set = adj.getOrDefault(t, Collections.emptySet());
        set.forEach(v->{
            if(deque.contains(v)){
                cycles.add(v);
                hasCycle = true;
                return;
            }
            if(searched.get(v)==null){
                dfs(v);
            }
        });
        deque.pop();
    }



}
