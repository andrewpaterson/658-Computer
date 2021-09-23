package name.bizna.emu65816.opcode;

import name.bizna.emu65816.AddressingMode;
import name.bizna.emu65816.Cpu65816;

public class OpCode_CLV
    extends OpCode
{
  public OpCode_CLV(String mName, byte mCode, AddressingMode mAddressingMode)
  {
    super(mName, mCode, mAddressingMode);
  }

  @Override
  public void execute(Cpu65816 cpu)
  {
    cpu.getCpuStatus().clearOverflowFlag();
    cpu.addToProgramAddressAndCycles(1, 2);
  }
}
