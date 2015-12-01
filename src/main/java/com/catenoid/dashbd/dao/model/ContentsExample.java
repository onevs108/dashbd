package com.catenoid.dashbd.dao.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ContentsExample {
    protected String orderByClause;

    protected String limitByClause;
    
    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public ContentsExample() {
        oredCriteria = new ArrayList<Criteria>();
    }

    public void setOrderByClause(String orderByClause) {
        this.orderByClause = orderByClause;
    }

    public String getOrderByClause() {
        return orderByClause;
    }
    
    public void setLimitByClause(String limitByClause) {
        this.limitByClause = limitByClause;
    }

    public String getLimitByClause() {
        return limitByClause;
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
    
    public Criteria and() {
    	Criteria criteria = createCriteriaInternal();
        oredCriteria.add(criteria);
        return criteria;
    }
    
    public void and(Criteria criteria) {
    	oredCriteria.add(criteria);
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

        public Criteria andTitleIsNull() {
            addCriterion("title is null");
            return (Criteria) this;
        }

        public Criteria andTitleIsNotNull() {
            addCriterion("title is not null");
            return (Criteria) this;
        }

        public Criteria andTitleEqualTo(String value) {
            addCriterion("title =", value, "title");
            return (Criteria) this;
        }

        public Criteria andTitleNotEqualTo(String value) {
            addCriterion("title <>", value, "title");
            return (Criteria) this;
        }

        public Criteria andTitleGreaterThan(String value) {
            addCriterion("title >", value, "title");
            return (Criteria) this;
        }

        public Criteria andTitleGreaterThanOrEqualTo(String value) {
            addCriterion("title >=", value, "title");
            return (Criteria) this;
        }

        public Criteria andTitleLessThan(String value) {
            addCriterion("title <", value, "title");
            return (Criteria) this;
        }

        public Criteria andTitleLessThanOrEqualTo(String value) {
            addCriterion("title <=", value, "title");
            return (Criteria) this;
        }

        public Criteria andTitleLike(String value) {
            addCriterion("title like", value, "title");
            return (Criteria) this;
        }

        public Criteria andTitleNotLike(String value) {
            addCriterion("title not like", value, "title");
            return (Criteria) this;
        }

        public Criteria andTitleIn(List<String> values) {
            addCriterion("title in", values, "title");
            return (Criteria) this;
        }

        public Criteria andTitleNotIn(List<String> values) {
            addCriterion("title not in", values, "title");
            return (Criteria) this;
        }

        public Criteria andTitleBetween(String value1, String value2) {
            addCriterion("title between", value1, value2, "title");
            return (Criteria) this;
        }

        public Criteria andTitleNotBetween(String value1, String value2) {
            addCriterion("title not between", value1, value2, "title");
            return (Criteria) this;
        }
        
        public Criteria andCategoryIsNull() {
            addCriterion("category is null");
            return (Criteria) this;
        }

        public Criteria andCategoryIsNotNull() {
            addCriterion("category is not null");
            return (Criteria) this;
        }

        public Criteria andCategoryEqualTo(String value) {
            addCriterion("category =", value, "category");
            return (Criteria) this;
        }

        public Criteria andCategoryNotEqualTo(String value) {
            addCriterion("category <>", value, "category");
            return (Criteria) this;
        }

        public Criteria andCategoryGreaterThan(String value) {
            addCriterion("category >", value, "category");
            return (Criteria) this;
        }

        public Criteria andCategoryGreaterThanOrEqualTo(String value) {
            addCriterion("category >=", value, "category");
            return (Criteria) this;
        }

        public Criteria andCategoryLessThan(String value) {
            addCriterion("category <", value, "category");
            return (Criteria) this;
        }

        public Criteria andCategoryLessThanOrEqualTo(String value) {
            addCriterion("category <=", value, "category");
            return (Criteria) this;
        }

        public Criteria andCategoryLike(String value) {
            addCriterion("category like", value, "category");
            return (Criteria) this;
        }

        public Criteria andCategoryNotLike(String value) {
            addCriterion("category not like", value, "category");
            return (Criteria) this;
        }

        public Criteria andCategoryIn(List<String> values) {
            addCriterion("category in", values, "category");
            return (Criteria) this;
        }

        public Criteria andCategoryNotIn(List<String> values) {
            addCriterion("category not in", values, "category");
            return (Criteria) this;
        }

        public Criteria andCategoryBetween(String value1, String value2) {
            addCriterion("category between", value1, value2, "category");
            return (Criteria) this;
        }

        public Criteria andCategoryNotBetween(String value1, String value2) {
            addCriterion("category not between", value1, value2, "category");
            return (Criteria) this;
        }        

        public Criteria andDirectorIsNull() {
            addCriterion("director is null");
            return (Criteria) this;
        }

        public Criteria andDirectorIsNotNull() {
            addCriterion("director is not null");
            return (Criteria) this;
        }

        public Criteria andDirectorEqualTo(String value) {
            addCriterion("director =", value, "director");
            return (Criteria) this;
        }

        public Criteria andDirectorNotEqualTo(String value) {
            addCriterion("director <>", value, "director");
            return (Criteria) this;
        }

        public Criteria andDirectorGreaterThan(String value) {
            addCriterion("director >", value, "director");
            return (Criteria) this;
        }

        public Criteria andDirectorGreaterThanOrEqualTo(String value) {
            addCriterion("director >=", value, "director");
            return (Criteria) this;
        }

        public Criteria andDirectorLessThan(String value) {
            addCriterion("director <", value, "director");
            return (Criteria) this;
        }

        public Criteria andDirectorLessThanOrEqualTo(String value) {
            addCriterion("director <=", value, "director");
            return (Criteria) this;
        }

        public Criteria andDirectorLike(String value) {
            addCriterion("director like", value, "director");
            return (Criteria) this;
        }

        public Criteria andDirectorNotLike(String value) {
            addCriterion("director not like", value, "director");
            return (Criteria) this;
        }

        public Criteria andDirectorIn(List<String> values) {
            addCriterion("director in", values, "director");
            return (Criteria) this;
        }

        public Criteria andDirectorNotIn(List<String> values) {
            addCriterion("director not in", values, "director");
            return (Criteria) this;
        }

        public Criteria andDirectorBetween(String value1, String value2) {
            addCriterion("director between", value1, value2, "director");
            return (Criteria) this;
        }

        public Criteria andDirectorNotBetween(String value1, String value2) {
            addCriterion("director not between", value1, value2, "director");
            return (Criteria) this;
        }

        public Criteria andActorsIsNull() {
            addCriterion("actors is null");
            return (Criteria) this;
        }

        public Criteria andActorsIsNotNull() {
            addCriterion("actors is not null");
            return (Criteria) this;
        }

        public Criteria andActorsEqualTo(String value) {
            addCriterion("actors =", value, "actors");
            return (Criteria) this;
        }

        public Criteria andActorsNotEqualTo(String value) {
            addCriterion("actors <>", value, "actors");
            return (Criteria) this;
        }

        public Criteria andActorsGreaterThan(String value) {
            addCriterion("actors >", value, "actors");
            return (Criteria) this;
        }

        public Criteria andActorsGreaterThanOrEqualTo(String value) {
            addCriterion("actors >=", value, "actors");
            return (Criteria) this;
        }

        public Criteria andActorsLessThan(String value) {
            addCriterion("actors <", value, "actors");
            return (Criteria) this;
        }

        public Criteria andActorsLessThanOrEqualTo(String value) {
            addCriterion("actors <=", value, "actors");
            return (Criteria) this;
        }

        public Criteria andActorsLike(String value) {
            addCriterion("actors like", value, "actors");
            return (Criteria) this;
        }

        public Criteria andActorsNotLike(String value) {
            addCriterion("actors not like", value, "actors");
            return (Criteria) this;
        }

        public Criteria andActorsIn(List<String> values) {
            addCriterion("actors in", values, "actors");
            return (Criteria) this;
        }

        public Criteria andActorsNotIn(List<String> values) {
            addCriterion("actors not in", values, "actors");
            return (Criteria) this;
        }

        public Criteria andActorsBetween(String value1, String value2) {
            addCriterion("actors between", value1, value2, "actors");
            return (Criteria) this;
        }

        public Criteria andActorsNotBetween(String value1, String value2) {
            addCriterion("actors not between", value1, value2, "actors");
            return (Criteria) this;
        }
        
        public Criteria andContentProviderIsNull() {
            addCriterion("content_provider is null");
            return (Criteria) this;
        }

        public Criteria andContentProviderIsNotNull() {
            addCriterion("content_provider is not null");
            return (Criteria) this;
        }

        public Criteria andContentProviderEqualTo(String value) {
            addCriterion("content_provider =", value, "content_provider");
            return (Criteria) this;
        }

        public Criteria andContentProviderNotEqualTo(String value) {
            addCriterion("content_provider <>", value, "content_provider");
            return (Criteria) this;
        }

        public Criteria andContentProviderGreaterThan(String value) {
            addCriterion("content_provider >", value, "content_provider");
            return (Criteria) this;
        }

        public Criteria andContentProviderGreaterThanOrEqualTo(String value) {
            addCriterion("content_provider >=", value, "content_provider");
            return (Criteria) this;
        }

        public Criteria andContentProviderLessThan(String value) {
            addCriterion("content_provider <", value, "content_provider");
            return (Criteria) this;
        }

        public Criteria andContentProviderLessThanOrEqualTo(String value) {
            addCriterion("content_provider <=", value, "content_provider");
            return (Criteria) this;
        }

        public Criteria andContentProviderLike(String value) {
            addCriterion("content_provider like", value, "content_provider");
            return (Criteria) this;
        }

        public Criteria andContentProviderNotLike(String value) {
            addCriterion("content_provider not like", value, "content_provider");
            return (Criteria) this;
        }

        public Criteria andContentProviderIn(List<String> values) {
            addCriterion("content_provider in", values, "content_provider");
            return (Criteria) this;
        }

        public Criteria andContentProviderNotIn(List<String> values) {
            addCriterion("content_provider not in", values, "content_provider");
            return (Criteria) this;
        }

        public Criteria andContentProviderBetween(String value1, String value2) {
            addCriterion("content_provider between", value1, value2, "content_provider");
            return (Criteria) this;
        }

        public Criteria andContentProviderNotBetween(String value1, String value2) {
            addCriterion("content_provider not between", value1, value2, "content_provider");
            return (Criteria) this;
        }
        
        public Criteria andFileFormatIsNull() {
            addCriterion("file_format is null");
            return (Criteria) this;
        }

        public Criteria andFileFormatIsNotNull() {
            addCriterion("file_format is not null");
            return (Criteria) this;
        }

        public Criteria andFileFormatEqualTo(String value) {
            addCriterion("file_format =", value, "file_format");
            return (Criteria) this;
        }

        public Criteria andFileFormatNotEqualTo(String value) {
            addCriterion("file_format <>", value, "file_format");
            return (Criteria) this;
        }

        public Criteria andFileFormatGreaterThan(String value) {
            addCriterion("file_format >", value, "file_format");
            return (Criteria) this;
        }

        public Criteria andFileFormatGreaterThanOrEqualTo(String value) {
            addCriterion("file_format >=", value, "file_format");
            return (Criteria) this;
        }

        public Criteria andFileFormatLessThan(String value) {
            addCriterion("file_format <", value, "file_format");
            return (Criteria) this;
        }

        public Criteria andFileFormatLessThanOrEqualTo(String value) {
            addCriterion("file_format <=", value, "file_format");
            return (Criteria) this;
        }

        public Criteria andFileFormatLike(String value) {
            addCriterion("file_format like", value, "file_format");
            return (Criteria) this;
        }

        public Criteria andFileFormatNotLike(String value) {
            addCriterion("file_format not like", value, "file_format");
            return (Criteria) this;
        }

        public Criteria andFileFormatIn(List<String> values) {
            addCriterion("file_format in", values, "file_format");
            return (Criteria) this;
        }

        public Criteria andFileFormatNotIn(List<String> values) {
            addCriterion("file_format not in", values, "file_format");
            return (Criteria) this;
        }

        public Criteria andFileFormatBetween(String value1, String value2) {
            addCriterion("file_format between", value1, value2, "file_format");
            return (Criteria) this;
        }

        public Criteria andFileFormatNotBetween(String value1, String value2) {
            addCriterion("file_format not between", value1, value2, "file_format");
            return (Criteria) this;
        }

        public Criteria andAgeRestrictionIsNull() {
            addCriterion("age_restriction is null");
            return (Criteria) this;
        }

        public Criteria andAgeRestrictionIsNotNull() {
            addCriterion("age_restriction is not null");
            return (Criteria) this;
        }

        public Criteria andAgeRestrictionEqualTo(Integer value) {
            addCriterion("age_restriction =", value, "ageRestriction");
            return (Criteria) this;
        }

        public Criteria andAgeRestrictionNotEqualTo(Integer value) {
            addCriterion("age_restriction <>", value, "ageRestriction");
            return (Criteria) this;
        }

        public Criteria andAgeRestrictionGreaterThan(Integer value) {
            addCriterion("age_restriction >", value, "ageRestriction");
            return (Criteria) this;
        }

        public Criteria andAgeRestrictionGreaterThanOrEqualTo(Integer value) {
            addCriterion("age_restriction >=", value, "ageRestriction");
            return (Criteria) this;
        }

        public Criteria andAgeRestrictionLessThan(Integer value) {
            addCriterion("age_restriction <", value, "ageRestriction");
            return (Criteria) this;
        }

        public Criteria andAgeRestrictionLessThanOrEqualTo(Integer value) {
            addCriterion("age_restriction <=", value, "ageRestriction");
            return (Criteria) this;
        }

        public Criteria andAgeRestrictionIn(List<Integer> values) {
            addCriterion("age_restriction in", values, "ageRestriction");
            return (Criteria) this;
        }

        public Criteria andAgeRestrictionNotIn(List<Integer> values) {
            addCriterion("age_restriction not in", values, "ageRestriction");
            return (Criteria) this;
        }

        public Criteria andAgeRestrictionBetween(Integer value1, Integer value2) {
            addCriterion("age_restriction between", value1, value2, "ageRestriction");
            return (Criteria) this;
        }

        public Criteria andAgeRestrictionNotBetween(Integer value1, Integer value2) {
            addCriterion("age_restriction not between", value1, value2, "ageRestriction");
            return (Criteria) this;
        }

        public Criteria andUrlIsNull() {
            addCriterion("url is null");
            return (Criteria) this;
        }

        public Criteria andUrlIsNotNull() {
            addCriterion("url is not null");
            return (Criteria) this;
        }

        public Criteria andUrlEqualTo(String value) {
            addCriterion("url =", value, "url");
            return (Criteria) this;
        }

        public Criteria andUrlNotEqualTo(String value) {
            addCriterion("url <>", value, "url");
            return (Criteria) this;
        }

        public Criteria andUrlGreaterThan(String value) {
            addCriterion("url >", value, "url");
            return (Criteria) this;
        }

        public Criteria andUrlGreaterThanOrEqualTo(String value) {
            addCriterion("url >=", value, "url");
            return (Criteria) this;
        }

        public Criteria andUrlLessThan(String value) {
            addCriterion("url <", value, "url");
            return (Criteria) this;
        }

        public Criteria andUrlLessThanOrEqualTo(String value) {
            addCriterion("url <=", value, "url");
            return (Criteria) this;
        }

        public Criteria andUrlLike(String value) {
            addCriterion("url like", value, "url");
            return (Criteria) this;
        }

        public Criteria andUrlNotLike(String value) {
            addCriterion("url not like", value, "url");
            return (Criteria) this;
        }

        public Criteria andUrlIn(List<String> values) {
            addCriterion("url in", values, "url");
            return (Criteria) this;
        }

        public Criteria andUrlNotIn(List<String> values) {
            addCriterion("url not in", values, "url");
            return (Criteria) this;
        }

        public Criteria andUrlBetween(String value1, String value2) {
            addCriterion("url between", value1, value2, "url");
            return (Criteria) this;
        }

        public Criteria andUrlNotBetween(String value1, String value2) {
            addCriterion("url not between", value1, value2, "url");
            return (Criteria) this;
        }
        
        public Criteria andDurationIsNull() {
            addCriterion("duration is null");
            return (Criteria) this;
        }

        public Criteria andDurationIsNotNull() {
            addCriterion("duration is not null");
            return (Criteria) this;
        }

        public Criteria andDurationEqualTo(Integer value) {
            addCriterion("duration =", value, "duration");
            return (Criteria) this;
        }

        public Criteria andDurationNotEqualTo(Integer value) {
            addCriterion("duration <>", value, "duration");
            return (Criteria) this;
        }

        public Criteria andDurationGreaterThan(Integer value) {
            addCriterion("duration >", value, "duration");
            return (Criteria) this;
        }

        public Criteria andDurationGreaterThanOrEqualTo(Integer value) {
            addCriterion("duration >=", value, "duration");
            return (Criteria) this;
        }

        public Criteria andDurationLessThan(Integer value) {
            addCriterion("duration <", value, "duration");
            return (Criteria) this;
        }

        public Criteria andDurationLessThanOrEqualTo(Integer value) {
            addCriterion("duration <=", value, "duration");
            return (Criteria) this;
        }

        public Criteria andDurationIn(List<Integer> values) {
            addCriterion("duration in", values, "duration");
            return (Criteria) this;
        }

        public Criteria andDurationNotIn(List<Integer> values) {
            addCriterion("duration not in", values, "duration");
            return (Criteria) this;
        }

        public Criteria andDurationBetween(Integer value1, Integer value2) {
            addCriterion("duration between", value1, value2, "duration");
            return (Criteria) this;
        }

        public Criteria andDurationNotBetween(Integer value1, Integer value2) {
            addCriterion("duration not between", value1, value2, "duration");
            return (Criteria) this;
        }
        
        public Criteria andBitrateIsNull() {
            addCriterion("bitrate is null");
            return (Criteria) this;
        }

        public Criteria andBitrateIsNotNull() {
            addCriterion("bitrate is not null");
            return (Criteria) this;
        }

        public Criteria andBitrateEqualTo(Integer value) {
            addCriterion("bitrate =", value, "bitrate");
            return (Criteria) this;
        }

        public Criteria andBitrateNotEqualTo(Integer value) {
            addCriterion("bitrate <>", value, "bitrate");
            return (Criteria) this;
        }

        public Criteria andBitrateGreaterThan(Integer value) {
            addCriterion("bitrate >", value, "bitrate");
            return (Criteria) this;
        }

        public Criteria andBitrateGreaterThanOrEqualTo(Integer value) {
            addCriterion("bitrate >=", value, "bitrate");
            return (Criteria) this;
        }

        public Criteria andBitrateLessThan(Integer value) {
            addCriterion("bitrate <", value, "bitrate");
            return (Criteria) this;
        }

        public Criteria andBitrateLessThanOrEqualTo(Integer value) {
            addCriterion("bitrate <=", value, "bitrate");
            return (Criteria) this;
        }

        public Criteria andBitrateIn(List<Integer> values) {
            addCriterion("bitrate in", values, "bitrate");
            return (Criteria) this;
        }

        public Criteria andBitrateNotIn(List<Integer> values) {
            addCriterion("bitrate not in", values, "bitrate");
            return (Criteria) this;
        }

        public Criteria andBitrateBetween(Integer value1, Integer value2) {
            addCriterion("bitrate between", value1, value2, "bitrate");
            return (Criteria) this;
        }

        public Criteria andBitrateNotBetween(Integer value1, Integer value2) {
            addCriterion("bitrate not between", value1, value2, "bitrate");
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