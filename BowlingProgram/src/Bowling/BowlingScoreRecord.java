package Bowling;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONObject;

public class BowlingScoreRecord {

	public void setNowScore(UserDTO userDto) {
		userDto.setPrintTemp("출력 = " + userDto.getScore() + "[" + userDto.getFrame() + ", " + userDto.getBall() + ", ");

		if (userDto.getFrame() < UserDTO.LAST_FRAME) {
			if (userDto.getPin() == UserDTO.EMPTY_PIN) {
				if (userDto.getBall() == UserDTO.FIRST_BALL) {
					if (userDto.getNowStatus("Turkey")) {
						userDto.setTotalTemp(userDto.getTotal() + "+10+10+");
						userDto.setNowStatus("Turkey");
					} else if (userDto.getNowStatus("Double")) {
						userDto.setTotalTemp(userDto.getTotalTemp() + "10+");
						userDto.setNowStatus("Turkey");
					} else if (userDto.getNowStatus("Strike")) {
						userDto.setTotalTemp(userDto.getTotalTemp() + "10+");
						userDto.setNowStatus("Double");
					} else {
						userDto.setTotalTemp(userDto.getTotal() + "+");
						userDto.setNowStatus("Strike");
					}
					userDto.setPrintTemp(userDto.getPrintTemp() + userDto.getTotalTemp());
				} else if (userDto.getBall() == UserDTO.SECOND_BALL) {
					userDto.setNowStatus("Spare");
					userDto.setPrintTemp(userDto.getPrintTemp() + userDto.getTotal() + "+");
				}
			} else {
				if (userDto.isOneOfStrikeTurkeyDouble() && userDto.getBall() == UserDTO.FIRST_BALL) {
					userDto.setPrintTemp(userDto.getPrintTemp() + userDto.getTotalTemp() + userDto.getScore() + "+");
				} else
					userDto.setPrintTemp(userDto.getPrintTemp() + userDto.getTotal());
			}
		} else if (userDto.getFrame() == UserDTO.LAST_FRAME) {
			if (!userDto.isLastBall()) {
				if (userDto.isOneOfStrikeTurkeyDouble()) {
					if (userDto.getBall() == UserDTO.FIRST_BALL) {
						if (userDto.getNowStatus("Turkey"))
							userDto.setTotalTemp(userDto.getTotal() + "+10+" + userDto.getScore() + "+");
						else
							userDto.setTotalTemp(userDto.getTotal() + userDto.getScore() + "+");
					}
				}
			}

			if (userDto.isOneOfStrikeTurkeyDouble())
				userDto.setPrintTemp(userDto.getPrintTemp() + userDto.getTotalTemp() + userDto.getScore() + "+");
			else
				userDto.setPrintTemp(userDto.getPrintTemp() + userDto.getTotal());

		}

		if (userDto.isLastBall())
			userDto.setPrintTemp("출력 = " + userDto.getScore() + "[10, 3, " + userDto.getTotal());

	}

	public void show(int[][] result, int score, int total) {
		System.out.println(
				"##################################################기록판###############################################");
		for (int i = 1; i <= result.length / 2; i++) {
			System.out.print("\t|\t" + i);
		}
		System.out.println("\t|");

		for (int i = 0; i < result.length / 2; i++) {
			if (result[i][0] == UserDTO.STRIKE_SPARE)
				System.out.print("\t|\tX, ");
			else if ((result[i][0] + result[i][1]) == UserDTO.STRIKE_SPARE) {
				System.out.print("\t|\t" + result[i][0] + ", /");
			} else
				System.out.print("\t|\t" + result[i][0] + ", " + result[i][1] + "");
		}
		System.out.println("\t|");

		System.out.println(
				"------------------------------------------------------------------------------------------------------");
		for (int i = result.length / 2 + 1; i <= result.length; i++) {
			System.out.print("\t|\t" + i);
		}
		System.out.println("\t|");

		for (int i = result.length / 2; i < result.length - 1; i++) {
			if (result[i][0] == UserDTO.STRIKE_SPARE)
				System.out.print("\t|\tX, ");
			else if ((result[i][0] + result[i][1]) == UserDTO.STRIKE_SPARE) {
				System.out.print("\t|\t" + result[i][0] + ", /");
			} else
				System.out.print("\t|\t" + result[i][0] + ", " + result[i][1] + "");
		}

		if (result[9][0] + result[9][1] == 20)
			System.out.print("\t|\tX, X, ");
		else if (result[9][0] + result[9][1] == UserDTO.STRIKE_SPARE)
			System.out.print("\t|\t" + result[9][0] + ", /, ");
		else if (result[9][0] + result[9][1] > UserDTO.STRIKE_SPARE)
			System.out.print("\t|\tX, " + result[9][1] + ", ");
		else
			System.out.println("\t|\t" + result[9][0] + ", " + result[9][1] + ", 0" + "\t|");

		if (result[9][0] + result[9][1] >= UserDTO.STRIKE_SPARE) {
			if (score == 10)
				System.out.println("X\t|");
			else if ((result[9][0] + result[9][1]) != 10 && (score + result[9][1]) == UserDTO.STRIKE_SPARE)
				System.out.println("/\t|");
			else
				System.out.println(score + "\t|");
		}

		System.out.println(
				"##################################################기록판###############################################");
		System.out.println("최종점수 :" + total);
	}

	public void nowScore(String printTemp) {
		System.out.println(printTemp + "]");
	}

	public void backup(List<UserDTO> playersDto, File file) throws IOException {
		JSONObject data;
		Map<String, Boolean> nowStatus;
		ArrayList<ArrayList<Integer>> result;
		ArrayList<Integer> child;
		JSONObject player = new JSONObject();
		BufferedWriter writer = new BufferedWriter(new FileWriter(file)); 
		
		for (UserDTO userDto : playersDto) {
			nowStatus = new HashMap<String, Boolean>();
			result = new ArrayList<ArrayList<Integer>>();
			child = new ArrayList<Integer>();
			data = new JSONObject();
			
			nowStatus.put("Turkey", userDto.getNowStatus("Turkey"));
			nowStatus.put("Strike", userDto.getNowStatus("Strike"));
			nowStatus.put("Spare", userDto.getNowStatus("Spare"));
			nowStatus.put("Double", userDto.getNowStatus("Double"));

			for (int[] result_1 : userDto.getResult()) {
				for (int i = 0; i < result_1.length; i++) {
					child.add(result_1[i]);
				}
				result.add(child);
				child = new ArrayList<Integer>();
			}
			
			data.put("ball", userDto.getBall());
			data.put("ballCnt", userDto.getBallCnt());
			data.put("frame", userDto.getFrame());
			data.put("nowStatus", nowStatus);
			data.put("nScore", userDto.getnScore());
			data.put("pin", userDto.getPin());
			data.put("printTemp", userDto.getPrintTemp());
			data.put("result", result);
			data.put("total", userDto.getTotal());
			data.put("totalTemp", userDto.getTotalTemp());
			
			player.put(userDto.getPlayerNumber(), data);
		}
		writer.write(player.toJSONString());
		writer.flush();
		writer.close();
		

	}
}
