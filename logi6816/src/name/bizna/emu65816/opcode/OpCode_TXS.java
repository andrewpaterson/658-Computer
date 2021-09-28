package name.bizna.emu65816.opcode;

import name.bizna.emu65816.Address;
import name.bizna.emu65816.AddressingMode;
import name.bizna.emu65816.Binary;
import name.bizna.emu65816.Cpu65816;

public class OpCode_TXS
    extends OpCode
{
  public OpCode_TXS(String mName, int mCode, AddressingMode mAddressingMode)
  {
    super(mName, mCode, mAddressingMode);
  }

  @Override
  public void executeOnFallingEdge(Cpu65816 cpu)
  {
    if (cpu.getCpuStatus().isEmulationMode())
    {
      short newStackPointer = 0x100;
      newStackPointer |= Binary.lower8BitsOf(cpu.getX());
      cpu.clearStack(new Address(0x00, newStackPointer));
    }
    else if (!cpu.getCpuStatus().isEmulationMode() && cpu.isIndex8Bit())
    {
      cpu.clearStack(new Address(0x00, Binary.lower8BitsOf(cpu.getX())));
    }
    else if (!cpu.getCpuStatus().isEmulationMode() && cpu.isIndex16Bit())
    {
      cpu.clearStack(new Address(0x00, cpu.getX()));
    }
    cpu.addToProgramAddressAndCycles(1, 2);
  }

  @Override
  public void executeOnRisingEdge(Cpu65816 cpu)
  {
  }
}

