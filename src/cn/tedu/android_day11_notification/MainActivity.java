package cn.tedu.android_day11_notification;

import android.os.Bundle;
import android.app.Activity;
import android.app.Notification;
import android.app.Notification.Builder;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.RemoteViews;

public class MainActivity extends Activity {

	private static final int NOTIFICATION_ID = 101;
	private static final int NOTIFICATION_ID2 = 102;
	private static final int NOTIFICATION_ID3 = 103;
	private static final int NOTIFICATION_ID4 = 104;
	private MyReceiver receiver;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		//ע��㲥������
		receiver = new MyReceiver();
		IntentFilter filter = new IntentFilter();
		filter.addAction("ACTION_CLICK_BUTTON2");
		registerReceiver(receiver, filter);
	}

	@Override
	protected void onDestroy() {
		this.unregisterReceiver(receiver);
		super.onDestroy();
	}
	
	/**
	 * ����
	 */
	public void doClick(View view){
		switch (view.getId()) {
		case R.id.button5: // �Զ���֪ͨ
			sendNotification4();
			break;
		case R.id.button4: // ���н�������֪ͨ
			sendNotification3();
			break;
		case R.id.button3: //�޸�֪ͨ������
			sendNotification2();
			break;
		case R.id.button2: //���֪ͨ
			clearNotification();
			break;
		case R.id.button1: //����֪ͨ
			sendNotification();
			break;
		}
	}
	
	/**
	 * button5  �Զ���֪ͨ
	 */
	private void sendNotification4() {
		NotificationManager manager = (NotificationManager)
				getSystemService(NOTIFICATION_SERVICE);
		//����Notification����
		Builder builder = new Builder(this);
		builder.setSmallIcon(R.drawable.ic_launcher)
			.setTicker("������Ϣ");
		//�Զ���RemoteViews����
		RemoteViews views = new RemoteViews(this.getPackageName(), R.layout.notification_main);
		//��views�еİ�ť��ӵ����ͼ
		Intent i1 = new Intent(this, MainActivity.class);
		PendingIntent pi1 = PendingIntent.getActivity(this, 0, i1, PendingIntent.FLAG_UPDATE_CURRENT);
		views.setOnClickPendingIntent(R.id.button1, pi1);
		//����ڶ�����ť���͹㲥
		Intent i2 = new Intent("ACTION_CLICK_BUTTON2");
		PendingIntent pi2 = PendingIntent.getBroadcast(this, 0, i2, PendingIntent.FLAG_UPDATE_CURRENT);
		views.setOnClickPendingIntent(R.id.button2, pi2);
		
		builder.setContent(views);
		Notification n = builder.build();
		//����֪ͨ
		manager.notify(NOTIFICATION_ID4, n);
	}

	// button3 ���н�������֪ͨ
	private void sendNotification3() {
		final NotificationManager manager = (NotificationManager)
				getSystemService(NOTIFICATION_SERVICE);
		//����Notification����
		final Builder builder = new Builder(this);
		builder.setTicker("���ֿ�ʼ����")
			.setContentTitle("��������")
			.setSmallIcon(R.drawable.ic_launcher)
			.setProgress(100, 0, true);
		Notification n = builder.build();
		//����֪ͨ
		manager.notify(NOTIFICATION_ID3, n);
		//ģ������  ÿ��1������10%
		new Thread(){
			public void run() {
				for(int i=1; i<=10; i++){
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					//����֪ͨ
					builder.setProgress(100, i*10, false);
					manager.notify(NOTIFICATION_ID3, builder.build());
				}
				//�������
				builder.setProgress(0, 0, false)
					.setContentText("�����Ѿ��������"); //ɾ��������
				manager.notify(NOTIFICATION_ID3, builder.build());
			}
		}.start();
	}

	//button3 �޸�֪ͨ
	private int number = 0;
	private void sendNotification2() {
		//1. Manager
		NotificationManager manager = (NotificationManager)
				getSystemService(NOTIFICATION_SERVICE);
		//2. Notification
		Builder builder = new Builder(this);
		builder.setTicker("��ʼ����....")
			.setContentTitle("�ļ�����")
			.setAutoCancel(true) //
			.setContentText("��ǰ���ȣ�"+number+"%")
			.setSmallIcon(R.drawable.ic_launcher);
		
		//��֪ͨ��ӵ����ͼ��
		Intent intent = new Intent(this, MainActivity.class);
		PendingIntent pi = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
		builder.setContentIntent(pi);
		
		Notification n = builder.build();
		//3. notify()
		manager.cancel(NOTIFICATION_ID2);
		manager.notify(NOTIFICATION_ID2, n);
		number += 10;
	}

	//button2 ���֪ͨ
	private void clearNotification() {
		//1. NotificationManager
		NotificationManager manager = (NotificationManager) 
				getSystemService(NOTIFICATION_SERVICE);
		//2. manager.cancel()
		manager.cancel(NOTIFICATION_ID);
	}

	/**
	 * button1
	 */
	private void sendNotification() {
		//1   NotificationManager
		NotificationManager manager =(NotificationManager) 
				getSystemService(
				Context.NOTIFICATION_SERVICE);
		//2  ����Notification
		Builder builder = new Builder(this);
		builder.setContentInfo("ContentInfo")
			.setContentText("ContentText")
			.setContentTitle("ContentTitle")
			.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher))
			.setSmallIcon(R.drawable.ic_launcher)
			.setSubText("SubText")
			.setTicker("״̬���еĹ�����Ϣ.....")
			.setWhen(System.currentTimeMillis());
		//֪ͨ��פ֪ͨ��
		builder.setOngoing(true);
		Notification n = builder.build();
		//1> n.flags = Notification.FLAG_NO_CLEAR;
		//3.  manager.notify()
		manager.notify(NOTIFICATION_ID, n);
	}

	
	class MyReceiver extends BroadcastReceiver{
		@Override
		public void onReceive(Context context, Intent intent) {
			Log.i("info", "����˵ڶ�����ť��"+intent.getAction());
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
