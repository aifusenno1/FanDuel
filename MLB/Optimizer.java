import java.io.*;
import java.text.DecimalFormat;
import java.util.*;

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

public class Optimizer {

	public static int salarySum, column, row;
	public static double valueSum, scoreSum;
	public static boolean otherConditions;
	public static WritableSheet shh;
	public static ArrayList<Player> roster;

	public static void main(String[] args) throws BiffException, IOException,
			RowsExceededException, WriteException {

		Workbook wb = Workbook.getWorkbook(new File("roster.xls"));
		WritableWorkbook lineup = Workbook.createWorkbook(new File(
				"5-26 top.xls"));
		shh = lineup.createSheet("sheet1", 0);

		Sheet sh = wb.getSheet("5-26");

		ArrayList<Player> p = new ArrayList<>();
		ArrayList<Player> c = new ArrayList<>();
		ArrayList<Player> b1 = new ArrayList<>();
		ArrayList<Player> b2 = new ArrayList<>();
		ArrayList<Player> b3 = new ArrayList<>();
		ArrayList<Player> ss = new ArrayList<>();
		ArrayList<Player> of = new ArrayList<>();

		for (int r = 1; r < sh.getRows(); r++) {
			String name = sh.getCell(0, r).getContents();
			String pos = sh.getCell(1, r).getContents();
			int salary = Integer.parseInt(sh.getCell(2, r).getContents());
			String team = sh.getCell(3, r).getContents();
			double value = Double.parseDouble(sh.getCell(4, r).getContents());
			double score = Double.parseDouble(sh.getCell(5, r).getContents());

			Player player = new Player(name, pos, salary, team, value, score);
			if (player.pos.equals("P"))
				p.add(player);
			if (player.pos.equals("C"))
				c.add(player);
			if (player.pos.equals("1B"))
				b1.add(player);
			if (player.pos.equals("2B"))
				b2.add(player);
			if (player.pos.equals("3B"))
				b3.add(player);
			if (player.pos.equals("SS"))
				ss.add(player);
			if (player.pos.equals("OF"))
				of.add(player);
		}

		shh.addCell(new Label(0, 0, "P"));
		shh.addCell(new Label(1, 0, "C"));
		shh.addCell(new Label(2, 0, "1B"));
		shh.addCell(new Label(3, 0, "2B"));
		shh.addCell(new Label(4, 0, "3B"));
		shh.addCell(new Label(5, 0, "SS"));
		shh.addCell(new Label(6, 0, "OF"));
		shh.addCell(new Label(7, 0, "OF"));
		shh.addCell(new Label(8, 0, "OF"));
		shh.addCell(new Label(9, 0, "Salary"));
		shh.addCell(new Label(10, 0, "Value"));
		shh.addCell(new Label(11, 0, "Score"));

		column = 0;
		row = 1;
		for (int p1 = 0; p1 < p.size(); p1++)
			for (int c1 = 0; c1 < c.size(); c1++)
				for (int b11 = 0; b11 < b1.size(); b11++)
					for (int b21 = 0; b21 < b2.size(); b21++)
						for (int b31 = 0; b31 < b3.size(); b31++)
							for (int ss1 = 0; ss1 < ss.size(); ss1++)
								for (int of1 = 0; of1 < of.size() - 2; of1++)
									for (int of2 = of1 + 1; of2 < of.size() - 1; of2++)
										for (int of3 = of2 + 1; of3 < of.size(); of3++) {
										//	System.out.println(p1);
											roster = new ArrayList<>();
											roster.add(p.get(p1));
											roster.add(c.get(c1));
											roster.add(b1.get(b11));
											roster.add(b2.get(b21));
											roster.add(b3.get(b31));
											roster.add(ss.get(ss1));
											roster.add(of.get(of1));
											roster.add(of.get(of2));
											roster.add(of.get(of3));
											
											salarySum = 0;
											valueSum = 0;
											scoreSum = 0;
											TreeMap teamNum = new TreeMap();
											
											for (Player ele : roster) {
												salarySum += ele.salary;
												valueSum += ele.value;
												scoreSum += ele.score;
												
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
											scoreSum = Double.parseDouble(String.format("%.2f", scoreSum));
											
											if (salarySum<=35000 && salarySum >= 34500 && scoreSum >=90 && otherConditions == true) {
												for (Player ele : roster) {
													Label label = new Label(column, row, ele.name);
													shh.addCell(label);
													column++;
												}
												shh.addCell(new Label(column, row, Integer.toString(salarySum)));
												shh.addCell(new Label(++column, row, Double.toString(valueSum)));
												shh.addCell(new Label(++column, row, Double.toString(scoreSum)));
												//System.out.println(scoreSum);
												column = 0;
												row++;
												
												for (Player ele : roster) {
													Label label = new Label(column, row, String.format("%.2f", ele.value));
													shh.addCell(label);
													column++;
												}
												
												column = 0;
												row++;
											}
										}
		lineup.write();
		lineup.close();
		wb.close();
	}
}