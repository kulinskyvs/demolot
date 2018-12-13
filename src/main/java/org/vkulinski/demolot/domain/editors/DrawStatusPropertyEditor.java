package org.vkulinski.demolot.domain.editors;

import org.vkulinski.demolot.domain.DrawStatus;

import java.beans.PropertyEditorSupport;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;


/**
 * @author M-VKU
 * @version 1.0
 */
public class DrawStatusPropertyEditor extends PropertyEditorSupport
{

  @Override
  public String getAsText()
  {
    return Optional
        .ofNullable(getValue())
        .map(status -> ((DrawStatus) status).getDescription())
        .orElse(null);
  }


  @Override
  public void setAsText(String text) throws IllegalArgumentException
  {
    setValue(Stream
        .of(DrawStatus.values())
        .filter(status -> Objects.equals(text, status.getDescription()))
        .findFirst()
        .orElse(null));
  }

}