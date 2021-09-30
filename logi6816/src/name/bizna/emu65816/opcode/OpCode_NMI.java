package name.bizna.emu65816.opcode;

import name.bizna.emu65816.AddressingMode;
import name.bizna.emu65816.Cpu65816;

public class NonMaskableInterrupt
    extends OpCode
{
  public NonMaskableInterrupt(String mName, int mCode, AddressingMode mAddressingMode)
  {
    super(mName, mCode, mAddressingMode);
  }

  @Override
  public void executeOnFallingEdge(Cpu65816 cpu)
  {
  }

  @Override
  public void executeOnRisingEdge(Cpu65816 cpu)
  {
  }
}

