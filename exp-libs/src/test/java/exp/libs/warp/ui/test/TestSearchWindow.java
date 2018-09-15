package exp.libs.warp.ui.test;

import java.awt.BorderLayout;
import java.util.Arrays;
import java.util.List;

import javax.swing.JPanel;

import exp.libs.warp.ui.BeautyEyeUtils;
import exp.libs.warp.ui.cpt.pnl.SearchPanel;
import exp.libs.warp.ui.cpt.win.MainWindow;

public class TestSearchWindow extends MainWindow {

	public static void main(String[] args) {
		BeautyEyeUtils.init();
		new TestSearchWindow("带宽检索", 400, 600);
	}
	
	/** serialVersionUID */
	private static final long serialVersionUID = -1504036521958169129L;
	
	private final static List<String> BANDWIDTH =  Arrays.asList(new String[] {
			"0M", "1M", "2M", "4M", "6M", "8M", "10M", "12M", "14M", "15M", 
			"16M", "18M", "20M", "22M", "24M", "26M", "28M", "30M", "32M", 
			"34M", "36M", "38M", "40M", "42M", "44M", "45M", "46M", "48M", 
			"50M", "64M", "10$100M", "100M", "155M", "280M", "622M", "1000M", 
			"1_25G", "2_5G", "10G", "40G", "100G"
	});
	
	private SearchPanel searchPanel;
	
	protected TestSearchWindow(String name, int width, int heigh) {
		super(name, width, heigh, false);
	}
	
	@Override
	protected void initComponents(Object... args) {
		this.searchPanel = new SearchPanel(BANDWIDTH);
	}

	@Override
	protected void setComponentsLayout(JPanel rootPanel) {
		rootPanel.add(searchPanel.getJPanel(), BorderLayout.CENTER);
	}

	@Override
	protected void setComponentsListener(JPanel rootPanel) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void AfterView() {}

	@Override
	protected void beforeHide() {}
	
	@Override
	protected void beforeExit() {}
	
}
