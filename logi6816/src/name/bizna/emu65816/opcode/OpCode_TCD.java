package name.bizna.emu65816.opcode;

import name.bizna.emu65816.AddressingMode;
import name.bizna.emu65816.Cpu65816;

public class OpCode_TCD
    extends OpCode
{
  public OpCode_TCD(String mName, byte mCode, AddressingMode mAddressingMode)
  {
    super(mName, mCode, mAddressingMode);
  }

  @Override
  public void execute(Cpu65816 cpu)
  {
    cpu.setD(cpu.getA());
    cpu.getCpuStatus().updateSignAndZeroFlagFrom16BitValue(cpu.getD());
    cpu.addToProgramAddressAndCycles(1, 2);
  }
}

