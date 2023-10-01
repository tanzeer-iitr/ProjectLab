// package Client;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.Socket;
//import java.util.zip.Deflater;
//import java.util.zip.DeflaterOutputStream;
import java.util.zip.*;
public class Client {
    //static JProgressBar progressBar;
    static int counter=0;
    //static int i=0;
    static File[] fileToSend = new File[1000];
    public static void main(String[] args) {

        // Accessed from within inner class needs to be final or effectively final.
        //final File[] fileToSend = new File[10];
        
        // Set the frame to house everything.
        JFrame jFrame = new JFrame("Client");
        // Set the size of the frame.
        jFrame.setSize(500, 300);
        // Make the layout to be box layout that places its children on top of each other.
        jFrame.setLayout(new BoxLayout(jFrame.getContentPane(), BoxLayout.Y_AXIS));
        // Make it so when the frame is closed the program exits successfully.
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Title above panel.
        JLabel jlTitle = new JLabel("File Sender");
        // Change the font family, size, and style.
        jlTitle.setFont(new Font("Arial", Font.BOLD, 25));
        // Add a border around the label for spacing.
        jlTitle.setBorder(new EmptyBorder(20,0,10,0));
        // Make it so the title is centered horizontally.
        jlTitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Label that has the file name.
        JLabel jlFileName = new JLabel("Choose a file to send");
        // Change the font.
        jlFileName.setFont(new Font("Arial", Font.BOLD, 20));
        // Make a border for spacing.
        jlFileName.setBorder(new EmptyBorder(10, 0, 0, 0));
        // Center the label on the x axis (horizontally).
        jlFileName.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Panel that contains the buttons.
        JPanel jpButton = new JPanel();
        // Border for panel that houses buttons.
        jpButton.setBorder(new EmptyBorder(25, 0, 10, 0));
        // Create send file button.
        JButton jbSendFile = new JButton("Send File");
        // Set preferred size works for layout containers.
        jbSendFile.setPreferredSize(new Dimension(150, 75));
        // Change the font style, type, and size for the button.
        jbSendFile.setFont(new Font("Arial", Font.BOLD, 20));
        jbSendFile.setBackground(Color.green);

        // Make the second button to choose a file.
        JButton jbChooseFile = new JButton("Choose File");
        // Set the size which must be preferred size for within a container.
        jbChooseFile.setPreferredSize(new Dimension(150, 75));
        // Set the font for the button.
        jbChooseFile.setFont(new Font("Arial", Font.BOLD, 20));
        jbChooseFile.setBackground(new Color(255, 102, 102));

        // Add the buttons to the panel.
        jpButton.add(jbChooseFile);
        jpButton.add(jbSendFile);

        // Button action for choosing the file.
        // This is an inner class so we need the fileToSend to be final.
        jbChooseFile.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Create a file chooser to open the dialog to choose a file.
                JFileChooser jFileChooser = new JFileChooser();
                // Set the title of the dialog.
                jFileChooser.setDialogTitle("Choose a file to send.");
                jFileChooser. setMultiSelectionEnabled(true) ;
                // Show the dialog and if a file is chosen from the file chooser execute the following statements.
                if (jFileChooser.showOpenDialog(null)  == JFileChooser.APPROVE_OPTION) {
                    
                    // Get the selected file.
                    //fileToSend[0] = jFileChooser.getSelectedFile();
                    //fileToSend[1]=jFileChooser.getSelectedFile();
                    
                    File[] files = jFileChooser.getSelectedFiles();
                    String fileNames = "";
                    //jlFileName.setText("Files: ");
                    for(File file: files){
                        fileToSend[counter++]=file;
                        //jlFileName.setText(" "+file.getName() +" ");
                        fileNames += file.getName() + " ";
                    }
                    for(int i=counter;i<1000;i++)
                    {
                        fileToSend[i]=null;
                    }
                    // Change the text of the java swing label to have the file name.
                    jlFileName.setText("Files: " + fileNames);//fileToSend[0].getName()+" "+fileToSend[1].getName());
                }
                counter=0;
            }
            
        });
        

        // Sends the file when the button is clicked.
        jbSendFile.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                
                for (File file : fileToSend) {
                Thread thread = new Thread(new FileSender(file));
                thread.start();
                }                
            }
        });

        // Add everything to the frame and make it visible.
        jFrame.add(jlTitle);
        jFrame.add(jlFileName);
        jFrame.add(jpButton);
        jFrame.setVisible(true);
        //i=0;
        //counter=0;
    }

    static class FileSender implements Runnable {

    private String serverAddress;
    private int serverPort;
    private File fileSend;

    public FileSender(File fileSend) {
        //this.serverAddress = serverAddress;
        //this.serverPort = serverPort;
        this.fileSend = fileSend;
    }

    @Override
    public void run() {
                {
                if (fileSend == null) {
                    //jlFileName.setText("Please choose a file to send first!");
                    // If a file has been selected then do the following.
                } else {
                    try {
                        // Create an input stream into the file you want to send.
                        FileInputStream fileInputStream = new FileInputStream(fileSend.getAbsolutePath());
                        String compressedFileName=fileSend.getName()+"_compressed.zip";
                        File compressedFile=new File(compressedFileName);
                        FileOutputStream fos = new FileOutputStream(compressedFile);
                        /*DeflaterOutputStream dos=new DeflaterOutputStream(fos);
  
                        //read data from FileInputStream and write it into DeflaterOutputStream
                        int data;
                        while ((data=fileInputStream.read())!=-1)
                        {
                            dos.write(data);
                        }*/
                        ZipOutputStream zos = new ZipOutputStream(fos);
                        ZipEntry ze = new ZipEntry(fileSend.getName());
                        zos.putNextEntry(ze);

                        // Read and write data
                        byte[] buffer = new byte[1024];
                        int bytesRead;
                        while ((bytesRead = fileInputStream.read(buffer)) > 0) {
                                zos.write(buffer, 0, bytesRead);
                        }
                        fileInputStream.close();
                        zos.close();
                        
                        
                        
            
                        
                        
                        
                        fileInputStream = new FileInputStream(compressedFileName);
                        // Create a socket connection to connect with the server.
                        Socket socket = new Socket("localhost", 1234);
                        // Create an output stream to write to write to the server over the socket connection.
                        DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
                        // Get the name of the file you want to send and store it in filename.
                        String fileName = compressedFile.getName();
                        // Convert the name of the file into an array of bytes to be sent to the server.
                        byte[] fileNameBytes = fileName.getBytes();
                        // Create a byte array the size of the file so don't send too little or too much data to the server.

                        // Send the length of the name of the file so server knows when to stop reading.
                        dataOutputStream.writeInt(fileNameBytes.length);
                        // Send the file name.
                        dataOutputStream.write(fileNameBytes);
                        
                        byte[] fileBytes = new byte[(int)compressedFile.length()];
                        // Send the length of the byte array so the server knows when to stop reading.
                        dataOutputStream.writeInt(fileBytes.length);

                        // progress bar
                        SendingBar pbar = new SendingBar(fileName);
                        
                        byte[] buffer2 = new byte[4 * 1024];
                        // Put the contents of the file into the array of bytes to be sent so these bytes can be sent to the server.
                        // fileInputStream.read(fileBytes);
                        // Send the actual file.
                        // dataOutputStream.write(fileBytes);
                        int bytes=0;
                        Long sentData=0L;
                        int prcnt=0;
                        while ((bytes = fileInputStream.read(buffer2))!= -1) {
                            sentData+=bytes;
                            if((100*sentData)/fileBytes.length>prcnt){
                                prcnt=(int)(100*sentData/fileBytes.length);
                                System.out.println("Percent sent - "+prcnt);
                                pbar.fill(prcnt);
                            }
                            // Send the file to Server Socket
                            dataOutputStream.write(buffer2, 0, bytes);
                            dataOutputStream.flush();  
                        }
                        prcnt=100;
                        fileInputStream.close();
                        dataOutputStream.close();
          

                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
               // i++;
                }
        
        
        }
    }
}
