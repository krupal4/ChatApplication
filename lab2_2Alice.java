/**
*19CP030
*Lab 2 Program 2 Alice(Client here)
*/

import java.net.Socket;
import java.io.PrintStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.net.UnknownHostException;
import java.net.SocketException;
import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.JTextArea;
import javax.swing.JButton;
import javax.swing.JScrollPane;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.FlowLayout;
import java.awt.event.FocusListener;
import java.awt.event.FocusEvent;
import java.awt.Color;

class lab2_2Alice extends JFrame implements ActionListener
{
	private Socket alice;
	Thread bobReceiver;
	PrintStream ps;
	BufferedReader br,brr;
	JTextField myJTextField;
	JTextArea myJTextArea;
	JButton myJButton;
	JScrollPane myJScrollPane;
	String placeholder;

	lab2_2Alice(String title)
	{
		super(title);
		placeholder=new String("Type a message");
		myJTextField=new JTextField(placeholder,15);
		myJButton=new JButton("Send");
		myJTextArea=new JTextArea(20,20);
		myJScrollPane=new JScrollPane(myJTextArea);

		this.setLayout(new FlowLayout());
		add(myJScrollPane);
		add(myJTextField);
		add(myJButton);

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
	
		try
		{
			alice=new Socket("127.0.0.1",1729);
			ps=new PrintStream(alice.getOutputStream());
			br =new BufferedReader(new InputStreamReader(System.in));
			brr=new BufferedReader(new InputStreamReader(alice.getInputStream()));
		}
		catch(UnknownHostException e)
		{
			//e.printStackTrace();
		}
		catch(IOException ioe)
		{
			//ioe.printStackTrace();
		}

		bobReceiver=new Thread(new Runnable()
		{
			public void run()
			{	
				while(true)
				{
					try
					{
						//System.out.println("Bob: "+brr.readLine());
						myJTextArea.append(" Bob: "+brr.readLine()+"\n");
					}
					catch(IOException e)	
					{
						//e.printStackTrace();
						break;
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
		ps.println(temp);
		myJTextArea.append(" Alice: "+temp+"\n");
		myJTextField.setText("");
	}

	public static void main(String args[])	
	{
		lab2_2Alice alice_ref=new lab2_2Alice("Alice's Chat");
		alice_ref.bobReceiver.start();	
		alice_ref.setSize(270,405);		//(width,height)
		alice_ref.setVisible(true);
		alice_ref.setResizable(false);
		alice_ref.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
}