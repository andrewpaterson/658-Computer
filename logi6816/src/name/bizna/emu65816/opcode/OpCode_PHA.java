package name.bizna.emu65816.opcode;

import name.bizna.emu65816.AddressingMode;
import name.bizna.emu65816.Binary;
import name.bizna.emu65816.Cpu65816;

public class OpCode_PHA
    extends OpCode
{
  public OpCode_PHA(String mName, int mCode, AddressingMode mAddressingMode)
  {
    super(mName, mCode, mAddressingMode);
  }

  @Override
  public void executeOnFallingEdge(Cpu65816 cpu)
  {
    if (cpu.isMemory8Bit())
    {
      cpu.push8Bit(Binary.getLowByte(cpu.getA()));
      cpu.addToProgramAddressAndCycles(1, 4);
    }
    else
    {
      cpu.push16Bit(cpu.getA());
      cpu.addToProgramAddressAndCycles(1, 3);
    }
  }

  @Override
  public void executeOnRisingEdge(Cpu65816 cpu)
  {
  }
}

