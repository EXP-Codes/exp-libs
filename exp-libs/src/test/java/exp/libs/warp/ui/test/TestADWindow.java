package exp.libs.warp.ui.test;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;

import exp.libs.warp.ui.BeautyEyeUtils;
import exp.libs.warp.ui.cpt.pnl.ADPanel;
import exp.libs.warp.ui.cpt.win.MainWindow;

public class TestADWindow extends MainWindow {

	public static void main(String[] args) {
		BeautyEyeUtils.init();
		new TestADWindow("TestADWindow", 700, 400, _MyADComponent.class);
	}
	
	
	/** serialVersionUID */
	private static final long serialVersionUID = -1092417226348292840L;

	private ADPanel<_MyADComponent> adPanel;
	
	private JButton printBtn;
	
	protected TestADWindow(String name, int width, int heigh, Class<_MyADComponent> component) {
		super(name, width, heigh, false, component);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	protected void initComponents(final Object... args) {
		if(args != null && args.length >= 1 && args[0] instanceof Class<?>) {
			Class<_MyADComponent> component = (Class<_MyADComponent>) args[0];
			this.adPanel = new ADPanel<_MyADComponent>(component);
		}
		this.printBtn = new JButton("打印内容");
	}
	
	@Override
	protected void setComponentsLayout(final JPanel rootPanel) {
		rootPanel.add(adPanel.getJScrollPanel(), BorderLayout.CENTER);
		rootPanel.add(printBtn, BorderLayout.SOUTH);
	}
	
	@Override
	protected void setComponentsListener(final JPanel rootPanel) {
		printBtn.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					List<_MyADComponent> components = adPanel.getLineComponents();
					for(_MyADComponent component : components) {
						System.out.println(component.getText());
					}
					
				} catch (Throwable ex) {
					List<JTextField> components = adPanel.getDefaultLineComponents();
					for(JTextField component : components) {
						System.out.println(component.getText());
					}
				}
			}
		});
	}

	@Override
	protected void AfterView() {}

	@Override
	protected void beforeHide() {}
	
	@Override
	protected void beforeExit() {}

}