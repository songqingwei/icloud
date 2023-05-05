package cn.isqing.icloud.common.utils.kit;

import java.util.*;

public class TopologicalSort {

    /**
     * 对图进行拓扑排序
     * @param adj 邻接表
     * @return 拓扑排序的结果，按层级给出，每一层都是可以并行执行的节点的集合
     */
    public static <T> List<List<T>> sort(Map<T, Set<T>> adj) {
        // 用于统计每个节点的入度
        Map<T, Integer> inDegree = new HashMap<>();
        // 初始化每个节点的入度为0
        for (T node : adj.keySet()) {
            inDegree.put(node, 0);
        }
        // 计算每个节点的入度
        for (T node : adj.keySet()) {
            for (T neighbor : adj.get(node)) {
                inDegree.put(neighbor, inDegree.getOrDefault(neighbor, 0) + 1);
            }
        }
        // 用于存储入度为0的节点
        Queue<T> queue = new LinkedList<>();
        // 找到所有入度为0的节点并加入队列
        for (T node : inDegree.keySet()) {
            if (inDegree.get(node) == 0) {
                queue.offer(node);
            }
        }
        // 用于存储按层级排列的节点集合
        List<List<T>> result = new ArrayList<>();
        // 每次处理入度为0的节点，加入结果集中，并更新其他节点的入度
        while (!queue.isEmpty()) {
            // 当前层级的节点数量
            int levelSize = queue.size();
            // 当前层级的节点集合
            List<T> level = new ArrayList<>();
            for (int i = 0; i < levelSize; i++) {
                T node = queue.poll();
                // 加入当前层级的节点集合
                level.add(node);
                // 更新node的邻居节点的入度
                for (T neighbor : adj.get(node)) {
                    inDegree.put(neighbor, inDegree.get(neighbor) - 1);
                    // 如果邻居节点的入度为0，则加入队列
                    if (inDegree.get(neighbor) == 0) {
                        queue.offer(neighbor);
                    }
                }
            }
            // 将当前层级的节点集合加入结果集
            result.add(level);
        }
        return result;
    }
}
