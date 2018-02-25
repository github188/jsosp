package com.jieshun.ops.util;

public class OpenfireBean
{
  private String openfire_domain;
  private String host;
  private String username;
  private String password;
  private static String login_url = "/login.jsp";
  private static String summer_url = "/session-summary.jsp?range=10000&refresh=0";

  public String getHost() {
    return this.host;
  }
  public void setHost(String host) {
    this.host = host;
  }
  public String getUsername() {
    return this.username;
  }
  public void setUsername(String username) {
    this.username = username;
  }
  public String getPassword() {
    return this.password;
  }
  public void setPassword(String password) {
    this.password = password;
  }
  public String getLoginUrl() {
    return this.host + login_url;
  }
  public String getSummerUrl() {
    return this.host + summer_url;
  }
  public String getOpenfire_domain() {
    return this.openfire_domain;
  }
  public void setOpenfire_domain(String openfire_domain) {
    this.openfire_domain = openfire_domain;
  }
}