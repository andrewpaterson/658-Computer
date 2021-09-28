package name.bizna.emu65816.addressingmode;

import name.bizna.emu65816.AddressingMode;

import java.util.Arrays;
import java.util.List;

public abstract class BusCycles
{
  protected List<BusCycle> cycles;
  protected AddressingMode addressingMode;

  public BusCycles(AddressingMode addressingMode,
                   BusCycle... cycles)
  {
    this.addressingMode = addressingMode;
    this.cycles = Arrays.asList(cycles);
  }

  public AddressingMode getAddressingMode()
  {
    return addressingMode;
  }

  public abstract boolean execute(int cycle);
}

