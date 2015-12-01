package com.catenoid.dashbd.dao.model;

import java.util.ArrayList;
import java.util.List;

public class ServiceExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public ServiceExample() {
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

        public Criteria andServiceTypeIdIsNull() {
            addCriterion("service_type_id is null");
            return (Criteria) this;
        }

        public Criteria andServiceTypeIdIsNotNull() {
            addCriterion("service_type_id is not null");
            return (Criteria) this;
        }

        public Criteria andServiceTypeIdEqualTo(Integer value) {
            addCriterion("service_type_id =", value, "serviceTypeId");
            return (Criteria) this;
        }

        public Criteria andServiceTypeIdNotEqualTo(Integer value) {
            addCriterion("service_type_id <>", value, "serviceTypeId");
            return (Criteria) this;
        }

        public Criteria andServiceTypeIdGreaterThan(Integer value) {
            addCriterion("service_type_id >", value, "serviceTypeId");
            return (Criteria) this;
        }

        public Criteria andServiceTypeIdGreaterThanOrEqualTo(Integer value) {
            addCriterion("service_type_id >=", value, "serviceTypeId");
            return (Criteria) this;
        }

        public Criteria andServiceTypeIdLessThan(Integer value) {
            addCriterion("service_type_id <", value, "serviceTypeId");
            return (Criteria) this;
        }

        public Criteria andServiceTypeIdLessThanOrEqualTo(Integer value) {
            addCriterion("service_type_id <=", value, "serviceTypeId");
            return (Criteria) this;
        }

        public Criteria andServiceTypeIdIn(List<Integer> values) {
            addCriterion("service_type_id in", values, "serviceTypeId");
            return (Criteria) this;
        }

        public Criteria andServiceTypeIdNotIn(List<Integer> values) {
            addCriterion("service_type_id not in", values, "serviceTypeId");
            return (Criteria) this;
        }

        public Criteria andServiceTypeIdBetween(Integer value1, Integer value2) {
            addCriterion("service_type_id between", value1, value2, "serviceTypeId");
            return (Criteria) this;
        }

        public Criteria andServiceTypeIdNotBetween(Integer value1, Integer value2) {
            addCriterion("service_type_id not between", value1, value2, "serviceTypeId");
            return (Criteria) this;
        }

        public Criteria andServiceStrIdIsNull() {
            addCriterion("service_str_id is null");
            return (Criteria) this;
        }

        public Criteria andServiceStrIdIsNotNull() {
            addCriterion("service_str_id is not null");
            return (Criteria) this;
        }

        public Criteria andServiceStrIdEqualTo(String value) {
            addCriterion("service_str_id =", value, "serviceStrId");
            return (Criteria) this;
        }

        public Criteria andServiceStrIdNotEqualTo(String value) {
            addCriterion("service_str_id <>", value, "serviceStrId");
            return (Criteria) this;
        }

        public Criteria andServiceStrIdGreaterThan(String value) {
            addCriterion("service_str_id >", value, "serviceStrId");
            return (Criteria) this;
        }

        public Criteria andServiceStrIdGreaterThanOrEqualTo(String value) {
            addCriterion("service_str_id >=", value, "serviceStrId");
            return (Criteria) this;
        }

        public Criteria andServiceStrIdLessThan(String value) {
            addCriterion("service_str_id <", value, "serviceStrId");
            return (Criteria) this;
        }

        public Criteria andServiceStrIdLessThanOrEqualTo(String value) {
            addCriterion("service_str_id <=", value, "serviceStrId");
            return (Criteria) this;
        }

        public Criteria andServiceStrIdLike(String value) {
            addCriterion("service_str_id like", value, "serviceStrId");
            return (Criteria) this;
        }

        public Criteria andServiceStrIdNotLike(String value) {
            addCriterion("service_str_id not like", value, "serviceStrId");
            return (Criteria) this;
        }

        public Criteria andServiceStrIdIn(List<String> values) {
            addCriterion("service_str_id in", values, "serviceStrId");
            return (Criteria) this;
        }

        public Criteria andServiceStrIdNotIn(List<String> values) {
            addCriterion("service_str_id not in", values, "serviceStrId");
            return (Criteria) this;
        }

        public Criteria andServiceStrIdBetween(String value1, String value2) {
            addCriterion("service_str_id between", value1, value2, "serviceStrId");
            return (Criteria) this;
        }

        public Criteria andServiceStrIdNotBetween(String value1, String value2) {
            addCriterion("service_str_id not between", value1, value2, "serviceStrId");
            return (Criteria) this;
        }

        public Criteria andServiceClassIsNull() {
            addCriterion("service_class is null");
            return (Criteria) this;
        }

        public Criteria andServiceClassIsNotNull() {
            addCriterion("service_class is not null");
            return (Criteria) this;
        }

        public Criteria andServiceClassEqualTo(String value) {
            addCriterion("service_class =", value, "serviceClass");
            return (Criteria) this;
        }

        public Criteria andServiceClassNotEqualTo(String value) {
            addCriterion("service_class <>", value, "serviceClass");
            return (Criteria) this;
        }

        public Criteria andServiceClassGreaterThan(String value) {
            addCriterion("service_class >", value, "serviceClass");
            return (Criteria) this;
        }

        public Criteria andServiceClassGreaterThanOrEqualTo(String value) {
            addCriterion("service_class >=", value, "serviceClass");
            return (Criteria) this;
        }

        public Criteria andServiceClassLessThan(String value) {
            addCriterion("service_class <", value, "serviceClass");
            return (Criteria) this;
        }

        public Criteria andServiceClassLessThanOrEqualTo(String value) {
            addCriterion("service_class <=", value, "serviceClass");
            return (Criteria) this;
        }

        public Criteria andServiceClassLike(String value) {
            addCriterion("service_class like", value, "serviceClass");
            return (Criteria) this;
        }

        public Criteria andServiceClassNotLike(String value) {
            addCriterion("service_class not like", value, "serviceClass");
            return (Criteria) this;
        }

        public Criteria andServiceClassIn(List<String> values) {
            addCriterion("service_class in", values, "serviceClass");
            return (Criteria) this;
        }

        public Criteria andServiceClassNotIn(List<String> values) {
            addCriterion("service_class not in", values, "serviceClass");
            return (Criteria) this;
        }

        public Criteria andServiceClassBetween(String value1, String value2) {
            addCriterion("service_class between", value1, value2, "serviceClass");
            return (Criteria) this;
        }

        public Criteria andServiceClassNotBetween(String value1, String value2) {
            addCriterion("service_class not between", value1, value2, "serviceClass");
            return (Criteria) this;
        }

        public Criteria andRetrieveIntervalIsNull() {
            addCriterion("retrieve_interval is null");
            return (Criteria) this;
        }

        public Criteria andRetrieveIntervalIsNotNull() {
            addCriterion("retrieve_interval is not null");
            return (Criteria) this;
        }

        public Criteria andRetrieveIntervalEqualTo(Integer value) {
            addCriterion("retrieve_interval =", value, "retrieveInterval");
            return (Criteria) this;
        }

        public Criteria andRetrieveIntervalNotEqualTo(Integer value) {
            addCriterion("retrieve_interval <>", value, "retrieveInterval");
            return (Criteria) this;
        }

        public Criteria andRetrieveIntervalGreaterThan(Integer value) {
            addCriterion("retrieve_interval >", value, "retrieveInterval");
            return (Criteria) this;
        }

        public Criteria andRetrieveIntervalGreaterThanOrEqualTo(Integer value) {
            addCriterion("retrieve_interval >=", value, "retrieveInterval");
            return (Criteria) this;
        }

        public Criteria andRetrieveIntervalLessThan(Integer value) {
            addCriterion("retrieve_interval <", value, "retrieveInterval");
            return (Criteria) this;
        }

        public Criteria andRetrieveIntervalLessThanOrEqualTo(Integer value) {
            addCriterion("retrieve_interval <=", value, "retrieveInterval");
            return (Criteria) this;
        }

        public Criteria andRetrieveIntervalIn(List<Integer> values) {
            addCriterion("retrieve_interval in", values, "retrieveInterval");
            return (Criteria) this;
        }

        public Criteria andRetrieveIntervalNotIn(List<Integer> values) {
            addCriterion("retrieve_interval not in", values, "retrieveInterval");
            return (Criteria) this;
        }

        public Criteria andRetrieveIntervalBetween(Integer value1, Integer value2) {
            addCriterion("retrieve_interval between", value1, value2, "retrieveInterval");
            return (Criteria) this;
        }

        public Criteria andRetrieveIntervalNotBetween(Integer value1, Integer value2) {
            addCriterion("retrieve_interval not between", value1, value2, "retrieveInterval");
            return (Criteria) this;
        }
        
        public Criteria andCancelledIsNull() {
            addCriterion("cancelled is null");
            return (Criteria) this;
        }

        public Criteria andCancelledIsNotNull() {
            addCriterion("cancelled is not null");
            return (Criteria) this;
        }

        public Criteria andCancelledEqualTo(Integer value) {
            addCriterion("cancelled =", value, "cancelled");
            return (Criteria) this;
        }

        public Criteria andCancelledNotEqualTo(Integer value) {
            addCriterion("cancelled <>", value, "cancelled");
            return (Criteria) this;
        }

        public Criteria andCancelledGreaterThan(Integer value) {
            addCriterion("cancelled >", value, "cancelled");
            return (Criteria) this;
        }

        public Criteria andCancelledGreaterThanOrEqualTo(Integer value) {
            addCriterion("cancelled >=", value, "cancelled");
            return (Criteria) this;
        }

        public Criteria andCancelledLessThan(Integer value) {
            addCriterion("cancelled <", value, "cancelled");
            return (Criteria) this;
        }

        public Criteria andCancelledLessThanOrEqualTo(Integer value) {
            addCriterion("cancelled <=", value, "cancelled");
            return (Criteria) this;
        }

        public Criteria andCancelledIn(List<Integer> values) {
            addCriterion("cancelled in", values, "cancelled");
            return (Criteria) this;
        }

        public Criteria andCancelledNotIn(List<Integer> values) {
            addCriterion("cancelled not in", values, "cancelled");
            return (Criteria) this;
        }

        public Criteria andCancelledBetween(Integer value1, Integer value2) {
            addCriterion("cancelled between", value1, value2, "cancelled");
            return (Criteria) this;
        }

        public Criteria andCancelledNotBetween(Integer value1, Integer value2) {
            addCriterion("cancelled not between", value1, value2, "cancelled");
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