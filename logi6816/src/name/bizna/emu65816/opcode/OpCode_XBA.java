package name.bizna.emu65816.opcode;

import name.bizna.emu65816.AddressingMode;
import name.bizna.emu65816.Binary;
import name.bizna.emu65816.Cpu65816;

public class OpCode_XBA
    extends OpCode
{
  public OpCode_XBA(String mName, byte mCode, AddressingMode mAddressingMode)
  {
    super(mName, mCode, mAddressingMode);
  }

  @Override
  public void execute(Cpu65816 cpu)
  {
    byte lowerA = Binary.lower8BitsOf(cpu.getA());
    byte higherA = Binary.higher8BitsOf(cpu.getA());
    cpu.setA((short) (higherA | (((short) (lowerA)) << 8)));
    cpu.getCpuStatus().updateSignAndZeroFlagFrom8BitValue(higherA);
    cpu.addToProgramAddressAndCycles(1, 3);
  }
}

