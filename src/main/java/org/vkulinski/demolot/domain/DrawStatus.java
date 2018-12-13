package org.vkulinski.demolot.domain;

/**
 * Represents the draw status of some particular {@link Demo}
 */
public enum DrawStatus
{
  PREPARATION("Preparation"),
  IN_PROGRESS("In progress"),
  FINISHED("Finished");

  private String description;


  DrawStatus(String description)
  {
    this.description = description;
  }


  public String getDescription()
  {
    return description;
  }
}
