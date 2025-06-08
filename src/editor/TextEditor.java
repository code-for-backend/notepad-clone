package editor;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.io.*;

public class TextEditor extends JFrame {
    private JTextField textField;
    private JTextArea textArea;


    public TextEditor() {
        // Configure the frame first
        setSize(800, 600);
        setTitle("The first stage");
        setLayout(new BorderLayout());//BY default it's border layout but added as a defensive check


        // Create JTextArea component
        textArea = new JTextArea();
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setFont(new Font("DejaVu Sans Mono", Font.PLAIN, 14));
        textArea.setName("TextArea");

        //create a textfield
        textField=new JTextField(20);
        textField.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 12));
        textField.setName("FileNameField");

        //create two buttons
        JButton loadButton=new JButton("Load");
        loadButton.setToolTipText("Load the file");
        loadButton.setName("LoadButton");
        loadButton.addActionListener(event->loadFile());






        JButton saveButton=new JButton("Save");
        saveButton.setToolTipText("Save the file");
        saveButton.setName("SaveButton");
        saveButton.addActionListener(event->saveFile());

        //add a panel which will contain a text field and two buttons,load and save
        JPanel panel=new JPanel();
        panel.add(textField);
        panel.add(loadButton);
        panel.add(saveButton);

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

    private void loadFile()
    {

        String fileName=textField.getText();
        if(fileName.isBlank()) //if input is whitespace or no input
            return;
        File file=new File(fileName);
        try(BufferedReader reader=new BufferedReader(new FileReader(file)))
        {
            textArea.read(reader,null);//handles newline issue while manual reading

        }
        catch(FileNotFoundException e)
        {
            textArea.setText("");//make text area empty when file is not found
            JOptionPane.showMessageDialog(null,"File "+fileName+" not found");

        }
        catch(IOException e)
        {
            JOptionPane.showMessageDialog(null,"Error reading file "+fileName);

        }



    }


    private void saveFile()
    {
        String fileName=textField.getText();
        if(fileName.isBlank())
            return;
        File file=new File(fileName);
        try(BufferedWriter writer=new BufferedWriter(new FileWriter(fileName)))
        {
            writer.write(textArea.getText());

        }
        catch(IOException e)
        {
            JOptionPane.showMessageDialog(null,"Error saving the file "+fileName);


        }






    }





}