package exp.libs.warp.ui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Toolkit;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JRootPane;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

import exp.libs.utils.io.FileUtils;
import exp.libs.utils.other.StrUtils;
import exp.libs.warp.ui.layout.VFlowLayout;

/**
 * <PRE>
 * swing组件工具
 * </PRE>
 * <br/><B>PROJECT : </B> exp-libs
 * <br/><B>SUPPORT : </B> <a href="http://www.exp-blog.com" target="_blank">www.exp-blog.com</a> 
 * @version   2015-12-27
 * @author    EXP: ***REMOVED***@qq.com
 * @since     jdk版本：jdk1.6
 */
public class SwingUtils {

	/** 滚动条模式：自动(当内容越界时自动出现滚动条) */
	public static final int AUTO_SCROLL_MODE = 0;
	
	/** 滚动条模式：显式(无论内容是否越界均显示滚动条) */
	public static final int SHOW_SCROLL_MODE = 1;
	
	/** 滚动条模式：无滚动条 */
	public static final int HIDE_SCROLL_MODE = -1;
	
	/** 私有化构造函数 */
	protected SwingUtils() {}
	
	/**
	 * 设置窗口无边框（需要在显示窗口前调用此方法）
	 * @param frame 窗口组件
	 */
	public static void setNoFrame(JFrame frame) {
		if(frame == null) {
			return;
		}
		
		frame.setUndecorated(true);
		frame.getRootPane().setWindowDecorationStyle(JRootPane.NONE);
	}
	
	/**
	 * 为指定组件添加自动滚动条（当文本超过宽/高边界时自动出现水平/垂直滚动条）
	 * @param component 组件
	 * @return 已添加滚动条的组件
	 */
	public static JScrollPane addScroll(Component component) {
		return addAutoScroll(component);
	}
	
	/**
	 * 为指定组件添加自动滚动条（当内容超过宽/高边界时自动出现水平/垂直滚动条）
	 * @param component 组件
	 * @return 已添加滚动条的组件
	 */
	public static JScrollPane addAutoScroll(Component component) {
		return addScroll(component, AUTO_SCROLL_MODE);
	}
	
	/**
	 * 为指定组件添加显式滚动条（总是显示水平/垂直滚动条）
	 * @param component 组件
	 * @return 已添加滚动条的组件
	 */
	public static JScrollPane addShowScroll(Component component) {
		return addScroll(component, SHOW_SCROLL_MODE);
	}
	
	/**
	 * 为指定组件添加隐式滚动条（总是隐藏水平/垂直滚动条）
	 * @param component 组件
	 * @return 已添加滚动条的组件
	 */
	public static JScrollPane addHideScroll(Component component) {
		return addScroll(component, HIDE_SCROLL_MODE);
	}
	
	/**
	 * 为指定组件添加滚动条
	 * @param component 组件
	 * @param mode 
	 * 		0:AUTO_SCROLL_MODE, 自动模式（当内容超过宽/高边界时自动出现水平/垂直滚动条）.
	 * 		1:SHOW_SCROLL_MODE, 显式模式（总是出现水平/垂直滚动条）.
	 * 		-1:HIDE_SCROLL_MODE, 隐式模式（总是隐藏水平/垂直滚动条）
	 * @return 已添加滚动条的组件
	 */
	public static JScrollPane addScroll(Component component, int mode) {
		component = (component == null ? new JTextArea() : component);
		JScrollPane scroll = new JScrollPane(component); 
		mode = (mode < AUTO_SCROLL_MODE ? HIDE_SCROLL_MODE : mode);
		mode = (mode > AUTO_SCROLL_MODE ? SHOW_SCROLL_MODE : mode);
		
		// 分别设置水平和垂直滚动条总是出现 
		if(mode == SHOW_SCROLL_MODE) {
			scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS); 
			scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
			
		//分别设置水平和垂直滚动条总是隐藏
		} else if(mode == HIDE_SCROLL_MODE) {
			scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER); 
			scroll.setVerticalScrollBarPolicy( JScrollPane.VERTICAL_SCROLLBAR_NEVER);
			
		//分别设置水平和垂直滚动条自动出现 
		} else {
			scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED); 
			scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED); 
		}
		return scroll;
	}
	
	/**
	 * 令滚动面板的垂直和水平滚动条均自动滚动到末尾
	 * @param scrollPanel 滚动面板
	 */
	public static void toEnd(JScrollPane scrollPanel) {
		if(scrollPanel == null) {
			return;
		}
		
		toEnd(scrollPanel.getVerticalScrollBar());
		toEnd(scrollPanel.getHorizontalScrollBar());
	}
	
	/**
	 * 令滚动面板的滚动条自动滚动到末尾
	 * @param scrollPanel 滚动面板
	 * @param vertical 是否为垂直方向的滚动条
	 */
	public static void toEnd(JScrollPane scrollPanel, boolean vertical) {
		if(scrollPanel == null) {
			return;
		}
		
		JScrollBar scrollBar = (vertical ? 
					scrollPanel.getVerticalScrollBar() : 
					scrollPanel.getHorizontalScrollBar());
		toEnd(scrollBar);
	}
	
	/**
	 * 令滚动条自动滚动到末尾
	 * @param scrollBar 垂直或水平滚动条
	 */
	public static void toEnd(JScrollBar scrollBar) {
		if(scrollBar != null) {
			scrollBar.setValue(scrollBar.getMaximum());
		}
	}
	
	/**
	 * 令文本区的光标移动到最后.
	 *   每次更新文本区的内容后调用此方法, 会有文本区自动滚动到末端的效果.
	 * @param textArea 文本区
	 */
	public static void toEnd(JTextArea textArea) {
		if(textArea != null) {
			textArea.setCaretPosition(textArea.getText().length());
		}
	}
	
	/**
	 * 获取水平切割面板（可左右拖拉切割线）
	 * @param left 左侧组件
	 * @param right 右侧组件
	 * @return 水平切割面板
	 */
	public static JSplitPane getHSplitPane(Component left, Component right) {
		return getHSplitPane(left, right, 0);
	}
	
	/**
	 * 获取水平切割面板（可左右拖拉切割线）
	 * @param left 左侧组件
	 * @param right 右侧组件
	 * @param divide 面板呈现时的上下切割比例, 取值范围(0.0, 1.0), 默认0.5,
	 *               只有在面板<b>可见时</b>此参数才有效(亦即只有在窗体view之后才能调用此方法)
	 * @return 水平切割面板
	 */
	public static JSplitPane getHSplitPane(Component left, Component right, double divide) {
		JSplitPane splitPanel = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
		splitPanel.setLeftComponent(left);
		splitPanel.setRightComponent(right);
		return setDivider(splitPanel, divide);
	}
	
	/**
	 * 获取垂直切割面板（可上下拖拉切割线）
	 * @param top 顶部组件
	 * @param bottom 底部组件
	 * @return 垂直切割面板
	 */
	public static JSplitPane getVSplitPane(Component top, Component bottom) {
		return getVSplitPane(top, bottom, 0);
	}
	
	/**
	 * 获取垂直切割面板（可上下拖拉切割线）
	 * @param top 顶部组件
	 * @param bottom 底部组件
	 * @param divide 面板呈现时的上下切割比例, 取值范围(0.0, 1.0), 默认0.5,
	 *               只有在面板<b>可见时</b>此参数才有效(亦即只有在窗体view之后才能调用此方法)
	 * @return 垂直切割面板
	 */
	public static JSplitPane getVSplitPane(Component top, Component bottom, double divide) {
		JSplitPane splitPanel = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
		splitPanel.setLeftComponent(top);
		splitPanel.setRightComponent(bottom);
		return setDivider(splitPanel, divide);
	}
	
	/**
	 * <pre>
	 * 设置切割面板在呈现时的切割比例.
	 *  只有在面板<b>可见时</b>此方法才有效(亦即只有在窗体view之后才能调用此方法).
	 * </pre>
	 * @param splitPanel 切割面板
	 * @param divide 面板呈现时的切割比例, 取值范围(0.0, 1.0), 默认0.5
	 * @return 切割面板
	 */
	public static JSplitPane setDivider(JSplitPane splitPanel, double divide) {
		if(splitPanel != null && divide > 0.0 && divide < 1.0) {
			splitPanel.setDividerLocation(divide);
		}
		return splitPanel;
	}
	
	/**
	 * 为组件添加一个面板.
	 * 	布局风格为BorderLayout, 组件的布局位置为CENTER
	 * @param component 组件
	 * @return 装入组件的面板
	 */
	public static JPanel addPanel(Component component) {
		JPanel panel = new JPanel(new BorderLayout());
		if(component != null) {
			panel.add(component, BorderLayout.CENTER);
		}
		return panel;
	}
	
	/**
	 * 获取配对组件面板（提示组件+输入组件）
	 *  布局风格为BorderLayout: 提示组件WEST, 输入组件CENTER
	 * @param label 提示组件的提示信息 ( 自动添加 [...] 包围 )
	 * @return JLabel + JTextFields
	 */
	public static JPanel getPairsPanel(String label) {
		return getPairsPanel(label, "");
	}
	
	/**
	 * 获取配对组件面板（提示组件+输入组件）
	 * 	布局风格为BorderLayout: 提示组件WEST, 输入组件CENTER
	 * @param label 提示组件的提示信息 ( 自动添加 [...] 包围 )
	 * @param textField 输入组件的默认输入值
	 * @return JLabel + JTextFields
	 */
	public static JPanel getPairsPanel(String label, String textField) {
		return getPairsPanel(label, new JTextField(textField));
	}
	
	/**
	 * 获取配对组件面板（提示组件+输入组件）
	 * 	布局风格为BorderLayout: 提示组件WEST, 输入组件CENTER
	 * @param label 提示组件的提示信息 ( 自动添加 [...] 包围 )
	 * @param component 输入组件
	 * @return JLabel + 自定义的输入组件
	 */
	public static JPanel getPairsPanel(String label, Component component) {
		return getPairsPanel(new JLabel(StrUtils.concat("  [", label, "]:  ")), component);
	}
	
	/**
	 * 获取配对组件面板（提示组件+输入组件）
	 * 	布局风格为BorderLayout: 提示组件WEST, 输入组件CENTER
	 * @param label 提示组件
	 * @param component 输入组件
	 * @return 自定义的提示组件 + 自定义的输入组件
	 */
	public static JPanel getPairsPanel(Component label, Component component) {
		return getWBorderPanel(component, label);
	}
	
	/**
	 * 获取水平流式布局面板
	 * @param components 添加到该面板的组件集合
	 * @return 水平流式布局面板
	 */
	public static JPanel getHFlowPanel(Component... components) {
		return getHFlowPanel(FlowLayout.CENTER, components);
	}
	
	/**
	 * 获取水平流式布局面板
	 * @param align 对齐模式(默认居中): FlowLayout.CENTER|LEFT|RIGHT
	 * @param components 添加到该面板的组件集合
	 * @return 水平流式布局面板
	 */
	public static JPanel getHFlowPanel(int align, Component... components) {
		JPanel panel = new JPanel(new FlowLayout(align));
		if(components != null) {
			for(Component component : components) {
				panel.add(component);
			}
		}
		return panel;
	}
	
	/**
	 * 获取垂直流式布局面板
	 * @param components 添加到该面板的组件集合
	 * @return 垂直流式布局面板
	 */
	public static JPanel getVFlowPanel(Component... components) {
		return getVFlowPanel(VFlowLayout.TOP, components);
	}
	
	/**
	 * 获取垂直流式布局面板
	 * @param align 对齐模式(默认置顶): VFlowLayout.TOP|MIDDLE|BOTTOM
	 * @param components 添加到该面板的组件集合
	 * @return 垂直流式布局面板
	 */
	public static JPanel getVFlowPanel(int align, Component... components) {
		JPanel panel = new JPanel(new VFlowLayout(align));
		if(components != null) {
			for(Component component : components) {
				panel.add(component);
			}
		}
		return panel;
	}
	
	/**
	 * 获取水平表格布局面板(1行N列)
	 * @param components 添加到该面板的组件集合
	 * @return 水平表格布局面板(1行N列)
	 */
	public static JPanel getHGridPanel(Component... components) {
		int num = (components == null ? 1 : components.length);
		JPanel panel = new JPanel(new GridLayout(1, num));
		if(components != null) {
			for(int i = 0; i < num; i++) {
				panel.add(components[i], i);
			}
		}
		return panel;
	}
	
	/**
	 * 获取垂直表格布局面板(N行1列)
	 * @param components 添加到该面板的组件集合
	 * @return 垂直表格布局面板(N行1列)
	 */
	public static JPanel getVGridPanel(Component... components) {
		int num = (components == null ? 1 : components.length);
		JPanel panel = new JPanel(new GridLayout(num, 1));
		if(components != null) {
			for(int i = 0; i < num; i++) {
				panel.add(components[i], i);
			}
		}
		return panel;
	}
	
	/**
	 * 获取 [中心-北] 边框布局面板
	 * @param center 期望置放到中心的组件
	 * @param north 期望置放到北方的组件
	 * @return [中心-北] 边框布局面板
	 */
	public static JPanel getNBorderPanel(Component center, Component north) {
		JPanel panel = new JPanel(new BorderLayout());
		panel.add(north, BorderLayout.NORTH);
		panel.add(center, BorderLayout.CENTER);
		return panel;
	}
	
	/**
	 * 获取 [中心-南] 边框布局面板
	 * @param center 期望置放到中心的组件
	 * @param south 期望置放到南方的组件
	 * @return [中心-南] 边框布局面板
	 */
	public static JPanel getSBorderPanel(Component center, Component south) {
		JPanel panel = new JPanel(new BorderLayout());
		panel.add(south, BorderLayout.SOUTH);
		panel.add(center, BorderLayout.CENTER);
		return panel;
	}
	
	/**
	 * 获取 [中心-西] 边框布局面板
	 * @param center 期望置放到中心的组件
	 * @param west 期望置放到西方的组件
	 * @return [中心-西] 边框布局面板
	 */
	public static JPanel getWBorderPanel(Component center, Component west) {
		JPanel panel = new JPanel(new BorderLayout());
		panel.add(west, BorderLayout.WEST);
		panel.add(center, BorderLayout.CENTER);
		return panel;
	}
	
	/**
	 * 获取 [中心-东] 边框布局面板
	 * @param center 期望置放到中心的组件
	 * @param east 期望置放到东方的组件
	 * @return [中心-东] 边框布局面板
	 */
	public static JPanel getEBorderPanel(Component center, Component east) {
		JPanel panel = new JPanel(new BorderLayout());
		panel.add(east, BorderLayout.EAST);
		panel.add(center, BorderLayout.CENTER);
		return panel;
	}
	
	/**
	 * 获取 [西-中心-东] 边框布局面板
	 * @param west 期望置放到西方的组件
	 * @param center 期望置放到中心的组件
	 * @param east 期望置放到东方的组件
	 * @return [西-中心-东] 边框布局面板
	 */
	public static JPanel getWEBorderPanel(Component west, Component center, Component east) {
		JPanel panel = new JPanel(new BorderLayout());
		panel.add(west, BorderLayout.WEST);
		panel.add(center, BorderLayout.CENTER);
		panel.add(east, BorderLayout.EAST);
		return panel;
	}
	
	/**
	 * 获取 [北-中心-南] 边框布局面板
	 * @param north 期望置放到北方的组件
	 * @param center 期望置放到中心的组件
	 * @param south 期望置放到南方的组件
	 * @return [北-中心-南] 边框布局面板
	 */
	public static JPanel getNSBorderPanel(Component north, Component center, Component south) {
		JPanel panel = new JPanel(new BorderLayout());
		panel.add(north, BorderLayout.NORTH);
		panel.add(center, BorderLayout.CENTER);
		panel.add(south, BorderLayout.SOUTH);
		return panel;
	}
	
	/**
	 * 为组件添加边框布局面板，并将其置放到中心
	 * @param center 期望置放到中心的组件
	 * @return [中心] 边框布局面板
	 */
	public static JPanel addCenterPanel(Component center) {
		JPanel panel = new JPanel(new BorderLayout());
		panel.add(center, BorderLayout.CENTER);
		return panel;
	}
	
	/**
	 * 获取下拉组件
	 * @param defavlt 默认值
	 * @param items 下拉列表
	 * @return 下拉组件
	 */
	public static JComboBox getComboBox(String defavlt, String... items) {
		JComboBox comboBox = new JComboBox();
		comboBox.addItem(defavlt);
		if(items != null) {
			for(String item : items) {
				comboBox.addItem(item);  
			}
		}
		return comboBox;
	}
	
	/**
	 * 获取下拉组件
	 * @param items 下拉列表
	 * @return 下拉组件
	 */
	public static JComboBox getComboBox(String[] items) {
		JComboBox comboBox = new JComboBox();
		if(items != null) {
			for(String item : items) {
				comboBox.addItem(item);  
			}
		}
		return comboBox;
	}
	
	/**
	 * 获取HTML编辑框
	 * @return HTML编辑框
	 */
	public static JEditorPane getHtmlTextArea() {
		JEditorPane panel = new JEditorPane();
		panel.setContentType("text/html");	// 把编辑框设置为支持html的编辑格式
		return addBorder(panel);
	}
	
	/**
	 * 为组件添加边框
	 * @param component 需要添加边框的组件
	 * @return 已添加边框的组件（与入参为同一对象）
	 */
	public static <T extends JComponent> T addBorder(T component) {
		return addBorder(component, "");
	}
	
	/**
	 * 为组件添加边框
	 * @param component 需要添加边框的组件
	 * @param borderTitle 边框提示
	 * @return 已添加边框的组件（与入参为同一对象）
	 */
	public static <T extends JComponent> T addBorder(T component, String borderTitle) {
		if(component != null && borderTitle != null) {
			component.setBorder(new TitledBorder(borderTitle));
		}
		return component;
	}
	
	/**
	 * 信息弹窗
	 * @param msg 普通消息
	 */
	public static void info(String msg) {
		JOptionPane.showMessageDialog(
			    null, msg, "INFO", JOptionPane.INFORMATION_MESSAGE);
	}
	
	/**
	 * 信息弹窗
	 * @param msgs 普通消息
	 */
	public static void info(Object... msgs) {
		info(StrUtils.concat(msgs));
	}
	
	/**
	 * 警告弹窗
	 * @param msg 警告消息
	 */
	public static void warn(String msg) {
		JOptionPane.showMessageDialog(
			    null, msg, "WARN", JOptionPane.WARNING_MESSAGE);
	}
	
	/**
	 * 警告弹窗
	 * @param msgs 警告消息
	 */
	public static void warn(Object... msgs) {
		warn(StrUtils.concat(msgs));
	}
	
	/**
	 * 异常弹窗
	 * @param msg 异常消息
	 * @param e 异常
	 */
	public static void error(String msg) {
		error(null, msg);
	}
	
	/**
	 * 异常弹窗
	 * @param msgs 异常消息
	 */
	public static void error(Object... msgs) {
		error(StrUtils.concat(msgs));
	}
	
	/**
	 * 异常弹窗
	 * @param e 异常
	 * @param msg 异常消息
	 */
	public static void error(Throwable e, String msg) {
		JOptionPane.showMessageDialog(
			    null, msg, "ERROR", JOptionPane.ERROR_MESSAGE);
		if(e != null) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 异常弹窗
	 * @param e 异常
	 * @param msgs 异常消息
	 */
	public static void error(Throwable e, Object... msgs) {
		error(e, StrUtils.concat(msgs));
	}
	
	/**
	 * 确认弹窗
	 * @param msg 确认消息
	 * @param true:是; false:否
	 */
	public static boolean confirm(String msg) {
		return (0 == JOptionPane.showConfirmDialog(
			    null, msg, "TIPS", JOptionPane.ERROR_MESSAGE));
	}
	
	/**
	 * 确认弹窗
	 * @param msg 确认消息
	 * @param yesBtnText [是(yes)] 按钮的文字
	 * @param noBtnText [否(no)] 按钮的文字
	 * @return true:是; false:否
	 */
	public static boolean confirm(String msg, String yesBtnText, String noBtnText) {
		Object[] options = { yesBtnText, noBtnText };
		int rst = JOptionPane.showOptionDialog(null, msg, "Tips", 
				JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE, 
				null, options, options[0]);
		return (rst == 0); 
	}
	
	/**
	 * 输入弹窗
	 * @param msg 提示消息
	 * @return 输入内容
	 */
	public static String input(String msg) {
		String input = JOptionPane.showInputDialog(
			    null, msg, "TIPS", JOptionPane.OK_CANCEL_OPTION);
		return (input == null ? "" : input);
	}
	
	/**
	 * 输入弹窗(含图片)
	 * @param msg 提示消息
	 * @param icon 提示图片
	 * @return 输入内容
	 */
	public static String input(String msg, Icon image) {
		Object input = JOptionPane.showInputDialog(
			    null, msg, "TIPS", JOptionPane.OK_CANCEL_OPTION, image, null, null);
		return (input == null ? "" : input.toString());
	}
	
	/**
	 * 输入弹窗(含图片)
	 * @param msg 提示消息
	 * @param imgPath 提示图片路径
	 * @return 输入内容
	 */
	public static String input(String msg, String imgPath) {
		JLabel image = new JLabel();
		setImage(image, imgPath);
		return input(msg, image.getIcon());
	}
	
	/**
	 * 隐藏密码框内容
	 * @param password 密码框组件
	 */
	public static void hide(JPasswordField password) {
		if(password != null) {
			password.setEchoChar('*');
		}
	}
	
	/**
	 * 显示密码框内容
	 * @param password 密码框组件
	 */
	public static void view(JPasswordField password) {
		if(password != null) {
			password.setEchoChar((char) 0);
		}
	}
	
	/**
	 * <PRE>
	 * 加载图片对象(对于路径不变但图像持续变化的图片， 支持实时更新).
	 * 
	 * 	此方法并没有使用new ImageIcon(imgPath)的方式去读取图片文件, 这是因为：
	 * 		对于路径不变但图像持续变化的图片, 会会因为图片路径没有变化, 而不去更新缓存, 导致显示的图片一直不变
	 * </PRE>
	 * @param imgPath 图片存储路径, 支持文件路径和包路径
	 * 			文件路径，如： ./foo/bar/img.png
	 * 			包路径，如： /foo/bar/img.png
	 * @return 图片对象, 若加载失败则返回null
	 */
	public static ImageIcon loadImage(String imgPath) {
		return loadImage(imgPath, -1, -1);
	}
	
	/**
	 * <PRE>
	 * 加载图片对象(对于路径不变但图像持续变化的图片， 支持实时更新).
	 * 
	 * 	此方法并没有使用new ImageIcon(imgPath)的方式去读取图片文件, 这是因为：
	 * 		对于路径不变但图像持续变化的图片, 会会因为图片路径没有变化, 而不去更新缓存, 导致显示的图片一直不变
	 * </PRE>
	 * @param imgPath 图片存储路径, 支持文件路径和包路径
	 * 			文件路径，如： ./foo/bar/img.png
	 * 			包路径，如： /foo/bar/img.png
	 * @param width 设置所加载图像的宽度
	 * @param height 设置所加载图像的高度
	 * @return 图片对象, 若加载失败则返回null
	 */
	public static ImageIcon loadImage(String imgPath, int width, int height) {
		ImageIcon icon = null;
		try {
			
			// 文件路径
			if(FileUtils.exists(imgPath)) {
				Image img = Toolkit.getDefaultToolkit().createImage(imgPath);
				icon = new ImageIcon(img);
				
			// 包路径
			} else {
				icon = new ImageIcon(SwingUtils.class.getResource(imgPath));
			}
			
			// 重设图像宽高
			if(width > 0 && height > 0) {
				Image img = modifySize(icon.getImage(), width, height);
				icon = new ImageIcon(img);
			}
		} catch(Exception e) {}
		return icon;
	}
	
	/**
	 * 设置JLabel上的图片(对于路径不变但图像持续变化的图片， 支持实时更新)
	 * @param label JLabel标签对象
	 * @param imgPath 图片存储路径, 支持文件路径和包路径
	 * 			文件路径，如： ./foo/bar/img.png
	 * 			包路径，如： /foo/bar/img.png
	 */
	public static void setImage(JLabel label, String imgPath) {
		setImage(label, imgPath, -1, -1);
	}
	
	/**
	 * 设置JLabel上的图片(支持实时更新)
	 * @param label JLabel标签对象
	 * @param imgPath 图片存储路径, 支持文件路径和包路径
	 * 			文件路径，如： ./foo/bar/img.png
	 * 			包路径，如： /foo/bar/img.png
	 */
	public static boolean setImage(JLabel label, String imgPath, int width, int height) {
		boolean isOk = false;
		if(label == null) {
			return isOk;
		}
		
		ImageIcon icon = loadImage(imgPath);
		if(icon != null) {
			if(width >= 0 && height >= 0) {
				icon = new ImageIcon(
						modifySize(icon.getImage(), width, height));
			}
			label.setIcon(icon);
			isOk = true;
		}
		return isOk;
	}
	
	/**
	 * 修改图片尺寸（原图对象的尺寸不会变化）
	 * @param img 图片对象
	 * @param width 宽
	 * @param height 高
	 * @return 修改尺寸后的新图片
	 */
	private static Image modifySize(Image img, int width, int height) {
		return img.getScaledInstance(width, height, Image.SCALE_FAST);
	}
	
	/**
	 * 重绘组件内容(用于动态刷新组件内容).
	 * @param component 组件
	 */
	public static <T extends Component> void repaint(T component) {
		if(component != null) {
			component.validate();	// 重构内容面板
			component.repaint();	// 重绘内容面板
		}
	}
	
}
