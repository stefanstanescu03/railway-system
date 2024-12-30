package com.example.railway_management_system.ruta;

import java.util.*;

public class Graph {
    static class Pair {
        int first, second;

        Pair(int first, int second) {
            this.first = first;
            this.second = second;
        }
    }

    private final int V;
    private final List<List<Pair>> adj;

    Graph(int V) {
        this.V = V;
        adj = new ArrayList<>();
        for (int i = 0; i < V; i++) {
            adj.add(new ArrayList<>());
        }
    }

    void addEdge(int start, int finish, int weight) {
        adj.get(start).add(new Pair(finish, weight));
    }

    List<Integer> shortestPath(int src, int dest) {
        PriorityQueue<Pair> pq = new PriorityQueue<>(V, Comparator.comparingInt(o -> o.second));
        int[] dist = new int[V];
        int[] prev = new int[V];
        Arrays.fill(dist, Integer.MAX_VALUE);
        Arrays.fill(prev, 0);

        pq.add(new Pair(0, src));
        dist[src] = 0;

        while(!pq.isEmpty()) {
            int curr = pq.poll().second;

            for (Pair neighbour : adj.get(curr)) {
                if (dist[neighbour.first] > dist[curr] + neighbour.second) {
                    dist[neighbour.first] = dist[curr] + neighbour.second;
                    prev[neighbour.first] = curr;
                    pq.add(new Pair(dist[neighbour.first], neighbour.first));
                }
            }
        }

        if (dist[dest] == Integer.MAX_VALUE) {
            return new ArrayList<Integer>();
        }

        List<Integer> shortest = new ArrayList<Integer>();
        int curr = dest;
        while (curr != src) {
            shortest.add(curr);
            curr = prev[curr];
        }
        shortest.add(curr);
        return shortest;
    }

}
