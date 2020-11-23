import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class LineEditor {
	public void show(Map<Integer, String> line_text) {
		System.out.println("===================================================");
		for (int i = 1; i <= line_text.size(); i++) {
			System.out.printf("%4d : %s\n", i, line_text.get(i));
		}
		System.out.println("===================================================");
	}

	public void help() {
		System.out.println("OPEN [���ϸ�] : ������ �о� �޸𸮿� �����Ѵ�. (��, OPEN file.txt)\n"
				+ "SHOW : �޸��� ������ ���ι�ȣ�� �Բ� ǥ���Ѵ�. (��, 0002:����)\n" + "ADD : �Է¹��� ������ �������� �߰��Ѵ�. (ADD ? ���� �Է�)\n"
				+ "INS [��ȣ] : �Է¹��� ������ ���ι�ȣ �տ� �߰��Ѵ�. (INS 2 ? �Է�)\n" + "EDIT [��ȣ] : ���ι�ȣ�� ������ ǥ���ϰ� ������ ������ �Է��Ѵ�.\n"
				+ "DEL [��ȣ] : �ش� ���ι�ȣ�� ������ �����Ѵ�.\n" + "SAVE : ������ ������ ���Ͽ� �����Ѵ�.\n"
				+ "EXIT : ������ ������ ���� Y�� �����ϸ� �����Ѵ�.\n");
	}

	public boolean isNumber(String input) {
		char temp = ' ';

		for (int i = 0; i < input.length(); i++) {
			temp = input.charAt(i);

			if (!('0' <= temp && temp <= '9')) {
				return false;
			}
		}
		return true;
	}

	public void delete(Map<Integer, String> line_text, int index) {
		if (index != 1) {
			for (int i = index+1; i <= line_text.size(); i++) {
				line_text.replace(i - 1, line_text.get(i));
			}
		}else{
			for (int i = 2; i <= line_text.size(); i++) {
				line_text.replace(i-1, line_text.get(i));
			}
		}
		line_text.remove(line_text.size());
	}

	public static void main(String[] args) throws IOException {
		LineEditor editor = new LineEditor();
		Scanner input = new Scanner(System.in);

		// ���� �̸� �޾ƿ���
		String command = "";
		String text = "";

		List<String> commandCheck;
		Map<Integer, String> line_text = new HashMap<Integer, String>();
		BufferedWriter writer = null;
		BufferedReader reader = null;
		File file = null;

		int lineNum = 1;
		boolean openCheck = false;
		boolean isWriting = true;
		boolean isSave = true; // ��������� true �ƴϸ� false

		System.out.println("===================================================");
		System.out.println("�ų��� ���� ������ ��ɾ �Է����ּ���!(HELP��ɾ �Է��ϼ���)");

		while (true) {
			System.out.print("CMD : ");
			command = input.nextLine();
			commandCheck = Arrays.asList(command.split(" "));

			if (commandCheck.size() == 1) {
				if ((commandCheck.get(0).equals("SAVE") || commandCheck.get(0).equals("save")) && openCheck) {
					// �������� ������ �����ϴ��� Ȯ��
					if (file.canRead() && file.canWrite() && file.exists()) {
						writer = new BufferedWriter(new FileWriter(file));
						for (int i = 1; i <= line_text.size(); i++) {
							writer.write(line_text.get(i) + "\n");
						}
						writer.flush();
						writer.close();
						isSave = true;
						System.out.println("=======================���峡=======================");
					} else {
						System.out.println("������ �������� �ʽ��ϴ�.");
					}

				} else if ((commandCheck.get(0).equals("SHOW") || commandCheck.get(0).equals("show")) && openCheck) {
					editor.show(line_text);

				} else if ((commandCheck.get(0).equals("ADD") || commandCheck.get(0).equals("add")) && openCheck) {
					System.out.println("=======================�ۼ���=======================");
					System.out.print(">>");
					text = input.nextLine();
					line_text.put(lineNum, text);
					System.out.println("=======================�ۼ���=======================");
					lineNum++;
					isSave = false;

				} else if (commandCheck.get(0).equals("HELP") || commandCheck.get(0).equals("help")) {
					editor.help();
				} else if (commandCheck.get(0).equals("EXIT") || commandCheck.get(0).equals("exit")) {
					System.out.print("���� �����Ͻðڽ��ϱ�?(y) : ");

					command = input.nextLine();
					if (command.equals("y")) {
						if (isSave) {
							if (!isWriting) {
								reader.close();
							}
							System.out.println("�����͸� �����մϴ�.");
							break;
						} else {
							System.out.print("����� ������ �ֽ��ϴ� �׷��� �����Ͻðڽ��ϱ�(y) :");
							command = input.nextLine();
							if (command.equals("y")) {
								reader.close();
								System.out.println("�����͸� �����մϴ�.");
								break;
							} else {
								System.out.println("�߸� �Է��ϼ̽��ϴ�!");
								continue;
							}
						}
					} else {
						System.out.println("�߸� �Է��ϼ̽��ϴ�!");
						continue;
					}
				} else {
					System.out.println("�߸��� ����Դϴ�. HELP�� �Է��Ͽ� ������ Ȯ���� �ּ���.");
					continue;
				}

			} else if (commandCheck.size() == 2) {
				if (commandCheck.get(0).equals("OPEN") || commandCheck.get(0).equals("open")) {
					if (isWriting) {
						file = new File(commandCheck.get(1));
						// ������ �ִ��� �а� ���� �ִ��� Ȯ���ϱ�
						if (file.canRead() && file.canWrite() && file.exists()) {
							reader = new BufferedReader(new FileReader(file));
							while ((text = reader.readLine()) != null) {
								line_text.put(lineNum, text);
								System.out.println(text);
								lineNum++;
								isWriting = false;
							}

							openCheck = true;
							System.out.println("=======================�б⼺��=======================");

						} else {
							System.out.println("������ �������� �ʰų� ���� �� ���� �����Դϴ�.");
							continue;
						}
					} else {
						System.out.println("�̹� �ۼ����� ������ �����մϴ�. ������ �ٽ� ������ �ּ���.");
					}

				} else if (commandCheck.get(0).equals("INS") || commandCheck.get(0).equals("ins")) {
					if (editor.isNumber(commandCheck.get(1))) {
						if (line_text.containsKey(Integer.parseInt(commandCheck.get(1)))) {
							System.out.println("=======================������=======================");
							System.out.println("������");
							System.out.println(line_text.get(Integer.parseInt(commandCheck.get(1))));
							System.out.print(">>");
							text = input.nextLine();
							line_text.put(Integer.parseInt(commandCheck.get(1)),
									text + line_text.get(Integer.parseInt(commandCheck.get(1))));
							System.out.println("������");
							System.out.println(line_text.get(Integer.parseInt(commandCheck.get(1))));
							System.out.println("=======================������=======================");
							isSave = false;
						} else {
							System.out.println("�������� �ʴ� ��ȣ�Դϴ�");
							continue;
						}
					} else {
						System.out.println("�������� �ʴ� ��ȣ�Դϴ�");
						continue;
					}
				} else if (commandCheck.get(0).equals("EDIT") || commandCheck.get(0).equals("edit")) {
					if (line_text.containsKey(Integer.parseInt(commandCheck.get(1)))) {
						System.out.println("=======================������=======================");
						System.out.println("������");
						System.out.println(line_text.get(Integer.parseInt(commandCheck.get(1))));
						System.out.print(">>");
						text = input.nextLine();
						line_text.replace(Integer.parseInt(commandCheck.get(1)),text);
						System.out.println("������");
						System.out.println(line_text.get(Integer.parseInt(commandCheck.get(1))));
						System.out.println("=======================������=======================");
						
						isSave = false;
					} else {
						System.out.println("�������� �ʴ� ��ȣ�Դϴ�");
						continue;
					}
					isSave = false;
					if (line_text.containsKey(Integer.parseInt(commandCheck.get(1)))) {
						editor.delete(line_text, Integer.parseInt(commandCheck.get(1)));
						isSave = false;
						System.out.println("=======================������=======================");
					} else {
						System.out.println("�������� �ʴ� ��ȣ�Դϴ�");
						continue;
					}
				} else {
					System.out.println("�߸��� ����Դϴ�. HELP�� �Է��Ͽ� ������ Ȯ���� �ּ���.");
					continue;
				}
			} else {
				// ��ɾ� ���� ����
				System.out.println("�߸��� �����Դϴ�. HELP�� �Է��Ͽ� ������ Ȯ���� �ּ���.");
				continue;
			}
		}

	}

}
