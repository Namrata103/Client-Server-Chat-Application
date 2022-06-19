import java.net.*;
import java.io.*;
import javax.swing.*;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import java.awt.Font;
import java.awt.BorderLayout;
import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;

public class Server extends JFrame
{
	ServerSocket server;
	Socket socket;
	
	BufferedReader br;
	PrintWriter out;
	
	//Component Declare
    	 private JLabel heading = new JLabel("Server Area");
    	 private JTextArea messageArea = new JTextArea();
     	private JTextField messageInput = new JTextField();
    	 private Font font = new Font("Roboto",Font.PLAIN,20);
   
	//constructor
	public Server()
	{
		try
		{
			server=new ServerSocket(7777);
			System.out.println("Server is ready to accept connection");
			System.out.println("Waiting");
			socket=server.accept();

			br=new BufferedReader(new InputStreamReader(socket.getInputStream()));
			out=new PrintWriter(socket.getOutputStream());

			createGUI();
			handleEvents();

			startReading();
			startWriting();


		}
		catch(Exception e)
		{
			e.printStackTrace();
		}

	}

	private void handleEvents()
    	{
    		messageInput.addKeyListener(new KeyListener(){

		public void keyPressed(KeyEvent e)
 		{      
      
  		}
		public void keyReleased(KeyEvent e)
 		{ 
    			//System.out.println("Key Realsed " +e.getKeyCode());
			if(e.getKeyCode() == 10)
			{
				//System.out.println("You have pressed enter button");

			
				String contentToSend = messageInput.getText();
				messageArea.append("Me : " +contentToSend + "\n" );
				out.println(contentToSend);
				out.flush();
				messageInput.setText("");
				messageInput.requestFocus();
			}
  		}
		public void keyTyped(KeyEvent e)
 		{
   		     
	 	} 

		});
     	}


     	private void createGUI()
     	{
		//gui code

		this.setTitle("Server Messenger");
		this.setSize(600,600);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		//CODING FOR COMPONENT

		heading.setFont(font);
		messageArea.setFont(font);
		messageInput.setFont(font);
	
		heading.setIcon(new ImageIcon("clogo.png"));
		heading.setHorizontalTextPosition(SwingConstants.CENTER);
		heading.setVerticalTextPosition(SwingConstants.BOTTOM);
		heading.setHorizontalAlignment(SwingConstants.CENTER);
		heading.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));
	
		messageArea.setEditable(false);
		messageInput.setHorizontalAlignment(SwingConstants.CENTER);
	
		//FRAME LAYOUT
		this.setLayout(new BorderLayout());
	
		//ADDING COMPONENTS TO FRAME

		this.add(heading,BorderLayout.NORTH);
		JScrollPane jscrollpane = new JScrollPane(messageArea);
		this.add(jscrollpane,BorderLayout.CENTER);
		this.add(messageInput,BorderLayout.SOUTH);

		this.setVisible(true);
     	}


	public void startReading()
	{
		Runnable r1=()->{
			
			System.out.println("Reader started...");
			
			try
			{
			while(true)
			{
				
					String msg=br.readLine();
					if(msg.equals("exit"))
					{
						System.out.println("Client Terminated the chat...");
						JOptionPane.showMessageDialog(this,"Server Terminated the chat");
						messageInput.setEnabled(false);

						socket.close();
						break;
					}
					//System.out.println("Client :"+msg);
					messageArea.append("Client : " + msg + "\n");
				
			}
		           }catch(Exception e){
			//e.printStackTrace();
			System.out.println("Connection Closed");
		}

		};
		new Thread(r1).start();
	}
	public void startWriting()
	{
		Runnable r2 = () ->{

			System.out.println("Writer Started...");
			try
			{
			while(!socket.isClosed())
			{
				
					BufferedReader br1 = new BufferedReader(new InputStreamReader(System.in));
					String content = br1.readLine();

					out.println(content);
					out.flush();

					if(content.equals("exit"))
					{
						socket.close();
						break;
					}

					
			 }
			
					
			}catch(Exception e){
			//e.printStackTrace();
			System.out.println("Connection Closed");
		}
		//System.out.println("Connection Closed");
		};
		new Thread(r2).start();
	}

	public static void main(String args[])
	{
		System.out.println("This is server.....");
		new Server();
	}
}