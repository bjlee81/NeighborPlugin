package com.neighbor.durian.wizards.project;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.plaf.basic.BasicTreeUI.TreeSelectionHandler;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;

import com.neighbor.durian.common.Messages;

/**
 * The "New" wizard page allows setting the container for the new file as well
 * as the file name. The page will only accept file name without the extension
 * OR with the extension that matches the expected one (mpe).
 */

@SuppressWarnings("restriction")
public class SpringMVCPropertiesPage extends WizardPage {
	private static final String PAGE_TITLE = "Spring MVC 프로젝트 설정 페이지";
	private static final String PAGE_DESCRIPTION = "Spring MVC 프로젝트 기본 설정";

	private ISelection selection;

	private Text servletName;
	private Text servleturlPatternName;
	private Text filterurlPatternName;
	private Combo characterSetType;

	private final static String filterName = "Set Character Encoding";
	private final static String[] encodingType = { "UTF-8", "UTF-16", "EUC-KR" };

	/**
	 * Constructor for SampleNewWizardPage.
	 * 
	 * @param pageName
	 */
	public SpringMVCPropertiesPage(ISelection selection) {
		super(PAGE_TITLE);
		setTitle(PAGE_TITLE);
		setDescription(PAGE_DESCRIPTION);
		this.selection = selection;
	}

	@Override
	public IWizardPage getNextPage() {
		return null;
	}
	
	@Override
	public void createControl(Composite parent) {

		GridLayout layout = new GridLayout(1, false);
		layout.marginHeight = 0;

		GridData layoutData = new GridData();
		layoutData.verticalAlignment = GridData.BEGINNING;

		Composite container = new Composite(parent, SWT.NULL);
		container.setLayout(layout);
		container.setLayoutData(layoutData);
//		Label label = new Label(container, SWT.NULL);
//		label.setText(PAGE_TITLE);
		initLayout(container);
		
		setControl(container);
	}

	private void initLayout(Composite container) {
		Label label1 = new Label(container, SWT.CENTER);
		GridData gd = new GridData();
		gd.horizontalSpan = 3;
		gd.horizontalIndent = 5;
		gd.grabExcessHorizontalSpace = true;
		label1.setLayoutData(gd);
		label1.setText("DurianProject/WEB-INF/web.xml");

		gd = new GridData();
		gd.horizontalSpan = 3;
		new Label(container, SWT.NULL).setLayoutData(gd);

		Label servlet_lb = new Label(container, SWT.NULL);
		servlet_lb.setText("servlet-name :");
		gd = new GridData();
		gd.horizontalSpan = 1;
		gd.horizontalIndent = 10;
		servlet_lb.setLayoutData(gd);

		servletName = new Text(container, SWT.SINGLE | SWT.BORDER);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.widthHint = 250;
		servletName.setToolTipText("<servlet-name> 설정");
		servletName.setText("firstServlet");
		servletName.setLayoutData(gd);
		servletName.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				CheckProperties();
			}
		});

		// url-pattern
		gd = new GridData();
		gd.horizontalSpan = 3;
		new Label(container, SWT.NULL).setLayoutData(gd);

		Label urlpt_lb1 = new Label(container, SWT.NULL);
		urlpt_lb1.setText("url-pattern :");
		gd = new GridData();
		gd.horizontalSpan = 1;
		gd.horizontalIndent = 10;
		urlpt_lb1.setLayoutData(gd);

		servleturlPatternName = new Text(container, SWT.SINGLE | SWT.BORDER);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.widthHint = 250;
		servleturlPatternName.setToolTipText("servlet mapping URL-pattern 설정" + "ex) /exam/*");
		servleturlPatternName.setText("/v1/*");
		servleturlPatternName.setLayoutData(gd);
		servleturlPatternName.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				CheckProperties();
			}
		});

		// character-set
		gd = new GridData();
		gd.horizontalSpan = 3;
		new Label(container, SWT.NULL).setLayoutData(gd);

		Label charset_lb1 = new Label(container, SWT.NULL);
		charset_lb1.setText("character Set :");
		gd = new GridData();
		gd.horizontalSpan = 1;
		gd.horizontalIndent = 10;
		charset_lb1.setLayoutData(gd);

		characterSetType = new Combo(container, SWT.READ_ONLY);
		characterSetType.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_FILL));
		characterSetType.setItems(encodingType);
		characterSetType.select(0);
		characterSetType.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent evt) {
				CheckProperties();
			}
		});

		// filter name
		gd = new GridData();
		gd.horizontalSpan = 3;
		new Label(container, SWT.NULL).setLayoutData(gd);

		Label filter_lb = new Label(container, SWT.NULL);
		filter_lb.setText("filter-name :");
		gd = new GridData();
		gd.horizontalSpan = 1;
		gd.horizontalIndent = 10;
		filter_lb.setLayoutData(gd);

		Text FilterName = new Text(container, SWT.SINGLE | SWT.READ_ONLY | SWT.BORDER);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.widthHint = 250;
		FilterName.setLayoutData(gd);
		FilterName.setText(filterName);

		// filter url-pattern
		gd = new GridData();
		gd.horizontalSpan = 3;
		new Label(container, SWT.NULL).setLayoutData(gd);

		Label urlpt_lb2 = new Label(container, SWT.NULL);
		urlpt_lb2.setText("filter url-pattern :");
		gd = new GridData();
		gd.horizontalSpan = 1;
		gd.horizontalIndent = 10;
		urlpt_lb2.setLayoutData(gd);

		filterurlPatternName = new Text(container, SWT.SINGLE | SWT.BORDER);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.widthHint = 250;
		filterurlPatternName.setLayoutData(gd);
		filterurlPatternName.setText("/*");
		filterurlPatternName.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				CheckProperties();
			}
		});
	}

	private void CheckProperties() {

		// 문자열 검사
		String s_name = "[^a-zA-Z]"; // 알파벳으로 시작하는 정규표현식 문자열선언
		String url_name = "[^/]";

		validateProperties(s_name, servletName);
		validateProperties(url_name, servleturlPatternName);
		validateProperties(url_name, filterurlPatternName);

		System.out.println(servletName.getText());
		System.out.println(servleturlPatternName.getText());
		System.out.println(filterurlPatternName.getText());

		if (getServletName().isEmpty() || getservleturlPatternName().isEmpty() || getfilterurlPatternName().isEmpty()) {
			setPageComplete(false);
		} else
			setPageComplete(true);
	}

	private void validateProperties(String patternStr, Text inputText) {
		Pattern p = Pattern.compile(patternStr);
		Matcher m = p.matcher(inputText.getText());
		if (m.matches()) {
			// 비교문자열이 정규표현식과 매칭되면 True
			MessageDialog.openInformation(getShell(), "Error", "입력조건에 어긋납니다.");
			inputText.setText("");
			// m.group(0); // 정규표현식에 ()로 묶어서 그룹지정을 했을 경우 0은 전체문자열
			// m.group(자릿수); // 1~n은 지정한 ()순번에 따른 매칭된 문자열을 가져옴
		}
	}
	
	public String getCharacterSetName() {
		return characterSetType.getItem(characterSetType.getSelectionIndex());
	}

	public String getfilterurlPatternName() {
		return filterurlPatternName.getText();
	}

	public String getServletName() {
		return servletName.getText();
	}

	public String getservleturlPatternName() {
		return servleturlPatternName.getText();
	}
}