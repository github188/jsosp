package com.jieshun.ops.autode.model;

import java.util.Date;

import javax.persistence.Entity;

import com.jieshun.ops.comm.BaseDataModel;

/**
 * 用户领域对象
 */
@Entity
public class Person extends BaseDataModel{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2032598748271515555L;
	private String id;
	private String person_code;
	private String person_name;
	private String org_id; // 员工编号
	private int is_clerk;
	private String sex;
	private int age ;
	private String native_place;
	private String telephone;
	private String address;
	private String email;
	private String card_type;
	private String card_no;
	private String photo;
	private String status;
	private String controlunitid;
	private String post;
	private String office_tel;
	private Date dimission_date;
	private Date join_date;
	private int is_edit;
	private String person_type;
	private int is_operator;
	private String visiter_key;
	private String sub_person_id;
	private String pass_auth;
	private Date invalid_date;
	private String work_type;
	@Override
	public String getId() {
		return id;
	}
	@Override
	public void setId(String id) {
		this.id = id;
	}
	public String getPerson_code() {
		return person_code;
	}
	public void setPerson_code(String person_code) {
		this.person_code = person_code;
	}
	public String getPerson_name() {
		return person_name;
	}
	public void setPerson_name(String person_name) {
		this.person_name = person_name;
	}
	public String getOrg_id() {
		return org_id;
	}
	public void setOrg_id(String org_id) {
		this.org_id = org_id;
	}
	public int getIs_clerk() {
		return is_clerk;
	}
	public void setIs_clerk(int is_clerk) {
		this.is_clerk = is_clerk;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
	public String getNative_place() {
		return native_place;
	}
	public void setNative_place(String native_place) {
		this.native_place = native_place;
	}
	public String getTelephone() {
		return telephone;
	}
	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getCard_type() {
		return card_type;
	}
	public void setCard_type(String card_type) {
		this.card_type = card_type;
	}
	public String getCard_no() {
		return card_no;
	}
	public void setCard_no(String card_no) {
		this.card_no = card_no;
	}
	public String getPhoto() {
		return photo;
	}
	public void setPhoto(String photo) {
		this.photo = photo;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getControlunitid() {
		return controlunitid;
	}
	public void setControlunitid(String controlunitid) {
		this.controlunitid = controlunitid;
	}
	public String getPost() {
		return post;
	}
	public void setPost(String post) {
		this.post = post;
	}
	public String getOffice_tel() {
		return office_tel;
	}
	public void setOffice_tel(String office_tel) {
		this.office_tel = office_tel;
	}
	public Date getDimission_date() {
		return dimission_date;
	}
	public void setDimission_date(Date dimission_date) {
		this.dimission_date = dimission_date;
	}
	public Date getJoin_date() {
		return join_date;
	}
	public void setJoin_date(Date join_date) {
		this.join_date = join_date;
	}
	public int getIs_edit() {
		return is_edit;
	}
	public void setIs_edit(int is_edit) {
		this.is_edit = is_edit;
	}
	public String getPerson_type() {
		return person_type;
	}
	public void setPerson_type(String person_type) {
		this.person_type = person_type;
	}
	public int getIs_operator() {
		return is_operator;
	}
	public void setIs_operator(int is_operator) {
		this.is_operator = is_operator;
	}
	public String getVisiter_key() {
		return visiter_key;
	}
	public void setVisiter_key(String visiter_key) {
		this.visiter_key = visiter_key;
	}
	public String getSub_person_id() {
		return sub_person_id;
	}
	public void setSub_person_id(String sub_person_id) {
		this.sub_person_id = sub_person_id;
	}
	public String getPass_auth() {
		return pass_auth;
	}
	public void setPass_auth(String pass_auth) {
		this.pass_auth = pass_auth;
	}
	public Date getInvalid_date() {
		return invalid_date;
	}
	public void setInvalid_date(Date invalid_date) {
		this.invalid_date = invalid_date;
	}
	public String getWork_type() {
		return work_type;
	}
	public void setWork_type(String work_type) {
		this.work_type = work_type;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
}