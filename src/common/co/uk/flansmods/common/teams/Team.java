package co.uk.flansmods.common.teams;

import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.List;

import co.uk.flansmods.common.FlansMod;

import net.minecraft.src.EntityPlayerMP;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;

public class Team {
	
	public static List<Team> teams = new ArrayList<Team>();
	public static Team spectators = new Team("spectators", "Spectators", 0xffffff, '7');
	
	public List<EntityPlayerMP> members = new ArrayList<EntityPlayerMP>();
	
	public List<ITeamBase> bases = new ArrayList<ITeamBase>();
	
	public List<PlayerClass> classes = new ArrayList<PlayerClass>();

	public String shortName;
	public String name;
	public int teamColour = 0xffffff;
	public char textColour = 'f';
	
	public ItemStack hat;
	public ItemStack chest;
	public ItemStack legs;
	public ItemStack shoes;
	
	public Team(String s, String s1, int teamCol, char textCol)
	{
		shortName = s;
		name = s1;
		teamColour = teamCol;
		textColour = textCol;
		teams.add(this);
	}
	
	public Team(BufferedReader file, String pack)
	{
		do
		{
			String line = null;
			try
			{
				line = file.readLine();
			} catch (Exception e)
			{
				break;
			}
			if (line == null)
			{
				break;
			}
			if (line.startsWith("//"))
				continue;
			String[] split = line.split(" ");
			if (split.length < 2)
				continue;
			read(split, file);
		} while (true);
		teams.add(this);
	}
	
	protected void read(String[] split, BufferedReader file)
	{
		try
		{
			if (split[0].equals("Name"))
			{
				name = split[1];
				for (int i = 0; i < split.length - 2; i++)
				{
					name = name + " " + split[i + 2];
				}
			}
			if (split[0].equals("ShortName"))
			{
				shortName = split[1];
			}
			if (split[0].equals("TeamColour"))
			{
				teamColour = (Integer.parseInt(split[1]) << 16) + ((Integer.parseInt(split[2])) << 8) + ((Integer.parseInt(split[3])));
			}			
			if (split[0].equals("TextColour"))
			{
				if(split[1].equals("Black"))
					textColour = '0';
				if(split[1].equals("Blue"))
					textColour = '1';
				if(split[1].equals("Green"))
					textColour = '2';
				if(split[1].equals("Aqua"))
					textColour = '3';
				if(split[1].equals("Red"))
					textColour = '4';
				if(split[1].equals("Purple"))
					textColour = '5';
				if(split[1].equals("Orange"))
					textColour = '6';
				if(split[1].equals("LGrey"))
					textColour = '7';
				if(split[1].equals("Grey"))
					textColour = '8';
				if(split[1].equals("LBlue"))
					textColour = '9';
				if(split[1].equals("LGreen"))
					textColour = 'a';
				if(split[1].equals("LAqua"))
					textColour = 'b';
				if(split[1].equals("Red"))
					textColour = 'c';
				if(split[1].equals("Pink"))
					textColour = 'd';
				if(split[1].equals("Yellow"))
					textColour = 'e';
				if(split[1].equals("White"))
					textColour = 'f';
			}
			if(split[0].equals("Hat") || split[0].equals("Helmet"))
			{
				if(split[1].equals("None"))
					return;
				for(Item item : Item.itemsList)
				{
					if(item != null && item.getItemName() != null && (item.getItemName().equals(split[1]) || (item.getItemName().split("\\.").length > 1 && item.getItemName().split("\\.")[1].equals(split[1]))))
						hat = new ItemStack(item);
				}
			}
			if(split[0].equals("Chest") || split[0].equals("Top"))
			{
				if(split[1].equals("None"))
					return;
				for(Item item : Item.itemsList)
				{
					if(item != null && item.getItemName() != null && (item.getItemName().equals(split[1]) || (item.getItemName().split("\\.").length > 1 && item.getItemName().split("\\.")[1].equals(split[1]))))
						chest = new ItemStack(item);
				}
			}
			if(split[0].equals("Legs") || split[0].equals("Bottom"))
			{
				if(split[1].equals("None"))
					return;
				for(Item item : Item.itemsList)
				{
					if(item != null && item.getItemName() != null && (item.getItemName().equals(split[1]) || (item.getItemName().split("\\.").length > 1 && item.getItemName().split("\\.")[1].equals(split[1]))))
						legs = new ItemStack(item);
				}
			}
			if(split[0].equals("Shoes") || split[0].equals("Boots"))
			{
				if(split[1].equals("None"))
					return;
				for(Item item : Item.itemsList)
				{
					if(item != null && item.getItemName() != null && (item.getItemName().equals(split[1]) || (item.getItemName().split("\\.").length > 1 && item.getItemName().split("\\.")[1].equals(split[1]))))
						shoes = new ItemStack(item);
				}
			}
			if(split[0].equals("AddDefaultClass") || split[0].equals("AddClass"))
			{
				classes.add(PlayerClass.getClass(split[1]));
			}
		} catch (Exception e)
		{
			System.out.println("Reading team file failed.");
			e.printStackTrace();
		}
	}
	
	public static Team getTeam(String s)
	{
		for(Team team : teams)
		{
			if(team.shortName.equals(s))
				return team;
		}
		return null;
	}
	
	//Called both by ops and the gametype
	public void addBase(ITeamBase base)
	{
		bases.add(base);
	}
	
	//Called both by ops and the gametype
	public void removeBase(ITeamBase base)
	{
		bases.remove(base);
	}
}
