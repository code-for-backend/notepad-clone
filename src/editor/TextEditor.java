package editor;
import javax.swing.*;
import java.awt.*;

public class TextEditor extends JFrame {

    public TextEditor() {
        // Configure the frame first
        setSize(800, 600);
        setTitle("The first stage");
        setLayout(new BorderLayout());//BY default it's border layout but added as a defensive check


        // Create JTextArea component
        JTextArea textArea = new JTextArea();
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setFont(new Font("DejaVu Sans Mono", Font.PLAIN, 14));

        JScrollPane scroller=new JScrollPane(textArea);
        scroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        scroller.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        getContentPane().add(BorderLayout.CENTER,scroller);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);//center the jframe
        // Make the window visible
        setVisible(true);
    }


}