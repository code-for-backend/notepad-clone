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
        textArea.setName("TextArea");

        //create a textfield
        JTextField textField=new JTextField(20);
        textField.setText("Enter file name...");

        //create two buttons
        JButton saveButton=new JButton("Save");
        saveButton.setName("SaveButton");
        JButton editButton=new JButton("Edit");
        editButton.setName("EditButton");

        //add a panel which will contain a text field and two buttons,load and save
        JPanel panel=new JPanel();
        panel.add(textField);
        panel.add(saveButton);
        panel.add(editButton);

        //add panel to frame
        getContentPane().add(BorderLayout.NORTH,panel);


        JScrollPane scroller=new JScrollPane(textArea);
        scroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        scroller.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scroller.setName("ScrollPane"); //for testing purpose only not needed necessarily
        getContentPane().add(BorderLayout.CENTER,scroller);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);//center the jframe
        // Make the window visible
        setVisible(true);
    }


}