package binaryTree;

import javafx.scene.canvas.GraphicsContext;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Stack;

public class BinaryTree{

    private Node root;

    public BinaryTree(){}

    public BinaryTree(String val){
        this.root = new Node(val);
    }

    public BinaryTree(Node root){
        this.root = root;
    }

    public Node getRoot(){
        return root;
    }

    /*
        Insert metodu, normalden farklı olarak bir Node'un parent'ınıda set eder.
     */
    public boolean insert(String val){
        if(root == null){
            root = new Node(val);
            return true;
        }

        Node current = root;
        Node parent = current;
        boolean isLeftChild = false;

        while(current != null){
            parent = current;
            int comparison = current.getValue().compareTo(val);

            if(comparison < 0){
                current = current.getRight();
                isLeftChild = false;
            }
            else if(comparison > 0){
                current = current.getLeft();
                isLeftChild = true;
            }
            else    //Node with the "val" exists.Don't allow duplicates
                return false;
        }

        if(isLeftChild)
            parent.setLeft(new Node(val, parent));
        else
            parent.setRight(new Node(val, parent));

        return true;
    }

    public void preorder(){
        preorder(root);
    }

    public void preorder(Node node){
        if(node == null)
            return;

        System.out.print(node + " ");
        preorder(node.getLeft());
        preorder(node.getRight());
    }

    public void displayTree()
    {
        Stack<Node> globalStack = new Stack<>();
        globalStack.push(root);
        int nBlanks = 32;
        boolean isRowEmpty = false;
        System.out.println(
                "......................................................");
        while(!isRowEmpty)
        {
            Stack<Node> localStack = new Stack<>();
            isRowEmpty = true;

            for(int j=0; j<nBlanks; j++)
                System.out.print(' ');

            while(!globalStack.isEmpty())
            {
                Node temp = globalStack.pop();
                if(temp != null) {
                    System.out.print(temp.getValue());
                    localStack.push(temp.getLeft());
                    localStack.push(temp.getRight());

                    if(temp.getLeft() != null ||
                            temp.getRight() != null)
                        isRowEmpty = false;
                } else {
                    System.out.print("--");
                    localStack.push(null);
                    localStack.push(null);
                }

                for(int j=0; j<nBlanks*2-2; j++)
                    System.out.print(' ');
            }  // end while globalStack not empty

            System.out.println();
            nBlanks /= 2;
            while(!localStack.isEmpty())
                globalStack.push( localStack.pop() );

        }  // end while isRowEmpty is false
        System.out.println(
                "......................................................");
    }  // end displayTree()

}
