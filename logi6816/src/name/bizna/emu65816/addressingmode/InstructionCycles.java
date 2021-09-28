package name.bizna.emu65816.addressingmode;

import name.bizna.emu65816.AddressingMode;

import java.util.Arrays;
import java.util.List;

public abstract class InstructionCycles
{
  protected List<BusCycle> cycles;
  protected AddressingMode addressingMode;

  public InstructionCycles(AddressingMode addressingMode,
                           BusCycle... cycles)
  {
    this.addressingMode = addressingMode;
    this.cycles = Arrays.asList(cycles);
  }

  public AddressingMode getAddressingMode()
  {
    return addressingMode;
  }
}

