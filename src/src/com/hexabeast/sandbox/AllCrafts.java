package com.hexabeast.sandbox;

import java.util.ArrayList;
import java.util.List;

public class AllCrafts {

	public static AllCrafts instance;
	
	public Integer[] StoneBlockCraft = new Integer[7];
	public Integer[] WoodenPlankCraft = new Integer[7];
	public Integer[] WoodenStickCraft = new Integer[7];
	public Integer[] WoodenStickAndStoneCraft = new Integer[7];
	public Integer[] ArrowCraft = new Integer[7];
	public Integer[] ChestCraft = new Integer[7];
	
	public List<Integer[]> craftList = new ArrayList<Integer[]>();
	
	
	public AllCrafts() {
		
		StoneBlockCraft[0] = AllBlocTypes.instance.Cobble.Id;
		StoneBlockCraft[1] = AllBlocTypes.instance.Cobble.Id;
		StoneBlockCraft[2] = AllBlocTypes.instance.Cobble.Id;
		StoneBlockCraft[3] = AllBlocTypes.instance.Cobble.Id;
		StoneBlockCraft[4] = AllBlocTypes.instance.StoneBricks.Id;
		StoneBlockCraft[5] = 1;//result number
		StoneBlockCraft[6] = 0;//tolerance 0 = no, 1 = yes
		
		WoodenPlankCraft[0] = AllBlocTypes.instance.Log.Id;
		WoodenPlankCraft[1] = 0;
		WoodenPlankCraft[2] = 0;
		WoodenPlankCraft[3] = 0;
		WoodenPlankCraft[4] = AllBlocTypes.instance.Wood.Id;
		WoodenPlankCraft[5] = 2;
		WoodenPlankCraft[6] = 1;//tolerance
		
		WoodenStickCraft[0] = AllBlocTypes.instance.Log.Id;
		WoodenStickCraft[1] = AllBlocTypes.instance.Log.Id;
		WoodenStickCraft[2] = 0;
		WoodenStickCraft[3] = 0;
		WoodenStickCraft[4] = AllTools.instance.WoodStickId;
		WoodenStickCraft[5] = 1;
		WoodenStickCraft[6] = 1;//tolerance
		
		ArrowCraft[0] = AllBlocTypes.instance.Log.Id;
		ArrowCraft[1] = AllBlocTypes.instance.Cobble.Id;
		ArrowCraft[2] = 0;
		ArrowCraft[3] = 0;
		ArrowCraft[4] = AllTools.instance.ArrowId;
		ArrowCraft[5] = 8;
		ArrowCraft[6] = 1;//tolerance
		
		WoodenStickAndStoneCraft[0] = AllTools.instance.WoodStickId;
		WoodenStickAndStoneCraft[1] = AllBlocTypes.instance.Cobble.Id;
		WoodenStickAndStoneCraft[2] = 0;
		WoodenStickAndStoneCraft[3] = 0;
		WoodenStickAndStoneCraft[4] = AllTools.instance.WoodStickAndStoneId;
		WoodenStickAndStoneCraft[5] = 1;
		WoodenStickAndStoneCraft[6] = 1;//tolerance
		
		
		ChestCraft[0] = AllBlocTypes.instance.Wood.Id;
		ChestCraft[1] = AllBlocTypes.instance.Wood.Id;
		ChestCraft[2] = AllBlocTypes.instance.Wood.Id;
		ChestCraft[3] = AllBlocTypes.instance.Wood.Id;
		ChestCraft[4] = AllTools.instance.ChestId;
		ChestCraft[5] = 1;
		ChestCraft[6] = 0;//tolerance
		
		craftList.add(StoneBlockCraft);
		craftList.add(WoodenPlankCraft);
		craftList.add(WoodenStickCraft);
		craftList.add(WoodenStickAndStoneCraft);
		craftList.add(ArrowCraft);
		craftList.add(ChestCraft);
		
	}
	
	public int[] getCraft(InvItem a, InvItem b,InvItem c,InvItem d)
	{
		for(int i = 0; i<craftList.size(); i++)
		{
			if(craftList.get(i)[6] == 0)
			{
				if(craftList.get(i)[0] == a.id && craftList.get(i)[1] == b.id && craftList.get(i)[2] == c.id && craftList.get(i)[3] == d.id)
				{
					int[] number = {craftList.get(i)[4],craftList.get(i)[5]};
					return number;
				}
			}
			else if(craftList.get(i)[6] == 1)
			{
				int[] given = {a.id,b.id,c.id,d.id};
				int[] from = {craftList.get(i)[0],craftList.get(i)[1],craftList.get(i)[2],craftList.get(i)[3]};
				if(isSame(from, given))
				{
					int[] number = {craftList.get(i)[4],craftList.get(i)[5]};
					return number;
				}
			}
			
		}
		int[] zero = {0,0};
		return zero;
	}
	
	public boolean isSame(int[] a, int[] b)
	{
		boolean[] aRight = {false,false,false,false};
		
		boolean[] bRight = {false,false,false,false};
		
		for(int i = 0; i<4; i++)
		{
			for(int j = 0; j<4; j++)
			{
				if(!aRight[i] && !bRight[j])
				{
					if(a[i] == b[j])
					{
						aRight[i] = true;
						bRight[j] = true;
					}
				}
			}
		}
		if(aRight[0] && aRight[1] && aRight[2] && aRight[3] && bRight[0] && bRight[1] && bRight[2] && bRight[3])return true;
		else return false;
	}

}
