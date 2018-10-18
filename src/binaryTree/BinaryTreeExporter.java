package binaryTree;

import javafx.scene.Scene;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.Window;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.io.*;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import java.util.stream.Stream;

public class BinaryTreeExporter {

    //Default değerler.
    private static int IMAGE_WIDTH = 900;
    private static int IMAGE_HEIGHT = 800;
    private static int MAX_TREE_LEVEL = 6;
    private static int HEIGHT_PER_LEVEL = IMAGE_HEIGHT / MAX_TREE_LEVEL;
    private static int NODE_DIAMETER = 40;

    /*
        options.properties dosyasını okuyup, ilgili static alanları doldurur.
        Bu alanlar, çıktı resminin boyutlarını ve Node'ların çapını ayarlamak için kullanılır.
     */
    static{
        try(BufferedReader reader =
                    new BufferedReader(
                        new InputStreamReader(
                                new FileInputStream("options.properties")))){

            Properties props = new Properties();
            props.load(reader);

            IMAGE_WIDTH = Integer.parseInt(props.getProperty("image_option.width"));
            IMAGE_HEIGHT = Integer.parseInt(props.getProperty("image_option.height"));
            MAX_TREE_LEVEL = Integer.parseInt(props.getProperty("image_option.max_tree_level"));
            HEIGHT_PER_LEVEL = IMAGE_HEIGHT / MAX_TREE_LEVEL;
            NODE_DIAMETER = Integer.parseInt(props.getProperty("node_option.diameter"));
        }catch(IOException ex){
            System.out.println("options.properties dosyasını okurken hata oluştu.Default değerler kullanılacak.");
            System.out.println("Hata Mesajı: " + ex.getMessage());
        }
    }

    public enum FILE_TYPE {
        JPG("jpg"), PNG("png"), GIF("gif");

        private String value;

        FILE_TYPE(String type){
            this.value = type;
        }

        public String getValue(){
            return value;
        }
    }

    /*
        Node'ları ve parent'ları ile bağlantılarını çizen metod.
     */
    private static void drawNode(Graphics2D g2d, Node node, int level, int nodePosition, int nodeCountAtLevel){
        if(node != null){
            //Node'ların üst boşluklarını hesapla.
            int topMargin = HEIGHT_PER_LEVEL * level + 30;
            /*
                Node'lar ile köşelerin arasındaki ve Node'ların birbiri arasındaki boşluğu hesapla.
                Toplam WIDTH alanının, geçerli seviyedeki(level) node sayısı + 1'e bölümüne eşittir.
            */
            int leftMargin = IMAGE_WIDTH / (nodeCountAtLevel + 1);

            node.setX(leftMargin * nodePosition - (NODE_DIAMETER / 2));
            node.setY(topMargin - (NODE_DIAMETER / 2));
            //Node'u çiz.
            g2d.drawOval(node.getX(), node.getY(), NODE_DIAMETER, NODE_DIAMETER);
            //Node value string'ini çiz ve ortalamaya çalış.
            g2d.drawString(node.getValue(),
                    node.getX() + (NODE_DIAMETER / 3),
                    node.getY() + (NODE_DIAMETER / 2) + 5);

            //Node ile parent'ı arasındaki edge'i çiz.
            Node parent;
            if((parent = node.getParent()) != null){
                g2d.drawLine(parent.getX() + (NODE_DIAMETER / 2), parent.getY() + NODE_DIAMETER, node.getX() + (NODE_DIAMETER / 2), node.getY());
            }
        }
    }

    /*
        BinaryTree'yi resim olarak çizip export eden metod.
        Node'ların çizimini seviye seviye(level) olarak gerçekleştirir.
     */
    public static void export(BinaryTree tree, FILE_TYPE filetype){
        BufferedImage buffImage = new BufferedImage(IMAGE_WIDTH, IMAGE_HEIGHT, BufferedImage.TYPE_INT_ARGB);

        //Çizim için bir BufferedImage'den bir graphic objesi al.
        Graphics2D g2d = buffImage.createGraphics();
        //Arka planı beyaza boya.
        g2d.setPaint(Color.WHITE);
        g2d.fillRect(0, 0, IMAGE_WIDTH, IMAGE_HEIGHT);
        //Çizgi rengini siyah yap.
        g2d.setPaint(Color.BLACK);

        /*
            Seviye seviye Node'ları tutmak için kullanılacak olan liste.
            Queue görevinde kullanılır.
        */
        List<Node> outerList = new LinkedList<>();
        //Ağacın root'unu ilk eleman olarak ekle.
        Node root = tree.getRoot();
        outerList.add(root);
        //Root Node'u çiz.
        drawNode(g2d, root, 0, 1, 1);

        //Yeni satırda eleman kalıp kalmadığını tutan boolean.
        boolean isRowEmpty = false;
        //Level değişkeni, Node'ları çizerken TopMargin değerini belirlemekte kullanılacak.
        int level = 1;

        while(!isRowEmpty){
            /*
                OuterList'ten çekilen nodeların çocuklarını tutacak.
                Level'deki çocukların sayısını okuyabilmek ve level'de çocuk var mı bilmek için gerekli.
             */
            List<Node> innerList = new LinkedList<>();
            isRowEmpty = true;

            /*
                OuterList'teki Node'ları tek tek alır ve çocuklarını "innerList"'e ekler.
             */
            while(!outerList.isEmpty()){
                Node temp = outerList.remove(0);

                if(temp != null){
                    innerList.add(temp.getLeft());
                    innerList.add(temp.getRight());
                    /*
                        Eğer sağ veya sol çocuktan biri varsa, yeni seviye(level) gerekiceği için,
                            isRowEmpty'yi false yap.
                    */
                    isRowEmpty = temp.getLeft() == null && temp.getRight() == null;
                }else{
                    innerList.add(null);
                    innerList.add(null);
                }
            }

            //Level'deki çocuk sayısı.
            int nodeCount = innerList.size();
            /*
                Position değişkeni, Node'ları çizerken LeftMargin değeriyle çarpılacak,
                    bulunan değer Node'un sol boşluğu olacak. (leftMargin = position * width);
             */
            int position = 1;
            //InnerList'ten çekilen çocukları çizmek için kullanılacak referans.
            Node temp;
            /*
                InnerList'teki Node'ları çek, çiz ve outerList'e ata.
             */
            while(!innerList.isEmpty()){
                temp = innerList.remove(0);
                drawNode(g2d, temp, level, position, nodeCount);
                ++position;
                outerList.add(temp);
            }

            ++level;
        }

        //Graphics2d kullanılarak oluşturulmuş bufferedImage objesini export et.
        exportBufferedImage(buffImage, filetype);
    }

    /*
        Dosya kayıt yeri seçicisi aç ve kullanıcıdan dosya kayıt yolunu al.
        Graphics2d kullanılarak çizilmiş olan BufferedImage objesini dosyaya yaz.
    */
    private static void exportBufferedImage(BufferedImage buffImage, FILE_TYPE filetype){
        try {
            JFrame frame = new JFrame();
            frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Dosyayı kaydetmek istediğiniz yeri seçin");

            frame.setVisible(true);

            int userSelection = fileChooser.showSaveDialog(frame);
            if (userSelection == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();
                ImageIO.write(buffImage,
                        filetype.getValue(),
                        new File( file.getAbsolutePath() + "." + filetype.getValue()));
            }

            frame.pack();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
