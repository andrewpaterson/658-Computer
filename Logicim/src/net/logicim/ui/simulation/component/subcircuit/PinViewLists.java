package net.logicim.ui.simulation.component.subcircuit;

import net.logicim.ui.simulation.component.passive.pin.PinView;

import java.util.List;

public class PinViewLists
{
  protected List<PinView> negativePinViews;
  protected List<PinView> centerPinViews;
  protected List<PinView> positivePinViews;

  protected float widestText;

  public PinViewLists(List<PinView> negativePinViews,
                      List<PinView> centerPinViews,
                      List<PinView> positivePinViews,
                      float widestText)
  {
    this.centerPinViews = centerPinViews;
    this.negativePinViews = negativePinViews;
    this.positivePinViews = positivePinViews;
    this.widestText = widestText;
  }
}

