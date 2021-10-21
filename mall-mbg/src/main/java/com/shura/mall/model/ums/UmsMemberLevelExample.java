package com.shura.mall.model.ums;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class UmsMemberLevelExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public UmsMemberLevelExample() {
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

        public Criteria andIdIsNull() {
            addCriterion("id is null");
            return (Criteria) this;
        }

        public Criteria andIdIsNotNull() {
            addCriterion("id is not null");
            return (Criteria) this;
        }

        public Criteria andIdEqualTo(Long value) {
            addCriterion("id =", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotEqualTo(Long value) {
            addCriterion("id <>", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThan(Long value) {
            addCriterion("id >", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThanOrEqualTo(Long value) {
            addCriterion("id >=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThan(Long value) {
            addCriterion("id <", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThanOrEqualTo(Long value) {
            addCriterion("id <=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdIn(List<Long> values) {
            addCriterion("id in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotIn(List<Long> values) {
            addCriterion("id not in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdBetween(Long value1, Long value2) {
            addCriterion("id between", value1, value2, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotBetween(Long value1, Long value2) {
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

        public Criteria andGrowthPointIsNull() {
            addCriterion("growth_point is null");
            return (Criteria) this;
        }

        public Criteria andGrowthPointIsNotNull() {
            addCriterion("growth_point is not null");
            return (Criteria) this;
        }

        public Criteria andGrowthPointEqualTo(Integer value) {
            addCriterion("growth_point =", value, "growthPoint");
            return (Criteria) this;
        }

        public Criteria andGrowthPointNotEqualTo(Integer value) {
            addCriterion("growth_point <>", value, "growthPoint");
            return (Criteria) this;
        }

        public Criteria andGrowthPointGreaterThan(Integer value) {
            addCriterion("growth_point >", value, "growthPoint");
            return (Criteria) this;
        }

        public Criteria andGrowthPointGreaterThanOrEqualTo(Integer value) {
            addCriterion("growth_point >=", value, "growthPoint");
            return (Criteria) this;
        }

        public Criteria andGrowthPointLessThan(Integer value) {
            addCriterion("growth_point <", value, "growthPoint");
            return (Criteria) this;
        }

        public Criteria andGrowthPointLessThanOrEqualTo(Integer value) {
            addCriterion("growth_point <=", value, "growthPoint");
            return (Criteria) this;
        }

        public Criteria andGrowthPointIn(List<Integer> values) {
            addCriterion("growth_point in", values, "growthPoint");
            return (Criteria) this;
        }

        public Criteria andGrowthPointNotIn(List<Integer> values) {
            addCriterion("growth_point not in", values, "growthPoint");
            return (Criteria) this;
        }

        public Criteria andGrowthPointBetween(Integer value1, Integer value2) {
            addCriterion("growth_point between", value1, value2, "growthPoint");
            return (Criteria) this;
        }

        public Criteria andGrowthPointNotBetween(Integer value1, Integer value2) {
            addCriterion("growth_point not between", value1, value2, "growthPoint");
            return (Criteria) this;
        }

        public Criteria andDefaultStatusIsNull() {
            addCriterion("default_status is null");
            return (Criteria) this;
        }

        public Criteria andDefaultStatusIsNotNull() {
            addCriterion("default_status is not null");
            return (Criteria) this;
        }

        public Criteria andDefaultStatusEqualTo(Integer value) {
            addCriterion("default_status =", value, "defaultStatus");
            return (Criteria) this;
        }

        public Criteria andDefaultStatusNotEqualTo(Integer value) {
            addCriterion("default_status <>", value, "defaultStatus");
            return (Criteria) this;
        }

        public Criteria andDefaultStatusGreaterThan(Integer value) {
            addCriterion("default_status >", value, "defaultStatus");
            return (Criteria) this;
        }

        public Criteria andDefaultStatusGreaterThanOrEqualTo(Integer value) {
            addCriterion("default_status >=", value, "defaultStatus");
            return (Criteria) this;
        }

        public Criteria andDefaultStatusLessThan(Integer value) {
            addCriterion("default_status <", value, "defaultStatus");
            return (Criteria) this;
        }

        public Criteria andDefaultStatusLessThanOrEqualTo(Integer value) {
            addCriterion("default_status <=", value, "defaultStatus");
            return (Criteria) this;
        }

        public Criteria andDefaultStatusIn(List<Integer> values) {
            addCriterion("default_status in", values, "defaultStatus");
            return (Criteria) this;
        }

        public Criteria andDefaultStatusNotIn(List<Integer> values) {
            addCriterion("default_status not in", values, "defaultStatus");
            return (Criteria) this;
        }

        public Criteria andDefaultStatusBetween(Integer value1, Integer value2) {
            addCriterion("default_status between", value1, value2, "defaultStatus");
            return (Criteria) this;
        }

        public Criteria andDefaultStatusNotBetween(Integer value1, Integer value2) {
            addCriterion("default_status not between", value1, value2, "defaultStatus");
            return (Criteria) this;
        }

        public Criteria andFreeFreightPointIsNull() {
            addCriterion("free_freight_point is null");
            return (Criteria) this;
        }

        public Criteria andFreeFreightPointIsNotNull() {
            addCriterion("free_freight_point is not null");
            return (Criteria) this;
        }

        public Criteria andFreeFreightPointEqualTo(BigDecimal value) {
            addCriterion("free_freight_point =", value, "freeFreightPoint");
            return (Criteria) this;
        }

        public Criteria andFreeFreightPointNotEqualTo(BigDecimal value) {
            addCriterion("free_freight_point <>", value, "freeFreightPoint");
            return (Criteria) this;
        }

        public Criteria andFreeFreightPointGreaterThan(BigDecimal value) {
            addCriterion("free_freight_point >", value, "freeFreightPoint");
            return (Criteria) this;
        }

        public Criteria andFreeFreightPointGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("free_freight_point >=", value, "freeFreightPoint");
            return (Criteria) this;
        }

        public Criteria andFreeFreightPointLessThan(BigDecimal value) {
            addCriterion("free_freight_point <", value, "freeFreightPoint");
            return (Criteria) this;
        }

        public Criteria andFreeFreightPointLessThanOrEqualTo(BigDecimal value) {
            addCriterion("free_freight_point <=", value, "freeFreightPoint");
            return (Criteria) this;
        }

        public Criteria andFreeFreightPointIn(List<BigDecimal> values) {
            addCriterion("free_freight_point in", values, "freeFreightPoint");
            return (Criteria) this;
        }

        public Criteria andFreeFreightPointNotIn(List<BigDecimal> values) {
            addCriterion("free_freight_point not in", values, "freeFreightPoint");
            return (Criteria) this;
        }

        public Criteria andFreeFreightPointBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("free_freight_point between", value1, value2, "freeFreightPoint");
            return (Criteria) this;
        }

        public Criteria andFreeFreightPointNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("free_freight_point not between", value1, value2, "freeFreightPoint");
            return (Criteria) this;
        }

        public Criteria andCommentGrowthPointIsNull() {
            addCriterion("comment_growth_point is null");
            return (Criteria) this;
        }

        public Criteria andCommentGrowthPointIsNotNull() {
            addCriterion("comment_growth_point is not null");
            return (Criteria) this;
        }

        public Criteria andCommentGrowthPointEqualTo(Integer value) {
            addCriterion("comment_growth_point =", value, "commentGrowthPoint");
            return (Criteria) this;
        }

        public Criteria andCommentGrowthPointNotEqualTo(Integer value) {
            addCriterion("comment_growth_point <>", value, "commentGrowthPoint");
            return (Criteria) this;
        }

        public Criteria andCommentGrowthPointGreaterThan(Integer value) {
            addCriterion("comment_growth_point >", value, "commentGrowthPoint");
            return (Criteria) this;
        }

        public Criteria andCommentGrowthPointGreaterThanOrEqualTo(Integer value) {
            addCriterion("comment_growth_point >=", value, "commentGrowthPoint");
            return (Criteria) this;
        }

        public Criteria andCommentGrowthPointLessThan(Integer value) {
            addCriterion("comment_growth_point <", value, "commentGrowthPoint");
            return (Criteria) this;
        }

        public Criteria andCommentGrowthPointLessThanOrEqualTo(Integer value) {
            addCriterion("comment_growth_point <=", value, "commentGrowthPoint");
            return (Criteria) this;
        }

        public Criteria andCommentGrowthPointIn(List<Integer> values) {
            addCriterion("comment_growth_point in", values, "commentGrowthPoint");
            return (Criteria) this;
        }

        public Criteria andCommentGrowthPointNotIn(List<Integer> values) {
            addCriterion("comment_growth_point not in", values, "commentGrowthPoint");
            return (Criteria) this;
        }

        public Criteria andCommentGrowthPointBetween(Integer value1, Integer value2) {
            addCriterion("comment_growth_point between", value1, value2, "commentGrowthPoint");
            return (Criteria) this;
        }

        public Criteria andCommentGrowthPointNotBetween(Integer value1, Integer value2) {
            addCriterion("comment_growth_point not between", value1, value2, "commentGrowthPoint");
            return (Criteria) this;
        }

        public Criteria andPrivilegeFreeFreightIsNull() {
            addCriterion("privilege_free_freight is null");
            return (Criteria) this;
        }

        public Criteria andPrivilegeFreeFreightIsNotNull() {
            addCriterion("privilege_free_freight is not null");
            return (Criteria) this;
        }

        public Criteria andPrivilegeFreeFreightEqualTo(Integer value) {
            addCriterion("privilege_free_freight =", value, "privilegeFreeFreight");
            return (Criteria) this;
        }

        public Criteria andPrivilegeFreeFreightNotEqualTo(Integer value) {
            addCriterion("privilege_free_freight <>", value, "privilegeFreeFreight");
            return (Criteria) this;
        }

        public Criteria andPrivilegeFreeFreightGreaterThan(Integer value) {
            addCriterion("privilege_free_freight >", value, "privilegeFreeFreight");
            return (Criteria) this;
        }

        public Criteria andPrivilegeFreeFreightGreaterThanOrEqualTo(Integer value) {
            addCriterion("privilege_free_freight >=", value, "privilegeFreeFreight");
            return (Criteria) this;
        }

        public Criteria andPrivilegeFreeFreightLessThan(Integer value) {
            addCriterion("privilege_free_freight <", value, "privilegeFreeFreight");
            return (Criteria) this;
        }

        public Criteria andPrivilegeFreeFreightLessThanOrEqualTo(Integer value) {
            addCriterion("privilege_free_freight <=", value, "privilegeFreeFreight");
            return (Criteria) this;
        }

        public Criteria andPrivilegeFreeFreightIn(List<Integer> values) {
            addCriterion("privilege_free_freight in", values, "privilegeFreeFreight");
            return (Criteria) this;
        }

        public Criteria andPrivilegeFreeFreightNotIn(List<Integer> values) {
            addCriterion("privilege_free_freight not in", values, "privilegeFreeFreight");
            return (Criteria) this;
        }

        public Criteria andPrivilegeFreeFreightBetween(Integer value1, Integer value2) {
            addCriterion("privilege_free_freight between", value1, value2, "privilegeFreeFreight");
            return (Criteria) this;
        }

        public Criteria andPrivilegeFreeFreightNotBetween(Integer value1, Integer value2) {
            addCriterion("privilege_free_freight not between", value1, value2, "privilegeFreeFreight");
            return (Criteria) this;
        }

        public Criteria andPrivilegeSignInIsNull() {
            addCriterion("privilege_sign_in is null");
            return (Criteria) this;
        }

        public Criteria andPrivilegeSignInIsNotNull() {
            addCriterion("privilege_sign_in is not null");
            return (Criteria) this;
        }

        public Criteria andPrivilegeSignInEqualTo(Integer value) {
            addCriterion("privilege_sign_in =", value, "privilegeSignIn");
            return (Criteria) this;
        }

        public Criteria andPrivilegeSignInNotEqualTo(Integer value) {
            addCriterion("privilege_sign_in <>", value, "privilegeSignIn");
            return (Criteria) this;
        }

        public Criteria andPrivilegeSignInGreaterThan(Integer value) {
            addCriterion("privilege_sign_in >", value, "privilegeSignIn");
            return (Criteria) this;
        }

        public Criteria andPrivilegeSignInGreaterThanOrEqualTo(Integer value) {
            addCriterion("privilege_sign_in >=", value, "privilegeSignIn");
            return (Criteria) this;
        }

        public Criteria andPrivilegeSignInLessThan(Integer value) {
            addCriterion("privilege_sign_in <", value, "privilegeSignIn");
            return (Criteria) this;
        }

        public Criteria andPrivilegeSignInLessThanOrEqualTo(Integer value) {
            addCriterion("privilege_sign_in <=", value, "privilegeSignIn");
            return (Criteria) this;
        }

        public Criteria andPrivilegeSignInIn(List<Integer> values) {
            addCriterion("privilege_sign_in in", values, "privilegeSignIn");
            return (Criteria) this;
        }

        public Criteria andPrivilegeSignInNotIn(List<Integer> values) {
            addCriterion("privilege_sign_in not in", values, "privilegeSignIn");
            return (Criteria) this;
        }

        public Criteria andPrivilegeSignInBetween(Integer value1, Integer value2) {
            addCriterion("privilege_sign_in between", value1, value2, "privilegeSignIn");
            return (Criteria) this;
        }

        public Criteria andPrivilegeSignInNotBetween(Integer value1, Integer value2) {
            addCriterion("privilege_sign_in not between", value1, value2, "privilegeSignIn");
            return (Criteria) this;
        }

        public Criteria andPrivilegeCommentIsNull() {
            addCriterion("privilege_comment is null");
            return (Criteria) this;
        }

        public Criteria andPrivilegeCommentIsNotNull() {
            addCriterion("privilege_comment is not null");
            return (Criteria) this;
        }

        public Criteria andPrivilegeCommentEqualTo(Integer value) {
            addCriterion("privilege_comment =", value, "privilegeComment");
            return (Criteria) this;
        }

        public Criteria andPrivilegeCommentNotEqualTo(Integer value) {
            addCriterion("privilege_comment <>", value, "privilegeComment");
            return (Criteria) this;
        }

        public Criteria andPrivilegeCommentGreaterThan(Integer value) {
            addCriterion("privilege_comment >", value, "privilegeComment");
            return (Criteria) this;
        }

        public Criteria andPrivilegeCommentGreaterThanOrEqualTo(Integer value) {
            addCriterion("privilege_comment >=", value, "privilegeComment");
            return (Criteria) this;
        }

        public Criteria andPrivilegeCommentLessThan(Integer value) {
            addCriterion("privilege_comment <", value, "privilegeComment");
            return (Criteria) this;
        }

        public Criteria andPrivilegeCommentLessThanOrEqualTo(Integer value) {
            addCriterion("privilege_comment <=", value, "privilegeComment");
            return (Criteria) this;
        }

        public Criteria andPrivilegeCommentIn(List<Integer> values) {
            addCriterion("privilege_comment in", values, "privilegeComment");
            return (Criteria) this;
        }

        public Criteria andPrivilegeCommentNotIn(List<Integer> values) {
            addCriterion("privilege_comment not in", values, "privilegeComment");
            return (Criteria) this;
        }

        public Criteria andPrivilegeCommentBetween(Integer value1, Integer value2) {
            addCriterion("privilege_comment between", value1, value2, "privilegeComment");
            return (Criteria) this;
        }

        public Criteria andPrivilegeCommentNotBetween(Integer value1, Integer value2) {
            addCriterion("privilege_comment not between", value1, value2, "privilegeComment");
            return (Criteria) this;
        }

        public Criteria andPrivilegePromotionIsNull() {
            addCriterion("privilege_promotion is null");
            return (Criteria) this;
        }

        public Criteria andPrivilegePromotionIsNotNull() {
            addCriterion("privilege_promotion is not null");
            return (Criteria) this;
        }

        public Criteria andPrivilegePromotionEqualTo(Integer value) {
            addCriterion("privilege_promotion =", value, "privilegePromotion");
            return (Criteria) this;
        }

        public Criteria andPrivilegePromotionNotEqualTo(Integer value) {
            addCriterion("privilege_promotion <>", value, "privilegePromotion");
            return (Criteria) this;
        }

        public Criteria andPrivilegePromotionGreaterThan(Integer value) {
            addCriterion("privilege_promotion >", value, "privilegePromotion");
            return (Criteria) this;
        }

        public Criteria andPrivilegePromotionGreaterThanOrEqualTo(Integer value) {
            addCriterion("privilege_promotion >=", value, "privilegePromotion");
            return (Criteria) this;
        }

        public Criteria andPrivilegePromotionLessThan(Integer value) {
            addCriterion("privilege_promotion <", value, "privilegePromotion");
            return (Criteria) this;
        }

        public Criteria andPrivilegePromotionLessThanOrEqualTo(Integer value) {
            addCriterion("privilege_promotion <=", value, "privilegePromotion");
            return (Criteria) this;
        }

        public Criteria andPrivilegePromotionIn(List<Integer> values) {
            addCriterion("privilege_promotion in", values, "privilegePromotion");
            return (Criteria) this;
        }

        public Criteria andPrivilegePromotionNotIn(List<Integer> values) {
            addCriterion("privilege_promotion not in", values, "privilegePromotion");
            return (Criteria) this;
        }

        public Criteria andPrivilegePromotionBetween(Integer value1, Integer value2) {
            addCriterion("privilege_promotion between", value1, value2, "privilegePromotion");
            return (Criteria) this;
        }

        public Criteria andPrivilegePromotionNotBetween(Integer value1, Integer value2) {
            addCriterion("privilege_promotion not between", value1, value2, "privilegePromotion");
            return (Criteria) this;
        }

        public Criteria andPrivilegeMemberPriceIsNull() {
            addCriterion("privilege_member_price is null");
            return (Criteria) this;
        }

        public Criteria andPrivilegeMemberPriceIsNotNull() {
            addCriterion("privilege_member_price is not null");
            return (Criteria) this;
        }

        public Criteria andPrivilegeMemberPriceEqualTo(Integer value) {
            addCriterion("privilege_member_price =", value, "privilegeMemberPrice");
            return (Criteria) this;
        }

        public Criteria andPrivilegeMemberPriceNotEqualTo(Integer value) {
            addCriterion("privilege_member_price <>", value, "privilegeMemberPrice");
            return (Criteria) this;
        }

        public Criteria andPrivilegeMemberPriceGreaterThan(Integer value) {
            addCriterion("privilege_member_price >", value, "privilegeMemberPrice");
            return (Criteria) this;
        }

        public Criteria andPrivilegeMemberPriceGreaterThanOrEqualTo(Integer value) {
            addCriterion("privilege_member_price >=", value, "privilegeMemberPrice");
            return (Criteria) this;
        }

        public Criteria andPrivilegeMemberPriceLessThan(Integer value) {
            addCriterion("privilege_member_price <", value, "privilegeMemberPrice");
            return (Criteria) this;
        }

        public Criteria andPrivilegeMemberPriceLessThanOrEqualTo(Integer value) {
            addCriterion("privilege_member_price <=", value, "privilegeMemberPrice");
            return (Criteria) this;
        }

        public Criteria andPrivilegeMemberPriceIn(List<Integer> values) {
            addCriterion("privilege_member_price in", values, "privilegeMemberPrice");
            return (Criteria) this;
        }

        public Criteria andPrivilegeMemberPriceNotIn(List<Integer> values) {
            addCriterion("privilege_member_price not in", values, "privilegeMemberPrice");
            return (Criteria) this;
        }

        public Criteria andPrivilegeMemberPriceBetween(Integer value1, Integer value2) {
            addCriterion("privilege_member_price between", value1, value2, "privilegeMemberPrice");
            return (Criteria) this;
        }

        public Criteria andPrivilegeMemberPriceNotBetween(Integer value1, Integer value2) {
            addCriterion("privilege_member_price not between", value1, value2, "privilegeMemberPrice");
            return (Criteria) this;
        }

        public Criteria andPrivilegeBirthdayIsNull() {
            addCriterion("privilege_birthday is null");
            return (Criteria) this;
        }

        public Criteria andPrivilegeBirthdayIsNotNull() {
            addCriterion("privilege_birthday is not null");
            return (Criteria) this;
        }

        public Criteria andPrivilegeBirthdayEqualTo(Integer value) {
            addCriterion("privilege_birthday =", value, "privilegeBirthday");
            return (Criteria) this;
        }

        public Criteria andPrivilegeBirthdayNotEqualTo(Integer value) {
            addCriterion("privilege_birthday <>", value, "privilegeBirthday");
            return (Criteria) this;
        }

        public Criteria andPrivilegeBirthdayGreaterThan(Integer value) {
            addCriterion("privilege_birthday >", value, "privilegeBirthday");
            return (Criteria) this;
        }

        public Criteria andPrivilegeBirthdayGreaterThanOrEqualTo(Integer value) {
            addCriterion("privilege_birthday >=", value, "privilegeBirthday");
            return (Criteria) this;
        }

        public Criteria andPrivilegeBirthdayLessThan(Integer value) {
            addCriterion("privilege_birthday <", value, "privilegeBirthday");
            return (Criteria) this;
        }

        public Criteria andPrivilegeBirthdayLessThanOrEqualTo(Integer value) {
            addCriterion("privilege_birthday <=", value, "privilegeBirthday");
            return (Criteria) this;
        }

        public Criteria andPrivilegeBirthdayIn(List<Integer> values) {
            addCriterion("privilege_birthday in", values, "privilegeBirthday");
            return (Criteria) this;
        }

        public Criteria andPrivilegeBirthdayNotIn(List<Integer> values) {
            addCriterion("privilege_birthday not in", values, "privilegeBirthday");
            return (Criteria) this;
        }

        public Criteria andPrivilegeBirthdayBetween(Integer value1, Integer value2) {
            addCriterion("privilege_birthday between", value1, value2, "privilegeBirthday");
            return (Criteria) this;
        }

        public Criteria andPrivilegeBirthdayNotBetween(Integer value1, Integer value2) {
            addCriterion("privilege_birthday not between", value1, value2, "privilegeBirthday");
            return (Criteria) this;
        }

        public Criteria andNoteIsNull() {
            addCriterion("note is null");
            return (Criteria) this;
        }

        public Criteria andNoteIsNotNull() {
            addCriterion("note is not null");
            return (Criteria) this;
        }

        public Criteria andNoteEqualTo(String value) {
            addCriterion("note =", value, "note");
            return (Criteria) this;
        }

        public Criteria andNoteNotEqualTo(String value) {
            addCriterion("note <>", value, "note");
            return (Criteria) this;
        }

        public Criteria andNoteGreaterThan(String value) {
            addCriterion("note >", value, "note");
            return (Criteria) this;
        }

        public Criteria andNoteGreaterThanOrEqualTo(String value) {
            addCriterion("note >=", value, "note");
            return (Criteria) this;
        }

        public Criteria andNoteLessThan(String value) {
            addCriterion("note <", value, "note");
            return (Criteria) this;
        }

        public Criteria andNoteLessThanOrEqualTo(String value) {
            addCriterion("note <=", value, "note");
            return (Criteria) this;
        }

        public Criteria andNoteLike(String value) {
            addCriterion("note like", value, "note");
            return (Criteria) this;
        }

        public Criteria andNoteNotLike(String value) {
            addCriterion("note not like", value, "note");
            return (Criteria) this;
        }

        public Criteria andNoteIn(List<String> values) {
            addCriterion("note in", values, "note");
            return (Criteria) this;
        }

        public Criteria andNoteNotIn(List<String> values) {
            addCriterion("note not in", values, "note");
            return (Criteria) this;
        }

        public Criteria andNoteBetween(String value1, String value2) {
            addCriterion("note between", value1, value2, "note");
            return (Criteria) this;
        }

        public Criteria andNoteNotBetween(String value1, String value2) {
            addCriterion("note not between", value1, value2, "note");
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