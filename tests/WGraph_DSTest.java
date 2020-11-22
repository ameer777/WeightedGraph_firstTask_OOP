package ex1.tests;

import ex1.src.WGraph_DS;
import ex1.src.node_info;
import ex1.src.weighted_graph;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.Iterator;

import static org.junit.jupiter.api.Assertions.*;

 class WGraph_DSTest {

     @Test
     void nodeSize() {
         weighted_graph g = new WGraph_DS();

         assertEquals(0,g.nodeSize());

         for(int i = 0; i < 5; i++)
             g.addNode(i);


         for(int i = 0; i < 5; i++)
             g.removeNode(i);

         int s0 =g.nodeSize();
         assertEquals(0,s0);

         for(int i =0 ; i < 150 ; i++)
             g.addNode(i);

         int s1 = g.nodeSize();
         assertEquals(150,s1);

         for(int i = 0; i < 10 ; i++ ){
             g.removeNode(1);
             g.removeNode(56);
             g.removeNode(s1+3);
             g.removeNode(i);
         }
         int s2 = g.nodeSize();
         assertEquals(139,s2);

         for(int i = s1 ; i < s1 + 11 ; i++)
            g.removeNode(i);

         int s3 = g.nodeSize();
         assertEquals(139,s3);

         for (int i = -1 ; i > -10 ; i--)
             g.removeNode(i);

         int s4 = g.nodeSize();
         assertEquals(139,s4);

     }

     @Test
     void edgeSize() {
         weighted_graph g = new WGraph_DS();

         for(int i = 0; i < 500; i++)
             g.addNode(i);

         for(int i = 0; i < 250 ; i++) {
         int x = i;
         int y = x + 5;
         double w = (double) (Math.random()*10);
         g.connect(x,y,w);
         }

         int e_size0 = g.edgeSize();
         assertEquals(250,e_size0);

         int counter = 0;
         for(int i = 0; i < 250 ; i++) {
             int x = (int) (Math.random() * 250);
             int y = (int) (Math.random() * 250);
             if(g.hasEdge(x,y)) {
                 g.removeEdge(x, y);
                 g.removeEdge(y, x);
                 g.removeEdge(x,x);
                 g.removeEdge(y,y);
                 counter++;
             }
         }

         int e_size1 = g.edgeSize();
         assertEquals(e_size0 - counter,e_size1);

         for(int i = 0 ; i < 50 ; i++){
             int x = (int) (Math.random() * 499);
             int y = (int) (Math.random() * 499);
             double wxy= g.getEdge(x,y);
             double wyx = g.getEdge(y,x);
             assertEquals(wxy,wyx);
         }

     }

     @Test
     void getV() {
         weighted_graph g = new WGraph_DS();

         for(int i = 0 ; i < 500 ; i++)
             g.addNode(i);

         Collection<node_info> col = g.getV();
         Iterator<node_info> it = col.iterator();
         while (it.hasNext()){
             node_info n = it.next();
             assertNotNull(n);
         }

         for(int i = 0 ;i < 30;i++){
             int x = 0;
             int y = i+1;
             double w = (Math.random()*10);
             g.connect(x,y,w);

         }

         Collection<node_info> colnode2 = g.getV(400);
         Collection<node_info> colnode = g.getV(0);
         assertEquals(30,colnode.size());
         assertEquals(30,colnode.size());

         for(int i = 0; i < 500 ; i++)
             g.removeNode(i);

         Collection<node_info> colnull = g.getV();
         assertEquals(0,colnull.size());

     }

     @Test
     void hasEdge() {
         weighted_graph g1 = new WGraph_DS();

         for(int i = 0 ; i < 500 ; i++)
             g1.addNode(i);

         for(int i = 0; i < g1.nodeSize(); i++) {
             int x = (int) (Math.random() * 499);
             int y = (int) (Math.random() * 499);
             assertFalse(g1.hasEdge(x,y));
             assertFalse(g1.hasEdge(y,x));
         }

         for(int i = 0; i < g1.nodeSize(); i++) {
             int x = (int) (Math.random() * 499);
             int y = (int) (Math.random() * 499);
             double w = (Math.random() * 10);
             g1.connect(x, y, w);
             boolean flag = g1.hasEdge(y, x);
             if (x!=y){
             assertTrue(flag);
             assertTrue(g1.hasEdge(x, y));
             }
             g1.removeEdge(x, y);
             g1.removeEdge(y, x);
             g1.removeEdge(y, x);
             g1.removeEdge(x, y);
             assertFalse(g1.hasEdge(x, y));
             assertFalse(g1.hasEdge(y, x));

         }
         weighted_graph g2 = new WGraph_DS();

         for(int i = 0 ; i < 500 ; i++)
             g2.addNode(i);

         for(int i = 0; i < 250 ; i++) {
             int x = i;
             int y = x + 1;
             double w = (double) (Math.random()*10);
             g2.connect(x,y,w);
         }

         for(int i = 0 ; i < 200 ; i++){
             int x = (int) (Math.random()*500);
             node_info node = g2.getNode(x);
             if(node !=null) {
                 Collection<node_info> col = g2.getV(node.getKey());
                 Iterator<node_info> ite = col.iterator();
                 g2.removeNode(x);
                 while (ite.hasNext()) {
                     node_info n = ite.next();
                     assertEquals(false, g2.hasEdge(n.getKey(), node.getKey()));
                 }
             }
         }

     }

     @Test
     void connect() {
         weighted_graph g = new WGraph_DS();

         for(int i = 0; i < 500 ; i++)
             g.addNode(i);

         for(int i = 0 ; i < 20 ; i++){
             int x = (int) (Math.random() * 499);
             int y = (int) (Math.random() * 499);
             double w = (Math.random() * 10);
             if(x!=y){
                 g.connect(x,y,w);
                 g.connect(x,y,w);
                 assertTrue(g.hasEdge(x,y));
                 assertTrue(g.hasEdge(y,x));
                 assertFalse(g.hasEdge(y,y));
                 assertFalse(g.hasEdge(x,x));
                 assertEquals(g.getEdge(x,y),w);
                 g.removeEdge(x,y);
                 g.removeEdge(x,y);
                 assertFalse(g.hasEdge(x,y));
                 assertFalse(g.hasEdge(y,x));
             }
             else i--;
         }
     }

     @Test
     void removeNode() {
         weighted_graph g = new WGraph_DS();

         for(int i = 0; i < 500; i++)
             g.addNode(i);

         int s0 = g.nodeSize();
         int e0 = g.edgeSize();

         for(int i = 0; i < 20; i++){
          g.connect(i,i+1,i);
          g.removeNode(i+500);
          g.removeNode(i);
          assertFalse(g.hasEdge(i,i+1));
         }

         for(int i = 0; i < 50; i++)
             g.removeNode(i);

         assertEquals(450,s0-50);
         assertEquals(0,e0);
     }

     @Test
     void removeEdge() {
         weighted_graph g = new WGraph_DS();

         for(int i = 0; i < 500 ; i++)
             g.addNode(i);

         for(int i = 0 ; i < 50 ; i++){
             g.connect(i,i+2,i+1);
             g.connect(i+1,i+5,i+4);
             double w = g.getEdge(i+1,i+5);
             assertEquals(w,i+4);
             g.removeEdge(i+1,i+5);
             g.removeEdge(i+1,i+5);
             w = g.getEdge(i+1,i+5);
             assertEquals(w,-1);
         }

         for(int i = 0; i < 100; i++){
             int x = (int) (Math.random() * 499);
             g.removeNode(i+2);
             g.removeNode(i+5);
             g.removeNode(i+4);
             g.removeNode(i+4);
             assertFalse(g.hasEdge(i+2,x));
             assertFalse(g.hasEdge(i+5,x));
             assertFalse(g.hasEdge(i+4,x));
         }

         assertEquals(0,g.edgeSize());
     }
}
