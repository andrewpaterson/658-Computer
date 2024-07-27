package net.logicim.ui.simulation.component.subcircuit;

import net.logicim.ui.shape.rectangle.Rectangle;

import java.util.List;

public class TopBottomPinViewOffsets
    extends PinViewOffsets
{
  public List<PinViewOffset> leftPinViewOffsets;
  public List<PinViewOffset> rightPinViewOffsets;

  public TopBottomPinViewOffsets(Rectangle rectangle, PinViewLists leftViewLists, PinViewLists rightViewLists)
  {
    int maxGridHeight = calculateMaxSize(leftViewLists.getExtendedSize(), rightViewLists.getExtendedSize());
    int bottom = rectangle.getBottomRight().getIntY();
    int top = rectangle.getTopLeft().getIntY();

    int size = bottom - top;
    if (maxGridHeight < size)
    {
      leftPinViewOffsets = calculatePinViewOffsets(leftViewLists, top, bottom);
      rightPinViewOffsets = calculatePinViewOffsets(rightViewLists, top, bottom);
    }
    else
    {
      int height = maxGridHeight - size;
      top -= height / 2;
      bottom += height - (height / 2);

      leftPinViewOffsets = calculatePinViewOffsets(leftViewLists, top, bottom);
      rightPinViewOffsets = calculatePinViewOffsets(rightViewLists, top, bottom);
    }

    rectangle.getTopLeft().setMinY(top);
    rectangle.getBottomRight().setMaxY(bottom);
  }
}

