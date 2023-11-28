package password_manager;

import java.io.File;
import java.io.FileWriter;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.io.IOException;
import java.util.TreeMap;
import java.util.Random;

public class Password_Manager
{
	private String username;
	private File passwords_File;
	private File users_File;
	private TreeMap<String,String> passwords;
	private TreeMap<String,String> users;
	public Scanner input = new Scanner(System.in);
	static String charSet = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwx"
			+ "yz1234567890!?!\"#$%&'()*+,-./:;<=>?@[\\]^_`{|}~";
	static String charSetNoSpecial = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghij"
			+ "klmnopqrstuvwxyz";
	
	public Password_Manager(String u)
	{
		username = u;
		passwords = new TreeMap<String, String>();
		users = new TreeMap<String, String>();
	}
	
	public void initialize()
	{
		boolean a = false;
		users_File = getFile("userdata");
		readUsers(users_File);
		if(users.containsKey(username))
		{
			while(!a)
			{
				System.out.print("Enter your password: ");
				if(input.next().equals(users.get(username)))
				{
					a = true;
				}
				else
				{
					System.out.print("\nWrong Password. Try Again.");
				}
			}
			passwords_File = getFile(username);
		}
		else
		{
			System.out.print("Create a master password: ");
			String pass = input.next();
			try
			{
				FileWriter write = new FileWriter("userdata", true);
				write.write(username + " " + pass + System.lineSeparator());
				write.close();
			}
			catch (IOException e) {
				e.printStackTrace();
			}
			passwords_File = getFile(username);
		}
		readPasswords(passwords_File);
	}
	
	private void readUsers(File x)
	{
		try
		{
			Scanner read = new Scanner(x);
			while(read.hasNext())
			{
				users.put(read.next(),read.next());
			}
			read.close();
		}
		catch(FileNotFoundException e)
		{
			
		}
	}
	
	private void readPasswords(File x)
	{
		try
		{
			Scanner read = new Scanner(x);
			while(read.hasNext())
			{
				passwords.put(read.next(),read.next());
			}
			read.close();
		}
		catch(FileNotFoundException e)
		{
			
		}
	}
	
	private File getFile(String name)
	{
		File f = new File(name);
		if(!f.exists())
		{
			try
			{
				f.createNewFile();
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
		return f;
	}
	
	private String genPassword(int length, String chars)
	{
		String newPass = "";
		Random rand = new Random();
		for(int i = 0; i < length; i++)
		{
	        int randomInt = rand.nextInt(chars.length());
	        char randomChar = chars.charAt(randomInt);
	        newPass = newPass + randomChar;
		}
		return newPass;
	}
	
	private void addPassword(String name, String pass)
	{
		try
		{
			FileWriter writer = new FileWriter(passwords_File, true);
			writer.write(name + " " + pass + System.lineSeparator());
			writer.close();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		passwords.put(name, pass);
	}
	
	private void addPassword(String name, int length, boolean special)
	{
		
		String newPass = "";
		if(special)
		{
			newPass = genPassword(length,charSet);
		}
		else
		{
			newPass = genPassword(length, charSetNoSpecial);
		}
		try
		{
			FileWriter writer = new FileWriter(passwords_File, true);
			writer.write(name + " " + newPass + System.lineSeparator());
			writer.close();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		passwords.put(name, newPass);
		System.out.print("\n" + newPass);
	}
	
	public String getPassword(String name)
	{
		return passwords.get(name);
	}
	
	public String toString()
	{
		String sum = "";
		for (String n : passwords.keySet())
		{
			sum = sum + n + "    " + passwords.get(n) + "\n";
		}
		return sum;
	}
	
	public void execute()
	{
		boolean cont = true;
		boolean cont2 = false;
		String un;
		String un2;
		String un3;
		int un4;
		while (cont)
		{
			System.out.print("\nWhat would you like to do (add, get, getall, end): ");
			un = input.next();
			if (un.equals("add"))
			{
				System.out.print("\nWhat is the password for: ");
				un = input.next();
				while(!cont2)
				{
					System.out.print("\nWould you like to create or gen(c/g): ");
					un2 = input.next();
					if(un2.equals("c"))
					{
						System.out.print("\nEnter password: ");
						un3 = input.next();
						addPassword(un,un3);
						cont2 = true;
					}
					else if(un2.equals("g"))
					{
						System.out.print("\nPassword length: ");
						un4 = input.nextInt();
						System.out.print("\nSpecial characters (y/n): ");
						un3 = input.next();
						if(un3.equals("y"))
						{
							addPassword(un,un4,true);
						}
						else if(un3.equals("n"))
						{
							addPassword(un,un4,false);
						}
						cont2 = true;
					}
					else
					{
						System.out.print("\nPlease enter c or g.");
					}
					
				}
				cont2 = false;
			}
			else if(un.equals("get"))
			{
				System.out.print("\nWhich password: ");
				un = input.next();
				if(passwords.containsKey(un))
				{
					System.out.print("\n" + passwords.get(un));
				}
				else
				{
					System.out.print("\nPassword not found.");
				}
			}
			else if(un.equals("getall"))
			{
				System.out.print("\n" + toString());
			}
			else if(un.equals("end"))
			{
				cont = false;
			}
		}
	}
	
	public static void main(String[] args)
	{
		Scanner uin = new Scanner(System.in);
		System.out.print("Enter your username: ");
		String t = uin.next();
		Password_Manager p = new Password_Manager(t);
		p.initialize();
		p.execute();
		uin.close();
	}
}