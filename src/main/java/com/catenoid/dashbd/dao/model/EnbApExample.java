package com.catenoid.dashbd.dao.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class EnbApExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public EnbApExample() {
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

    public Criteria and() {
        Criteria criteria = createCriteriaInternal();
        oredCriteria.add(criteria);
        return criteria;
    }
    
    public void and(Criteria criteria) {
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

        public Criteria andNameIsNull() {
            addCriterion("name is null");
            return (Criteria) this;
        }

        public Criteria andNameIsNotNull() {
            addCriterion("name is not null");
            return (Criteria) this;
        }

        public Criteria andNameEqualTo(String value) {
            addCriterion("name =", value, "name");
            return (Criteria) this;
        }

        public Criteria andNameNotEqualTo(String value) {
            addCriterion("name <>", value, "name");
            return (Criteria) this;
        }

        public Criteria andNameGreaterThan(String value) {
            addCriterion("name >", value, "name");
            return (Criteria) this;
        }

        public Criteria andNameGreaterThanOrEqualTo(String value) {
            addCriterion("name >=", value, "name");
            return (Criteria) this;
        }

        public Criteria andNameLessThan(String value) {
            addCriterion("name <", value, "name");
            return (Criteria) this;
        }

        public Criteria andNameLessThanOrEqualTo(String value) {
            addCriterion("name <=", value, "name");
            return (Criteria) this;
        }

        public Criteria andNameLike(String value) {
            addCriterion("name like", value, "name");
            return (Criteria) this;
        }

        public Criteria andNameNotLike(String value) {
            addCriterion("name not like", value, "name");
            return (Criteria) this;
        }

        public Criteria andNameIn(List<String> values) {
            addCriterion("name in", values, "name");
            return (Criteria) this;
        }

        public Criteria andNameNotIn(List<String> values) {
            addCriterion("name not in", values, "name");
            return (Criteria) this;
        }

        public Criteria andNameBetween(String value1, String value2) {
            addCriterion("name between", value1, value2, "name");
            return (Criteria) this;
        }

        public Criteria andNameNotBetween(String value1, String value2) {
            addCriterion("name not between", value1, value2, "name");
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

        public Criteria andCircleIsNull() {
            addCriterion("circle is null");
            return (Criteria) this;
        }

        public Criteria andCircleIsNotNull() {
            addCriterion("circle is not null");
            return (Criteria) this;
        }

        public Criteria andCircleEqualTo(String value) {
            addCriterion("circle =", value, "circle");
            return (Criteria) this;
        }

        public Criteria andCircleNotEqualTo(String value) {
            addCriterion("circle <>", value, "circle");
            return (Criteria) this;
        }

        public Criteria andCircleGreaterThan(String value) {
            addCriterion("circle >", value, "circle");
            return (Criteria) this;
        }

        public Criteria andCircleGreaterThanOrEqualTo(String value) {
            addCriterion("circle >=", value, "circle");
            return (Criteria) this;
        }

        public Criteria andCircleLessThan(String value) {
            addCriterion("circle <", value, "circle");
            return (Criteria) this;
        }

        public Criteria andCircleLessThanOrEqualTo(String value) {
            addCriterion("circle <=", value, "circle");
            return (Criteria) this;
        }

        public Criteria andCircleLike(String value) {
            addCriterion("circle like", value, "circle");
            return (Criteria) this;
        }

        public Criteria andCircleNotLike(String value) {
            addCriterion("circle not like", value, "circle");
            return (Criteria) this;
        }

        public Criteria andCircleIn(List<String> values) {
            addCriterion("circle in", values, "circle");
            return (Criteria) this;
        }

        public Criteria andCircleNotIn(List<String> values) {
            addCriterion("circle not in", values, "circle");
            return (Criteria) this;
        }

        public Criteria andCircleBetween(String value1, String value2) {
            addCriterion("circle between", value1, value2, "circle");
            return (Criteria) this;
        }

        public Criteria andCircleNotBetween(String value1, String value2) {
            addCriterion("circle not between", value1, value2, "circle");
            return (Criteria) this;
        }

        public Criteria andCircleNameIsNull() {
            addCriterion("circle_name is null");
            return (Criteria) this;
        }

        public Criteria andCircleNameIsNotNull() {
            addCriterion("circle_name is not null");
            return (Criteria) this;
        }

        public Criteria andCircleNameEqualTo(String value) {
            addCriterion("circle_name =", value, "circleName");
            return (Criteria) this;
        }

        public Criteria andCircleNameNotEqualTo(String value) {
            addCriterion("circle_name <>", value, "circleName");
            return (Criteria) this;
        }

        public Criteria andCircleNameGreaterThan(String value) {
            addCriterion("circle_name >", value, "circleName");
            return (Criteria) this;
        }

        public Criteria andCircleNameGreaterThanOrEqualTo(String value) {
            addCriterion("circle_name >=", value, "circleName");
            return (Criteria) this;
        }

        public Criteria andCircleNameLessThan(String value) {
            addCriterion("circle_name <", value, "circleName");
            return (Criteria) this;
        }

        public Criteria andCircleNameLessThanOrEqualTo(String value) {
            addCriterion("circle_name <=", value, "circleName");
            return (Criteria) this;
        }

        public Criteria andCircleNameLike(String value) {
            addCriterion("circle_name like", value, "circleName");
            return (Criteria) this;
        }

        public Criteria andCircleNameNotLike(String value) {
            addCriterion("circle_name not like", value, "circleName");
            return (Criteria) this;
        }

        public Criteria andCircleNameIn(List<String> values) {
            addCriterion("circle_name in", values, "circleName");
            return (Criteria) this;
        }

        public Criteria andCircleNameNotIn(List<String> values) {
            addCriterion("circle_name not in", values, "circleName");
            return (Criteria) this;
        }

        public Criteria andCircleNameBetween(String value1, String value2) {
            addCriterion("circle_name between", value1, value2, "circleName");
            return (Criteria) this;
        }

        public Criteria andCircleNameNotBetween(String value1, String value2) {
            addCriterion("circle_name not between", value1, value2, "circleName");
            return (Criteria) this;
        }

        public Criteria andClusterIdIsNull() {
            addCriterion("cluster_id is null");
            return (Criteria) this;
        }

        public Criteria andClusterIdIsNotNull() {
            addCriterion("cluster_id is not null");
            return (Criteria) this;
        }

        public Criteria andClusterIdEqualTo(Integer value) {
            addCriterion("cluster_id =", value, "clusterId");
            return (Criteria) this;
        }

        public Criteria andClusterIdNotEqualTo(Integer value) {
            addCriterion("cluster_id <>", value, "clusterId");
            return (Criteria) this;
        }

        public Criteria andClusterIdGreaterThan(Integer value) {
            addCriterion("cluster_id >", value, "clusterId");
            return (Criteria) this;
        }

        public Criteria andClusterIdGreaterThanOrEqualTo(Integer value) {
            addCriterion("cluster_id >=", value, "clusterId");
            return (Criteria) this;
        }

        public Criteria andClusterIdLessThan(Integer value) {
            addCriterion("cluster_id <", value, "clusterId");
            return (Criteria) this;
        }

        public Criteria andClusterIdLessThanOrEqualTo(Integer value) {
            addCriterion("cluster_id <=", value, "clusterId");
            return (Criteria) this;
        }

        public Criteria andClusterIdIn(List<Integer> values) {
            addCriterion("cluster_id in", values, "clusterId");
            return (Criteria) this;
        }

        public Criteria andClusterIdNotIn(List<Integer> values) {
            addCriterion("cluster_id not in", values, "clusterId");
            return (Criteria) this;
        }

        public Criteria andClusterIdBetween(Integer value1, Integer value2) {
            addCriterion("cluster_id between", value1, value2, "clusterId");
            return (Criteria) this;
        }

        public Criteria andClusterIdNotBetween(Integer value1, Integer value2) {
            addCriterion("cluster_id not between", value1, value2, "clusterId");
            return (Criteria) this;
        }

        public Criteria andIpaddressIsNull() {
            addCriterion("ipaddress is null");
            return (Criteria) this;
        }

        public Criteria andIpaddressIsNotNull() {
            addCriterion("ipaddress is not null");
            return (Criteria) this;
        }

        public Criteria andIpaddressEqualTo(String value) {
            addCriterion("ipaddress =", value, "ipaddress");
            return (Criteria) this;
        }

        public Criteria andIpaddressNotEqualTo(String value) {
            addCriterion("ipaddress <>", value, "ipaddress");
            return (Criteria) this;
        }

        public Criteria andIpaddressGreaterThan(String value) {
            addCriterion("ipaddress >", value, "ipaddress");
            return (Criteria) this;
        }

        public Criteria andIpaddressGreaterThanOrEqualTo(String value) {
            addCriterion("ipaddress >=", value, "ipaddress");
            return (Criteria) this;
        }

        public Criteria andIpaddressLessThan(String value) {
            addCriterion("ipaddress <", value, "ipaddress");
            return (Criteria) this;
        }

        public Criteria andIpaddressLessThanOrEqualTo(String value) {
            addCriterion("ipaddress <=", value, "ipaddress");
            return (Criteria) this;
        }

        public Criteria andIpaddressLike(String value) {
            addCriterion("ipaddress like", value, "ipaddress");
            return (Criteria) this;
        }

        public Criteria andIpaddressNotLike(String value) {
            addCriterion("ipaddress not like", value, "ipaddress");
            return (Criteria) this;
        }

        public Criteria andIpaddressIn(List<String> values) {
            addCriterion("ipaddress in", values, "ipaddress");
            return (Criteria) this;
        }

        public Criteria andIpaddressNotIn(List<String> values) {
            addCriterion("ipaddress not in", values, "ipaddress");
            return (Criteria) this;
        }

        public Criteria andIpaddressBetween(String value1, String value2) {
            addCriterion("ipaddress between", value1, value2, "ipaddress");
            return (Criteria) this;
        }

        public Criteria andIpaddressNotBetween(String value1, String value2) {
            addCriterion("ipaddress not between", value1, value2, "ipaddress");
            return (Criteria) this;
        }

        public Criteria andEarfcnIsNull() {
            addCriterion("earfcn is null");
            return (Criteria) this;
        }

        public Criteria andEarfcnIsNotNull() {
            addCriterion("earfcn is not null");
            return (Criteria) this;
        }

        public Criteria andEarfcnEqualTo(String value) {
            addCriterion("earfcn =", value, "earfcn");
            return (Criteria) this;
        }

        public Criteria andEarfcnNotEqualTo(String value) {
            addCriterion("earfcn <>", value, "earfcn");
            return (Criteria) this;
        }

        public Criteria andEarfcnGreaterThan(String value) {
            addCriterion("earfcn >", value, "earfcn");
            return (Criteria) this;
        }

        public Criteria andEarfcnGreaterThanOrEqualTo(String value) {
            addCriterion("earfcn >=", value, "earfcn");
            return (Criteria) this;
        }

        public Criteria andEarfcnLessThan(String value) {
            addCriterion("earfcn <", value, "earfcn");
            return (Criteria) this;
        }

        public Criteria andEarfcnLessThanOrEqualTo(String value) {
            addCriterion("earfcn <=", value, "earfcn");
            return (Criteria) this;
        }

        public Criteria andEarfcnLike(String value) {
            addCriterion("earfcn like", value, "earfcn");
            return (Criteria) this;
        }

        public Criteria andEarfcnNotLike(String value) {
            addCriterion("earfcn not like", value, "earfcn");
            return (Criteria) this;
        }

        public Criteria andEarfcnIn(List<String> values) {
            addCriterion("earfcn in", values, "earfcn");
            return (Criteria) this;
        }

        public Criteria andEarfcnNotIn(List<String> values) {
            addCriterion("earfcn not in", values, "earfcn");
            return (Criteria) this;
        }

        public Criteria andEarfcnBetween(String value1, String value2) {
            addCriterion("earfcn between", value1, value2, "earfcn");
            return (Criteria) this;
        }

        public Criteria andEarfcnNotBetween(String value1, String value2) {
            addCriterion("earfcn not between", value1, value2, "earfcn");
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

        public Criteria andMbmsServiceAreaIdIsNull() {
            addCriterion("mbms_service_area_id is null");
            return (Criteria) this;
        }

        public Criteria andMbmsServiceAreaIdIsNotNull() {
            addCriterion("mbms_service_area_id is not null");
            return (Criteria) this;
        }

        public Criteria andMbmsServiceAreaIdEqualTo(Integer value) {
            addCriterion("mbms_service_area_id =", value, "mbmsServiceAreaId");
            return (Criteria) this;
        }

        public Criteria andMbmsServiceAreaIdNotEqualTo(Integer value) {
            addCriterion("mbms_service_area_id <>", value, "mbmsServiceAreaId");
            return (Criteria) this;
        }

        public Criteria andMbmsServiceAreaIdGreaterThan(Integer value) {
            addCriterion("mbms_service_area_id >", value, "mbmsServiceAreaId");
            return (Criteria) this;
        }

        public Criteria andMbmsServiceAreaIdGreaterThanOrEqualTo(Integer value) {
            addCriterion("mbms_service_area_id >=", value, "mbmsServiceAreaId");
            return (Criteria) this;
        }

        public Criteria andMbmsServiceAreaIdLessThan(Integer value) {
            addCriterion("mbms_service_area_id <", value, "mbmsServiceAreaId");
            return (Criteria) this;
        }

        public Criteria andMbmsServiceAreaIdLessThanOrEqualTo(Integer value) {
            addCriterion("mbms_service_area_id <=", value, "mbmsServiceAreaId");
            return (Criteria) this;
        }

        public Criteria andMbmsServiceAreaIdIn(List<Integer> values) {
            addCriterion("mbms_service_area_id in", values, "mbmsServiceAreaId");
            return (Criteria) this;
        }

        public Criteria andMbmsServiceAreaIdNotIn(List<Integer> values) {
            addCriterion("mbms_service_area_id not in", values, "mbmsServiceAreaId");
            return (Criteria) this;
        }

        public Criteria andMbmsServiceAreaIdBetween(Integer value1, Integer value2) {
            addCriterion("mbms_service_area_id between", value1, value2, "mbmsServiceAreaId");
            return (Criteria) this;
        }

        public Criteria andMbmsServiceAreaIdNotBetween(Integer value1, Integer value2) {
            addCriterion("mbms_service_area_id not between", value1, value2, "mbmsServiceAreaId");
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