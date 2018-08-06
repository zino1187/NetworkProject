package net;

import java.awt.BorderLayout;
import java.awt.Checkbox;
import java.awt.CheckboxGroup;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class MyServer extends JFrame{
	//���� ���� ��ü��...
	JTextField t_ip, t_port;
	JButton bt_open, bt_save;
	JPanel p_north;
	
	//���Ϳ��� 
	JTextArea area;
	JScrollPane scroll;
	
	//���ʿ��� 
	JButton bt_start;
	CheckboxGroup g;
	Checkbox ch_on, ch_off;
	JPanel p_south;
	
	//��Ʈ���� 
	//�ڹ��� �⺻ �ڷ��� ���� 1:1 ���� Ŭ������ �����Ǵµ�
	//�� Ŭ�������� ������ Wrapper Ŭ������ �θ���..	
	//�ַ� �⺻�ڷ������� --> ��ü�ڷ������� ��ȯ 
	int port=7777; 
	
	//������ �������� ���ڱ�� ����ó���� ��Ʈ�� �غ�
	BufferedReader buffr;
	FileReader reader;
	
	BufferedWriter buffw;
	FileWriter writer;
	
	String arduinoFile="E:/incubator/NetworkProject/arduino/client/client.ino";
	
	public MyServer() {
		t_ip=new JTextField(IPManager.getIp(),15);
		t_port = new JTextField(Integer.toString(port),5);
		bt_open = new JButton("����ġ ����");
		bt_save = new JButton("����ġ ����");
		p_north = new JPanel();
		
		p_north.add(t_ip);
		p_north.add(t_port);
		p_north.add(bt_open);
		p_north.add(bt_save);
		
		//�������� ���ʿ� p_north �� ����
		add(p_north, BorderLayout.NORTH);
		
		area = new JTextArea();
		scroll = new JScrollPane(area);
		add(scroll);
		area.setFont(new Font("Verdana",Font.BOLD,14));
		
		//���ʿ��� 
		bt_start = new JButton("��������");
		g = new CheckboxGroup();
		ch_on = new Checkbox("on",g, false);
		ch_off = new Checkbox("off",g, true);
		p_south = new JPanel();
		p_south.add(bt_start);
		p_south.add(ch_on);
		p_south.add(ch_off);
		add(p_south, BorderLayout.SOUTH);
		
		//�̺��� �ҽ��� �����ʿ��� ����!!
		bt_open.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				openFile();
			}		
		});
		
		//���� ��ư�� �����ʿ��� ���� 
		bt_save.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				saveFile();
			}
		});
		
		//������ �Ӽ� ����
		setVisible(true);
		setSize(600, 600);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
	}
	
	//�Ƶ��̳� ����ġ ���� ����
	public void openFile() {
		try {
			reader = new FileReader(arduinoFile);
			buffr = new BufferedReader(reader);
			
			//area �о���� ���ε��� �������!!
			while(true){
				String data=buffr.readLine();
				if(data==null)break;
				area.append(data+"\n");
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}finally {
			if(reader!=null) {
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if(buffr!=null){
				try {
					buffr.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
	}
	
	//�����ִ� �Ƶ��̳� ����ġ ������ ������ area�� ��������
	//��ü����!!
	public void saveFile() {
		try {
			//FileWriter ������, ��� ������ ����� ���Ϸ� ����
			writer = new FileWriter(arduinoFile);		
			buffw=new BufferedWriter(writer);
			
			buffw.write(area.getText());
			//��½�Ʈ�� �迭�� ��½� flush() �� ȣ���Ͽ� 
			//��Ʈ���� ������ �Ѵ�..
			buffw.flush();
			JOptionPane.showMessageDialog(this, "�������� �Ϸ�");
		} catch (IOException e) {
			e.printStackTrace();
		}finally {
			if(writer!=null) {
				try {
					writer.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if(buffw!=null) {
				try {
					buffw.close();
				} catch (IOException e) {
						e.printStackTrace();
				}
			}
		}
		
		
	}
	
	public static void main(String[] args) {
		new MyServer();

	}


}




