package net;

import java.awt.BorderLayout;
import java.awt.Checkbox;
import java.awt.CheckboxGroup;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class MyServer extends JFrame implements ItemListener{
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
	
	//��Ʈ��ũ�� �ʿ��� �͵�...
	ServerSocket server; //��ȭ��X, ������ ������
	Thread serverThread; //���� ������ ������!!
	//��� ���ξ����� ��� ��� ���¿� �����ش�...
	
	//��ȭ�� ��Ʈ�� 
	BufferedWriter bw;
	
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
		
		//üũ�ڽ��� �����ʿ��� ���� 
		ch_on.addItemListener(this);
		ch_off.addItemListener(this);
		
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
		
		//������ư�� ������ ���� 
		bt_start.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				serverRun();
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
	
	//���� ���� �޼���!!
	//���ξ������ ���� ���ѷ�����, �����¿� ������ �ؼ���
	//�ȵ�..����?? ���ξ������ GUI ���α׷����� ȭ�鿡
	//�������� �׷��� ó����, �̺�Ʈ ���� ���� ó���ϱ� ������
	//���ѷ����� �����¿� ������ ���α׷��� ��Ұ�..
	public void serverRun() {
		//Anonymous 
		serverThread=new Thread() {
			public void run() {
				try {
					server = new ServerSocket(port);//��������
					area.append("Server starting...\n");
					
					//�����ڰ� ���������� ������...
					Socket client=server.accept();
					area.append("client detected..\n");
					
					//������ ������ �̿��Ͽ� Ŭ���̾�Ʈ�� ��ȭ
					//�ְ� �ޱ�!!
					bw=new BufferedWriter(new OutputStreamWriter(client.getOutputStream()));
					
					
					
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			
		};
		
		// Runnable���·� �д�
		serverThread.start();
		
	}
	
	public static void main(String[] args) {
		new MyServer();

	}

	@Override
	public void itemStateChanged(ItemEvent e) {
		Object obj=e.getSource();
		if(obj==ch_on) {
			try {
				bw.write("on\n");
				bw.flush();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}else if(obj==ch_off) {
			try {
				bw.write("off\n");
				bw.flush();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		
		
	}


}




