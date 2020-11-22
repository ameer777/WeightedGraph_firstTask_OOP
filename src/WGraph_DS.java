package ex1.src;

import java.util.*;
import java.io.Serializable;

public class WGraph_DS implements weighted_graph,Serializable{

    private Hashtable<Integer, node_info> myGraph;
    private int mc = 0; // Some changes we made graph = mc.  At first 0
    private int edgeCount = 0; /* How many edges do I have in the graph = edgecount . At first 0 */
    //constructor
    public WGraph_DS(){
        this.myGraph = new Hashtable<Integer,node_info>();
    }

    @Override
    public node_info getNode(int key) {
        /* Return the node only if it is in the graph and it is not null */
        if(this.myGraph.get(key) != null && this.myGraph.containsKey(key) == true)
            return this.myGraph.get(key);

        return null;
    }

    @Override
    public boolean equals(Object o) {
        if (this.myGraph == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WGraph_DS wGraph_ds = (WGraph_DS) o;
        return mc == wGraph_ds.mc &&
                edgeCount == wGraph_ds.edgeCount && nodeSize() == wGraph_ds.nodeSize()&&
                Objects.equals(myGraph.getClass(),wGraph_ds.myGraph.getClass());
    }

    @Override
    public boolean hasEdge(int node1, int node2) {
        /* Starting to check if there is a edge between two nodes only if both are in our graph */
        if(this.myGraph.containsKey(node1) == true && this.myGraph.containsKey(node2) == true){

            NodeInfo n1 = (NodeInfo) this.myGraph.get(node1);
            NodeInfo n2 = (NodeInfo) this.myGraph.get(node2);
            /* If the two nodes are not null and one of them is a neighbor of the other return true */
            if(n1 != null && n2 != null){
                if(n1.hasNi(n2.getKey())) return true;

                return false;
            }

            return false;
        }
        return false;
    }

    @Override
    public double getEdge(int node1, int node2) {

        //if my nodes is not null .
        if(this.myGraph.containsKey(node1) == true && this.myGraph.containsKey(node2) == true){

            NodeInfo n1 = (NodeInfo) this.myGraph.get(node1);
            NodeInfo n2 = (NodeInfo) this.myGraph.get(node2);

            if(hasEdge(node1 , node2) == true){
                return n1.edge.get(n2.getKey()); // return my weight
            }
            return -1;// if there is not edge between the nodes then there is not weight,then return -1.
        }
        return -1;//if my graph is not contains my node then return -1.
    }

    @Override
    public void addNode(int key) {
        /* Add a node to our graph only if it is not here earlier (Checking if it is not here in advance) */
        if(!this.myGraph.containsKey(key)){
            node_info node = new NodeInfo(key);
            this.myGraph.put(key , node);
            ++this.mc;/*Increase the number of changes by one*/
        }

    }

    @Override
    public void connect(int node1, int node2, double w) {
        /* Link two nodes in the graph only if both are in the graph,
         there is really no edge between them, we are not in the same node (node1 != node2) */
        if(this.myGraph.containsKey(node1) && this.myGraph.containsKey(node2) && node1 != node2 && w >= 0){
            NodeInfo node_1 = (NodeInfo) this.myGraph.get(node1);
            NodeInfo node_2 = (NodeInfo) this.myGraph.get(node2);
             /* If the two nodes are not empty then no more problems, add each to the list of neighbors of the other,
             do two-way add  because it is an offline graph so there are no arrows and lines in certain directions
             between each node*/
            if(node_1 != null && node_2 != null && hasEdge(node1,node2) == false){
                node_1.addNi(node_2);
                node_2.addNi(node_1);

                ++this.edgeCount;// Increase edgeSize
            }
            //Anyway (if there is a edge between the nodes or not always update the weight) , then update is outside the if..
            node_1.edge.put(node_2.getKey() , w);
            node_2.edge.put(node_1.getKey() , w);
            ++this.mc;// Increase changes number , update,connect+update : is change.
        }
    }

    @Override
    public Collection<node_info> getV() {
        /* Return the list of the graph's node collection only if it is not null */
        if(this.myGraph != null)
            return this.myGraph.values();

        return null;
    }

    @Override
    public Collection<node_info> getV(int node_id) {
        /* Returns the list of the node collection of nodes of the key that are accepted only if it is not null */
        if(this.myGraph.containsKey(node_id)) {
            NodeInfo node = (NodeInfo) this.myGraph.get(node_id);
            ArrayList<node_info> arr = new ArrayList<node_info>();
            Collection<node_info> myList = node.getNi();
            Iterator<node_info> i = myList.iterator();

            while (i.hasNext()){
                node_info n = i.next();
                arr.add(n);
            }

            return arr;

        }
        return null;
    }

    @Override
    public node_info removeNode(int key) {
        /* Delete a node only if it is not null and it is also in our graph */
        if(this.myGraph.get(key) != null && this.myGraph.containsKey(key)){
            ArrayList<node_info> arr = new ArrayList<>();

            NodeInfo node_remove = (NodeInfo) this.myGraph.get(key);//The node we delete
            /* Go through the node list of neighbors to delete his edges first */
            Collection<node_info> myList = getV(key);
            Iterator<node_info> i = myList.iterator();

            /* Need to lower the edges of the node from his neighbors,
             it is not possible to lower inside the iterator because such an action hurts his pointer therefore:
             Keeps the nodes we want to lower the edges between them within an array we defined earlier,
             Then go over the array outside the iterator (while) and
             delete all the edges between the node that we want to delete */
            while(i.hasNext()){
                node_info nodeI = i.next();
                arr.add(nodeI);
            }

            /* Here we go through an array in which we kept the edges that need to be lowered and lower them */
            for(int j= 0 ; j < arr.size() ; j++) {
                removeEdge(arr.get(j).getKey(), key);
            }

            /* And now delete the node from the graph */
            this.myGraph.remove(key);
            ++this.mc;// increase changes counter
            return node_remove;// return the node deleted

        }
        return null;
    }

    @Override
    public void removeEdge(int node1, int node2) {
        //Delete a edge only if it exists
        if(hasEdge(node1, node2) == true) {
            NodeInfo node_1 = (NodeInfo) this.myGraph.get(node1);
            NodeInfo node_2 = (NodeInfo) this.myGraph.get(node2);

            /* If the two nodes that have a side are not empty,
             therefore lower the side: two-way erasure (as in connect) */
            if(node_2 != null && node_1 != null) {
                node_1.removeNode(node_2);
                node_2.removeNode(node_1);

                --edgeCount;
                ++this.mc;
            }
        }

    }

    @Override
    public int nodeSize() {
        return this.myGraph.size();
    }

    @Override
    public int edgeSize() {

        return this.edgeCount;
    }

    @Override
    public int getMC() {
        return this.mc;
    }

    private static class NodeInfo implements node_info,Serializable {

        private Hashtable<Integer,Double> edge;// edge weight,Weight: Identify it via "INTEGER" and value = double.
        private Hashtable<Integer,node_info> list;// Neighbors list
        private int key ;// my id
        private double tag;//my weight
        private String info;// my color

        //constructor :
        public NodeInfo(int k){

            this.edge = new Hashtable<Integer,Double>();
            this.list = new Hashtable<Integer, node_info>();
            this.key = k;
            this.tag = -1;//Initially 0 to match with the algorithm I implemented later
            this.info = "white";//At first all the nodes are white (to match with the bfs algorithm)
        }

        //list.values is collection
        public Collection<node_info> getNi() {

            return list.values();// List of neighbors list

        }

        public boolean hasNi(int key) {
            /*Does the list of neighbors containing the node belong to the key they receive?
            If so return true . else return false*/
            if(this.list.containsKey(key) == true) return true;
            return false;
        }

        public void addNi(node_info t) {

            if(t != null && hasNi(t.getKey()) == false && this.key != t.getKey()) {
                this.list.put(t.getKey() , t);
            }
        }

        public void removeNode(node_info node) {

            /*  If the node is not empty and we do not have it in the list of neighbors and it is not the node itself,
             add it to the list of neighbors  */
            if( node != null && this.list.containsKey(node.getKey())) {
                this.list.remove(node.getKey(), node);
            }
        }

        public void setKey(int ke){
            this.key = ke;
        }

        @Override
        public int getKey() {

            return this.key;
        }

        @Override
        public String getInfo() {

            return this.info;// return the color node
        }

        @Override
        public void setInfo(String s) {

            this.info = s;// change the node color
        }

        @Override
        public double getTag()
        {
            return this.tag;// return tag of node
        }

        @Override
        public void setTag(double t) {

            this.tag  = t; // change the tag node
        }

    }

}
