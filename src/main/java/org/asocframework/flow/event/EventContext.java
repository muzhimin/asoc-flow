package org.asocframework.flow.event;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author jiqing
 * @version $Id: EventContext，v 1.0 2017/9/27 14:39 jiqing Exp $
 * @desc
 */
public class EventContext<P,R> implements Serializable{
    /**
     * 指令
     */
    private String event;

    /**
     * 组件
     */
    private List<Object> beans;

    /**
     * 请求参数
     */
    private P param;

    /**
     * 返回结果
     */
    private R result;

    /**
     * 调用结果
     */
    private boolean success = true;

    /**
     * 错误信息
     */
    private String errorMsg;

    /**
     * 中断
     */
    private boolean  breaked;

    /**
     * 透传数据
     */
    private Map<String, Object> attachments = new HashMap();



    public static  <P> EventContext create(String event, P param){
        return new EventContext(event,param);
    }

    public EventContext(String event, P param) {
        this.event = event;
        this.param = param;
    }

    public EventContext(String event, P param, R result) {
        this.event = event;
        this.param = param;
        this.result = result;
    }

    public P getParam() {
        return param;
    }

    public void setParam(P param) {
        this.param = param;
    }

    public R getResult() {
        return result;
    }

    public void setResult(R result) {
        this.result = result;
    }

    public boolean isExecSuccess() {
        return success;
    }

    public void setExecSuccess(boolean execSuccess) {
        this.success = execSuccess;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public List<Object> getBeans() {
        return beans;
    }

    public void setBeans(List<Object> beans) {
        this.beans = beans;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public boolean isBreaked() {
        return breaked;
    }

    public void setBreaked(boolean breaked) {
        this.breaked = breaked;
    }

    public Map<String, Object> getAttachments() {
        return attachments;
    }

    public void setAttachments(Map<String, Object> attachments) {
        this.attachments = attachments;
    }

    /**
     * 值类型强制匹配查询
     * @param name
     * @param clazz
     * @param <T>
     * @return
     */
    public <T> T findAttachment(String name,Class<T> clazz){
        Object object = attachments.get(name);
        if(object!=null && object.getClass().getName().equals(clazz.getName())){
            return (T) object;
        }
        return null;
    }

    /**
     * 值类型强制转换查询
     * @param name
     * @param clazz
     * @param <T>
     * @return
     */
    public <T> T getAttachment(String name,Class<T> clazz){
        Object object = attachments.get(name);
        if(object!=null){
            return (T) object;
        }
        return null;
    }
}
