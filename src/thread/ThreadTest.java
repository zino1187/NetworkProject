/*
 �������? 
  - �ϳ��� ���μ������� ���������� ����Ǵ�
    ���� �������
    
  �������-
    �����ڴ� ���ϴ� ������ �������� run�� �����
    �� jvm�� ���� �����Ǹ鼭 �������ش�..
 */
package thread;

public class ThreadTest {

	Thread t1;
	Thread t2;
	public ThreadTest() {
		//�����͸� Ŭ������? 
		//���밡�ɼ��� �������� �ڵ带 ����
		//.java�� �����ʿ䰡 �ֳ�?....
		//�̷���, ���� �������� �ڹ�Ŭ��������
		//�̸����� Ŭ������ ������ �� �ִ�..
		//���� ���� ����? �̺�Ʈ ������ �̺�Ʈ������
		//�����Ҷ� ���� ��..
		t1=new Thread() {
			public void run() {
				while(true) {
					System.out.println("��");	
					try {
						Thread.sleep(500);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		};
		
		t2=new Thread() {
			public void run() {
				while(true) {
					System.out.println("��");	
					try {
						Thread.sleep(500);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		};

		//jvm���� �ñ���!!
		t1.start();//������ �վƱ� ���...
		t2.start();
	}
	
	public static void main(String[] args) {	
		new ThreadTest();
	}
}


