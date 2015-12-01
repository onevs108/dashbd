package com.catenoid.dashbd.dao.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TransReportExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public TransReportExample() {
        oredCriteria = new ArrayList<Criteria>();
    }

    public void setOrderByClause(String orderByClause) {
        this.orderByClause = orderByClause;
    }

    public String getOrderByClause() {
        return orderByClause;
    }

    public void setDistinct(boolean distinct) {
        this.distinct = distinct;
    }

    public boolean isDistinct() {
        return distinct;
    }

    public List<Criteria> getOredCriteria() {
        return oredCriteria;
    }

    public void or(Criteria criteria) {
        oredCriteria.add(criteria);
    }

    public Criteria or() {
        Criteria criteria = createCriteriaInternal();
        oredCriteria.add(criteria);
        return criteria;
    }
    
    public void and(Criteria criteria) {
        oredCriteria.add(criteria);
    }

    public Criteria and() {
        Criteria criteria = createCriteriaInternal();
        oredCriteria.add(criteria);
        return criteria;
    }

    public Criteria createCriteria() {
        Criteria criteria = createCriteriaInternal();
        if (oredCriteria.size() == 0) {
            oredCriteria.add(criteria);
        }
        return criteria;
    }

    protected Criteria createCriteriaInternal() {
        Criteria criteria = new Criteria();
        return criteria;
    }

    public void clear() {
        oredCriteria.clear();
        orderByClause = null;
        distinct = false;
    }

    protected abstract static class GeneratedCriteria {
        protected List<Criterion> criteria;

        protected GeneratedCriteria() {
            super();
            criteria = new ArrayList<Criterion>();
        }

        public boolean isValid() {
            return criteria.size() > 0;
        }

        public List<Criterion> getAllCriteria() {
            return criteria;
        }

        public List<Criterion> getCriteria() {
            return criteria;
        }

        protected void addCriterion(String condition) {
            if (condition == null) {
                throw new RuntimeException("Value for condition cannot be null");
            }
            criteria.add(new Criterion(condition));
        }

        protected void addCriterion(String condition, Object value, String property) {
            if (value == null) {
                throw new RuntimeException("Value for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value));
        }

        protected void addCriterion(String condition, Object value1, Object value2, String property) {
            if (value1 == null || value2 == null) {
                throw new RuntimeException("Between values for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value1, value2));
        }
        
        public Criteria andIdIsNull() {
            addCriterion("id is null");
            return (Criteria) this;
        }

        public Criteria andIdIsNotNull() {
            addCriterion("id is not null");
            return (Criteria) this;
        }

        public Criteria andIdEqualTo(Integer value) {
            addCriterion("id =", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotEqualTo(Integer value) {
            addCriterion("id <>", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThan(Integer value) {
            addCriterion("id >", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThanOrEqualTo(Integer value) {
            addCriterion("id >=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThan(Integer value) {
            addCriterion("id <", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThanOrEqualTo(Integer value) {
            addCriterion("id <=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdIn(List<Integer> values) {
            addCriterion("id in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotIn(List<Integer> values) {
            addCriterion("id not in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdBetween(Integer value1, Integer value2) {
            addCriterion("id between", value1, value2, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotBetween(Integer value1, Integer value2) {
            addCriterion("id not between", value1, value2, "id");
            return (Criteria) this;
        }

        public Criteria andTransactionIdIsNull() {
            addCriterion("transaction_id is null");
            return (Criteria) this;
        }

        public Criteria andTransactionIdIsNotNull() {
            addCriterion("transaction_id is not null");
            return (Criteria) this;
        }

        public Criteria andTransactionIdEqualTo(Integer value) {
            addCriterion("transaction_id =", value, "transactionId");
            return (Criteria) this;
        }

        public Criteria andTransactionIdNotEqualTo(Integer value) {
            addCriterion("transaction_id <>", value, "transactionId");
            return (Criteria) this;
        }

        public Criteria andTransactionIdGreaterThan(Integer value) {
            addCriterion("transaction_id >", value, "transactionId");
            return (Criteria) this;
        }

        public Criteria andTransactionIdGreaterThanOrEqualTo(Integer value) {
            addCriterion("transaction_id >=", value, "transactionId");
            return (Criteria) this;
        }

        public Criteria andTransactionIdLessThan(Integer value) {
            addCriterion("transaction_id <", value, "transactionId");
            return (Criteria) this;
        }

        public Criteria andTransactionIdLessThanOrEqualTo(Integer value) {
            addCriterion("transaction_id <=", value, "transactionId");
            return (Criteria) this;
        }

        public Criteria andTransactionIdIn(List<Integer> values) {
            addCriterion("transaction_id in", values, "transactionId");
            return (Criteria) this;
        }

        public Criteria andTransactionIdNotIn(List<Integer> values) {
            addCriterion("transaction_id not in", values, "transactionId");
            return (Criteria) this;
        }

        public Criteria andTransactionIdBetween(Integer value1, Integer value2) {
            addCriterion("transaction_id between", value1, value2, "transactionId");
            return (Criteria) this;
        }

        public Criteria andTransactionIdNotBetween(Integer value1, Integer value2) {
            addCriterion("transaction_id not between", value1, value2, "transactionId");
            return (Criteria) this;
        }

        public Criteria andAgentKeyIsNull() {
            addCriterion("agent_key is null");
            return (Criteria) this;
        }

        public Criteria andAgentKeyIsNotNull() {
            addCriterion("agent_key is not null");
            return (Criteria) this;
        }

        public Criteria andAgentKeyEqualTo(String value) {
            addCriterion("agent_key =", value, "agentKey");
            return (Criteria) this;
        }

        public Criteria andAgentKeyNotEqualTo(String value) {
            addCriterion("agent_key <>", value, "agentKey");
            return (Criteria) this;
        }

        public Criteria andAgentKeyGreaterThan(String value) {
            addCriterion("agent_key >", value, "agentKey");
            return (Criteria) this;
        }

        public Criteria andAgentKeyGreaterThanOrEqualTo(String value) {
            addCriterion("agent_key >=", value, "agentKey");
            return (Criteria) this;
        }

        public Criteria andAgentKeyLessThan(String value) {
            addCriterion("agent_key <", value, "agentKey");
            return (Criteria) this;
        }

        public Criteria andAgentKeyLessThanOrEqualTo(String value) {
            addCriterion("agent_key <=", value, "agentKey");
            return (Criteria) this;
        }

        public Criteria andAgentKeyLike(String value) {
            addCriterion("agent_key like", value, "agentKey");
            return (Criteria) this;
        }

        public Criteria andAgentKeyNotLike(String value) {
            addCriterion("agent_key not like", value, "agentKey");
            return (Criteria) this;
        }

        public Criteria andAgentKeyIn(List<String> values) {
            addCriterion("agent_key in", values, "agentKey");
            return (Criteria) this;
        }

        public Criteria andAgentKeyNotIn(List<String> values) {
            addCriterion("agent_key not in", values, "agentKey");
            return (Criteria) this;
        }

        public Criteria andAgentKeyBetween(String value1, String value2) {
            addCriterion("agent_key between", value1, value2, "agentKey");
            return (Criteria) this;
        }

        public Criteria andAgentKeyNotBetween(String value1, String value2) {
            addCriterion("agent_key not between", value1, value2, "agentKey");
            return (Criteria) this;
        }

        public Criteria andServicesIdIsNull() {
            addCriterion("services_id is null");
            return (Criteria) this;
        }

        public Criteria andServicesIdIsNotNull() {
            addCriterion("services_id is not null");
            return (Criteria) this;
        }

        public Criteria andServicesIdEqualTo(Integer value) {
            addCriterion("services_id =", value, "servicesId");
            return (Criteria) this;
        }

        public Criteria andServicesIdNotEqualTo(Integer value) {
            addCriterion("services_id <>", value, "servicesId");
            return (Criteria) this;
        }

        public Criteria andServicesIdGreaterThan(Integer value) {
            addCriterion("services_id >", value, "servicesId");
            return (Criteria) this;
        }

        public Criteria andServicesIdGreaterThanOrEqualTo(Integer value) {
            addCriterion("services_id >=", value, "servicesId");
            return (Criteria) this;
        }

        public Criteria andServicesIdLessThan(Integer value) {
            addCriterion("services_id <", value, "servicesId");
            return (Criteria) this;
        }

        public Criteria andServicesIdLessThanOrEqualTo(Integer value) {
            addCriterion("services_id <=", value, "servicesId");
            return (Criteria) this;
        }

        public Criteria andServicesIdIn(List<Integer> values) {
            addCriterion("services_id in", values, "servicesId");
            return (Criteria) this;
        }

        public Criteria andServicesIdNotIn(List<Integer> values) {
            addCriterion("services_id not in", values, "servicesId");
            return (Criteria) this;
        }

        public Criteria andServicesIdBetween(Integer value1, Integer value2) {
            addCriterion("services_id between", value1, value2, "servicesId");
            return (Criteria) this;
        }

        public Criteria andServicesIdNotBetween(Integer value1, Integer value2) {
            addCriterion("services_id not between", value1, value2, "servicesId");
            return (Criteria) this;
        }

        public Criteria andSendDtmIsNull() {
            addCriterion("send_dtm is null");
            return (Criteria) this;
        }

        public Criteria andSendDtmIsNotNull() {
            addCriterion("send_dtm is not null");
            return (Criteria) this;
        }

        public Criteria andSendDtmEqualTo(Date value) {
            addCriterion("send_dtm =", value, "sendDtm");
            return (Criteria) this;
        }

        public Criteria andSendDtmNotEqualTo(Date value) {
            addCriterion("send_dtm <>", value, "sendDtm");
            return (Criteria) this;
        }

        public Criteria andSendDtmGreaterThan(Date value) {
            addCriterion("send_dtm >", value, "sendDtm");
            return (Criteria) this;
        }

        public Criteria andSendDtmGreaterThanOrEqualTo(Date value) {
            addCriterion("send_dtm >=", value, "sendDtm");
            return (Criteria) this;
        }

        public Criteria andSendDtmLessThan(Date value) {
            addCriterion("send_dtm <", value, "sendDtm");
            return (Criteria) this;
        }

        public Criteria andSendDtmLessThanOrEqualTo(Date value) {
            addCriterion("send_dtm <=", value, "sendDtm");
            return (Criteria) this;
        }

        public Criteria andSendDtmIn(List<Date> values) {
            addCriterion("send_dtm in", values, "sendDtm");
            return (Criteria) this;
        }

        public Criteria andSendDtmNotIn(List<Date> values) {
            addCriterion("send_dtm not in", values, "sendDtm");
            return (Criteria) this;
        }

        public Criteria andSendDtmBetween(Date value1, Date value2) {
            addCriterion("send_dtm between", value1, value2, "sendDtm");
            return (Criteria) this;
        }

        public Criteria andSendDtmNotBetween(Date value1, Date value2) {
            addCriterion("send_dtm not between", value1, value2, "sendDtm");
            return (Criteria) this;
        }

        public Criteria andResultCodeIsNull() {
            addCriterion("result_code is null");
            return (Criteria) this;
        }

        public Criteria andResultCodeIsNotNull() {
            addCriterion("result_code is not null");
            return (Criteria) this;
        }

        public Criteria andResultCodeEqualTo(Integer value) {
            addCriterion("result_code =", value, "resultCode");
            return (Criteria) this;
        }

        public Criteria andResultCodeNotEqualTo(Integer value) {
            addCriterion("result_code <>", value, "resultCode");
            return (Criteria) this;
        }

        public Criteria andResultCodeGreaterThan(Integer value) {
            addCriterion("result_code >", value, "resultCode");
            return (Criteria) this;
        }

        public Criteria andResultCodeGreaterThanOrEqualTo(Integer value) {
            addCriterion("result_code >=", value, "resultCode");
            return (Criteria) this;
        }

        public Criteria andResultCodeLessThan(Integer value) {
            addCriterion("result_code <", value, "resultCode");
            return (Criteria) this;
        }

        public Criteria andResultCodeLessThanOrEqualTo(Integer value) {
            addCriterion("result_code <=", value, "resultCode");
            return (Criteria) this;
        }

        public Criteria andResultCodeIn(List<Integer> values) {
            addCriterion("result_code in", values, "resultCode");
            return (Criteria) this;
        }

        public Criteria andResultCodeNotIn(List<Integer> values) {
            addCriterion("result_code not in", values, "resultCode");
            return (Criteria) this;
        }

        public Criteria andResultCodeBetween(Integer value1, Integer value2) {
            addCriterion("result_code between", value1, value2, "resultCode");
            return (Criteria) this;
        }

        public Criteria andResultCodeNotBetween(Integer value1, Integer value2) {
            addCriterion("result_code not between", value1, value2, "resultCode");
            return (Criteria) this;
        }

        public Criteria andRetryCountIsNull() {
            addCriterion("retry_count is null");
            return (Criteria) this;
        }

        public Criteria andRetryCountIsNotNull() {
            addCriterion("retry_count is not null");
            return (Criteria) this;
        }

        public Criteria andRetryCountEqualTo(Integer value) {
            addCriterion("retry_count =", value, "retryCount");
            return (Criteria) this;
        }

        public Criteria andRetryCountNotEqualTo(Integer value) {
            addCriterion("retry_count <>", value, "retryCount");
            return (Criteria) this;
        }

        public Criteria andRetryCountGreaterThan(Integer value) {
            addCriterion("retry_count >", value, "retryCount");
            return (Criteria) this;
        }

        public Criteria andRetryCountGreaterThanOrEqualTo(Integer value) {
            addCriterion("retry_count >=", value, "retryCount");
            return (Criteria) this;
        }

        public Criteria andRetryCountLessThan(Integer value) {
            addCriterion("retry_count <", value, "retryCount");
            return (Criteria) this;
        }

        public Criteria andRetryCountLessThanOrEqualTo(Integer value) {
            addCriterion("retry_count <=", value, "retryCount");
            return (Criteria) this;
        }

        public Criteria andRetryCountIn(List<Integer> values) {
            addCriterion("retry_count in", values, "retryCount");
            return (Criteria) this;
        }

        public Criteria andRetryCountNotIn(List<Integer> values) {
            addCriterion("retry_count not in", values, "retryCount");
            return (Criteria) this;
        }

        public Criteria andRetryCountBetween(Integer value1, Integer value2) {
            addCriterion("retry_count between", value1, value2, "retryCount");
            return (Criteria) this;
        }

        public Criteria andRetryCountNotBetween(Integer value1, Integer value2) {
            addCriterion("retry_count not between", value1, value2, "retryCount");
            return (Criteria) this;
        }

        public Criteria andCreatedAtIsNull() {
            addCriterion("created_at is null");
            return (Criteria) this;
        }

        public Criteria andCreatedAtIsNotNull() {
            addCriterion("created_at is not null");
            return (Criteria) this;
        }

        public Criteria andCreatedAtEqualTo(Date value) {
            addCriterion("created_at =", value, "createdAt");
            return (Criteria) this;
        }

        public Criteria andCreatedAtNotEqualTo(Date value) {
            addCriterion("created_at <>", value, "createdAt");
            return (Criteria) this;
        }

        public Criteria andCreatedAtGreaterThan(Date value) {
            addCriterion("created_at >", value, "createdAt");
            return (Criteria) this;
        }

        public Criteria andCreatedAtGreaterThanOrEqualTo(Date value) {
            addCriterion("created_at >=", value, "createdAt");
            return (Criteria) this;
        }

        public Criteria andCreatedAtLessThan(Date value) {
            addCriterion("created_at <", value, "createdAt");
            return (Criteria) this;
        }

        public Criteria andCreatedAtLessThanOrEqualTo(Date value) {
            addCriterion("created_at <=", value, "createdAt");
            return (Criteria) this;
        }

        public Criteria andCreatedAtIn(List<Date> values) {
            addCriterion("created_at in", values, "createdAt");
            return (Criteria) this;
        }

        public Criteria andCreatedAtNotIn(List<Date> values) {
            addCriterion("created_at not in", values, "createdAt");
            return (Criteria) this;
        }

        public Criteria andCreatedAtBetween(Date value1, Date value2) {
            addCriterion("created_at between", value1, value2, "createdAt");
            return (Criteria) this;
        }

        public Criteria andCreatedAtNotBetween(Date value1, Date value2) {
            addCriterion("created_at not between", value1, value2, "createdAt");
            return (Criteria) this;
        }

        public Criteria andUpdatedAtIsNull() {
            addCriterion("updated_at is null");
            return (Criteria) this;
        }

        public Criteria andUpdatedAtIsNotNull() {
            addCriterion("updated_at is not null");
            return (Criteria) this;
        }

        public Criteria andUpdatedAtEqualTo(Date value) {
            addCriterion("updated_at =", value, "updatedAt");
            return (Criteria) this;
        }

        public Criteria andUpdatedAtNotEqualTo(Date value) {
            addCriterion("updated_at <>", value, "updatedAt");
            return (Criteria) this;
        }

        public Criteria andUpdatedAtGreaterThan(Date value) {
            addCriterion("updated_at >", value, "updatedAt");
            return (Criteria) this;
        }

        public Criteria andUpdatedAtGreaterThanOrEqualTo(Date value) {
            addCriterion("updated_at >=", value, "updatedAt");
            return (Criteria) this;
        }

        public Criteria andUpdatedAtLessThan(Date value) {
            addCriterion("updated_at <", value, "updatedAt");
            return (Criteria) this;
        }

        public Criteria andUpdatedAtLessThanOrEqualTo(Date value) {
            addCriterion("updated_at <=", value, "updatedAt");
            return (Criteria) this;
        }

        public Criteria andUpdatedAtIn(List<Date> values) {
            addCriterion("updated_at in", values, "updatedAt");
            return (Criteria) this;
        }

        public Criteria andUpdatedAtNotIn(List<Date> values) {
            addCriterion("updated_at not in", values, "updatedAt");
            return (Criteria) this;
        }

        public Criteria andUpdatedAtBetween(Date value1, Date value2) {
            addCriterion("updated_at between", value1, value2, "updatedAt");
            return (Criteria) this;
        }

        public Criteria andUpdatedAtNotBetween(Date value1, Date value2) {
            addCriterion("updated_at not between", value1, value2, "updatedAt");
            return (Criteria) this;
        }
    }

    public static class Criteria extends GeneratedCriteria {

        protected Criteria() {
            super();
        }
    }

    public static class Criterion {
        private String condition;

        private Object value;

        private Object secondValue;

        private boolean noValue;

        private boolean singleValue;

        private boolean betweenValue;

        private boolean listValue;

        private String typeHandler;

        public String getCondition() {
            return condition;
        }

        public Object getValue() {
            return value;
        }

        public Object getSecondValue() {
            return secondValue;
        }

        public boolean isNoValue() {
            return noValue;
        }

        public boolean isSingleValue() {
            return singleValue;
        }

        public boolean isBetweenValue() {
            return betweenValue;
        }

        public boolean isListValue() {
            return listValue;
        }

        public String getTypeHandler() {
            return typeHandler;
        }

        protected Criterion(String condition) {
            super();
            this.condition = condition;
            this.typeHandler = null;
            this.noValue = true;
        }

        protected Criterion(String condition, Object value, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.typeHandler = typeHandler;
            if (value instanceof List<?>) {
                this.listValue = true;
            } else {
                this.singleValue = true;
            }
        }

        protected Criterion(String condition, Object value) {
            this(condition, value, null);
        }

        protected Criterion(String condition, Object value, Object secondValue, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.secondValue = secondValue;
            this.typeHandler = typeHandler;
            this.betweenValue = true;
        }

        protected Criterion(String condition, Object value, Object secondValue) {
            this(condition, value, secondValue, null);
        }
    }
}