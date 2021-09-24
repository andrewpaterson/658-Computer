package name.bizna.emu65816.opcode;

import name.bizna.emu65816.AddressingMode;
import name.bizna.emu65816.Cpu65816;

public class OpCode_SEC
    extends OpCode
{
  public OpCode_SEC(String mName, byte mCode, AddressingMode mAddressingMode)
  {
    super(mName, mCode, mAddressingMode);
  }

  @Override
  public void execute(Cpu65816 cpu)
  {
    cpu.getCpuStatus().setCarryFlag(true);
    cpu.addToProgramAddressAndCycles(1, 2);
  }
}

