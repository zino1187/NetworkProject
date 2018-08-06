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
	//북쪽 관련 객체들...
	JTextField t_ip, t_port;
	JButton bt_open, bt_save;
	JPanel p_north;
	
	//센터영역 
	JTextArea area;
	JScrollPane scroll;
	
	//남쪽영역 
	JButton bt_start;
	CheckboxGroup g;
	Checkbox ch_on, ch_off;
	JPanel p_south;
	
	//포트설정 
	//자바의 기본 자료형 마다 1:1 대응 클래스가 지원되는데
	//이 클래스들을 가리켜 Wrapper 클래스라 부른다..	
	//주로 기본자료형에서 --> 객체자료형으로 변환 
	int port=7777; 
	
	//파일을 열기위한 문자기반 버퍼처리된 스트림 준비
	BufferedReader buffr;
	FileReader reader;
	
	BufferedWriter buffw;
	FileWriter writer;
	
	String arduinoFile="E:/incubator/NetworkProject/arduino/client/client.ino";
	
	//네트워크에 필요한 것들...
	ServerSocket server; //대화용X, 접속자 감지용
	Thread serverThread; //서버 가동용 쓰레드!!
	//얘는 메인쓰레드 대신 대기 상태에 빠져준다...
	
	//대화용 스트림 
	BufferedWriter bw;
	
	public MyServer() {
		t_ip=new JTextField(IPManager.getIp(),15);
		t_port = new JTextField(Integer.toString(port),5);
		bt_open = new JButton("스케치 열기");
		bt_save = new JButton("스케치 저장");
		p_north = new JPanel();
		
		p_north.add(t_ip);
		p_north.add(t_port);
		p_north.add(bt_open);
		p_north.add(bt_save);
		
		//프레임의 북쪽에 p_north 를 부착
		add(p_north, BorderLayout.NORTH);
		
		area = new JTextArea();
		scroll = new JScrollPane(area);
		add(scroll);
		area.setFont(new Font("Verdana",Font.BOLD,14));
		
		//남쪽영역 
		bt_start = new JButton("서버가동");
		g = new CheckboxGroup();
		ch_on = new Checkbox("on",g, false);
		ch_off = new Checkbox("off",g, true);
		p_south = new JPanel();
		p_south.add(bt_start);
		p_south.add(ch_on);
		p_south.add(ch_off);
		add(p_south, BorderLayout.SOUTH);
		
		//체크박스와 리스너와의 연결 
		ch_on.addItemListener(this);
		ch_off.addItemListener(this);
		
		//이벤스 소스와 리스너와의 연결!!
		bt_open.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				openFile();
			}		
		});
		
		//저장 버튼과 리스너와의 연결 
		bt_save.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				saveFile();
			}
		});
		
		//가동버튼과 리스너 연결 
		bt_start.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				serverRun();
			}
		});
		
		
		//윈도우 속성 설정
		setVisible(true);
		setSize(600, 600);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
	}
	
	//아두이노 스케치 파일 열기
	public void openFile() {
		try {
			reader = new FileReader(arduinoFile);
			buffr = new BufferedReader(reader);
			
			//area 읽어들인 라인들을 출력하자!!
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
	
	//열려있는 아두이노 스케치 파일의 내용을 area의 내용으로
	//대체하자!!
	public void saveFile() {
		try {
			//FileWriter 생성시, 대상 파일을 비워진 파일로 생성
			writer = new FileWriter(arduinoFile);		
			buffw=new BufferedWriter(writer);
			
			buffw.write(area.getText());
			//출력스트림 계열은 출력시 flush() 를 호출하여 
			//스트림을 비워줘야 한다..
			buffw.flush();
			JOptionPane.showMessageDialog(this, "파일저장 완료");
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
	
	//서버 가동 메서드!!
	//메인쓰레드는 절대 무한루프나, 대기상태에 빠지게 해서는
	//안됨..이유?? 메인쓰레드는 GUI 프로그램에서 화면에
	//보여지는 그래픽 처리나, 이벤트 감지 등을 처리하기 때문에
	//무한루프나 대기상태에 빠지면 프로그램이 운영불가..
	public void serverRun() {
		//Anonymous 
		serverThread=new Thread() {
			public void run() {
				try {
					server = new ServerSocket(port);//서버가동
					area.append("Server starting...\n");
					
					//접속자가 있을때까지 대기상태...
					Socket client=server.accept();
					area.append("client detected..\n");
					
					//생성된 소켓을 이용하여 클라이언트와 대화
					//주고 받기!!
					bw=new BufferedWriter(new OutputStreamWriter(client.getOutputStream()));
					
					
					
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			
		};
		
		// Runnable상태로 둔다
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




