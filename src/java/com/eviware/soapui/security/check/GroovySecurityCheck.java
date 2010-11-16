/*
 *  soapUI, copyright (C) 2004-2010 eviware.com 
 *
 *  soapUI is free software; you can redistribute it and/or modify it under the 
 *  terms of version 2.1 of the GNU Lesser General Public License as published by 
 *  the Free Software Foundation.
 *
 *  soapUI is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without 
 *  even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. 
 *  See the GNU Lesser General Public License for more details at gnu.org.
 */
package com.eviware.soapui.security.check;

import java.awt.BorderLayout;

import javax.swing.JComponent;
import javax.swing.JEditorPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import com.eviware.soapui.SoapUI;
import com.eviware.soapui.config.GroovySecurityCheckConfig;
import com.eviware.soapui.config.SecurityCheckConfig;
import com.eviware.soapui.model.ModelItem;
import com.eviware.soapui.model.testsuite.TestStep;
import com.eviware.soapui.security.SecurityTestContext;
import com.eviware.soapui.security.log.SecurityTestLog;
import com.eviware.soapui.support.components.SimpleForm;

/**
 * 
 * @author soapui team
 */

public class GroovySecurityCheck extends AbstractSecurityCheck {
	public static final String SCRIPT_PROPERTY = GroovySecurityCheck.class
			.getName()
			+ "@script";
	public static final String TYPE = "GroovySecurityCheck";
	// private String script;
	private GroovySecurityCheckConfig groovySecurityCheckConfig;
	protected JTextArea scriptTextArea;

	public GroovySecurityCheck(SecurityCheckConfig config, ModelItem parent,
			String icon) {
		super(config, parent, icon);
		if (config == null) {
			config = SecurityCheckConfig.Factory.newInstance();
			GroovySecurityCheckConfig groovyscc = GroovySecurityCheckConfig.Factory
					.newInstance();
			config.setConfig(groovyscc);
		} else {
			GroovySecurityCheckConfig groovyscc = GroovySecurityCheckConfig.Factory
					.newInstance();
			config.setConfig(groovyscc);
		}

		// this.script = groovySecurityCheckConfig.getScript() != null ?
		// groovySecurityCheckConfig.getScript()
		// .getStringValue() : "";
	}

	@Override
	protected void execute(TestStep testStep, SecurityTestContext context,
			SecurityTestLog securityTestLog) {
		scriptEngine.setScript(getScript());
		scriptEngine.setVariable("testStep", testStep);
		scriptEngine.setVariable("log", SoapUI.ensureGroovyLog());
		// scriptEngine.setVariable( "context", context );

		try {
			scriptEngine.run();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			scriptEngine.clearVariables();
		}
	}

	public void setScript(String script) {
		String old = getScript();
		if (groovySecurityCheckConfig.getScript() == null) {
			groovySecurityCheckConfig.addNewScript();
		}

		groovySecurityCheckConfig.getScript().setStringValue(script);
		// this.script=script;
		notifyPropertyChanged(SCRIPT_PROPERTY, old, script);
	}

	private String getScript() {
		return groovySecurityCheckConfig.getScript() != null ? groovySecurityCheckConfig
				.getScript().getStringValue()
				: "";
	}

	@Override
	public void analyze(TestStep testStep, SecurityTestContext context,
			SecurityTestLog securityTestLog) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean acceptsTestStep(TestStep testStep) {
		return true;
	}

	@Override
	public JComponent getComponent() {
		if (panel == null) {
			panel = new JPanel(new BorderLayout());

			form = new SimpleForm();
			form.addSpace(5);

			form.setDefaultTextFieldColumns(50);

			scriptTextArea = form.appendTextArea("Script", "Script to use");
			scriptTextArea.setText("");
		}
		return panel;
	}

	@Override
	public String getType() {
		return TYPE;
	}
}