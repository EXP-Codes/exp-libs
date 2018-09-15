package exp.libs.warp.ui.cpt.win;


/**
 * <PRE>
 * swing主窗口
 * </PRE>
 * <br/><B>PROJECT : </B> exp-libs
 * <br/><B>SUPPORT : </B> <a href="http://www.exp-blog.com" target="_blank">www.exp-blog.com</a> 
 * @version   2017-08-17
 * @author    EXP: 272629724@qq.com
 * @since     jdk版本：jdk1.6
 */
@SuppressWarnings("serial")
public abstract class MainWindow extends _SwingWindow {

	protected MainWindow() {
		super("MainWindow");
	}
	
	protected MainWindow(String name) {
		super(name);
	}
	
	protected MainWindow(String name, int width, int height) {
		super(name, width, height);
	}
	
	protected MainWindow(String name, int width, int height, boolean relative) {
		super(name, width, height, relative);
	}
	
	protected MainWindow(String name, int width, int height, boolean relative, Object... args) {
		super(name, width, height, relative, args);
	}
	
	@Override
	protected final boolean isMainWindow() {
		return true;
	}
	
}
