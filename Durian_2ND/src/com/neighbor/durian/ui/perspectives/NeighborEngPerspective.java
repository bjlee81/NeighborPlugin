package com.neighbor.durian.ui.perspectives;

import org.eclipse.ui.IFolderLayout;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;
import org.eclipse.ui.console.IConsoleConstants;
import org.eclipse.jdt.ui.JavaUI;


/**
 *  This class is meant to serve as an example for how various contributions 
 *  are made to a perspective. Note that some of the extension point id's are
 *  referred to as API constants while others are hardcoded and may be subject 
 *  to change. 
 */
public class NeighborEngPerspective implements IPerspectiveFactory {

	private IPageLayout layout;

	public NeighborEngPerspective() {
		super();
	}

	public void createInitialLayout(IPageLayout factory) {
		this.layout = factory;
		addViews();
		addActionSets();
		addNewWizardShortcuts();
		addPerspectiveShortcuts();
		addViewShortcuts();
	}

	private void addViews() {
		// Creates the overall folder layout. 
		// Note that each new Folder uses a percentage of the remaining EditorArea.
		
		String editorArea = layout.getEditorArea();
		IFolderLayout explorerLayout = layout.createFolder("explorer", IPageLayout.LEFT, 0.25f, editorArea);
		explorerLayout.addView("com.neighbor.durian.ui.neighborview.NeighborExplorer");
		explorerLayout.addView(IPageLayout.ID_PROJECT_EXPLORER);

		IFolderLayout bottom = layout.createFolder("bottomRight", // NON-NLS-1
				IPageLayout.BOTTOM, 0.75f, editorArea);
		bottom.addView(IPageLayout.ID_PROBLEM_VIEW);
		bottom.addView("org.eclipse.team.ui.GenericHistoryView"); // NON-NLS-1
		bottom.addPlaceholder(IConsoleConstants.ID_CONSOLE_VIEW);
		
		IFolderLayout outLineLayout = layout.createFolder("outLine", IPageLayout.RIGHT, 0.75f, editorArea);
		outLineLayout.addView(IPageLayout.ID_OUTLINE);

		layout.addView("org.eclipse.ant.ui.views.AntView", IPageLayout.BOTTOM, 0.55f, "outLine");
		layout.addView("org.eclipse.jdt.junit.ResultView", IPageLayout.BOTTOM, 0.55f, "org.eclipse.ant.ui.views.AntView");
		layout.addPlaceholder("org.eclipse.wst.server.ui.ServersView", IPageLayout.BOTTOM, 0.55f, "outLine");
		
				
		layout.addFastView("org.eclipse.team.ccvs.ui.RepositoriesView",0.50f); //NON-NLS-1
		layout.addFastView("org.eclipse.team.sync.views.SynchronizeView", 0.50f); //NON-NLS-1
	}

	private void addActionSets() {
		layout.addActionSet("org.eclipse.debug.ui.launchActionSet"); //NON-NLS-1
		layout.addActionSet("org.eclipse.debug.ui.debugActionSet"); //NON-NLS-1
		layout.addActionSet("org.eclipse.debug.ui.profileActionSet"); //NON-NLS-1
		layout.addActionSet("org.eclipse.jdt.debug.ui.JDTDebugActionSet"); //NON-NLS-1
		layout.addActionSet("org.eclipse.jdt.junit.JUnitActionSet"); //NON-NLS-1
		layout.addActionSet("org.eclipse.team.ui.actionSet"); //NON-NLS-1
		layout.addActionSet("org.eclipse.team.cvs.ui.CVSActionSet"); //NON-NLS-1
		layout.addActionSet("org.eclipse.ant.ui.actionSet.presentation"); //NON-NLS-1
		layout.addActionSet(JavaUI.ID_ACTION_SET);
		layout.addActionSet(JavaUI.ID_ELEMENT_CREATION_ACTION_SET);
		layout.addActionSet(IPageLayout.ID_NAVIGATE_ACTION_SET); //NON-NLS-1
	}

	private void addPerspectiveShortcuts() {
		layout.addPerspectiveShortcut("org.eclipse.team.ui.TeamSynchronizingPerspective"); //NON-NLS-1
		layout.addPerspectiveShortcut("org.eclipse.team.cvs.ui.cvsPerspective"); //NON-NLS-1
		layout.addPerspectiveShortcut("org.eclipse.ui.resourcePerspective"); //NON-NLS-1
	}

	private void addNewWizardShortcuts() {
		layout.addNewWizardShortcut("org.eclipse.team.cvs.ui.newProjectCheckout");//NON-NLS-1
		layout.addNewWizardShortcut("org.eclipse.ui.wizards.new.folder");//NON-NLS-1
		layout.addNewWizardShortcut("org.eclipse.ui.wizards.new.file");//NON-NLS-1
	}

	private void addViewShortcuts() {
		layout.addShowViewShortcut("org.eclipse.ant.ui.views.AntView"); //NON-NLS-1
		layout.addShowViewShortcut("org.eclipse.team.ccvs.ui.AnnotateView"); //NON-NLS-1
		layout.addShowViewShortcut("org.eclipse.pde.ui.DependenciesView"); //NON-NLS-1
		layout.addShowViewShortcut("org.eclipse.jdt.junit.ResultView"); //NON-NLS-1
		layout.addShowViewShortcut("org.eclipse.team.ui.GenericHistoryView"); //NON-NLS-1
		layout.addShowViewShortcut(IConsoleConstants.ID_CONSOLE_VIEW);
		layout.addShowViewShortcut(JavaUI.ID_PACKAGES);
		layout.addShowViewShortcut(IPageLayout.ID_RES_NAV);
		layout.addShowViewShortcut(IPageLayout.ID_PROBLEM_VIEW);
		layout.addShowViewShortcut(IPageLayout.ID_OUTLINE);
	}

}
