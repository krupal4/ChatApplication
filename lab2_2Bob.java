/**
*19CP030
*Lab 2 Program 2 Server(Bob here)
*/
import java.net.Socket;
import java.net.ServerSocket;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.IOException;
import java.net.UnknownHostException;
import javax.swing.JFrame;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;
import javax.swing.JButton;
import javax.swing.JTextField;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.FlowLayout;
import java.awt.event.FocusListener;
import java.awt.event.FocusEvent;
import java.awt.Color;

class lab2_2Bob extends JFrame implements ActionListener
{
	private ServerSocket ss;
	private Socket bob;
	Thread aliceReceiver;
	PrintStream ps;
	BufferedReader br,brr;
	JTextArea myJTextArea;
	JScrollPane myJScrollPane;
	JButton myJButton;
	JTextField myJTextField;
	String placeholder;

	lab2_2Bob(String title)
	{
		super(title);
		myJTextArea=new JTextArea(20,20);
		myJButton=new JButton("Send");
		placeholder=new String("Type a message");
		myJTextField=new JTextField(placeholder,15);
		myJScrollPane=new JScrollPane(myJTextArea);
		
		myJTextField.setForeground(Color.GRAY);
		myJButton.addActionListener(this);
		myJTextField.addActionListener(this);		
		
		myJTextField.addFocusListener(new FocusListener(){
		@Override
		public void focusGained(FocusEvent fe)
		{
			if(myJTextField.getText().equals(placeholder))
			{
				myJTextField.setText("");
				myJTextField.setForeground(Color.BLACK);
			}
		}

		@Override
		public void focusLost(FocusEvent fe)
		{
			if(myJTextField.getText().isEmpty())
			{
				myJTextField.setForeground(Color.GRAY);
				myJTextField.setText(placeholder);
			}
		}
		});

		setLayout(new FlowLayout());
		add(myJScrollPane);
		add(myJTextField);
		add(myJButton);
		try
		{
			System.out.println("Waiting for request...");
			ss=new ServerSocket(1729);				
			bob=ss.accept();
			System.out.println("Connected...");
			br =new BufferedReader(new InputStreamReader(System.in));
			brr=new BufferedReader(new InputStreamReader(bob.getInputStream()));	
			ps=new PrintStream(bob.getOutputStream());
		}
		catch(UnknownHostException e)
		{
			//e.printStackTrace();
		}
		catch(IOException ioe)
		{
			//ioe.printStackTrace();
		}
		
		aliceReceiver=new Thread(new Runnable()
		{
			public void run()
			{
				while(true)
				{
					try
					{
						//System.out.println(" Alice: "+brr.readLine());
						myJTextArea.append(" Alice: "+ brr.readLine()+"\n");
					}
					catch(IOException ioe)
					{
						//ioe.printStackTrace();
						return;
					}
				}
			}
		});	
	}

	public void actionPerformed(ActionEvent ae)
	{
		if(myJTextField.getText().isEmpty())
		return;

		String temp=myJTextField.getText();
		myJTextArea.append(" Bob: "+temp+"\n");
		ps.println(temp);
		myJTextField.setText("");
	}

	public static void main(String args[])
	{
		lab2_2Bob bob_ref=new lab2_2Bob("Bob's Chat");
		bob_ref.aliceReceiver.start();
		bob_ref.setSize(270,405);
		bob_ref.setVisible(true);
		bob_ref.setResizable(false);
		bob_ref.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
	}
}

