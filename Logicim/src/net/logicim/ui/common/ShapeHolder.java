package net.logicim.ui.common;

import net.logicim.common.type.Tuple2;
import net.logicim.ui.shape.ShapeView;

public interface ShapeHolder
{
  void add(ShapeView shapeView);

  Tuple2 getPosition();

  Rotation getRotation();
}

