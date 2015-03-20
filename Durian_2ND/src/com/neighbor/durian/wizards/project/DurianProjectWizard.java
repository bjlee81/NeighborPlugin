package com.neighbor.durian.wizards.project;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;

/**
 * This is a sample new wizard. Its role is to create a new file
 * resource in the provided container. If the container resource
 * (a folder or a project) is selected in the workspace
 * when the wizard is opened, it will accept it as the target
 * container. The wizard creates one file with the extension
 * "mpe". If a sample multi-page editor (also available
 * as a template) is registered for the same extension, it will
 * be able to open it.
 */

public class DurianProjectWizard extends Wizard implements INewWizard {
	private DurianProjectSelectPage projectSelectPage;
	private GovernmentPropertiesPage governmentPropertiesPage;
	private SpringMVCPropertiesPage springMVCPropertiesPage;
	private SpringSimplePropertiesPage springSimplePropertiesPage;
	private TCPClientPropertiesPage tcpClientPropertiesPage;
	private TCPServerPropertiesPage tcpServerPropertiesPage;

	private ISelection selection;

	/**
	 * Constructor for SampleNewWizard.
	 */
	public DurianProjectWizard() {
		super();
		setNeedsProgressMonitor(true);
	}

	/**
	 * Adding the page to the wizard.
	 */

	public void addPages() {
		projectSelectPage = new DurianProjectSelectPage(selection);
		addPage(projectSelectPage);
		governmentPropertiesPage = new GovernmentPropertiesPage(selection);
		addPage(governmentPropertiesPage);
		springMVCPropertiesPage = new SpringMVCPropertiesPage(selection);
		addPage(springMVCPropertiesPage);
		springSimplePropertiesPage = new SpringSimplePropertiesPage(selection);
		addPage(springSimplePropertiesPage);
		tcpClientPropertiesPage = new TCPClientPropertiesPage(selection);
		addPage(tcpClientPropertiesPage);
		tcpServerPropertiesPage = new TCPServerPropertiesPage(selection);
		addPage(tcpServerPropertiesPage);
	}

	
	public GovernmentPropertiesPage getGovernmentPropertiesPage() {
		return governmentPropertiesPage;
	}

	public SpringMVCPropertiesPage getSpringMVCPropertiesPage() {
		return springMVCPropertiesPage;
	}

	public SpringSimplePropertiesPage getSpringSimplePropertiesPage() {
		return springSimplePropertiesPage;
	}

	public TCPClientPropertiesPage getTcpClientPropertiesPage() {
		return tcpClientPropertiesPage;
	}

	public TCPServerPropertiesPage getTcpServerPropertiesPage() {
		return tcpServerPropertiesPage;
	}

	public void init(IWorkbench workbench, IStructuredSelection selection) {
		this.selection = selection;
	}

	@Override
	public boolean performFinish() {
		System.out.println("performFinish");
		return true;
	}
}