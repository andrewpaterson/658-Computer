package name.bizna.emu65816.opcode;

import name.bizna.emu65816.AddressingMode;
import name.bizna.emu65816.Binary;
import name.bizna.emu65816.Cpu65816;

public class OpCode_TYA
    extends OpCode
{
  public OpCode_TYA(String mName, int mCode, AddressingMode mAddressingMode)
  {
    super(mName, mCode, mAddressingMode);
  }

  @Override
  public void executeOnFallingEdge(Cpu65816 cpu)
  {
    if (cpu.isMemory8Bit() && cpu.isIndex8Bit())
    {
      int value = Binary.getLowByte(cpu.getY());
      cpu.setA(Binary.setLowByte(cpu.getA(), value));
      cpu.getCpuStatus().updateSignAndZeroFlagFrom8BitValue(value);
    }
    else if (cpu.isMemory8Bit() && cpu.isIndex16Bit())
    {
      int value = Binary.getLowByte(cpu.getY());
      cpu.setA(Binary.setLowByte(cpu.getA(), value));
      cpu.getCpuStatus().updateSignAndZeroFlagFrom8BitValue(value);
    }
    else if (cpu.isMemory16Bit() && cpu.isIndex8Bit())
    {
      int value = Binary.getLowByte(cpu.getY());
      cpu.setA(value);
      cpu.getCpuStatus().updateSignAndZeroFlagFrom8BitValue(value);
    }
    else
    {
      cpu.setA(cpu.getY());
      cpu.getCpuStatus().updateSignAndZeroFlagFrom16BitValue(cpu.getA());
    }
    cpu.addToProgramAddressAndCycles(1, 2);
  }

  @Override
  public void executeOnRisingEdge(Cpu65816 cpu)
  {
  }
}

