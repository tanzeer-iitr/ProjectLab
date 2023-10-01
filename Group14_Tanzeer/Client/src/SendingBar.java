import java.awt.*;
import javax.swing.*;


public class SendingBar{
    int x = 100;
    int y = 100;
    public JFrame frame = new JFrame();
	public JProgressBar bar = new JProgressBar(0,100);
    public int size, prog=0, temp=-1, flag=0, count=0;
    public SendingBar(String fileName){
		
        bar = new JProgressBar(0,100);
		bar.setValue(0);
		bar.setBounds(15,20,360,30);
		bar.setStringPainted(true);
		bar.setFont(new Font("Arial",Font.BOLD,20));
		bar.setForeground(Color.green);
		bar.setBackground(Color.black);
			
		frame.add(bar);
        frame.setLocationRelativeTo(null);
        frame.setTitle(fileName);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setSize(400, 100);
		frame.setLayout(null);
		frame.setVisible(true);
		
		fill(0);
	}
    public void fill(int p) {//System.out.print("*"+p);
		bar.setValue(p);
        bar.update(bar.getGraphics());
        // System.out.print("*"+p);
	}  
  
    public static void main(String[] args) 
    { 
        int x = 0;
        SendingBar demo = new SendingBar("Lecture");
        // demo.fill(56);
        while(x<=100){
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            demo.fill(x);
            x += 1;
        }
        // demo.bar.setString("Sent");
        
    }
}
