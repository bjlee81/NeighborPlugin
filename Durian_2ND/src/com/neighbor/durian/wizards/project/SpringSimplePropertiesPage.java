package com.neighbor.durian.wizards.project;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.swing.plaf.basic.BasicTreeUI.TreeSelectionHandler;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.Path;
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
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
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
public class SpringSimplePropertiesPage extends WizardPage {
	private static final String PAGE_TITLE = "Spring Simple 프로젝트 설정 페이지";
	private static final String PAGE_DESCRIPTION = "Spring Simple 프로젝트 기본 설정";

	private ISelection selection;

	/**
	 * Constructor for SampleNewWizardPage.
	 * 
	 * @param pageName
	 */
	public SpringSimplePropertiesPage(ISelection selection) {
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
		Label label = new Label(container, SWT.NULL);
		label.setText(PAGE_TITLE);

		setControl(container);
	}
}