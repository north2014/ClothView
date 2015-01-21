package com.example.particle;

import java.util.ArrayList;

/***
 * ������Ӷ��󼯺�
 * 
 * @author zhangjia
 * 
 */
public class ParticleSet {
	ArrayList<Particle> particals;

	public ParticleSet() {
		super();
		particals = new ArrayList<Particle>();
	}


	/***
	 * �������
	 * 
	 * @param count
	 *            ����
	 * @param startTime
	 *            ��ʼʱ��
	 * @param spacing
	 */

	public void addPartical(int count, int window_Width, int space) {
		int spacing = space;
		
		for (int i = 0; i < count; i++) {

			double vertical_v = 0, horizontal_v = 0;
			int Start = (window_Width - ParticleView.width * spacing) / 2;
			int w = i % ParticleView.width;// ����
			int h = i / ParticleView.width;// ����
			// Y����
			int startX = w * spacing + Start;
			// Y����
			int startY = h * spacing + Start;

			Particle partical = new Particle(vertical_v,
					horizontal_v, startX, startY);

			// ��ÿ�����ӷֱ����ӵ��������ϲ�������ϣ�����еĻ���
			
			
			if (w != 0) {// �������еĻ������ӵ���ߵ�����
				partical.attach(this.particals.get(this.particals.size() - 1));
			}

			if (h == 0)// ����ǵ�һ�ŵĻ������������λ����
			{
				partical.pin(partical.startX, partical.startY);
			} else {
				partical.pin(0, 0);
			}

			if (h != 0)// ����ϱ��еĻ������ӵ���ߵ�����
				partical.attach(this.particals.get(w + (h - 1)
						* ParticleView.width));

			particals.add(partical);
		}
	}
}
