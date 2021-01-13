import java.util.*; 



public class EightPuzzle
{ 
   
   public static void main(String args[])
   {
      
      Scanner scan = new Scanner(System.in); 
      
      byte[] game = {5,2,6,0,4,1,7,8,3};
      //int[] game = {1, 2, 0, 3};  
      byte[] goal = {1,2,3,4,5,6,7,8,0}; 
      
     
      String goalString = "";
      
      /*
      System.out.println("Enter the " + game.length + " numbers of goal state with spaces");
      byte[] goal = new byte[game.length];*/
      for(int i = 0; i < game.length; i++)
      {
      //goal[i] = scan.nextByte();
         goalString += i; 
      }
      
      
      long start = System.currentTimeMillis();     
      puzzleSolver test = new puzzleSolver(game, goal);
      
      long end = System.currentTimeMillis();
      
      System.out.println(test.explored.size()); 
      System.out.println(test.explored.contains(goalString));
      
      System.out.println(end - start);
           
   }
   
}  
class puzzleSolver
{
   
   public int sideLength; 
   public byte[] goalArray;
   public String goalString; 
   public HashSet<String> explored = new HashSet<>(); 
  
   
   
   public puzzleSolver(byte[] game, byte[] goalArr)
   {
      sideLength = (int) java.lang.Math.sqrt((double) game.length);
      goalArray = new byte[game.length];
      goalArray = goalArr;
      System.out.println(solvable(game));
      /*for(int i = 1; i < (sideLength * sideLength); i++)
      {
         goalArray[i-1] = i; 
          
      }
      goalArray[game.length - 1] = 0; 
      */
      
      Node root = new Node(game); 
      explored.add(root.toString()); 
       
      
      System.out.println(pathCreator(root)); 
      
   /*
   Setting the original board to the "root" node. Then will be able to create the children.
   Path creator will print the moves in the matrix
   */
   
   }
   public boolean solvable(byte[] start) {
      int inversions = 0;
      byte ind = (byte)getArrayIndex(start, (byte)0);
      
      int n = (int) Math.sqrt(start.length);
      
      for (int i = 0; i < start.length - 1; i++) {
      
         for (int k = i+1; k < start.length; k++) {
         
            if (start[i] > start[k] && start[k] != 0 && start[i] != 0)
               inversions++;
               
         }
         
      }
      if (n % 2 == 1) {
      
         if (inversions % 2 == 0)
            return true;
         else
            return false;
            
      }
      else {
         if (((start.length - ind - 1) / n) % 2 == 0 && inversions % 2 == 1)
            return true;
         else if (((start.length - ind - 1) / n) % 2 == 1 && inversions % 2 == 0)
            return true;
         else
            return false;
            
      }
   }
   public String pathCreator(Node n) throws NullPointerException
   {
      Node current = makeChildren(n);
      /*
      
      Make children method is enacted to construct the tree
      make children method returns the first Node that represents the goalNode
      
      */
      
      if(current != null)
      {
         ArrayList<String> path = new ArrayList<>();
         path.add(current.toString());
      
         String s = "";
         String totalPath = "";
      
         while(current.hasParent())
         {  
           
            path.add(current.getParent().toString());
            current = current.getParent();
            
         // creates path of strings (toString from Node class)       
         }
      
         for(int i = path.size() - 1; i >= 0; i--)
         {
            String state = path.get(i); 
         
            s = "";
            for(int x = 0; x < sideLength; x++)
            {
               s = "|" + state.substring(sideLength * (x), sideLength * (x + 1)) + "|" + "\n";
               totalPath += s;
            }
            totalPath += "\n";
         
         // formatting strings to look like a matrix
         }
      
         System.out.println(path.size() - 1 + " Moves\n");
         return totalPath;
      }
      return "UNSOLVABLE\n";
      
      //returns if the original board is unsolvable (makeChildren returns null)
   }

   public Node makeChildren(Node currentNode)
   {
      LinkedList<Node> current = new LinkedList<>(); 
      current.addFirst(currentNode); 
      
      byte[] state;
      int i;     
      Node goalNode = null;     
      boolean found = false; 
   
      do{
      
         state = current.get(0).getData();
         i = getArrayIndex(state, 0); 
         
         
         if(goal_Test(state) && !found)
         {
         
         //checks if current node is the goal state
         
            goalNode = current.get(0); 
            found = true; 
         }
        
         if(i == 0 || i == sideLength - 1 || i == ((sideLength * sideLength) - sideLength) || i == goalArray.length - 1)
         {
         //checks if the empty space is in the corner
         
            currentNode = current.pop(); 
            //pops top of queue
            state = currentNode.getData();
            i = getArrayIndex(state, 0); 
            
            currentNode.addChildren(corner(currentNode, i));
            //activates corner method which returns the children of the block
            
            for(Node n1 : currentNode.getChildren())
            {
            //adds all of the children to the "explored set" to avoid duplicates
            //also adds the new board states to the queue so that their children can be explored 
               if(explored.add(n1.toString()))
               {
                  current.add(n1); 
               }
            }
                        
         
         }
         else if(i % sideLength == 0 || i / sideLength == 0 || (i + sideLength) > (sideLength * sideLength) - 1 || (i + 1) % sideLength == 0 ) 
         {
         
         //runs this code if the empty space is on a side (not corner or any middle piece)
         
            currentNode = current.pop(); 
            state = currentNode.getData();
            i = getArrayIndex(state, 0); 
            
            if(i < 1)
            {
            
               currentNode.addChildren(corner(currentNode, i));
            
            }
            if(i == goalArray.length - 1)
            {
            
               currentNode.addChildren(corner(currentNode, i));
            
            }
            else{
               currentNode.addChildren(side(currentNode, i));
            }
            for(Node n1 : currentNode.getChildren())
            {
               if(explored.add(n1.toString()))
               {
                  current.add(n1);
               }
            }
            
         
         }
         else
         {
            currentNode = current.pop(); 
            state = currentNode.getData();
            i = getArrayIndex(state, 0);
         
            currentNode.addChildren(middle(currentNode));
         
            for(Node n1 : currentNode.getChildren())
            {
               if(explored.add(n1.toString()))
               {
                  current.add(n1); 
               }
            }
         
         
         }
          
      }while(current.size() > 0 && found == false);
      //continues to loop until the queue is empty or until it finds the goal
      
      return goalNode; 
      //return the Node that contains the goal state. If it is unsolvable this will return null
      
   }
   public ArrayList<Node> corner(Node n, int i)
   {
   
      ArrayList<Node> nodes = new ArrayList<Node>(); 
      
      if(i == ((sideLength * sideLength) - sideLength))
      {
         Node nodeUp = new Node(up(n.getData(), i), n); 
         nodes.add(nodeUp); 
      
         // adds a node for the child of the input node to a ArrayList of nodes
               
         Node nodeRight = new Node(right(n.getData(), i), n); 
         nodes.add(nodeRight);
      
      }
      else if(i == (sideLength - 1))
      {
      
         Node nodeUp = new Node(down(n.getData(), i), n); 
         nodes.add(nodeUp); 
        
        // adds a node for the child of the input node to a ArrayList of nodes
         Node nodeLeft = new Node(left(n.getData(), i), n); 
         nodes.add(nodeLeft);
      }
      else if(i == 0)
      {
         Node nodeDown = new Node(down(n.getData(), i), n);  
         nodes.add(nodeDown);
      
         // adds a node for the child of the input node to a ArrayList of nodes
      
         Node nodeRight = new Node(right(n.getData(), i), n);  
         nodes.add(nodeRight);
      
      }
      else if(i == ((sideLength * sideLength) - 1))
      {
      
         Node nodeLeft = new Node(left(n.getData(), i), n); 
         nodes.add(nodeLeft);
      
         // adds a node for the child of the input node to a ArrayList of nodes
      
         Node nodeUp = new Node(up(n.getData(), i), n); 
         nodes.add(nodeUp); 
      
      }
   
      return nodes;
   }
   public ArrayList<Node> side(Node n, int i)
   {
   
      ArrayList<Node> nodes = new ArrayList<Node>(); 
      
      if(i % sideLength == 0)
      {
      //using try catch to isolate where error keeps occuring// 
         try{
         
            
            Node nodeUp = new Node(up(n.getData(), i), n);  
            nodes.add(nodeUp);  
            
            // adds a node for the child of the input node to a ArrayList of nodes   
              
            Node nodeDown = new Node(down(n.getData(), i), n); 
            nodes.add(nodeDown);
         
            Node nodeRight = new Node(right(n.getData(), i), n); 
            nodes.add(nodeRight);
         }
         catch(IndexOutOfBoundsException e)
         {
            System.out.println("1");
         
         }
      
      }
      else if( i / sideLength == 0)
      {
         try{
         
            Node nodeLeft = new Node(left(n.getData(), i), n); 
            nodes.add(nodeLeft);
         
            // adds a node for the child of the input node to a ArrayList of nodes
            
            Node nodeDown = new Node(down(n.getData(), i), n); 
            nodes.add(nodeDown);
         
            Node nodeRight = new Node(right(n.getData(), i), n);  
            nodes.add(nodeRight);
         }
         catch(IndexOutOfBoundsException e)
         {
         
            System.out.println("2");
         
         }
      
      }
      else if((i + sideLength) > (sideLength * sideLength))
      {
         try{
            Node nodeLeft = new Node(left(n.getData(), i), n); 
            nodes.add(nodeLeft);
            
            // adds a node for the child of the input node to a ArrayList of nodes
            
            Node nodeUp = new Node(up(n.getData(), i), n); 
            nodes.add(nodeUp);
         
            Node nodeRight = new Node(right(n.getData(), i), n); 
            nodes.add(nodeRight);
         }
         catch(IndexOutOfBoundsException e)
         {
         
            System.out.println("3");
         
         }
      }
      else if( (i + 1) % sideLength == 0)
      {
         try{
         
            Node nodeUp = new Node(up(n.getData(), i), n); 
            nodes.add(nodeUp);  
           
            // adds a node for the child of the input node to a ArrayList of nodes
            
            Node nodeDown = new Node(down(n.getData(), i), n); 
            nodes.add(nodeDown);
         
            Node nodeLeft = new Node(left(n.getData(), i), n); 
            nodes.add(nodeLeft);
         }
         catch(IndexOutOfBoundsException e)
         {
         
            System.out.println("4");
         
         }
      }
      
      return nodes;
   
   
   }
   
   public ArrayList<Node> middle(Node n)
   {
   
   
      ArrayList<Node> nodes = new ArrayList<Node>(); 
   
      Node nodeLeft = new Node(left(n.getData(), goalArray.length / 2), n); 
      nodes.add(nodeLeft); 
   
      // adds a node for the child of the input node to a ArrayList of nodes
      
      Node nodeRight = new Node(right(n.getData(), goalArray.length / 2) , n); 
      nodes.add(nodeRight); 
   
      Node nodeUp = new Node(up(n.getData(), goalArray.length / 2) , n);  
      nodes.add(nodeUp); 
   
      Node nodeDown = new Node(down(n.getData(), goalArray.length / 2) , n); 
      nodes.add(nodeDown); 
   
      return nodes;
   
   }
   
   public byte[] left(byte[] board, int zeroIndex){
   
      ArrayList<Integer> swapList = new ArrayList<>();
      byte[] returnBoard = new byte[goalArray.length];
   
      for(byte i : board)
      {
         swapList.add((int) i); 
      }
      
      Collections.swap(swapList, zeroIndex, zeroIndex - 1);
      
      
      return toArray(swapList); 
   
   }
   
   public byte[] right(byte[] board, int zeroIndex){
      
      ArrayList<Integer> swapList = new ArrayList<>();
      byte[] returnBoard = new byte[goalArray.length];
   
       
      for(byte i : board)
      {
         swapList.add((int)i); 
      }
      
      Collections.swap(swapList, zeroIndex, zeroIndex + 1);
      
      return toArray(swapList); 
   
   }
   
   public byte[] up(byte[] board, int zeroIndex){
      
      ArrayList<Integer> swapList = new ArrayList<>();
      byte[] returnBoard = new byte[goalArray.length];
   
       
      for(byte i : board)
      {
         swapList.add((int)i); 
      }
      
      Collections.swap(swapList, zeroIndex, zeroIndex - sideLength);
      
      return toArray(swapList); 
   
   }
   
   public byte[] down(byte[] board, int zeroIndex){
      ArrayList<Integer> swapList = new ArrayList<>();
      byte[] returnBoard = new byte[goalArray.length];
             
      for(byte i : board)
      {
         swapList.add((int)i); 
      }
      
      Collections.swap(swapList, zeroIndex, zeroIndex + sideLength);
      
      return toArray(swapList); 
   
   }
   public boolean goal_Test(byte[] i)
   {
   
      return Arrays.equals(i, goalArray);
   
   }
   
     
   public byte[] toArray(List<Integer> l)
   {
      byte[] returnBoard = new byte[l.size()]; 
   
      for(int i = 0; i < l.size(); i++)
      {
         returnBoard[i] = l.get(i).byteValue(); 
      }
   
      return returnBoard; 
   
   }
   
   
   private int getArrayIndex(byte[] arr,int value) {
   
      int k=0;
      for(int i=0;i<arr.length;i++){
      
         if(arr[i]==value){
            k=i;
            break;
         }
      }
      return k;
   }

}

class Node{

   byte[] data = null; 

   private Node parent = null; 
   private List<Node> children = new ArrayList<>(); 
   
      
   public Node(byte[] data)
   {
      this.data = data;
      
      
   }
   public Node(byte[] data,  Node n)
   {
      this.data = data;
      this.parent = n; 
      
      this.parent = n; 
   }

   
   public byte[] getData()
   {
   
      return data; 
   
   }
   
   
   public void addChild(Node child)
   {
   
      child.setParent(this);
   
      this.children.add(child);
     
   }
   
   public void addChildren(ArrayList<Node> childs)
   {
   
      for(int i = 0; i < childs.size(); i++)
      {
         childs.get(i).setParent(this);
         this.children.add(childs.get(i));
      
      }
   
   }
   
   public void setParent(Node parent) {
      this.parent = parent;
   }
 
   public Node getParent() {
      return parent;
   }
   
   public List<Node> getChildren(){
   
      return children; 
   }
   
   public boolean hasParent(){
   
      if(parent == null)
      {
         return false;
      
      }
   
      return true; 
   }
   
   public String toString()
   {
      String s = "";
      for(int i = 0; i < data.length; i++)
      {
         s += data[i];
      
      }
      return s;
   
   }
   
}
