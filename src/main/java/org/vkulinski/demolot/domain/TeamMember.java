package org.vkulinski.demolot.domain;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.Objects;

import static org.apache.commons.lang3.builder.ToStringStyle.MULTI_LINE_STYLE;


@Entity
@Table(name = "TEAM_MEMBER", uniqueConstraints = @UniqueConstraint(columnNames = { "NAME", "SURNAME" }))
public class TeamMember implements HasLongId
{

  public static final int NAME_MAX_LENGTH = 50;
  public static final int SURNAME_MAX_LENGTH = 50;
  public static final int EMAIL_MAX_LENGTH = 50;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "NAME", nullable = false)
  @NotBlank
  @Length(min = 1, max = NAME_MAX_LENGTH)
  private String name;

  @Column(name = "SURNAME", nullable = false)
  @NotBlank
  @Length(min = 1, max = SURNAME_MAX_LENGTH)
  private String surname;

  @Column(name = "EMAIL", nullable = false)
  @Email
  @NotEmpty
  @Length(min = 1, max = EMAIL_MAX_LENGTH)
  private String email;

  @Column(name = "IS_ACTIVE", nullable = false)
  private boolean active = true;


  public TeamMember()
  {
  }


  public TeamMember(@NotBlank @Length(max = NAME_MAX_LENGTH) String name,
                    @NotBlank @Length(max = SURNAME_MAX_LENGTH) String surname)
  {
    this.name = name;
    this.surname = surname;
  }


  public Long getId()
  {
    return id;
  }


  public void setId(Long id)
  {
    this.id = id;
  }


  public String getName()
  {
    return name;
  }


  public void setName(String name)
  {
    this.name = name;
  }


  public String getSurname()
  {
    return surname;
  }


  public void setSurname(String surname)
  {
    this.surname = surname;
  }


  public String getEmail()
  {
    return email;
  }


  public void setEmail(String email)
  {
    this.email = email;
  }


  public boolean isActive()
  {
    return active;
  }


  public void setActive(boolean active)
  {
    this.active = active;
  }


  @Override
  public boolean equals(Object o)
  {
    if (this == o) return true;
    if (o == null || !(o instanceof TeamMember)) return false;

    TeamMember that = (TeamMember) o;
    return new EqualsBuilder()
        .append(name, that.getName())
        .append(surname, that.getSurname())
        .isEquals();
  }


  @Override
  public int hashCode()
  {
    return Objects.hash(name, surname);
  }


  @Override
  public String toString()
  {
    return new ToStringBuilder(this, MULTI_LINE_STYLE)
        .append("id", id)
        .append("name", name)
        .append("surname", surname)
        .append("email", email)
        .append("active", active)
        .toString();
  }
}