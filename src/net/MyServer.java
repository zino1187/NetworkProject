package net;

import java.awt.BorderLayout;
import java.awt.Checkbox;
import java.awt.CheckboxGroup;

import javax.swing.JButton;
import javax.swing.JFrame;
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
	
	public MyServer() {
		t_ip=new JTextField(15);
		t_port = new JTextField(5);
		bt_open = new JButton("����ġ ����");
		bt_save = new JButton("����ġ ����");
		p_north = new JPanel();
		
		p_north.add(t_ip);
		p_north.add(t_port);
		p_north.add(bt_open);
		p_north.add(bt_save);
		
		//�������� ���ʿ� p_north �� ����
		add(p_north, BorderLayout.NORTH);
		
		//������ �Ӽ� ����
		setVisible(true);
		setSize(600, 600);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
	}
	
	public static void main(String[] args) {
		new MyServer();

	}

}




