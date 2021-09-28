package name.bizna.emu65816.opcode;

import name.bizna.emu65816.AddressingMode;
import name.bizna.emu65816.Binary;
import name.bizna.emu65816.Cpu65816;

public class OpCode_PHY
    extends OpCode
{
  public OpCode_PHY(String mName, int mCode, AddressingMode mAddressingMode)
  {
    super(mName, mCode, mAddressingMode);
  }

  @Override
  public void executeOnFallingEdge(Cpu65816 cpu)
  {
    if (cpu.isIndex8Bit())
    {
      cpu.push8Bit(Binary.lower8BitsOf(cpu.getY()));
      cpu.addToProgramAddressAndCycles(1, 3);
    }
    else
    {
      cpu.push16Bit(cpu.getY());
      cpu.addToProgramAddressAndCycles(1, 4);
    }
  }

  @Override
  public void executeOnRisingEdge(Cpu65816 cpu)
  {
  }
}

