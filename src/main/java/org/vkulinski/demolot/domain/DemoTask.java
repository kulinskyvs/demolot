package org.vkulinski.demolot.domain;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.URL;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
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

  public static final int KEY_MAX_LENGTH = 20;
  public static final int TITLE_MAX_LENGTH = 100;
  public static final int LINK_MAX_LENGTH = 200;


  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne
  @JoinColumn(name = "DEMO_ID", nullable = false)
  private Demo demo;


  @Column(name = "KEY", nullable = false)
  @NotBlank
  @Length(min = 1, max = KEY_MAX_LENGTH)
  private String key;


  @Column(name = "TITLE", nullable = false)
  @NotBlank
  @Length(min = 1, max = TITLE_MAX_LENGTH)
  private String title;


  @OneToOne
  @JoinColumn(name = "OWNER_ID", nullable = false)
  @NotNull
  private TeamMember owner;


  @Column(name = "LINK")
  @URL
  @Length(max = LINK_MAX_LENGTH)
  private String link;


  @Column(name = "DRAW_TIMESTAMP")
  @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  private LocalDateTime drawDateTime;


  @OneToOne
  @JoinColumn(name = "WINNER_ID")
  private TeamMember winner;


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


  public String getLink()
  {
    return link;
  }


  public void setLink(String link)
  {
    this.link = link;
  }


  public LocalDateTime getDrawDateTime()
  {
    return drawDateTime;
  }


  public void setDrawDateTime(LocalDateTime drawDateTime)
  {
    this.drawDateTime = drawDateTime;
  }


  public TeamMember getWinner()
  {
    return winner;
  }


  public void setWinner(TeamMember winner)
  {
    this.winner = winner;
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


  /**
   * Returns a boolean flag that denotes whether a winner was defined for the task or not
   *
   * @return boolean value that denotes whether a winner was defined for the task or not
   */
  public boolean hasWinner()
  {
    return Objects.nonNull(winner);
  }
}