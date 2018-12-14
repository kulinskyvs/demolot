package org.vkulinski.demolot.domain.editors;

import org.vkulinski.demolot.domain.TeamMember;

import java.beans.PropertyEditorSupport;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;


/**
 * @author M-VKU
 * @version 1.0
 */
public class TeamMemberPropertyEditor extends PropertyEditorSupport
{
  public static final Function<TeamMember, String> DEFAULT_FORMATTER = member -> Optional
      .ofNullable(member)
      .map(safeMember -> safeMember.getName() + " " + safeMember.getSurname())
      .orElse("");

  private final Function<TeamMember, String> formatter;
  private final Supplier<List<TeamMember>> membersSupplier;


  public TeamMemberPropertyEditor(Supplier<List<TeamMember>> membersSupplier)
  {
    this(membersSupplier, DEFAULT_FORMATTER);
  }


  public TeamMemberPropertyEditor(Supplier<List<TeamMember>> membersSupplier,
                                  Function<TeamMember, String> formatter)
  {
    this.membersSupplier = membersSupplier;
    this.formatter = formatter;
  }


  @Override
  public String getAsText()
  {
    return formatter.apply((TeamMember) getValue());
  }


  @Override
  public void setAsText(String text)
  {
    setValue(membersSupplier
        .get()
        .stream()
        .filter(member -> Objects.equals(text, formatter.apply(member)))
        .findFirst()
        .orElse(null)
    );
  }

}