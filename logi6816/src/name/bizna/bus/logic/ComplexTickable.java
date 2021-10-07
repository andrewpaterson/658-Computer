package name.bizna.bus.logic;

public interface ComplexTickable
    extends Tickable
{
  void undoInternalPropagationChange();
}
