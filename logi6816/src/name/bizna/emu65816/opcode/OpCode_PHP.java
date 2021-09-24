package name.bizna.emu65816.opcode;

import name.bizna.emu65816.AddressingMode;
import name.bizna.emu65816.Cpu65816;

public class OpCode_PHP
    extends OpCode
{
  public OpCode_PHP(String mName, int mCode, AddressingMode mAddressingMode)
  {
    super(mName, mCode, mAddressingMode);
  }

  @Override
  public void execute(Cpu65816 cpu, int cycle, boolean clock)
  {
    cpu.getStack().push8Bit(cpu.getCpuStatus().getRegisterValue());
    cpu.addToProgramAddressAndCycles(1, 3);
  }
}

