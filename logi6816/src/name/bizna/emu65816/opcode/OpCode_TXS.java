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
    if (cpu.getCpuStatus().emulationFlag())
    {
      short newStackPointer = 0x100;
      newStackPointer |= Binary.lower8BitsOf(cpu.getX());
      cpu.clearStack(new Address(newStackPointer));
    }
    else if (!cpu.getCpuStatus().emulationFlag() && cpu.indexIs8BitWide())
    {
      cpu.clearStack(new Address(Binary.lower8BitsOf(cpu.getX())));
    }
    else if (!cpu.getCpuStatus().emulationFlag() && cpu.indexIs16BitWide())
    {
      cpu.clearStack(new Address(cpu.getX()));
    }
    cpu.addToProgramAddressAndCycles(1, 2);
  }

  @Override
  public void executeOnRisingEdge(Cpu65816 cpu)
  {
  }
}

