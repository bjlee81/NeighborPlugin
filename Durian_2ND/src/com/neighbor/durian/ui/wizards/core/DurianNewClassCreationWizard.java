package com.neighbor.durian.ui.wizards.core;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.internal.ui.JavaPlugin;
import org.eclipse.jdt.internal.ui.JavaPluginImages;
import org.eclipse.jdt.internal.ui.wizards.NewElementWizard;
import org.eclipse.jdt.internal.ui.wizards.NewWizardMessages;
import org.eclipse.jface.wizard.Wizard;

public class DurianNewClassCreationWizard extends NewElementWizard {
	private DurianNewClassWizardPage fPage;
	private boolean fOpenEditorOnFinish;

	public DurianNewClassCreationWizard(DurianNewClassWizardPage page,
			boolean openEditorOnFinish) {
		setDefaultPageImageDescriptor(JavaPluginImages.DESC_WIZBAN_NEWCLASS);
		setDialogSettings(JavaPlugin.getDefault().getDialogSettings());
		setWindowTitle(NewWizardMessages.NewClassCreationWizard_title + " with Durian");

		this.fPage = page;
		this.fOpenEditorOnFinish = openEditorOnFinish;
	}
	
	public DurianNewClassCreationWizard() {
		this(null, true);
	}

	@Override
	public void addPages() {
		super.addPages();
		if (this.fPage == null) {
			this.fPage = new DurianNewClassWizardPage();
			this.fPage.setWizard(this);
			this.fPage.init(getSelection());
		}
		addPage(this.fPage);
	}

	protected boolean canRunForked() {
		return (!(this.fPage.isEnclosingTypeSelected()));
	}

	protected void finishPage(IProgressMonitor monitor)
			throws InterruptedException, CoreException {
		this.fPage.createType(monitor);
	}

	public boolean performFinish() {
		warnAboutTypeCommentDeprecation();
		boolean res = super.performFinish();
		if (res) {
			IResource resource = this.fPage.getModifiedResource();
			if (resource != null) {
				selectAndReveal(resource);
				if (this.fOpenEditorOnFinish) {
					openResource((IFile) resource);
				}
			}
		}
		return res;
	}

	public IJavaElement getCreatedElement() {
		return this.fPage.getCreatedType();
	}

}
