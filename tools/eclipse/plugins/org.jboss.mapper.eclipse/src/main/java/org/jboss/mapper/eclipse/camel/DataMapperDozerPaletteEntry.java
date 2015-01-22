/*******************************************************************************
 * Copyright (c) 2014 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.mapper.eclipse.camel;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.camel.model.RouteDefinition;
import org.apache.camel.model.dataformat.DataFormatsDefinition;
import org.apache.camel.spring.CamelEndpointFactoryBean;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.graphiti.features.ICreateFeature;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.ICreateContext;
import org.eclipse.graphiti.mm.pictograms.Diagram;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.graphiti.tb.IImageDecorator;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PartInitException;
import org.fusesource.ide.camel.editor.Activator;
import org.fusesource.ide.camel.editor.editor.RiderDesignEditor;
import org.fusesource.ide.camel.editor.features.create.ext.CreateEndpointFigureFeature;
import org.fusesource.ide.camel.editor.provider.ext.ICustomPaletteEntry;
import org.fusesource.ide.camel.editor.provider.ext.PaletteCategoryItemProvider.CATEGORY_TYPE;
import org.fusesource.ide.camel.model.AbstractNode;
import org.fusesource.ide.camel.model.Endpoint;
import org.fusesource.ide.camel.model.RouteContainer;
import org.fusesource.ide.camel.model.RouteSupport;
import org.fusesource.ide.camel.model.connectors.ConnectorDependency;
import org.fusesource.ide.camel.model.io.ContainerMarshaler;
//import org.jboss.mapper.camel.config.CamelContextFactoryBean;
import org.jboss.mapper.eclipse.internal.util.JavaUtil;
import org.jboss.mapper.eclipse.wizards.NewTransformationWizard;

/**
 * @author bfitzpat
 */
@SuppressWarnings("restriction")
public class DataMapperDozerPaletteEntry implements ICustomPaletteEntry {


    /* (non-Javadoc)
     * @see org.fusesource.ide.camel.editor.provider.ICustomPaletteEntry#newCreateFeature(org.eclipse.graphiti.features.IFeatureProvider)
     */
    @Override
    public ICreateFeature newCreateFeature(IFeatureProvider fp) {
    	// questions
    	// - how much do we need to create here? 
    	// - is this really a component or a defined endpoint?
    	// - fire up wizard, which creates the bits we need
    	// --- do we then have a compound feature that adds each component feature in turn?
//
//        <endpoint uri="transform:xml2json?sourceModel=abcorder.ABCOrder&amp;targetModel=xyzorderschema.XyzOrderSchema&amp;marshalId=transform-json&amp;unmarshalId=abcorder" id="xml2json"/>
//        <dataFormats>
//            <jaxb contextPath="abcorder" id="abcorder"/>
//            <json library="Jackson" id="transform-json"/>
//        </dataFormats>
//	      <route>
//            ...
//        </route>
//        <bean class="org.apache.camel.converter.dozer.DozerTypeConverterLoader" id="dozerConverterLoader"/>
//        <bean class="org.dozer.DozerBeanMapper" id="mapper">
//          <property name="mappingFiles">
//            <list>
//              <value>dozerBeanMapping.xml</value>
//            </list>
//          </property>
//        </bean>    	
    	    	
        return new DataMapperEndpointFigureFeature(fp, 
        		"Data Mapper", 
        		"Creates a Data Mapper endpoint...");
    }

    /* (non-Javadoc)
     * @see org.fusesource.ide.camel.editor.provider.ICustomPaletteEntry#getImageDecorator(java.lang.Object)
     */
    @Override
    public IImageDecorator getImageDecorator(Object object) {
        return null;
    }

    /* (non-Javadoc)
     * @see org.fusesource.ide.camel.editor.provider.ICustomPaletteEntry#getTypeName()
     */
    @Override
    public String getTypeName() {
        return "DataMapper";
    }

    /* (non-Javadoc)
     * @see org.fusesource.ide.camel.editor.provider.ICustomPaletteEntry#supports(java.lang.Class)
     */
    @SuppressWarnings("rawtypes")
	@Override
    public boolean supports(Class type) {
        return false;
    }

    /* (non-Javadoc)
     * @see org.fusesource.ide.camel.editor.provider.ICustomPaletteEntry#getRequiredCapabilities(java.lang.Object)
     */
    @Override
    public List<ConnectorDependency> getRequiredCapabilities(Object object) {
        List<ConnectorDependency> deps = new ArrayList<ConnectorDependency>();
        ConnectorDependency dep = new ConnectorDependency();
        dep.setGroupId("org.jboss.mapper");
        dep.setArtifactId("camel-transform");
        dep.setVersion("1.0.0");
        deps.add(dep);
        return deps;
    }
    
    class DataMapperEndpointFigureFeature extends CreateEndpointFigureFeature {

		public DataMapperEndpointFigureFeature(IFeatureProvider fp,
				String name, String description) {
			super(fp, name, description, null);
		}

		@Override
		public Object[] create(ICreateContext context) {
			
			AbstractNode node = null;
			RouteSupport selectedRoute = Activator.getDiagramEditor().getSelectedRoute();
//			Diagram diagram = getDiagram();

			// fire up the wizard, get the URI
	    	NewTransformationWizard wizard = new NewTransformationWizard();
	    	RiderDesignEditor camelEditor = Activator.getDiagramEditor();
	    	IProject project = camelEditor.getCamelContextFile().getProject();
	    	WizardDialog dialog = new WizardDialog(
	    			Display.getCurrent().getActiveShell(), wizard);
	    	wizard.setProject(project);
	    	IFile camelContextFile = camelEditor.getCamelContextFile();
	    	if (camelContextFile != null) {
	    	    IPath pathToCamel = JavaUtil.getJavaPathForResource(camelContextFile);
	            wizard.setCamelFilePath(pathToCamel.toPortableString());
	            wizard.setOpenEditorOnFinish(false);
	    	}
	    	int status = dialog.open();
	    	if (status == IStatus.OK) {
	    		String camelOutput = wizard.getCamelOutput();
	    		ContainerMarshaler marshaller = camelEditor.getMultiPageEditor().getDesignEditorData().marshaller;
	    		RouteContainer routeContainer = marshaller.loadRoutesFromText(camelOutput);

	    		String mapId = wizard.getMappingId();
	    		if (mapId != null) {
	    			Endpoint myEndpoint = new Endpoint("ref:" + mapId);
	    			node =  new Endpoint(myEndpoint);
	    		}
	    		
	    		org.apache.camel.spring.CamelContextFactoryBean newContext = 
	    				routeContainer.getModel().getContextElement();
	    		
	    		if (selectedRoute != null) {
	    			List<CamelEndpointFactoryBean> oldEndpoints =
	    					selectedRoute.getModel().getContextElement().getEndpoints();
	    			List<CamelEndpointFactoryBean> newEndpoints =
	    					newContext.getEndpoints();
	    			CamelEndpointFactoryBean importantEndpoint = null;
	    			for (Iterator<CamelEndpointFactoryBean> iterator = newEndpoints.iterator(); iterator
							.hasNext();) {
						CamelEndpointFactoryBean camelEndpointFactoryBean = 
								(CamelEndpointFactoryBean) iterator.next();
						
						if (camelEndpointFactoryBean.getId().equals(mapId)) {
							importantEndpoint = camelEndpointFactoryBean;
							break;
						}
					}
	    			if (importantEndpoint != null) {
	    				CamelEndpointFactoryBean oldEndpoint = null;
		    			for (Iterator<CamelEndpointFactoryBean> iterator = oldEndpoints.iterator(); iterator
								.hasNext();) {
							CamelEndpointFactoryBean oldCamelEndpointFactoryBean = 
									(CamelEndpointFactoryBean) iterator.next();
							
							if (oldCamelEndpointFactoryBean.getId().equals(mapId)) {
								oldEndpoint = oldCamelEndpointFactoryBean;
								break;
							}
						}
		    			oldEndpoints.remove(oldEndpoint);
		    			newEndpoints.add(importantEndpoint);
	    			}
	    		}
	    		
	    	} else {
	    		return new Object[]{};
	    	}
			
			if (selectedRoute != null) {
				selectedRoute.addChild(node);
			} else {
				Activator.getLogger().warning("Warning! Could not find currently selectedNode, so can't associate this node with the route!: " + node);
			}

			// do the add
			PictogramElement pe = addGraphicalRepresentation(context, node);

			getFeatureProvider().link(pe, node);
			
			// activate direct editing after object creation
			getFeatureProvider().getDirectEditingInfo().setActive(true);
			
			// return newly created business object(s)
			return new Object[] { node };
		}

//		@Override
//		protected AbstractNode createNode() {
//			
//			// fire up the wizard, get the URI
//	    	DataMappingWizard wizard = new DataMappingWizard();
//	    	RiderDesignEditor camelEditor = Activator.getDiagramEditor();
//	    	IProject project = camelEditor.getCamelContextFile().getProject();
//	    	WizardDialog dialog = new WizardDialog(
//	    			Display.getCurrent().getActiveShell(), wizard);
//	    	wizard.setProject(project);
//	    	int status = dialog.open();
//	    	if (status == IStatus.OK) {
//	    		String camelOutput = wizard.getCamelOutput();
//	    		ContainerMarshaler marshaller = camelEditor.getMultiPageEditor().getDesignEditorData().marshaller;
//	    		RouteContainer routeContainer = marshaller.loadRoutesFromText(camelOutput);
//	    		camelEditor.getModel().getModel().update(routeContainer.getModel().getContextElement());
//	    		String text = camelEditor.updateEditorText(camelOutput);
//	    		camelEditor.switchRoute((RouteSupport) camelEditor.getModel().getChildren().get(0));
////	    		camelEditor.refreshDiagramContents();
//	    		String mapId = wizard.getMappingId();
//	    		if (mapId != null) {
//	    			Endpoint myEndpoint = new Endpoint("ref:" + mapId);
//	    			return new Endpoint(myEndpoint);
//	    		}
//	    	}
//
//			return null;
//		}
    	
    }

	@Override
	public String getPaletteCategory() {
        return CATEGORY_TYPE.TRANSFORMATION.name();
	}
}
