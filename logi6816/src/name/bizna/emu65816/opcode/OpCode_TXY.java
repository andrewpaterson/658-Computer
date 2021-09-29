package name.bizna.emu65816.opcode;

import name.bizna.emu65816.AddressingMode;
import name.bizna.emu65816.Binary;
import name.bizna.emu65816.Cpu65816;

public class OpCode_TXY
    extends OpCode
{
  public OpCode_TXY(String mName, int mCode, AddressingMode mAddressingMode)
  {
    super(mName, mCode, mAddressingMode);
  }

  @Override
  public void executeOnFallingEdge(Cpu65816 cpu)
  {
    if (cpu.isIndex8Bit())
    {
      int value = Binary.getLowByte(cpu.getX());
      cpu.setY(Binary.setLower8BitsOf16BitsValue(cpu.getY(), value));
      cpu.getCpuStatus().updateSignAndZeroFlagFrom8BitValue(value);
    }
    else
    {
      cpu.setY(cpu.getX());
      cpu.getCpuStatus().updateSignAndZeroFlagFrom16BitValue(cpu.getY());
    }
    cpu.addToProgramAddressAndCycles(1, 2);
  }

  @Override
  public void executeOnRisingEdge(Cpu65816 cpu)
  {
  }
}

