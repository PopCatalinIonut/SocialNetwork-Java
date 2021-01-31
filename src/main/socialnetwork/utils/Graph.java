package main.socialnetwork.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Graph {
    private int dim;
    private Map<Long, ArrayList<Long>> adjList;

    public Graph(int dim) {
        this.dim = dim;
        adjList = new HashMap<>();
    }

    public void setAdjList(Map<Long, ArrayList<Long>> adjList) {
        this.adjList = adjList;
    }

    public void dfs(long start, HashMap<Long, Boolean> visited) {
        //System.out.println(start+" ");
        visited.put(start, true);
        for (long neighbour : adjList.get(start))
            if (!visited.get(neighbour))
                dfs(neighbour, visited);
    }

    public int countConnectedComponents() {
        boolean done = false;
        int count = 0;
        HashMap<Long, Boolean> visited = new HashMap<>();
        for (Long key : adjList.keySet())
            visited.put(key, false);
        for (Long node : adjList.keySet()) {
            if (!visited.get(node)) {
                    dfs(node, visited);
                    count++;
                //System.out.println();
            }
        }
        return count;
    }
}
