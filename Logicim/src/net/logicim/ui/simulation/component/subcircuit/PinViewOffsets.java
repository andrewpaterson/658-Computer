package net.logicim.ui.simulation.component.subcircuit;

import net.common.type.Tuple2;
import net.logicim.ui.simulation.component.passive.pin.PinView;

import java.util.ArrayList;
import java.util.List;

public abstract class PinViewOffsets
{
  protected List<PinViewOffset> calculatePinViewOffsets(PinViewLists pinViewLists, int offset, int oppositeOffset)
  {
    int missing = (oppositeOffset - offset) - pinViewLists.getPinSize();

    ArrayList<PinViewOffset> pinViewOffsets = new ArrayList<>();
    int firstTopPinOffset = offset + 1;
    int lastTopPinOffset = createPinViewOffsets(pinViewOffsets, firstTopPinOffset, pinViewLists.negativePinViews);

    int firstBottomPinOffset = oppositeOffset - pinViewLists.positivePinViews.size();
    createPinViewOffsets(pinViewOffsets, firstBottomPinOffset, pinViewLists.positivePinViews);

    int firstCenterPinOffset = lastTopPinOffset + missing / 2;
    createPinViewOffsets(pinViewOffsets, firstCenterPinOffset, pinViewLists.centerPinViews);

    return pinViewOffsets;
  }

  private int createPinViewOffsets(ArrayList<PinViewOffset> pinViewOffsets, int offset, List<PinView> pinViews)
  {
    if (pinViews.size() > 0)
    {
      for (PinView pinView : pinViews)
      {
        pinViewOffsets.add(new PinViewOffset(pinView, offset));
        offset++;
      }
      offset++;
    }

    return offset;
  }

  protected int calculateMaxSize(int firstSize, int secondSize)
  {
    firstSize = firstSize != 0 ? firstSize - 1 : 0;
    secondSize = secondSize != 0 ? secondSize - 1 : 0;
    return Math.max(firstSize, secondSize);
  }

  protected float calculateMaxSize(List<PinViewOffset> pinViewOffsets)
  {
    float maxWidth = 0;
    for (PinViewOffset pinViewOffset : pinViewOffsets)
    {
      Tuple2 dimension = pinViewOffset.pinView.getLabelView().getTextDimension();
      if (dimension.getX() > maxWidth)
      {
        maxWidth = dimension.getX();
      }
    }
    return maxWidth;
  }
}
