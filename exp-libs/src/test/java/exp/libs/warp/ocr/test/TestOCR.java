package exp.libs.warp.ocr.test;

import java.io.File;

import exp.libs.warp.ocr.OCR;

public class TestOCR {

	public static void main(String[] args) {
		String ocrDir = "./conf/ocr/tesseract";
		OCR ocr = new OCR(ocrDir);
		
		String imgDir = "./src/test/resources/exp/libs/warp/ocr/test";
		File dir = new File(imgDir);
		File[] imgs = dir.listFiles();
		for(File img : imgs) {
			String name = img.getName();
			String content = ocr.recognizeText(img.getAbsolutePath());
			System.out.println(name + " : " + content);
		}
	}
	
}
