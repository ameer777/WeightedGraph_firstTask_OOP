package ex1.src;

import java.io.*;
import java.util.*;

public class WGraph_Algo implements weighted_graph_algorithms {

    weighted_graph graphAlgo = new WGraph_DS();

    @Override
    public void init(weighted_graph g) {

        this.graphAlgo = g;//update my graphalgo to work on g.
    }

    @Override
    public weighted_graph getGraph() {

        if(this.graphAlgo != null) return this.graphAlgo;
        return null;
    }

    @Override
    public weighted_graph copy() {
        if(graphAlgo != null) {  //Copy the graph only if it is not null
            WGraph_DS copyGraph = new WGraph_DS();// Define a new graph that will be the copy at the end

            Collection <node_info> c =  graphAlgo.getV();
            Iterator <node_info> l = c.iterator();

            /* in the first while : Define a new node and give it all the values
            of the node that belongs to the original graph */
            while(l.hasNext()) {
                node_info mynode = l.next();
                copyGraph.addNode(mynode.getKey());// Insert the new node into the new graph
                copyGraph.getNode(mynode.getKey()).setInfo(mynode.getInfo());
                copyGraph.getNode(mynode.getKey()).setTag(mynode.getTag());

                Collection <node_info> cc =  graphAlgo.getV(mynode.getKey());
                Iterator <node_info> l2 = cc.iterator();

                /* int the second while : Go through another loop to add edges in the new graph (copy) ,
                 * go on the list neibourts of the node and connect the nodes in copygraph */
                while(l2.hasNext()) {
                    node_info mynode2 = l2.next();
                    copyGraph.addNode(mynode2.getKey());
                    copyGraph.getNode(mynode2.getKey()).setInfo(mynode2.getInfo());
                    copyGraph.getNode(mynode2.getKey()).setTag(mynode2.getTag());
                    copyGraph.connect(mynode.getKey(),mynode2.getKey(),graphAlgo.getEdge(mynode.getKey(),mynode2.getKey()));

                }
            }

            return copyGraph;

        }
        return null;
    }

    // bfs function :
    public void bfs(WGraph_DS g , node_info n) {

        if(n != null && g != null) {
            Collection <node_info> c =  g.getV();
            Iterator <node_info> j = c.iterator();
            /* Go through all the nodes in the graph and paint them "white" and gives them tag = 0 at the first */
            while(j.hasNext()) {
                node_info nodeS = j.next();
                if(nodeS != null) {
                    nodeS.setInfo("white");
                    nodeS.setTag(0);
                }

            }
            // Paint the node I get in gray .
            g.getNode(n.getKey()).setInfo("gray");

            Queue<node_info> queue = new LinkedList<node_info>();
            queue.add(n);//add to the queue my node

            //Run in the while while queue is not empty
            while(!queue.isEmpty()) {
                 /* In each iteration,
                 pulls out the  we are working on is removed from the queue and kept at a new node */
                node_info u = queue.remove();
                Collection <node_info> neighbours = this.graphAlgo.getV(u.getKey());
                Iterator <node_info> i = neighbours.iterator();

                /* Going through the nodes: by the way we will get a node in white or gray,
                 if the color is white: paint it gray to know that we passed an insult at least once
                  + give him dad's weight and another side weight between the node and his dad
                  + add him to the queue. Option two - get a node in gray: it means we passed this node earlier,
                   so let's see if we have a shorter path to update the weights, if we found then let's update,
                    otherwise we do nothing (because we are on the shortest path) */
                while(i.hasNext()) {
                    node_info index = i.next();
                    if(index.getInfo() == "white") {
                        index.setInfo("gray");
                        index.setTag(u.getTag() + g.getEdge(u.getKey(), index.getKey()));
                        queue.add(index);
                    }
                    else if(index.getInfo() == "gray"){
                        if(index.getTag() > u.getTag() + g.getEdge(u.getKey() , index.getKey()))
                            index.setTag(u.getTag() + g.getEdge(u.getKey() , index.getKey()));
                    }
                }
                //And at the end the father paints in black so as not to go over it again
                if(u != null && this.graphAlgo.getV(u.getKey()).size() != 0)
                    g.getNode(u.getKey()).setInfo("black");

            }
        }
        return;
    }

    @Override
    public boolean isConnected() {
        if(this.graphAlgo.nodeSize() == 0) return true;// if there is no nodes in the graph then is connected
        if(this.graphAlgo.nodeSize() == 1) return true;// If there is one node in the graph then is connected
        if(this.graphAlgo == null) return false;

        Collection <node_info> c =  this.graphAlgo.getV();
        Iterator <node_info> j = c.iterator();

        /* Configure iterator to send proper node to bfs (proper node means no null node) */
        node_info n1 = j.next();

        bfs((WGraph_DS) this.graphAlgo, this.graphAlgo.getNode(n1.getKey()));

        Iterator <node_info> i = c.iterator();

        /* After we call bfs each node has a color graph, according to the bfs we get at the end:
        -Nodes have black color in the graph: We have passed through them and have neighbors.
        - White nodes in the graph: Nodes that are in the graph but we could not get past them
        because they are not connected to any node (they have no edge between them).
        So if all the nodes in the graph are black it means
        that the graph is connected and we were able to reach each node,
         and if there is even one node in white it means that we could not reach and this node and
         the graph are not connected */
        while(i.hasNext()) {
            node_info node = i.next();
            if(node!= null)
                if(node.getInfo() != "black") return false;
        }
        return true;
    }

    @Override
    public double shortestPathDist(int src, int dest) {
        /* After calling bfs we will have a tag for each node, just put it back here */
        node_info node1 = this.graphAlgo.getNode(src);
        node_info node2 = this.graphAlgo.getNode(dest);
        if(node1 != null&& node2 != null) bfs((WGraph_DS) this.graphAlgo, node1);
        if(node1 != null &&node2 != null && node2.getTag() != 0) return node2.getTag();

        return-1;// if there is no edge between src , dest return -1
    }

    @Override
    public List<node_info> shortestPath(int src, int dest) {
        double shortest = shortestPathDist(src, dest);//Keep the shortest path length

        if (shortest != -1 && this.graphAlgo.getNode(src) != null && this.graphAlgo.getNode(dest) != null) {

        if(this.graphAlgo.getV(dest) == null || this.graphAlgo.getV(src) == null) return null;

            Queue<node_info> queue = new LinkedList<node_info>();
            queue.add(this.graphAlgo.getNode(dest));//At first add a dist to the queue
            //Define a node and call it in parent to be the father of father's neighbors
            node_info parent = this.graphAlgo.getNode(dest);//

            //Set up a stack to keep the nodes of the shortest path in it
            Stack<node_info> swapQ = new Stack<node_info>();
            while (!queue.isEmpty()) {

                swapQ.add(queue.remove());// at the first we add the dest , after that we add the nodes of the shortest path..

                if (parent == null) break;// if we in the source node then break (We arrived to the gol)
                //Run on the list of neighbors to check with which node to continue on the path to the source
                Collection<node_info> path = this.graphAlgo.getV(parent.getKey());
                Iterator<node_info> i = path.iterator();

                /*We decide with whom to proceed through a comparator: an employee decides with whom to proceed from the
                nodes by weight (taking the shortest weight) , Put the options we have (list of neighbors) in the list and
                 then save it in the list and send the list to the comprator so that he can decide and give us back
                  with whom we should continue.*/
                ArrayList<node_info> array = new ArrayList<node_info>();
                while (i.hasNext()) {
                    node_info node = i.next();
                    //Take only the neighbors on the return route (to save a lot of cases)
                    if (parent.getTag() == this.graphAlgo.getEdge(parent.getKey(), node.getKey()) + node.getTag()) {
                        array.add(node);// add the nodes to the list
                    }
                }

                if (parent.getKey() != src) {
                    parent = compartor(array);//Now Dad is the node that has the shortest path to the source from her
                    queue.add(parent);//add my dad to the queue becouse it's from the shortest path and we have to save it
                }
            }
            /*to flip the shortest path : dest -> source to source -> dest As requested */
            Stack<node_info> swapcopyQ = new Stack<node_info>();
            while (!swapQ.isEmpty())
                swapcopyQ.add(swapQ.pop());

            if(swapcopyQ != null && swapcopyQ.size() > 0)//return the stack if its not null and his size > 0
            return swapcopyQ;
        }
        return null;
    }

    /* The comparator (which I built myself) as an auxiliary function,
     will return to us the node that has its trajectory from it to the shortest weight source */
    public node_info compartor(List<node_info> arr) {

        //if my nei arr is not null
        if (arr != null && arr.size() > 0) {
            node_info node = arr.get(0);//at the first my tag node  is 0
            double t = arr.get(0).getTag();// the weight 0

            for (int i = 0; i < arr.size(); i++) {
                if (arr.get(i).getTag() < t) {// if we found weight < t --> my node weight(t) = weight , my node ..
                    t = arr.get(i).getTag();
                    node = arr.get(i);
                }
            }
            return node;
        }
        return null;
    }

    /**
     * Saves this weighted (undirected) graph to the given
     * file name
     * @param - the file name (may include a relative path).
     * @return true - iff the file was successfully saved
     */
    @Override
    public boolean save(String file_name) {
        try {

            FileOutputStream file = new FileOutputStream(file_name);
            ObjectOutputStream out = new ObjectOutputStream(file);

            out.writeObject(graphAlgo);
            out.close();
            file.close();

            System.out.println("Object has been serialized");
            return true;
        }
        catch (FileNotFoundException ex) {
            ex.printStackTrace();
        }
        catch (IOException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    /**
     * This method load a graph to this graph algorithm.
     * if the file was successfully loaded - the underlying graph
     * of this class will be changed (to the loaded one), in case the
     * graph was not loaded the original graph should remain "as is".
     * @param  - file name
     * @return true - iff the graph was successfully loaded.
     */
    @Override
    public boolean load(String file_name) {

        try {
            FileInputStream file = new FileInputStream(file_name);
            ObjectInputStream in = new ObjectInputStream(file);

            graphAlgo = (weighted_graph) in.readObject();
            in.close();
            file.close();

            System.out.println("Object has been deserialized");
            return true;
        }
        catch (FileNotFoundException ex) {
            ex.printStackTrace();
        }
        catch (IOException ex) {
            ex.printStackTrace();
        }
        catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        }
        return false;
    }
}