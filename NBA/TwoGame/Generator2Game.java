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

public class Generator2Game{
	
	public static int salarySum, column, row;
	public static double valueSum, consSum;
	public static boolean otherConditions;
	public static ArrayList<Player> roster;
	public static WritableSheet shh;


	public static void main(String[] args) throws IOException, BiffException,
			RowsExceededException, WriteException {
		Workbook wb = Workbook.getWorkbook(new File("roster.xls"));
		
		// Input Area Starts
		WritableWorkbook lineup = Workbook.createWorkbook(new File ("5-15.xls"));
		shh = lineup.createSheet("sheet1", 0);

		Sheet sh = wb.getSheet("5-15");
		
		double min = -4.26;
		double max = 425.6;
		
		double higherThan = 46;
		double lowerThan = -0.45;
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

			Player player = new Player(name, position, salary, team, value,cons,game,start);
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
		// System.out.println(pg);

		
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
											int game1 = 0;
											int game2 = 0;
											int highValue = 0;
											int lowCons = 0;
											int startNum = 0;
											
											TreeMap teamNum = new TreeMap();
											for (Player ele : roster) {
												salarySum += ele.salary;
												valueSum += ele.value;
												consSum += ele.cons;
												
												if (ele.start.equals("x"))
													startNum++;
												
												if (ele.game == 1)
													game1++;
												if (ele.game == 2)
													game2++;
												if (ele.value>=higherThan)
													highValue++;
												if (ele.cons<=lowerThan)
													lowCons++;
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
											
											if (game1>=7 || game2 >=7 )
												otherConditions = false;
											
											if (highValue < 3 || highValue > 6)
												otherConditions = false;

											if (lowCons < 1 || lowCons >4)
												otherConditions = false;
											
//											if (startNum > 3 || startNum <1)
//												otherConditions = false;
											
											valueSum = Double.parseDouble(String.format("%.1f", valueSum));
											consSum = Double.parseDouble(String.format("%.2f", consSum));
											
//											findMaxMin();
											generateLineups(max, min);
											
										
										}
		lineup.write();
		lineup.close();
		wb.close();

	}

	public static void findMaxMin() throws RowsExceededException, WriteException {
		if ((salarySum <= 60000 && consSum <= -4 && otherConditions == true)
				|| (salarySum <= 60000 && valueSum >= 390 && otherConditions == true)) {
			for (Player ele : roster) {
				Label label = new Label(column, row, ele.name);
				shh.addCell(label);
				column++;
			}
			shh.addCell(new Label(column, row, Integer.toString(salarySum)));
			shh.addCell(new Label(++column, row, Double.toString(valueSum)));
			shh.addCell(new Label(++column, row, Double.toString(consSum)));

			column = 0;
			row++;
		}
	}
	
	public static void generateLineups(double max, double min) throws RowsExceededException,
			WriteException {
		// max of valueSum and min of consSum
		if (salarySum <= 60000 && salarySum >= 58000 && otherConditions == true) {

			if (valueSum <= (max - 8) && valueSum >= (max - 9)
					|| valueSum <= (max - 10.4) && valueSum >= (max - 11.4)
					|| valueSum <= (max - 13) && valueSum >= (max - 14)
					|| valueSum <= (max - 15.1) && valueSum >= (max - 16.2)
					|| valueSum <= (max - 18) && valueSum >= (max - 19)
					|| valueSum <= (max - 19.5) && valueSum >= (max - 21)
					|| valueSum <= (max - 21.5) && valueSum >= (max - 22.5)
					|| valueSum <= (max - 22.9) && valueSum >= (max - 24)
					|| valueSum <= (max - 24.5) && valueSum >= (max - 25.5)
					|| valueSum <= (max - 26.5) && valueSum >= (max - 27)
					|| valueSum <= (max - 27.39) && valueSum >= (max - 27.8)
					|| valueSum <= (max - 29) && valueSum >= (max - 30)
					|| valueSum <= (max - 30.6) && valueSum >= (max - 32.4)
					|| valueSum <= (max - 34.4) && valueSum >= (max - 35.4)
					|| valueSum <= (max - 37.6) && valueSum >= (max - 38.1)
					|| valueSum <= (max - 39) && valueSum >= (max - 40)
					|| valueSum <= (max - 41) && valueSum >= (max - 43)
					|| valueSum <= (max - 44.5) && valueSum >= (max - 45.1)
					|| valueSum <= (max - 45.6) && valueSum >= (max - 46.7)
					|| valueSum <= (max - 47) && valueSum >= (max - 48)
					|| valueSum <= (max - 49) && valueSum >= (max - 50.6)
					|| valueSum <= (max - 51.3) && valueSum >= (max - 51.8)
					|| valueSum <= (max - 52.6) && valueSum >= (max - 53.4)
					|| valueSum <= (max - 54.4) && valueSum >= (max - 55.2)
					|| valueSum <= (max - 61) && valueSum >= (max - 62)
					|| valueSum <= (max - 66.5) && valueSum >= (max - 67)
					|| valueSum <= (max - 67.5) && valueSum >= (max - 68)
					|| valueSum <= (max - 69.5) && valueSum >= (max - 70))

				if (consSum >= (min + 1.35) && consSum <= (min + 1.45)
						|| consSum >= (min + 1.8) && consSum <= (min + 1.9)
						|| consSum >= (min + 2.05) && consSum <= (min + 2.15)
						|| consSum >= (min + 2.25) && consSum <= (min + 2.35)
						|| consSum >= (min + 2.45) && consSum <= (min + 2.55)
						|| consSum >= (min + 2.65) && consSum <= (min + 2.75)
						|| consSum >= (min + 2.9) && consSum <= (min + 3)
						|| consSum >= (min + 3.1) && consSum <= (min + 3.20)
						|| consSum >= (min + 3.31) && consSum <= (min + 3.36)
						|| consSum >= (min + 3.45) && consSum <= (min + 3.5)
						|| consSum >= (min + 3.58) && consSum <= (min + 3.63)
						|| consSum >= (min + 3.69) && consSum <= (min + 3.75)
						|| consSum >= (min + 3.9) && consSum <= (min + 4.05)
						|| consSum >= (min + 4.1) && consSum <= (min + 4.15)
						|| consSum >= (min + 4.23) && consSum <= (min + 4.271)
						|| consSum >= (min + 4.32) && consSum <= (min + 4.37)
						|| consSum >= (min + 4.44) && consSum <= (min + 4.55)
						|| consSum >= (min + 4.65) && consSum <= (min + 4.75)
						|| consSum >= (min + 4.82) && consSum <= (min + 4.87)
						|| consSum >= (min + 4.95) && consSum <= (min + 5)
						|| consSum >= (min + 5.05) && consSum <= (min + 5.25)
						|| consSum >= (min + 5.35) && consSum <= (min + 5.45)
						|| consSum >= (min + 5.5) && consSum <= (min + 5.65)
						|| consSum >= (min + 5.7) && consSum <= (min + 5.75)
						|| consSum >= (min + 5.82) && consSum <= (min + 5.87)
						|| consSum >= (min + 6) && consSum <= (min + 6.05)
						|| consSum >= (min + 6.35) && consSum <= (min + 6.4)
						|| consSum >= (min + 6.48) && consSum <= (min + 6.53)
						|| consSum >= (min + 6.97) && consSum <= (min + 7.02)
						|| consSum >= (min + 7.09) && consSum <= (min + 7.14)
						|| consSum >= (min + 7.25) && consSum <= (min + 7.3)
						|| consSum >= (min + 7.35) && consSum <= (min + 7.47)
						|| consSum >= (min + 7.75) && consSum <= (min + 7.8)
						|| consSum >= (min + 8) && consSum <= (min + 8.05)) {
			


						for (Player ele : roster) {
							Label label = new Label(column, row, ele.name);
							shh.addCell(label);
							column++;
						}
						shh.addCell(new Label(column, row, Integer
								.toString(salarySum)));
						shh.addCell(new Label(++column, row, Double
								.toString(valueSum)));
						shh.addCell(new Label(++column, row, Double
								.toString(consSum)));

						column = 0;
						row++;

				}
		}
	}
	
}
