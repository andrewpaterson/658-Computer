package net.logicim.ui.editor;

import net.common.type.Float2D;

public class SubcircuitViewParameters
{
  protected Float2D position;
  protected float zoom;

  public SubcircuitViewParameters(Float2D position, float zoom)
  {
    this.position = position;
    this.zoom = zoom;
  }

  public Float2D getPosition()
  {
    return position;
  }

  public float getZoom()
  {
    return zoom;
  }

  public void set(Float2D position, float zoom)
  {
    this.position = position;
    this.zoom = zoom;
  }
}

