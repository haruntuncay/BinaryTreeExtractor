package app;

import binaryTree.BinaryTree;
import binaryTree.BinaryTreeExporter;
import binaryTree.BinaryTreeParser;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Main {

    public static void main(String[] args) {
        //daa(baa(caa)(aaa))(faa(kaa)(eaa))
        BinaryTree tree = BinaryTreeParser.treeFromFormatString(getInput("BinaryTree'nizi formatlı bicimde giriniz:"));
        BinaryTreeExporter.export(tree, BinaryTreeExporter.FILE_TYPE.PNG);
        tree.displayTree();
        System.exit(0);
    }

    private static String getInput(String message){
        System.out.println(message);
        String input;

        try(BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))){
            input = reader.readLine();
        }catch(IOException ex){
            System.out.println("Bir hata oluştu.\r\nException: " + ex);
            System.out.println("Varsayılan input string'i kullanılacak.");
            input = "daa(baa(caa)(aaa))(faa(kaa)(eaa))";
        }

        return input;
    }
}
