package com.neighbor.durian.ui.wizards.core;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.SubProgressMonitor;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.Signature;
import org.eclipse.jdt.internal.ui.wizards.NewWizardMessages;
import org.eclipse.jdt.internal.ui.wizards.dialogfields.DialogField;
import org.eclipse.jdt.internal.ui.wizards.dialogfields.LayoutUtil;
import org.eclipse.jdt.internal.ui.wizards.dialogfields.SelectionButtonDialogFieldGroup;
import org.eclipse.jdt.ui.CodeGeneration;
import org.eclipse.jdt.ui.wizards.NewTypeWizardPage;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.PlatformUI;

public class DurianNewClassWizardPage extends NewTypeWizardPage {

	private static final String PAGE_NAME = "NewClassWizardPage";
	private static final String SETTINGS_CREATEMAIN = "create_main";
	private static final String SETTINGS_CREATECONSTR = "create_constructor";
	private static final String SETTINGS_CREATEUNIMPLEMENTED = "create_unimplemented";
	private SelectionButtonDialogFieldGroup fMethodStubsButtons;

	/**
	 * Create the wizard.
	 */
	public DurianNewClassWizardPage() {
		super(true, "DurianNewClassWizardPage");

		setTitle("Java Class With DurianFramework");
		setDescription("Create a new Java class with Durian(based on SpringFramework).");

		String[] buttonNames3 = { NewWizardMessages.NewClassWizardPage_methods_main,
				NewWizardMessages.NewClassWizardPage_methods_constructors,
				NewWizardMessages.NewClassWizardPage_methods_inherited };

		this.fMethodStubsButtons = new SelectionButtonDialogFieldGroup(32, buttonNames3, 1);
		this.fMethodStubsButtons.setLabelText(NewWizardMessages.NewClassWizardPage_methods_label);
	}

	public void init(IStructuredSelection selection) {
		IJavaElement jelem = getInitialJavaElement(selection);
		initContainerPage(jelem);
		initTypePage(jelem);
		doStatusUpdate();

		boolean createMain = false;
		boolean createConstructors = false;
		boolean createUnimplemented = true;
		IDialogSettings dialogSettings = getDialogSettings();
		if (dialogSettings != null) {
			IDialogSettings section = dialogSettings.getSection("DurianNewClassWizardPage");
			if (section != null) {
				createMain = section.getBoolean("create_main");
				createConstructors = section.getBoolean("create_constructor");
				createUnimplemented = section.getBoolean("create_unimplemented");
			}
		}

		setMethodStubSelection(createMain, createConstructors, createUnimplemented, true);
	}

	private void doStatusUpdate() {
		IStatus[] status = { this.fContainerStatus,
				(isEnclosingTypeSelected()) ? this.fEnclosingTypeStatus : this.fPackageStatus, this.fTypeNameStatus,
				this.fModifierStatus, this.fSuperClassStatus, this.fSuperInterfacesStatus };

		updateStatus(status);
	}

	protected void handleFieldChanged(String fieldName) {
		super.handleFieldChanged(fieldName);

		doStatusUpdate();
	}

	public void createControl(Composite parent) {
		initializeDialogUnits(parent);

		Composite composite = new Composite(parent, 0);
		composite.setFont(parent.getFont());

		int nColumns = 4;

		GridLayout layout = new GridLayout();
		layout.numColumns = nColumns;
		composite.setLayout(layout);

		createContainerControls(composite, nColumns);
		createPackageControls(composite, nColumns);
		createEnclosingTypeControls(composite, nColumns);

		createSeparator(composite, nColumns);

		createTypeNameControls(composite, nColumns);
		createModifierControls(composite, nColumns);

		createSuperClassControls(composite, nColumns);
		createSuperInterfacesControls(composite, nColumns);

		createMethodStubSelectionControls(composite, nColumns);

		createCommentControls(composite, nColumns);
		enableCommentControl(true);

		setControl(composite);

		Dialog.applyDialogFont(composite);

	}

	public void setVisible(boolean visible) {
		super.setVisible(visible);
		if (visible)
			setFocus();
	}

	private void createMethodStubSelectionControls(Composite composite, int nColumns) {
		Control labelControl = this.fMethodStubsButtons.getLabelControl(composite);
		LayoutUtil.setHorizontalSpan(labelControl, nColumns);

		DialogField.createEmptySpace(composite);

		Control buttonGroup = this.fMethodStubsButtons.getSelectionButtonsGroup(composite);
		LayoutUtil.setHorizontalSpan(buttonGroup, nColumns - 1);
	}

	public boolean isCreateMain() {
		return this.fMethodStubsButtons.isSelected(0);
	}

	public boolean isCreateConstructors() {
		return this.fMethodStubsButtons.isSelected(1);
	}

	public boolean isCreateInherited() {
		return this.fMethodStubsButtons.isSelected(2);
	}

	public void setMethodStubSelection(boolean createMain, boolean createConstructors, boolean createInherited,
			boolean canBeModified) {
		this.fMethodStubsButtons.setSelection(0, createMain);
		this.fMethodStubsButtons.setSelection(1, createConstructors);
		this.fMethodStubsButtons.setSelection(2, createInherited);

		this.fMethodStubsButtons.setEnabled(canBeModified);
	}

	protected void createTypeMembers(IType type, NewTypeWizardPage.ImportsManager imports, IProgressMonitor monitor)
			throws CoreException {
		boolean doMain = isCreateMain();
		boolean doConstr = isCreateConstructors();
		boolean doInherited = isCreateInherited();
		createInheritedMethods(type, doConstr, doInherited, imports, new SubProgressMonitor(monitor, 1));

		if (doMain) {
			StringBuffer buf = new StringBuffer();

			String comment = CodeGeneration.getMethodComment(type.getCompilationUnit(), type.getTypeQualifiedName('.'),
					"main", new String[] { "args" }, new String[0], Signature.createTypeSignature("void", true), null,
					"\n");
			if (comment != null) {
				buf.append(comment);
				buf.append("\n");
			}
			buf.append("public static void main(");
			buf.append(imports.addImport("java.lang.String"));
			buf.append("[] args) {");
			buf.append("\n");
			String content = CodeGeneration.getMethodBodyContent(type.getCompilationUnit(),
					type.getTypeQualifiedName('.'), "main", false, "", "\n");
			if ((content != null) && (content.length() != 0))
				buf.append(content);
			buf.append("\n");
			buf.append("}");
			type.createMethod(buf.toString(), null, false, null);
		}

		IDialogSettings dialogSettings = getDialogSettings();
		if (dialogSettings != null) {
			IDialogSettings section = dialogSettings.getSection("DurianNewClassWizardPage");
			if (section == null) {
				section = dialogSettings.addNewSection("DurianNewClassWizardPage");
			}
			section.put("create_main", isCreateMain());
			section.put("create_constructor", isCreateConstructors());
			section.put("create_unimplemented", isCreateInherited());
		}

		if (monitor != null)
			monitor.done();
	}

}
