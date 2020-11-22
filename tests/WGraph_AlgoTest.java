package ex1.tests;

import ex1.src.*;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class WGraph_AlgoTest {

    @Test
    void copy() {
        WGraph_Algo graphAlgo = new WGraph_Algo();
        WGraph_Algo graphAlgoCopy = new WGraph_Algo();
        weighted_graph graph = new WGraph_DS();
        weighted_graph graphcopy = new WGraph_DS();

        for(int i = 0; i < 500; i++)
            graph.addNode(i);

        for(int i = 0; i < 800; i++){
            int x = (int) (Math.random() * 799);
            int y = (int) (Math.random() * 799);
            double w = (Math.random() * 10);
            graph.connect(x,y,w);
        }
        graphAlgo.init(graph);
        graphcopy = graphAlgo.copy();
        graphAlgoCopy.init(graphcopy);

        if(graphAlgo.isConnected())
            assertTrue(graphAlgoCopy.isConnected());
        else assertFalse(graphAlgoCopy.isConnected());

        for(int i = 0; i < 1000;i++) {
            int x = (int) (Math.random() * 499);
            int y = (int) (Math.random() * 499);
            if (graph.hasEdge(x, y)) {
                double shortp = graphAlgo.shortestPathDist(x, y);
                double shortpcopy = graphAlgoCopy.shortestPathDist(x, y);
                assertEquals(shortp, shortpcopy);

                List<node_info> path1 =  graphAlgo.shortestPath(x,y);
                List<node_info> path2 =  graphAlgoCopy.shortestPath(x,y);

                assertEquals(path1.size(),path2.size());

                for(int j = 0; j <path1.size();j++)
                    assertEquals(path1.get(j).getKey(),path2.get(j).getKey());

            }
        }

        for(int i = 0; i < 500; i++)
            assertTrue(graphcopy.getNode(i) != null);


        int s = graph.nodeSize();
        int sc = graphcopy.nodeSize();
        assertEquals(s,sc);

        int e = graph.edgeSize();
        int ec = graphcopy.edgeSize();
        assertEquals(e,ec);

        for(int i = 0; i < 2000;i++){
           int x = (int) (Math.random() * 799);
           int y = (int) (Math.random() * 799);
           if(graph.hasEdge(x,y)){
               assertTrue(graphcopy.hasEdge(x,y));
               assertTrue(graphcopy.hasEdge(y,x));
               double w1 = graph.getEdge(x,y);
               double w2 = graphcopy.getEdge(x,y);
               assertEquals(w1,w2);
           }
        }

        for(int i =0; i < 2000;i++) {
            int x = (int) (Math.random() * 499);
            int y = (int) (Math.random() * 499);
            double w =  (Math.random()*100);
            graph.removeNode(x);
            graph.removeEdge(x,y);
            assertTrue(graphcopy.getNode(x) != null);
            assertTrue(graph.getNode(x) == null);

            if (graph.hasEdge(x,y)) {
                assertFalse(graph.hasEdge(x,y));
                assertTrue(graphcopy.hasEdge(x,y));
            }
            if(!graph.hasEdge(x,y) && !graphcopy.hasEdge(x,y)){
                graph.connect(x,y,w);
                assertFalse(graphcopy.hasEdge(x,y));
            }

            if(graph.hasEdge(x,y) && graphcopy.hasEdge(x,y)){
                graphcopy.removeEdge(x,y);
                assertFalse(graphcopy.hasEdge(x,y));
                assertTrue(graph.hasEdge(x,y));
            }

        }
    }

    @Test
    void isConnected() {
        WGraph_Algo graphAlgo = new WGraph_Algo();
        weighted_graph graph2 = new WGraph_DS();
        weighted_graph graph = new WGraph_DS();

        graph2.addNode(1);
        graphAlgo.init(graph2);
        assertTrue(graphAlgo.isConnected());

        graph2.removeNode(1);
        assertTrue(graphAlgo.isConnected());

        for(int i = 0; i < 200; i++) {
            graph.addNode(i);
            graph2.addNode(i);
        }

        graphAlgo.init(graph);
        assertFalse(graphAlgo.isConnected());

        for(int i = 0 ; i < 200; i++){
            int j = i +1;
            double w = Math.random();
            graph.connect(i,j,w);
            graph2.connect(i,j,w);
        }

        assertTrue(graphAlgo.isConnected());

        graph.addNode(200);
        assertFalse(graphAlgo.isConnected());

        graph.connect(199,200,4.2);
        assertTrue(graphAlgo.isConnected());

        graph.removeNode(200);
        assertTrue(graphAlgo.isConnected());

        graph.removeNode(190);
        assertFalse(graphAlgo.isConnected());

        graphAlgo.init(graph2);

        for(int i = 0; i < 200; i ++){
            int x = (int) ((Math.random()*200) + 201);
            double w =  Math.random();
            graph2.addNode(x);
            assertFalse(graphAlgo.isConnected());

            graph2.connect(x,i,w);
            assertTrue(graphAlgo.isConnected());
            graph2.removeNode(x);
        }
    }

    @Test
    void shortestPathDist() {
        weighted_graph graph = small_graph();
        WGraph_Algo graphAlgo = new WGraph_Algo();
        graphAlgo.init(graph);

        double w0 = graphAlgo.shortestPathDist(3,4);
        assertEquals(5.8,w0);
        w0 = graphAlgo.shortestPathDist(4,3);
        assertEquals(5.8,w0);

        for(int i = 1; i  <= 7 ; i++){
            double w1 = graphAlgo.shortestPathDist(i,8);
            assertEquals(-1,w1);
        }

        double w2 = graphAlgo.shortestPathDist(6,2);
        assertEquals(10.899999999999999,w2);

        graph.removeNode(1);

       for(int i = 1; i <= 7 ; i++){
            double w3 = graphAlgo.shortestPathDist(i,2);
            assertEquals(-1,w3);
         }

       graph.removeEdge(3,5);
       graph.removeEdge(3,7);

       for(int i = 1 ; i <= 7; i++){
           double w4 = graphAlgo.shortestPathDist(i,3);
           assertEquals(-1,w4);
        }

       graph.connect(3,2,15.76);
       assertEquals(graphAlgo.shortestPathDist(3,2) , 15.76);

       graph.addNode(1);
       graph.connect(1,3,1.6);
       graph.connect(1,5,1);

       double w5 = graphAlgo.shortestPathDist(5,2);
       assertEquals(w5,2.6+15.76);

       graph.removeEdge(4,6);
       graph.removeEdge(4,5);

       for(int i = 1 ; i <= 8 ; i++){
           double w6 = graphAlgo.shortestPathDist(i,4);
           assertEquals(-1,w6);
       }

    }

    @Test
    void shortestPath() {
        weighted_graph graph = small_graph();
        WGraph_Algo graphAlgo = new WGraph_Algo();
        graphAlgo.init(graph);

        List<node_info> nodeList = graphAlgo.shortestPath(3,4);
        assertEquals(3,nodeList.size());

        assertTrue(nodeList.get(0).getKey() == 3);
        assertTrue(nodeList.get(1).getKey() == 5);
        assertTrue(nodeList.get(2).getKey() == 4);

        nodeList = graphAlgo.shortestPath(8,3);
        assertEquals(null,nodeList);
        nodeList = graphAlgo.shortestPath(8,2);
        assertEquals(null,nodeList);
        nodeList = graphAlgo.shortestPath(8,10);
        assertEquals(null,nodeList);
        nodeList = graphAlgo.shortestPath(9,3);
        assertEquals(null,nodeList);

        graph.removeNode(1);
        for(int i = 0 ; i < 8 ; i++) {
            nodeList = graphAlgo.shortestPath(1, i);
            assertEquals(null, nodeList);
            nodeList = graphAlgo.shortestPath(i, 1);
            assertEquals(null, nodeList);
        }

        graph.removeEdge(3,5);
        graph.removeEdge(3,7);

        for(int i = 0 ; i < 8 ; i++) {
            nodeList = graphAlgo.shortestPath(3, i);
            assertEquals(null, nodeList);
            nodeList = graphAlgo.shortestPath(i, 3);
            assertEquals(null, nodeList);
        }

        nodeList = graphAlgo.shortestPath(7, 4);
        assertTrue(nodeList.get(0).getKey() == 7);
        assertTrue(nodeList.get(1).getKey() == 6);
        assertTrue(nodeList.get(2).getKey() == 4);

        graph.removeNode(3);
        graph.removeNode(3);
        graph.addNode(1);
        graph.connect(5,1,1.8);
        nodeList = graphAlgo.shortestPath(6, 1);
        assertTrue(nodeList.get(0).getKey() == 6);
        assertTrue(nodeList.get(1).getKey() == 7);
        assertTrue(nodeList.get(2).getKey() == 5);
        assertTrue(nodeList.get(3).getKey() == 1);

        graph.connect(1,4,3.9);
        nodeList = graphAlgo.shortestPath(6, 1);
        assertTrue(nodeList.get(0).getKey() == 6);
        assertTrue(nodeList.get(1).getKey() == 4);
        assertTrue(nodeList.get(2).getKey() == 1);

    }

    @Test
    void save_load() {
        weighted_graph graph1 = new WGraph_DS();
        weighted_graph graph2 = new WGraph_DS();
        weighted_graph_algorithms graphAlgo = new WGraph_Algo();

        for(int i  =0; i < 10; i++) {
            graph1.addNode(i);
            graph2.addNode(i);
        }

        for(int i = 0 ; i <30 ; i++){
            int x = (int) ((Math.random()*10) +1);
            int y = (int) ((Math.random()*10) +1);
            double w = Math.random();
            graph1.connect(x,y,w);
            graph2.connect(x,y,w);
        }
        graphAlgo.init(graph1);
        String str = "g0.obj";
        graphAlgo.save(str);
        graphAlgo.load(str);
        assertEquals(graph2,graph1);
        graph1.removeNode(0);
        assertNotEquals(graph1,graph2);

    }

    private weighted_graph small_graph() {
        WGraph_DS graph = new WGraph_DS();

        graph.addNode(1);
        graph.addNode(2);
        graph.addNode(3);
        graph.addNode(4);
        graph.addNode(5);
        graph.addNode(6);
        graph.addNode(7);
        graph.addNode(8);

        graph.connect(3, 7,1.7);
        graph.connect(3, 5,1.2);
        graph.connect(3, 1,3.4);
        graph.connect(7, 6,2.7);
        graph.connect(7, 5,2.1);
        graph.connect(5, 4,4.6);
        graph.connect(5, 1,1.8);
        graph.connect(1, 4,3.9);
        graph.connect(1, 2,4.3);
        graph.connect(6, 4,2.7);

        return graph;
    }
}
