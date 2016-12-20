package Programs;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;
import java.io.*;
public class Assig4 {
	
	private JFrame window;
	private LeftPanel lPanel;
	private RightPanel rPanel;
	private int currentUserID;
	private String userName;
	private boolean userTried;
	private String[] voterArray;
	private ArrayList<Ballots> ballots;
	public Assig4()
	{
		userTried=false;
		lPanel = new LeftPanel();
		rPanel = new RightPanel();
		window = new JFrame("Voting Module");
		window.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		window.add(lPanel, BorderLayout.WEST);
		window.add(rPanel, BorderLayout.EAST);
		window.pack();
		window.setVisible(true);
	}
	
	private class LeftPanel extends JPanel
	{
		public LeftPanel()
		{
			
			try
			{
			ballots= new ArrayList<Ballots>();
			Scanner fileScan = new Scanner(new FileInputStream("ballots.txt"));
			int ballotCount=Integer.parseInt(fileScan.nextLine());
			String[] temps;
			for(int i=0; i<ballotCount; i++)
			{
				temps = fileScan.nextLine().split(":");
				int qID = Integer.parseInt(temps[0]);
				String bN = temps[1];
				String[] temps2 = temps[2].split(",");
				int questionCount = temps2.length;
				Ballots b1 = new Ballots(qID, bN, temps2, questionCount);
				ballots.add(b1);
				add(b1);
			}
			}
			catch(IOException i)
			{}
		}
	}
	
	private class RightPanel extends JPanel
	{
		JButton userLogin;
		JButton castVote;
		public RightPanel()
		{
			userLogin = new JButton("Login to Vote!");
			castVote = new JButton("Cast Your Vote!");
			Listener listen = new Listener();
			userLogin.addActionListener(listen);
			castVote.addActionListener(listen);
			castVote.setEnabled(false);
			add(userLogin);
			add(castVote);
			for(int i=0; i<ballots.size(); i++)
			{
				ballots.get(i).disable();
			}
		}
		
		private class Listener implements ActionListener
		{
			
			public void actionPerformed(ActionEvent e)
			{
			try
			{
				if(e.getSource().equals(userLogin))
				{
					
					 currentUserID = Integer.parseInt(JOptionPane.showInputDialog("Enter your ID"));
					 Scanner scanFile = new Scanner(new FileInputStream("voters.txt"));
					 ArrayList<String> ln = new ArrayList<String>();
					 while(scanFile.hasNext())
					 {
						 String line = scanFile.nextLine();
						 ln.add(line);
						 
					 }
					 voterArray = new String[ln.size()];
					 for(int i=0; i<ln.size(); i++)
					 {
						 voterArray[i] = ln.get(i);
					 }
					 for(int i=0; i<ln.size(); i++)
					 {
						 String[] subs = ln.get(i).split(":");
						 if(Integer.parseInt(subs[0])==currentUserID)
						 {
							 userName = subs[1];
							 userTried = Boolean.parseBoolean(subs[2]);
							 if(userTried)
							 {
								 JOptionPane.showMessageDialog(null, "Nice try " + userName + ", you can't vote twice you weasel");
								 break;
							 }
							 JOptionPane.showMessageDialog(null, userName+" make your selection now");
							 castVote.setEnabled(true);
							 userLogin.setEnabled(false);
							 for(int j=0; j<ballots.size(); j++)
							 {
								 ballots.get(j).enable();
							 }
							 break;
						 }
					 }
				}
				if(e.getSource().equals(castVote))
				{
					int ans;
					ans = JOptionPane.showConfirmDialog(null, "Cast your votes?");
					try
					{
						if(ans==0)
						{
						Scanner asdf;
						for(int i=0; i<ballots.size(); i++)
						{
							String[] ballotArray= new String[ballots.get(i).getNumQuestions()];
							asdf = new Scanner(new FileInputStream(ballots.get(i).getID()+".txt"));
							String backToTheFile;
							int questionAmount=0;
							String[] temp= null;
							for(int j=0; j<ballots.get(i).getNumQuestions(); j++)
							{
								temp = asdf.nextLine().split(":");
								questionAmount = Integer.parseInt(temp[1]);
								if(ballots.get(i).getCheck(j))
								{
									questionAmount+=1;
								}
								backToTheFile = temp[0] + ":" + String.valueOf(questionAmount);
								ballotArray[j] = backToTheFile;
							}
							save(ballotArray, String.valueOf(ballots.get(i).getID()));	
						}
						JOptionPane.showMessageDialog(null, "Thanks!");
						String userInfo = currentUserID+":"+userName+":"+userTried;
						for(int i=0; i<voterArray.length; i++)
						{
							if(voterArray[i].equals(userInfo))
							{
								userTried = true;
								userInfo = currentUserID+":"+userName+":"+userTried;
								voterArray[i]=userInfo;
								break;
							}
						}
						save2(voterArray);
						castVote.setEnabled(false);
						for(int i=0; i<ballots.size(); i++)
						{
							ballots.get(i).disable();
						}
						userLogin.setEnabled(true);
					}
					}	
					catch(IOException p)
					{}
				}
			}
			catch(IOException q)
			{}		
		}
		}
		
	}
	public void save(String info[], String ID)throws IOException
	{
		String fName = ID+".txt";
		PrintWriter writer = new PrintWriter(fName);
		for(int i=0; i<info.length; i++)
		{
			writer.println(info[i]);
		}
		writer.close();
	}
	
	public void save2(String info[])throws IOException
	{
		PrintWriter writer = new PrintWriter("voters.txt");
		for(int i=0; i<info.length; i++)
		{
			writer.println(info[i]);
		}
		writer.close();
		
	}
	
	public static void main(String[] args) {
		new Assig4();
	}

}