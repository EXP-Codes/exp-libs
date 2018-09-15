package exp.libs.warp.ver;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import exp.libs.utils.other.StrUtils;
import exp.libs.utils.time.TimeUtils;
import exp.libs.warp.ui.SwingUtils;
import exp.libs.warp.ui.cpt.win.PopChildWindow;
import exp.libs.warp.ui.layout.VFlowLayout;

/**
 * <PRE>	
 * 单次版本信息
 * </PRE>
 * <br/><B>PROJECT : </B> exp-libs
 * <br/><B>SUPPORT : </B> <a href="http://www.exp-blog.com" target="_blank">www.exp-blog.com</a> 
 * @version   2015-12-27
 * @author    EXP: ***REMOVED***@qq.com
 * @since     jdk版本：jdk1.6
 */
class _VerInfo extends PopChildWindow {

	private static final long serialVersionUID = -6820494056901951178L;

	private String author;
	
	private JTextField authorTF;
	
	private String version;
	
	private JTextField versionTF;
	
	private JButton snapshotBtn;
	
	private String datetime;
	
	private JTextField datetimeTF;
	
	private String upgradeContent;
	
	private JTextArea upgradeContentTA;
	
	private String upgradeStep;
	
	private JTextArea upgradeStepTA;
	
	private JButton curTimeBtn;
	
	protected _VerInfo() {
		super("版本信息", 600, 490);
	}

	@Override
	protected void initComponents(Object... args) {
		this.authorTF = new JTextField();
		this.versionTF = new JTextField();
		this.snapshotBtn = new JButton("快照版本");
		this.datetimeTF = new JTextField();
		this.upgradeContentTA = new JTextArea(6, 8);
		this.upgradeStepTA = new JTextArea(6, 8);
		this.curTimeBtn = new JButton("当前时间");
		clear();
	}

	protected void clear() {
		author = "";
		version = "";
		datetime = "";
		upgradeContent = "";
		upgradeStep = "";
		setValToUI();
	}
	
	protected void setValToUI() {
		authorTF.setText(author);
		versionTF.setText(version);
		datetimeTF.setText(datetime);
		upgradeContentTA.setText(upgradeContent);
		upgradeStepTA.setText(upgradeStep);
	}
	
	protected void setValFromUI(_VerInfo other) {
		if(other != null) {
			author = other.getAuthorTF().getText();
			version = other.getVersionTF().getText();
			datetime = other.getDatetimeTF().getText();
			upgradeContent = other.getUpgradeContentTA().getText();
			upgradeStep = other.getUpgradeStepTA().getText();
			setValToUI();
			
		} else {
			author = getAuthorTF().getText();
			version = getVersionTF().getText();
			datetime = getDatetimeTF().getText();
			upgradeContent = getUpgradeContentTA().getText();
			upgradeStep = getUpgradeStepTA().getText();
		}
	}
	
	@Override
	protected void setComponentsLayout(JPanel rootPanel) {
		JScrollPane verPanel = SwingUtils.addAutoScroll(toPanel(false));
		rootPanel.add(verPanel, BorderLayout.CENTER);
	}

	@Override
	protected void setComponentsListener(JPanel rootPanel) {
		snapshotBtn.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				String version = versionTF.getText();
				if(StrUtils.isNotTrimEmpty(version)) {
					version = version.replaceAll("(?i)-SNAPSHOT", "");
					version = version.concat("-SNAPSHOT");
					versionTF.setText(version);
				}
			}
		});
		
		curTimeBtn.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				datetimeTF.setText(TimeUtils.getSysDate());
			}
		});
	}
	
	@Override
	protected void AfterView() {}

	@Override
	protected void beforeHide() {}
	
	protected JScrollPane toPanel(boolean isEditable) {
		setValToUI();
		
		authorTF.setEditable(isEditable);
		versionTF.setEditable(isEditable);
		datetimeTF.setEditable(isEditable);
		
		JPanel panel = new JPanel(new VFlowLayout()); {
			panel.add(SwingUtils.getPairsPanel(" 责任人 ", authorTF));
			panel.add(SwingUtils.getPairsPanel(" 版本号 ", !isEditable ? versionTF : 
				SwingUtils.getEBorderPanel(versionTF, snapshotBtn)));
			panel.add(SwingUtils.getPairsPanel("定版时间", !isEditable ? datetimeTF : 
					SwingUtils.getEBorderPanel(datetimeTF, curTimeBtn)));
			panel.add(SwingUtils.getPairsPanel("升级内容", 
					SwingUtils.addScroll(upgradeContentTA)));
			panel.add(SwingUtils.getPairsPanel("升级步骤", 
					SwingUtils.addScroll(upgradeStepTA)));
		}
		return SwingUtils.addAutoScroll(panel);
	}

	protected boolean contains(String keyword) {
		boolean isContains = false;
		if(StrUtils.isNotEmpty(keyword)) {
			isContains = StrUtils.concat(author, version, datetime, 
					upgradeContent, upgradeStep).contains(keyword);
		}
		return isContains;
	}
	
	protected JTextField getAuthorTF() {
		return authorTF;
	}

	protected JTextField getVersionTF() {
		return versionTF;
	}

	protected JTextField getDatetimeTF() {
		return datetimeTF;
	}

	protected JTextArea getUpgradeContentTA() {
		return upgradeContentTA;
	}

	protected JTextArea getUpgradeStepTA() {
		return upgradeStepTA;
	}

	protected String getAuthor() {
		return author;
	}

	protected void setAuthor(String author) {
		this.author = author;
	}

	protected String getVersion() {
		return version;
	}

	protected void setVersion(String version) {
		this.version = version;
	}

	protected String getDatetime() {
		return datetime;
	}

	protected void setDatetime(String datetime) {
		this.datetime = datetime;
	}

	protected String getUpgradeContent() {
		return upgradeContent;
	}

	protected void setUpgradeContent(String upgradeContent) {
		this.upgradeContent = upgradeContent;
	}

	protected String getUpgradeStep() {
		return upgradeStep;
	}
	
	protected void setUpgradeStep(String upgradeStep) {
		this.upgradeStep = upgradeStep;
	}
	
}
