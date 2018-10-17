# BinaryTreeExtractor
Extracts a binary tree from specially formatted string and exports it as a png file representing the tree.

The string format looks like below example:

    d(b(c)(a))(f(k)(e))

Which represents the tree below: (where "d" is the root node)



                                d                
                                
                b                              f     
                
        c              a              k              e     
        

Outputs a png file like below:
![png output](https://github.com/haruntuncay/BinaryTreeExtractor/blob/master/images/binaryAsPng.png)

Saves png file to chosen location:

![file chooser](https://github.com/haruntuncay/BinaryTreeExtractor/blob/master/images/fileSaver.png)
