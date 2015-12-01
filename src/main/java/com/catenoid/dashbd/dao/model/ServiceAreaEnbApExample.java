package com.catenoid.dashbd.dao.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ServiceAreaEnbApExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public ServiceAreaEnbApExample() {
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
        Criteria criteria = null;
        if (oredCriteria.size() == 0) {
        	criteria = createCriteriaInternal();
            oredCriteria.add(criteria);
        }
        else {
        	criteria = oredCriteria.get(0);
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

        public Criteria andServiceAreaIdIsNull() {
            addCriterion("service_area_id is null");
            return (Criteria) this;
        }

        public Criteria andServiceAreaIdIsNotNull() {
            addCriterion("service_area_id is not null");
            return (Criteria) this;
        }

        public Criteria andServiceAreaIdEqualTo(Integer value) {
            addCriterion("service_area_id =", value, "serviceAreaId");
            return (Criteria) this;
        }

        public Criteria andServiceAreaIdNotEqualTo(Integer value) {
            addCriterion("service_area_id <>", value, "serviceAreaId");
            return (Criteria) this;
        }

        public Criteria andServiceAreaIdGreaterThan(Integer value) {
            addCriterion("service_area_id >", value, "serviceAreaId");
            return (Criteria) this;
        }

        public Criteria andServiceAreaIdGreaterThanOrEqualTo(Integer value) {
            addCriterion("service_area_id >=", value, "serviceAreaId");
            return (Criteria) this;
        }

        public Criteria andServiceAreaIdLessThan(Integer value) {
            addCriterion("service_area_id <", value, "serviceAreaId");
            return (Criteria) this;
        }

        public Criteria andServiceAreaIdLessThanOrEqualTo(Integer value) {
            addCriterion("service_area_id <=", value, "serviceAreaId");
            return (Criteria) this;
        }

        public Criteria andServiceAreaIdIn(List<Integer> values) {
            addCriterion("service_area_id in", values, "serviceAreaId");
            return (Criteria) this;
        }

        public Criteria andServiceAreaIdNotIn(List<Integer> values) {
            addCriterion("service_area_id not in", values, "serviceAreaId");
            return (Criteria) this;
        }

        public Criteria andServiceAreaIdBetween(Integer value1, Integer value2) {
            addCriterion("service_area_id between", value1, value2, "serviceAreaId");
            return (Criteria) this;
        }

        public Criteria andServiceAreaIdNotBetween(Integer value1, Integer value2) {
            addCriterion("service_area_id not between", value1, value2, "serviceAreaId");
            return (Criteria) this;
        }

        public Criteria andEnbApIdIsNull() {
            addCriterion("enb_ap_id is null");
            return (Criteria) this;
        }

        public Criteria andEnbApIdIsNotNull() {
            addCriterion("enb_ap_id is not null");
            return (Criteria) this;
        }

        public Criteria andEnbApIdEqualTo(Integer value) {
            addCriterion("enb_ap_id =", value, "enbApId");
            return (Criteria) this;
        }

        public Criteria andEnbApIdNotEqualTo(Integer value) {
            addCriterion("enb_ap_id <>", value, "enbApId");
            return (Criteria) this;
        }

        public Criteria andEnbApIdGreaterThan(Integer value) {
            addCriterion("enb_ap_id >", value, "enbApId");
            return (Criteria) this;
        }

        public Criteria andEnbApIdGreaterThanOrEqualTo(Integer value) {
            addCriterion("enb_ap_id >=", value, "enbApId");
            return (Criteria) this;
        }

        public Criteria andEnbApIdLessThan(Integer value) {
            addCriterion("enb_ap_id <", value, "enbApId");
            return (Criteria) this;
        }

        public Criteria andEnbApIdLessThanOrEqualTo(Integer value) {
            addCriterion("enb_ap_id <=", value, "enbApId");
            return (Criteria) this;
        }

        public Criteria andEnbApIdIn(List<Integer> values) {
            addCriterion("enb_ap_id in", values, "enbApId");
            return (Criteria) this;
        }

        public Criteria andEnbApIdNotIn(List<Integer> values) {
            addCriterion("enb_ap_id not in", values, "enbApId");
            return (Criteria) this;
        }

        public Criteria andEnbApIdBetween(Integer value1, Integer value2) {
            addCriterion("enb_ap_id between", value1, value2, "enbApId");
            return (Criteria) this;
        }

        public Criteria andEnbApIdNotBetween(Integer value1, Integer value2) {
            addCriterion("enb_ap_id not between", value1, value2, "enbApId");
            return (Criteria) this;
        }
        
        public Criteria andPlmnIsNull() {
            addCriterion("plmn is null");
            return (Criteria) this;
        }

        public Criteria andPlmnIsNotNull() {
            addCriterion("plmn is not null");
            return (Criteria) this;
        }

        public Criteria andPlmnEqualTo(String value) {
            addCriterion("plmn =", value, "plmn");
            return (Criteria) this;
        }

        public Criteria andPlmnNotEqualTo(String value) {
            addCriterion("plmn <>", value, "plmn");
            return (Criteria) this;
        }

        public Criteria andPlmnGreaterThan(String value) {
            addCriterion("plmn >", value, "plmn");
            return (Criteria) this;
        }

        public Criteria andPlmnGreaterThanOrEqualTo(String value) {
            addCriterion("plmn >=", value, "plmn");
            return (Criteria) this;
        }

        public Criteria andPlmnLessThan(String value) {
            addCriterion("plmn <", value, "plmn");
            return (Criteria) this;
        }

        public Criteria andPlmnLessThanOrEqualTo(String value) {
            addCriterion("plmn <=", value, "plmn");
            return (Criteria) this;
        }

        public Criteria andPlmnLike(String value) {
            addCriterion("plmn like", value, "plmn");
            return (Criteria) this;
        }

        public Criteria andPlmnNotLike(String value) {
            addCriterion("plmn not like", value, "plmn");
            return (Criteria) this;
        }

        public Criteria andPlmnIn(List<String> values) {
            addCriterion("plmn in", values, "plmn");
            return (Criteria) this;
        }

        public Criteria andPlmnNotIn(List<String> values) {
            addCriterion("plmn not in", values, "plmn");
            return (Criteria) this;
        }

        public Criteria andPlmnBetween(String value1, String value2) {
            addCriterion("plmn between", value1, value2, "plmn");
            return (Criteria) this;
        }

        public Criteria andPlmnNotBetween(String value1, String value2) {
            addCriterion("plmn not between", value1, value2, "plmn");
            return (Criteria) this;
        }
        
        public Criteria andMbsfnIsNull() {
            addCriterion("mbsfn is null");
            return (Criteria) this;
        }

        public Criteria andMbsfnIsNotNull() {
            addCriterion("mbsfn is not null");
            return (Criteria) this;
        }

        public Criteria andMbsfnEqualTo(String value) {
            addCriterion("mbsfn =", value, "mbsfn");
            return (Criteria) this;
        }

        public Criteria andMbsfnNotEqualTo(String value) {
            addCriterion("mbsfn <>", value, "mbsfn");
            return (Criteria) this;
        }

        public Criteria andMbsfnGreaterThan(String value) {
            addCriterion("mbsfn >", value, "mbsfn");
            return (Criteria) this;
        }

        public Criteria andMbsfnGreaterThanOrEqualTo(String value) {
            addCriterion("mbsfn >=", value, "mbsfn");
            return (Criteria) this;
        }

        public Criteria andMbsfnLessThan(String value) {
            addCriterion("mbsfn <", value, "mbsfn");
            return (Criteria) this;
        }

        public Criteria andMbsfnLessThanOrEqualTo(String value) {
            addCriterion("mbsfn <=", value, "mbsfn");
            return (Criteria) this;
        }

        public Criteria andMbsfnLike(String value) {
            addCriterion("mbsfn like", value, "mbsfn");
            return (Criteria) this;
        }

        public Criteria andMbsfnNotLike(String value) {
            addCriterion("mbsfn not like", value, "mbsfn");
            return (Criteria) this;
        }

        public Criteria andMbsfnIn(List<String> values) {
            addCriterion("mbsfn in", values, "mbsfn");
            return (Criteria) this;
        }

        public Criteria andMbsfnNotIn(List<String> values) {
            addCriterion("mbsfn not in", values, "mbsfn");
            return (Criteria) this;
        }

        public Criteria andMbsfnBetween(String value1, String value2) {
            addCriterion("mbsfn between", value1, value2, "mbsfn");
            return (Criteria) this;
        }

        public Criteria andMbsfnNotBetween(String value1, String value2) {
            addCriterion("mbsfn not between", value1, value2, "mbsfn");
            return (Criteria) this;
        }
        
        public Criteria andLongitudeIsNull() {
            addCriterion("longitude is null");
            return (Criteria) this;
        }

        public Criteria andLongitudeIsNotNull() {
            addCriterion("longitude is not null");
            return (Criteria) this;
        }

        public Criteria andLongitudeEqualTo(BigDecimal value) {
            addCriterion("longitude =", value, "longitude");
            return (Criteria) this;
        }

        public Criteria andLongitudeNotEqualTo(BigDecimal value) {
            addCriterion("longitude <>", value, "longitude");
            return (Criteria) this;
        }

        public Criteria andLongitudeGreaterThan(BigDecimal value) {
            addCriterion("longitude >", value, "longitude");
            return (Criteria) this;
        }

        public Criteria andLongitudeGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("longitude >=", value, "longitude");
            return (Criteria) this;
        }

        public Criteria andLongitudeLessThan(BigDecimal value) {
            addCriterion("longitude <", value, "longitude");
            return (Criteria) this;
        }

        public Criteria andLongitudeLessThanOrEqualTo(BigDecimal value) {
            addCriterion("longitude <=", value, "longitude");
            return (Criteria) this;
        }

        public Criteria andLongitudeIn(List<BigDecimal> values) {
            addCriterion("longitude in", values, "longitude");
            return (Criteria) this;
        }

        public Criteria andLongitudeNotIn(List<BigDecimal> values) {
            addCriterion("longitude not in", values, "longitude");
            return (Criteria) this;
        }

        public Criteria andLongitudeBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("longitude between", value1, value2, "longitude");
            return (Criteria) this;
        }

        public Criteria andLongitudeNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("longitude not between", value1, value2, "longitude");
            return (Criteria) this;
        }

        public Criteria andLatitudeIsNull() {
            addCriterion("latitude is null");
            return (Criteria) this;
        }

        public Criteria andLatitudeIsNotNull() {
            addCriterion("latitude is not null");
            return (Criteria) this;
        }

        public Criteria andLatitudeEqualTo(BigDecimal value) {
            addCriterion("latitude =", value, "latitude");
            return (Criteria) this;
        }

        public Criteria andLatitudeNotEqualTo(BigDecimal value) {
            addCriterion("latitude <>", value, "latitude");
            return (Criteria) this;
        }

        public Criteria andLatitudeGreaterThan(BigDecimal value) {
            addCriterion("latitude >", value, "latitude");
            return (Criteria) this;
        }

        public Criteria andLatitudeGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("latitude >=", value, "latitude");
            return (Criteria) this;
        }

        public Criteria andLatitudeLessThan(BigDecimal value) {
            addCriterion("latitude <", value, "latitude");
            return (Criteria) this;
        }

        public Criteria andLatitudeLessThanOrEqualTo(BigDecimal value) {
            addCriterion("latitude <=", value, "latitude");
            return (Criteria) this;
        }

        public Criteria andLatitudeIn(List<BigDecimal> values) {
            addCriterion("latitude in", values, "latitude");
            return (Criteria) this;
        }

        public Criteria andLatitudeNotIn(List<BigDecimal> values) {
            addCriterion("latitude not in", values, "latitude");
            return (Criteria) this;
        }

        public Criteria andLatitudeBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("latitude between", value1, value2, "latitude");
            return (Criteria) this;
        }

        public Criteria andLatitudeNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("latitude not between", value1, value2, "latitude");
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