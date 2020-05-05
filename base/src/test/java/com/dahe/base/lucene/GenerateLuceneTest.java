package com.dahe.base.lucene;

import com.dahe.base.lucene.generate.service.impl.CameraIndexDealer;

public class GenerateLuceneTest {

	public static void main(String args[]) {
		CameraIndexDealer camera = new CameraIndexDealer();
		camera.index();
	}
}
