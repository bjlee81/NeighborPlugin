package com.neighbor.durian.wizards.project;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.swing.plaf.basic.BasicTreeUI.TreeSelectionHandler;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
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
public class DurianProjectSelectPage extends WizardPage {
	private static final String PAGE_TITLE = Messages.DurianProjectSelectPage_title;
	private static final String PAGE_DESCRIPTION = Messages.DurianProjectSelectPage_description;

	protected TreeViewer treeViewer;
	protected ProjectLabelProvider labelProvider;

	private ISelection selection;

	/**
	 * Constructor for SampleNewWizardPage.
	 * 
	 * @param pageName
	 */
	public DurianProjectSelectPage(ISelection selection) {
		super(PAGE_TITLE);
		setTitle(PAGE_TITLE);
		setDescription(PAGE_DESCRIPTION);
		this.selection = selection;
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
		treeViewer = new TreeViewer(container, SWT.NULL);
		treeViewer.setContentProvider(new ProjectContentProvider());
		treeViewer.setLabelProvider(new ProjectLabelProvider());
		treeViewer.setUseHashlookup(true);
		treeViewer.addDoubleClickListener(doubleClickListener);

		// layout the tree viewer below the text field
		layoutData = new GridData();
		// layoutData.grabExcessHorizontalSpace = true;
		// layoutData.grabExcessVerticalSpace = true;
		// layoutData.horizontalAlignment = GridData.FILL;
		layoutData.verticalAlignment = GridData.BEGINNING;
		layoutData.widthHint = GridData.GRAB_HORIZONTAL;
		layoutData.heightHint = parent.getSize().y;
		treeViewer.getControl().setLayoutData(layoutData);
		treeViewer.setInput(getInitalInput());

		treeViewer.addSelectionChangedListener(selectionChangedListener);

		setControl(container);
	}

	private ProjectObject getInitalInput() {

		ProjectObject root = new ProjectObject();

		ProjectObject springParent = new ProjectObject("Spring", "100"); //$NON-NLS-1$
		springParent.setChildProject(new ProjectObject[] { new ProjectObject("Spring MVC Project", "101"), //$NON-NLS-1$
						new ProjectObject("Spring Simple Project", "102") }); //$NON-NLS-1$

		ProjectObject TcpParent = new ProjectObject("TCP", "200"); //$NON-NLS-1$
		TcpParent.setChildProject(new ProjectObject[] { new ProjectObject("TCP Server Project", "201"), //$NON-NLS-1$
						new ProjectObject("TCP Client Project", "202") }); //$NON-NLS-1$
		ProjectObject GovernmentParent = new ProjectObject("전자정부 프레임워크", "300"); //$NON-NLS-1$
		GovernmentParent.setChildProject(new ProjectObject[] { new ProjectObject("전자정부 프레임워크 Project", "301") }); //$NON-NLS-1$

		root.setChildProject(new ProjectObject[] { springParent, TcpParent, GovernmentParent });

		return root;
	}

	private void dialogChanged() {
		// if (container == null
		// || (container.getType() & (IResource.PROJECT | IResource.FOLDER)) ==
		// 0) {
		//			updateStatus("File container must exist"); //$NON-NLS-1$
		// return;
		// }
		// if (!container.isAccessible()) {
		//			updateStatus("Project must be writable"); //$NON-NLS-1$
		// return;
		// }
		// if (fileName.length() == 0) {
		//			updateStatus("File name must be specified"); //$NON-NLS-1$
		// return;
		// }
		// if (fileName.replace('\\', '/').indexOf('/', 1) > 0) {
		//			updateStatus("File name must be valid"); //$NON-NLS-1$
		// return;
		// }
		// int dotLoc = fileName.lastIndexOf('.');
		// if (dotLoc != -1) {
		// String ext = fileName.substring(dotLoc + 1);
		//			if (ext.equalsIgnoreCase("mpe") == false) { //$NON-NLS-1$
		//				updateStatus("File extension must be \"mpe\""); //$NON-NLS-1$
		// return;
		// }
		// }
		updateStatus(null);
	}

	private void updateStatus(String message) {
		setErrorMessage(message);
		setPageComplete(message == null);
	}

	private IWizardPage selectPage(String id) {

		DurianProjectWizard wizard = (DurianProjectWizard) getWizard();

		if ("101".equals(id) == true) {
			return wizard.getSpringMVCPropertiesPage();
		} else if ("102".equals(id) == true) {
			return wizard.getSpringSimplePropertiesPage();
		} else if ("201".equals(id) == true) {
			return wizard.getTcpClientPropertiesPage();
		} else if ("202".equals(id) == true) {
			return wizard.getTcpServerPropertiesPage();
		} else if ("301".equals(id) == true) { return wizard.getGovernmentPropertiesPage(); }

		return null;
	}

	@Override
	public IWizardPage getNextPage() {

		if (selection == null) { return null; }

		ProjectObject object = (ProjectObject) ((TreeSelection) selection).getFirstElement();
		if (object == null) { return null; }
		if (object.hasChildProjectList() == false) { return selectPage(object.getId()); }

		return super.getNextPage();
	}

	private IDoubleClickListener doubleClickListener = new IDoubleClickListener() {

		@Override
		public void doubleClick(DoubleClickEvent event) {
			selection = event.getSelection();
			ProjectObject object = (ProjectObject) ((TreeSelection) selection).getFirstElement();
			
			if(object.hasChildProjectList() == true) {
				if(treeViewer.getExpandedState(object) == true) {
					treeViewer.setExpandedState(object, false);
				} else {
					treeViewer.setExpandedState(object, true);
				}
			}
			
		}
	};

	private ISelectionChangedListener selectionChangedListener = new ISelectionChangedListener() {

		@Override
		public void selectionChanged(SelectionChangedEvent event) {
			selection = event.getSelection();
			ProjectObject object = (ProjectObject) ((TreeSelection) selection).getFirstElement();
			System.out.println("selection.getFirstElement() " + object.getName());
			System.out.println("selection.getFirstElement() " + object.getId());

			if (object.hasChildProjectList() == true) {
				setPageComplete(false);
			} else {
				setPageComplete(true);
			}

			System.out.println("event.getSource() " + event.getSource().getClass().getName());
		}
	};

	private class ProjectObject {
		private String id;
		private String name;
		private ProjectObject[] childProject;
		private ProjectObject parentProject;

		public ProjectObject() {
			name = null;
		}

		public ProjectObject(String name, String id) {
			this.name = name;
			this.id = id;
		}

		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public ProjectObject[] getChildProject() {
			return childProject;
		}

		public void setChildProject(ProjectObject[] childProject) {
			this.childProject = childProject;
			if (childProject != null) {
				for (int i = 0; i < childProject.length; i++) {
					childProject[i].setParentProject(this);
				}
			}
		}

		public ProjectObject getParentProject() {
			return parentProject;
		}

		public void setParentProject(ProjectObject parentProject) {
			this.parentProject = parentProject;
		}

		public boolean hasChildProjectList() {
			if (childProject == null || childProject.length == 0) { return false; }
			return true;
		}

		public boolean hasParentProject() {
			if (parentProject == null) { return false; }

			return true;
		}

	}

	public class ProjectContentProvider implements ITreeContentProvider {
		protected TreeViewer viewer;

		public void dispose() {}

		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
			this.viewer = (TreeViewer) viewer;
			// if (oldInput != null) {
			// removeListenerFrom((ProjectObject) oldInput);
			// }
			// if (newInput != null) {
			// addListenerTo((ProjectObject) newInput);
			// }
		}

		//
		// protected void removeListenerFrom(ProjectObject box) {
		// box.removeListener(this);
		// for (Iterator iterator = box.getBoxes().iterator(); iterator
		// .hasNext();) {
		// ProjectObject aBox = (ProjectObject) iterator.next();
		// removeListenerFrom(aBox);
		// }
		// }
		//
		// protected void addListenerTo(ProjectObject box) {
		// box.addListener(this);
		// for (Iterator iterator = box.getBoxes().iterator(); iterator
		// .hasNext();) {
		// ProjectObject aBox = (ProjectObject) iterator.next();
		// addListenerTo(aBox);
		// }
		// }

		public Object[] getChildren(Object parentElement) {

			ProjectObject project = (ProjectObject) parentElement;

			if (project.hasChildProjectList() == true) { return project.getChildProject(); }
			return new ProjectObject[0];
		}

		// protected Object[] concat(Object[] object, Object[] more, Object[]
		// more2) {
		// Object[] both = new Object[object.length + more.length
		// + more2.length];
		// System.arraycopy(object, 0, both, 0, object.length);
		// System.arraycopy(more, 0, both, object.length, more.length);
		// System.arraycopy(more2, 0, both, object.length + more.length,
		// more2.length);
		// return both;
		// }

		public Object getParent(Object element) {
			ProjectObject project = (ProjectObject) element;

			if (project.hasParentProject() == true) { return project.getParentProject(); }
			return null;
		}

		public boolean hasChildren(Object element) {
			return getChildren(element).length > 0;
		}

		public Object[] getElements(Object inputElement) {
			return getChildren(inputElement);
		}

		// public void add(DeltaEvent event) {
		// Object movingBox = ((Model) event.receiver()).getParent();
		// viewer.refresh(movingBox, false);
		// }
		//
		// public void remove(DeltaEvent event) {
		// add(event);
		// }

	}

	public class ProjectLabelProvider extends LabelProvider {

		public Image getImage(Object element) {
			ImageDescriptor descriptor = null;
			ProjectObject project = (ProjectObject) element;

			if (project.hasChildProjectList() == true) {
				descriptor = PlatformUI.getWorkbench().getSharedImages().getImageDescriptor(ISharedImages.IMG_OBJ_FOLDER);
			} else {
				descriptor = PlatformUI.getWorkbench().getSharedImages().getImageDescriptor(ISharedImages.IMG_OBJ_FILE);
			}

			return descriptor.createImage();
		}

		public String getText(Object element) {
			ProjectObject project = (ProjectObject) element;
			System.out.println("ProjectLabelProvider getText " //$NON-NLS-1$
							+ project.getName());
			return project.getName();
		}

		public void dispose() {}

		protected RuntimeException unknownElement(Object element) {
			return new RuntimeException("Unknown type of element in tree of type " //$NON-NLS-1$
							+ element.getClass().getName());
		}

	}
}