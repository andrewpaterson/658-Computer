package net.logicim.ui.simulation.component.subcircuit;

import net.logicim.ui.shape.rectangle.Rectangle;

import java.util.List;

public class LeftRightPinViewOffsets
    extends PinViewOffsets
{
  public List<PinViewOffset> topPinViewOffsets;
  public List<PinViewOffset> bottomPinViewOffsets;

  public LeftRightPinViewOffsets(Rectangle rectangle, PinViewLists topViewLists, PinViewLists bottomViewLists)
  {
    int maxGridWidth = calculateMaxSize(topViewLists.getExtendedSize(), bottomViewLists.getExtendedSize());

    int left = -maxGridWidth / 2;
    int right = maxGridWidth + left;

    topPinViewOffsets = calculatePinViewOffsets(topViewLists, left, right);
    bottomPinViewOffsets = calculatePinViewOffsets(bottomViewLists, left, right);

    int top = (int) Math.ceil(rectangle.getTopLeft().getY() - calculateMaxSize(topPinViewOffsets));
    int bottom = (int) Math.floor(rectangle.getBottomRight().getY() + calculateMaxSize(bottomPinViewOffsets));

    rectangle.getTopLeft().setMinY(top);
    rectangle.getBottomRight().setMaxY(bottom);

    int heightOffset = (maxGridWidth / 2);
    rectangle.getTopLeft().setMinX(-heightOffset);
    rectangle.getBottomRight().setMaxX(maxGridWidth - heightOffset);
  }
}

