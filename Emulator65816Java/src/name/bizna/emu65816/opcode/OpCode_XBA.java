package name.bizna.emu65816.opcode;

import name.bizna.emu65816.AddressingMode;
import name.bizna.emu65816.Binary;
import name.bizna.emu65816.Cpu65816;

import static name.bizna.emu65816.Unsigned.toShort;

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
    int lowerA = Binary.lower8BitsOf(cpu.getA());
    int higherA = Binary.higher8BitsOf(cpu.getA());
    cpu.setA(toShort(higherA | (((lowerA)) << 8)));
    cpu.getCpuStatus().updateSignAndZeroFlagFrom8BitValue(higherA);
    cpu.addToProgramAddressAndCycles(1, 3);
  }
}

