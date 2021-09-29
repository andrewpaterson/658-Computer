package name.bizna.emu65816.opcode;

import name.bizna.emu65816.AddressingMode;
import name.bizna.emu65816.Binary;
import name.bizna.emu65816.Cpu65816;

public class OpCode_TAX
    extends OpCode
{
  public OpCode_TAX(String mName, int mCode, AddressingMode mAddressingMode)
  {
    super(mName, mCode, mAddressingMode);
  }

  @Override
  public void executeOnFallingEdge(Cpu65816 cpu)
  {
    if ((cpu.isMemory8Bit() && cpu.isIndex8Bit()) ||
        (cpu.isMemory16Bit() && cpu.isIndex8Bit()))
    {
      int lower8BitsOfA = Binary.getLowByte(cpu.getA());
      cpu.setX(Binary.setLower8BitsOf16BitsValue(cpu.getX(), lower8BitsOfA));
      cpu.getCpuStatus().updateSignAndZeroFlagFrom8BitValue(lower8BitsOfA);
    }
    else
    {
      cpu.setX(cpu.getA());
      cpu.getCpuStatus().updateSignAndZeroFlagFrom16BitValue(cpu.getA());
    }
    cpu.addToProgramAddressAndCycles(1, 2);
  }

  @Override
  public void executeOnRisingEdge(Cpu65816 cpu)
  {
  }
}

