//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.4-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2015.02.13 at 12:09:41 PM EST 
//


package org.jboss.mapper.camel.blueprint;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for xPathExpression complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="xPathExpression">
 *   &lt;simpleContent>
 *     &lt;extension base="&lt;http://camel.apache.org/schema/blueprint>namespaceAwareExpression">
 *       &lt;attribute name="resultType" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="saxon" type="{http://www.w3.org/2001/XMLSchema}boolean" />
 *       &lt;attribute name="factoryRef" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="objectModel" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="logNamespaces" type="{http://www.w3.org/2001/XMLSchema}boolean" />
 *       &lt;attribute name="headerName" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/extension>
 *   &lt;/simpleContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "xPathExpression")
public class XPathExpression
    extends NamespaceAwareExpression
{

    @XmlAttribute(name = "resultType")
    protected String resultType;
    @XmlAttribute(name = "saxon")
    protected Boolean saxon;
    @XmlAttribute(name = "factoryRef")
    protected String factoryRef;
    @XmlAttribute(name = "objectModel")
    protected String objectModel;
    @XmlAttribute(name = "logNamespaces")
    protected Boolean logNamespaces;
    @XmlAttribute(name = "headerName")
    protected String headerName;

    /**
     * Gets the value of the resultType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getResultType() {
        return resultType;
    }

    /**
     * Sets the value of the resultType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setResultType(String value) {
        this.resultType = value;
    }

    /**
     * Gets the value of the saxon property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isSaxon() {
        return saxon;
    }

    /**
     * Sets the value of the saxon property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setSaxon(Boolean value) {
        this.saxon = value;
    }

    /**
     * Gets the value of the factoryRef property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFactoryRef() {
        return factoryRef;
    }

    /**
     * Sets the value of the factoryRef property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFactoryRef(String value) {
        this.factoryRef = value;
    }

    /**
     * Gets the value of the objectModel property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getObjectModel() {
        return objectModel;
    }

    /**
     * Sets the value of the objectModel property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setObjectModel(String value) {
        this.objectModel = value;
    }

    /**
     * Gets the value of the logNamespaces property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isLogNamespaces() {
        return logNamespaces;
    }

    /**
     * Sets the value of the logNamespaces property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setLogNamespaces(Boolean value) {
        this.logNamespaces = value;
    }

    /**
     * Gets the value of the headerName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getHeaderName() {
        return headerName;
    }

    /**
     * Sets the value of the headerName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setHeaderName(String value) {
        this.headerName = value;
    }

}
