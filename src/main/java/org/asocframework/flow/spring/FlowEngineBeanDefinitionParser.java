package org.asocframework.flow.spring;

import org.asocframework.flow.constants.FlowEngineColumn;
import org.asocframework.flow.constants.FlowEngineTag;
import org.asocframework.flow.engine.FlowEngine;
import org.asocframework.flow.engine.FlowEngineContext;
import org.asocframework.flow.event.EventHolder;
import org.asocframework.flow.event.EventInvoker;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.TypedStringValue;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.ManagedList;
import org.springframework.beans.factory.support.ManagedMap;
import org.springframework.beans.factory.support.ManagedProperties;
import org.springframework.beans.factory.xml.AbstractBeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.springframework.util.xml.DomUtils;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;

import java.util.List;
import java.util.Map;

import static javax.swing.text.html.CSS.getAttribute;
import static sun.jvm.hotspot.oops.CellTypeState.ref;

/**
 * @author jiqing
 * @version $Id: FlowEngineBeanDefinitionParser，v 1.0 2017/9/27 14:36 jiqing Exp $
 * @desc
 */
public class FlowEngineBeanDefinitionParser extends AbstractBeanDefinitionParser{

    protected AbstractBeanDefinition parseInternal(Element element, ParserContext parserContext) {
        BeanDefinitionBuilder factory = BeanDefinitionBuilder.rootBeanDefinition(FlowEngineContext.class);
        Element events = DomUtils.getChildElementByTagName(element, FlowEngineTag.EVENTS_TAG);
        factory.addPropertyValue(FlowEngineColumn.ENGINE_HOLDERS,parseHolders(events));
        parserProperties(element,factory,FlowEngineContext.class);
        return factory.getBeanDefinition();
    }

    private void parserProperties(Element element,BeanDefinitionBuilder factory,Class clazz){
        List<Element> properties = DomUtils.getChildElementsByTagName(element,"property");
        for (Element property : properties) {
            parseProperty(property,factory,clazz);
        }
    }

    private void parseProperty(Element property,BeanDefinitionBuilder builder,Class clazz){
        String name = property.getAttribute("name");
        String ref = property.getAttribute("ref");
        if(ref!=null&&ref.length()>0){
            builder.addPropertyReference(name,ref);
            return;
        }
        String value = property.getAttribute("value");
        if(value==null || value.length()<=0){
            throw new RuntimeException();
        }
        builder.addPropertyValue(name,value);
    }



    private Map<String,BeanDefinition> parseHolders(Element holders){
        List<Element> events = DomUtils.getChildElementsByTagName(holders, FlowEngineTag.EVENT_TAG);
        Map<String, BeanDefinition> result = new ManagedMap(events.size());
        for (Element element: events) {
            String eventName = element.getAttribute(FlowEngineTag.EVENT_NAME_ATTRIBUTE);
            BeanDefinition beanDefinition = parseHolder(element);
            result.put(eventName,beanDefinition);
        }
        return result;
    }

    private BeanDefinition parseHolder(Element holder){
        BeanDefinitionBuilder factory = BeanDefinitionBuilder.rootBeanDefinition(EventHolder.class);
        String eventName = holder.getAttribute(FlowEngineTag.EVENT_NAME_ATTRIBUTE);
        factory.addPropertyValue(FlowEngineColumn.HOLDER_EVENT_NAME,eventName);
        List<Element> processes = DomUtils.getChildElementsByTagName(holder, FlowEngineTag.PROCESS_TAG);
        List<BeanDefinition> invokers = new ManagedList<BeanDefinition>();
        for (Element element: processes) {
            BeanDefinition beanDefinition = parseInvoker(element);
            invokers.add(beanDefinition);
        }
        factory.addPropertyValue(FlowEngineColumn.HOLDER_INVOKERS,invokers);
        return factory.getBeanDefinition();
    }

    private BeanDefinition parseInvoker(Element invoker){
        BeanDefinitionBuilder factory = BeanDefinitionBuilder.rootBeanDefinition(EventInvoker.class);
        String bean = invoker.getAttribute(FlowEngineTag.PROCESS_BEAN_ATTRIBUTE);
        Integer order = Integer.parseInt(invoker.getAttribute(FlowEngineTag.PROCESS_ORDER_ATTRIBUTE));
        Boolean useful = Boolean.parseBoolean(invoker.getAttribute(FlowEngineTag.PROCESS_USEFUL_ATTRIBUTE));
        Boolean async = Boolean.parseBoolean(invoker.getAttribute(FlowEngineTag.PROCESS_ASYNC_ATTRIBUTE));
        String method = invoker.getAttribute(FlowEngineTag.PROCESS_METHOD_ATTRIBUTE);
        factory.addPropertyValue(FlowEngineColumn.INVOKER_ORDER,order);
        factory.addPropertyReference(FlowEngineColumn.INVOKER_BEAN,bean);
        factory.addPropertyValue(FlowEngineColumn.INVOKER_USEFUL,useful);
        factory.addPropertyValue(FlowEngineColumn.INVOKER_ASYNC,async);
        factory.addPropertyValue(FlowEngineColumn.INVOKER_METHOD,method);
        return factory.getBeanDefinition();
    }

}
