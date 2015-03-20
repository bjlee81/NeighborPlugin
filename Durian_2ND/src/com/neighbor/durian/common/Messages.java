package com.neighbor.durian.common;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS {
	private static final String BUNDLE_NAME = "com.neighbor.durian.common.messages"; //$NON-NLS-1$
	public static String DurianProjectSelectPage_description;
	public static String DurianProjectSelectPage_title;
	public static String SampleView_id;
	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}

	private Messages() {
	}
}
