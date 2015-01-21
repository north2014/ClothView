package com.example.particle;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

/**
 * 
 * ģ�Ⲽ�ϵ�����ṹ
 * 
 * @author ��С��
 * @version �㷨������http://html5gamedev.org/?p=1997
 */

// �̳���SurfaceView��ʵ��SurfaceHolder.Callback�ӿڵ���
public class ParticleView extends SurfaceView implements SurfaceHolder.Callback {

	DrawThread drawThread; // ��̨ˢ����Ļ�߳�
	ParticleSet particleSet; // ParticleSet��������

	String fps = "FPS:N/A"; // ����֡�����ַ���
	static int width = 8;// ������
	int height = 12;// ����߶�
	int window_Width;// Canvas���
	int window_Height;// Canvas�߶�

	// �߽�
	static int boundsx, boundsy;// ��ȡ� �߶�
	static int spacing = 50;// ���Ӽ�ļ��
	public static float tear_distance = 90;// ���ϵľ���
	static int gravity = 10;// ����
	int TouchCricle = spacing / 2 - 10;// �������С

	// ����������ʼ����Ҫ��Ա����
	public ParticleView(Context context, int window_Width, int window_Height) {
		super(context); // ���ø��๹����
		this.window_Width = window_Width;
		this.window_Height = window_Height;

		boundsx = window_Width - 1;
		boundsy = window_Height - 1;

		this.getHolder().addCallback(this); // ���Callback�ӿ�
		drawThread = new DrawThread(this, getHolder()); // ����DrawThread����

		particleSet = new ParticleSet(); // ����ParticleSet����
		particleSet.addPartical(width * height, window_Width, spacing);// ÿ�����5��
	}

	/**
	 * ���״̬
	 * 
	 * @author Administrator
	 * 
	 */
	public static class mouse {
		static boolean down = false;// �Ƿ���
		static float x = 0;// ��ǰxλ��
		static float y = 0;// ��ǰyλ��
		static float px = 0;// ֮ǰxλ��
		static float py = 0;// ֮ǰyλ��
	}

	// ������������Ļ
	public void doDraw(Canvas canvas) {
		canvas.drawColor(Color.BLACK); // ����
		ArrayList<Particle> particleSet = this.particleSet.particals; // ���ParticleSet�����е����Ӽ��϶���
		Paint paint = new Paint(); // �������ʶ���

		this.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View arg0, MotionEvent event) {
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:// ��ָ����ʱ
					mouse.px = mouse.x;
					mouse.py = mouse.y;
					mouse.x = event.getX();
					mouse.y = event.getY();
					mouse.down = true;
					break;
				case MotionEvent.ACTION_MOVE:// ��ָ�ƶ�
					mouse.px = mouse.x;
					mouse.py = mouse.y;
					mouse.x = (int) event.getX();
					mouse.y = (int) event.getY();
					break;
				case MotionEvent.ACTION_UP:// ��ָ̧��
					mouse.down = false;
					break;
				default:
					break;

				}
				return true;
			}
		});

		for (int i = 0; i < particleSet.size(); i++) { // �������Ӽ��ϣ�����ÿ������
			Particle partical = particleSet.get(i);
			if (mouse.down) {
				int diff_x = (int) (partical.x - mouse.x), // xλ��
				diff_y = (int) (partical.y - mouse.y), // yλ��
				dist = (int) Math.sqrt(diff_x * diff_x + diff_y * diff_y); // ����λ��
				if (dist < TouchCricle) {// ���ڻ������ƾ���
					partical.startX = (int) (partical.x - (mouse.x - mouse.px) * 1.8);
					partical.startY = (int) (partical.y - (mouse.y - mouse.py) * 1.8);
				}
			}
			partical.resolve_constraints(canvas);// �ֽ�ÿ������
			partical.add_force(0, gravity);// �������
			partical.update((float) 0.5);// �����Ȼ����

		}

		paint.setColor(Color.WHITE); // ���û�����ɫ
		paint.setTextSize(18); // �������ִ�С
		paint.setAntiAlias(true); // ���ÿ����
		canvas.drawText(fps, 15, 15, paint);// ����֡�����ַ���
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {// ��дsurfaceChanged����
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {// ��дsurfaceCreated����
		if (!drawThread.isAlive()) { // ���DrawThreadû������������������߳�
			drawThread.start();
		}
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {// ��дsurfaceDestroyed����
		drawThread.flag = false; // ֹͣ�̵߳�ִ��
		drawThread = null; // ��dtָ��Ķ�������Ϊ����
	}

	/***
	 * �����ػ���Ļ�ͼ���֡��
	 * 
	 * @author zhangjia
	 * 
	 */
	public class DrawThread extends Thread {
		ParticleView particalView;// �Զ���View
		SurfaceHolder surfaceHolder;
		boolean flag = false;// �̱߳�ʶ
		int sleepSpan = 15;// �߳�����
		long start = System.nanoTime();// ��ʵʱ�䣬���ڼ���֡����
		int count = 0;// ����֡��

		public DrawThread(ParticleView particalView, SurfaceHolder surfaceHolder) {
			super();
			this.particalView = particalView;
			this.surfaceHolder = surfaceHolder;
			this.flag = true;
		}

		@Override
		public void run() {
			Canvas canvas = null;
			while (flag) {
				try {
					canvas = surfaceHolder.lockCanvas();// ��ȡcanvas.
					synchronized (surfaceHolder) {
						particalView.doDraw(canvas);// ���л���ballView.

					}
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					if (canvas != null) {
						surfaceHolder.unlockCanvasAndPost(canvas);// ����
					}
				}
				this.count++;
				if (count == 20) { // �������20֡
					count = 0; // ��ռ�����
					long tempStamp = System.nanoTime();// ��ȡ��ǰʱ��
					long span = tempStamp - start; // ��ȡʱ����
					start = tempStamp; // Ϊstart���¸�ֵ
					double fps = Math.round(100000000000.0 / span * 20) / 100.0;// ����֡����
					particalView.fps = "FPS:" + fps;// ���������֡�������õ�BallView����Ӧ�ַ���������
				}
				try {
					Thread.sleep(sleepSpan); // �߳�����һ��ʱ��
				} catch (Exception e) {
					e.printStackTrace(); // ���񲢴�ӡ�쳣
				}
			}
		}

	}
}