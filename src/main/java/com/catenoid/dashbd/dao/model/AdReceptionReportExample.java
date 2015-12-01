package com.catenoid.dashbd.dao.model;

import java.util.ArrayList;
import java.util.List;

public class AdReceptionReportExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public AdReceptionReportExample() {
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

        public Criteria andServiceIdIsNull() {
            addCriterion("service_id is null");
            return (Criteria) this;
        }

        public Criteria andServiceIdIsNotNull() {
            addCriterion("service_id is not null");
            return (Criteria) this;
        }

        public Criteria andServiceIdEqualTo(Integer value) {
            addCriterion("service_id =", value, "serviceId");
            return (Criteria) this;
        }

        public Criteria andServiceIdNotEqualTo(Integer value) {
            addCriterion("service_id <>", value, "serviceId");
            return (Criteria) this;
        }

        public Criteria andServiceIdGreaterThan(Integer value) {
            addCriterion("service_id >", value, "serviceId");
            return (Criteria) this;
        }

        public Criteria andServiceIdGreaterThanOrEqualTo(Integer value) {
            addCriterion("service_id >=", value, "serviceId");
            return (Criteria) this;
        }

        public Criteria andServiceIdLessThan(Integer value) {
            addCriterion("service_id <", value, "serviceId");
            return (Criteria) this;
        }

        public Criteria andServiceIdLessThanOrEqualTo(Integer value) {
            addCriterion("service_id <=", value, "serviceId");
            return (Criteria) this;
        }

        public Criteria andServiceIdIn(List<Integer> values) {
            addCriterion("service_id in", values, "serviceId");
            return (Criteria) this;
        }

        public Criteria andServiceIdNotIn(List<Integer> values) {
            addCriterion("service_id not in", values, "serviceId");
            return (Criteria) this;
        }

        public Criteria andServiceIdBetween(Integer value1, Integer value2) {
            addCriterion("service_id between", value1, value2, "serviceId");
            return (Criteria) this;
        }

        public Criteria andServiceIdNotBetween(Integer value1, Integer value2) {
            addCriterion("service_id not between", value1, value2, "serviceId");
            return (Criteria) this;
        }

        public Criteria andReportTypeIsNull() {
            addCriterion("report_type is null");
            return (Criteria) this;
        }

        public Criteria andReportTypeIsNotNull() {
            addCriterion("report_type is not null");
            return (Criteria) this;
        }

        public Criteria andReportTypeEqualTo(String value) {
            addCriterion("report_type =", value, "reportType");
            return (Criteria) this;
        }

        public Criteria andReportTypeNotEqualTo(String value) {
            addCriterion("report_type <>", value, "reportType");
            return (Criteria) this;
        }

        public Criteria andReportTypeGreaterThan(String value) {
            addCriterion("report_type >", value, "reportType");
            return (Criteria) this;
        }

        public Criteria andReportTypeGreaterThanOrEqualTo(String value) {
            addCriterion("report_type >=", value, "reportType");
            return (Criteria) this;
        }

        public Criteria andReportTypeLessThan(String value) {
            addCriterion("report_type <", value, "reportType");
            return (Criteria) this;
        }

        public Criteria andReportTypeLessThanOrEqualTo(String value) {
            addCriterion("report_type <=", value, "reportType");
            return (Criteria) this;
        }

        public Criteria andReportTypeLike(String value) {
            addCriterion("report_type like", value, "reportType");
            return (Criteria) this;
        }

        public Criteria andReportTypeNotLike(String value) {
            addCriterion("report_type not like", value, "reportType");
            return (Criteria) this;
        }

        public Criteria andReportTypeIn(List<String> values) {
            addCriterion("report_type in", values, "reportType");
            return (Criteria) this;
        }

        public Criteria andReportTypeNotIn(List<String> values) {
            addCriterion("report_type not in", values, "reportType");
            return (Criteria) this;
        }

        public Criteria andReportTypeBetween(String value1, String value2) {
            addCriterion("report_type between", value1, value2, "reportType");
            return (Criteria) this;
        }

        public Criteria andReportTypeNotBetween(String value1, String value2) {
            addCriterion("report_type not between", value1, value2, "reportType");
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

        public Criteria andSamplePercentageIsNull() {
            addCriterion("sample_percentage is null");
            return (Criteria) this;
        }

        public Criteria andSamplePercentageIsNotNull() {
            addCriterion("sample_percentage is not null");
            return (Criteria) this;
        }

        public Criteria andSamplePercentageEqualTo(Integer value) {
            addCriterion("sample_percentage =", value, "samplePercentage");
            return (Criteria) this;
        }

        public Criteria andSamplePercentageNotEqualTo(Integer value) {
            addCriterion("sample_percentage <>", value, "samplePercentage");
            return (Criteria) this;
        }

        public Criteria andSamplePercentageGreaterThan(Integer value) {
            addCriterion("sample_percentage >", value, "samplePercentage");
            return (Criteria) this;
        }

        public Criteria andSamplePercentageGreaterThanOrEqualTo(Integer value) {
            addCriterion("sample_percentage >=", value, "samplePercentage");
            return (Criteria) this;
        }

        public Criteria andSamplePercentageLessThan(Integer value) {
            addCriterion("sample_percentage <", value, "samplePercentage");
            return (Criteria) this;
        }

        public Criteria andSamplePercentageLessThanOrEqualTo(Integer value) {
            addCriterion("sample_percentage <=", value, "samplePercentage");
            return (Criteria) this;
        }

        public Criteria andSamplePercentageIn(List<Integer> values) {
            addCriterion("sample_percentage in", values, "samplePercentage");
            return (Criteria) this;
        }

        public Criteria andSamplePercentageNotIn(List<Integer> values) {
            addCriterion("sample_percentage not in", values, "samplePercentage");
            return (Criteria) this;
        }

        public Criteria andSamplePercentageBetween(Integer value1, Integer value2) {
            addCriterion("sample_percentage between", value1, value2, "samplePercentage");
            return (Criteria) this;
        }

        public Criteria andSamplePercentageNotBetween(Integer value1, Integer value2) {
            addCriterion("sample_percentage not between", value1, value2, "samplePercentage");
            return (Criteria) this;
        }

        public Criteria andOffsetTimeIsNull() {
            addCriterion("offset_time is null");
            return (Criteria) this;
        }

        public Criteria andOffsetTimeIsNotNull() {
            addCriterion("offset_time is not null");
            return (Criteria) this;
        }

        public Criteria andOffsetTimeEqualTo(Integer value) {
            addCriterion("offset_time =", value, "offsetTime");
            return (Criteria) this;
        }

        public Criteria andOffsetTimeNotEqualTo(Integer value) {
            addCriterion("offset_time <>", value, "offsetTime");
            return (Criteria) this;
        }

        public Criteria andOffsetTimeGreaterThan(Integer value) {
            addCriterion("offset_time >", value, "offsetTime");
            return (Criteria) this;
        }

        public Criteria andOffsetTimeGreaterThanOrEqualTo(Integer value) {
            addCriterion("offset_time >=", value, "offsetTime");
            return (Criteria) this;
        }

        public Criteria andOffsetTimeLessThan(Integer value) {
            addCriterion("offset_time <", value, "offsetTime");
            return (Criteria) this;
        }

        public Criteria andOffsetTimeLessThanOrEqualTo(Integer value) {
            addCriterion("offset_time <=", value, "offsetTime");
            return (Criteria) this;
        }

        public Criteria andOffsetTimeIn(List<Integer> values) {
            addCriterion("offset_time in", values, "offsetTime");
            return (Criteria) this;
        }

        public Criteria andOffsetTimeNotIn(List<Integer> values) {
            addCriterion("offset_time not in", values, "offsetTime");
            return (Criteria) this;
        }

        public Criteria andOffsetTimeBetween(Integer value1, Integer value2) {
            addCriterion("offset_time between", value1, value2, "offsetTime");
            return (Criteria) this;
        }

        public Criteria andOffsetTimeNotBetween(Integer value1, Integer value2) {
            addCriterion("offset_time not between", value1, value2, "offsetTime");
            return (Criteria) this;
        }

        public Criteria andRandomTimeIsNull() {
            addCriterion("random_time is null");
            return (Criteria) this;
        }

        public Criteria andRandomTimeIsNotNull() {
            addCriterion("random_time is not null");
            return (Criteria) this;
        }

        public Criteria andRandomTimeEqualTo(Integer value) {
            addCriterion("random_time =", value, "randomTime");
            return (Criteria) this;
        }

        public Criteria andRandomTimeNotEqualTo(Integer value) {
            addCriterion("random_time <>", value, "randomTime");
            return (Criteria) this;
        }

        public Criteria andRandomTimeGreaterThan(Integer value) {
            addCriterion("random_time >", value, "randomTime");
            return (Criteria) this;
        }

        public Criteria andRandomTimeGreaterThanOrEqualTo(Integer value) {
            addCriterion("random_time >=", value, "randomTime");
            return (Criteria) this;
        }

        public Criteria andRandomTimeLessThan(Integer value) {
            addCriterion("random_time <", value, "randomTime");
            return (Criteria) this;
        }

        public Criteria andRandomTimeLessThanOrEqualTo(Integer value) {
            addCriterion("random_time <=", value, "randomTime");
            return (Criteria) this;
        }

        public Criteria andRandomTimeIn(List<Integer> values) {
            addCriterion("random_time in", values, "randomTime");
            return (Criteria) this;
        }

        public Criteria andRandomTimeNotIn(List<Integer> values) {
            addCriterion("random_time not in", values, "randomTime");
            return (Criteria) this;
        }

        public Criteria andRandomTimeBetween(Integer value1, Integer value2) {
            addCriterion("random_time between", value1, value2, "randomTime");
            return (Criteria) this;
        }

        public Criteria andRandomTimeNotBetween(Integer value1, Integer value2) {
            addCriterion("random_time not between", value1, value2, "randomTime");
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