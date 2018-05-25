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
import org.hibernate.validator.constraints.URL;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

import static org.apache.commons.lang3.builder.ToStringStyle.MULTI_LINE_STYLE;


/**
 * @author M-VKU
 * @version 1.0
 */
@Entity
@Table(name = "DEMO")
public class Demo implements HasLongId
{
  public static final int TITLE_MAX_LENGTH = 100;
  public static final int SUMMARY_MAX_LENGTH = 1000;
  public static final int LINK_MAX_LENGTH = 200;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;


  @Column(name = "TITLE", nullable = false)
  @NotBlank
  @Length(min = 1, max = TITLE_MAX_LENGTH)
  private String title;


  @Column(name = "PLANNED_DATE", nullable = false)
  @NotNull
  @DateTimeFormat(pattern = "yyyy-MM-dd")
  private LocalDate plannedDate;


  @Column(name = "SUMMARY")
  @Length(max = SUMMARY_MAX_LENGTH)
    private String summary;


  @Column(name = "LINK")
  @URL
  @Length(max = LINK_MAX_LENGTH)
  private String link;


  @OneToMany(mappedBy = "demo", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<DemoTask> tasks;


  public Demo()
  {
  }


  public Long getId()
  {
    return id;
  }


  public void setId(Long id)
  {
    this.id = id;
  }


  public String getTitle()
  {
    return title;
  }


  public void setTitle(String title)
  {
    this.title = title;
  }


  public LocalDate getPlannedDate()
  {
    return plannedDate;
  }


  public void setPlannedDate(LocalDate plannedDate)
  {
    this.plannedDate = plannedDate;
  }


  public String getSummary()
  {
    return summary;
  }


  public void setSummary(String summary)
  {
    this.summary = summary;
  }


  public String getLink()
  {
    return link;
  }


  public void setLink(String link)
  {
    this.link = link;
  }


  public List<DemoTask> getTasks()
  {
    return tasks;
  }


  public void setTasks(List<DemoTask> tasks)
  {
    this.tasks = tasks;
  }


  @Override
  public boolean equals(Object o)
  {
    if (this == o) return true;
    if (o == null || !(o instanceof Demo)) return false;

    Demo that = (Demo) o;
    return new EqualsBuilder()
        .append(title, that.getTitle())
        .append(plannedDate, that.getPlannedDate())
        .isEquals();
  }


  @Override
  public int hashCode()
  {
    return Objects.hash(title, plannedDate);
  }


  @Override
  public String toString()
  {
    return new ToStringBuilder(this, MULTI_LINE_STYLE)
        .append("id", id)
        .append("title", title)
        .append("plannedDate", plannedDate)
        .toString();
  }

}