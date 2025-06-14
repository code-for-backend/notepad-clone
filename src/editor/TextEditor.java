package editor;
import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Highlighter;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.*;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TextEditor extends JFrame {
    private JTextField textField;
    private JTextArea textArea;
    private JButton searchButton;
    private JButton previousButton;
    private JButton nextButton;
    private JMenuItem loadMenuItem;
    private JMenuItem saveMenuItem;
    private JMenuItem exitMenuItem;
    private static Highlighter.HighlightPainter currentHighlightPainter=new DefaultHighlighter.DefaultHighlightPainter(Color.CYAN);//highlight current word
    private static Highlighter.HighlightPainter highlightPainter=new DefaultHighlighter.DefaultHighlightPainter(Color.YELLOW);//highlight all words
    private java.util.List<Integer> matchPositions=new ArrayList<>();//keeps track of starting index of the word found
    private int matchCount;
    private int currentMatchPosition=-1;
    private java.util.List<Object> yellowHighlights=new ArrayList<>();
    private Object currentHighlight=null; //needed so as to remove the previous highlighted word and restore yellow highlight




    public TextEditor() {

       initializeComponents();
       addEventListeners();
        setupLayout();
       //configure the frame
        setTitle("Leafpad");
        setSize(800,600);
       setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
       setLocationRelativeTo(null); //center the frame
        // Make the window visible
        setVisible(true);
    }

private void initializeComponents()
{
    // Create JTextArea component
    textArea = new JTextArea();
    textArea.setLineWrap(true);
    textArea.setWrapStyleWord(true);
    textArea.setFont(new Font("DejaVu Sans Mono", Font.PLAIN, 14));
    textArea.setName("TextArea");
    textArea.setText("Welcome to the Text Search Example!\n\n" +
            "This is a sample text area where you can search for words.\n" +
            "Try searching for words like 'search', 'text', 'example', or 'Java'.\n\n" +
            "The search function is case-insensitive, so searching for 'SEARCH' " +
            "will find 'search' as well.\n\n" +
            "Java is a powerful programming language.\n" +
            "Java applications can run on various platforms.\n" +
            "Learning Java opens many opportunities in software development.\n\n" +
            "You can edit this text and search within your own content too!");

    //create a textfield
    textField=new JTextField(20);
    textField.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 12));
    textField.setName("FileNameField");

    //create menu bar and add two menu items
    JMenuBar menuBar=new JMenuBar();
    setJMenuBar(menuBar);
    JMenu fileMenu=new JMenu("File");
    saveMenuItem =new JMenuItem("Save");
    loadMenuItem=new JMenuItem("Open");
    exitMenuItem=new JMenuItem("Exit");

    menuBar.add(fileMenu);

    fileMenu.add(loadMenuItem);
    fileMenu.add(saveMenuItem);
    fileMenu.addSeparator();
    fileMenu.add(exitMenuItem);

    //create buttons
    searchButton=new JButton("Search");
    previousButton=new JButton("Previous");
    previousButton.setEnabled(false);
    nextButton=new JButton("Next");
    nextButton.setEnabled(false);




}


private void setupLayout()
{
    //search panel
    JPanel mainPanel=new JPanel();
    JPanel searchPanel=new JPanel();
    searchPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
    searchPanel.add(new JLabel("Search"));
    searchPanel.add(textField);
    searchPanel.add(searchButton);

    //nav panel
    JPanel navPanel=new JPanel();
    navPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
    navPanel.add(previousButton);
    navPanel.add(nextButton);

    mainPanel.setLayout(new BorderLayout());
    mainPanel.add(BorderLayout.NORTH,searchPanel);
    mainPanel.add(BorderLayout.SOUTH,navPanel);

    JScrollPane scroller=new JScrollPane(textArea);
    scroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
    scroller.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

    getContentPane().add(BorderLayout.NORTH,mainPanel);
    getContentPane().add(scroller);


}

private void addEventListeners()
{
    loadMenuItem.addActionListener(event->loadFile());
    saveMenuItem.addActionListener(event->saveFile());
    searchButton.addActionListener(event->search());
    previousButton.addActionListener(event->previousSearch());
    nextButton.addActionListener(event->nextSearch());

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


    private void search()
    {
        // for new word search
        resetSearchConfiguration();

        String text=textField.getText().trim();
        if(text.isEmpty())
        {
            JOptionPane.showMessageDialog(this,"Enter a word!");
            return;
        }

        String content=textArea.getText();
        if(content.isEmpty())
        {
            JOptionPane.showMessageDialog(this,"Text area is empty!!");
            return;
        }

        //we have a valid word as input...We will use the regex API

        Pattern pattern=Pattern.compile(text,Pattern.CASE_INSENSITIVE);
        Matcher matcher=pattern.matcher(content);
        while(matcher.find())
        {

            int start=matcher.start();
            int end=matcher.end();
            matchPositions.add(start);//starting index of each word found

            System.out.println("Start is "+start+" and end is "+end);
            matchCount++;
            try{
                Object yellowHighlight=textArea.getHighlighter().addHighlight(start,end,highlightPainter);
                yellowHighlights.add(yellowHighlight);
            } catch (BadLocationException e) {
                JOptionPane.showMessageDialog(this,"Oops something went wrong");
                return;
            }


        }

        if(matchCount==0)
        {
            JOptionPane.showMessageDialog(this,"0 matches found");
            return;
        }

        //Matches found make previous and next button active
        JOptionPane.showMessageDialog(this,"Matches found "+matchCount);
        previousButton.setEnabled(true);
        nextButton.setEnabled(true);


    }




    /*
    Lets say we found 5 matches for a word
    Then in the nextSearch method:-
    nextWordNumber refers to the word we want to highlightNext.Think of it like 1/5...2/5...and so on


     */


//highlight the next word
    //also currentMatchPosition can be thought as currentWordNumber
    private void nextSearch()
    {
        if(matchCount==0)//In case of no matches
            return;

        try {
            String word = textField.getText();
            if(word.isEmpty())
                return;
            int nextWordNumber = (currentMatchPosition + 1) % (matchPositions.size());
            int startIndex = matchPositions.get(nextWordNumber);//give me the startingIndex for the next word to be highlighted
            if (currentHighlight!=null) {

                textArea.getHighlighter().removeHighlight(currentHighlight);//clear the previous word highlighted
                Object highlightPreviousWord=textArea.getHighlighter().addHighlight(matchPositions.get(currentMatchPosition),matchPositions.get(currentMatchPosition)+word.length(),
                        highlightPainter);//restore the previous highlighted word to yellow

                yellowHighlights.set(currentMatchPosition,highlightPreviousWord);//update the highlight associated with the previous word

            }
//removes the yellow highlight and highlights the next word
            textArea.getHighlighter().removeHighlight(yellowHighlights.get(nextWordNumber));
            currentHighlight = textArea.getHighlighter().addHighlight(matchPositions.get(nextWordNumber), matchPositions.get(nextWordNumber) + word.length(), currentHighlightPainter);
            currentMatchPosition=nextWordNumber;
            textArea.setCaretPosition(startIndex+word.length()); //set caret position at the end of highlighted word
            textArea.requestFocusInWindow();

        }



        catch (BadLocationException e)
        {
            JOptionPane.showMessageDialog(this,"Something went wrong");
        }



    }

    private void previousSearch()
    {
        if(matchCount==0)  //In case of zero matches
            return;

        try {
            String word=textField.getText();
            if(word.isEmpty())
                return;

            //this will be the word to be highlighted
            int previousWordNumber=((currentMatchPosition==-1)||(currentMatchPosition==0))?matchPositions.size()-1:currentMatchPosition-1;
            if(currentHighlight!=null)
            {
                //clear the current highlight
                textArea.getHighlighter().removeHighlight(currentHighlight);
                Object yellowHighlight=textArea.getHighlighter().addHighlight(matchPositions.get(currentMatchPosition),matchPositions.get(currentMatchPosition)+word
                        .length(),highlightPainter);
                yellowHighlights.set(currentMatchPosition,yellowHighlight);



            }
            textArea.getHighlighter().removeHighlight(yellowHighlights.get(previousWordNumber));
            currentHighlight=textArea.getHighlighter().addHighlight(matchPositions.get(previousWordNumber),matchPositions.get(previousWordNumber)+
                    word.length(),currentHighlightPainter);
            currentMatchPosition=previousWordNumber;
            textArea.setCaretPosition(matchPositions.get(previousWordNumber)+word.length()); //put caret after the highlighted previous word
            textArea.requestFocusInWindow();

        } catch (BadLocationException e) {
            JOptionPane.showMessageDialog(this,"Some error occured!");

        }


    }

    //for a fresh search
    private void resetSearchConfiguration()
    {
        // for a new word search
        textArea.getHighlighter().removeAllHighlights();
        matchPositions.clear();//clear indices of matched words
        currentMatchPosition=-1;
        matchCount=0;
        yellowHighlights.clear();
        currentHighlight=null;
    }


    public static void main(String[] args) {
        new TextEditor();
    }




}