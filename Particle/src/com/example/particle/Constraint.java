package com.example.particle;


import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

/**
 * Լ��
 * 
 * @author Administrator
 * 
 */
public class Constraint {

	private Particle p1;
	private Particle p2;
	private int length;

	public Constraint(Particle p1, Particle p2) {
		this.p1 = p1;
		this.p2 = p2;
		this.length = ParticleView.spacing;
	}

	/**
	 * �ṹԼ��
	 */
	public void resolve() {
		float diff_x = this.p1.x - this.p2.x, // x����
		diff_y = this.p1.y - this.p2.y; // y����

		float dist = (float) Math.sqrt(diff_x * diff_x + diff_y * diff_y), // ����λ��
		diff = (this.length - dist) / dist;

		if (dist > ParticleView.tear_distance)// ������
			this.p1.remove_constraint(this);
		
		// ���������Ӹ��ƶ�һ��·�̣�ʹ�����ƶ���ľ���Ϊ��ֹ����
		float px = (float) (diff_x * diff * 0.5);
		float py = (float) (diff_y * diff * 0.5);

		//this.p1.x += px;
		this.p1.y += py;
		//this.p2.x -= px;
		this.p2.y -= py;
	}

	public void draw(Canvas mCanvas) {
		// ����
		Paint p = new Paint();
		p.setColor(Color.RED);
		p.setStrokeWidth(2);
		mCanvas.drawLine(this.p1.x, this.p1.y, this.p2.x, this.p2.y, p);
	}
}
