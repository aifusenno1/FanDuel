package TwoGame;
import java.io.*;
import java.text.DecimalFormat;
import java.util.*;

import Player.Player;
import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;
import jxl.write.Label;
import jxl.write.Number;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

public class Optimizer2Game {
	/*
	 * Optimizer generates all the top lineups from today's roster with salarySum between 58000 and 60000 
	 * (because people don't usually use lineups with lower salary than that)
	 */
	
	public static int salarySum, column, row;
	public static double valueSum, consSum, scoreSum;
	public static boolean otherConditions;
	public static ArrayList<Player> roster;
	public static WritableSheet shh;


	public static void main(String[] args) throws IOException, BiffException,
			RowsExceededException, WriteException {
		Workbook wb = Workbook.getWorkbook(new File("roster.xls"));
		
		// Input Area Starts
		WritableWorkbook lineup = Workbook.createWorkbook(new File ("10-27 top 2.xls"));
		shh = lineup.createSheet("sheet1", 0);

		Sheet sh = wb.getSheet("10-27 3 game");

		//Input Area Ends
		
		ArrayList<Player> pg = new ArrayList<>();
		ArrayList<Player> sg = new ArrayList<>();
		ArrayList<Player> sf = new ArrayList<>();
		ArrayList<Player> pf = new ArrayList<>();
		ArrayList<Player> c = new ArrayList<>();

		for (int r = 1; r < sh.getRows(); r++) {
			String name = sh.getCell(0, r).getContents();
			String position = sh.getCell(1, r).getContents();
			int salary = Integer.parseInt(sh.getCell(2, r).getContents());
			String team = sh.getCell(3, r).getContents();
			double value = Double.parseDouble(sh.getCell(4, r).getContents());
			double cons = Double.parseDouble(sh.getCell(5, r).getContents());
			int game = Integer.parseInt(sh.getCell(6, r).getContents());
			String start = sh.getCell(7,r).getContents();
			double score = Double.parseDouble(sh.getCell(8, r).getContents());

			Player player = new Player(name, position, salary, team, value,cons,game,start,score);
			if (player.position.equals("PG"))
				pg.add(player);
			if (player.position.equals("SG"))
				sg.add(player);
			if (player.position.equals("SF"))
				sf.add(player);
			if (player.position.equals("PF"))
				pf.add(player);
			if (player.position.equals("C"))
				c.add(player);
		}
		
		
		shh.addCell(new Label(0,0,"PG"));
		shh.addCell(new Label(1,0,"PG"));
		shh.addCell(new Label(2,0,"SG"));
		shh.addCell(new Label(3,0,"SG"));
		shh.addCell(new Label(4,0,"SF"));
		shh.addCell(new Label(5,0,"SF"));
		shh.addCell(new Label(6,0,"PF"));
		shh.addCell(new Label(7,0,"PF"));
		shh.addCell(new Label(8,0,"C"));
		shh.addCell(new Label(9,0,"Salary"));
		shh.addCell(new Label(10,0,"Projected Value"));
		shh.addCell(new Label(11,0,"Consistency"));
		shh.addCell(new Label(14,0,"start"));
		shh.addCell(new Label(15,0,"Score"));

		column = 0;
		row = 1;

		for (int pg1 = 0; pg1 < pg.size() - 1; pg1++)
			for (int pg2 = pg1 + 1; pg2 < pg.size(); pg2++)
				for (int sg1 = 0; sg1 < sg.size() - 1; sg1++)
					for (int sg2 = sg1 + 1; sg2 < sg.size(); sg2++)
						for (int sf1 = 0; sf1 < sf.size() - 1; sf1++)
							for (int sf2 = sf1 + 1; sf2 < sf.size(); sf2++)
								for (int pf1 = 0; pf1 < pf.size() - 1; pf1++)
									for (int pf2 = pf1 + 1; pf2 < pf.size(); pf2++)
										for (int c1 = 0; c1 < c.size(); c1++) {
											roster = new ArrayList<>();
											roster.add(pg.get(pg1));
											roster.add(pg.get(pg2));
											roster.add(sg.get(sg1));
											roster.add(sg.get(sg2));
											roster.add(sf.get(sf1));
											roster.add(sf.get(sf2));
											roster.add(pf.get(pf1));
											roster.add(pf.get(pf2));
											roster.add(c.get(c1));

											salarySum = 0;
											valueSum = 0;
											consSum = 0;
											scoreSum = 0;
											int game1 = 0;
											int game2 = 0;
											int startNum = 0;
											TreeMap teamNum = new TreeMap();
											for (Player ele : roster) {
												salarySum += ele.salary;
												valueSum += ele.value;
												consSum += ele.cons;
												scoreSum += ele.score;
												if (ele.start.equals("x"))
													startNum++;
											
												if (ele.game == 1)
													game1++;
												if (ele.game == 2)
													game2++;
												
												if (!teamNum.containsKey(ele.team))
													teamNum.put(ele.team, 1);
												else
													teamNum.put(ele.team,(int)teamNum.get(ele.team) + 1);
											}
											
											
											otherConditions = true;
											for (Object ele:teamNum.values()){
												if ((int)ele > 4)
													otherConditions = false;
											}
											

											valueSum = Double.parseDouble(String.format("%.1f", valueSum));
											consSum = Double.parseDouble(String.format("%.2f", consSum));
											scoreSum = Double.parseDouble(String.format("%.1f", scoreSum));
											
											if (salarySum>=58000 && salarySum<=60000 && scoreSum >=315 && otherConditions == true) {
												for (Player ele : roster) {
													Label label = new Label(column, row, ele.name);
													shh.addCell(label);
													column++;
												}
												shh.addCell(new Label(column, row, Integer.toString(salarySum)));
												shh.addCell(new Label(++column, row, Double.toString(valueSum)));
												shh.addCell(new Label(++column, row, Double.toString(consSum)));
												shh.addCell(new Label(++column, row, game1>game2?Integer.toString(game1):Integer.toString(game2)));
												shh.addCell(new Label(++column, row, Integer.toString(startNum)));
												shh.addCell(new Label(++column, row, Double.toString(scoreSum)));

												column = 0;
												row++;
											}
											
										
										}
		lineup.write();
		lineup.close();
		wb.close();

	}
	
}
	