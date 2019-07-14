package exp.libs.utils.img;

import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import exp.libs.envm.FileType;

/**
 * <PRE>
 * 图像处理工具
 * </PRE>
 * <br/><B>PROJECT : </B> exp-libs
 * <br/><B>SUPPORT : </B> <a href="http://www.exp-blog.com" target="_blank">www.exp-blog.com</a> 
 * @version   2017-12-17
 * @author    EXP: ***REMOVED***@qq.com
 * @since     jdk版本：jdk1.6
 */
public class ImageUtils {

	/** 日志器 */
	private final static Logger log = LoggerFactory.getLogger(ImageUtils.class);
	
	/** javax.imageio.ImageIO 的黑色RGB(应该是反码, 理论值应为0) */
	public final static int RGB_BLACK = -16777216;
	
	/** javax.imageio.ImageIO 的白色RGB值(应该是反码, 理论值应为16777215) */
	public final static int RGB_WHITE = -1;
	
	/** 私有化构造函数 */
	private ImageUtils() {}
	
	/**
	 * 构造默认图像
	 * @return 二值化1x1图像
	 */
	private static BufferedImage createDefaultImage() {
		return new BufferedImage(1, 1, BufferedImage.TYPE_BYTE_BINARY);
	}
	
	/**
	 * 读取图像
	 * @param imgPath 图像路径
	 * @return 图像缓存数据
	 */
	public static BufferedImage read(String imgPath) {
		BufferedImage image = null;
		try {
			image = ImageIO.read(new File(imgPath));
		} catch (Exception e) {
			log.error("读取图片失败: {}", imgPath, e);
		}
		return image;
	}
	
	/**
	 * 保存图像
	 * @param image 图像缓存数据
	 * @param savePath 图像保存位置
	 * @param imageType 图像类型
	 * @return true:保存成功; false:保存失败
	 */
	public static boolean write(BufferedImage image, String savePath, FileType imageType) {
		boolean isOk = false;
		try {
			isOk = ImageIO.write(image, imageType.NAME, new File(savePath));
		} catch (Exception e) {
			log.error("保存图片失败: {}", savePath, e);
		}
		return isOk;
	}
	
	/**
	 * 图像二值化(背景色为白色，前景色为黑色)
	 * @param image 原图
	 * @return 二值化图像
	 */
	public static BufferedImage toBinary(BufferedImage image) {
		return toBinary(image, false);
	}
	
	/**
	 * 图像二值化(背景色为白色，前景色为黑色)
	 * @param image 原图
	 * @param nWoB 非白即黑模式（此模式下只要不是白色均视为黑色）; 
	 *             反之会根据二值化算法会把阀值范围内的颜色认为是黑色或白色
	 * @return 二值化图像
	 */
	public static BufferedImage toBinary(BufferedImage image, boolean nWoB) {
		if(image == null) {
			return createDefaultImage();
		}
		
		final int W = image.getWidth();
		final int H = image.getHeight();
		BufferedImage binaryImage = image;
		try {

			// 把原图转换为二值化图像
			int whiteCnt = 0; // 白色像素计数器
			int blackCnt = 0; // 黑色像素极速器
			binaryImage = new BufferedImage(W, H,
					BufferedImage.TYPE_BYTE_BINARY); // 可选择模式: 二值化/灰度化
			for (int i = 0; i < W; i++) {
				for (int j = 0; j < H; j++) {
					int RGB = image.getRGB(i, j);
					RGB = (nWoB && RGB != RGB_WHITE ? RGB_BLACK : RGB);
					binaryImage.setRGB(i, j, RGB); // 根据图像模式, RGB会自动转换为黑/白

					RGB = binaryImage.getRGB(i, j);
					if (RGB == RGB_WHITE) {
						whiteCnt++;
					} else {
						blackCnt++;
					}
				}
			}

			// 默认白色为背景色, 黑色为前景色, 当背景色像素小于前景色时， 则对图像取反
			if (whiteCnt < blackCnt) {
				for (int i = 0; i < W; i++) {
					for (int j = 0; j < H; j++) {
						int RGB = binaryImage.getRGB(i, j);
						if (RGB == RGB_WHITE) {
							binaryImage.setRGB(i, j, RGB_BLACK);
						} else {
							binaryImage.setRGB(i, j, RGB_WHITE);
						}
					}
				}
			}
		} catch (Exception e) {
			log.error("二值化图片失败", e);
		}
		return binaryImage;
	}
	
	/**
	 * 把图像转换为01像素矩阵
	 * @param image 图像
	 * @return 01像素矩阵
	 */
	public static int[][] toBinaryMatrix(BufferedImage image) {
		if(image == null) {
			return new int[0][0];
		}
		
		final int W = image.getWidth();
		final int H = image.getHeight();
		int[][] matrix = new int[H][W];
		for (int i = 0; i < W; i++) {
			for (int j = 0; j < H; j++) {
				int RGB = image.getRGB(i, j);
				matrix[j][i] = (RGB != RGB_WHITE ? 1 : 0);
			}
		}
		return matrix;
	}
	
	/**
	 * 垂直切割图像
	 * @param image 原图像
	 * @param left 左起始边界索引, 取值范围：[0, image.Width)
	 * @return 切割子图, 切割范围为 [left, right)
	 */
	public static BufferedImage cutVertical(BufferedImage image, int left) {
		return cutVertical(image, left, -1);
	}
	
	/**
	 * 垂直切割图像
	 * @param image 原图像
	 * @param left 左起始边界索引, 取值范围：[0, image.Width)
	 * @param offset 切割宽度(-1表示切到末尾)
	 * @return 切割子图, 切割范围为 [left, left + offset)
	 */
	public static BufferedImage cutVertical(BufferedImage image, int left, int offset) {
		if(image == null) {
			return createDefaultImage();
		}
		
		final int H = image.getHeight();
		final int W = image.getWidth();
		
		left = (left < 0 || left >= W ? 0 : left);
		offset = (offset <= 0 || left + offset > W ? W - left : offset);
		int right = left + offset;
		
		BufferedImage subImage = new BufferedImage(offset, H, image.getType());
		for (int i = left; i < right; i++) {
			for (int j = 0; j < H; j++) {
				int RGB = image.getRGB(i, j);
				subImage.setRGB(i - left, j, RGB);
			}
		}
		return subImage;
	}
	
	/**
	 * 水平切割图像
	 * @param image 原图像数据缓存
	 * @param top 上起始边界索引, 取值范围：[0, image.Height)
	 * @return 切割子图, 切割范围为 [top, bottom)
	 */
	public static BufferedImage cutHorizontal(BufferedImage image, int top) {
		return cutHorizontal(image, top, -1);
	}
	
	/**
	 * 水平切割图像
	 * @param image 原图像数据缓存
	 * @param top 上起始边界索引, 取值范围：[0, image.Height)
	 * @param offset 切割高度(-1表示切到末尾)
	 * @return 切割子图, 切割范围为 [top, top + offset)
	 */
	public static BufferedImage cutHorizontal(BufferedImage image, int top, int offset) {
		if(image == null) {
			return createDefaultImage();
		}
		
		final int H = image.getHeight();
		final int W = image.getWidth();
		
		top = (top < 0 || top >= H ? 0 : top);
		offset = (offset <= 0 || top + offset > H ? H - top : offset);
		int buttom = top + offset;
		
		BufferedImage subImage = new BufferedImage(W, offset, image.getType());
		for (int i = 0; i < W; i++) {
			for (int j = top; j < buttom; j++) {
				int RGB = image.getRGB(i, j);
				subImage.setRGB(i, j - top, RGB);
			}
		}
		return subImage;
	}
	
}
