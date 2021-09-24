package name.bizna.emu65816.opcode;

import name.bizna.emu65816.AddressingMode;
import name.bizna.emu65816.Cpu65816;

public class OpCode_TDC
    extends OpCode
{
  public OpCode_TDC(String mName, int mCode, AddressingMode mAddressingMode)
  {
    super(mName, mCode, mAddressingMode);
  }

  @Override
  public void execute(Cpu65816 cpu, int cycle, boolean clock)
  {
    cpu.setA(cpu.getD());
    cpu.getCpuStatus().updateSignAndZeroFlagFrom16BitValue(cpu.getA());
    cpu.addToProgramAddressAndCycles(1, 2);
  }
}

