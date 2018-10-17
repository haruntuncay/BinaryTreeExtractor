package binaryTree;


import java.awt.*;

public class Node{

    private String value;
    private Node left;
    private Node right;
    private Node parent;
    private Point point = new Point();

    public Node(String value, Node left, Node right){
        this.value = value;
        this.left = left;
        this.right = right;
    }

    public Node(String value){
        this.value = value;
    }

    public Node(String value, Node parent){
        this.value = value;
        this.parent = parent;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Node getLeft() {
        return left;
    }

    public void setLeft(Node left) {
        this.left = left;
    }

    public Node getRight() {
        return right;
    }

    public void setRight(Node right) {
        this.right = right;
    }

    public Node getParent() {
        return parent;
    }

    public void setParent(Node parent) {
        this.parent = parent;
    }

    public void setPoint(int x, int y){
        point.setLocation(x, y);
    }

    public int getX(){
        return point.x;
    }

    public void setX(int x){
        point.setLocation(x, point.getY());
    }

    public int getY(){
        return point.y;
    }

    public void setY(int y){
        point.setLocation(point.getX(), y);
    }


    @Override
    public String toString(){
        return value;
    }

    @Override
    public boolean equals(Object other){
        Node node = (Node) other;
        return value.compareTo(node.getValue()) == 0;
    }
}
