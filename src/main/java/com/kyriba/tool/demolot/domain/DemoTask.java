/********************************************************************************
 * Copyright 2000 - 2018 Kyriba Corp. All Rights Reserved.                   *
 * The content of this file is copyrighted by Kyriba Corporation and can not be *
 * reproduced, distributed, altered or used in any form, in whole or in part.   *
 * Date        Author  Changes                                                  *
 * 5/25/2018     M-VKU   Initial                                                  *
 * Copyright 2000 - 2018 Kyriba Corp. All Rights Reserved.                   *
 ********************************************************************************/
package com.kyriba.tool.demolot.domain;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Objects;

import static org.apache.commons.lang3.builder.ToStringStyle.MULTI_LINE_STYLE;


/**
 * @author M-VKU
 * @version 1.0
 */
@Entity
@Table(name = "DEMO_TASK")
public class DemoTask implements HasLongId
{

  public static final int KEY_MAX_LENGTH = 10;
  public static final int TITLE_MAX_LENGTH = 100;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;


  @Column(name = "KEY", nullable = false )
  @NotBlank
  @Length(min = 1, max = KEY_MAX_LENGTH)
  private String key;


  @Column(name = "TITLE", nullable = false)
  @NotBlank
  @Length(min = 1, max = TITLE_MAX_LENGTH)
  private String title;


  @ManyToOne
  @JoinColumn(name = "DEMO_ID", nullable = false)
  private Demo demo;


  @OneToOne
  @JoinColumn(name = "OWNER_ID", nullable = false)
  @NotNull
  private TeamMember owner;


  public Long getId()
  {
    return id;
  }


  public void setId(Long id)
  {
    this.id = id;
  }


  public String getKey()
  {
    return key;
  }


  public void setKey(String key)
  {
    this.key = key;
  }


  public String getTitle()
  {
    return title;
  }


  public void setTitle(String title)
  {
    this.title = title;
  }


  public Demo getDemo()
  {
    return demo;
  }


  public void setDemo(Demo demo)
  {
    this.demo = demo;
  }


  public TeamMember getOwner()
  {
    return owner;
  }


  public void setOwner(TeamMember owner)
  {
    this.owner = owner;
  }


  @Override
  public boolean equals(Object o)
  {
    if (this == o) return true;
    if (o == null || !(o instanceof DemoTask)) return false;

    DemoTask that = (DemoTask) o;
    return new EqualsBuilder()
        .append(key, that.getKey())
        .isEquals();
  }


  @Override
  public int hashCode()
  {
    return Objects.hash(key);
  }


  @Override
  public String toString()
  {
    return new ToStringBuilder(this, MULTI_LINE_STYLE)
        .append("id", id)
        .append("key", key)
        .append("title", title)
        .toString();
  }
}