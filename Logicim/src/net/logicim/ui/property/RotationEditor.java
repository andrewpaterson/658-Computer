package net.logicim.ui.property;

import net.logicim.ui.common.Rotation;
import net.logicim.ui.components.typeeditor.EnumPropertyEditor;

import java.util.HashSet;
import java.util.Set;

public class RotationEditor
    extends EnumPropertyEditor<Rotation>
{
  public RotationEditor(String name, Rotation current)
  {
    super(name, Rotation.class, current, toIgnored(current));
    addIgnored(Rotation.Cannot);
  }

  private static Set<Rotation> toIgnored(Rotation current)
  {
    HashSet<Rotation> rotations = new HashSet<>();
    if (current.isCannot())
    {
      rotations.add(Rotation.North);
      rotations.add(Rotation.South);
      rotations.add(Rotation.East);
      rotations.add(Rotation.West);
    }
    else
    {
      rotations.add(Rotation.Cannot);
    }
    return rotations;
  }
}

