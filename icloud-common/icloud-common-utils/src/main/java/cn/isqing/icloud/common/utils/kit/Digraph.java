package cn.isqing.icloud.common.utils.kit;

import lombok.Data;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * 有向图实现
 * 注意自定义对象需要重写hashCode和equals方法
 *
 * @author songqingwei@aliyun.com
 * @version 1.0
 **/
@Data
public class Digraph<T> {

    /**
     * 邻接表 adjacency list
     */
    private Map<T, Set<T>> adj = new HashMap<>();

    // 往图中添加一条边v->w
    public void addEdge(T vertexLeft, T vertexRight) {
        Set<T> set = getSet(vertexLeft);
        if(vertexRight !=null) {
            set.add(vertexRight);
        }
    }

    // 获取某个顶点的邻接元素集合
    public Set<T> getSet(T vertex) {
        return adj.computeIfAbsent(vertex, l -> new HashSet<>());
    }

    // 获取该图的反向图
    public void reverse() {
        // 创建一个图保存反向图
        Map<T, Set<T>> adj = this.adj;
        this.adj = new HashMap<>();
        // 遍历邻接表，构建反向图
        adj.forEach((left, set) -> {
            if(set.isEmpty()){
                addEdge(left,null);
            }
            set.forEach(right -> addEdge(right, left));
        });
    }

}
