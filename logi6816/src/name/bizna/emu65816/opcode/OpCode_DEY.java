package name.bizna.emu65816.opcode;

import name.bizna.emu65816.AddressingMode;
import name.bizna.emu65816.Binary;
import name.bizna.emu65816.Cpu65816;

public class OpCode_DEY
    extends OpCode
{
  public OpCode_DEY(String mName, byte mCode, AddressingMode mAddressingMode)
  {
    super(mName, mCode, mAddressingMode);
  }

  @Override
  public void execute(Cpu65816 cpu)
  {
    if (cpu.indexIs8BitWide())
    {
      byte lowerY = Binary.lower8BitsOf(cpu.getY());
      lowerY--;
      cpu.setY(Binary.setLower8BitsOf16BitsValue(cpu.getY(), lowerY));
      cpu.getCpuStatus().updateSignAndZeroFlagFrom8BitValue(lowerY);
    }
    else
    {
      cpu.decY();
      cpu.getCpuStatus().updateSignAndZeroFlagFrom16BitValue(cpu.getY());
    }
    cpu.addToProgramAddressAndCycles(1, 2);
  }
}
