package com.catenoid.dashbd.dao.model;

import java.util.ArrayList;
import java.util.List;

public class TransferConfigExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public TransferConfigExample() {
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

        public Criteria andQosGbrIsNull() {
            addCriterion("qos_gbr is null");
            return (Criteria) this;
        }

        public Criteria andQosGbrIsNotNull() {
            addCriterion("qos_gbr is not null");
            return (Criteria) this;
        }

        public Criteria andQosGbrEqualTo(Integer value) {
            addCriterion("qos_gbr =", value, "qosGbr");
            return (Criteria) this;
        }

        public Criteria andQosGbrNotEqualTo(Integer value) {
            addCriterion("qos_gbr <>", value, "qosGbr");
            return (Criteria) this;
        }

        public Criteria andQosGbrGreaterThan(Integer value) {
            addCriterion("qos_gbr >", value, "qosGbr");
            return (Criteria) this;
        }

        public Criteria andQosGbrGreaterThanOrEqualTo(Integer value) {
            addCriterion("qos_gbr >=", value, "qosGbr");
            return (Criteria) this;
        }

        public Criteria andQosGbrLessThan(Integer value) {
            addCriterion("qos_gbr <", value, "qosGbr");
            return (Criteria) this;
        }

        public Criteria andQosGbrLessThanOrEqualTo(Integer value) {
            addCriterion("qos_gbr <=", value, "qosGbr");
            return (Criteria) this;
        }

        public Criteria andQosGbrIn(List<Integer> values) {
            addCriterion("qos_gbr in", values, "qosGbr");
            return (Criteria) this;
        }

        public Criteria andQosGbrNotIn(List<Integer> values) {
            addCriterion("qos_gbr not in", values, "qosGbr");
            return (Criteria) this;
        }

        public Criteria andQosGbrBetween(Integer value1, Integer value2) {
            addCriterion("qos_gbr between", value1, value2, "qosGbr");
            return (Criteria) this;
        }

        public Criteria andQosGbrNotBetween(Integer value1, Integer value2) {
            addCriterion("qos_gbr not between", value1, value2, "qosGbr");
            return (Criteria) this;
        }

        public Criteria andQosQciIsNull() {
            addCriterion("qos_qci is null");
            return (Criteria) this;
        }

        public Criteria andQosQciIsNotNull() {
            addCriterion("qos_qci is not null");
            return (Criteria) this;
        }

        public Criteria andQosQciEqualTo(Integer value) {
            addCriterion("qos_qci =", value, "qosQci");
            return (Criteria) this;
        }

        public Criteria andQosQciNotEqualTo(Integer value) {
            addCriterion("qos_qci <>", value, "qosQci");
            return (Criteria) this;
        }

        public Criteria andQosQciGreaterThan(Integer value) {
            addCriterion("qos_qci >", value, "qosQci");
            return (Criteria) this;
        }

        public Criteria andQosQciGreaterThanOrEqualTo(Integer value) {
            addCriterion("qos_qci >=", value, "qosQci");
            return (Criteria) this;
        }

        public Criteria andQosQciLessThan(Integer value) {
            addCriterion("qos_qci <", value, "qosQci");
            return (Criteria) this;
        }

        public Criteria andQosQciLessThanOrEqualTo(Integer value) {
            addCriterion("qos_qci <=", value, "qosQci");
            return (Criteria) this;
        }

        public Criteria andQosQciIn(List<Integer> values) {
            addCriterion("qos_qci in", values, "qosQci");
            return (Criteria) this;
        }

        public Criteria andQosQciNotIn(List<Integer> values) {
            addCriterion("qos_qci not in", values, "qosQci");
            return (Criteria) this;
        }

        public Criteria andQosQciBetween(Integer value1, Integer value2) {
            addCriterion("qos_qci between", value1, value2, "qosQci");
            return (Criteria) this;
        }

        public Criteria andQosQciNotBetween(Integer value1, Integer value2) {
            addCriterion("qos_qci not between", value1, value2, "qosQci");
            return (Criteria) this;
        }

        public Criteria andArpLevelIsNull() {
            addCriterion("arp_level is null");
            return (Criteria) this;
        }

        public Criteria andArpLevelIsNotNull() {
            addCriterion("arp_level is not null");
            return (Criteria) this;
        }

        public Criteria andArpLevelEqualTo(Integer value) {
            addCriterion("arp_level =", value, "qosArpLevel");
            return (Criteria) this;
        }

        public Criteria andArpLevelNotEqualTo(Integer value) {
            addCriterion("arp_level <>", value, "qosArpLevel");
            return (Criteria) this;
        }

        public Criteria andArpLevelGreaterThan(Integer value) {
            addCriterion("arp_level >", value, "qosArpLevel");
            return (Criteria) this;
        }

        public Criteria andArpLevelGreaterThanOrEqualTo(Integer value) {
            addCriterion("arp_level >=", value, "qosArpLevel");
            return (Criteria) this;
        }

        public Criteria andArpLevelLessThan(Integer value) {
            addCriterion("arp_level <", value, "qosArpLevel");
            return (Criteria) this;
        }

        public Criteria andArpLevelLessThanOrEqualTo(Integer value) {
            addCriterion("arp_level <=", value, "qosArpLevel");
            return (Criteria) this;
        }

        public Criteria andArpLevelIn(List<Integer> values) {
            addCriterion("arp_level in", values, "qosArpLevel");
            return (Criteria) this;
        }

        public Criteria andArpLevelNotIn(List<Integer> values) {
            addCriterion("arp_level not in", values, "qosArpLevel");
            return (Criteria) this;
        }

        public Criteria andArpLevelBetween(Integer value1, Integer value2) {
            addCriterion("arp_level between", value1, value2, "qosArpLevel");
            return (Criteria) this;
        }

        public Criteria andArpLevelNotBetween(Integer value1, Integer value2) {
            addCriterion("arp_level not between", value1, value2, "qosArpLevel");
            return (Criteria) this;
        }

        public Criteria andArpPreEmptionCapabilityIsNull() {
            addCriterion("arp_pre_emption_capability is null");
            return (Criteria) this;
        }

        public Criteria andArpPreEmptionCapabilityIsNotNull() {
            addCriterion("arp_pre_emption_capability is not null");
            return (Criteria) this;
        }

        public Criteria andArpPreEmptionCapabilityEqualTo(Integer value) {
            addCriterion("arp_pre_emption_capability =", value, "qosArpPreEmptionCapability");
            return (Criteria) this;
        }

        public Criteria andArpPreEmptionCapabilityNotEqualTo(Integer value) {
            addCriterion("arp_pre_emption_capability <>", value, "qosArpPreEmptionCapability");
            return (Criteria) this;
        }

        public Criteria andArpPreEmptionCapabilityGreaterThan(Integer value) {
            addCriterion("arp_pre_emption_capability >", value, "qosArpPreEmptionCapability");
            return (Criteria) this;
        }

        public Criteria andArpPreEmptionCapabilityGreaterThanOrEqualTo(Integer value) {
            addCriterion("arp_pre_emption_capability >=", value, "qosArpPreEmptionCapability");
            return (Criteria) this;
        }

        public Criteria andArpPreEmptionCapabilityLessThan(Integer value) {
            addCriterion("arp_pre_emption_capability <", value, "qosArpPreEmptionCapability");
            return (Criteria) this;
        }

        public Criteria andArpPreEmptionCapabilityLessThanOrEqualTo(Integer value) {
            addCriterion("arp_pre_emption_capability <=", value, "qosArpPreEmptionCapability");
            return (Criteria) this;
        }

        public Criteria andArpPreEmptionCapabilityIn(List<Integer> values) {
            addCriterion("arp_pre_emption_capability in", values, "qosArpPreEmptionCapability");
            return (Criteria) this;
        }

        public Criteria andArpPreEmptionCapabilityNotIn(List<Integer> values) {
            addCriterion("arp_pre_emption_capability not in", values, "qosArpPreEmptionCapability");
            return (Criteria) this;
        }

        public Criteria andArpPreEmptionCapabilityBetween(Integer value1, Integer value2) {
            addCriterion("arp_pre_emption_capability between", value1, value2, "qosArpPreEmptionCapability");
            return (Criteria) this;
        }

        public Criteria andArpPreEmptionCapabilityNotBetween(Integer value1, Integer value2) {
            addCriterion("arp_pre_emption_capability not between", value1, value2, "qosArpPreEmptionCapability");
            return (Criteria) this;
        }

        public Criteria andArpPreEmptionVulnerabilityIsNull() {
            addCriterion("arp_pre_emption_vulnerability is null");
            return (Criteria) this;
        }

        public Criteria andArpPreEmptionVulnerabilityIsNotNull() {
            addCriterion("arp_pre_emption_vulnerability is not null");
            return (Criteria) this;
        }

        public Criteria andArpPreEmptionVulnerabilityEqualTo(Integer value) {
            addCriterion("arp_pre_emption_vulnerability =", value, "qosArpPreEmptionVulnerability");
            return (Criteria) this;
        }

        public Criteria andArpPreEmptionVulnerabilityNotEqualTo(Integer value) {
            addCriterion("arp_pre_emption_vulnerability <>", value, "qosArpPreEmptionVulnerability");
            return (Criteria) this;
        }

        public Criteria andArpPreEmptionVulnerabilityGreaterThan(Integer value) {
            addCriterion("arp_pre_emption_vulnerability >", value, "qosArpPreEmptionVulnerability");
            return (Criteria) this;
        }

        public Criteria andArpPreEmptionVulnerabilityGreaterThanOrEqualTo(Integer value) {
            addCriterion("arp_pre_emption_vulnerability >=", value, "qosArpPreEmptionVulnerability");
            return (Criteria) this;
        }

        public Criteria andArpPreEmptionVulnerabilityLessThan(Integer value) {
            addCriterion("arp_pre_emption_vulnerability <", value, "qosArpPreEmptionVulnerability");
            return (Criteria) this;
        }

        public Criteria andArpPreEmptionVulnerabilityLessThanOrEqualTo(Integer value) {
            addCriterion("arp_pre_emption_vulnerability <=", value, "qosArpPreEmptionVulnerability");
            return (Criteria) this;
        }

        public Criteria andArpPreEmptionVulnerabilityIn(List<Integer> values) {
            addCriterion("arp_pre_emption_vulnerability in", values, "qosArpPreEmptionVulnerability");
            return (Criteria) this;
        }

        public Criteria andArpPreEmptionVulnerabilityNotIn(List<Integer> values) {
            addCriterion("arp_pre_emption_vulnerability not in", values, "qosArpPreEmptionVulnerability");
            return (Criteria) this;
        }

        public Criteria andArpPreEmptionVulnerabilityBetween(Integer value1, Integer value2) {
            addCriterion("arp_pre_emption_vulnerability between", value1, value2, "qosArpPreEmptionVulnerability");
            return (Criteria) this;
        }

        public Criteria andArpPreEmptionVulnerabilityNotBetween(Integer value1, Integer value2) {
            addCriterion("arp_pre_emption_vulnerability not between", value1, value2, "qosArpPreEmptionVulnerability");
            return (Criteria) this;
        }

        public Criteria andFecTypeIsNull() {
            addCriterion("fec_type is null");
            return (Criteria) this;
        }

        public Criteria andFecTypeIsNotNull() {
            addCriterion("fec_type is not null");
            return (Criteria) this;
        }

        public Criteria andFecTypeEqualTo(String value) {
            addCriterion("fec_type =", value, "qosFecType");
            return (Criteria) this;
        }

        public Criteria andFecTypeNotEqualTo(String value) {
            addCriterion("fec_type <>", value, "qosFecType");
            return (Criteria) this;
        }

        public Criteria andFecTypeGreaterThan(String value) {
            addCriterion("fec_type >", value, "qosFecType");
            return (Criteria) this;
        }

        public Criteria andFecTypeGreaterThanOrEqualTo(String value) {
            addCriterion("fec_type >=", value, "qosFecType");
            return (Criteria) this;
        }

        public Criteria andFecTypeLessThan(String value) {
            addCriterion("fec_type <", value, "qosFecType");
            return (Criteria) this;
        }

        public Criteria andFecTypeLessThanOrEqualTo(String value) {
            addCriterion("fec_type <=", value, "qosFecType");
            return (Criteria) this;
        }

        public Criteria andFecTypeLike(String value) {
            addCriterion("fec_type like", value, "qosFecType");
            return (Criteria) this;
        }

        public Criteria andFecTypeNotLike(String value) {
            addCriterion("fec_type not like", value, "qosFecType");
            return (Criteria) this;
        }

        public Criteria andFecTypeIn(List<String> values) {
            addCriterion("fec_type in", values, "qosFecType");
            return (Criteria) this;
        }

        public Criteria andFecTypeNotIn(List<String> values) {
            addCriterion("fec_type not in", values, "qosFecType");
            return (Criteria) this;
        }

        public Criteria andFecTypeBetween(String value1, String value2) {
            addCriterion("fec_type between", value1, value2, "qosFecType");
            return (Criteria) this;
        }

        public Criteria andFecTypeNotBetween(String value1, String value2) {
            addCriterion("fec_type not between", value1, value2, "qosFecType");
            return (Criteria) this;
        }

        public Criteria andFecRatioIsNull() {
            addCriterion("fec_ratio is null");
            return (Criteria) this;
        }

        public Criteria andFecRatioIsNotNull() {
            addCriterion("fec_ratio is not null");
            return (Criteria) this;
        }

        public Criteria andFecRatioEqualTo(Integer value) {
            addCriterion("fec_ratio =", value, "qosFecRatio");
            return (Criteria) this;
        }

        public Criteria andFecRatioNotEqualTo(Integer value) {
            addCriterion("fec_ratio <>", value, "qosFecRatio");
            return (Criteria) this;
        }

        public Criteria andFecRatioGreaterThan(Integer value) {
            addCriterion("fec_ratio >", value, "qosFecRatio");
            return (Criteria) this;
        }

        public Criteria andFecRatioGreaterThanOrEqualTo(Integer value) {
            addCriterion("fec_ratio >=", value, "qosFecRatio");
            return (Criteria) this;
        }

        public Criteria andFecRatioLessThan(Integer value) {
            addCriterion("fec_ratio <", value, "qosFecRatio");
            return (Criteria) this;
        }

        public Criteria andFecRatioLessThanOrEqualTo(Integer value) {
            addCriterion("fec_ratio <=", value, "qosFecRatio");
            return (Criteria) this;
        }

        public Criteria andFecRatioIn(List<Integer> values) {
            addCriterion("fec_ratio in", values, "qosFecRatio");
            return (Criteria) this;
        }

        public Criteria andFecRatioNotIn(List<Integer> values) {
            addCriterion("fec_ratio not in", values, "qosFecRatio");
            return (Criteria) this;
        }

        public Criteria andFecRatioBetween(Integer value1, Integer value2) {
            addCriterion("fec_ratio between", value1, value2, "qosFecRatio");
            return (Criteria) this;
        }

        public Criteria andFecRatioNotBetween(Integer value1, Integer value2) {
            addCriterion("fec_ratio not between", value1, value2, "qosFecRatio");
            return (Criteria) this;
        }

        public Criteria andSegmentAvailableOffsetIsNull() {
            addCriterion("segment_available_offset is null");
            return (Criteria) this;
        }

        public Criteria andSegmentAvailableOffsetIsNotNull() {
            addCriterion("segment_available_offset is not null");
            return (Criteria) this;
        }

        public Criteria andSegmentAvailableOffsetEqualTo(Integer value) {
            addCriterion("segment_available_offset =", value, "segmentAvailableOffset");
            return (Criteria) this;
        }

        public Criteria andSegmentAvailableOffsetNotEqualTo(Integer value) {
            addCriterion("segment_available_offset <>", value, "segmentAvailableOffset");
            return (Criteria) this;
        }

        public Criteria andSegmentAvailableOffsetGreaterThan(Integer value) {
            addCriterion("segment_available_offset >", value, "segmentAvailableOffset");
            return (Criteria) this;
        }

        public Criteria andSegmentAvailableOffsetGreaterThanOrEqualTo(Integer value) {
            addCriterion("segment_available_offset >=", value, "segmentAvailableOffset");
            return (Criteria) this;
        }

        public Criteria andSegmentAvailableOffsetLessThan(Integer value) {
            addCriterion("segment_available_offset <", value, "segmentAvailableOffset");
            return (Criteria) this;
        }

        public Criteria andSegmentAvailableOffsetLessThanOrEqualTo(Integer value) {
            addCriterion("segment_available_offset <=", value, "segmentAvailableOffset");
            return (Criteria) this;
        }

        public Criteria andSegmentAvailableOffsetIn(List<Integer> values) {
            addCriterion("segment_available_offset in", values, "segmentAvailableOffset");
            return (Criteria) this;
        }

        public Criteria andSegmentAvailableOffsetNotIn(List<Integer> values) {
            addCriterion("segment_available_offset not in", values, "segmentAvailableOffset");
            return (Criteria) this;
        }

        public Criteria andSegmentAvailableOffsetBetween(Integer value1, Integer value2) {
            addCriterion("segment_available_offset between", value1, value2, "segmentAvailableOffset");
            return (Criteria) this;
        }

        public Criteria andSegmentAvailableOffsetNotBetween(Integer value1, Integer value2) {
            addCriterion("segment_available_offset not between", value1, value2, "segmentAvailableOffset");
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