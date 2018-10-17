package binaryTree;

import java.util.Stack;

public class BinaryTreeParser {

    //Helper metod.
    public static BinaryTree treeFromFormatString(String input){
        return new BinaryTree(treeFromFormatString(input.toCharArray(), 0, input.length() - 1, null));
    }

    private static Node treeFromFormatString(char[] charArray, int start, int end, Node parent){
        if(start > end)
            return null;

        //Node objesinin value'su olacak String'in length değerini bul.
        int valueLength = getValueLength(charArray, start);
        //String value ve parentNode değerini atayan constructor.
        Node node = new Node(new String(charArray, start, valueLength), parent);

        //Node'a ait kapalı parantezin indexi.
        int index = -1;

        /*
            char[] sonuna gelmediysek
                ve String value length'i kadar ilerlediğimizde '(' görürsek
                    o açık paranteze ait kapalı parantez indexi arıyoruz.
         */
        if(start + 1 <= end && charArray[start + valueLength] == '('){
            index = findIndex(charArray, start + 1, end);
        }

        /*
            Kapalı parantezin index'i -1 den farklı ise, 2 artırarak diğer Stringin başlangıç indexini buluyoruz.
                Bu index'ten taranan değeri sağ çocuğa atıyoruz.

            Start indexinden ilerleyek bulduğumuz String'in 2 karakter sonrasında başlayan
                yeni String'e ilerliyoruz ve sol çocuk olarak atıyoruz.

            (NOT: start + 2 yerine start + 1 olmasının sebebi
                valueLength'in dizi indexine vurulduğunda 1 ilerden başlamasındandır.)
         */
        if(index != -1){
            node.setLeft(treeFromFormatString(charArray, start + 1 + valueLength, index - 1, node));
            node.setRight(treeFromFormatString(charArray, index + 2, end - 1, node));
        }

        return node;
    }

    /*
        Açık parantez '(' gördükçe stack'e ekler.
        ')' gördükçe stack'ten pop eder.
        Eğer herhangi bir pop anında stack boşalmışsa ilgili kapalı parantez bulunmuş olur
            ve o anki index döndürülür.
     */
    private static int findIndex(char[] str, int start, int end){
        Stack<Character> stack = new Stack<>();
        char c;

        for(int i = start; i <= end; i++){
            c = str[i];
            if(c == '(')
                stack.push(c);
            else if(c == ')'){
                if(stack.peek() == '(') {
                    stack.pop();
                    if(stack.isEmpty()){
                        return i;
                    }
                }
            }

        }

        return -1;
    }

    /*
        Verilen start indexinden başlayarak, '(' veya ')' görene kadar start indexini arttırır.
        Start değişkeninin geçerli değerinden ilk değerini çıkartarak String length hesaplar.
     */
    private static int getValueLength(char[] str, int start){
        int initialStart = start;
        char currentChar;

        while(start < str.length){
            currentChar = str[start];
            if(currentChar == '(' || currentChar == ')')
                break;
            ++start;
        }

        return start - initialStart;
    }
}
