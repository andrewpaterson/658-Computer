package net.logicim.ui.simulation.component.subcircuit;

import net.logicim.ui.simulation.component.passive.pin.PinView;

import java.util.List;

public class PinViewLists
{
  protected List<PinView> negativePinViews;
  protected List<PinView> centerPinViews;
  protected List<PinView> positivePinViews;

  protected float widestText;
  protected int pinSize;
  protected int extendedSize;

  public PinViewLists(List<PinView> negativePinViews,
                      List<PinView> centerPinViews,
                      List<PinView> positivePinViews,
                      float widestText)
  {
    this.centerPinViews = centerPinViews;
    this.negativePinViews = negativePinViews;
    this.positivePinViews = positivePinViews;
    this.widestText = widestText;
    this.pinSize = calculatePinSize();
    this.extendedSize = calculateExtendedSize(pinSize);
  }

  private int calculateExtendedSize(int pinSize)
  {
    if (pinSize == 0)
    {
      return 0;
    }
    return pinSize + 2;
  }

  public int calculatePinSize()
  {
    int pins = 0;
    if (negativePinViews.size() > 0)
    {
      pins++;
    }
    if (centerPinViews.size() > 0)
    {
      pins++;
    }
    if (positivePinViews.size() > 0)
    {
      pins++;
    }
    if (pins == 0)
    {
      return 0;
    }
    pins--; //Separating spaces.

    pins += negativePinViews.size();
    pins += centerPinViews.size();
    pins += positivePinViews.size();

    return pins;
  }

  public int getPinSize()
  {
    return pinSize;
  }

  public int getExtendedSize()
  {
    return extendedSize;
  }
}

