import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.regex.*;

class Editor implements ActionListener {
    private Frame fr;
    private TextArea t;
    private boolean change;
    private String filePath;

    public Editor() {
        fr = new Frame("Editor");
        fr.setSize(500, 500);
        fr.setBackground(Color.WHITE); 

        MenuBar menuBar = new MenuBar();
        Menu fileMenu = new Menu("File");
        Menu editMenu = new Menu("Edit");

        MenuItem newItem = new MenuItem("New");
        MenuItem openItem = new MenuItem("Open");
        MenuItem saveItem = new MenuItem("Save");
        MenuItem saveAsItem = new MenuItem("Save As");
        MenuItem closeItem = new MenuItem("Close");
        MenuItem findItem = new MenuItem("Find");
        MenuItem findReplaceItem = new MenuItem("Find & Replace");

        newItem.addActionListener(this);
        openItem.addActionListener(this);
        saveItem.addActionListener(this);
        saveAsItem.addActionListener(this);
        closeItem.addActionListener(this);
        findItem.addActionListener(this);
        findReplaceItem.addActionListener(this);

        fileMenu.add(newItem);
        fileMenu.add(openItem);
        fileMenu.add(saveItem);
        fileMenu.add(saveAsItem);
        fileMenu.addSeparator();
        fileMenu.add(closeItem);

        editMenu.add(findItem);
        editMenu.add(findReplaceItem);

        menuBar.add(fileMenu);
        menuBar.add(editMenu);

        fr.setMenuBar(menuBar);

        t = new TextArea();
        t.addTextListener(new TextListener() {
            public void textValueChanged(TextEvent e) {
                change = true; 
            }
        });
        fr.add(t);

        fr.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                handleClose();
            }
        });

        fr.setVisible(true);
        change = false; 
    }

    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();

        switch (command) {
            case "New":
                handleNew();
                break;
            case "Open":
                handleOpen();
                break;
            case "Save":
                handleSave();
                break;
            case "Save As":
                handleSaveAs();
                break;
            case "Close":
                handleClose();
                break;
            case "Find":
                handleFind();
                break;
            case "Find & Replace":
                handleFindReplace();
                break;
            default:
                System.out.println("Unknown command: " + command);
                break;
        }
    }

    private void handleNew() {
        if (change) {
            Dialog dialog = new Dialog(fr, "Unsaved Changes", true);
            dialog.setLayout(new FlowLayout());
            dialog.setSize(300, 150);

            Label message = new Label("You have unsaved changes. Do you want to save them?");
            Button saveButton = new Button("Save");
            Button dontSaveButton = new Button("Don't Save");
            Button cancelButton = new Button("Cancel");

            saveButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    handleSave();
                    dialog.dispose();
                    t.setText(""); 
                    change = false; 
                }
            });

            dontSaveButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    dialog.dispose();
                    t.setText(""); 
                    change = false; 
                }
            });

            cancelButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    dialog.dispose(); 
                }
            });

            dialog.add(message);
            dialog.add(saveButton);
            dialog.add(dontSaveButton);
            dialog.add(cancelButton);

            dialog.setVisible(true);
        } else {
            t.setText("");
            change = false; 
        }
    }

    private void handleOpen() {
        if(change){
            Dialog dialog = new Dialog(fr, "Unsaved Changes", true);
            dialog.setLayout(new FlowLayout());
            dialog.setSize(300, 150);

            Label message = new Label("You have unsaved changes. Do you want to save them?");
            Button saveButton = new Button("Save");
            Button dontSaveButton = new Button("Don't Save");
            Button cancelButton = new Button("Cancel");

            saveButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    handleSave();
                    dialog.dispose();
                    t.setText(""); 
                    change = false; 
                }
            });

            dontSaveButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    dialog.dispose();
                    t.setText(""); 
                    change = false; 
                }
            });

            cancelButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    dialog.dispose();  
                }
            });

            dialog.add(message);
            dialog.add(saveButton);
            dialog.add(dontSaveButton);
            dialog.add(cancelButton);

            dialog.setVisible(true);
        }
        else{
            FileDialog fileDialog = new FileDialog(fr, "Open File", FileDialog.LOAD);
            fileDialog.setVisible(true);
            
            filePath = fileDialog.getDirectory() + fileDialog.getFile();
            if (filePath != null) {
                File file = new File(filePath);
                try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                    t.setText(""); 
                    StringBuffer content = new StringBuffer();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        content.append(line).append("\n");
                    }
                    t.setText(content.toString()); 
                    change = false; 
                } catch (IOException e) {
                    System.out.println("Error reading file: " + e.getMessage());
                }
            }
        }
        }
    

    private void handleSave() {
        
        if(change){
            
            
            if (filePath != null) {
                File file = new File(filePath);
                try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                    writer.write(t.getText());
                    change = false;
                } catch (IOException e) {
                    System.out.println("Error writing file: " + e.getMessage());
                }
            }
            else{
                FileDialog fileDialog = new FileDialog(fr, "Save File", FileDialog.SAVE);
                    fileDialog.setVisible(true);
                    filePath = fileDialog.getDirectory() + fileDialog.getFile();

                    File file = new File(filePath);
                    try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                        writer.write(t.getText());
                        change = false;
                    } catch (IOException e) {
                        System.out.println("Error writing file: " + e.getMessage());
                    }

            }
        }
        else{
            System.out.println("already saved");
        }
    }

    private void handleSaveAs() {
        FileDialog fileDialog = new FileDialog(fr, "Save File", FileDialog.SAVE);
        fileDialog.setVisible(true);

        filePath = fileDialog.getDirectory() + fileDialog.getFile();
        if (filePath != null) {
            File file = new File(filePath);
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                writer.write(t.getText());
                change = false;
            } catch (IOException e) {
                System.out.println("Error writing file: " + e.getMessage());
            }
        }
      
    }

    private void handleClose() {
        if (change) {
            Dialog dialog = new Dialog(fr, "Unsaved Changes", true);
            dialog.setLayout(new FlowLayout());
            dialog.setSize(300, 150);

            Label message = new Label("You have unsaved changes. Do you want to save them before closing?");
            Button saveButton = new Button("Save");
            Button dontSaveButton = new Button("Don't Save");
            Button cancelButton = new Button("Cancel");

            saveButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    handleSave();
                    dialog.dispose();
                    fr.dispose(); 
                }
            });

            dontSaveButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    dialog.dispose();
                    fr.dispose();
                }
            });

            cancelButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    dialog.dispose(); 
                }
            });

            dialog.add(message);
            dialog.add(saveButton);
            dialog.add(dontSaveButton);
            dialog.add(cancelButton);

            dialog.setVisible(true);
        } else {
            fr.dispose(); 
        }
    }

private void handleFind() {
    final Dialog findDialog = new Dialog(fr, "Find", true);
    findDialog.setLayout(new FlowLayout());
    findDialog.setSize(300, 200);

    Label findLabel = new Label("Find:");
    TextField findField = new TextField(20);
    Button findButton = new Button("Find");
    Button findNextButton = new Button("Find Next");
    Button closeButton = new Button("Close");

    final int[] lastIndex = {-1};

    findButton.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            String searchText = findField.getText();
            if (!searchText.isEmpty()) {
                String content = t.getText();
                Pattern pattern = Pattern.compile(Pattern.quote(searchText), Pattern.CASE_INSENSITIVE);
                Matcher matcher = pattern.matcher(content);
                if (matcher.find()) {
                    lastIndex[0] = matcher.start();
                    t.select(lastIndex[0], matcher.end());
                    t.requestFocus();
                } else {
                    Dialog errorDialog = new Dialog(fr, "Error", true);
                    errorDialog.setLayout(new FlowLayout());
                    errorDialog.setSize(300, 150);
                    Label errorLabel = new Label("Text not found.");
                    Button okButton = new Button("OK");
                    errorDialog.add(errorLabel);
                    errorDialog.add(okButton);

                    okButton.addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent e) {
                            errorDialog.dispose();
                        }
                    });

                    errorDialog.setVisible(true);
                }
            }
        }
    });

    findNextButton.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            String searchText = findField.getText();
            if (!searchText.isEmpty()) {
                String content = t.getText();
                Pattern pattern = Pattern.compile(Pattern.quote(searchText), Pattern.CASE_INSENSITIVE);
                Matcher matcher = pattern.matcher(content);
                
                if (lastIndex[0] != -1) {
                    matcher.region(lastIndex[0] + 1, content.length());
                }

                if (matcher.find()) {
                    lastIndex[0] = matcher.start();
                    t.select(lastIndex[0], matcher.end());
                    t.requestFocus();
                } else {
                    Dialog errorDialog = new Dialog(fr, "Error", true);
                    errorDialog.setLayout(new FlowLayout());
                    errorDialog.setSize(300, 150);
                    Label errorLabel = new Label("No more occurrences found.");
                    Button okButton = new Button("OK");
                    errorDialog.add(errorLabel);
                    errorDialog.add(okButton);

                    okButton.addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent e) {
                            errorDialog.dispose();
                        }
                    });

                    errorDialog.setVisible(true);
                }
            }
        }
    });

    closeButton.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            findDialog.dispose(); 
            if (lastIndex[0] != -1) {
                String searchText = findField.getText();
                t.select(lastIndex[0], lastIndex[0] + searchText.length());
                t.requestFocus();
            }
        }
    });

    findDialog.addWindowListener(new WindowAdapter() {
        public void windowClosing(WindowEvent e) {
            findDialog.dispose();
            if (lastIndex[0] != -1) {
                String searchText = findField.getText();
                t.select(lastIndex[0], lastIndex[0] + searchText.length());
                t.requestFocus();
            }
        }
    });

    findDialog.add(findLabel);
    findDialog.add(findField);
    findDialog.add(findButton);
    findDialog.add(findNextButton);
    findDialog.add(closeButton);

    findDialog.setVisible(true);
}
    
    
    

private void handleFindReplace() {
    final Dialog replaceDialog = new Dialog(fr, "Find & Replace", true);
    replaceDialog.setLayout(new FlowLayout());
    replaceDialog.setSize(300, 250);

    Label findLabel = new Label("Find:");
    TextField findField = new TextField(20);
    Label replaceLabel = new Label("Replace with:");
    TextField replaceField = new TextField(20);
    Button replaceButton = new Button("Replace");
    Button replaceAllButton = new Button("Replace All");
    Button cancelButton = new Button("Cancel");

    replaceButton.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            String searchText = findField.getText();
            String replaceText = replaceField.getText();
            if (!searchText.isEmpty()) {
                String content = t.getText();
                Pattern pattern = Pattern.compile(searchText, Pattern.CASE_INSENSITIVE);
                Matcher matcher = pattern.matcher(content);
                
                if (matcher.find()) {
                    t.setText(content.substring(0, matcher.start()) + replaceText + content.substring(matcher.end()));
                    t.requestFocus();
                } else {
                    Dialog errorDialog = new Dialog(fr, "Error", true);
                    errorDialog.setLayout(new FlowLayout());
                    errorDialog.setSize(300, 150);
                    Label errorLabel = new Label("Text not found.");
                    Button okButton = new Button("OK");
                    errorDialog.add(errorLabel);
                    errorDialog.add(okButton);

                    okButton.addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent e) {
                            errorDialog.dispose();
                        }
                    });

                    errorDialog.setVisible(true);
                }
            }
        }
    });

    replaceAllButton.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            String searchText = findField.getText();
            String replaceText = replaceField.getText();
            if (!searchText.isEmpty()) {
                String content = t.getText();
                Pattern pattern = Pattern.compile(searchText, Pattern.CASE_INSENSITIVE);
                Matcher matcher = pattern.matcher(content);
                String updatedContent = matcher.replaceAll(replaceText);
                t.setText(updatedContent);
                t.requestFocus();
            }
        }
    });

    cancelButton.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            replaceDialog.dispose();
        }
    });

    replaceDialog.addWindowListener(new WindowAdapter() {
        public void windowClosing(WindowEvent e) {
            replaceDialog.dispose();
        }
    });

    replaceDialog.add(findLabel);
    replaceDialog.add(findField);
    replaceDialog.add(replaceLabel);
    replaceDialog.add(replaceField);
    replaceDialog.add(replaceButton);
    replaceDialog.add(replaceAllButton);
    replaceDialog.add(cancelButton);

    replaceDialog.setVisible(true);
}

    public static void main(String[] args) {
        new Editor();
    }
}
